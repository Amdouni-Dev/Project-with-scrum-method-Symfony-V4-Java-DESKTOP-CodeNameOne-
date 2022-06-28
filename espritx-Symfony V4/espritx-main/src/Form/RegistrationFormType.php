<?php

namespace App\Form;

use App\Entity\User;
use App\Validator\EmailDomain;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\Email;
use Symfony\Component\Validator\Constraints\IsTrue;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\NotBlank;

class RegistrationFormType extends AbstractType
{
  public function buildForm(FormBuilderInterface $builder, array $options): void
  {

    $builder
      ->add("firstName", TextType::class, [
        "attr" => [
          "placeholder" => "Please use your legal first name."
        ]
      ])
      ->add('lastName', TextType::class, [
        "attr" => [
          "placeholder" => "Please use your legal last name."
        ]
      ])
      ->add('email', EmailType::class, [
        "attr" => [
          "placeholder" => "@esprit.tn Email"
        ],
        'constraints' => [
          new EmailDomain(["domains" => ["esprit.tn"]])
        ]
      ])
      ->add('agreeTerms', CheckboxType::class, [
        'mapped' => false,
        'label' => "Agree to usage terms",
        'constraints' => [
          new IsTrue([
            'message' => 'You should agree to our terms.',
          ]),
        ],
      ])
      ->add('plainPassword', PasswordType::class, [
        'attr' => [
          'autocomplete' => 'new-password',
          'placeholder' => "A strong, memorable password.."
        ]
      ]);
  }

  public function configureOptions(OptionsResolver $resolver): void
  {
    $resolver->setDefaults([
      'data_class' => User::class,
    ]);
  }
}
