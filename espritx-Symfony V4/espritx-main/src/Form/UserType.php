<?php

namespace App\Form;

use App\Entity\Group;
use App\Entity\Permission;
use App\Entity\User;
use App\Enum\DocumentIdentityTypeEnum;
use App\Enum\UserStatus;
use Elao\Enum\Bridge\Symfony\Form\Type\EnumType;
use Symfony\Component\Form\Extension\Core\Type\ResetType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Vich\UploaderBundle\Form\Type\VichImageType;

class UserType extends AbstractBootstrapType
{
  public function buildForm(FormBuilderInterface $builder, array $options): void
  {
    $this
      ->addSimpleTextInput($builder, 'first_name', "First Name", "Please use your legal name.")
      ->addSimpleTextInput($builder, 'last_name', "Last Name", "Please use your legal name.")
      ->addSimpleTextInput($builder, 'email', "Email", "Email")
      ->addSimpleTextInput($builder, 'class', "Class", "Class currently enrolled in..")
      ->addSimpleTextInput($builder, 'plainPassword', "New Password", "Leave empty if unchanged...")
      ->addSimpleTextInput($builder, 'identityDocumentNumber', "Identity Number", "Full number of the identity provider selected")
      ->addSimpleTextInput($builder, 'phoneNumber', "Phone Number", "Please include a country code if outside Tunisia")
      ->addSelect2EntityField($builder, 'groups', Group::class, "display_name", 'ajax_autocomplete_groups_user_form', "Select groups for this user..")
      ->addSelect2EntityField($builder, 'individualPermissions', Permission::class, "title", 'ajax_autocomplete_permissions_user_form', "Or attach individual permissions..")
      ->addButton($builder, "save")
      ->addButton($builder, "reset", "btn-outline-secondary", ResetType::class);

    $builder->add('userStatus', EnumType::class, [
      'enum_class' => UserStatus::class,
    ]);

    $builder->add('identityType', EnumType::class, [
      'enum_class' => DocumentIdentityTypeEnum::class,
    ]);

    $builder->add("avatarFile", VichImageType::class, [
      'allow_delete' => true,
      'delete_label' => "Delete?",
      'image_uri' => false,
      'download_uri' => false,
    ]);
  }

  public function configureOptions(OptionsResolver $resolver): void
  {
    $resolver->setDefaults([
      'data_class' => User::class,
      'allow_extra_fields' => true
    ]);
  }
}
