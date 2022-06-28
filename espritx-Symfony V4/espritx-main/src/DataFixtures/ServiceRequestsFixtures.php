<?php

namespace App\DataFixtures;

use App\Entity\Group;
use App\Entity\Service;
use App\Entity\ServiceRequest;
use App\Entity\ServiceRequests;
use App\Entity\User;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class ServiceRequestsFixtures extends AbstractFixtureEx implements DependentFixtureInterface
{
  public const LOADED_SERVICE_REQUEST_FIXTURES = "loaded_service_requests";

  public function load(ObjectManager $manager): void
  {
    $service_requests = new ArrayCollection();
    $generator = Factory::create();
    for ($i = 0; $i < 30; $i++) {
      $service_request = new ServiceRequest();
      $service_request->setTitle(str_replace("'", "", implode(" ", $generator->words(2))));
      $service_request->setDescription(str_replace("'", "", $generator->realText(25)));
      /** @var User $requester */
      $requester = $this->getSingleRandomItem(UserFixtures::LOADED_USER_FIXTURES);
      $service_request->setRequester($requester);

      /** @var Service $service_type */
      $service_type = $this->getSingleRandomItem(ServiceFixtures::LOADED_SERVICE_FIXTURES);
      $service_request->setType($service_type);

      $service_request->setRespondedAt(\DateTimeImmutable::createFromMutable($generator->dateTimeBetween("-10 days")));
      $service_request->setCreatedAt($generator->dateTimeBetween("-10 days"));

      $manager->persist($service_request);
      $service_requests->add($service_request);
    }
    $manager->flush();
    $this->addReferenceArray(self::LOADED_SERVICE_REQUEST_FIXTURES, $service_requests);
  }

  public function getDependencies()
  {
    return [UserFixtures::class, ServiceFixtures::class];
  }
}
