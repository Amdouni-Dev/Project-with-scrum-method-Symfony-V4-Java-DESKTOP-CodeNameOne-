<?php

namespace App\Form;
use FOS\CKEditorBundle\Form\Type\CKEditorType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Mime\Test\Constraint\EmailTextBodyContains;
use Symfony\Component\OptionsResolver\OptionsResolver;

class PostContactType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('title',TextType::class,[
                'disabled'=>true,
                'attr'=>[
                    'class'=>'form-control'
                ]
            ])
            ->add('email',null,[
                'label'=>'Votre e-mail',
                'attr'=>[
                    'class'=>'form-control'
                ]
            ])
            ->add('message',CKEditorType::class,[
                'label'=>'Votre message'
            ])

        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            // Configure your form options here
        ]);
    }
}
