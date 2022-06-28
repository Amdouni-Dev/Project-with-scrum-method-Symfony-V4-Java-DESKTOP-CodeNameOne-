<?php

namespace App\Form;

use App\Entity\GroupPost;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class GroupPostType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nomGroupe',TextType::class , [
                'attr' => ['autofocus' => true],
                'label' => 'Taper le nom de votre groupe',
            ])

            ->add('image', FileType::class, array(
                'label' => 'Charger une image ',
                'required' => false,
                'data_class' => null
            ))
            ->add('but', TextareaType::class, array(
                'label' => 'Taper le but de votre groupe ',

            ))


        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => GroupPost::class,
        ]);
    }
}
