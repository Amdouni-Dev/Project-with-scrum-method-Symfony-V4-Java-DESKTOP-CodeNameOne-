<?php

namespace App\Serializer\Normalizer;

use App\Entity\Channel;
use App\Entity\Message;
use App\Entity\Permission;
use App\Entity\Service;
use App\Entity\User;
use Symfony\Bridge\Twig\Extension\HttpFoundationExtension;
use Symfony\Component\Serializer\Normalizer\CacheableSupportsMethodInterface;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Vich\UploaderBundle\Templating\Helper\UploaderHelper;

class MessageNormalizer implements NormalizerInterface, CacheableSupportsMethodInterface
{
  public function __construct(private HttpFoundationExtension $httpFoundationExtension, private UploaderHelper $helper,)
  {
  }

  /**
   * @param Message $object
   * @param $format
   * @param array $context
   * @return array
   * @throws \Symfony\Component\Serializer\Exception\ExceptionInterface
   */
  public function normalize($object, $format = null, array $context = []): array
  {
    return [
      'id' => $object->getId(),
      'content' => $object->getContent(),
      'author' => [
        'id' => $object->getAuthor()->getId(),
        'first_name' => $object->getAuthor()->getFirstName(),
        'last_name' => $object->getAuthor()->getLastName(),
        'email' => $object->getAuthor()->getEmail(),
        'class' => $object->getAuthor()->getClass(),
        'userStatus' => $object->getAuthor()->getUserStatus(),
        'avatarFile' => ($object->getAuthor()->getAvatar()?->getName() !== null) ? $this->httpFoundationExtension->generateAbsoluteUrl($this->helper->asset($object->getAuthor(), "avatarFile")) : null,
        'identityType' => $object->getAuthor()->getIdentityType(),
        'identityDocumentNumber' => $object->getAuthor()->getIdentityDocumentNumber(),
        'phoneNumber' => $object->getAuthor()->getPhoneNumber(),
        'plainPassword' => $object->getAuthor()->getPlainPassword()
      ], 'Conversationid' => $object->getChannel()->getId(),
    ];
  }

  public function supportsNormalization($data, $format = null): bool
  {
    return $data instanceof \App\Entity\Message;
  }

  public function hasCacheableSupportsMethod(): bool
  {
    return true;
  }
}
