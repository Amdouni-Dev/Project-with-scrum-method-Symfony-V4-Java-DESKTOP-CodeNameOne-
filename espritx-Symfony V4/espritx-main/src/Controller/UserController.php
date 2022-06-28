<?php

namespace App\Controller;

use App\Entity\User;
use App\Enum\AccessType;
use App\Form\ChannelType;
use App\Form\GroupType;
use App\Form\PermissionType;
use App\Form\UserType;
use App\Repository\UserRepository;
use Doctrine\ORM\EntityManagerInterface;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Tetranz\Select2EntityBundle\Service\AutocompleteService;

/** @Route("/user") */
class UserController extends AbstractController
{
  /**
   * @Route("/", name="user_index", methods={"GET"})
   */
  public function all(EntityManagerInterface $em,
                      PaginatorInterface     $paginator,
                      Request                $request,
                      UserRepository         $userRepository): Response
  {
    //$this->denyAccessUnlessGranted(AccessType::READ, User::class);
    $dql = <<<DQL
    select u from App\Entity\User u 
    DQL;
    $query = $em->createQuery($dql);
    $q = $userRepository->getCountByStatus();
    $pagination = $paginator->paginate(
      $query,
      $request->query->getInt('page', 1),
      10
    );

    return $this->render('views/content/apps/user/app-user-list.html.twig', [
      'breadcrumbs' => [
        ["name" => "Management"],
        ["name" => "Users", "link" => "user_index"],
      ],
      'statusStatistics' => $q,
      'pagination' => $pagination,
    ]);
  }

  /**
   * @Route("/create", name="user_create", methods={"GET", "POST"})
   */
  public function create(Request $request, EntityManagerInterface $entityManager): Response
  {
    //$this->isGranted(AccessType::CREATE, User::class);
    $user = new User();
    $form = $this->createForm(UserType::class, $user);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
      $entityManager->persist($user);
      $entityManager->flush();
      return $this->redirectToRoute('user_index', [], Response::HTTP_SEE_OTHER);
    }

    return $this->render('views/content/apps/user/app-user-form.html.twig', [
      'user' => $user,
      'breadcrumbs' => [
        ["name" => "Management"],
        ["name" => "Users", "link" => "user_index"],
        ["name" => "Create"],
      ],
      'form' => $form->createView(),
    ]);
  }

  /**
   * @Route("/{id}", name="user_show", methods={"GET"})
   */
  public function show(User $user): Response
  {
    //$this->isGranted(AccessType::READ, User::class);

    return $this->render('user/show.html.twig', [
      'user' => $user,
    ]);
  }

  /**
   * @Route("/update/{id}", name="user_edit", methods={"GET", "POST"})
   */
  public function update(Request $request, User $user, EntityManagerInterface $entityManager): Response
  {
    //$this->isGranted(AccessType::EDIT, User::class);

    $form = $this->createForm(UserType::class, $user);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
      $entityManager->flush();

      return $this->redirectToRoute('user_index', [], Response::HTTP_SEE_OTHER);
    }

    return $this->render('views/content/apps/user/app-user-form.html.twig', [
      'user' => $user,
      'breadcrumbs' => [
        ["name" => "Management"],
        ["name" => "Users", "link" => "user_index"],
        ["name" => "Edit"],
      ],
      'form' => $form->createView(),
    ]);
  }

  /**
   * @Route("/delete/{id}", name="user_delete", methods={"GET"})
   */
  public function delete(Request $request, User $user, EntityManagerInterface $entityManager): Response
  {
    //$this->isGranted(AccessType::DELETE, User::class);
    if ($this->isCsrfTokenValid('delete' . $user->getId(), $request->query->get('_token'))) {
      $entityManager->remove($user);
      $entityManager->flush();
    }

    return $this->redirectToRoute('user_index', [], Response::HTTP_SEE_OTHER);
  }


  /**
   * @param Request $request
   * @param AutocompleteService $autocompleteService
   * @return JsonResponse
   * @Route("/autocomplete/permission_form", name="ajax_autocomplete_users_permission_form")
   */
  public function ajax_autocomplete_users_permission_form(Request $request, AutocompleteService $autocompleteService): JsonResponse
  {
    $result = $autocompleteService->getAutocompleteResults($request, PermissionType::class);
    return new JsonResponse($result);
  }

    /**
     * @param Request $request
     * @param AutocompleteService $autocompleteService
     * @return JsonResponse
     * @Route("/autocomplete/chat", name="ajax_autocomplete_chat_permission_form")
     */
    public function ajax_autocomplete_chat_form(Request $request, AutocompleteService $autocompleteService): JsonResponse
    {
        $result = $autocompleteService->getAutocompleteResults($request, ChannelType::class);
        return new JsonResponse($result);
    }

  /**
   * @param Request $request
   * @param AutocompleteService $autocompleteService
   * @return JsonResponse
   * @Route("/autocomplete/group_form", name="ajax_autocomplete_users_group_form")
   */
  public function ajax_autocomplete_users_group_form(Request $request, AutocompleteService $autocompleteService): JsonResponse
  {
    $result = $autocompleteService->getAutocompleteResults($request, GroupType::class);
    return new JsonResponse($result);
  }
}
