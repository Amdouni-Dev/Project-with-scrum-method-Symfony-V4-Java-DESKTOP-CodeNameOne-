<?php

namespace App\Controller;

use App\Entity\Call;
use App\Entity\Event;
use App\Enum\AccessType;
use App\Form\CallType;
use App\Form\EventType;
use App\Repository\CallRepository;
use App\Repository\UserRepository;
use App\Service\Mail;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * @Route("/call")
 */
class CallController extends AbstractController
{
  private $mailer;

  public function __construct(Mail $mailer)
  {
    $this->mailer = $mailer;
  }

  /**
   * @Route("/", name="call")
   */
  public function index(): Response
  {
    return $this->render('call/index.html.twig', [
      'controller_name' => 'CallController',
    ]);
  }

  /**
   * @Route("/add", name="addNewCall")
   */
  public function new(Request $request, EntityManagerInterface $entityManager): Response
  {
    $this->denyAccessUnlessGranted(AccessType::CREATE, Call::class);
    $call = new Call();
    $call->setUser($this->getUser());

    $form = $this->createForm(CallType::class, $call);
    $form->handleRequest($request);

    $userCall = [];

    if ($form->isSubmitted() && $form->isValid()) {
      $users = $call->getUsers();
      //dd($users);
      foreach ($users as $u) {
        $userCall[] = $u->getEmail();
      }
      $entityManager->persist($call);
      $entityManager->flush();
      $this->mailer->sendNewCallEmail($userCall, ["call" => $call]);
      $this->addFlash('success', 'Les membres du call sont notifies par un mail !');
      $this->addFlash('notice', 'call ajouté avec succée !');
      return $this->redirectToRoute('indexCall');
    }

    return $this->render('call/add.html.twig', [
      'call' => $call,
      'form' => $form->createView(),
    ]);
  }

  /**
   * @Route("/{id}/editcall", name="updatecall")
   */
  public function edit($id, Request $request, CallRepository $rep)
  {
    $call = $rep->find($id);
    $this->denyAccessUnlessGranted(AccessType::EDIT, $call);
    $form1 = $this->createForm(CallType::class, $call);
    $form1->handleRequest($request);
    $em = $this->getDoctrine()->getManager();
    if (($form1->isSubmitted() && $form1->isValid())) {
      $em->persist($call);
      $em->flush();
      $this->addFlash('notice', 'call modifie avec succées !');
      return $this->redirectToRoute("indexCall");
    }
    return $this->render('call/edit.html.twig', [
      'call' => $call,
      'form' => $form1->createView(),
    ]);
  }
  /**
   * @Route("/{id}/delete", name="deleteCall")
   */
  public function supprimer($id, UserRepository $userRep)
  {

    $entityManager = $this->getDoctrine()->getManager();
    $users = $userRep->findAll();
    // dd($users);
    $userEmail = [];
    foreach ($users as $u) {
      $userEmail[] = $u->getEmail();
    }
    $call = $entityManager->getRepository(Call::class)->find($id);
    $this->mailer->sendDeactivatedCallEmail($userEmail,["call"=>$call]);
    $this->denyAccessUnlessGranted(AccessType::DELETE, $call);


      $entityManager->remove($call);
      $this->addFlash('success', 'Call bien été supprimée.');
      $entityManager->flush();


    return $this->redirectToRoute('indexCall');
  }

  /**
   * @Route("/{id}/deleteAdmin", name="deleteCallback")
   */
  public function supprimercall($id)
  {
    $entityManager = $this->getDoctrine()->getManager();
    $call = $entityManager->getRepository(Call::class)->find($id);
    $this->denyAccessUnlessGranted(AccessType::DELETE, $call);
    $entityManager->remove($call);
    $this->addFlash('success', 'Call bien été supprimée.');
    $entityManager->flush();
    return $this->redirectToRoute('backofficeCall');
  }
}
