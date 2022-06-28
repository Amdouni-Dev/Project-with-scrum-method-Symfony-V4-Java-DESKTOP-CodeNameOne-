<?php

namespace App\Entity;

use App\Enum\DocumentIdentityTypeEnum;
use App\Enum\GroupType;
use App\Enum\UserStatus;
use App\Repository\UserRepository;
use DateTime;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Event\LifecycleEventArgs;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\ORM\Mapping\Column;
use Doctrine\ORM\PersistentCollection;
use Exception;
use Gedmo\Timestampable\Traits\TimestampableEntity;
use JetBrains\PhpStorm\Internal\LanguageLevelTypeAware;
use Mgilet\NotificationBundle\Annotation\Notifiable;
use Mgilet\NotificationBundle\NotifiableInterface;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use Symfony\Component\HttpFoundation\File\File;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\Security\Core\User\EquatableInterface;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\Validator\Constraints as Assert;
use Vich\UploaderBundle\Mapping\Annotation as Vich;
use Vich\UploaderBundle\Entity\File as EmbeddedFile;
use Symfony\Component\Serializer\Annotation\Groups;


/**
 * @ORM\Entity(repositoryClass=UserRepository::class)
 * @ORM\Table(name="`user`")
 * @ORM\HasLifecycleCallbacks
 * @ORM\EntityListeners({"App\Entity\Listener\UserListener"})
 * @UniqueEntity(fields={"email"}, message="There is already an account with this email")
 * @Vich\Uploadable
 * @Notifiable(name="user")
 */
class User implements UserInterface, EquatableInterface, \Serializable, NotifiableInterface
{
  use TimestampableEntity;

  public function __construct()
  {
    $this->individualPermissions = new ArrayCollection();
    $this->posts = new ArrayCollection();
    $this->groups = new ArrayCollection();
    $this->userStatus = UserStatus::get(UserStatus::PENDING);
    $this->commentaires = new ArrayCollection();
    $this->likes = new ArrayCollection();

    $this->events = new ArrayCollection();
    $this->calls = new ArrayCollection();
    $this->identityType = DocumentIdentityTypeEnum::UNKNOWN();
    $this->avatar = new EmbeddedFile();
    $this->serviceRequests = new ArrayCollection();
    $this->UserCall = new ArrayCollection();
    $this->postCategories = new ArrayCollection();
    $this->groupPosts = new ArrayCollection();
    $this->groupes = new ArrayCollection();
    $this->contacts = new ArrayCollection();
    $this->contacted_by = new ArrayCollection();
    $this->messages = new ArrayCollection();
    $this->channels = new ArrayCollection();

  }

  //<editor-fold desc="id">

  /**
   * @ORM\Id
   * @ORM\GeneratedValue
   * @ORM\Column(type="integer")
   * @Groups("post:read")
   */
  private $id;

  public function getId(): ?int
  {
    return $this->id;
  }
  //</editor-fold>
  //<editor-fold desc="Avatar">
  /**
   * NOTE: This is not a mapped field of entity metadata, just a simple property.
   *
   * @Vich\UploadableField(
   *   mapping="avatar_image",
   *   fileNameProperty="avatar.name"
   * )
   *
   * @var File|null
   */
  private ?File $avatarFile = null;

  public function setAvatarFile(File|UploadedFile|null $avatarFile = null): static
  {
    $this->avatarFile = $avatarFile;
    if ($avatarFile !== null) {
      $this->updatedAt = new \DateTimeImmutable();
    }
    return $this;
  }

  public function getAvatarFile(): ?File
  {
    return $this->avatarFile;
  }

  /**
   * @ORM\Embedded(class="Vich\UploaderBundle\Entity\File")
   * @Groups ("Service")
   * @Groups ("Request")
   */
  private ?EmbeddedFile $avatar;

  public function setAvatar(?EmbeddedFile $avatar): static
  {
    $this->avatar = $avatar;
    return $this;
  }

  public function getAvatar(): ?EmbeddedFile
  {
    return $this->avatar;
  }
  //</editor-fold>
  //<editor-fold desc="First Name">
  /**
   * @Assert\NotNull
   * @Assert\Length(min=3, max=16)
   * @Assert\Regex(
   *     pattern="/\d/",
   *     match=false,
   *     message="Your name cannot contain a number"
   * )
   * @ORM\Column(type="string", length=20)
   * @Groups("post:read")
   * @Groups ("Service")
   * @Groups ("Request")
   */
  private ?string $first_name = null;

  public function getFirstName(): ?string
  {
    return $this->first_name;
  }

  public function setFirstName(?string $first_name): self
  {
    $this->first_name = $first_name;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Last Name">
  /**
   * @Assert\NotNull
   * @Assert\Length(min=3, max=16)
   * @Assert\Regex(
   *     pattern="/\d/",
   *     match=false,
   *     message="Your name cannot contain a number"
   * )
   * @ORM\Column(type="string", length=25)
   * @Groups("post:read")
   * @Groups ("Service")
   * @Groups ("Request")
   */
  private ?string $last_name = null;

  public function getLastName(): ?string
  {
    return $this->last_name;
  }

  public function setLastName(?string $last_name): self
  {
    $this->last_name = $last_name;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Email">
  /**
   * @Assert\NotBlank
   * @Assert\Email(
   *    message = "The email '{{ value }}' is not a valid email."
   * )
   * @var string|null
   * @ORM\Column(type="string", unique=true)
   * @Groups("post:read")
   */
  protected ?string $email = null;

  public function getEmail(): ?string
  {
    return $this->email;
  }

  public function setEmail(?string $email): static
  {
    $this->email = $email;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Phone Number">
  /**
   * @Assert\Regex("/^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/",
   * message="Respect this format: +21611111111")
   * @ORM\Column(type="string", length=255, nullable=true)
   * @Assert\NotBlank
   * @Groups("post:read")
   */
  private ?string $phoneNumber = null;

  public function getPhoneNumber(): ?string
  {
    return $this->phoneNumber;
  }

  public function setPhoneNumber(?string $phonenumber): self
  {
    $this->phoneNumber = $phonenumber;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Class">
  /**
   * @Assert\Regex("/^([1-5](A|B|TWIN|SLEAMS)\d{1,3})|$/")
   * @ORM\Column(type="string", length=255, nullable=true)
   */
  private $class;

  public function getClass(): ?string
  {
    return $this->class;
  }

  public function setClass(?string $class): self
  {
    $this->class = $class;

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="PlainPassword">
  /**
   * @var string|null
   * @Assert\Expression(
   *   "(this.getPassword() != '' and value == '') or (value != '' and this.getPassword() == '')",
   *    message="A password of at least 6 characters must be set."
   * )
   * @Groups("post:read")
   */
  protected ?string $plainPassword = null;

  public function setPlainPassword(?string $password): static
  {
    if (empty($password)) {
      return $this;
    }
    $this->plainPassword = $password;
    $this->password = null;
    return $this;
  }

  public function getPlainPassword(): ?string
  {
    return $this->plainPassword;
  }
  //</editor-fold>
  //<editor-fold desc="Password">
  /**
   * @var string The hashed password
   * @ORM\Column(type="string")
   * @Groups("post:read")
   */
  private $password;

  /**
   * @see UserInterface
   */
  public function getPassword(): ?string
  {
    return $this->password;
  }

  public function setPassword(string $password): self
  {
    $this->password = $password;
    return $this;
  }

  public function getSalt()
  {
    return null; // todo: for now.
  }

  //</editor-fold>
  //<editor-fold desc="LastLogin">
  /**
   * @ORM\Column(type="datetime", nullable=true)
   */
  protected ?DateTime $last_login;

  public function getLastLogin(): ?DateTime
  {
    return $this->last_login;
  }

  public function setLastLogin(DateTime $time = null)
  {
    $this->last_login = $time;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="LastActive">
  /**
   * @ORM\Column(name="last_activity_at", type="datetime", nullable=true)
   */
  protected ?DateTime $lastActivityAt = null;

  public function setLastActivityAt(?DateTime $lastActivityAt): static
  {
    $this->lastActivityAt = $lastActivityAt;
    return $this;
  }

  public function getLastActivityAt(): ?DateTime
  {
    return $this->lastActivityAt;
  }

  public function isActiveNow(): bool
  {
    $delay = new DateTime('5 minutes ago');
    return ($this->getLastActivityAt() > $delay);
  }
  //</editor-fold>
  //<editor-fold desc="ConfirmationToken">
  /**
   * @Column(type="string", nullable=true)
   */
  protected ?string $confirmationToken = null;

  public function getConfirmationToken(): ?string
  {
    return $this->confirmationToken;
  }

  public function setConfirmationToken(string $confirmationToken): static
  {
    $this->confirmationToken = $confirmationToken;

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="PasswordRequestedAt">
  /**
   * @ORM\Column(type="datetime", nullable=true)
   */
  protected ?DateTime $passwordRequestedAt;

  public function setPasswordRequestedAt(DateTime $date = null): static
  {
    $this->passwordRequestedAt = $date;
    return $this;
  }

  public function getPasswordRequestedAt(): ?DateTime
  {
    return $this->passwordRequestedAt;
  }
  //</editor-fold>
  //<editor-fold desc="UserStatus">
  /**
   * @Assert\NotNull
   * @Elao\Enum\Bridge\Symfony\Validator\Constraint\Enum(class=UserStatus::class)
   * @ORM\Column(type="userstatus")
   */
  protected UserStatus $userStatus;

  public function getUserStatus(): UserStatus
  {
    return $this->userStatus;
  }

  public function setUserStatus(UserStatus $userStatus): self
  {
    $this->userStatus = $userStatus;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Groups">
  /**
   * @ORM\ManyToMany(targetEntity=Group::class, inversedBy="members")
   */
  private $groups;

  /**
   * @return Collection<int, Group>
   */
  public function getGroups(): Collection
  {
    return $this->groups;
  }
  public function isPartOfGroupType(GroupType $groupType): bool
  {
    /** @var Group $group */
    foreach ($this->groups as $group) {
      if ($group->getGroupType() === $groupType)
        return true;
    }
    return false;
  }

  public function addGroup(Group $group): self
  {
    if (!$this->groups->contains($group)) {
      $this->groups[] = $group;
    }

    return $this;
  }

  public function removeGroup(Group $group): self
  {
    $this->groups->removeElement($group);
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Permissions">
  /**
   * @ORM\ManyToMany(targetEntity=Permission::class, inversedBy="users")
   */
  private Collection|array $individualPermissions;

  /**
   * @return Collection
   */
  public function getIndividualPermissions(): Collection
  {
    return $this->individualPermissions;
  }

  public function addIndividualPermission(Permission $permission): self
  {
    if (!$this->individualPermissions->contains($permission)) {
      $this->individualPermissions[] = $permission;
    }

    return $this;
  }

  public function removeIndividualPermission(Permission $permission): self
  {
    $this->individualPermissions->removeElement($permission);
    return $this;
  }

  /**
   * @return Collection<Permission>|array
   */
  public function getAggregatePermissions(): array|Collection
  {
    $perms = [];
    /** @var Group $group */
    foreach ($this->groups as $group) {
      foreach ($group->getPermissions() as $group_perm) {
        $perms[] = $group_perm;
      }
    }
    foreach ($this->getIndividualPermissions() as $permission) {
      $perms[] = $permission;
    }
    return array_unique($perms);
  }
  //</editor-fold>
  //<editor-fold desc="Commentaires">
  /**
   * @ORM\OneToMany(targetEntity=Commentaire::class, mappedBy="user", cascade={"persist", "remove"})
   */
  private $commentaires;
  //</editor-fold>
  //<editor-fold desc="Posts">
  /**
   * @ORM\OneToMany(targetEntity=Post::class, mappedBy="user", orphanRemoval=true, cascade={"persist", "remove"})
   */
  private Collection $posts;

  /**
   * @return Collection|Post[]
   */
  public function getPosts(): Collection|array
  {
    return $this->posts;
  }

  public function addPost(Post $post): self
  {
    if (!$this->posts->contains($post)) {
      $this->posts[] = $post;
      $post->setUser($this);
    }

    return $this;
  }

  public function removePost(Post $post): self
  {
    if ($this->posts->removeElement($post)) {
      // set the owning side to null (unless already changed)
      if ($post->getUser() === $this) {
        $post->setUser(null);
      }
    }
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Likes">
  /**
   * @ORM\OneToMany(targetEntity=PostLike::class, mappedBy="user", cascade={"persist", "remove"})
   */
  private $likes;

  public function removeLike(PostLike $like): self
  {
    if ($this->likes->removeElement($like)) {
      // set the owning side to null (unless already changed)
      if ($like->getUser() === $this) {
        $like->setUser(null);
      }
    }
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="DocIdentityType">
  /**
   * @ORM\Column(type="identitydoctype")
   * @Elao\Enum\Bridge\Symfony\Validator\Constraint\Enum(class=DocumentIdentityTypeEnum::class)
   * @Groups("post:read")
   */
  protected DocumentIdentityTypeEnum $identityType;

  public function getIdentityType(): DocumentIdentityTypeEnum
  {
    return $this->identityType;
  }

  public function setIdentityType(DocumentIdentityTypeEnum $identityType): self
  {
    $this->identityType = $identityType;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Identity Document Number">
  /**
   * @ORM\Column(type="string", length=8, nullable=true)
   * @Assert\Regex("/([A-Z0-9<]{9}[0-9]{1}[A-Z]{3}[0-9]{7}[A-Z]{1}[0-9]{7}[A-Z0-9<]{14}[0-9]{2})|(\d{8})/")
   * @Assert\NotBlank
   * @Groups("post:read")
   */
  private ?string $identityDocumentNumber = null;


  public function getIdentityDocumentNumber(): ?string
  {
    return $this->identityDocumentNumber;
  }

  public function setIdentityDocumentNumber(?string $identityDocumentNumber): self
  {
    $this->identityDocumentNumber = $identityDocumentNumber;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Is Verified">
  /**
   * @ORM\Column(type="boolean")
   * @Groups("post:read")
   */
  private $isVerified = false;

  public function isVerified(): bool
  {
    return $this->isVerified;
  }

  public function setIsVerified(bool $isVerified): self
  {
    $this->isVerified = $isVerified;
    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="UserInterface">
  /**
   * @see UserInterface
   */
  public function eraseCredentials()
  {
    $this->plainPassword = null;
  }

  public function getUsername(): string
  {
    return $this->email;
  }

  public function getRoles()
  {
    /** @var Group $g */
    return array_map(static fn($g) => $g->getSecurityTitle(), $this->groups->toArray());
  }

  //</editor-fold>
  //<editor-fold desc="Serializable">
  public function serialize()
  {
    return serialize(array(
      $this->id,
      $this->email,
      $this->password
    ));
  }

  public function unserialize(string $data)
  {
    [
      $this->id,
      $this->email,
      $this->password
    ] = unserialize($data, [
      'allowed_classes' => true
    ]);
  }
  //</editor-fold>
  //<editor-fold desc="Service Requests">
  /**
   * @ORM\OneToMany(targetEntity=ServiceRequest::class, mappedBy="Requester", orphanRemoval=true, cascade={"persist", "remove"})
   */
  private $serviceRequests;

  /**
   * @return Collection<int, ServiceRequest>
   */
  public function getServiceRequests(): Collection
  {
    return $this->serviceRequests;
  }

  public function addServiceRequest(ServiceRequest $serviceRequest): self
  {
    if (!$this->serviceRequests->contains($serviceRequest)) {
      $this->serviceRequests[] = $serviceRequest;
      $serviceRequest->setRequester($this);
    }

    return $this;
  }

  public function removeServiceRequest(ServiceRequest $serviceRequest): self
  {
    if ($this->serviceRequests->removeElement($serviceRequest)) {
      // set the owning side to null (unless already changed)
      if ($serviceRequest->getRequester() === $this) {
        $serviceRequest->setRequester(null);
      }
    }

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Events">
  /**
   * @ORM\OneToMany(targetEntity=Event::class, mappedBy="user", cascade={"persist", "remove"})
   */
  private $events;

  /**
   * @return Collection<int, Event>
   */
  public function getEvents(): Collection
  {
    return $this->events;
  }

  public function addEvent(Event $event): self
  {
    if (!$this->events->contains($event)) {
      $this->events[] = $event;
      $event->setUser($this);
    }

    return $this;
  }

  public function removeEvent(Event $event): self
  {
    if ($this->events->removeElement($event)) {
      // set the owning side to null (unless already changed)
      if ($event->getUser() === $this) {
        $event->setUser(null);
      }
    }

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Calls">
  /**
   * @ORM\ManyToMany(targetEntity=Call::class, mappedBy="users")
   */
  private $calls;

  /**
   * @return Collection<int, Call>
   */
  public function getCalls(): Collection
  {
    return $this->calls;
  }

  public function addCall(Call $call): self
  {
    if (!$this->calls->contains($call)) {
      $this->calls[] = $call;
      $call->addUser($this);
    }

    return $this;
  }

  public function removeCall(Call $call): self
  {
    if ($this->calls->removeElement($call)) {
      $call->removeUser($this);
    }

    return $this;
  }

  //</editor-fold>
  public function isEqualTo(UserInterface $user)
  {
    return $this->getUsername() === $user->getUsername();
    // do we add check for password; or delegate the username uniqueness constraint to the database?
  }

  public function __toString(): string
  {
    return $this->email;
  }
  //<editor-fold desc="User Call">

  /**
   * @ORM\OneToMany(targetEntity=Call::class, mappedBy="user", cascade={"persist", "remove"})
   */
  private $UserCall;

  /**
   * @return Collection<int, Call>
   */
  public function getUserCall(): Collection
  {
    return $this->UserCall;
  }

  public function addUserCall(Call $userCall): self
  {
    if (!$this->UserCall->contains($userCall)) {
      $this->UserCall[] = $userCall;
      $userCall->setUser($this);
    }

    return $this;
  }

  public function removeUserCall(Call $userCall): self
  {
    if ($this->UserCall->removeElement($userCall)) {
      // set the owning side to null (unless already changed)
      if ($userCall->getUser() === $this) {
        $userCall->setUser(null);
      }
    }

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Post Categories">
  /**
   * @ORM\OneToMany(targetEntity=PostCategory::class, mappedBy="user", cascade={"persist", "remove"})
   */
  private $postCategories;

  /**
   * @return Collection<int, PostCategory>
   */
  public function getPostCategories(): Collection
  {
    return $this->postCategories;
  }

  public function addPostCategory(PostCategory $postCategory): self
  {
    if (!$this->postCategories->contains($postCategory)) {
      $this->postCategories[] = $postCategory;
      $postCategory->setUser($this);
    }

    return $this;
  }

  public function removePostCategory(PostCategory $postCategory): self
  {
    if ($this->postCategories->removeElement($postCategory)) {
      // set the owning side to null (unless already changed)
      if ($postCategory->getUser() === $this) {
        $postCategory->setUser(null);
      }
    }

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Group posts">
  /**
   * @ORM\OneToMany(targetEntity=GroupPost::class, mappedBy="user", orphanRemoval=true, cascade={"persist", "remove"})
   */
  private $groupPosts;

  /**
   * @return Collection<int, GroupPost>
   */
  public function getGroupPosts(): Collection
  {
    return $this->groupPosts;
  }

  public function addGroupPost(GroupPost $groupPost): self
  {
    if (!$this->groupPosts->contains($groupPost)) {
      $this->groupPosts[] = $groupPost;
      $groupPost->setUser($this);
    }

    return $this;
  }

  public function removeGroupPost(GroupPost $groupPost): self
  {
    if ($this->groupPosts->removeElement($groupPost)) {
      // set the owning side to null (unless already changed)
      if ($groupPost->getUser() === $this) {
        $groupPost->setUser(null);
      }
    }

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Groupe">
  /**
   * @ORM\ManyToMany(targetEntity=GroupPost::class, mappedBy="membre", orphanRemoval=true)
   */
  private $groupes;

  /**
   * @return Collection<int, GroupPost>
   */
  public function getGroupes(): Collection
  {
    return $this->groupes;
  }

  public function addGroupe(GroupPost $groupe): self
  {
    if (!$this->groupes->contains($groupe)) {
      $this->groupes[] = $groupe;
      $groupe->addMembre($this);
    }

    return $this;
  }

  public function removeGroupe(GroupPost $groupe): self
  {
    if ($this->groupes->removeElement($groupe)) {
      $groupe->removeMembre($this);
    }

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Messages">
  /**
   * @ORM\OneToMany(targetEntity=Message::class, mappedBy="author", cascade={"persist", "remove"})
   * @ORM\JoinColumn(nullable=false)
   */
  private $messages;

  /**
   * @return Collection<int, Message>
   */
  public function getMessages(): Collection
  {
    return $this->messages;
  }

  public function addMessage(Message $message): self
  {
    if (!$this->messages->contains($message)) {
      $this->messages[] = $message;
      $message->setAuthor($this);
    }

    return $this;
  }

  public function removeMessage(Message $message): self
  {
    if ($this->messages->removeElement($message)) {
      // set the owning side to null (unless already changed)
      if ($message->getAuthor() === $this) {
        $message->setAuthor(null);
      }
    }

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Channels">
  /**
   * @ORM\ManyToMany(targetEntity=Channel::class, mappedBy="participants", cascade={"persist", "remove"})
   */
  private $channels;

  /**
   * @return Collection<int, Channel>
   */
  public function getChannels(): Collection
  {
    return $this->channels;
  }

  public function addChannel(Channel $channel): self
  {
    if (!$this->channels->contains($channel)) {
      $this->channels[] = $channel;
      $channel->addParticipant($this);
    }

    return $this;
  }

  public function removeChannel(Channel $channel): self
  {
    if ($this->channels->removeElement($channel)) {
      $channel->removeParticipant($this);
    }

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="About">
  /**
   * @ORM\Column(type="string", length=255, nullable=true)
   */
  private $about;


  public function getAbout(): ?string
  {
    return $this->about;
  }

  public function setAbout(?string $about): self
  {
    $this->about = $about;

    return $this;
  }
  //</editor-fold>

  //<editor-fold desc="Contacts">
  /**
   * @ORM\ManyToMany(targetEntity=User::class, inversedBy="contacted_by", cascade={"persist", "remove"})
   */
  private $contacts;

  /**
   * @return Collection<int, self>
   */
  public function getContacts(): Collection
  {
    return $this->contacts;
  }

  public function addContact(self $contact): self
  {
    if (!$this->contacts->contains($contact)) {
      $this->contacts[] = $contact;
    }

    return $this;
  }

  public function removeContact(self $contact): self
  {
    $this->contacts->removeElement($contact);

    return $this;
  }
  //</editor-fold>
  //<editor-fold desc="Contacted By">
  /**
   * @ORM\ManyToMany(targetEntity=User::class, mappedBy="contacts")
   */
  private $contacted_by;


  /**
   * @return Collection<int, self>
   */
  public function getContactedBy(): Collection
  {
    return $this->contacted_by;
  }

  public function addContactedBy(self $contactedBy): self
  {
    if (!$this->contacted_by->contains($contactedBy)) {
      $this->contacted_by[] = $contactedBy;
      $contactedBy->addContact($this);
    }

    return $this;
  }

  public function removeContactedBy(self $contactedBy): self
  {
    if ($this->contacted_by->removeElement($contactedBy)) {
      $contactedBy->removeContact($this);
    }

    return $this;
  }
  //</editor-fold>

  //<editor-fold desc="Google ID">
  /**
   * @ORM\Column(type="string", length=255, nullable=true)
   */
  private $googleId;


  public function getGoogleId(): ?string
  {
    return $this->googleId;
  }

  public function setGoogleId(?string $googleId): self
  {
    $this->googleId = $googleId;

    return $this;
  }

  //</editor-fold>

  public function __debugInfo(): ?array
  {
    return [];     // TODO: Implement __debugInfo() method.
  }



}
