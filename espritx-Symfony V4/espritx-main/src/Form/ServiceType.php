<?php

namespace App\Form;

use App\Entity\Group;
use App\Entity\Service;
use Doctrine\Common\Collections\ArrayCollection;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ButtonType;
use Symfony\Component\Form\Extension\Core\Type\CollectionType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\FormEvents;

class ServiceType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder

            ->add('Name')
            ->add('Responsible',
                EntityType::class,
                ['class'=>Group::class,
                    'choice_label'=>'display_name',
                    'placeholder'=>'Select the responsible department'])
            ->add('Recipient',
                EntityType::class,
                ['class'=>Group::class,
                    'choice_label'=>'display_name',
                    'expanded'=>true,
                    'multiple'=>true])
            ->add('Other_Fields',
                CollectionType::class,[
                'entry_type' => FieldsType::class,
                'entry_options' => ['label' => false],
                    'allow_add'=>true,
                    'allow_delete'=>true,
                    'by_reference' => false,
                ])
            ->add('Add',SubmitType::class)
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Service::class,
        ]);
    }
}
