<?php

namespace App\Controller;

use App\Entity\Call;
use App\Entity\Event;
use App\Entity\User;
use App\Enum\AccessType;
use App\Form\EventType;
use App\Repository\EventRepository;
use App\Repository\UserRepository;
use App\Service\Mail;
use App\Services\MailerService;
use DateTime;
use Doctrine\ORM\EntityManagerInterface;
use Exception;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\SerializerInterface;

class EventController extends AbstractController
{
  private $mailer;

  public function __construct(Mail $mailer)
  {
    $this->mailer = $mailer;
  }

  /**
   * @Route("/", name="show_calendar")
   */
  public function index(): Response
  {
    return $this->render('views/content/apps/calendar/app-calendar.html.twig', [
      'controller_name' => 'EventController',
    ]);
  }

  /**
   * @Route("/add", name="addNewEvent")
   */
  public function new(Request $request, EntityManagerInterface $entityManager, UserRepository $userRep): Response
  {
    $this->denyAccessUnlessGranted(AccessType::CREATE, Event::class);
    $event = new Event();
    $event->setUser($this->getUser());

    $form = $this->createForm(EventType::class, $event);
    $form->handleRequest($request);
    $users = $userRep->findAll();
    // dd($users);
    $userEmail = [];
    foreach ($users as $u) {
      $userEmail[] = $u->getEmail();
    }
    // dd($userEmail);
    if ($form->isSubmitted() && $form->isValid()) {

      $entityManager->persist($event);
      $entityManager->flush();
      $this->mailer->sendNewEventEmail($userEmail, ["event" => $event]);
      $this->addFlash('success', 'Les membres d\'esprit sont notifies par un mail !');
      $this->addFlash('success', 'Evenement ajouté avec succée !');
      return $this->redirectToRoute('all_events_data');
    }

    return $this->render('event/add.html.twig', [
      'event' => $event,
      'form' => $form->createView(),
    ]);
  }

  /**
   * @Route("/event/{id}/edit", name="update")
   */
  public function edit($id, Request $request, EventRepository $rep)
  {

    $event = $rep->find($id);
    $this->denyAccessUnlessGranted(AccessType::EDIT, $event);

    $form1 = $this->createForm(EventType::class, $event);
    $form1->handleRequest($request);

    $em = $this->getDoctrine()->getManager();
    if (($form1->isSubmitted() && $form1->isValid())) {

      $em->persist($event);

      $em->flush();
      $this->addFlash('notice', 'Evenement modifie avec succée !');
      return $this->redirectToRoute("all_events_data");
    }
    return $this->render('event/edit.html.twig', [
      'event' => $event,
      'form' => $form1->createView(),
    ]);
  }

  /**
   * @Route("/event/{id}/delete", name="deleteEvent")
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
    $event = $entityManager->getRepository(Event::class)->find($id);
    $this->denyAccessUnlessGranted(AccessType::DELETE, $event);

    $entityManager->remove($event);
    $this->addFlash('success', 'Evenement bien été supprimée.');
    $this->mailer->sendDeactivatedEventEmail($userEmail, ["event" => $event]);
    $entityManager->flush();

    return $this->redirectToRoute('all_events_data');
  }

  /**
   * @Route("/{id}/delete", name="deleteEventback")
   */
  public function supprimerEvent($id)
  {
    //$this->denyAccessUnlessGranted([AccessType::READ], ServiceRequest::class);

    $entityManager = $this->getDoctrine()->getManager();

    $event = $entityManager->getRepository(Event::class)->find($id);
    $this->denyAccessUnlessGranted(AccessType::DELETE, $event);
    $entityManager->remove($event);
    $this->addFlash('success', 'Evenement bien été supprimée.');


    $entityManager->flush();
    return $this->redirectToRoute('backoffice');
  }

  /**
   * @Route("/{id}/deleteAdmin", name="deleteEventback")
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

  /**
   * @Route("api/events", name="apiEvents")
   */
  public function getAllEvents(EventRepository $eventRepository)
  {
    $events = $eventRepository->findAll();
    $rdvs = [];
    foreach ($events as $event) {
      $rdvs[] = [
        'id' => $event->getId(),
        'start' => $event->getStart()->format('Y-m-d H:i:s'),
        'end' => $event->getEnd()->format('Y-m-d H:i:s'),
        'title' => $event->getTitle(),
        'description' => $event->getDescription(),
        'allDay' => $event->getAllDay(),
        'userId' =>$event->getUser()->getId(),
        'Userfirstname'=>$event->getUser()->getFirstName(),
        'Userlastname'=>$event->getUser()->getLastName()
        ];
    }
    $data = json_encode($rdvs);
    return new Response($data);
  }

  /**
   * @Route ("api/addEvent",name="addevent_api" )
   */

  public function addEvent_api(Request $request)
  {
    $start = $request->query->get("start");
    $end = $request->query->get("end");

    $em = $this->getDoctrine()->getManager();
    $event = new Event();
    //$user = $this->getDoctrine()->getRepository(User::class)->find(2);
    $event->setUser($this->getUser());
    //$event->setUser($user);
    $event->setAllDay($request->query->get("allDay"));
    $event->setStart(new DateTime(($start)));
    $event->setEnd(new DateTime(($end)));
    $event->setDescription($request->query->get("description"));
    $event->setTitle($request->query->get("title"));

    $em->persist($event);
    $em->flush();
    return new Response('event ajouté', 200);
  }

  /**
   * @Route("api/events/{startDate}", name="apiEventByDate")
   */
  public function getEventByDate(EventRepository $eventRepository, $startDate)
  {

    $eventss = $eventRepository->findByStart($startDate);
    if (!empty($eventss)) {
      $rdvs = [];
      foreach ($eventss as $events) {
        $rdvs[] = [
          'id' => $events->getId(),
          'start' => $events->getStart()->format('Y-m-d H:i:s'),
          'end' => $events->getEnd()->format('Y-m-d H:i:s'),
          'title' => $events->getTitle(),
          'description' => $events->getDescription(),
          'allDay' => $events->getAllDay(),
          'userId' =>$events->getUser()->getId(),
          'Userfirstname'=>$events->getUser()->getFirstName(),
          'Userlastname'=>$events->getUser()->getLastName()

        ];
      }
      //dd($rdvs);

      $data = json_encode($rdvs);
      return new Response($data);
    }
    return new Response("error");
  }

  /**
   * @Route("api/events/delete/{id}", name="apiDeleteEventById")
   */
  public function deleteEvent($id)
  {
    try {
      $event = $this->getDoctrine()->getRepository(Event::class)->find($id);
      $this->getDoctrine()->getManager()->remove($event);
      $this->getDoctrine()->getManager()->flush();
      return new JsonResponse("Event {$id} has been deleted", 200);
    } catch (Exception $ex) {
      return new JsonResponse("No event with this id found", 404);
    }
  }

  /**
   * @Route("api/events/update/{id}", name="apiUpdateEventById")
   */
  public function updateEvent(Request $request, $id)
  {
    $em = $this->getDoctrine()->getManager();
    $event = $em->getRepository(Event::class)->find($id);

    $start = $request->get("start");
    $end = $request->get("end");
    $event->setAllDay($request->get("allDay"));
    $event->setStart(new DateTime(($start)));
    $event->setEnd(new DateTime(($end)));
    $event->setDescription($request->get("description"));
    $event->setTitle($request->get("title"));
    $em->flush();

    $rdvs[] = [
      'id' => $event->getId(),
      'start' => $event->getStart()->format('Y-m-d H:i:s'),
      'end' => $event->getEnd()->format('Y-m-d H:i:s'),
      'title' => $event->getTitle(),
      'description' => $event->getDescription(),
      'allDay' => $event->getAllDay(),
    ];
    $data = json_encode($rdvs);
    return new Response("Event updated succefully" . json_encode($data));
  }
}
