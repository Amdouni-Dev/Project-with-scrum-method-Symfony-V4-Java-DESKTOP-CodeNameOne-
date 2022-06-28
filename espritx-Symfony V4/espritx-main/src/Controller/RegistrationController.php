<?php

namespace App\Controller;

use App\Entity\User;
use App\Form\RegistrationFormType;
use App\Repository\GroupRepository;
use App\Repository\UserRepository;
use App\Security\LoginFormAuthenticator;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Address;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Guard\GuardAuthenticatorHandler;
use SymfonyCasts\Bundle\VerifyEmail\Exception\VerifyEmailExceptionInterface;
use SymfonyCasts\Bundle\VerifyEmail\VerifyEmailHelperInterface;

/**
 * @Route("/register")
 */
class RegistrationController extends AbstractController
{

  public function __construct(
    private VerifyEmailHelperInterface $verifyEmailHelper,
    private MailerInterface            $mailer,
    private EntityManagerInterface     $entityManager
  )
  {
  }

  /**
   * @Route("/", name="app_register")
   */
  public function register(Request                   $request,
                           GuardAuthenticatorHandler $guardHandler,
                           LoginFormAuthenticator    $authenticator,
                            GroupRepository $groupRepository
  ): Response
  {
    $user = new User();
    $form = $this->createForm(RegistrationFormType::class, $user);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
      $user->addGroup($groupRepository->getDefaultGroup());
      $this->entityManager->persist($user);
      $this->entityManager->flush();

      $signatureComponents = $this->verifyEmailHelper->generateSignature(
        'registration_confirmation_route',
        $user->getId(),
        $user->getEmail(),
        ['id' => $user->getId()]
      );

      $this->mailer->send((new TemplatedEmail())
        ->from(new Address('postmaster@espritx.xyz', 'ESPRITx'))
        ->to($user->getEmail())
        ->subject('ESPRITx Account Verification')
        ->htmlTemplate('emails/mail-verify-email.html.twig')
        ->context([
          'signedUrl' => $signatureComponents->getSignedUrl(),
          'expiresAt' => $signatureComponents->getExpiresAtIntervalInstance(),
          'user' => $user
        ]));

      return $guardHandler->authenticateUserAndHandleSuccess(
        $user,
        $request,
        $authenticator,
        'main'
      );
    }

    return $this->render('views/content/authentication/auth-register-cover.html.twig', [
      'form' => $form->createView(),
    ]);
  }

  /**
   * @Route("/verify", name="registration_confirmation_route")
   */
  public function verifyUserEmail(Request $request, UserRepository $userRepository): Response
  {
    $id = $request->get('id');

    if (null === $id) {
      return $this->redirectToRoute('app_register');
    }
    $user = $userRepository->find($id);
    if (null === $user) {
      return $this->redirectToRoute('app_register');
    }

    // validate email confirmation link, sets User::isVerified=true and persists
    try {
      $this->verifyEmailHelper->validateEmailConfirmation($request->getUri(), $user->getId(), $user->getEmail());
      $user->setIsVerified(true);
      $this->entityManager->persist($user); // is this already tracked?
      $this->entityManager->flush();
    } catch (VerifyEmailExceptionInterface $exception) {
      $this->addFlash('verify_email_error', $exception->getReason());
      return $this->redirectToRoute('app_register');
    }

    // @TODO Change the redirect on success and handle or remove the flash message in your templates
    $this->addFlash('success', 'Your email address has been verified.');

    return $this->redirectToRoute('show_my_profile');
  }
}
