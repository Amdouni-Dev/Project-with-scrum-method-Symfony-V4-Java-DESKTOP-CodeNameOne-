<?php

namespace App\DataFixtures;

use App\Entity\Channel;
use App\Entity\Group;
use App\Entity\Message;
use App\Entity\User;
use App\Enum\DocumentIdentityTypeEnum;
use App\Enum\GroupType;
use App\Enum\UserStatus;
use Doctrine\Bundle\FixturesBundle\FixtureGroupInterface;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;
use phpDocumentor\Reflection\Types\Collection;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;

class ChatFixtures extends AbstractFixtureEx implements DependentFixtureInterface
{
  public const LOADED_CHAT_FIXTURES = "loaded_chats";

  public function load(ObjectManager $manager): void
  {
    $generator = Factory::create();
    $testing_channels = new ArrayCollection();
    for ($i = 0; $i < 25; $i++) {
      $user1 = $this->getSingleRandomItem(UserFixtures::LOADED_USER_FIXTURES);
      $user2 = $this->getSingleRandomItem(UserFixtures::LOADED_USER_FIXTURES);
      while ($user1->getId() === $user2->getId()) {
        $user2 = $this->getSingleRandomItem(UserFixtures::LOADED_USER_FIXTURES);
      }
      $channel = new Channel();
      $channel->addParticipant($user1);
      $channel->addParticipant($user2);
      for ($j = 2; $j <= 6; $j++) {
        $msg = new Message();
        $msg->setCreatedAt(new \DateTime(strtotime('-$j minutes')));
        $msg->setChannel($channel);
        $msg->setContent($generator->realText(64));
        $msg->setAuthor($generator->randomElement([$user1, $user2]));
        $manager->persist($msg);
      }
      $manager->persist($channel);
      $testing_channels->add($channel);
    }
    $manager->flush();
    $this->addReferenceArray(self::LOADED_CHAT_FIXTURES, $testing_channels);
  }

  public function getDependencies()
  {
    return [UserFixtures::class];
  }
}
