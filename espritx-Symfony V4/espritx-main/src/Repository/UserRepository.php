<?php

namespace App\Repository;

use App\Entity\User;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\ORM\Query\Expr\Join;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Component\Security\Core\Exception\UnsupportedUserException;
use Symfony\Component\Security\Core\User\PasswordUpgraderInterface;
use Symfony\Component\Security\Core\User\UserInterface;
use DoctrineExtensions\Query\Mysql;

/**
 * @method User|null find($id, $lockMode = null, $lockVersion = null)
 * @method User|null findOneBy(array $criteria, array $orderBy = null)
 * @method User[]    findAll()
 * @method User[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class UserRepository extends ServiceEntityRepository implements PasswordUpgraderInterface
{
  public function __construct(ManagerRegistry $registry, private EntityManagerInterface $em)
  {
    parent::__construct($registry, User::class);
  }

  public function upgradePassword(UserInterface $user, string $newEncodedPassword): void
  {
    if (!$user instanceof User) {
      throw new UnsupportedUserException(sprintf('Instances of "%s" are not supported.', \get_class($user)));
    }
    $user->setPassword($newEncodedPassword);
    $this->_em->persist($user);
    $this->_em->flush();
  }

  public function getCountByStatus()
  {
    $res = $this->createQueryBuilder('u')
      ->groupBy("u.userStatus")
      ->select("u.userStatus as status, COUNT(u) as cnt")
      ->getQuery()
      ->getResult();
    $ret = [];
    foreach ($res as $user_status) {
      $ret[strtolower((string)$user_status["status"])] = $user_status["cnt"];
    }
    $ret["total"] = array_sum(array_values($ret));
    return $ret;
  }

  /**
   * @param User $user1
   * @param User $user2
   * @return User[]
   */
  public function getCommonContacts(User $user1, User $user2): array/*toDO */
  {
    return $this->createQueryBuilder('u')
      ->where(':user1 MEMBER OF u.contacts')
      ->andWhere(':user2 MEMBER OF u.contacts')
      ->setParameter('user1', $user1)
      ->setParameter('user2', $user2)
      ->getQuery()
      ->getResult();
  }

  public function makeFriendSuggestions(User $user, int $limit = 10)/*toDO */
  {
    $pool = [];
    foreach ($user->getGroups() as $group) {
      $pool = array_merge($group->getMembers()->toArray(), $pool);
    }
    shuffle($pool);
    return array_slice($pool, 0, $limit);
  }

  public function CountByDate()
  {
    return $this->createQueryBuilder('u')
      ->select('count(u.id) as cnt', 'DAY(u.createdAt) AS daycreation')
      ->where('DATE_DIFF( CURRENT_DATE(),u.createdAt )<7')
      ->groupBy('daycreation')
      ->getQuery()
      ->getResult();
  }

  public function CountByActivity()
  {
    return $this->createQueryBuilder('u')
      ->select('count(u.lastActivityAt) as cnt', 'DAY(u.lastActivityAt) AS lastactivity')
      ->where('DATE_DIFF( CURRENT_DATE(),u.lastActivityAt )<7')
      ->groupBy('lastactivity')
      ->getQuery()
      ->getResult();
  }

  public function searchUsersByEmail(string $email)
  {
    return $this->em->createQuery(<<<DQL
  select u.email from App\Entity\User u
  where u.email LIKE '%$email%' 
DQL
    )->getArrayResult();
  }
}