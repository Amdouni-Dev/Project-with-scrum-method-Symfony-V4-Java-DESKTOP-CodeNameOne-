<?php

namespace App\Form;

use App\Entity\Group;
use App\Entity\Permission;
use App\Entity\User;
use Symfony\Component\Form\Extension\Core\Type\ResetType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class PermissionType extends AbstractBootstrapType
{
  public function buildForm(FormBuilderInterface $builder, array $options): void
  {
    $this
      ->addSimpleTextInput ($builder, "title", "Title", "Should be easy and memorable.")
      ->addSimpleTextInput($builder, "description", "Description", "Describes the intent behind the permission.")
      ->addSimpleTextInput($builder, 'subject', 'Subject', "The entity that will invoke the permission check.")
      ->addSimpleTextInput($builder, 'attribute', 'Attribute', "Operation put on guard.")
      ->addFeatherIconInputGroup($builder, 'expression', 'code', 'Control expression (if needed)')
      ->addSelect2EntityField($builder, 'groups', Group::class, "display_name", 'ajax_autocomplete_groups', "Select groups for this permission..")
      ->addSelect2EntityField($builder, 'users', User::class, "email", 'ajax_autocomplete_users_permission_form', "Or attach permission to users manually")
      ->addSwitchInput($builder, "enabled")
      ->addButton($builder, "save")
      ->addButton($builder, "reset", "btn-outline-secondary", ResetType::class);
  }

  public function configureOptions(OptionsResolver $resolver): void
  {
    $resolver->setDefaults([
      'data_class' => Permission::class,
    ]);
  }
}
