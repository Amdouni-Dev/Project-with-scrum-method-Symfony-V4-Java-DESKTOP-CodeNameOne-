<?php

namespace App\Controller;

use App\Entity\DeviceAuthorization;
use App\Entity\Group;
use App\Entity\Permission;
use App\Entity\User;
use App\Form\ConversationType;
use App\Form\UserType;
use App\Repository\GroupRepository;
use App\Repository\UserRepository;
use App\Serializer\Normalizer\UserNormalizer;
use Doctrine\ORM\EntityManagerInterface;
use FOS\RestBundle\Controller\Annotations\Route;
use Lexik\Bundle\JWTAuthenticationBundle\Event\AuthenticationSuccessEvent;
use Lexik\Bundle\JWTAuthenticationBundle\Events;
use Lexik\Bundle\JWTAuthenticationBundle\Response\JWTAuthenticationSuccessResponse;
use Lexik\Bundle\JWTAuthenticationBundle\Services\JWTTokenManagerInterface;
use Psr\Log\LoggerInterface;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\ParamConverter;
use Symfony\Component\EventDispatcher\EventDispatcherInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\UrlHelper;
use Symfony\Component\HttpKernel\Exception\BadRequestHttpException;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;

/**
 * @Route("/api/user")
 */
class UserApiController extends AbstractApiController
{
  /**
   * @Route("/", name="user_api_index", methods={"GET"})
   */
  public function index_users(EntityManagerInterface $entityManager)
  {
    $users = $entityManager->getRepository(User::class)->findAll();
    return $this->json($users);
  }

  /**
   * @Route("/", name="user_api_add", methods={"POST"})
   */
  public function addUser(Request $request, EntityManagerInterface $entityManager, GroupRepository $groupRepository)
  {
    $form = $this->buildForm(UserType::class);
    $form->submit($request->request->all(), false);
    if (!$form->isSubmitted() || !$form->isValid()) {
      return $this->respond($form, Response::HTTP_BAD_REQUEST);
    }

    /** @var User $user */
    $user = $form->getData();
    $user->addGroup($groupRepository->getDefaultGroup());
    $entityManager->persist($user);
    $entityManager->flush();
    return $this->respond($user);
  }

  /**
   * @Route("/{id}", name="user_api_edit", methods={"PATCH"})
   * @ParamConverter("id", class="App\Entity\User")
   */
  public function editUser(Request $request, EntityManagerInterface $em, User $user, UrlHelper $helper, LoggerInterface $logger)
  {
    //$request->request->set("groups", array_map(static fn($g) => $g["id"], $request->request->get("groups")));
    //$request->request->set("avatarFile", $helper->getRelativePath($request->get("avatarFile")));
    $editForm = $this->buildForm(UserType::class, $user);
    $editForm->remove("avatarFile");
    $editForm->remove("groups");
    $editForm->submit($request->request->all(), false);
    if (!$editForm->isSubmitted() || !$editForm->isValid()) {
      $errors = $editForm->getErrors(true, true);
      $errorString = "";
      foreach ($errors as $error) {
        $errorString .= $error->getMessage() . "\n";
      }
      $logger->error($errorString);
      throw new BadRequestHttpException($errorString);
    }
    /** @var User $user */
    $user = $editForm->getData();
    $em->flush();
    return $this->respond($user);
  }

  /**
   * @Route("/{id}", name="user_api_delete", methods={"DELETE"})
   * @ParamConverter("id", class="App\Entity\User")
   */
  public function deleteUser(Request $request, EntityManagerInterface $em, User $user)
  {
    try {
      $em->remove($user);
      $em->flush();
      return $this->json(["status" => true]);
    } catch (\Exception $e) {
      return $this->json(["status" => false, "message" => $e->getMessage()], 500);
    }
  }

  /**
   * @Route("/fetch_my_data", name="user_fetch_authenticated_data", methods={"GET"})
   */
  public function return_current_user(Request $request)
  {
    $user = $this->getUser();
    return $this->json($user);
  }

  /**
   * @Route("/autocomplete_emails", name="user_autocomplete_email", methods={"POST"})
   */
  public function user_autocomplete_email(Request $request, UserRepository $userRepository)
  {
    $beacon = $request->get("email");
    $results = $userRepository->searchUsersByEmail($beacon);
    $filtered = array_column($results, 'email');
    return $this->json(["values" => $filtered]);
  }

  /**
   * @Route("/fetch_by_email", name="fetch_user_by_email", methods={"GET"})
   */
  public function fetch_user(Request $request, UserRepository $userRepository, UserNormalizer $userNormalizer)
  {
    $user = $userRepository->findOneBy(["email" => $request->get("email")]);
    if ($user === null) {
      throw new NotFoundHttpException("Could not find user with email " . $request->get("email"));
    }
    return $this->json($userNormalizer->normalize($user));
  }

  /**
   * @Route("/oauth_mobile_callback", name="oauth_mobile_callback", methods={"GET"})
   */
  public function get_jwt_for_google_oauth(Request $request, JWTTokenManagerInterface $jwtManager, EventDispatcherInterface $dispatcher)
  {
    $user = $this->getUser();
    $jwt = $jwtManager->create($user);
    return $this->json(["token" => $jwt]);
  }

}