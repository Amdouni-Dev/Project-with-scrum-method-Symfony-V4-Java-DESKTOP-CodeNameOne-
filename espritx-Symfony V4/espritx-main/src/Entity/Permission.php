<?php

namespace App\Entity;

use App\Enum\AccessType;
use App\Repository\PermissionRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use JetBrains\PhpStorm\Pure;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @ORM\Entity(repositoryClass=PermissionRepository::class)
 */
class Permission
{
  public function __construct()
  {
    $this->groups = new ArrayCollection();
    $this->users = new ArrayCollection();
  }

  //<editor-fold desc="id">
  /**
   * @ORM\Id
   * @ORM\GeneratedValue
   * @ORM\Column(type="integer")
   */
  private ?int $id;

  public function getId(): ?int
  {
    return $this->id;
  }
  //</editor-fold>
  //<editor-fold desc="Description">
  /**
   * @Assert\NotNull(message="Please provide a description.")
   * @Assert\Length(
   *      min = 8,
   *      max = 120,
   *      minMessage = "Description be at least {{ limit }} characters long",
   *      maxMessage = "Description cannot be longer than {{ limit }} characters",
   *      allowEmptyString = false
   * )
   * @Assert\Type("string")
   * @ORM\Column(type="string", length=255)
   */
  private $description;

  public function getDescription(): ?string
  {
    return $this->description;
  }

  public function setDescription(string $description): self
  {
    $this->description = $description;

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Title">
  /**
   * @Assert\NotBlank
   * @Assert\Length(
   *      min = 8,
   *      max = 20,
   *      minMessage = "Title be at least {{ limit }} characters long",
   *      maxMessage = "Title cannot be longer than {{ limit }} characters",
   *      allowEmptyString = false
   * )
   * @Assert\Type("string")
   * @ORM\Column(type="string", length=255)
   */
  private $title;

  public function getTitle(): ?string
  {
    return $this->title;
  }

  public function setTitle(string $title): self
  {
    $this->title = $title;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Attribute">
  /**
   * Holds the authorization beacon for the permission
   * @Elao\Enum\Bridge\Symfony\Validator\Constraint\Enum(class=AccessType::class)
   * @ORM\Column(type="accesstype")
   * @Assert\NotNull(message="Please provide an attribute.")
   */
  private AccessType $attribute;

  public function getAttribute(): AccessType
  {
    return $this->attribute;
  }

  public function setAttribute(AccessType $attribute): self
  {
    $this->attribute = $attribute;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Subject">
  /**
   * Holds the FQCN for the authorization subject; i.e Entity::class
   * @Assert\NotNull(message="Please provide a guard subject.")
   * @App\Validator\EntityFQCN
   * @ORM\Column(type="string", length=255)
   */
  private $subject;

  public function getSubject(): ?string
  {
    return $this->subject;
  }

  public function setSubject(string $subject): self
  {
    $this->subject = $subject;

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Groups">
  /**
   * @var Collection<Group>|Group[]
   * @ORM\ManyToMany(targetEntity=Group::class, inversedBy="permissions", cascade={"persist"})
   */
  private Collection|array $groups;

  /**
   * @return Collection<Group>|Group[]
   */
  public function getGroups(): Collection|array
  {
    return $this->groups;
  }

  public function addGroup(Group $role): self
  {
    if (!$this->groups->contains($role)) {
      $this->groups[] = $role;
    }
    return $this;
  }

  public function removeGroup(Group $role): self
  {
    if ($this->groups->removeElement($role)) {
      $role->removePermission($this);
    }

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Enabled">
  /**
   * @Assert\NotNull
   * @ORM\Column(type="boolean")
   */
  private ?bool $enabled;

  public function getEnabled(): ?bool
  {
    return $this->enabled;
  }

  public function setEnabled(bool $enabled): self
  {
    $this->enabled = $enabled;

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Expression">
  /**
   * @ORM\Column(type="string", length=255, nullable=true)
   * @App\Validator\ExpressionLanguageSyntax
   */
  private ?string $expression;

  public function getExpression(): ?string
  {
    return $this->expression;
  }

  public function setExpression(?string $expression): self
  {
    $this->expression = $expression;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="User Inverter">
  /**
   * @ORM\ManyToMany(targetEntity=User::class, mappedBy="individualPermissions")
   */
  private $users;

  /**
   * @return Collection|User[]
   */
  public function getUsers(): Collection|array
  {
    return $this->users;
  }

  public function addUser(User $user): self
  {
    if (!$this->users->contains($user)) {
      $this->users[] = $user;
      $user->addIndividualPermission($this);
    }

    return $this;
  }

  public function removeUser(User $user): self
  {
    if ($this->users->removeElement($user)) {
      $user->removeIndividualPermission($this);
    }

    return $this;
  }
  //</editor-fold>

  #[Pure] public function __toString(): string
  {
    return $this->getTitle();
  }
}
