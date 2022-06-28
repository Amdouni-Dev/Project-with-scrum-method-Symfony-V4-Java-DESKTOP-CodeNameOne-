<?php

namespace App\Twig;

use App\Entity\Message;
use App\Entity\User;
use Doctrine\ORM\PersistentCollection;
use FontLib\TrueType\Collection;
use JetBrains\PhpStorm\Pure;
use Symfony\Component\Security\Core\Security;
use Twig\Extension\AbstractExtension;
use Twig\TwigFunction;

class ChatExtension extends AbstractExtension
{
    private const CLAMP_LIMIT = 32;

    public function __construct()
    {
    }

    public function getFunctions()
    {
        return [
            new TwigFunction('getFirstNonCurrentUser', [$this, 'getFirstNonCurrentUser']),
            new TwigFunction('makeNameFromGroupChatParticipants', [$this, 'makeNameFromGroupChatParticipants']),
            new TwigFunction('partitionChatMessages', [$this, 'partitionChatMessages']),
        ];
    }

    public function getFirstNonCurrentUser(User $user, $participants): User
    {
        foreach ($participants as $participant) {
            if ($participant !== $user) {
                return $participant;
            }
        }
        throw new \RuntimeException("This conversation has only one party. Are you okay?");
    }

    /**
     * @param Collection<User> $participants
     */
    public function makeNameFromGroupChatParticipants(User $user, $participants): string
    {
        if (count($participants) > 2) {
            $anchor = "Chat with ";
            /** @var User $participant */
            foreach ($participants as $participant) {
                $anchor .= $participant->getFirstName() . " " . $participant->getLastName();
            }
            if (strlen($anchor) > self::CLAMP_LIMIT)
                return substr($anchor, 0, self::CLAMP_LIMIT - 2) . "..";
            else return $anchor;
        } else {
            $second_party = $this->getFirstNonCurrentUser($user, $participants);
            return $second_party->getFirstName() . " " . $second_party->getLastName();
        }
    }

    /**
     * @param Message[] $messages
     */
    public function partitionChatMessages(array $messages)
    {
        $resulting = [];
        $last_block = [];
        $last_block[] = array_pop($messages);
        while (!empty($messages)) {
            if ($messages[count($messages) - 1]->getAuthor()->getId() !== $last_block[count($last_block) - 1]->getAuthor()->getId()) {
                $resulting[] = $last_block;
                $last_block = [array_pop($messages)];
            } else {
                $last_block[] = array_pop($messages);
            }
        }
        $resulting[] = $last_block;
        return $resulting;
    }
}

