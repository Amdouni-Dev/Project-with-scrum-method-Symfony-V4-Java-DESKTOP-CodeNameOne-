<?php

namespace App\Validator;

use Symfony\Component\Validator\Constraint;
use Symfony\Component\Validator\ConstraintValidator;

class EmailDomainValidator extends ConstraintValidator
{
  public function validate($value, Constraint $constraint)
  {
    $explodedEmail = explode('@', $value);
    $domain = array_pop($explodedEmail);

    if (!in_array($domain, $constraint->domains)) {
      $this->context->buildViolation($constraint->message)
        ->setParameter('%domain%', $domain)
        ->addViolation();
    }
  }
}