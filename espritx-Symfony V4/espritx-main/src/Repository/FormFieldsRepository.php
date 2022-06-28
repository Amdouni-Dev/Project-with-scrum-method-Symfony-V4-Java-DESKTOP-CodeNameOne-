<?php

namespace App\Repository;

use App\Entity\FormFields;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method FormFields|null find($id, $lockMode = null, $lockVersion = null)
 * @method FormFields|null findOneBy(array $criteria, array $orderBy = null)
 * @method FormFields[]    findAll()
 * @method FormFields[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class FormFieldsRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, FormFields::class);
    }

    // /**
    //  * @return FormFields[] Returns an array of FormFields objects
    //  */
    /*
    public function findByExampleField($value)
    {
        return $this->createQueryBuilder('f')
            ->andWhere('f.exampleField = :val')
            ->setParameter('val', $value)
            ->orderBy('f.id', 'ASC')
            ->setMaxResults(10)
            ->getQuery()
            ->getResult()
        ;
    }
    */

    /*
    public function findOneBySomeField($value): ?FormFields
    {
        return $this->createQueryBuilder('f')
            ->andWhere('f.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
}
