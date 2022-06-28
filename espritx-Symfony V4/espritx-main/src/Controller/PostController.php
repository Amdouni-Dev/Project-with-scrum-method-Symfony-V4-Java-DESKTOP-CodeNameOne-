<?php

namespace App\Controller;

use App\Entity\Commentaire;
use App\Entity\Images;
use App\Entity\Post;
use App\Entity\PostLike;
use App\Entity\User;
use App\Form\CommentaireType;
use App\Form\EditPostType;
use App\Form\PostContactType;
use App\Form\PostType;
use App\Repository\CommentaireRepository;
use App\Repository\GroupPostRepository;
use App\Repository\PostLikeRepository;
use App\Repository\PostRepository;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\Persistence\ObjectManager;
use Knp\Component\Pager\PaginatorInterface;
use phpDocumentor\Reflection\Types\This;
use PhpParser\Node\Expr\Cast\Object_;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Config\Definition\Exception\Exception;
use Symfony\Component\HttpFoundation\File\File;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Address;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\Serializer\SerializerInterface;
use Symfony\Contracts\Translation\TranslatorInterface;
use function Doctrine\Common\Annotations\AnnotationException;


class PostController extends AbstractController
{

    private $postRepository;
    private $entityManager;

    public function __construct(PostRepository $postRepository, EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
        $this->postRepository = $postRepository;
    }

    ///////////////////// Afficher tous les posts /////////////////////////
    /// en tant qu'administrateur je veux gerer les posts

    /**
     * @Route("admin/post/all", name="postall")
     * @param PostRepository $repository
     * @param Request $request
     * @return Response
     * @throws Exception
     */
    public function afficher_tous_les_Post(PostRepository $repository, Request $request, SerializerInterface $serializer, PaginatorInterface $paginator): Response
    {

        $totalPubs = $repository->createQueryBuilder('a')
            // Filter by some parameter if you want
            ->where('a.isValid != 1 ')
            ->select('count(a.id)')
            ->getQuery()
            ->getSingleScalarResult();

        $donnees = $this->getDoctrine()->getRepository(Post::class)->findBy([], [
            'created_at' => 'desc'
        ]);
        $posts = $paginator->paginate(
            $donnees, // n3adi les donnees
            $request->query->getInt('page', 1), // num l page
            10
        );

        //$json=$serializer->serialize($posts,'json',['groups'=>'posts']);
        // dump($json);
        // die;
        return $this->render('views/content/posts/Admin/allpost.html.twig', [
            'pubs' => $totalPubs,
            'posts' => $posts]);
    }


    /////////////////// Ajouter post /////////////////
    /// En tant qu'utilisateur, je veux ajouter une publication.
    ///
    /**
     * @Route("/post/new", name="newpost")
     * @param Request $request
     * @param UserRepository $repository
     * @return Response
     * @throws Exception
     */
    public function ajouterPost(Request $request, UserRepository $repository): Response
    {
        try {
            $now = new \DateTimeImmutable('now');
            $post = new Post();
            $form1 = $this->createForm(PostType::class, $post);

            $form1->handleRequest($request);

            $em = $this->getDoctrine()->getManager();
            if (($form1->isSubmitted() && $form1->isValid())) {


                $images = $form1->get('images')->getData();

                // On boucle sur les images
                foreach ($images as $image) {
                    // On génère un nouveau nom de fichier
                    $fichier = md5(uniqid()) . '.' . $image->guessExtension();

                    // On copie le fichier dans le dossier uploads
                    $image->move(
                        $this->getParameter('imagesPost_directory'),
                        $fichier
                    );

                    // On crée l'image dans la base de données
                    $img = new Images();
                    $img->setName($fichier);
                    $post->addImage($img);
                }


                $a = $request->request->get('markers1');
                $b = $request->request->get('markers2');
                $post->setLongitude($a);
                $post->setLatitude($b);
                /*   $file = $form1->get('image')->getData();
                   $fileName = md5(uniqid()) . '.' . $file->guessExtension();
                   $file->move(
                     $this->getParameter('imagesPost_directory'),
                     $fileName


                   );
                 */
                // $post->setImage($fileName);
                $post->setIsValid(0);
                $post->setIsDeleted(0);

                $post->setCreatedAt($now);
                $post->setUpdatedAt($now);

                $post->setUser($repository->find($this->getUser()->getId()));
                $em->persist($img);
                $em->persist($post);
                $em->flush();
                $request->getSession()->getFlashBag()->add("info", "Publication ajoutée! mùais doit etre approuvée ar notre admin ");


                // $this->addFlash("info", "Publication ajoutée ");

                return $this->redirectToRoute("acceuil_user_posts");
            }
        } catch (\Exception $e) {
            echo "Exception Found - " . $e->getMessage() . "<br/>";
        }
        return $this->render('views/content/posts/User/newPost.html.twig', [
            'form' => $form1->createView(),
            'breadcrumbs' => [
                ["name" => "Management"],
                ["name" => "posts", "link" => "postall"],
            ],
        ]);
    }

    /**
     * @return string
     */
    private function generateUniqueFileName()
    {
        // md5() reduces the similarity of the file names generated by
        // uniqid(), which is based on timestamps
        return md5(uniqid());
    }

    ////////////////////Modifuer Post ////////////////////////////////////
    /// En tant qu'utilisateur, je veux modifier une publication.
    /**
     * @Route("/post/edit/{id}", name="editpost")
     * @param Request $request
     * @param UserRepository $repository
     * @return Response
     * @throws Exception
     */
    public function editpublication($id, Request $request, UserRepository $repository, PostRepository $rep): Response
    {

        $post = $rep->find($id);

        $form1 = $this->createForm(EditPostType::class, $post);
        $form1->handleRequest($request);

        // dd($post);
        $em = $this->getDoctrine()->getManager();
        if (($form1->isSubmitted() && $form1->isValid())) {
            $a = $request->request->get('markers1');
            $b = $request->request->get('markers2');
            $post->setLongitude($a);
            $post->setLatitude($b);

            // $post->setImage(
            //   new File($this->getParameter('uploads/brochures').'/'.$post->getImage())
            //);
            $post->setUpdatedAt(new \DateTimeImmutable('now'));
            $post->setIsValid(0);
            $post->setUser($repository->find($this->getUser()->getId()));
            $em->persist($post);

            $em->flush();
            $request->getSession()->getFlashBag()->add("info", "Publication bien modifiée! mais doit etre approuvée par notre admin ! ");
            return $this->redirectToRoute("acceuil_user_posts");
        }
        return $this->render('views/content/posts/User/editPost.html.twig', ['form' => $form1->createView()]);

    }

    //////////////////Supprimer Post ////////////////////////////////////////
    /// En tant qu'utilisateur, je veux supprimer une publication.

    /**
     * @Route("/user/post/changedelete/{id}",name="changedelete_post")
     */
    public function supprimer_Post_user(Post $post, PostRepository $postRepository, Request $request)
    {
        $post = $postRepository->changeDelete($post);
        $request->getSession()->getFlashBag()->add("info", "Publication supprimée");
        return $this->redirectToRoute('acceuil_user_posts');
        // return $this->json(["message"=>"success","value"=>$post->getIsDeleted()]);
    }

    /////////////////////////////Approuver Post/////////////////////
    /// En tant qu'administrateur, je veux approuver une publication.
    /// En tant qu'administrateur, je veux refuser une publication.
    /**
     * @Route("/admin/post/changevalidite/{id}",name="changevalidite_post")
     */
    public function approuverPost(Post $post, PostRepository $postRepository)
    {
        $post = $postRepository->changeValidite($post);
        //  $this->addFlash('success', 'Publication approuvée');
        // return $this->json(["message"=>"success","value"=>$post->getIsValid()]);
        $this->addFlash('success', 'Publication bien été approuvée');
        return $this->redirectToRoute('postall');

    }

    //////////////////////////Supprimer Post //////////////////////////////
    /// En tant qu'administrateur, je veux supprimer defintivement une publication.

    /**
     * @Route("/admin/delete/post/{id}",name="deletepost_admin")
     */
    public function supprimer_Post_admin($id)
    {


        $entityManager = $this->getDoctrine()->getManager();

        $post = $entityManager->getRepository(Post::class)->find($id);
        $entityManager->remove($post);
        $this->addFlash('success', 'Publication bien été supprimée.');

        $entityManager->flush();
        return $this->redirectToRoute('postall');


    }
////////////////////////////// Acceuil posts ////////////////////////////
/// Entant qu'utilisateur je veux consulter les postes
    /**
     * @Route("/acceuil/user/post",name="acceuil_user_posts")
     */
    public function afficher_posts(UserRepository $userRepository, PostRepository $repository, Request $request, CommentaireRepository $commentaireRepository, GroupPostRepository $groupPostRepository)
    {
        // dd($request->getContent());
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
//     return $this->render('views/content/pages/page-profile.html.twig', ['mes_groups'=>$mesgrps,'allgroups'=>$allgroups,'comments' => $comments, 'posts' => $posts, 'form' => $form->createView()]);
    }
/////////////////////////////////////////////////////////////////////////////////////////////// Commentaires /////////////////////////////////
///


    /**
     * @Route("/post/{id}/addcomment", name="newcomment")
     * @param $id
     * @param Request $request
     * @param PostRepository $repository
     * @param UserRepository $rep
     * @return Response
     */
    public function addcomment($id, Request $request, PostRepository $repository, UserRepository $rep): Response
    {
        $pub = $repository->find($id);
        $now = new \DateTimeImmutable('now');
        $commentaire = new Commentaire();
//        $comment = $_POST['aa'];
        $form = $this->createForm(CommentaireType::class, $commentaire);
        $form->handleRequest($request);

        $data = $request->request->get('aa');
        //  dd($data);
        //if ($form->isSubmitted() && $form->isValid()) {

        $em = $this->getDoctrine()->getManager();
        $commentaire->setCreatedAt($now);
        $commentaire->setUser($rep->find($this->getUser()->getId()));
        $commentaire->setPost($pub);
        $commentaire->setContent($data);
        $em->persist($commentaire);
        $em->flush();
        return $this->json(['code' => 200, 'nbrcomments' => $pub->getCommentaires()->count(),
            'commentaire' => $commentaire->getContent(),
            'dateajout' => $commentaire->getCreatedAt()->format('H:i')], 200);
        //  }
        //   return $this->json(['code' => 200, 'nbrcomments' => $pub->getCommentaires()->count(),
        // ], 200);
    }


    /**
     * @Route("/comment/neww/{id}", name="comment_new")
     * @param Request $request
     * @param UserRepository $repository
     * @return Response
     * @throws Exception
     */
    public function ajouterComment($id, Request $request, UserRepository $repository, PostRepository $postRepository): Response
    {
        $commentaire = new Commentaire();
        $form = $this->createForm(CommentaireType::class, $commentaire);

        $now = new \DateTimeImmutable('now');

        $post = $postRepository->find($id);
        $form->handleRequest($request);

        $em = $this->getDoctrine()->getManager();
        if (($form->isSubmitted() && $form->isValid())) {
            $commentaire->setPost($post);

            $commentaire->setCreatedAt($now);


            $commentaire->setUser($repository->find($this->getUser()->getId()));
            $em->persist($commentaire);
            $em->flush();


            $this->addFlash("success", "Publication ajoutée ");
            return $this->json(['code' => 200, 'nbrcomments' => $post->getCommentaires()->count(),
                'dateajout' => $commentaire->getCreatedAt()->format('H:i')], 200);


        }


        return $this->render('views/content/posts/User/newComment.html.twig', ['form' => $form->createView()]);
    }

//////////////////////////////////////////////////Comment Modif////////////////////////////
/// en tant qu'utilisateur je veux editer mon commentaire
    /**
     * @Route("/comment/edit/{id}", name="editcomment")
     * @param Request $request
     * @param UserRepository $repository
     * @return Response
     * @throws Exception
     */
    public function editcommentaire($id, Request $request, UserRepository $repository, CommentaireRepository $rep): Response
    {

        $comm = $rep->find($id);

        $form1 = $this->createForm(CommentaireType::class, $comm);
        $form1->handleRequest($request);

        // dd($post);
        $em = $this->getDoctrine()->getManager();
        if (($form1->isSubmitted() && $form1->isValid())) {


            $comm->setUser($repository->find($this->getUser()->getId()));
            $em->persist($comm);

            $em->flush();
            $this->addFlash('notice', 'Commentaire modifiée avec succée !');
            return $this->redirectToRoute("acceuil_user_posts");
        }
        return $this->render('views/content/posts/User/editComment.html.twig', ['form' => $form1->createView()]);

    }

/////////////////////////////////////Delete comment//////////////////////////////
/// En tant qu'utilisateur je veux supprimer mes commentaires////////////////////
    /**
     * @Route("/user/delete/post/{id}",name="delete_commnt_user")
     */
    public function supprimerComment($id)
    {


        $entityManager = $this->getDoctrine()->getManager();

        $post = $entityManager->getRepository(Commentaire::class)->find($id);
        $entityManager->remove($post);
        $this->addFlash('success', 'commentaire bien été supprimée.');


        $entityManager->flush();
        return $this->redirectToRoute('acceuil_user_posts');


    }

///////////////////////// Likes ////////////////////////////
/// cette fonction permet de liker ou unliker un post
    /**
     * @param Post $post
     * @param PostLikeRepository $likeRepository
     * @return Response
     * @Route ("/post/{id}/like", name="post_like")
     */
    public function like(Request $request, Post $post, PostLikeRepository $likeRepository): Response
    {
        $em = $this->getDoctrine()->getManager();
        $user = $this->getUser();
        // savoir si le user est connecté ou nn
        if (!$user) return $this->json([
            'code' => 403,
            'message' => 'il faut etre connecté'
        ], 403);
        // savoir si ce post est liké par user ou non
        if ($post->isLikedByUser($user)) {
            // retrouver le j'aime
            $like = $likeRepository->findOneBy([
                'post' => $post,
                'user' => $user
            ]);
            $em->remove($like);
            $em->flush();
            $request->getSession()->getFlashBag()->add("info", "oh oh !! ");
            return $this->json([
                'code' => 200,
                'message' => 'like bien supprimé',
                'likess' => $likeRepository->count(['post' => $post])
            ], 200);

        }

        $like = new PostLike();
        $like->setPost($post)
            ->setUser($user);
        $em->persist($like);
        $em->flush();
        $request->getSession()->getFlashBag()->add('info', 'Merci');
        return $this->json([
            'code' => 200,
            'message' => 'like bien ajouté',
            'likess' => $likeRepository->count(['post' => $post])
        ], 200);


    }

    /**
     * @Route("/post/{id}", name="singlepost")
     * @param PostRepository $repository
     * @param Request $request
     * @return Response
     */
    public function singlepost($id, PostRepository $repository, Request $request, MailerInterface $mailer): Response
    {
        $pub = $repository->find($id);
        $commentaire = new Commentaire();
        $form = $this->createForm(CommentaireType::class, $commentaire);
        $formcontact = $this->createForm(PostContactType::class);
        $contact = $formcontact->handleRequest($request);
        if ($formcontact->isSubmitted() && $formcontact->isValid()) {
            $email = (new TemplatedEmail())
                ->from(new Address($contact->get('email')->getData(), 'ESPRITx'))
                ->to($pub->getUser()->getEmail())
                ->subject('Your post has received a comment!')
                ->htmlTemplate('views/content/posts/email/contact_post.html.twig')
                ->context([
                    'post' => $pub,
                    'mail' => $contact->get('email')->getData(),
                    'message' => $contact->get('message')->getData()
                ]);
            $mailer->send($email);
            $this->addFlash('message', 'Votre email a bien envoyé ');
            return $this->redirectToRoute('singlepost', ['id' => $pub->getId()]);
        }
        return $this->render('views/content/posts/User/SinglPost.html.twig', ['formContact' => $formcontact->createView(), 'post' => $pub, 'form' => $form->createView()]);
    }


    ////////////////////////////////////// ///////////////////////////////   API /////////////////// //////////////////  //////////////

    /**
     * @Route("api/post/all", name="postall_api")
     * @return Response
     * @throws Exception
     */
    public function api_tous_les_Post(NormalizerInterface $normalizer,PostRepository $repository,CommentaireRepository $commentaireRepository)
    {



        $totalPubs = $repository->createQueryBuilder('a')
            // Filter by some parameter if you want
            ->where('a.isValid != 1 ')
            ->select('count(a.id)')
            ->getQuery()
            ->getSingleScalarResult();

        $totalCommets = $commentaireRepository->createQueryBuilder('a')
            // Filter by some parameter if you want

            ->select('count(a.id)')
            ->getQuery()
            ->getSingleScalarResult();


        $posts = $this->getDoctrine()->getManager()->getRepository(Post::class)->PostforApi();



        $rdvs = [];

        $rdvs2 = [];


        foreach ($posts as $post) {
            $comments= $commentaireRepository->getCommentsForPost($post->getId());
            foreach ( $comments as $c ) {
if($c->getPost()== $post) {
    $rdvs2[] = [

        'commentaire' => $c->getContent(),


    ];
}
            }


                $rdvs[] = [
                    'id' => $post->getId(),
                    'createdAt' => $post->getCreatedAt()->format('y-m-d'),
                    'isValid' => $post->getIsValid(),
                    'title' => $post->getTitle(),
                    'content' => $post->getContent(),
                    'userId' => $post->getUser()->getId(),
                    'longitude'=>$post->getLongitude(),

                    'latitude'=>$post->getLatitude(),
                    'nbLikes'=>$post->getLikes()->count(),
                     'nbPub'=>$totalPubs,
                    'nbrcomments' => $post->getCommentaires()->count(),
                    'totalComments'=> $totalCommets ,
                    'commentaires'=>$rdvs2,
                    'nom'=>$post->getUser()->getFirstName(),
                    'prenom'=>$post->getUser()->getLastName(),
                    'email'=>$post->getUser()->getEmail(),



                ];

        }

        $data = json_encode($rdvs);

        return new Response($data);

    }






    /**
     * @Route("api/Commmm/all", name="Commmall_api")
     * @return Response
     * @throws Exception
     */
    public function api_tous_les_Comm(NormalizerInterface $normalizer,PostRepository $repository,CommentaireRepository $commentaireRepository)
    {


        $posts = $this->getDoctrine()->getManager()->getRepository(Post::class)->findAll();

        $rdvs2 = [];
     foreach ($posts as $post) {
            $comments= $commentaireRepository->getCommentsForPost($post->getId());
            foreach ( $comments as $c ) {
                if($c->getPost()== $post) {
                    $rdvs2[] = [

                        'commentaire' => $c->getContent(),
                        'postId'=>$c->getPost()->getId(),
                        'nom'=>$c->getUser()->getFirstName(),
                        'prenom'=>$c->getUser()->getLastName()


                    ];
                }
            }



        }

        $data = json_encode($rdvs2);

        return new Response($data);

    }




    /**
     * @Route ("api/addPost",name="addpost_api" , methods={"GET", "POST"})
     */

    public function addPost_api(Request $request, NormalizerInterface $normalizer)
    {
        $now = new \DateTimeImmutable('now');
        $donnees = json_decode($request->getContent());
        $em = $this->getDoctrine()->getManager();
        $post = new Post();
        $post->setIsValid(0);
        $post->setIsDeleted(0);

        $post->setCreatedAt($now);
        $post->setUpdatedAt($now);
        //  $user = $this->getDoctrine()->getRepository(User::class)->find(76);
//$idUser= $request->query->get("user");
        $post->setUser($this->getUser());

  //      $post->setUser($this->getDoctrine()->getManager()->getRepository(User::class)->find($idUser));

        $post->setImage($request->get('image'));

        $post->setTitle($request->get('title'));
        $post->setContent($request->get('content'));

        $post->setLongitude($request->get('longitude'));
        $post->setLatitude($request->get('latitude'));

        $em->persist($post);
        $em->flush();


        return new Response('post ajouté');

    }





    /**
     * @Route("api/comment/newwApi/{id}", name="comment_newApi")
     * @param Request $request
     * @param UserRepository $repository
     * @return Response
     * @throws Exception
     */
    public function ajouterCommentApi($id, Request $request, UserRepository $repository, PostRepository $postRepository): Response
    {
        $commentaire = new Commentaire();
        $form = $this->createForm(CommentaireType::class, $commentaire);


        $post = $postRepository->find($id);
        $now = new \DateTimeImmutable('now');
        $donnees = json_decode($request->getContent());
        $em = $this->getDoctrine()->getManager();



$commentaire->setCreatedAt($now);
$commentaire->setPost($post);
        //  $user = $this->getDoctrine()->getRepository(User::class)->find(76);
//$idUser= $request->query->get("user");
        $commentaire->setUser($this->getUser());

        //      $post->setUser($this->getDoctrine()->getManager()->getRepository(User::class)->find($idUser));

        $commentaire->setContent($request->get('content'));



        $em->persist($commentaire);
        $em->flush();


        return new Response('pcommentaire ajouté');

    }












    /*
        public function addPost_api(Request $request,SerializerInterface $serializer,EntityManagerInterface $entityManager){

            $content=$request->getContent();

            $data=$serializer->deserialize($content,Post::class,'json');
            $entityManager->persist($data);
            $entityManager->flush();
            return new Response('post ajouté');

        }
    */
    /**
     * @param Request $request
     * @param $id
     * @param NormalizerInterface $normalizer
     * @Route("/api/post/{id}" , name="api_post_id")
     */


    public function Post_By_Id_JSON(Request $request, $id, NormalizerInterface $normalizer)
    {
        $em = $this->getDoctrine()->getManager();
        $post = $em->getRepository(Post::class)->find($id);
        $jsoncontent = $normalizer->normalize($post, 'json', ['groups' => 'post:read']);
        return new Response(json_encode(($jsoncontent)));
    }

    /**
     * @param Request $request
     * @param NormalizerInterface $normalizer
     * @param $id
     */


    /**
     * @param Request $request
     * @param NormalizerInterface $normalizer
     * @param $id
     * @return Response
     * @throws \Symfony\Component\Serializer\Exception\ExceptionInterface
     * @Route("/api/updatePost/{id}",name="api_post_update")
     */
    public function Update_Post_Json(Request $request, NormalizerInterface $normalizer, $id)
    {
        $now = new \DateTimeImmutable('now');
     //   $now2 = new \DateTime('now');
        $em = $this->getDoctrine()->getManager();
        $post = $em->getRepository(Post::class)->find($id);

        $post->setIsDeleted(0);

       // $post->setCreatedAt($now2);
       // $post->setUpdatedAt($now);

    //    $post->setUser($this->getUser());
        $post->setTitle($request->get('title'));
        $post->setContent($request->get('content'));
        $post->setIsValid($request->get('isValid'));
        $em->flush();
        $content = $normalizer->normalize($post, 'json', ['groups' => 'post:read']);
        return new Response("Post bien modifié" . json_encode($content));


    }

    /**
     * @param NormalizerInterface $normalizer
     * @param $id
     * Route("/api/DeletePosttt/{id}",name="api_delete_post")
     * @return Response
     * @throws \Symfony\Component\Serializer\Exception\ExceptionInterface
     */

    public function deletePost_Json(NormalizerInterface $normalizer, $id)
    {

        $em = $this->getDoctrine()->getManager();
        $post = $em->getRepository(Post::class)->find($id);
        $em->remove($post);
        $em->flush();
     $content = $normalizer->normalize($post, 'json', ['groups' => 'post:read']);
        return new Response("Post bien eté supprimé" . json_encode($content));
    }

    /**
     * @Route("/stats", name="stats")
     */
    public function statistiques()
    {
        return $this->render('views/content/posts/Admin/stats.html.twig');

    }

    /**
     * @Route("/supprime/image/{id}", name="post_delete_image")
     */
    public function Supprimerimages(Images $image, Request $request)
    {
        $data = json_decode($request->getContent(), true);

        // On vérifie si le token est valide
        if ($this->isCsrfTokenValid('delete' . $image->getId(), $data['_token'])) {
            // On récupère le nom de l'image
            $nom = $image->getName();
            // On supprime le fichier
            unlink($this->getParameter('images_directory') . '/' . $nom);

            // On supprime l'entrée de la base
            $em = $this->getDoctrine()->getManager();
            $em->remove($image);
            $em->flush();

            // On répond en json
            return new JsonResponse(['success' => 1]);
        }

    }

    /**
     * @Route ("/user/{id}/posts", name="user_posts")
     */
    public function ShowUsersPosts($id, PostRepository $postRepository, UserRepository $userRepository)
    {
        $user = $userRepository->find($id);
        $posts = $postRepository->findBy(['user' => $user]);
        dd($posts);

    }




    /**
     * @Route("/api/deletePost/{id}", name="delete_pollllssss")

     */

    public function deletePostAction(Request $request) {
        $id = $request->get("id");

        $em = $this->getDoctrine()->getManager();
        $reclamation = $em->getRepository(Post::class)->find($id);
        if($reclamation!=null ) {
            $em->remove($reclamation);
            $em->flush();

            $serialize = new Serializer([new ObjectNormalizer()]);
            $formatted = $serialize->normalize("Post a ete supprimee avec success.");
            return new JsonResponse($formatted);

        }
        return new JsonResponse("id post invalide.");


    }

    /**
     * @param Request $request
     * @param NormalizerInterface $normalizer
     * @param $id
     * @return Response
     * @throws \Symfony\Component\Serializer\Exception\ExceptionInterface
     * @Route("/api/updatePoste",name="api_posteee" , methods={"GET", "POST"})
     */
    public function Update_Poste(Request $request)
    {

        $em = $this->getDoctrine()->getManager();
        $post = $this->getDoctrine()->getManager()
            ->getRepository(Post::class)
            ->find($request->get('id'));



      //  $post->setUser($user);
        $post->setTitle($request->get('title'));
        $post->setContent($request->get('content'));

        $post->setIsValid($request->get('valid'));
       // $post->setIsValid(true);
    //  $post->setIsValid(true);
        $em->flush();
        $serialize = new Serializer([new ObjectNormalizer()]);
        $formatted = $serialize->normalize("Post a ete modifié avec success.");
        return new JsonResponse($formatted);


    }













}