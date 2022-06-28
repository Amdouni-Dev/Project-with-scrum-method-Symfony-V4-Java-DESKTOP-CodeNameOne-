<?php

namespace App\Entity;
use Mgilet\NotificationBundle\Annotation\Notifiable;
use Mgilet\NotificationBundle\NotifiableInterface;
use App\Repository\PostRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Gedmo\Mapping\Annotation as Gedmo;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\Annotation\Groups;
use Vich\UploaderBundle\Mapping\Annotation as Vich;


/**
 * @ORM\Entity(repositoryClass=PostRepository::class)
 */
class Post
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     * @Groups("post:read")
     *
     */
    private $id;

    /**
     *@Assert\NotBlank
     *@Assert\Length(
     *      min = 3,
     *      max = 15,
     *      minMessage = "le titre de votre post doit avoir au moins {{ limit }} caracteres ",
     *      maxMessage = "le titre de votre post ne doit pas depasser  {{ limit }} caracteres"
     * )

     *
     * @ORM\Column(type="string", length=255)
     * @Groups("post:read")
     */
    private $title;

    /**
     * @Gedmo\Slug(fields={"title"})
     * @ORM\Column(type="string", length=255)
     * @Groups("post:read")
     */
    private $slug;

    /**
     *@Assert\Length(
     *      min = 5,
     *      max = 2000,
     *      minMessage = "le contenu de votre post doit avoir au moins {{ limit }} caracteres ",
     *      maxMessage = "le contenue de votre post ne doit pas depasser  {{ limit }} caracteres"
     * )
     * @ORM\Column(type="text")
     * @Groups("post:read")
     */
    private $content;

    /**
     * @ORM\Column(type="datetime")
     * @Groups("post:read")
     */
    private $created_at;



    /**
     * @ORM\ManyToOne(targetEntity=User::class, inversedBy="posts", cascade={"persist"})
     * @ORM\JoinColumn(nullable=false)
     * @Groups("post:read")
     */
    private $user;



    /**
     * @ORM\Column(type="boolean")
     * @Groups("post:read")
     */
    private $isValid;

    /**
     * @ORM\Column(type="datetime_immutable")
     * @Groups("post:read")
     *
     */
    private $updated_at;

    /**
     * @ORM\Column(type="boolean")
     * @Groups("post:read")
     */
    private $is_deleted;

    /**
     * @ORM\OneToMany(targetEntity=Commentaire::class, mappedBy="post",cascade={"remove"})
     *
     */
    private $commentaires;

    /**
     * @ORM\OneToMany(targetEntity=PostLike::class, mappedBy="post",cascade={"remove"})
     * @Groups("post:read")
     */
    private $likes;



    /**
     * @ORM\Column(type="string", length=255, nullable=true)
     */
    private $localisation;

    /**
     * @ORM\Column(type="string", length=255, nullable=true)
     * @Groups("post:read")
     */
    private $longitude;

    /**
     * @ORM\Column(type="string", length=255, nullable=true)
     * @Groups("post:read")
     */
    private $latitude;

    /**
     * @ORM\ManyToOne(targetEntity=GroupPost::class, inversedBy="posts")
     */
    private $groupPost;

    /**
     * @ORM\OneToMany(targetEntity=Images::class, mappedBy="post",orphanRemoval=true ,cascade={"persist"} )
     */
    private $images;

    /**
     * @ORM\Column(type="string", length=255, nullable=true)
     */
    private $image;



    public function __construct()
    {
        $this->commentaires = new ArrayCollection();
        $this->likes = new ArrayCollection();
        $this->images = new ArrayCollection();
    }



    public function getId(): ?int
    {
        return $this->id;
    }


    public function getTitle(): ?string
    {
        return $this->title;
    }

    public function setTitle(string $title): self
    {
        $this->title = $title;

        return $this;
    }

    public function getSlug(): ?string
    {
        return $this->slug;
    }

    public function setSlug(string $slug): self
    {
        $this->slug = $slug;

        return $this;
    }

    public function getContent(): ?string
    {
        return $this->content;
    }

    public function setContent(string $content): self
    {
        $this->content = $content;

        return $this;
    }

    public function getCreatedAt(): ?\DateTimeInterface
    {
        return $this->created_at;
    }

    public function setCreatedAt(\DateTimeInterface $created_at): self
    {
        $this->created_at = $created_at;

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



    public function getIsValid(): ?bool
    {
        return $this->isValid;
    }

    public function setIsValid(bool $isValid): self
    {
        $this->isValid = $isValid;

        return $this;
    }

    public function getUpdtaedAT(): ?\DateTimeImmutable
    {
        return $this->updated_at;
    }

    public function setUpdatedAt(\DateTimeImmutable $updatedat): self
    {
        $this->updated_at = $updatedat;

        return $this;
    }

    public function isValid(): ?bool
    {
        return $this->isValid;
    }

    public function getIsDeleted(): ?bool
    {
        return $this->is_deleted;
    }

    public function setIsDeleted(bool $is_deleted): self
    {
        $this->is_deleted = $is_deleted;

        return $this;
    }

    /**
     * @return Collection|Commentaire[]
     */
    public function getCommentaires(): Collection
    {
        return $this->commentaires;
    }

    public function addCommentaire(Commentaire $commentaire): self
    {
        if (!$this->commentaires->contains($commentaire)) {
            $this->commentaires[] = $commentaire;
            $commentaire->setPost($this);
        }

        return $this;
    }

    public function removeCommentaire(Commentaire $commentaire): self
    {
        if ($this->commentaires->removeElement($commentaire)) {
            // set the owning side to null (unless already changed)
            if ($commentaire->getPost() === $this) {
                $commentaire->setPost(null);
            }
        }

        return $this;
    }

    public function __toString(): string
    {
        return $this->getTitle();
    }

    /**
     * @return Collection|PostLike[]
     */
    public function getLikes(): Collection
    {
        return $this->likes;
    }

    public function addLike(PostLike $like): self
    {
        if (!$this->likes->contains($like)) {
            $this->likes[] = $like;
            $like->setPost($this);
        }

        return $this;
    }

    public function removeLike(PostLike $like): self
    {
        if ($this->likes->removeElement($like)) {
            // set the owning side to null (unless already changed)
            if ($like->getPost() === $this) {
                $like->setPost(null);
            }
        }

        return $this;
    }

    //  cette fonction permet de savoir si ce post est liké par un user
    public function isLikedByUser(User $user):bool{
        foreach ($this->likes as $like){
            if($like->getUser()=== $user)
                return  true;



        }
        return  false;

    }

    /* public function getPost($length = null)
     {
         if (false === is_null($length) && $length > 0)
             return substr($this->getPost(), 0, $length);
         else
             return $this->getPost();
     }
 */


    /**
     * @return mixed
     */
    public function getUpdatedAt()
    {
        return $this->updated_at;
    }

    public function getLocalisation(): ?string
    {
        return $this->localisation;
    }

    public function setLocalisation(?string $localisation): self
    {
        $this->localisation = $localisation;

        return $this;
    }

    public function getLongitude(): ?string
    {
        return $this->longitude;
    }

    public function setLongitude(?string $longitude): self
    {
        $this->longitude = $longitude;

        return $this;
    }

    public function getLatitude(): ?string
    {
        return $this->latitude;
    }

    public function setLatitude(?string $latitude): self
    {
        $this->latitude = $latitude;

        return $this;
    }

    public function getGroupPost(): ?GroupPost
    {
        return $this->groupPost;
    }

    public function setGroupPost(?GroupPost $groupPost): self
    {
        $this->groupPost = $groupPost;

        return $this;
    }

    /**
     * @return Collection<int, Images>
     */
    public function getImages(): Collection
    {
        return $this->images;
    }

    public function addImage(Images $image): self
    {
        if (!$this->images->contains($image)) {
            $this->images[] = $image;
            $image->setPost($this);
        }

        return $this;
    }

    public function removeImage(Images $image): self
    {
        if ($this->images->removeElement($image)) {
            // set the owning side to null (unless already changed)
            if ($image->getPost() === $this) {
                $image->setPost(null);
            }
        }

        return $this;
    }

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(?string $image): self
    {
        $this->image = $image;

        return $this;
    }

}