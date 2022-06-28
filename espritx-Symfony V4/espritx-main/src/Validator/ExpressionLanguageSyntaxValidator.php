<?php

/*
 * This file is part of the Symfony package.
 *
 * (c) Fabien Potencier <fabien@symfony.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

namespace App\Validator;

use Symfony\Component\ExpressionLanguage\ExpressionLanguage;
use Symfony\Component\ExpressionLanguage\SyntaxError;
use Symfony\Component\Validator\Constraint;
use Symfony\Component\Validator\ConstraintValidator;
use Symfony\Component\Validator\Exception\UnexpectedTypeException;
use Symfony\Component\Validator\Exception\UnexpectedValueException;

class ExpressionLanguageSyntaxValidator extends ConstraintValidator
{
  public function validate($value, Constraint $constraint): void
  {
    if (!$constraint instanceof ExpressionLanguageSyntax) {
      throw new UnexpectedTypeException($constraint, ExpressionLanguageSyntax::class);
    }

    if (is_null($value) or empty($value)) return;
    
    if (!\is_string($value)) {
      throw new UnexpectedValueException($value, 'string');
    }

    try {
      (new ExpressionLanguage())->parse($value, $constraint->allowedVariables);
    } catch (SyntaxError $exception) {
      $this->context->buildViolation($constraint->message)
        ->setParameter('{{ syntax_error }}', $this->formatValue($exception->getMessage()))
        ->setInvalidValue($value)
        ->addViolation();
    }
  }
}
