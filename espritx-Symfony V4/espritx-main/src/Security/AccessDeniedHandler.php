<?php

// src/Security/AccessDeniedHandler.php
namespace App\Security;

use Psr\Container\ContainerInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Security\Core\Exception\AccessDeniedException;
use Symfony\Component\Security\Http\Authorization\AccessDeniedHandlerInterface;

class AccessDeniedHandler implements AccessDeniedHandlerInterface
{
  public function __construct(private ContainerInterface $container)
  {
  }

  public function handle(Request $request, AccessDeniedException $accessDeniedException): JsonResponse|Response
  {
    if ($request->isXmlHttpRequest()) {
      return new JsonResponse(array('success' => false, 'message' => $accessDeniedException->getMessage()));
    }

    $content = $this->container->get("twig")->render("bundles/TwigBundle/Exception/error403.html.twig");
    return new Response($content, 403);
  }
}