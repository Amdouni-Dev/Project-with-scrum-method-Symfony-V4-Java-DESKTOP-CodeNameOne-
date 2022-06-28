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

use Symfony\Component\Validator\Constraint;

/**
 * @Annotation
 */
class ExpressionLanguageSyntax extends Constraint
{
  public $message = 'Syntax error: {{ syntax_error }}';
  public $allowedVariables = [];

  public function __construct(array $options = null,
                              string $message = null,
                              array $allowedVariables = [])
  {
    parent::__construct($options);
    $this->message = $message ?? $this->message;
    $this->allowedVariables = $allowedVariables ?? $this->allowedVariables;
  }
  public function validatedBy(): string
  {
    return static::class . 'Validator';
  }
}
