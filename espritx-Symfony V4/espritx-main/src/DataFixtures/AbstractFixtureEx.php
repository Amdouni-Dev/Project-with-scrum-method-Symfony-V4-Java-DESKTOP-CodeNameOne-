<?php

namespace App\DataFixtures;

use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Common\Collections\ArrayCollection;

abstract class AbstractFixtureEx extends Fixture
{
  public function addReferenceArray(string $name, ArrayCollection $object): void
  {
    for ($i = 0; $i < $object->count(); $i++) {
      $this->setReference("$name-$i", $object[$i]);
    }
  }

  public function getReferenceArray(string $name): ArrayCollection
  {
    $collection = new ArrayCollection();
    $i = 0;
    while ($this->hasReference("$name-$i")) {
      $collection->add($this->getReference("$name-$i"));
      $i++;
    }
    return $collection;
  }

  public function getSingleRandomItem(string $collection_reference)
  {
    $arr = $this->getReferenceArray($collection_reference)->toArray();
    return $arr[array_rand($arr)];
  }

  public function sampleReferenceArray(string $collection_reference, $n_min, $n_max)
  {
    $arr = $this->getReferenceArray($collection_reference)->toArray();
    return $this->sampleSubArray($arr, $n_min, $n_max);
  }

  public function sampleSubArray(array $original_array, $n_min, $n_max)
  { //todo: wipe out?
    $indices = array_rand($original_array, random_int($n_min, $n_max));
    if (!is_array($indices)) {
      $indices = [$indices];
    }
    $sample = [];
    foreach ($indices as $index) {
      $sample[] = $original_array[$index];
    }
    return $sample;
  }
}