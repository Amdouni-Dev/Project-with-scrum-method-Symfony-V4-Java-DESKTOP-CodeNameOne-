<?php

namespace App\Controller;

use App\Entity\Group;
use App\Form\GroupType;
use Doctrine\ORM\EntityManagerInterface;
use FOS\RestBundle\Controller\Annotations\Route;
use SebastianBergmann\Type\Exception;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\ParamConverter;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Exception\BadRequestHttpException;

/**
 * @Route("/api/group")
 */
class GroupApiController extends AbstractApiController
{
  /**
   * @Route("/", name="group_api_index", methods={"GET"})
   */
  public function index_groups(EntityManagerInterface $entityManager)
  {
    $groups = $entityManager->getRepository(Group::class)->findAll();
    return $this->json($groups);
  }


  /**
   * @Route("/", name="group_api_add", methods={"POST"})
   */
  public function add_group(Request $request, EntityManagerInterface $entityManager)
  {
    $form = $this->buildForm(GroupType::class);
    $request->request->set("members", array_map(static fn($u) => $u["id"], $request->get("members")));
    $form->submit($request->request->all(), false);
    if (!$form->isSubmitted() || !$form->isValid()) {
      return $this->respond($form, Response::HTTP_BAD_REQUEST);
    }
    /** @var Group $group */
    $group = $form->getData();
    $entityManager->persist($group);
    $entityManager->flush();
    return $this->respond($group);
  }

  /**
   * @Route("/{id}", name="group_api_edit", methods={"PATCH"})
   * @ParamConverter("id", class="App\Entity\Group")
   */
  public function edit_group(Request $request, EntityManagerInterface $em, Group $group)
  {

    $editForm = $this->buildForm(GroupType::class, $group);
    $request->request->set("members", array_map(static fn($u) => $u["id"], $request->get("members")));
    $editForm->submit($request->request->all(), false);
    if (!$editForm->isSubmitted() || !$editForm->isValid()) {
      throw new BadRequestHttpException($editForm->getErrors(true));
    }
    /** @var Group $group */
    $group = $editForm->getData();
    $em->flush();
    return $this->respond($group);

  }

  /**
   * @Route("/{id}", name="group_api_delete", methods={"DELETE"})
   * @ParamConverter("id", class="App\Entity\Group")
   */
  public function delete_group(Request $request, EntityManagerInterface $em, Group $user)
  {
    try {
      $em->remove($user);
      $em->flush();
      return $this->json(["status" => true]);
    } catch (\Exception $e) {
      return $this->json(["status" => false, "message" => $e->getMessage()], 500);
    }
  }
}