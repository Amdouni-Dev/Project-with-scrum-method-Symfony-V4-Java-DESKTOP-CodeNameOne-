<?php

namespace App\Serializer\Normalizer;

use App\Entity\Group;
use App\Entity\Permission;
use App\Entity\User;
use Symfony\Bridge\Twig\Extension\HttpFoundationExtension;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Symfony\Component\Serializer\Normalizer\CacheableSupportsMethodInterface;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Vich\UploaderBundle\Templating\Helper\UploaderHelper;

class UserNormalizer implements NormalizerInterface, CacheableSupportsMethodInterface
{

  public function __construct(private UploaderHelper   $helper,
                              private GroupNormalizer $groupNormalizer,
                              private HttpFoundationExtension $httpFoundationExtension)
  {
  }

  /**
   * @param User $user
   * @param $format
   * @param array $context
   * @return array
   * @throws \Symfony\Component\Serializer\Exception\ExceptionInterface
   */
  public function normalize($user, $format = null, array $context = []): array
  {
    return [
      'id' => $user->getId(),
      'first_name' => $user->getFirstName(),
      'last_name' => $user->getLastName(),
      'email' => $user->getEmail(),
      'class' => $user->getClass(),
      'userStatus' => $user->getUserStatus(),
      'groups' => array_map(fn(Group $g) => $this->groupNormalizer->normalize($g), $user->getGroups()->toArray()),
      'individualPermissions' => array_map(static fn(Permission $g) => $g->getTitle(), $user->getIndividualPermissions()->toArray()),
      'avatarFile' => ($user->getAvatar()?->getName() !== null) ? $this->httpFoundationExtension->generateAbsoluteUrl($this->helper->asset($user, "avatarFile")): null,
      'identityType' => $user->getIdentityType(),
      'identityDocumentNumber' => $user->getIdentityDocumentNumber(),
      'phoneNumber' => $user->getPhoneNumber(),
      'plainPassword' => $user->getPlainPassword(),
      'about' => $user->getAbout()
    ];
  }

  public function supportsNormalization($data, $format = null): bool
  {
    return $data instanceof \App\Entity\User;
  }

  public function hasCacheableSupportsMethod(): bool
  {
    return true;
  }
}
