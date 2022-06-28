<?php

namespace App\DataFixtures;

use App\Entity\Commentaire;
use App\Entity\Group;
use App\Entity\GroupPost;
use App\Entity\Images;
use App\Entity\Post;
use App\Entity\User;
use App\Enum\DocumentIdentityTypeEnum;
use App\Enum\GroupType;
use App\Enum\UserStatus;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\DataFixtures\DependentFixtureInterface;
use Doctrine\Persistence\ObjectManager;
use Faker\Factory;
use phpDocumentor\Reflection\Types\Collection;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;

class PostGroupPostFixtures extends AbstractFixtureEx implements DependentFixtureInterface
{
  public const LOADED_POST_FIXTURES = "loaded_posts";
  public const LOADED_GROUP_FIXTURES = "loaded_groups";
  public const LOADED_COMMENT_FIXTURES = "loaded_groups";

  public function load(ObjectManager $manager): void
  {
    $generator = Factory::create();

    //// Testing groups
    $testing_groups = new ArrayCollection();
    for ($i = 0; $i <= 6; $i++) {
      $groupPost = new GroupPost();
      $groupPost->setUser($this->getSingleRandomItem(UserFixtures::LOADED_USER_FIXTURES));
      for ($j = 0; $j <= random_int(10, 20); $j++) {
        $groupPost->addMembre($this->getSingleRandomItem(UserFixtures::LOADED_USER_FIXTURES));
      }
      $groupPost->setBut($generator->realText(32));
      $groupPost->setNomGroupe($generator->name);
      $groupPost->setImage("content-img-1.jpg");
      $groupPost->setCreatedAt(\DateTimeImmutable::createFromMutable($generator->dateTimeBetween("-15 days", 'now')));
      $groupPost->setIsValid(true);
      $groupPost->setIsDeleted(false);
      $manager->persist($groupPost);
      $testing_groups->add($groupPost);
    }
    $this->addReferenceArray(self::LOADED_GROUP_FIXTURES, $testing_groups);
    $manager->flush();

    ///// Testing posts
    $testing_posts = new ArrayCollection();
    $testing_pictures = ["content-img-1.jpg", "content-img-2.jpg", "content-img-3.jpg", "content-img-4.jpg"];
    for ($i = 0; $i <= 10; $i++) {
      $post = new Post();
      $post->setUser($this->getSingleRandomItem(UserFixtures::LOADED_USER_FIXTURES));
      $post->setContent($generator->realText(128));
      $post->setTitle($generator->realText(32));
      $post->setIsValid(true);
      $post->setSlug("slug-post-" . $generator->randomNumber(2));
      $post->setCreatedAt(\DateTimeImmutable::createFromMutable($generator->dateTimeBetween("-15 days", 'now')));
      $post->setUpdatedAt(\DateTimeImmutable::createFromMutable($generator->dateTimeBetween("-15 days", 'now')));
      $post->setGroupPost($this->getSingleRandomItem(self::LOADED_GROUP_FIXTURES));
      $post->setIsDeleted(false);
      $image = new Images();
      $image->setName($testing_pictures[array_rand($testing_pictures)]);
      $post->addImage($image);
      $post->setLatitude("36.76994563296694");
      $post->setLongitude("9.084594623126216");
      $post->setLocalisation($generator->realText(16));
      $manager->persist($image);
      $manager->persist($post);
      $testing_posts->add($post);
    }
    $manager->flush();
    $this->addReferenceArray(self::LOADED_POST_FIXTURES, $testing_posts);

    ///// Testing comments
    $testing_comments = new ArrayCollection();
    for ($i = 0; $i <= 30; $i++){
      $comment = new Commentaire();
      $comment->setCreatedAt(\DateTimeImmutable::createFromMutable($generator->dateTimeBetween("-15 days", 'now')));
      $comment->setContent($generator->realText());
      $comment->setUser($this->getSingleRandomItem(UserFixtures::LOADED_USER_FIXTURES));
      $comment->setPost($this->getSingleRandomItem(self::LOADED_POST_FIXTURES));
      $manager->persist($comment);
    }
    $manager->flush();
    $this->addReferenceArray(self::LOADED_COMMENT_FIXTURES, $testing_comments);
  }

  public function getDependencies()
  {
    return [UserFixtures::class];
  }
}
