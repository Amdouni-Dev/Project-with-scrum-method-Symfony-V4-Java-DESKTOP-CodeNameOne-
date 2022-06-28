<?php

namespace App\Form;

use App\Entity\Group;
use App\Entity\Permission;
use App\Entity\User;
use App\Enum\UserStatus;
use Elao\Enum\Bridge\Symfony\Form\Type\EnumType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ResetType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class GroupType extends AbstractBootstrapType
{
  public function buildForm(FormBuilderInterface $builder, array $options): void
  {
    $this
      ->addInputGroup($builder, "security_title", "ROLE_", "Security title internally used by the system. Think about logs.")
      ->addFloatingLabelTextInput($builder, "display_name", "Display Name", "Short title to describe who's here.")
      ->addSelect2EntityField($builder, 'members', User::class, "email", 'ajax_autocomplete_users_group_form', "Members of the group")
      ->addSelect2EntityField($builder, 'permissions', Permission::class, "title", 'ajax_autocomplete_permissions', "Group Permissions")
      ->addButton($builder, "save")
      ->addButton($builder, "reset", "btn-outline-secondary", ResetType::class);
    $builder->add('groupType', EnumType::class, [
      'enum_class' => \App\Enum\GroupType::class,
    ]);
    $builder->add("enjoyable_services")->add("provided_services");

  }

  public function configureOptions(OptionsResolver $resolver): void
  {
    $resolver->setDefaults([
      'data_class' => Group::class,
      'allow_extra_fields' => true,
      'csrf_protection' => false,
    ]);
  }
}
