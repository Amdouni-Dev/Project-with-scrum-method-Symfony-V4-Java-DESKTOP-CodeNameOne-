<?php

namespace App\EventListener;

use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Security\Http\Event\InteractiveLoginEvent;

class LoginListener
{
  private EntityManagerInterface $em;

  public function __construct(EntityManagerInterface $em)
  {
    $this->em = $em;
  }

  public function onSecurityInteractiveLogin(InteractiveLoginEvent $event)
  {
    $user = $event->getAuthenticationToken()->getUser();

    // Update your field here.
    $user->setLastLogin(new \DateTime());

    // Persist the data to database.
    $this->em->persist($user);
    $this->em->flush();
  }
}