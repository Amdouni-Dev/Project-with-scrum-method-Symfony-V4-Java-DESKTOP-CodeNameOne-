<?php

namespace App\Form;

use App\Entity\Service;
use App\Entity\ServiceRequest;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Vich\UploaderBundle\Form\Type\VichFileType;
use Vich\UploaderBundle\Form\Type\VichImageType;

class SerRequestType extends AbstractBootstrapType
{
  public function buildForm(FormBuilderInterface $builder, array $options): void
  {
    $builder
      ->add('Title')
      ->add('Description', TextareaType::class)
      ->add('Email', EmailType::class)
      ->add('PictureFile', VichImageType::class, [
        'allow_delete' => true,
        'delete_label' => "Delete?",
        'image_uri' => false,
        'download_uri' => false,])
      ->add('AttachementsFile', VichFileType::class, [
        'allow_delete' => true,
        'delete_label' => "Delete?",
        'download_uri' => true,
      ])
      ->add('Type', EntityType::class,
        ['class' => Service::class,
          'choice_label' => 'Name'])
      ->add('Envoyer', SubmitType::class);
  }

  public function configureOptions(OptionsResolver $resolver): void
  {
    $resolver->setDefaults([
      'data_class' => ServiceRequest::class,
      'allow_extra_fields' => true
    ]);
  }
}
