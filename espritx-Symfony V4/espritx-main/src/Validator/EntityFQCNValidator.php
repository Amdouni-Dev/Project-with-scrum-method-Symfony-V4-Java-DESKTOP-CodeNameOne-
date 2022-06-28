<?php

// src/Validator/ContainsAlphanumericValidator.php
namespace App\Validator;

use Symfony\Component\Validator\Constraint;
use Symfony\Component\Validator\ConstraintValidator;
use Symfony\Component\Validator\Exception\UnexpectedTypeException;
use Symfony\Component\Validator\Exception\UnexpectedValueException;

class EntityFQCNValidator extends ConstraintValidator
{
  public function validate($value, Constraint $constraint)
  {
    if (!$constraint instanceof EntityFQCN) {
      throw new UnexpectedTypeException($constraint, EntityFQCN::class);
    }

    if (null === $value || '' === $value) {
      return;
    }

    if (!is_string($value)) {
      throw new UnexpectedValueException($value, 'string');
    }

    if (!class_exists($value)) {
      // the argument must be a string or an object implementing __toString()
      $this->context->buildViolation($constraint->message)
        ->setParameter('{{ string }}', $value)
        ->addViolation();
    }
  }
}