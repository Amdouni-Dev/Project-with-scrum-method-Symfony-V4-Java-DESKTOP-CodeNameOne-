<?php

namespace App\Entity;

use App\Repository\DeviceAuthorizationRepository;
use Doctrine\ORM\Mapping as ORM;

/**
 * @ORM\Entity(repositoryClass=DeviceAuthorizationRepository::class)
 */
class DeviceAuthorization
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;

    /**
     * @ORM\Column(type="string", length=255)
     */
    private $device_code;

    /**
     * @ORM\ManyToOne (targetEntity=User::class, cascade={"persist", "remove"})
     */
    private $user;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDeviceCode(): ?string
    {
        return $this->device_code;
    }

    public function setDeviceCode(string $device_code): self
    {
        $this->device_code = $device_code;

        return $this;
    }

    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(?User $user): self
    {
        $this->user = $user;

        return $this;
    }
}
