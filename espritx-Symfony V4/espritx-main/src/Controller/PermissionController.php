<?php

namespace App\Controller;

use App\Entity\Permission;
use App\Enum\AccessType;
use App\Form\GroupType;
use App\Form\PermissionType;
use App\Form\UserType;
use App\Repository\PermissionRepository;
use Doctrine\ORM\EntityManagerInterface;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Tetranz\Select2EntityBundle\Service\AutocompleteService;

/**
 * @Route("/permission")
 */
class PermissionController extends AbstractController
{
  /**
   * @Route("/", name="permission_index", methods={"GET"})
   */
  public function index(EntityManagerInterface $em, PaginatorInterface $paginator, Request $request): Response
  {
    $this->denyAccessUnlessGranted(AccessType::READ, Permission::class);
    $dql = <<<DQL
    select p from App\Entity\Permission p 
    DQL;
    $query = $em->createQuery($dql);

    $pagination = $paginator->paginate(
      $query,
      $request->query->getInt('page', 1),
      10
    );

    return $this->render('views/content/apps/rolesPermission/permission/app-access-permission.html.twig', [
      'breadcrumbs' => [
        ["name" => "Management"],
        ["name" => "Permissions", "link" => "permission_index"],
      ],
      'pagination' => $pagination,
    ]);
  }

  /**
   * @param Request $request
   * @param AutocompleteService $autocompleteService
   * @return JsonResponse
   * @Route("/autocomplete/group_form", name="ajax_autocomplete_permissions")
   */
  public function autocompleteAction(Request $request, AutocompleteService $autocompleteService): JsonResponse
  {
    $result = $autocompleteService->getAutocompleteResults($request, GroupType::class);
    return new JsonResponse($result);
  }
  /**
   * @param Request $request
   * @param AutocompleteService $autocompleteService
   * @return JsonResponse
   * @Route("/autocomplete/user_form", name="ajax_autocomplete_permissions_user_form")
   */
  public function ajax_autocomplete_permissions_user_form(Request $request, AutocompleteService $autocompleteService): JsonResponse
  {
    $result = $autocompleteService->getAutocompleteResults($request, UserType::class);
    return new JsonResponse($result);
  }

  /**
   * @Route("/create", name="permission_create", methods={"GET", "POST"})
   */
  public function create(Request $request, EntityManagerInterface $entityManager): Response
  {
    $this->denyAccessUnlessGranted(AccessType::CREATE, Permission::class);

    $permission = new Permission();
    $form = $this->createForm(PermissionType::class, $permission);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
      $entityManager->persist($permission);
      $entityManager->flush();
      return $this->redirectToRoute('permission_index', [], Response::HTTP_CREATED);
    }

    return $this->render('views/content/apps/rolesPermission/permission/app-access-permission-form.html.twig', [
      'permission' => $permission,
      'form' => $form->createView(),
      'breadcrumbs' => [
        ["name" => "Management"],
        ["name" => "Permissions", "link" => "permission_index"],
        ["name" => "Create"],
      ]
    ]);
  }

  /**
   * @Route("/{id}", name="permission_show", methods={"GET"})
   */
  public function show(Permission $permission): Response
  {
    $this->denyAccessUnlessGranted(AccessType::READ, $permission);

    return $this->render('permission/show.html.twig', [
      'permission' => $permission,
    ]);
  }

  /**
   * @Route("/{id}/edit", name="permission_edit", methods={"GET", "POST"})
   */
  public function edit(Request $request, Permission $permission, EntityManagerInterface $entityManager): Response
  {
    $this->denyAccessUnlessGranted(AccessType::READ, $permission);

    $form = $this->createForm(PermissionType::class, $permission);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
      $entityManager->persist($permission);
      $entityManager->flush();
      return $this->redirectToRoute('permission_index', [], Response::HTTP_SEE_OTHER);
    }
    return $this->render('views/content/apps/rolesPermission/permission/app-access-permission-form.html.twig', [
      'permission' => $permission,
      'breadcrumbs' => [
        ["name" => "Management"],
        ["name" => "Permissions", "link" => "permission_index"],
        ["name" => "Edit"],
      ],
      'form' => $form->createView(),
    ]);
  }

  /**
   * @Route("/{id}/delete", name="permission_delete", methods={"GET"})
   */
  public function delete(Request $request, Permission $permission, EntityManagerInterface $entityManager): Response
  {
    $this->denyAccessUnlessGranted(AccessType::DELETE, $permission);
    if ($this->isCsrfTokenValid('delete-perm' . $permission->getId(), $request->query->get('_token'))) {
      $entityManager->remove($permission);
      $entityManager->flush();
    }
    return $this->redirectToRoute('permission_index', [], Response::HTTP_SEE_OTHER);
  }
}
