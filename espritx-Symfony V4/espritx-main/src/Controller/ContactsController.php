<?php

namespace App\Controller;

use App\Entity\User;
use App\Service\NotificationService;
use Doctrine\ORM\EntityManagerInterface;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\ParamConverter;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;

/**
 * @Route("/contacts")
 */
class ContactsController extends AbstractController
{
  /**
   * @Route("/add/{id}", name="add_contact")
   * @ParamConverter("user", class="App\Entity\User")
   */
  public function add_contact(Request $request, User $user, NotificationService $notificationService, EntityManagerInterface $em)
  {

    /** @var User $current_user */
    $current_user = $this->getUser();
    $current_user->addContact($user);
    $notificationService->notifyUser(
      $user,
      $this->getUser()->getFirstName() . " " . $this->getUser()->getLastName() . " has added you as a contact!",
      "How about you go say hi?",
      $this->generateUrl("show_user_profile", ["id" => $this->getUser()->getId()]
      )
    );
    $em->flush();
    if ($request->query->has("redirect_uri")) {
      return $this->redirect($request->query->get("redirect_uri"));
    } else {
      return $this->generateUrl("show_my_profile");
    }
  }

  /**
   * @Route("/remove/{id}", name="remove_contact")
   * @ParamConverter("user", class="App\Entity\User")
   */
  public function remove_contact(Request $request, User $user, EntityManagerInterface $em)
  {
    $this->getUser()->removeContact($user);
    $em->flush();
    if ($request->query->has("redirect_uri")) {
      return $this->redirect($request->query->get("redirect_uri"));
    } else {
      return $this->generateUrl("show_my_profile");
    }
  }
}