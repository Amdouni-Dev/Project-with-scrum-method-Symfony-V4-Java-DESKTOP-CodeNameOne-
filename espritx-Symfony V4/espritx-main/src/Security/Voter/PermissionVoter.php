<?php

namespace App\Security\Voter;

use App\Entity\Group;
use App\Entity\Permission;
use App\Entity\User;
use App\Enum\GroupType;
use App\Repository\PermissionRepository;
use HaydenPierce\ClassFinder\ClassFinder;
use Psr\Log\LoggerInterface;
use Symfony\Component\ExpressionLanguage\Expression;
use Symfony\Component\ExpressionLanguage\ExpressionLanguage;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Authorization\AccessDecisionManagerInterface;
use Symfony\Component\Security\Core\Authorization\Voter\VoterInterface;
use Symfony\Component\Security\Core\Security;

class PermissionVoter implements VoterInterface
{

  /**
   * @var string[]
   */
  private array $subjects;

  public function __construct(
    private LoggerInterface $logger,
  )
  {
    $this->subjects = ClassFinder::getClassesInNamespace('App\Entity');
  }

  public function vote(TokenInterface $token, $subject, array $attributes): int
  {
    // sanity check
    if (!in_array((is_string($subject) ? $subject : get_class($subject)), $this->subjects, true)) {
      $this->logger->info("Subject must be an entity. Current value: $subject");
      return VoterInterface::ACCESS_ABSTAIN;
    }

    // tf you're doing here?
    /** @var User $user */
    $user = $token->getUser();
    if (!$user instanceof User)
      return self::ACCESS_DENIED;

    if ($user->isPartOfGroupType(GroupType::SUPER_ADMIN())) { // Heil Hitler.
      return self::ACCESS_GRANTED;
    }

    $applicable_permissions = array_filter($user->getAggregatePermissions(),
      static fn(Permission $p) => $p->getEnabled() &&
        $p->getSubject() === (is_string($subject) ? $subject : get_class($subject))
    );
    $expressionLanguage = new ExpressionLanguage();
    $decision = self::ACCESS_DENIED;
    /** @var Permission $applicable_permission */
    foreach ($applicable_permissions as $applicable_permission) {
      foreach ($attributes as $attribute) {
        if ($applicable_permission->getAttribute()->hasFlag($attribute)) {
          if (!is_string($subject) && $applicable_permission->getExpression() !== null) {
            // Not gonna even bother preventing an RCE todo.
            $result = $expressionLanguage->evaluate($applicable_permission->getExpression(), [
              'user' => $token->getUser(),
              'object' => $subject
            ]);
            if ($result)
              $decision = self::ACCESS_GRANTED;
          } else $decision = self::ACCESS_GRANTED;
        }
        if ($decision === self::ACCESS_GRANTED) return $decision;
      }
      if ($decision === self::ACCESS_GRANTED) return $decision;
    }
    return $decision;
  }
}
