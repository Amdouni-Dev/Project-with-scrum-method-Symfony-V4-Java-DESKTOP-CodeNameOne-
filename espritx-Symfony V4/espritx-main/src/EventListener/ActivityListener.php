<?php

namespace App\EventListener;

use App\Entity\User;
use Doctrine\ORM\EntityManager;
use Doctrine\ORM\EntityManagerInterface;
use FOS\UserBundle\Model\UserManagerInterface;
use Symfony\Component\HttpKernel\Event\ControllerEvent;
use Symfony\Component\Security\Core\Security;

class ActivityListener
{
  protected Security $security;
  protected EntityManager $entityManager;

  public function __construct(Security $security, EntityManagerInterface $entityManager)
  {
    $this->entityManager = $entityManager;
    $this->security = $security;
  }

  public function onCoreController(ControllerEvent $event)
  {
    /**
     * The master request is the one, that is triggered by the browser and the sub requests are requests from within the application.
     * For example a template can render another action. We only want browser requests.
     */
    if (!$event->isMasterRequest()) {
      return;
    }
    if ($this->security->getToken()) {
      $user = $this->security->getToken()->getUser();

      if (($user instanceof User) && !($user->isActiveNow())) {
        $user->setLastActivityAt(new \DateTime());
        $this->entityManager->flush($user);
      }
    }
  }
}