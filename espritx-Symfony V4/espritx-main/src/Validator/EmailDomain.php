<?php

namespace App\Validator;

use Symfony\Component\Validator\Constraint;

/**
 * @Annotation
 */
class EmailDomain extends Constraint
{
  public $domains;
  public $message;
  public function __construct($options = null)
  {
    parent::__construct($options);
    $this->message = 'The email @%domain% is not allowed. Only ' . implode(" ", $this->domains).' emails are allowed.';
  }
}