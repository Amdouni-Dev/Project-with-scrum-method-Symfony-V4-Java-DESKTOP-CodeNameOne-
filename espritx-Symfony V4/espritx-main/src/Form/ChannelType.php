<?php

namespace App\Form;

use App\Entity\Channel;
use App\Entity\User;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ResetType;

class ChannelType extends AbstractBootstrapType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $this
            ->addSelect2EntityField($builder, "participants", User::class, "email", 'ajax_autocomplete_chat_permission_form', "Select email of wanted user")
            ->addButton($builder, "save")
            ->addButton($builder, "reset", "btn-outline-secondary", ResetType::class);

    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Channel::class,
        ]);
    }
}
