<?php

namespace App\Controller;

use App\Entity\Channel;
use App\Entity\User;
use App\Service\NotificationService;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Address;
use Symfony\Component\Routing\Annotation\Route;

class JitsiController extends AbstractController
{
    public function __construct(private MailerInterface $mailer)
    {
    }

    /**
     * @Route("/jitsi", name="app_jitsi")
     */
    public function index(NotificationService $notify): Response
    {
        $notify->notifyUser($this->getUser());
        return $this->render('jitsi/index.html.twig', [
            'controller_name' => 'JitsiController',
        ]);
    }

    /**
     * @Route("/notifvideo/{id}",name="notif_video")"
     */
    public function notify(NotificationService $notify, int $id)
    {
        $user = $this->getDoctrine()->getRepository(User::class)->find($id);
        $chat_id = $this->getDoctrine()->getRepository(Channel::class)->findByIds($this->getUser(), $user);
        $url = $this->generateUrl("chat_show", ["id" => $chat_id]);

        $notify->notifyUser($user, "Video Chat", "Voulez vous lancez l'appel video ?", "https://139-162-157-203.ip.linodeusercontent.com/PiDev3A", true);
        $this->mailer->send((new TemplatedEmail())
            ->from(new Address('postmaster@espritx.xyz', 'ESPRITx'))
            ->to($user->getEmail())
            ->subject('ESPRITx Appel video')
            ->htmlTemplate('email/videocall.html.twig')
            ->context([
                'url' => $url,
                'user' => $user
            ]));

        return $this->redirect("https://139-162-157-203.ip.linodeusercontent.com/PiDev3A");
    }
}
