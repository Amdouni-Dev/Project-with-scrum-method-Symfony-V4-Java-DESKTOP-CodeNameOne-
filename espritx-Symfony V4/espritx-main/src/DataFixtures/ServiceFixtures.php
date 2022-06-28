<?php

namespace App\DataFixtures;

use App\Entity\Group;
use App\Entity\Service;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class ServiceFixtures extends AbstractFixtureEx implements DependentFixtureInterface
{
  public const LOADED_SERVICE_FIXTURES = "loaded_services";

  public function load(ObjectManager $manager): void
  {
    $groups = $this->getReferenceArray(AccessControlFixtures::LOADED_ROLE_FIXTURES)->toArray();
    $generator = Factory::create();
    $testing_services = new ArrayCollection();
    for ($i = 0; $i < 10; $i++) {
      $service = new Service();
      $service->setName("Demand of " . str_replace("'", "", implode(" ", $generator->words(2))));
      /** @var Group $group */
      foreach ($this->sampleSubArray($groups, 1, 2) as $group){
        $service->addRecipient($group);
      }
      $service->setResponsible($groups[array_rand($groups)]);
      $manager->persist($service);
      $testing_services->add($service);
    }
    $manager->flush();
    $this->addReferenceArray(self::LOADED_SERVICE_FIXTURES, $testing_services);
  }

  public function getDependencies()
  {
    return [AccessControlFixtures::class];
  }
}
