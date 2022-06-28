<?php

namespace App\Repository;

use App\Entity\ServiceRequest;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method ServiceRequest|null find($id, $lockMode = null, $lockVersion = null)
 * @method ServiceRequest|null findOneBy(array $criteria, array $orderBy = null)
 * @method ServiceRequest[]    findAll()
 * @method ServiceRequest[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ServiceRequestRepository extends ServiceEntityRepository
{
    private $manager;

    public function __construct(ManagerRegistry $registry,EntityManagerInterface $manager)
    {
        parent::__construct($registry, ServiceRequest::class);
        $this->manager = $manager;
    }

    // /**
    //  * @return ServiceRequest[] Returns an array of ServiceRequest objects
    //  */
    /*
    public function findByExampleField($value)
    {
        return $this->createQueryBuilder('s')
            ->andWhere('s.exampleField = :val')
            ->setParameter('val', $value)
            ->orderBy('s.id', 'ASC')
            ->setMaxResults(10)
            ->getQuery()
            ->getResult()
        ;
    }
    */

    /*
    public function findOneBySomeField($value): ?ServiceRequest
    {
        return $this->createQueryBuilder('s')
            ->andWhere('s.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
    public function findByResponseTime()
    {
        return $this->createQueryBuilder('sr')
            ->select('AVG(DATE_DIFF( sr.createdAt,sr.RespondedAt ))AS cnt')
            ->getQuery()
            ->getResult();
    }

    public function saveRequest($Title, $Description, $Type, $Email,$User)
    {
        $newreq = new ServiceRequest();

        $newreq
            ->setTitle($Title)
            ->setDescription($Description)
            ->setType($Type)
            ->setRequester($User);
        if ($Email !="")
            $newreq->setEmail($Email);
        $this->manager->persist($newreq);
        $this->manager->flush();
    }

    public function updateRequest(ServiceRequest $req): ServiceRequest
    {
        $this->manager->persist($req);
        $this->manager->flush();

        return $req;
    }

    public function removeRequest(ServiceRequest $req)
    {
        $this->manager->remove($req);
        $this->manager->flush();
    }
}
