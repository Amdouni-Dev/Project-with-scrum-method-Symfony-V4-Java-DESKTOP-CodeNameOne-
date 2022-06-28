<?php

namespace App\Enum;

use App\Traits\RandomizableEnum;
use Elao\Enum\FlaggedEnum;

/**
 * @method static MANAGE
 * @method static EDIT
 * @method static READ
 * @method static CREATE
 * @method static READ_CREATE_EDIT
 * @method static READ_CREATE
 * @method static READ_EDIT
 * @method static READ_CREATE_DELETE
 * @method static DELETE_EDIT
 */
final class AccessType extends FlaggedEnum
{
  use RandomizableEnum;

  public const MANAGE = self::CREATE | self::EDIT | self::READ | self::DELETE;
  public const READ_CREATE_EDIT = self::READ | self::CREATE | self::EDIT;
  public const READ_CREATE = self::READ | self::CREATE;
  public const READ_CREATE_DELETE = self::READ | self::CREATE | self::DELETE;
  public const READ_EDIT = self::READ | self::EDIT;
  public const DELETE_EDIT = self::DELETE | self::EDIT;
  public const READ = 1;
  public const CREATE = 2;
  public const DELETE = 4;
  public const EDIT = 8;

  public static function values(): array
  {
    return [
      self::CREATE, self::EDIT, self::READ, self::DELETE
    ];
  }



  public static function readables(): array
  {
    return [
      self::CREATE => "Create",
      self::EDIT => "Edit",
      self::READ => "Read",
      self::DELETE => "Delete",

      // Combinations
      self::MANAGE => "Manage",
      self::DELETE_EDIT => "Delete and Edit",
      self::READ_CREATE_EDIT => "Read, Create & Delete",
    ];
  }
}