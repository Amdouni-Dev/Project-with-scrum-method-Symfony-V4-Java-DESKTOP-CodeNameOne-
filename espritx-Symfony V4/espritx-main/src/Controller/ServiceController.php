<?php

namespace App\Controller;

use App\Entity\Fields;
use App\Entity\Service;
use App\Repository\ServiceRepository;
use App\Form\ServiceType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;


/**
 * @Route ("/service")
 */
class ServiceController extends AbstractController
{
    /**
     * @Route("/", name="service")
     */
    public function Services(ServiceRepository $Repo): Response
    {

        return $this->render('views/content/apps/administrativeService/serviceAffiche.html.twig', [
            'breadcrumbs' => [
                ["name" => "Management"],
            ],
            'Services' => $Repo->findAll(),
        ]);
    }

    /**
     * @Route("/add", name="Serivce_Add")
     */
    public function addService(Request $request, EntityManagerInterface $em): Response
    {
        $ser = new Service();
        $f = $this->createForm(ServiceType::class, $ser);
        $f->handleRequest($request);
        if ($f->isSubmitted() && $f->isValid()) {
            $em->persist($ser);
            $em->flush();
            return $this->redirectToRoute('service', [], Response::HTTP_SEE_OTHER);
        }
        return $this->render('views/content/apps/administrativeService/app-service-form.html.twig', [
            'form' => $f->createView(),
        ]);
    }

    /**
     * @param EntityManagerInterface $em
     * @return Response
     * @Route ("/{id}/edit", name="Service_Edit")
     */
    public function ModifServices(EntityManagerInterface $em,Request $request, Service $ser): Response
    {
        $form=$this->createForm(ServiceType::class,$ser);
        $form->handleRequest($request);
        if($form->isSubmitted()&&$form->isValid())
        {
            $em->flush();
            return $this->redirectToRoute('service', [], Response::HTTP_SEE_OTHER);
        }
        return $this->render('views/content/apps/administrativeService/app-service-form-Modif.html.twig', [
            'ser'=>$ser,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @param EntityManagerInterface $em
     * @return Response
     * @Route ("/{id}/delete", name="Service_Del")
     */
    public function SuppServices(EntityManagerInterface $em, Service $ser): Response
    {
        $em->remove($ser);
        $em->flush();
        return $this->redirectToRoute('service', [], Response::HTTP_SEE_OTHER);
    }

    /**
     * @Route("/{id}/showRequests", name="ser_sh_requests")
     */
    public function affRequests(ServiceRepository $Repo,$id): Response
    {

        $ser=$Repo->find($id);
        $SerReq=$ser->getServiceRequests();
        return $this->render('views/content/apps/administrativeService/Requests/Affiche.html.twig', [
            'breadcrumbs' => [
                ["name" => "Management"],
                ["name" => "All Services"],
                ["name" => $ser->getName(), "link" => ""],
            ],
            'SerReq' => $SerReq,
            'ser'=>$ser,
        ]);
    }
}
