<?php

namespace App\Entity;

use App\Repository\ServiceRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use http\Env\Response;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Entity;
use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @ORM\Entity(repositoryClass=ServiceRepository::class)
 */
class Service
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;

    /**
     * @ORM\Column(type="string", length=255, unique=true)
     * @Assert\NotBlank(message="Veuillez ajouter un nom de service!")
     * @Groups("Service")
     * @Groups ("Request")
     */
    private $Name;

    /**
     * @ORM\ManyToOne(targetEntity=Group::class, inversedBy="provided_services")
     * @ORM\JoinColumn(nullable=false)
     * @Assert\NotNull(message="Veuillez sélectionnez un responsable pour ce service!")
     * @Groups("Service")
     */
    private $Responsible;

    /**
     * @ORM\ManyToMany(targetEntity=Group::class, inversedBy="enjoyable_services")
     * @Assert\Count(min="1", minMessage="Sélectionnez au moins un bénéficaire de ce service!")
     * @Groups("Service")
     */
    private $Recipient;

    /**
     * @ORM\OneToMany(targetEntity=ServiceRequest::class, mappedBy="Type" ,cascade={"remove"})
     * @Groups("Service")
     */
    private $serviceRequests;

    /**
     * @ORM\OneToMany(targetEntity=Fields::class, mappedBy="service", orphanRemoval=true, cascade={"persist"})
     */
    private $Other_Fields;

    public function __construct()
    {
        $this->Recipient = new ArrayCollection();
        $this->serviceRequests = new ArrayCollection();
        $this->Other_Fields = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getName(): ?string
    {
        return $this->Name;
    }

    public function setName(string $Name): self
    {
        $this->Name = $Name;

        return $this;
    }

    public function getResponsible(): ?Group
    {
        return $this->Responsible;
    }

    public function setResponsible(?Group $Responsible): self
    {
        $this->Responsible = $Responsible;

        return $this;
    }

    /**
     * @return Collection|Group[]
     */
    public function getRecipient(): Collection
    {
        return $this->Recipient;
    }

    public function addRecipient(Group $recipient): self
    {
        if (!$this->Recipient->contains($recipient)) {
            $this->Recipient[] = $recipient;
        }

        return $this;
    }

    public function removeRecipient(Group $recipient): self
    {
        $this->Recipient->removeElement($recipient);

        return $this;
    }

    public function resetRecipient(): self
    {
        $this->Recipient= new ArrayCollection();

        return $this;
    }

    /**
     * @return Collection|ServiceRequest[]
     */
    public function getServiceRequests(): Collection
    {
        return $this->serviceRequests;
    }

    public function addServiceRequest(ServiceRequest $serviceRequest): self
    {
        if (!$this->serviceRequests->contains($serviceRequest)) {
            $this->serviceRequests[] = $serviceRequest;
            $serviceRequest->setType($this);
        }

        return $this;
    }

    public function removeServiceRequest(ServiceRequest $serviceRequest): self
    {
        if ($this->serviceRequests->removeElement($serviceRequest)) {
            // set the owning side to null (unless already changed)
            if ($serviceRequest->getType() === $this) {
                $serviceRequest->setType(null);
            }
        }

        return $this;
    }


    public function __toString(): string
    {
        return $this->getName();
    }

    /**
     * @return Collection<int, Fields>
     */
    public function getOtherFields(): Collection
    {
        return $this->Other_Fields;
    }

    public function addOtherField(Fields $otherField): self
    {
        if (!$this->Other_Fields->contains($otherField)) {
            $this->Other_Fields[] = $otherField;
            $otherField->setService($this);
        }

        return $this;
    }

    public function removeOtherField(Fields $otherField): self
    {
        if ($this->Other_Fields->removeElement($otherField)) {
            // set the owning side to null (unless already changed)
            if ($otherField->getService() === $this) {
                $otherField->setService(null);
            }
        }

        return $this;
    }

    public function addField(Fields $f):void
    {
        $f->setService($this);
        $this->Other_Fields->add($f);
    }

    public function removeField(Fields $f):void
    {
        $this->Other_Fields->removeElement($f);
    }
}
