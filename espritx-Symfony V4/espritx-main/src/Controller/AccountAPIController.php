<?php

namespace App\Controller;

use App\Entity\User;
use App\Form\UserType;
use Doctrine\ORM\EntityManagerInterface;
use FOS\RestBundle\Controller\Annotations\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\ParamConverter;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

/**
 * @Route("/api/profile")
 */
class AccountAPIController extends AbstractApiController
{
  // NEW ACHIEVEMENT UNLOCKED: 3FAT LEVEL GOD.
  // SYMFONY CAN'T READ A FILE IN PATCH AND PUT REQUESTS
  // BECAUSE WHO IN THE FUCK NEEDS TO BE RESTFUL! symfony#36307
  /**
   * @Route("/edit/{id}", name="api_update_profile", methods={"POST"})
   * @ParamConverter("id", class="App\Entity\User")
   */
  public function edit_profile(Request $request, User $user, EntityManagerInterface $em)
  {
    $request->setMethod("PATCH");
    $request->request->replace(json_decode($request->request->get("userProfile"), true));
    $request->files->replace([
      "avatarFile" => ["file" => $request->files->get("avatarFile")]
    ]);
    $request->request->remove("groups");
    $editForm = $this->get('form.factory')->createNamed('', UserType::class, $user, [
      "method" => "PATCH",
    ]);
    $editForm->remove('email');
    $editForm->remove('class');
    $editForm->remove('groups');
    $editForm->remove('individualPermissions');
    $editForm->remove('userStatus');
    $editForm->handleRequest($request);
    if (!$editForm->isSubmitted() || !$editForm->isValid()) {
      return $this->respond($editForm, Response::HTTP_BAD_REQUEST);
    }
    /** @var User $user */
    $user = $editForm->getData();
    $em->flush();
    return $this->respond($user);
  }
}