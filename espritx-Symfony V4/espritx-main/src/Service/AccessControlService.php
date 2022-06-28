<?php

namespace App\Service;

use App\Entity\Group;
use App\Entity\Permission;
use App\Entity\User;
use App\Enum\AccessType;
use App\Repository\GroupRepository;
use App\Repository\PermissionRepository;
use Doctrine\ORM\EntityManagerInterface;
use Exception;
use HaydenPierce\ClassFinder\ClassFinder;

class AccessControlService
{
  private array $subjects;

  public function __construct(
    private EntityManagerInterface $em
  )
  {
    $this->subjects = ClassFinder::getClassesInNamespace('App\Entity');
  }

  public function GrantAccess(AccessType $accessType,
                              string     $subject,
                              Group|User $receiver,
                              string     $expression = null,
                              string     $description = null,
                              string     $title = null,
                              bool       $flush = false)
  {
    $permission = $this->make_permission($accessType, $title, $subject, $expression, $description);
    $this->em->persist($permission);
    if ($receiver instanceof Group) {
      $receiver->addPermission($permission);
    } else {
      $receiver->addIndividualPermission($permission);
    }
    $this->em->persist($receiver);
    if ($flush) {
      $this->em->flush();
    }
  }

  /**
   * @param AccessType $accessType
   * @param string|null $title
   * @param string $subject
   * @param string|null $expression
   * @param string|null $description
   * @return Permission
   */
  private function make_permission(AccessType $accessType, ?string $title, string $subject, ?string $expression, ?string $description): Permission
  {
    $permission = new Permission();
    $permission->setAttribute($accessType);
    $permission->setTitle(is_null($title) ? ($accessType . " " . explode("\\", $subject)[2]) : $title);
    $permission->setExpression($expression);
    $permission->setDescription(is_null($description) ? "N/A" : $description);
    if (!in_array($subject, $this->subjects, true)) {
      throw new \InvalidArgumentException("Trying to set access control on a non entity.");
    }
    $permission->setSubject($subject);
    $permission->setEnabled(true);
    return $permission;
  }

}