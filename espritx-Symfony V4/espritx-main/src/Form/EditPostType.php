<?php

namespace App\Form;

use App\Entity\Post;

use Hoa\Iterator\Map;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class EditPostType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('title',TextType::class , [
                'attr' => ['autofocus' => true],
                'label' => 'Taper le titre de votre post',
            ])
            //  ->add('slug')

            ->add('content', TextType::class, [
                'attr' => ['rows' => 10],
                'help' => "merci d'accepter les regles d'utilisation",
                'label' => 'Ecrire ....',
            ])
            ->add('images', FileType::class,[
                'label' => false,
                'multiple' => true,
                'mapped' => false,
                'required' => false
            ])



            // ->add('created_at')
            //   ->add('active')
            //   ->add('user')

        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {

        $resolver->setDefaults(array(
            'data_class' => Post::class,
        ));
    }
}
