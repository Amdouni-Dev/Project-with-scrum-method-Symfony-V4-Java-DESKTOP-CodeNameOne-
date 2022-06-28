<?php

namespace App\Entity\Listener;

use App\Entity\User;
use Doctrine\ORM\Event\LifecycleEventArgs;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;

class UserListener
{
  function __construct(private UserPasswordEncoderInterface $encoderInterface)
  {
  }

  /** @ORM\PrePersist */
  public function prePersist(User $user, LifecycleEventArgs $event): void
  {
    if (!empty($user->getPlainPassword())) {
      $user->setPassword($this->encoderInterface->encodePassword($user, $user->getPlainPassword()));
    }
  }

  /** @ORM\PreUpdate */
  public function preUpdate(User $user, LifecycleEventArgs $event): void
  {
    if (!empty($user->getPlainPassword())) {
      $user->setPassword($this->encoderInterface->encodePassword($user, $user->getPlainPassword()));
    }
  }
}