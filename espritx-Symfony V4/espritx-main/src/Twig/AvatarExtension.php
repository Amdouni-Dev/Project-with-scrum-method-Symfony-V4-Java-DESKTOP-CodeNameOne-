<?php

namespace App\Twig;

use App\Entity\User;
use JetBrains\PhpStorm\Pure;
use Twig\Extension\AbstractExtension;
use Twig\TwigFunction;

class AvatarExtension extends AbstractExtension
{
  public function getFunctions()
  {
    return [
      new TwigFunction('initialsAvatarColor', [$this, 'initialsAvatarColor']),
    ];
  }

  public function initialsAvatarColor(User $user): string
  {
    $colors = ["primary", "secondary", "success", "danger", "warning", "info"];
    $seed = ord($user->getFirstName()) + ord($user->getLastName());
    $index = $seed % count($colors);
    return "bg-light-" . $colors[$index];
  }
}

