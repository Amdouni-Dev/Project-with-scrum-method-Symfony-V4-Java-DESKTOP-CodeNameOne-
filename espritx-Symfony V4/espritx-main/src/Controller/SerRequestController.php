<?php

namespace App\Controller;

use App\Entity\ServiceRequest;
use App\Enum\AccessType;
use App\Form\SerRequestType;
use App\Repository\ServiceRepository;
use App\Repository\ServiceRequestRepository;
use App\Service\NotificationService;
use BotMan\BotMan\Drivers\DriverManager;
use BotMan\Drivers\Web\WebDriver;
use DateTime;
use DateTimeImmutable;
use Doctrine\ORM\EntityManagerInterface;
use Dompdf\Dompdf;
use Dompdf\Options;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Security;
use BotMan\BotMan\BotMan;
use BotMan\BotMan\BotManFactory;

/**
 * @Route ("/request")
 */
class SerRequestController extends AbstractController
{
    /**
     * @Route ("/user/requests", name="SerivceReq_User")
     * @return Response
     */
    public function affUserRequests(): Response
    {
        $user = $this->getUser();
        $SerReq = $user->getServiceRequests();
        $pageConfigs = [
            'mainLayoutType' => 'horizontal',
            'pageHeader' => false
        ];
        return $this->render('views/content/apps/administrativeService/Requests/AfficheUser.html.twig', [
            'pageConfigs' => $pageConfigs,
            'SerReq' => $SerReq,
            'user' => $user,
        ]);
    }

    /**
     * @Route("/showAll", name="ser_requests")
     */
    public function affRequests(EntityManagerInterface $em, PaginatorInterface $paginator, Request $request): Response
    {
      //$this->denyAccessUnlessGranted([AccessType::READ], ServiceRequest::class);

      $dql = <<<DQL
    select sr from App\Entity\ServiceRequest sr 
    DQL;
        $query = $em->createQuery($dql);

        $pagination = $paginator->paginate(
            $query,
            $request->query->getInt('page', 1),
            10);
        return $this->render('views/content/apps/administrativeService/Requests/AfficheAll.html.twig', [
            'breadcrumbs' => [
                ["name" => "Management"],
                ["name" => "All Services", "link" => ""]
            ],
            'pagination' => $pagination,
        ]);
    }

    /**
     * @Route("/add", name="SerivceReq_Add")
     */
    public function addServiceRequest(Request $request, EntityManagerInterface $em, NotificationService $Notif): Response
    {
        $serreq = new ServiceRequest();
        $f = $this->createForm(SerRequestType::class, $serreq);
        $f->handleRequest($request);
        if ($f->isSubmitted() && $f->isValid()) {
            $serreq->setRequester($this->getUser());
            $em->persist($serreq);
            $em->flush();
            $Notif->notifyGroup($serreq->getType()->getResponsible(),"New Request","You have received a new service request",null,true);
            return $this->redirectToRoute('SerivceReq_User', [], Response::HTTP_SEE_OTHER);
        }

        $pageConfigs = [
            'mainLayoutType' => 'horizontal',
            'pageHeader' => false
        ];
        return $this->render('views/content/apps/administrativeService/Requests/request-service-form.html.twig', [
            'form' => $f->createView(),
            'pageConfigs' => $pageConfigs,
        ]);
    }

    /**
     * @param EntityManagerInterface $em
     * @return Response
     * @Route ("/{id}/edit", name="SerivceReq_Edit")
     */
    public function ModifServiceRequest(EntityManagerInterface $em, Request $request, ServiceRequest $serreq,NotificationService $Notif): Response
    {
        // $this->denyAccessUnlessGranted([AccessType::EDIT], $serreq);
        $form = $this->createForm(SerRequestType::class, $serreq);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $Notif->notifyGroup($serreq->getType()->getResponsible(),"Edited Request","A service request has new updates",null,true);
            $em->flush();
            return $this->redirectToRoute('SerivceReq_User', [], Response::HTTP_SEE_OTHER);
        }
        return $this->render('views/content/apps/administrativeService/Requests/request-service-form.html.twig', [
            'serreq' => $serreq,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @param EntityManagerInterface $em
     * @return Response
     * @Route ("/{id}/delete", name="SerivceReq_Del")
     */
    public function SuppServiceRequest(EntityManagerInterface $em, ServiceRequest $serreq): Response
    {
        $em->remove($serreq);
        $em->flush();
        return $this->redirectToRoute('SerivceReq_User', [], Response::HTTP_SEE_OTHER);
    }

    /**
     * @Route ("/{id}/print", name="SerivceReq_Print")
     */
    public function PrintServiceRequest(ServiceRequest $serreq)
    {
        $pdfOptions = new Options();
        $pdfOptions->set('defaultFont', 'Arial');
        $dompdf = new Dompdf($pdfOptions);
        $html = $this->renderView('views/content/apps/administrativeService/Requests/printable.html.twig');
        $dompdf->loadHtml($html);
        $dompdf->setPaper('A4', 'landscape');
        $dompdf->render();
        $dompdf->stream("mypdf2.pdf", [
            "Attachment" => true
        ]);
        return new Response();
    }

    /**
     * @Route ("/{id}/Respond", name="SerivceReq_Respond")
     */
    public function RespondServiceRequest(EntityManagerInterface $em, Request $request, ServiceRequest $serreq,NotificationService $Notif)
    {
        $form = $this->createForm(SerRequestType::class, $serreq);
        $form->add('RequestResponse',TextareaType::class);
        $form->add('Status',ChoiceType::class, [
           'choices'=>[
               'Hold' => 'processing',
               'Deny' => 'denied',
               'Done' => 'complete',
           ]
        ]);
        $form->remove('Title');
        $form->remove('PictureFile');
        $form->remove('AttachementsFile');
        $form->remove('Type');
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $Notif->notifyUser($serreq->getRequester(),"Request Response","Your service request has a new response",null,true);
            if ($serreq->getStatus() != "unseen"){
                $serreq->setRespondedAt(new DateTimeImmutable());
            }
            $em->flush();
            return $this->redirectToRoute('ser_requests', [], Response::HTTP_SEE_OTHER);
        }
        return $this->render('views/content/apps/administrativeService/Requests/request-service-answer.html.twig', [
            'serreq' => $serreq,
            'form' => $form->createView(),
            'role'=>"EDIT",
        ]);
    }

    /**
     * @Route ("/{id}/Show", name="SerivceReq_Show")
     */

    public function ShowServiceRequest(EntityManagerInterface $em, Request $request, ServiceRequest $serreq)
    {
        $form = $this->createForm(SerRequestType::class, $serreq);
        $form->add('RequestResponse',TextareaType::class);
        $form->remove('Title');
        $form->remove('PictureFile');
        $form->remove('AttachementsFile');
        $form->remove('Type');
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            if ($serreq->getStatus() != "unseen"){
                $serreq->setRespondedAt(new DateTimeImmutable());
            }
            $em->flush();
            return $this->redirectToRoute('ser_requests', [], Response::HTTP_SEE_OTHER);
        }
        return $this->render('views/content/apps/administrativeService/Requests/request-service-answer.html.twig', [
            'breadcrumbs' => [
                ["name" => $serreq->getRequester()],
                ["name" => $serreq->getType(), "link" => ""]
            ],
            'serreq' => $serreq,
            'form' => $form->createView(),
            'role'=>"VIEW",
        ]);
    }
}
