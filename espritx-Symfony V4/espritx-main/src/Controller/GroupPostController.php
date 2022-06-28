<?php

namespace App\Controller;

use App\Entity\Commentaire;
use App\Entity\GroupPost;
use App\Entity\Images;
use App\Entity\Post;
use App\Form\CommentaireType;
use App\Form\GroupPostType;
use App\Form\PostContactType;
use App\Form\PostType;
use App\Repository\GroupPostRepository;
use App\Repository\PostGroupRepository;
use App\Repository\PostRepository;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Config\Definition\Exception\Exception;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Serializer\SerializerInterface;

class GroupPostController extends AbstractController
{
    /**
     * @Route("/group/post", name="app_group_post")
     */
    public function index(): Response
    {
        return $this->render('group_post/index.html.twig', [
            'controller_name' => 'GroupPostController',
        ]);
    }

    /**
     * @Route("/group/new", name="newgroup")
     * @param Request $request
     * @param UserRepository $repository
     * @return Response
     * @throws Exception
     */
    public function ajouterGroupPost(Request $request, UserRepository $repository): Response
    {
        try {
            $now = new \DateTimeImmutable('now');
            $groupPost = new GroupPost();
            $form1 = $this->createForm(GroupPostType::class, $groupPost);
            $form1->handleRequest($request);

            $em = $this->getDoctrine()->getManager();
            if (($form1->isSubmitted() && $form1->isValid())) {


                $file = $form1->get('image')->getData();
                $fileName = md5(uniqid()) . '.' . $file->guessExtension();
                $file->move(
                    $this->getParameter('imagesGroupPost_directory'),
                    $fileName
                );
                $groupPost->setImage($fileName);

                $groupPost->setIsValid(0);
                $groupPost->setIsDeleted(0);

                $groupPost->setCreatedAt($now);


                $groupPost->setUser($repository->find($this->getUser()->getId()));
                $em->persist($groupPost);
                $em->flush();
                $request->getSession()->getFlashBag()->add("info", "votre groupe est crée ! Merci d'attendre l'approuvation admin  ");


                // $this->addFlash("info", "Publication ajoutée ");

                return $this->redirectToRoute("acceuil_user_posts");
            }
        } catch (\Exception $e) {
            echo "Exception Found - " . $e->getMessage() . "<br/>";
            dd($e);
        }

        return $this->render('views/content/posts/GroupPost/NewGroupPost.html.twig', [
            'form1' => $form1->createView(),

        ]);
    }
    /**
     * @Route("group/post/new/{id}", name="newpost_group")
     * @param Request $request
     * @param UserRepository $repository
     * @return Response
     * @throws Exception
     */
    public function ajouterPostGroup($id,Request $request, UserRepository $repository,GroupPostRepository $groupPostRepository,GroupPost $groupPost): Response
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


                $groupPost = $groupPostRepository->find($id);
                $a = $request->request->get('markers1');
                $b = $request->request->get('markers2');
                $post->setLongitude($a);
                $post->setLatitude($b);


                $post->setIsValid(0);
                $post->setIsDeleted(0);

                $post->setCreatedAt($now);
                $post->setUpdatedAt($now);
                $post->setGroupPost($groupPost );
                $post->setUser($repository->find($this->getUser()->getId()));

                $em->persist($post);
                $em->persist($img);
                $groupPost->addPost($post);

                $em->flush();
                $request->getSession()->getFlashBag()->add("info", "Publication ajoutée ! mais doit etre approuvée par notre admin .");


                // $this->addFlash("info", "Publication ajoutée ");

                return $this->redirectToRoute("acceuil_user_posts");
            }
        } catch (\Exception $e) {
            echo "Exception Found - " . $e->getMessage() . "<br/>";
            dd($e);
        }
        return $this->render('views/content/posts/User/newPost.html.twig', [
            'form' => $form1->createView(),

        ]);
    }


    /**
     * @Route("group/membre/new/{id}/{idU}", name="new_membre_group")
     * @param Request $request
     * @param UserRepository $repository
     * @return Response
     * @throws Exception
     */
    public function ajouterMembreGroup($idU,UserInterface $user,$id,Request $request,EntityManagerInterface $manager, UserRepository $repository,GroupPostRepository $groupPostRepository,GroupPost $groupPost): Response
    {

        $groupPost = $groupPostRepository->find($id);



        $membre=$repository->find($idU);
        $groupPost->addMembre($membre);
        $membre->addGroupPost($groupPost);

        $membres=$groupPost->getMembre();
        $manager->persist($membre);
        $manager->persist($groupPost);
        $manager->flush();
        return $this->redirectToRoute("acceuil_user_posts",['membres'=>$membres]);

    }


    /**
     * @Route("group/membre/quitter/{id}/{idU}", name="quitter_membre_group")
     * @param Request $request
     * @param UserRepository $repository
     * @return Response
     * @throws Exception
     */
    public function QuitterGroup($idU,UserInterface $user,$id,Request $request,EntityManagerInterface $manager, UserRepository $repository,GroupPostRepository $groupPostRepository,GroupPost $groupPost): Response
    {

        $groupPost = $groupPostRepository->find($id);

        $membre=$repository->find($idU);


        $groupPost->removeMembre($membre);



        $manager->persist($groupPost);

        $manager->flush();
        return $this->redirectToRoute("acceuil_user_posts");

    }






    // ************************************* Admin
    /**
     * @Route("admin/group/all", name="groupall")

     * @param Request $request
     * @return Response
     * @throws Exception
     */
    public function afficher_tous_les_Post(GroupPostRepository $repository, Request $request,SerializerInterface $serializer,PaginatorInterface $paginator ): Response
    {

        $totalG=$repository->createQueryBuilder('a')
            // Filter by some parameter if you want
            ->where('a.isValid != 1 ')
            ->select('count(a.id)')
            ->getQuery()
            ->getSingleScalarResult();

        $donnees = $this->getDoctrine()->getRepository(GroupPost::class)->findBy([],[
            'createdAt'=>'desc'
        ]);
        $groups=$paginator->paginate(
            $donnees, // n3adi les donnees
            $request->query->getInt('page',1), // num l page
            10
        );

        //$json=$serializer->serialize($posts,'json',['groups'=>'posts']);
        // dump($json);
        // die;
        return $this->render('views/content/posts/Admin/allGroups.html.twig', ['groups' => $groups,'groupsT'=>$totalG]);
    }

    /**
     * @Route("/admin/delete/group/{id}",name="deletegroup_admin")
     */
    public function supprimer_Group_admin($id)
    {


        $entityManager = $this->getDoctrine()->getManager();

        $group = $entityManager->getRepository(GroupPost::class)->find($id);
        $entityManager->remove($group);
        $this->addFlash('success', 'Groupe bien eté supprimé.');


        $entityManager->flush();
        return $this->redirectToRoute('groupall');

    }




    /**
     * @Route("/admin/group/changevalidite/{id}",name="changevalidite_group")
     */
    public function approuverGroup(GroupPost $groupPost, GroupPostRepository $groupPostRepository)
    {
        $groupPost = $groupPostRepository->changeValiditeG($groupPost);
        //  $this->addFlash('success', 'Publication approuvée');
        // return $this->json(["message"=>"success","value"=>$post->getIsValid()]);
        $this->addFlash('success', 'Groupe bien eté approuvé');
        return $this->redirectToRoute('groupall');

    }

    /**
     * @Route("/group/{id}", name="singleGroup")

     * @param Request $request
     * @return Response
     */
    public function singleGroup($id,GroupPostRepository $repository, Request $request): Response
    {
        $group= $repository->find($id);
        $membres=$group->getMembre();

        return $this->render('views/content/posts/GroupPost/SingleGroup.html.twig', [ 'membres'=>$membres,'group' => $group,
            "user" => $this->getUser()
            ]);
    }

}
