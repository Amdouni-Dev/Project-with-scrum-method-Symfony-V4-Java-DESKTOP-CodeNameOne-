<?php

namespace App\Controller;

use App\Entity\Call;
use App\Entity\User;
use App\Entity\Event;
use App\Enum\AccessType;
use App\Repository\CallRepository;
use App\Repository\EventRepository;
use DateTime;
use Doctrine\ORM\EntityManagerInterface;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;

class AllEventsDataController extends AbstractController
{
  /**
   * @Route("app/calendar", name="all_events_data")
   */
  public function index(EventRepository $eventRepository)
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
        'editable' => $this->getUser() == $event->getUser()
      ];
    }

    $data = json_encode($rdvs);
    return $this->render('all_events_data/app-calendar.html.twig', [
      'event' => $data
    ]);
  }


  /**
   * @Route("/api/{id}/edit", name="api_event_edit",methods={"PUT"})
   */
  public function majEvent(?Event $event, Request $request)
  {
    $this->denyAccessUnlessGranted(AccessType::EDIT, $event);
    $donnees = json_decode($request->getContent());

    if (
      isset($donnees->title) && !empty($donnees->title) &&
      isset($donnees->description) && !empty($donnees->description) &&
      isset($donnees->start) && !empty($donnees->start)
    ) {
      $code = 200;
      if (!$event) {
        $event = new Event;
        $code = 201;
      }
      $event->setTitle($donnees->title);
      $event->setDescription($donnees->description);
      $event->setStart(new DateTime($donnees->start));
      if ($donnees->allDay) {
        $event->setEnd(new DateTime($donnees->start));
      } else {
        $event->setEnd(new DateTime($donnees->end));
      }
      $event->setAllDay($donnees->allDay);

      $em = $this->getDoctrine()->getManager();
      $em->persist($event);
      $em->flush();

      return new Response('ok', $code);
    } else {
      return new Response('Données incomplétes', 404);
    }
    return $this->render('main/index.html.twig', compact('data'));
  }

  /**
   * @Route("app/calendar/call", name="indexCall")
   */
  public function indexCall(CallRepository $rep)
  {
    //$this->denyAccessUnlessGranted(AccessType::READ, Event::class);
    $calls = $this->getUser()->getCalls()->toArray();

    $rdvs = [];
    foreach ($calls as $call) {
      $rdvs[] = [
        'id' => $call->getId(),
        'start' => $call->getStart()->format('Y-m-d H:i:s'),
        'end' => $call->getEnd()->format('Y-m-d H:i:s'),
        'title' => $call->getTitle(),
        'description' => $call->getDescription(),
        'editable' => $this->getUser() == $call->getUser()

      ];
    }

    $data = json_encode($rdvs);

    return $this->render('call/index.html.twig', [
      'call' => $data
    ]);
  }


  /**
   * @Route("/api/{id}/editcall", name="api_call_edit",methods={"PUT"})
   */
  public function majCall(?Call $call, Request $request)
  {
    $this->denyAccessUnlessGranted(AccessType::EDIT, $call);

    $donnees = json_decode($request->getContent());

    if (
      isset($donnees->title) && !empty($donnees->title) &&
      isset($donnees->description) && !empty($donnees->description) &&
      isset($donnees->start) && !empty($donnees->start)
    ) {
      $code = 200;
      if (!$call) {
        $call = new Call;
        $code = 201;
      }
      $call->setTitle($donnees->title);
      $call->setDescription($donnees->description);
      $call->setStart(new DateTime($donnees->start));
      $call->setEnd(new DateTime($donnees->end));

      $em = $this->getDoctrine()->getManager();
      $em->persist($call);
      $em->flush();

      return new Response('ok', $code);
    } else {
      return new Response('Données incomplétes', 404);
    }
    return $this->render('main/index.html.twig', compact('data'));
  }

  /**
   * @Route("/app/calendar/backoffice/call", name="backofficeCall")
   */
  function backOffice(CallRepository $callEntity, EntityManagerInterface $em, PaginatorInterface $paginator, Request $request)
  {
    /*   $dataEvent = $event->findAll();
      $dataCall = $call->findAll(); */
    $this->denyAccessUnlessGranted(AccessType::READ, Call::class);
    $this->denyAccessUnlessGranted(AccessType::DELETE, Call::class);
    $dql = <<<DQL
    select tel from App\Entity\Call tel
    DQL;
    $query = $em->createQuery($dql);

    $pagination = $paginator->paginate(
      $query,
      $request->query->getInt('page', 1),
      5);
    //  dd($pagination);
    return $this->render('all_events_data/backofficeCall.html.twig', compact('pagination'));
  }

  /**
   * @Route("/app/calendar/backoffice/event", name="backofficeEvent")
   */
  function backOfficeEvent(EntityManagerInterface $em, PaginatorInterface $paginator, Request $request)
  {
    /*   $dataEvent = $event->findAll();
      $dataCall = $call->findAll(); */

    $this->denyAccessUnlessGranted(AccessType::READ, Event::class);
    $this->denyAccessUnlessGranted(AccessType::DELETE, Event::class);
    $dql = <<<DQL
    select tel from App\Entity\Event tel
    DQL;
    $query = $em->createQuery($dql);

    $pagination = $paginator->paginate(
      $query,
      $request->query->getInt('page', 1),
      5);
    //  dd($pagination);
    return $this->render('all_events_data/backofficeEvent.html.twig', compact('pagination'));
  }

  /**
   * @Route("api/events/all", name="apiEvents")
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
      ];
    }

    $data = json_encode($rdvs);
    return new Response($data);

  }

  /**
   * @Route ("api/addEvent",name="addevent_api")
   */

  public function addEvent_api(Request $request, NormalizerInterface $normalizer)
  {


    $em = $this->getDoctrine()->getManager();
    $event = new Event();
    $user = $this->getDoctrine()->getRepository(User::class)->find(2);
    $event->setUser($user);
    $event->setAllDay(false);
    $event->setStart($request->query->get("start"));
    $event->setEnd($request->query->get("end"));
    $event->setDescription($request->query->get("description"));
    $event->setTitle($request->query->get("title"));

    $em->persist($event);
    $em->flush();


    return new Response('event ajouter ajouté', 200);

  }
}
