<?php

namespace App\Validator;

use Symfony\Component\Validator\Constraint;

/**
 * @Annotation
 */
class EntityFQCN extends Constraint
{
  public $message = '"{{ string }}" is not a valid FQCN. Please provide an FQCN of an object within the application.';
}