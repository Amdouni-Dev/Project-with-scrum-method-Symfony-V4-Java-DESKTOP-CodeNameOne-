<?php

namespace App\Controller;

use App\Entity\User;
use App\Enum\GroupType;
use App\Repository\DeviceAuthorizationRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Http\Authentication\AuthenticationUtils;

class SecurityController extends AbstractController
{
  /**
   * @Route("/login", name="login")
   */
  public function login(AuthenticationUtils $authenticationUtils): Response
  {
    /** @var User $user */
    $user = $this->getUser();
    if ($user) {
      foreach ($user->getGroups() as $group) {
        if ($group->getGroupType() === GroupType::STUDENT() || $group->getGroupType() === GroupType::TEACHERS()) {
          return $this->redirectToRoute('show_my_profile');
        }
      }
      return $this->redirectToRoute('app_admin_home');
    }
    $error = $authenticationUtils->getLastAuthenticationError();
    $lastUsername = $authenticationUtils->getLastUsername();
    return $this->render('views/content/authentication/auth-login-cover.html.twig',
      [
        'last_username' => $lastUsername,
        'error' => $error
      ]);
  }

  /**
   * @Route("/logout", name="app_logout")
   */
  public function logout(): void
  {
    throw new \LogicException('Method should have been intercepted by the firewall.');
  }
}
