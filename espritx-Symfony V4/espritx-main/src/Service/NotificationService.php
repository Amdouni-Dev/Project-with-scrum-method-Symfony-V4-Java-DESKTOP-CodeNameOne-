<?php

namespace App\Service;

use App\Entity\Group;
use App\Entity\User;
use Mgilet\NotificationBundle\Manager\NotificationManager;

class NotificationService
{
  public function __construct(private NotificationManager $manager)
  {
  }

  /**
   * @throws \Doctrine\ORM\OptimisticLockException
   */
  public function notifyUser(User $user, string $subject, string $message = null, string $link = null, $flush = true)
  {
    $notif = $this->manager->createNotification($subject, $message, $link);
    $this->manager->addNotification([$user], $notif, $flush);
  }

  public function notifyGroup(Group $group, string $subject, string $message = null, string $link = null, $flush = true)
  {
    $users = [];
    foreach ($group->getMembers() as $member) {
      $users[] = $member;
    }
    $users = array_unique($users);
    foreach ($users as $user){
      $this->notifyUser($user, $subject, $message, $link, $flush);
    }
  }
}