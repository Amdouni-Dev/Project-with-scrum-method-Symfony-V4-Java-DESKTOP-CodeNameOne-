<?php

namespace App\Controller;

use App\Entity\DeviceAuthorization;
use App\Repository\DeviceAuthorizationRepository;
use Doctrine\ORM\EntityManagerInterface;
use Lexik\Bundle\JWTAuthenticationBundle\Services\JWTTokenManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\Routing\Annotation\Route;

/**
 * @Route("/authenticated-device-authorization")
 */
class AuthenticatedDeviceAuthorizationController extends AbstractController
{
  /**
   * @Route("/mobile/{code}", name="authorize_mobile_client", methods={"GET"})
   */
  public function authenticate_mobile_device($code, DeviceAuthorizationRepository $repository, EntityManagerInterface $entityManager, JWTTokenManagerInterface $jwtManager)
  {
    $code = $repository->findOneBy(['device_code' => $code]);
    if (!$code) {
      return new NotFoundHttpException();
    }
    $code->setUser($this->getUser());
    $entityManager->persist($code);
    $entityManager->flush();
    return new RedirectResponse("espritxandroid://token-" . $jwtManager->create($this->getUser()));
  }


  /**
   * @Route("/{device_code}", name="device_authenticate", methods={"GET"})
   */
  public function authenticate_device($device_code, DeviceAuthorizationRepository $repository, EntityManagerInterface $entityManager)
  {
    $code = $repository->findOneBy(['device_code' => $device_code]);
    if (!$code) {
      return new NotFoundHttpException();
    }
    $code->setUser($this->getUser());
    $entityManager->persist($code);
    $entityManager->flush();
    return new Response("You have successfully authenticated your device. You can close this tab now.", Response::HTTP_OK);
  }
}