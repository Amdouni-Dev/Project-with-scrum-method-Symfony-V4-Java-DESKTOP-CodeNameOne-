<?php

namespace App\Controller;

use Mgilet\NotificationBundle\Entity\Notification;
use Mgilet\NotificationBundle\Manager\NotificationManager;
use Mgilet\NotificationBundle\NotifiableInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\JsonResponse;


/**
 * @Route("/notifications")
 */
class NotificationController extends AbstractController
{
  /**
   * List of all notifications
   *
   * @Route("/{notifiable}", name="notification_list", methods={"GET"})
   * @param NotifiableInterface $notifiable
   *
   * @return \Symfony\Component\HttpFoundation\Response
   */
  public function listAction($notifiable)
  {
    $notifiableRepo = $this->get('doctrine.orm.entity_manager')->getRepository('MgiletNotificationBundle:NotifiableNotification');
    $notificationList = $notifiableRepo->findAllForNotifiableId($notifiable);
    return $this->render('@MgiletNotification/notifications.html.twig', array(
      'notificationList' => $notificationList,
      'notifiableNotifications' => $notificationList // deprecated: alias for backward compatibility only
    ));
  }

  /**
   * Set a Notification as seen
   *
   * @Route("/{notifiable}/mark_as_seen/{notification}", name="notification_mark_as_seen", methods={"GET"})
   * @param int $notifiable
   * @param Notification $notification
   *
   * @return JsonResponse
   * @throws \RuntimeException
   * @throws \InvalidArgumentException
   * @throws \Doctrine\ORM\OptimisticLockException
   * @throws \Doctrine\ORM\NonUniqueResultException
   * @throws \Doctrine\ORM\EntityNotFoundException
   * @throws \LogicException
   */
  public function markAsSeenAction($notifiable, $notification, NotificationManager $manager)
  {
    $manager->markAsSeen(
      $manager->getNotifiableInterface($manager->getNotifiableEntityById($notifiable)),
      $manager->getNotification($notification),
      true
    );

    return new JsonResponse(true);
  }

  /**
   * Set a Notification as unseen
   *
   * @Route("/{notifiable}/mark_as_unseen/{notification}", name="notification_mark_as_unseen", methods={"GET"})
   * @param $notifiable
   * @param $notification
   *
   * @return JsonResponse
   * @throws \RuntimeException
   * @throws \InvalidArgumentException
   * @throws \Doctrine\ORM\OptimisticLockException
   * @throws \Doctrine\ORM\NonUniqueResultException
   * @throws \Doctrine\ORM\EntityNotFoundException
   * @throws \LogicException
   */
  public function markAsUnSeenAction($notifiable, $notification, NotificationManager $manager)
  {
    $manager->markAsUnseen(
      $manager->getNotifiableInterface($manager->getNotifiableEntityById($notifiable)),
      $manager->getNotification($notification),
      true
    );

    return new JsonResponse(true);
  }

  /**
   * Set all Notifications for a User as seen
   *
   * @Route("/{notifiable}/markAllAsSeen", name="notification_mark_all_as_seen", methods={"GET"})
   * @param $notifiable
   *
   * @return JsonResponse
   * @throws \RuntimeException
   * @throws \InvalidArgumentException
   * @throws \Doctrine\ORM\OptimisticLockException
   */
  public function markAllAsSeenAction($notifiable, NotificationManager $manager)
  {
    $manager->markAllAsSeen(
      $manager->getNotifiableInterface($manager->getNotifiableEntityById($notifiable)),
      true
    );

    return new JsonResponse(true);
  }
}
