<?php

namespace App\Service;

use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Component\Mailer\MailerInterface;
class Mail{

    private $mailer;
    public function __construct(MailerInterface $mailer)
    {
        $this->mailer = $mailer;
    }
 
    public function sendNewEventEmail($toMail , array $var)
    {
        $email = (new TemplatedEmail())
            ->from("postmaster@espritx.xyz")
            ->to(...$toMail)
            ->subject('You have new event')
            ->htmlTemplate('email/event.html.twig')
            ->context(
                $var
            );
        $this->mailer->send($email);
    }

    public function sendNewCallEmail($toMail , array $var)
    {
        $email = (new TemplatedEmail())
            ->from("postmaster@espritx.xyz")
            ->to(...$toMail)
            ->subject('You have new call')
            ->htmlTemplate('email/call.html.twig')
            ->context(
                $var
            );
        $this->mailer->send($email);
    }

    public function sendDeactivatedEventEmail($toMail, array $var){
        $email = (new TemplatedEmail())
            ->from("postmaster@espritx.xyz")
            ->to(...$toMail)
            ->subject('An event has been deactivated')
            ->htmlTemplate('email/deactivatedevent.html.twig')
            ->context(
                $var
            );
        $this->mailer->send($email);
    }

    public function sendDeactivatedCallEmail($toMail, array $var){
        $email = (new TemplatedEmail())
            ->from("postmaster@espritx.xyz")
            ->to(...$toMail)
            ->subject('A call has been deactivated')
            ->htmlTemplate('email/deactivatedcall.html.twig')
            ->context(
                $var
            );
        $this->mailer->send($email);
    }
}