<?php

namespace App\Security;

use App\Entity\User;

// your user entity
use App\Enum\UserStatus;
use App\Repository\GroupRepository;
use Doctrine\ORM\EntityManagerInterface;
use KnpU\OAuth2ClientBundle\Client\OAuth2ClientInterface;
use KnpU\OAuth2ClientBundle\Client\Provider\GoogleClient;
use KnpU\OAuth2ClientBundle\Security\Authenticator\SocialAuthenticator;
use KnpU\OAuth2ClientBundle\Client\Provider\FacebookClient;
use KnpU\OAuth2ClientBundle\Client\ClientRegistry;
use League\OAuth2\Client\Provider\GoogleUser;
use Lexik\Bundle\JWTAuthenticationBundle\Events;
use Lexik\Bundle\JWTAuthenticationBundle\Response\JWTAuthenticationSuccessResponse;
use Lexik\Bundle\JWTAuthenticationBundle\Services\JWTTokenManagerInterface;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Address;
use Symfony\Component\Routing\RouterInterface;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\Security\Core\Event\AuthenticationSuccessEvent;
use Symfony\Component\Security\Core\Exception\AuthenticationException;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\Security\Core\User\UserProviderInterface;

class GoogleAuthenticator extends SocialAuthenticator
{
  public function __construct(private ClientRegistry           $clientRegistry,
                              private EntityManagerInterface   $em,
                              private RouterInterface          $router,
                              private MailerInterface          $mailer,
                              private JWTTokenManagerInterface $jwtManager,
                              private Security                 $security,
                              private GroupRepository          $repository)
  {
  }

  public function supports(Request $request)
  {
    return $request->attributes->get('_route') === 'connect_google_check';
  }

  public function getCredentials(Request $request)
  {
    return $this->fetchAccessToken($this->getGoogleClient());
  }

  public function getUser($credentials, UserProviderInterface $userProvider)
  {
    /** @var GoogleUser $googleUser */
    $googleUser = $this->getGoogleClient()->fetchUserFromToken($credentials);
    $email = $googleUser->getEmail();
    $existingUser = $this->em->getRepository(User::class)->findOneBy(['googleId' => $googleUser->getId()]);
    if ($existingUser) {
      return $existingUser;
    }

    $user = $this->em->getRepository(User::class)->findOneBy(['email' => $email]);

    if ($googleUser == null) {
      throw new AuthenticationException("Could not fetch user from Google Hosted Domain");
    }

    if ($googleUser->getHostedDomain() != "esprit.tn") {
      throw new AuthenticationException("Only @esprit.tn emails are allowed to use this application");
    }

    if ($user == null) {
      $user = new User();
      $user->setFirstName($googleUser->getFirstName());
      $user->setLastName($googleUser->getLastName());
      $user->setEmail($googleUser->getEmail());
      $user->setIsVerified(true);
      $user->setUserStatus(UserStatus::ACTIVE());
      $password = bin2hex(random_bytes(8));
      $user->setPlainPassword($password);
      $default_group = $this->repository->getDefaultGroup();
      $default_group->addMember($user);
      $this->em->persist($default_group);
      $email = (new TemplatedEmail())
        ->from(new Address('postmaster@espritx.xyz', 'ESPRITx'))
        ->to($user->getEmail())
        ->subject('Welcome to ESPRITx!')
        ->htmlTemplate('emails/mail-welcome.html.twig')
        ->context([
          'user' => $user,
          'password' => $password
        ]);
      $this->mailer->send($email);
    }
    $user->setGoogleId($googleUser->getId());
    $this->em->persist($user);
    $this->em->flush();
    return $user;
  }

  private function getGoogleClient(): OAuth2ClientInterface
  {
    return $this->clientRegistry->getClient('google');
  }

  public function onAuthenticationSuccess(Request $request, TokenInterface $token, $providerKey)
  {
    return new RedirectResponse($this->router->generate('app_home'));
  }

  public function onAuthenticationFailure(Request $request, AuthenticationException $exception)
  {
    $message = strtr($exception->getMessageKey(), $exception->getMessageData());

    return new Response($message, Response::HTTP_FORBIDDEN);
  }

  /**
   * Called when authentication is needed, but it's not sent.
   * This redirects to the 'login'.
   */
  public function start(Request $request, AuthenticationException $authException = null)
  {
    return new RedirectResponse(
      '/connect/', // might be the site, where users choose their oauth provider
      Response::HTTP_TEMPORARY_REDIRECT
    );
  }

  // ...
}