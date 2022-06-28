<?php

namespace App\Controller;

use App\Entity\User;
use App\Form\ChangePasswordFormType;
use App\Form\ResetPasswordRequestFormType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Address;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use SymfonyCasts\Bundle\ResetPassword\Controller\ResetPasswordControllerTrait;
use SymfonyCasts\Bundle\ResetPassword\Exception\ResetPasswordExceptionInterface;
use SymfonyCasts\Bundle\ResetPassword\ResetPasswordHelperInterface;

/**
 * @Route("/resetpassword")
 */
class ResetPasswordController extends AbstractController
{
  use ResetPasswordControllerTrait;

  private $resetPasswordHelper;
  private $entityManager;

  public function __construct(ResetPasswordHelperInterface $resetPasswordHelper, EntityManagerInterface $entityManager)
  {
    $this->resetPasswordHelper = $resetPasswordHelper;
    $this->entityManager = $entityManager;
  }

  /**
   * @Route("/", name="app_forgot_password_request", methods={"GET", "POST"})
   */
  public function request(Request $request, MailerInterface $mailer): Response
  {
    $form = $this->createForm(ResetPasswordRequestFormType::class);
    $form->handleRequest($request);
    if ($form->isSubmitted() && $form->isValid()) {
      return $this->processSendingPasswordResetEmail(
        $form->get('email')->getData(), $mailer, $request
      );
    }
    return $this->render('views/content/authentication/auth-forgot-password-cover.html.twig', [
      'requestForm' => $form->createView(),
    ]);
  }

  /**
   * @Route("/check-email", name="app_check_email")
   */
  public function checkEmail(): Response
  {
    if (!$token = $this->getTokenFromSession()) {
      $token = $this->resetPasswordHelper->generateFakeResetToken();
    }
    return $this->render('views/content/authentication/auth-check-email-cover.html.twig', [
      'resetToken' => $token,
    ]);
  }

  /**
   * @Route("/reset/{token}", name="app_reset_password")
   */
  public function reset(Request $request, UserPasswordEncoderInterface $userPasswordEncoder, string $token = null): Response
  {
    if ($token) {
      $this->storeTokenInSession($token);
      return $this->redirectToRoute('app_reset_password');
    }

    $token = $this->getTokenFromSession();
    if (null === $token) {
      throw $this->createNotFoundException('No reset password token found in the URL or in the session.');
    }

    try {
      $user = $this->resetPasswordHelper->validateTokenAndFetchUser($token);
    } catch (ResetPasswordExceptionInterface $e) {
      $this->addFlash('reset_password_error', 'There was a problem validating your reset request' . $e->getReason());
      return $this->redirectToRoute('app_forgot_password_request');
    }

    $form = $this->createForm(ChangePasswordFormType::class);
    $form->handleRequest($request);
    if ($form->isSubmitted() && $form->isValid()) {
      $this->resetPasswordHelper->removeResetRequest($token);
      $encodedPassword = $userPasswordEncoder->encodePassword(
        $user,
        $form->get('plainPassword')->getData()
      );

      $user->setPassword($encodedPassword);
      $this->entityManager->flush();
      $this->cleanSessionAfterReset();
      return $this->redirectToRoute('app_home');
    }
    return $this->render('views/content/authentication/auth-reset-password-cover.html.twig', [
      'resetForm' => $form->createView(),
    ]);
  }

  private function processSendingPasswordResetEmail(string $emailFormData, MailerInterface $mailer, Request $request): RedirectResponse
  {
    $user = $this->entityManager->getRepository(User::class)->findOneBy([
      'email' => $emailFormData,
    ]);

    if (!$user) {
      return $this->redirectToRoute('app_check_email'); // we don't want anyone enumerating our users. Just redirect.
    }

    try {
      $resetToken = $this->resetPasswordHelper->generateResetToken($user);
    } catch (ResetPasswordExceptionInterface $e) {
      // for security reasons; this should be commented. But validation...
      $this->addFlash("reset_password_error", "Could not reset your password: " . $e->getReason());
      return $this->redirectToRoute('app_check_email');
    }

    $email = (new TemplatedEmail())
      ->from(new Address('postmaster@espritx.xyz', 'ESPRITx'))
      ->to($user->getEmail())
      ->subject('Your password reset request')
      ->htmlTemplate('emails/mail-reset-password.html.twig')
      ->context([
        'resetToken' => $resetToken,
        'resetURL' => $this->generateUrl("app_reset_password", ["token" => $resetToken->getToken()]),
        'user' => $user,
        'ip' => $request->getClientIp()
      ]);
    $mailer->send($email);
    $this->setTokenObjectInSession($resetToken);
    return $this->redirectToRoute('app_check_email');
  }
}
