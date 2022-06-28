<?php

namespace App\Serializer\Normalizer;

use App\Entity\Service;
use App\Entity\Group;
use App\Entity\ServiceRequest;
use Symfony\Component\Serializer\Normalizer\CacheableSupportsMethodInterface;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Vich\UploaderBundle\Templating\Helper\UploaderHelper;

class ServiceNormalizer implements NormalizerInterface, CacheableSupportsMethodInterface
{

    public function __construct(private ObjectNormalizer $normalizer,
                                private UploaderHelper   $helper,
                                private GroupNormalizer $groupNormalizer,
                                private RequestNormalizer $requestNormalizer)
    {
    }

    /**
     * @param Service $service
     * @param $format
     * @param array $context
     * @return array
     * @throws \Symfony\Component\Serializer\Exception\ExceptionInterface
     */
    public function normalize($service, $format = null, array $context = []): array
    {
        return [
            'id' => $service->getId(),
            'Name' => $service->getName(),
            'Responsible' => $this->groupNormalizer->normalize($service->getResponsible()),
            'Recipient' => array_map(fn(Group $g) => $this->groupNormalizer->normalize($g), $service->getRecipient()->toArray()),
            'serviceRequests' => array_map(fn(ServiceRequest $r) => $this->requestNormalizer->normalize($r), $service->getServiceRequests()->toArray()),
        ];
    }

    public function supportsNormalization($data, $format = null): bool
    {
        return $data instanceof \App\Entity\Service;
    }

    public function hasCacheableSupportsMethod(): bool
    {
        return true;
    }
}
