<?php

namespace App\Entity;

use App\Repository\MessageRepository;
use Doctrine\ORM\Mapping as ORM;
use Gedmo\Timestampable\Traits\TimestampableEntity;

/**
 * @ORM\Entity(repositoryClass=MessageRepository::class)
 */
class Message
{
  use TimestampableEntity;

  /**
   * @ORM\Id
   * @ORM\GeneratedValue
   * @ORM\Column(type="integer")
   */
  private $id;

  /**
   * @ORM\Column(type="string", length=255)
   */
  private $content;

  /**
   * @ORM\ManyToOne(targetEntity=User::class, inversedBy="messages")
   * @ORM\JoinColumn(nullable=false)
   */
  private $author;

  /**
   * @ORM\ManyToOne(targetEntity=Channel::class, inversedBy="messages")
   * @ORM\JoinColumn(nullable=false)
   */
  private $channel;

  public function getId(): ?int
  {
    return $this->id;
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

  public function getAuthor(): ?User
  {
    return $this->author;
  }

  public function setAuthor(?User $author): self
  {
    $this->author = $author;

    return $this;
  }

  public function getChannel(): ?Channel
  {
    return $this->channel;
  }

  public function setChannel(?Channel $channel): self
  {
    $this->channel = $channel;

    return $this;
  }
}
