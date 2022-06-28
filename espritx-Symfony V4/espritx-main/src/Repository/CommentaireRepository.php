<?php

namespace App\Repository;

use App\Entity\Commentaire;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method Commentaire|null find($id, $lockMode = null, $lockVersion = null)
 * @method Commentaire|null findOneBy(array $criteria, array $orderBy = null)
 * @method Commentaire[]    findAll()
 * @method Commentaire[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class CommentaireRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Commentaire::class);
    }

    // /**
    //  * @return Commentaire[] Returns an array of Commentaire objects
    //  */
    /*
    public function findByExampleField($value)
    {
        return $this->createQueryBuilder('c')
            ->andWhere('c.exampleField = :val')
            ->setParameter('val', $value)
            ->orderBy('c.id', 'ASC')
            ->setMaxResults(10)
            ->getQuery()
            ->getResult()
        ;
    }
    */

    /*
    public function findOneBySomeField($value): ?Commentaire
    {
        return $this->createQueryBuilder('c')
            ->andWhere('c.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */


    public function getCommentsForPost($postId)
    {
        $qb = $this->createQueryBuilder('c')
            ->select('c')
            ->where('c.post = :post_id')
            ->addOrderBy('c.created_at')
            ->setParameter('post_id', $postId);



        return $qb->getQuery()
            ->getResult();
    }

    public function CountByMonth()
    {
        return $this->createQueryBuilder('c')
            ->select('c.created_at','count(c.id) as cnt','MONTH(c.created_at) AS daycreation')
            ->where('MONTH(c.created_at) BETWEEN 1 AND 6')
            ->groupBy('daycreation')
            ->getQuery()
            ->getResult();
    }
}
