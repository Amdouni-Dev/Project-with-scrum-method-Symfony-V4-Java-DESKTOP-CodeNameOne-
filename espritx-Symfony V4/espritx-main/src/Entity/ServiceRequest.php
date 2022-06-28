<?php

namespace App\Entity;

use App\Repository\ServiceRequestRepository;
use Doctrine\ORM\Mapping as ORM;
use Gedmo\Timestampable\Traits\TimestampableEntity;
use Symfony\Component\HttpFoundation\File\File;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Validator\Constraints as Assert;
use Vich\UploaderBundle\Entity\File as EmbeddedFile;
use Vich\UploaderBundle\Mapping\Annotation as Vich;

/**
 * @ORM\Entity(repositoryClass=ServiceRequestRepository::class)
 * @Vich\Uploadable
 */
class ServiceRequest
{
    use TimestampableEntity;

    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;

    /**
     * @ORM\Column(type="datetime_immutable", nullable=true)
     */
    private $RespondedAt = null;

    /**
     * @ORM\Column(type="string", length=255, unique=true)
     * @Assert\NotNull (message="Ce champ ne peut pas être nul")
     * @Groups ("Request")
     */
    private $Title;

    /**
     * @ORM\Column(type="string", length=255, nullable=true)
     * @Groups ("Request")
     */
    private $Description;

    /**
     * @ORM\ManyToOne(targetEntity=Service::class, inversedBy="serviceRequests")
     * @ORM\JoinColumn(nullable=false)
     * @Groups ("Request")
     */
    private $Type;

    /**
     * @ORM\Column(type="string", length=255, nullable=true)
     * @Assert\Email(message="Veuillez introduire un email valide",
     * normalizer="trim")
     */
    private $Email;

    /**
     * @ORM\Embedded(class="Vich\UploaderBundle\Entity\File")
     */
    private $Picture;

    /**
     * @ORM\Embedded(class="Vich\UploaderBundle\Entity\File")
     */
    private ?EmbeddedFile $Attachements;

    /**
     * @ORM\Column(type="string", length=255, nullable=true)
     * @Groups ("Request")
     */
    private $Status = "unseen";

    /**
     * @ORM\Column(type="string", length=255, nullable=true)
     */
    private $RequestResponse;

    /**
     * @ORM\ManyToOne(targetEntity=User::class, inversedBy="serviceRequests")
     * @ORM\JoinColumn(nullable=true)
     * @Groups ("Service")
     * @Groups ("Request")
     */
    private $Requester;

    /**
     * @Vich\UploadableField(
     *   mapping="requestfile_file",
     *   fileNameProperty="Attachements.name"
     * )
     * @var File|null
     * @Assert\File(
     *     maxSize = "10M",
     *     mimeTypes = {"application/pdf", "application/x-pdf"},
     *     mimeTypesMessage = "Please upload a valid PDF"
     * )
     */
    private ?File $AttachementsFile = null;

    /**
     * @Vich\UploadableField(
     *   mapping="requestimage_image",
     *   fileNameProperty="Picture.name"
     * )
     * @var File|null
     * @Assert\File(
     *     maxSize = "5M"
     * )
     */
    private ?File $PictureFile = null;

    /**
     * ServiceRequest constructor.
     */
    public function __construct()
    {
        $this->Attachements = new EmbeddedFile();
        $this->Picture = new EmbeddedFile();
    }

    public function getAttachementsFile(): ?File
    {
        return $this->AttachementsFile;
    }

    public function setAttachementsFile(File|UploadedFile|null $Attachements = null): static
    {
        $this->AttachementsFile = $Attachements;
        if ($Attachements !== null) {
        $this->updatedAt = new \DateTimeImmutable();
        }
        return $this;
    }

    public function getPictureFile(): ?File
    {
        return $this->PictureFile;
    }

    public function setPictureFile(File|UploadedFile|null $Picture = null): static
    {
        $this->PictureFile = $Picture;
        if ($Picture !== null) {
        $this->updatedAt = new \DateTimeImmutable();
        }
        return $this;
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getRespondedAt(): ?\DateTimeImmutable
    {
        return $this->RespondedAt;
    }

    public function setRespondedAt(?\DateTimeImmutable $RespondedAt): self
    {
        $this->RespondedAt = $RespondedAt;

        return $this;
    }

    public function getTitle(): ?string
    {
        return $this->Title;
    }

    public function setTitle(string $Title): self
    {
        $this->Title = $Title;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->Description;
    }

    public function setDescription(string $Description): self
    {
        $this->Description = $Description;

        return $this;
    }

    public function getType(): ?Service
    {
        return $this->Type;
    }

    public function setType(?Service $Type): self
    {
        $this->Type = $Type;

        return $this;
    }

    public function getEmail(): ?string
    {
        return $this->Email;
    }

    public function setEmail(string $Email): self
    {
        $this->Email = $Email;

        return $this;
    }

    public function getPicture(): ?EmbeddedFile
    {
        return $this->Picture;
    }

    public function setPicture(?EmbeddedFile $Picture): static
    {
        $this->Picture = $Picture;

        return $this;
    }

    public function getAttachements(): ?EmbeddedFile
    {
        return $this->Attachements;
    }

    public function setAttachements(?EmbeddedFile $Attachements): static
    {
        $this->Attachements = $Attachements;

        return $this;
    }

    public function getStatus(): ?string
    {
        return $this->Status;
    }

    public function setStatus(string $Status): self
    {
        $this->Status = $Status;

        return $this;
    }

    public function getRequestResponse(): ?string
    {
        return $this->RequestResponse;
    }

    public function setRequestResponse(?string $RequestResponse): self
    {
        $this->RequestResponse = $RequestResponse;

        return $this;
    }

    public function getRequester(): ?User
    {
        return $this->Requester;
    }

    public function setRequester(?User $Requester): self
    {
        $this->Requester = $Requester;

        return $this;
    }
}
