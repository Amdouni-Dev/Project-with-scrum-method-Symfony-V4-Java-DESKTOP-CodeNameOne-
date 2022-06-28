<?php

namespace App\DataFixtures;

use App\Entity\Call;
use App\Entity\Commentaire;
use App\Entity\Event;
use App\Entity\Group;
use App\Entity\Permission;
use App\Entity\Post;
use App\Entity\PostCategory;
use App\Entity\Service;
use App\Entity\ServiceRequest;
use App\Entity\User;
use App\Enum\AccessType;
use App\Enum\GroupType;
use App\Service\AccessControlService;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;

class AccessControlFixtures extends AbstractFixtureEx
{
  public function __construct(private AccessControlService $aclService)
  {
  }

  public const LOADED_ROLE_FIXTURES = "loaded-role-fixtures";

  public function load(ObjectManager $manager): void
  {
    $generator = Factory::create();
    $testing_groups = new ArrayCollection();

    foreach (GroupType::instances() as $groupType) {
      $group = new Group();
      $group->setGroupType($groupType);
      $group->setDisplayName("$groupType group " . $generator->unique()->randomNumber(2));
      $group->setSecurityTitle("ROLE_" . strtoupper(str_replace(" ", "_", $groupType)));
      switch ($groupType) {
        case GroupType::STUDENT():
          $this->aclService->GrantAccess(AccessType::get(AccessType::READ | AccessType::CREATE), Post::class, $group);
          $this->aclService->GrantAccess(AccessType::EDIT(), Post::class, $group, "object.getUser() == user");
          $this->aclService->GrantAccess(AccessType::CREATE(), ServiceRequest::class, $group);
          $this->aclService->GrantAccess(AccessType::get(AccessType::READ | AccessType::EDIT), ServiceRequest::class, $group, "object.Requester == user");
          $this->aclService->GrantAccess(AccessType::READ_CREATE(), Commentaire::class, $group);
          $this->aclService->GrantAccess(AccessType::EDIT(), Commentaire::class, $group, "object.getUser() == user");
          $this->aclService->GrantAccess(AccessType::READ(), User::class, $group, "object.getId() == user.getId()");
          $this->aclService->GrantAccess(AccessType::READ(), Event::class, $group);
          $this->aclService->GrantAccess(AccessType::READ(), Call::class, $group, "object.getUser() == user or object.getUsers().contains(user)");
          $this->aclService->GrantAccess(AccessType::MANAGE(), Call::class, $group, "object.getUser() == user");
          break;
        case GroupType::TEACHERS():
          $this->aclService->GrantAccess(AccessType::READ_CREATE(), Post::class, $group);
          $this->aclService->GrantAccess(AccessType::EDIT(), Post::class, $group, "object.getUser() == user");
          $this->aclService->GrantAccess(AccessType::READ_CREATE(), Commentaire::class, $group);
          $this->aclService->GrantAccess(AccessType::EDIT(), Commentaire::class, $group);
          $this->aclService->GrantAccess(AccessType::CREATE(), ServiceRequest::class, $group);
          $this->aclService->GrantAccess(AccessType::READ_EDIT(), ServiceRequest::class, $group);
          $this->aclService->GrantAccess(AccessType::READ(), Event::class, $group);
          $this->aclService->GrantAccess(AccessType::MANAGE(), Event::class, $group, "object.getUser() == user");
          $this->aclService->GrantAccess(AccessType::READ(), User::class, $group, "object.getId() == user.getId()");
          $this->aclService->GrantAccess(AccessType::MANAGE(), Call::class, $group, "object.getUser() == user");
          $this->aclService->GrantAccess(AccessType::READ(), Group::class, $group);
          $this->aclService->GrantAccess(AccessType::READ(), User::class, $group);
          break;

        case GroupType::FACULTY_STAFF():
          $this->aclService->GrantAccess(AccessType::READ_CREATE(), ServiceRequest::class, $group);
          $this->aclService->GrantAccess(AccessType::DELETE_EDIT(), ServiceRequest::class, $group, "user.getGroups().contains(object.getType().getResponsible())");
          $this->aclService->GrantAccess(AccessType::READ_CREATE(), Post::class, $group);
          $this->aclService->GrantAccess(AccessType::EDIT(), Post::class, $group);
          $this->aclService->GrantAccess(AccessType::READ_CREATE(), Commentaire::class, $group);
          $this->aclService->GrantAccess(AccessType::EDIT(), Commentaire::class, $group);
          $this->aclService->GrantAccess(AccessType::READ(), User::class, $group);
          $this->aclService->GrantAccess(AccessType::READ(), Service::class, $group, "user.getGroups().contains(object.getRecipient())");
          $this->aclService->GrantAccess(AccessType::get(AccessType::CREATE | AccessType::DELETE | AccessType::READ), PostCategory::class, $group);
          $this->aclService->GrantAccess(AccessType::READ(), Event::class, $group);
          $this->aclService->GrantAccess(AccessType::MANAGE(), Event::class, $group, "object.getUser() == user");
          $this->aclService->GrantAccess(AccessType::READ(), Call::class, $group, "object.getUser() == user or object.getUsers().contains(user)");
          $this->aclService->GrantAccess(AccessType::MANAGE(), Call::class, $group, "object.getUser() == user");
          $this->aclService->GrantAccess(AccessType::READ(), Permission::class, $group);
          $this->aclService->GrantAccess(AccessType::READ(), Group::class, $group);
          $this->aclService->GrantAccess(AccessType::MANAGE(), User::class, $group);

          break;
        case GroupType::SITE_STAFF():
          $this->aclService->GrantAccess(AccessType::MANAGE(), User::class, $group);
          $this->aclService->GrantAccess(AccessType::MANAGE(), Group::class, $group);
          $this->aclService->GrantAccess(AccessType::READ_CREATE_DELETE(), Commentaire::class, $group);
          $this->aclService->GrantAccess(AccessType::EDIT(), Commentaire::class, $group);
          $this->aclService->GrantAccess(AccessType::READ_CREATE_DELETE(), Post::class, $group);
          $this->aclService->GrantAccess(AccessType::EDIT(), Post::class, $group);
          $this->aclService->GrantAccess(AccessType::get(AccessType::CREATE | AccessType::DELETE | AccessType::READ), PostCategory::class, $group);
          $this->aclService->GrantAccess(AccessType::MANAGE(), Event::class, $group);
          $this->aclService->GrantAccess(AccessType::MANAGE(), Call::class, $group);
          $this->aclService->GrantAccess(AccessType::MANAGE(), Permission::class, $group);
          $this->aclService->GrantAccess(AccessType::MANAGE(), User::class, $group);
          break;
        default:
          break;
      }
      $manager->persist($group);
      $testing_groups->add($group);
    }
    $manager->flush();
    $this->addReferenceArray(self::LOADED_ROLE_FIXTURES, $testing_groups);
  }

  private function GrantUserRelatedPermissions(Group $group)
  {

  }
}
