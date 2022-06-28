<?php

namespace App\Twig;

use App\Entity\User;
use App\Repository\UserRepository;
use Twig\Extension\AbstractExtension;
use Twig\TwigFunction;

class UserExtensions extends AbstractExtension
{
  public function __construct(private UserRepository $userRepository)
  {
  }

  public function getFunctions()
  {
    return [
      new TwigFunction('countCommonContacts', [$this, 'countCommonContacts']),
      new TwigFunction('makeFriendSuggestions', [$this, 'makeFriendSuggestions'])
    ];
  }

  public function countCommonContacts(User $user1, User $user2): int
  {
    return count($this->userRepository->getCommonContacts($user1, $user2));
  }

  public function makeFriendSuggestions(User $user, int $limit = 10): array
  {
    return $this->userRepository->makeFriendSuggestions($user);
  }
}