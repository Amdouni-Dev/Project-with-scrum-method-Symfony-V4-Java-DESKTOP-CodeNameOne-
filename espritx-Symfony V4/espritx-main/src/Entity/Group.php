<?php

namespace App\Entity;

use App\Enum\GroupType;
use App\Repository\GroupRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Serializer\Annotation\Groups;

/**
 * @ORM\Entity(repositoryClass=GroupRepository::class)
 * @ORM\Table(name="`group`")
 */
class Group
{
  public const ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

  public function __construct()
  {
    $this->permissions = new ArrayCollection();
    $this->members = new ArrayCollection();
  }

  //<editor-fold desc="id">

  /**
   * @Groups("post")
   * @ORM\Id
   * @ORM\GeneratedValue
   * @ORM\Column(type="integer")
   *
   */
  private int $id;

  public function getId(): int
  {
    return $this->id;
  }

  public function setId(int $id): self
  {
    $this->id = $id;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Display Name">
  /**
   * @ORM\Column(type="string", length=64)
   * @Assert\NotBlank
   * @Assert\Length(min=5, max=64)
   * @Groups("post")
   * @Groups("Service")
   */
  private ?string $display_name;


  public function getDisplayName(): ?string
  {
    return $this->display_name;
  }

  public function setDisplayName(?string $display_name): self
  {
    $this->display_name = $display_name;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Security Title">
  /**
   * @ORM\Column(type="string", length=64)
   * @Assert\NotBlank
   * @Assert\Length(min=5, max=64)
   */
  private ?string $security_title;

  public function getSecurityTitle(): ?string
  {
    return $this->security_title;
  }

  public function setSecurityTitle(?string $security_title): self
  {
    $security_title = strtoupper($security_title);
    if (!str_starts_with($security_title, "ROLE_")) {
      throw new \RuntimeException("Role name must start with ROLE_ to be compatible with Symfony Security Implementation.");
    }
    $this->security_title = $security_title;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Permissions">
  /**
   * @var Collection<Permission>|Permission[]
   * @ORM\ManyToMany(targetEntity=Permission::class, mappedBy="groups", fetch="EAGER")
   */
  private Collection|array $permissions;

  /**
   * @return Collection|Permission[]
   */
  public function getPermissions(): Collection|array
  {
    return $this->permissions;
  }

  public function addPermission(Permission $permission): self
  {
    if (!$this->permissions->contains($permission)) {
      $this->permissions[] = $permission;
    }
    $permission->addGroup($this);
    return $this;
  }

  public function removePermission(Permission $permission): self
  {
    $this->permissions->removeElement($permission);
    $permission->removeGroup($this);
    return $this;
  }

  public function setPermissions(array|ArrayCollection $permissions): self
  {
    $this->permissions->clear();
    $this->permissions = $permissions;
    return $this;
  }
  //</editor-fold>

  //<editor-fold desc="Group Type">
  /**
   * @ORM\Column(type="grouptype", nullable=true)
   * @Assert\NotNull
   */
  protected $groupType;

  public function getGroupType(): ?GroupType
  {
    return $this->groupType;
  }

  public function setGroupType(GroupType $groupType): self
  {
    $this->groupType = $groupType;
    return $this;
  }
  //</editor-fold>

  //<editor-fold desc="Members">
  /**
   * @ORM\ManyToMany(targetEntity=User::class, mappedBy="groups",cascade={"persist", "remove"})
   */
  private $members;

  /**
   * @return Collection<int, User>
   */
  public function getMembers(): Collection
  {
    return $this->members;
  }

  public function addMember(User $member): self
  {
    if (!$this->members->contains($member)) {
      $this->members[] = $member;
      $member->addGroup($this);
    }

    return $this;
  }

  public function removeMember(User $member): self
  {
    if ($this->members->removeElement($member)) {
      $member->removeGroup($this);
    }

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Services Enjoyed By Group">
  /**
   * @ORM\ManyToMany(targetEntity=Service::class, mappedBy="Recipient", cascade={"persist", "remove"})
   */
  private $enjoyable_services;

  /**
   * @return mixed
   */
  public function getEnjoyableServices()
  {
    return $this->enjoyable_services;
  }

  /**
   * @param mixed $enjoyable_services
   * @return Group
   */
  public function setEnjoyableServices($enjoyable_services)
  {
    $this->enjoyable_services = $enjoyable_services;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Services Provided By Group">
  /**
   * @ORM\OneToMany(targetEntity=Service::class, mappedBy="Responsible",cascade={"persist", "remove"})
   */
  private $provided_services;

  /**
   * @return mixed
   */
  public function getProvidedServices()
  {
    return $this->provided_services;
  }

  /**
   * @param mixed $provided_services
   * @return Group
   */
  public function setProvidedServices($provided_services)
  {
    $this->provided_services = $provided_services;
    return $this;
  }

  //</editor-fold>

  public function __toString(): string
  {
    return $this->display_name;
  }


}
