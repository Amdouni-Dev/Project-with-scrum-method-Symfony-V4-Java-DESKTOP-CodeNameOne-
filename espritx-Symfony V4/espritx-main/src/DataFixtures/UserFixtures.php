<?php

namespace App\DataFixtures;

use App\Entity\Group;
use App\Entity\User;
use App\Enum\DocumentIdentityTypeEnum;
use App\Enum\GroupType;
use App\Enum\UserStatus;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;
use phpDocumentor\Reflection\Types\Collection;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;

class UserFixtures extends AbstractFixtureEx implements DependentFixtureInterface
{
  public const LOADED_USER_FIXTURES = "loaded_users";

  public function load(ObjectManager $manager): void
  {
    /** @var Collection<Group> $groups */
    $groups = $this->getReferenceArray(AccessControlFixtures::LOADED_ROLE_FIXTURES)->toArray();
    $faker = Factory::create();
    $faker->seed(12345);
    $testing_users = new ArrayCollection();
    /** @var Group $group */
    foreach ($groups as $group) {
      for ($i = 0; $i < 30; $i++) {
        $user = new User();
        $user->addGroup($group);
        $user->setFirstName($faker->firstName);
        $user->setLastName($faker->lastName);
        $user->setEmail("test_" . $group->getGroupType()->slufigy() . "_" . $faker->unique()->randomNumber(3) . "@esprit.tn");
        if ($group->getGroupType() === GroupType::STUDENT())
          $user->setClass("3A" . $faker->randomNumber(2));
        $user->setPhoneNumber("+216" . $faker->randomNumber(8));
        $user->setUserStatus(UserStatus::ACTIVE());
        $user->setIdentityType(DocumentIdentityTypeEnum::Random());
        $user->setIdentityDocumentNumber($faker->randomNumber(8));
        $user->setPlainPassword("12345");
        $user->setAbout($faker->realText(254));
        $user->setCreatedAt($faker->dateTimeBetween("-10 days"));
        $user->setlastActivityAt($faker->dateTimeBetween("-10 days"));
        $manager->persist($user);
        $testing_users->add($user);
      }
    }
    $manager->flush();
    $this->addReferenceArray(self::LOADED_USER_FIXTURES, $testing_users);
    /** @var User $testing_user */
    foreach ($testing_users as $testing_user){
      $testing_user->addContact($this->getSingleRandomItem(self::LOADED_USER_FIXTURES));
      $manager->persist($testing_user);
    }
    $manager->flush();
  }

  public function getDependencies()
  {
    return [AccessControlFixtures::class];
  }
}
