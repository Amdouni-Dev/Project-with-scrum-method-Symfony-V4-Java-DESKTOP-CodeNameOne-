<?php

namespace App\Controller;

use KnpU\OAuth2ClientBundle\Client\ClientRegistry;
use KnpU\OAuth2ClientBundle\Client\Provider\GoogleClient;
use League\OAuth2\Client\Provider\Exception\IdentityProviderException;
use League\OAuth2\Client\Provider\GoogleUser;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;

class GoogleController extends AbstractApiController
{
  /**
   * @Route("/connect/google", name="connect_google_start")
   */
  public function connectAction(ClientRegistry $clientRegistry) : RedirectResponse
  {
    return $clientRegistry
      ->getClient('google') // key used in config/packages/knpu_oauth2_client.yaml
      ->redirect([
        'profile', 'email' // the scopes you want to access
      ]);
  }

  /**
   * @Route("/connect/google/check", name="connect_google_check")
   */
  public function connectCheckAction(Request $request, ClientRegistry $clientRegistry)
  {
    /** @var GoogleClient $client */
    $client = $clientRegistry->getClient('google');

    try {
      // the exact class depends on which provider you're using
      /** @var GoogleUser $user */
      $user = $client->fetchUser();

      // do something with all this new power!
      // e.g. $name = $user->getFirstName();
      var_dump($user);
      die;
      // ...
    } catch (IdentityProviderException $e) {
      var_dump($e->getMessage());
      die;
    }
  }
}
