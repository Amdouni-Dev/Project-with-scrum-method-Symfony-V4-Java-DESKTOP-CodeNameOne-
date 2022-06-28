<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Mgilet\NotificationBundle\Entity\NotificationInterface;
use Mgilet\NotificationBundle\Model\Notification as NotificationBase;
/**
 * Class Notification
 * @ORM\Entity
 */
class Notification extends NotificationBase implements NotificationInterface
{
}