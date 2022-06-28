<?php

namespace App\Enum;

use App\Traits\RandomizableEnum;
use Elao\Enum\SimpleChoiceEnum;
use ReflectionClass;

/**
 * @extends ReadableEnum<string>
 *
 * @method static ACTIVE
 * @method static PENDING
 * @method static RESTRICTED
 * @method static ALUMNUS
 */
final class UserStatus extends SimpleChoiceEnum
{
  use RandomizableEnum;
  public const ACTIVE = 'active';
  public const PENDING = 'pending';
  public const RESTRICTED = 'restricted';
  public const ALUMNUS = 'alumnus';
}