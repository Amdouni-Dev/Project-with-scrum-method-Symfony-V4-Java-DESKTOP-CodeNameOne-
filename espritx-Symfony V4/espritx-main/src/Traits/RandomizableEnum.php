<?php

namespace App\Traits;

use PHPStan\BetterReflection\Reflection\ReflectionClass;

trait RandomizableEnum
{
  public static function Random(): static
  {
    $statuses = array_keys(static::readables());
    return self::get($statuses[array_rand($statuses)]);
  }
}