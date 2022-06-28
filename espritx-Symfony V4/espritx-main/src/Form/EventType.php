<?php

namespace App\Form;

use App\Entity\Event;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\GreaterThanOrEqual;
use Symfony\Component\Validator\Constraints\LessThanOrEqual;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;


class EventType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('title')
            ->add('start',DateTimeType::class,[
                'date_widget'=>'single_text',
                'constraints' => [
                    new LessThanOrEqual([
                        'propertyPath' => 'parent.all[end].data'
                    ]),
                ],

            ])
            ->add('end',DateTimeType::class,[
                'constraints' => [
                    new GreaterThanOrEqual([
                        'propertyPath' => 'parent.all[start].data'
                    ]),
                ],
                'date_widget'=>'single_text',

            ])
            ->add('description',TextareaType::class)
            ->add('allDay')
           // ->add('user')
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Event::class,
        ]);
    }
}
