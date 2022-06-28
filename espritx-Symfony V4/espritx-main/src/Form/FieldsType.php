<?php

namespace App\Form;

use App\Entity\Fields;
use Doctrine\DBAL\Types\TextType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class FieldsType extends AbstractBootstrapType
{
  public function buildForm(FormBuilderInterface $builder, array $options): void
  {
    $this->addSimpleTextInput($builder, "name", "", "Field name");
    $builder
      ->add('Type', ChoiceType::class, [
        'choices' => [
          'Text' => "TextType",
          'Email' => "EmailType",
          'Number' => "NumberType",
          'Date' => "DateType",
          'Date and Time' => "DateTimeType",
          'Country' => "CountryType",
          'File' => "FileType",
        ],
        'expanded' => false,
        'multiple' => false,
        'placeholder' => "Data type",
      ]);
    $this->addSwitchInput($builder, "Required", true,);
  }

  public function configureOptions(OptionsResolver $resolver): void
  {
    $resolver->setDefaults([
      'data_class' => Fields::class,
    ]);
  }
}
