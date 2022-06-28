<?php

namespace App\Repository;

use App\Entity\Commentaire;
use App\Entity\Post;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method Post|null find($id, $lockMode = null, $lockVersion = null)
 * @method Post|null findOneBy(array $criteria, array $orderBy = null)
 * @method Post[]    findAll()
 * @method Post[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class PostRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Post::class);
    }

    // /**
    //  * @return Post[] Returns an array of Post objects
    //  */
    /*
    public function findByExampleField($value)
    {
        return $this->createQueryBuilder('p')
            ->andWhere('p.exampleField = :val')
            ->setParameter('val', $value)
            ->orderBy('p.id', 'ASC')
            ->setMaxResults(10)
            ->getQuery()
            ->getResult()
        ;
    }
    */

    /*
    public function findOneBySomeField($value): ?Post
    {
        return $this->createQueryBuilder('p')
            ->andWhere('p.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
    public function changeValidite(Post $post){
        $em=$this->getEntityManager();
        if ($post->isValid())
            $post->setIsValid(false);
        else
            $post->setIsValid(true);
        $em->persist($post);
        $em->flush();
        return $post;
    }
    public function changeDelete(Post $post){
        $em=$this->getEntityManager();
        if ($post->getIsDeleted())
            $post->setIsDeleted(false);
        else
            $post->setIsDeleted(true);
        $em->persist($post);
        $em->flush();
        return $post;
    }




    public function getLatestPosts($limit = null,$filters=null)
    {


        $qb = $this->createQueryBuilder('b')
            ->select('b')
            ->addOrderBy('b.created_at', 'DESC');
        if($filters != null){
$qb->andWhere('b.groupPost IN (:grps)')
    ->setParameter(':grps',array_values($filters));
        }
        if (false === is_null($limit)){
            $qb->setMaxResults($limit);}

        return $qb->getQuery()
            ->getResult();
    }
    public function CommentsMaxQuatre()
    {
        $queryBuilder = $this->getEntityManager()->createQueryBuilder();
        $queryBuilder->select('o')
            ->from(Commentaire::class, 'o')
            ->orderBy('o.created_at','DESC')
            ->setMaxResults(4);

        $query = $queryBuilder->getQuery();
        return $query->getResult();


    }

    public function PostsMaxQuatre()
    {


        $queryBuilder = $this->createQueryBuilder('o')
        ->select('o')
            ->where('o.isValid = 1')
            ->orderBy('o.created_at','DESC')
            ->setMaxResults(3);

        $query = $queryBuilder->getQuery();
        return $query->getResult();}



    public function PostforApi()
    {


        $queryBuilder = $this->createQueryBuilder('o')
            ->select('o')

            ->orderBy('o.created_at','DESC');


        $query = $queryBuilder->getQuery();
        return $query->getResult();}

}



