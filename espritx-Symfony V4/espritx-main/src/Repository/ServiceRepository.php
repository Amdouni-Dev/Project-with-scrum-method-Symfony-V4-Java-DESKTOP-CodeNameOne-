<?php

namespace App\Repository;

use App\Entity\Group;
use App\Entity\Service;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method Service|null find($id, $lockMode = null, $lockVersion = null)
 * @method Service|null findOneBy(array $criteria, array $orderBy = null)
 * @method Service[]    findAll()
 * @method Service[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ServiceRepository extends ServiceEntityRepository
{
    private $manager;

    public function __construct(ManagerRegistry $registry, EntityManagerInterface $manager)
    {
        parent::__construct($registry, Service::class);
        $this->manager = $manager;
    }

    // /**
    //  * @return Service[] Returns an array of Service objects
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
    public function findOneBySomeField($value): ?Service
    {
        return $this->createQueryBuilder('s')
            ->andWhere('s.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
    public function saveService($Name, $Responsible, $Recipients)
    {
        $newService = new Service();

        $newService
            ->setName($Name)
            ->setResponsible($Responsible);
        for ($i=0;$i<count($Recipients);$i++) {
            $em=$this->getEntityManager();
            $Recipient = $em->getRepository(Group::class)->findOneBy(['display_name'=>$Recipients[$i]]);
            $newService->addRecipient($Recipient);
        }
        $this->manager->persist($newService);
        $this->manager->flush();
    }

    public function updateService(Service $ser): Service
    {
        $this->manager->persist($ser);
        $this->manager->flush();

        return $ser;
    }

    public function removeService(Service $ser)
    {
        $this->manager->remove($ser);
        $this->manager->flush();
    }
}
