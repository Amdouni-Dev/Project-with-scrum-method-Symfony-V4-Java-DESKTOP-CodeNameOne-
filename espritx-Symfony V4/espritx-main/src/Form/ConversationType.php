<?php

namespace App\Form;

use App\Entity\Conversation;
use App\Entity\User;
use App\Form\AbstractBootstrapType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ResetType;

class ConversationType extends AbstractBootstrapType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $options=array_merge($options,[
            'csrf_protection'=>false,
        ]);
        $builder
            ->add('participant1',
                EntityType::class,
                ['class'=>User::class,
                    'choice_label'=>'first_name',
                    'placeholder'=>'Sélectionner le nom de la Personne:'])
        ->add('save',SubmitType::class);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Conversation::class,
        ]);
    }
}
