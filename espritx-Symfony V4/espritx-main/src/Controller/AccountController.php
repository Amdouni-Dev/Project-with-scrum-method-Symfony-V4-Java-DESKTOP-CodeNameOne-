<?php

namespace App\Controller;

use App\Entity\User;
use App\Form\ChangePasswordFormType;
use App\Form\UserType;
use App\Repository\EventRepository;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Endroid\QrCode\Builder\BuilderInterface;
use Endroid\QrCodeBundle\Response\QrCodeResponse;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\ParamConverter;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\Security\Core\Validator\Constraints\UserPassword;
use Symfony\Component\Validator\Constraints\NotBlank;

/**
 * @Route ("/account")
 */
class AccountController extends AbstractController
{
  /**
   * @Route("/settings", name="account_settings")
   */
  public function edit_account(Request $request, EntityManagerInterface $entityManager): Response
  {
    $user = $this->getUser();
    $form = $this->createForm(UserType::class, $user);
    $form->remove('email');
    $form->remove('class');
    $form->remove('plainPassword');
    $form->remove('groups');
    $form->remove('individualPermissions');
    $form->remove('userStatus');
    $form->handleRequest($request);
    if ($form->isSubmitted() && $form->isValid()) {
      $entityManager->flush();

      return $this->redirectToRoute('account_settings', [], Response::HTTP_SEE_OTHER);
    }
    return $this->render('views/content/apps/user/account/page-account-settings-account.html.twig', [
      'form' => $form->createView(),
      'user' => $user,
    ]);
  }

  /**
   * @Route ("/password", name="account_password")
   */
  public function edit_password(Request $request, EntityManagerInterface $entityManager, UserPasswordEncoderInterface $userPasswordEncoder): Response
  {
    $user = $this->getUser();
    $form = $this->createForm(ChangePasswordFormType::class);
    $form->add('currentPassowrd', PasswordType::class, [
      'constraints' => [
        new UserPassword([
          'message' => 'Wrong Password',
        ]),
      ],
    ]);
    $form->handleRequest($request);
    if ($form->isSubmitted() && $form->isValid()) {
      $user->setPlainPassword($form->get('plainPassword')->getData());
      $entityManager->flush();
      return $this->redirectToRoute('account_settings', [], Response::HTTP_SEE_OTHER);
    }
    return $this->render('views/content/apps/user/account/page-account-settings-security.html.twig', [
      'resetForm' => $form->createView(),
    ]);
  }

  /**
   * @Route("/me", name="show_my_profile")
   */
  public function show_user_account(Request $request,EventRepository $eventRepository): Response
  {
    $events = $eventRepository->findAll();

    $rdvs = [];
    foreach ($events as $event) {
        $rdvs[] = [
            'id' => $event->getId(),
            'start' => $event->getStart()->format('Y-m-d H:i:s'),
            'end' => $event->getEnd()->format('Y-m-d H:i:s'),
            'title' => $event->getTitle(),
            'description' => $event->getDescription(),
            'allDay' => $event->getAllDay(),
        ];
    }

    $data = json_encode($rdvs);
    return $this->render('views/content/pages/page-profile.html.twig', [
      "user" => $this->getUser(),
      'event' => $data,
    ]);
  }
  /**
   * @Route("/profile/{id}", name="show_user_profile")
   * @ParamConverter("user", class="App\Entity\User")
   */
  public function show_account(Request $request, User $user): Response
  {
    return $this->render('views/content/pages/page-profile.html.twig', [
      "user" => $user
    ]);
  }

  /**
   * @Route("/qc-code/{id}", name="make_quick_code")
   */
  public function generate_qc_code(Request $request, BuilderInterface $customQrCodeBuilder): QrCodeResponse
  {
    $result = $customQrCodeBuilder
      ->size(185)
      ->data($this->generateUrl("show_user_profile",
        ["id" => $request->get("id")], UrlGeneratorInterface::ABSOLUTE_URL
      ))
      ->margin(15)
      ->build();
    return new QrCodeResponse($result);
  }
}
