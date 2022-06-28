<?php

namespace App\Controller;

use App\Entity\BlogPost;
use App\Entity\User;

use App\Form\BlogPostType;
use App\Repository\BlogPostRepository;
use App\Repository\PostCategoryRepository;
use App\Repository\UserRepository;
use Knp\Component\Pager\PaginatorInterface;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Method;
use Symfony\Component\Config\Definition\Exception\Exception;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;

/** @Route("/forum") */
class BlogPostController extends AbstractController
{
  /**
   * @Route("/blog/post", name="blog_post")
   */
  public function afficher_blog(BlogPostRepository $blogPostRepository)
  {
    $blogs = $blogPostRepository->findAll();
//        $blogs=$paginator->paginate(
//            $data,
//            $request->query->getInt('page',1), // num l page
//            10);
    $pageConfigs = [
      'mainLayoutType' => 'horizontal',
      'pageHeader' => false
    ];

    return $this->render('blog_post/index.html.twig', [
      'blogs' => $blogs,
      'controller_name' => 'BlogPostController',
      'pageConfigs' => $pageConfigs
    ]);
  }

  /**
   * @Route("/blogpost/new", name="newblogpost")
   * @param Request $request
   * @param PostCategoryRepository $repository
   * @return Response
   */
  public function newBlogPost(Request $request, PostCategoryRepository $repository): Response
  {
    $blogpost = new BlogPost();
    $form = $this->createForm(BlogPostType::class, $blogpost);
    $form->handleRequest($request);

    $em = $this->getDoctrine()->getManager();
    if ($form->isSubmitted() && $form->isValid()) {
      $file = $form->get('image')->getData();
      $fileName = md5(uniqid()) . '.' . $file->guessExtension();
      $file->move(
        $this->getParameter('imagesPost_directory'),
        $fileName
      );
      $blogpost->setImage($fileName);
      $em->persist($blogpost);
      $em->flush();
      return $this->redirectToRoute("blog_post");
    }
    $pageConfigs = [
      'mainLayoutType' => 'horizontal',
      'pageHeader' => false
    ];

    return $this->render('blog_post/formblogpost.html.twig', [
      'form' => $form->createView(),
      'pageConfigs' => $pageConfigs
    ]);
  }

  /**
   * @Route("/delete/blogpost/{id}",name="deleteblog_post")
   */
  public function supprimer_blog_post($id)
  {
    $entityManager = $this->getDoctrine()->getManager();
    $blogpost = $entityManager->getRepository(BlogPost::class)->find($id);
    $entityManager->remove($blogpost);
    $entityManager->flush();
    return $this->redirectToRoute('blog_post');
  }

  /**
   * @Route("blogpost/Update/{id}",name="update_blog")
   */
  function Update_blog_post(BlogPostRepository $repository, $id, Request $request)
  {
    $blogpost = $repository->find($id);
    $form = $this->createForm(BlogPostType::class, $blogpost);
    $form->add('Update', SubmitType::class);
    $form->handleRequest($request);
    if ($form->isSubmitted() && $form->isValid()) {
      $em = $this->getDoctrine()->getManager();
      $em->flush();
      return $this->redirectToRoute("blog_post");
    }
    return $this->render('blog_post/Update.html.twig',
      [
        'f' => $form->createView(),

      ]);
  }

  /**
   * @Route("/searchcategory", name="ajaxcategory")
   */
  public function SearchCat(Request $request)
  {
    $repository = $this->getDoctrine()->getRepository(BlogPost::class);
    $requestString = $request->get('searchValue');
    $blogs = $repository->SearchCat($requestString);
    return $this->render('blog_post/ajaxblog.html.twig', [
      "blogs" => $blogs,
    ]);
  }

  /**
   * @Route("/add_post_blog_api" , name="add_blog_post_api")
   * @Method("POST")
   */
  public function ajouterBlogPost(Request $request){

      $blogpost = new BlogPost();
      $slug = $request->query->get("slug");
      $body = $request->query->get("body");
     // $post_category = $request->query->get("post_category");
      $em = $this->getDoctrine()->getManager();

      $blogpost->setSlug($slug);
      $blogpost->setBody($body);
     // $blogpost->setPostCategory("post_category");

      $em->persist($blogpost);
      $em->flush();
      $serializer = new Serializer([new ObjectNormalizer()]);
      $formatted = $serializer->normalize($blogpost);
      return new JsonResponse($formatted );

  }

  /**
   * @Route("/show_blog_post_api" , name="show_blogpost_api")
   */
    public function showforum(){
        $blogpost = $this->getDoctrine()->getManager()->getRepository(BlogPost::class )->findAll();
        $serializer = new Serializer([new ObjectNormalizer()]);
        $formatted = $serializer->normalize($blogpost);
        return new JsonResponse($formatted);
    }

    /**
     * @Route("/delete_blog_post_api/{id}",name="delete_blog_post_api")
     */
    public function deleteBlogPost(Request $request){
        $id = $request->get("id");
         $em =$this->getDoctrine()->getManager();
         $blogpost= $em->getRepository(BlogPost::class)->find($id);
         if($blogpost!=null){
             $em->remove($blogpost);
             $em->flush();

             $serialize = new Serializer([new ObjectNormalizer()]);
             $formatted = $serialize->normalize("Forum Post a ete supprimer avec success");
         }

         return new JsonResponse("id post invalide");
    }

    /**
     * @Route("/update_blog_post_api/{id}",name="update_blog_post_api")
     * @Method("PUT")
     */
    public function modifierBlogPost(Request $request){
        $em = $this->getDoctrine()->getManager();
        $blogpost=$this->getDoctrine()->getManager()
            ->getRepository(BlogPost::class)->find($request->get("id"));

        $blogpost->setSlug($request->get("slug"));
        $blogpost->setBody($request->get("body"));
        $blogpost->setPostCategory($request->get("post_category"));

        $em->persist($blogpost);
        $em->flush();
        $serializer = new Serializer([new ObjectNormalizer()]);
        $formatted = $serializer->normalize($blogpost);

        return new JsonResponse("Forum Post a ete bien modifiee avec success");

    }


}




