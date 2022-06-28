<?php

namespace App\Controller;

// ...
use App\Form\CommentaireType;
use App\Repository\ChannelRepository;
use App\Repository\CommentaireRepository;
use App\Repository\EventRepository;
use App\Repository\GroupPostRepository;
use App\Repository\MessageRepository;
use App\Repository\PostRepository;
use App\Repository\ServiceRequestRepository;
use App\Repository\UserRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Http\Authentication\AuthenticationUtils;
use Symfony\Component\HttpFoundation\Response;
use App\Entity\Commentaire;

class HomeController extends AbstractController
{
  /**
   * @Route("/user/home", name="app_home", methods={"GET"})
   */
  public function show(UserRepository $userRepository,PostRepository $repository, Request $request, CommentaireRepository $commentaireRepository,GroupPostRepository  $groupPostRepository): Response
  {

      $commentaire = new Commentaire();
      $form = $this->createForm(CommentaireType::class, $commentaire);
      $limit = 10;
      $limit2 = 4;
      // les filtres
      $filters = $request->get("allgroups");
      //  dd($request);
      //    dd($filters);

      $posts = $repository->getLatestPosts($limit, $filters);
      $recentP = $repository->PostsMaxQuatre();
      $allgroups = $groupPostRepository->findAll();
      $membre = $userRepository->find($this->getUser());

      $mesgrps = $membre->getGroupes();
      //dd($mesgrps);
      $comments = $repository->CommentsMaxQuatre();
//dd($commentaire);


      $forms = [];

      if ($request->get('ajax')) {
          return new JsonResponse([
              'content' => $this->renderView('views/content/posts/User/contentPosts.html.twig', ['recentP' => $recentP, 'mes_groups' => $mesgrps, 'comments' => $comments, 'posts' => $posts, 'form' => $form->createView()
              ])

          ]);
      }


      //  return $this->render('views/content/posts/User/acceuilposts.html.twig', ['recentP' => $recentP, 'mes_groups' => $mesgrps, 'allgroups' => $allgroups, 'comments' => $comments, 'posts' => $posts, 'form' => $form->createView()]);
      return $this->render('views/content/posts/User/NewHome.html.twig', ['recentP' => $recentP, 'mes_groups' => $mesgrps, 'allgroups' => $allgroups, 'comments' => $comments, 'posts' => $posts, 'form' => $form->createView()]);

  }

    /**
     * @Route("/admin/home", name="app_admin_home", methods={"GET"})
     */
    public function showAdmin(MessageRepository $messageRepository,
                              ChannelRepository $channelRepository,
                              UserRepository $userRepository,
                              GroupPostRepository $groupPostRepository,
                              ServiceRequestRepository $requestRepository,
                              CommentaireRepository $commentaireRepository,
                              EventRepository $eventRepository): Response
    {
      $user=$this->getUser();

      $users=$userRepository->CountByDate();
      $us=$userRepository->CountByActivity();
      $use=$userRepository->count([]);

      $groups=$groupPostRepository->CountByDate();
      $gr=$groupPostRepository->count([]);
      $groups_Monthly=$groupPostRepository->CountByMonth();
      $comm=$commentaireRepository->CountByMonth();

      $ch=$channelRepository->count([]);
      $msg=$messageRepository->count([]);

      $sr=$requestRepository->count([]);
      $sr_unseen=$requestRepository->findBy(['Status'=>'unseen']);
      $sr_success=$requestRepository->findBy(['Status'=>'success']);
      //$sr_response=$requestRepository->findByResponseTime()[0];

        $event=$eventRepository->Last4Events();
      return $this->render('views/content/dashboard/dashboard-analytics.html.twig',
          [
              'user'=>$user,
              'users_per_day' => array_column($users, "cnt"),
              'users_last_activity'=> array_column($us, "cnt"),
              'users'=>$use,
              'groups_per_day' => array_column($groups, "cnt"),
              'groups'=>$gr,
              'disc'=>$ch,
              'msg'=>$msg,
              'sr'=>$sr,
              'sr_unseen'=>$sr_unseen,
              'sr_success'=>$sr_success,
              //'sr_response'=> $sr_response
              'groups_month'=> array_column($groups_Monthly, "cnt"),
              'comm_month'=> array_column($comm, "cnt"),
              'events'=>$event,]
      );
  }
}