<?php

namespace App\Controller;

use App\Enum\GroupType;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;


class TestController extends AbstractController
{

    // root
    /**
     * @Route("/",name="root")
     */
    public function index()
    {
        $user = $this->getUser();
        if ($user) {
            foreach ($user->getGroups() as $group) {
                if ($group->getGroupType() === GroupType::STUDENT() || $group->getGroupType() === GroupType::TEACHERS()) {
                    return $this->redirectToRoute('show_my_profile');
                }
            }
            return $this->redirectToRoute('app_admin_home');
        }
        else {
            return $this->redirectToRoute("login");
        }
    }
    /**
     * @Route("/testuas",name="testuas")
     */
    public function testuas(){
        return $this->render('views/content/_partials/_modals/modal-add-new-address.html.twig');
    }
    // invoice list App
    /**
     * @Route("/app/invoice/list",name="app-invoice-list")
     */
    public function invoice_list()
    {
        $pageConfigs = ['pageHeader' => false];

        return $this->render('views/content/apps/invoice/app-invoice-list.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // invoice preview App
    /**
     * @Route("app/invoice/preview",name="app-invoice-preview")
     */
    public function invoice_preview()
    {
        $pageConfigs = ['pageHeader' => false];

        return $this->render('views/content/apps/invoice/app-invoice-preview.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // invoice edit App
    /**
     * @Route("app/invoice/edit",name="app-invoice-edit")
     */
    public function invoice_edit()
    {
        $pageConfigs = ['pageHeader' => false];

        return $this->render('views/content/apps/invoice/app-invoice-edit.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // invoice edit App
    /**
     * @Route("app/invoice/add",name="app-invoice-add")
     */
    public function invoice_add()
    {
        $pageConfigs = ['pageHeader' => false];

        return $this->render('views/content/apps/invoice/app-invoice-add.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // invoice print App
    /**
     * @Route("app/invoice/print",name="app-invoice-print")
     */
    public function invoice_print()
    {
        $pageConfigs = ['pageHeader' => false];

        return $this->render('views/content/apps/invoice/app-invoice-print.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // User List Page
    /**
     * @Route("app/user/list",name="app-user-list")
     */
    public function user_list()
    {
        $pageConfigs = ['pageHeader' => false];
        return $this->render('views/content/apps/user/app-user-list.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // User Account Page
    /**
     * @Route("app/user/view/account",name="app-user-view-account")
     */
    public function user_view_account()
    {
        $pageConfigs = ['pageHeader' => false];
        return $this->render('views/content/apps/user/app-user-view-account.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // User Security Page
    /**
     * @Route("app/user/view/security",name="app-user-view-security")
     */
    public function user_view_security()
    {
        $pageConfigs = ['pageHeader' => false];
        return $this->render('views/content/apps/user/app-user-view-security.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // User Billing and Plans Page
    /**
     * @Route("app/user/view/billing",name="app-user-view-billing")
     */
    public function user_view_billing()
    {
        $pageConfigs = ['pageHeader' => false];
        return $this->render('views/content/apps/user/app-user-view-billing.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // User Notification Page
    /**
     * @Route("app/user/view/notifications",name="app-user-view-notifications")
     */
    public function user_view_notifications()
    {
        $pageConfigs = ['pageHeader' => false];
        return $this->render('views/content/apps/user/app-user-view-notifications.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // User Connections Page

    /**
     * @Route("app/user/view/connections",name="app-user-view-connections")
     */
    public function user_view_connections()
    {
        $pageConfigs = ['pageHeader' => false];
        return $this->render('views/content/apps/user/app-user-view-connections.html.twig', ['pageConfigs' => $pageConfigs]);
    }


    // Chat App
    /**
     * @Route("app/chat",name="app-chat")
     */
    public function chatApp()
    {
        $pageConfigs = [
            'pageHeader' => false,
            'contentLayout' => "content-left-sidebar",
            'pageClass' => 'chat-application',
        ];

        return $this->render('views/content/apps/chat/app-chat.html.twig', [
            'pageConfigs' => $pageConfigs
        ]);
    }

    /* // Calender App
    /**
     * @Route("app/calendar",name="app-calendar")
     
    public function calendarApp()
    {
        $pageConfigs = [
            'pageHeader' => false
        ];

        return $this->render('all_events_data/app-calendar.html.twig', [
            'pageConfigs' => $pageConfigs
        ]);
    } */   

    // Email App
    /**
     * @Route("app/email",name="app-email")
     */
    public function emailApp()
    {
        $pageConfigs = [
            'pageHeader' => false,
            'contentLayout' => "content-left-sidebar",
            'pageClass' => 'email-application',
        ];

        return $this->render('views/content/apps/email/app-email.html.twig', ['pageConfigs' => $pageConfigs]);
    }
    /**
     * @Route("app/todo",name="app-todo")
     */
    public function todoApp()
    {
        $pageConfigs = [
            'pageHeader' => false,
            'contentLayout' => "content-left-sidebar",
            'pageClass' => 'todo-application',
        ];

        return $this->render('views/content/apps/todo/app-todo.html.twig', [
            'pageConfigs' => $pageConfigs
        ]);
    }
    // File manager App
    /**
     * @Route("app/file-manager",name="app-file-manage")
     */
    public function file_manager()
    {
        $pageConfigs = [
            'pageHeader' => false,
            'contentLayout' => "content-left-sidebar",
            'pageClass' => 'file-manager-application',
        ];

        return $this->render('views/content/apps/fileManager/app-file-manager.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // Access Roles App
    /**
     * @Route("app/access-roles",name="app-access-roles")
     */
    public function access_roles()
    {
        $pageConfigs = ['pageHeader' => false,];

        return $this->render('views/content/apps/rolesPermission/app-access-roles.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // Access permission App
    /**
     * @Route("app/access-permission",name="app-access-permission")
     */
    public function access_permission()
    {
        $pageConfigs = ['pageHeader' => false,];

        return $this->render('views/content/apps/rolesPermission/app-service-form.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // Kanban App
    /**
     * @Route("app/kanban",name="app-kanban")
     */
    public function kanbanApp()
    {
        $pageConfigs = [
            'pageHeader' => false,
            'pageClass' => 'kanban-application',
        ];

        return $this->render('views/content/apps/kanban/app-kanban.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // Ecommerce Shop
    /**
     * @Route("app/ecommerce/shop",name="app-ecommerce-shop")
     */
    public function ecommerce_shop()
    {
        $pageConfigs = [
            'contentLayout' => "content-detached-left-sidebar",
            'pageClass' => 'ecommerce-application',
        ];

        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "eCommerce"], ['name' => "Shop"]
        ];

        return $this->render('views/content/apps/ecommerce/app-ecommerce-shop.html.twig', [
            'pageConfigs' => $pageConfigs,
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Ecommerce Details
    /**
     * @Route("app/ecommerce/details",name="app-ecommerce-details")
     */
    public function ecommerce_details()
    {
        $pageConfigs = [
            'pageClass' => 'ecommerce-application',
        ];

        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "eCommerce"], ['link' => "/app/ecommerce/shop", 'name' => "Shop"], ['name' => "Details"]
        ];

        return $this->render('views/content/apps/ecommerce/app-ecommerce-details.html.twig', [
            'pageConfigs' => $pageConfigs,
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Ecommerce Wish List
    /**
     * @Route("app/ecommerce/wishlist",name="app-ecommerce-wishlist")
     */
    public function ecommerce_wishlist()
    {
        $pageConfigs = [
            'pageClass' => 'ecommerce-application',
        ];

        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "eCommerce"], ['name' => "Wish List"]
        ];

        return $this->render('views/content/apps/ecommerce/app-ecommerce-wishlist.html.twig', [
            'pageConfigs' => $pageConfigs,
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Ecommerce Checkout
    /**
     * @Route("app/ecommerce/checkout",name="app-ecommerce-checkout")
     */
    public function ecommerce_checkout()
    {
        $pageConfigs = [
            'pageClass' => 'ecommerce-application',
        ];

        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "eCommerce"], ['name' => "Checkout"]
        ];

        return $this->render('views/content/apps/ecommerce/app-ecommerce-checkout.html.twig', [
            'pageConfigs' => $pageConfigs,
            'breadcrumbs' => $breadcrumbs
        ]);
    }
    // Dashboard - Analytics
    /**
     * @Route("dashboard/analytics",name="dashboard-analytics")
     */
    public function dashboardAnalytics()
    {
        $pageConfigs = ['pageHeader' => false];

        return $this->render('views/content/dashboard/dashboard-analytics.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // Dashboard - Ecommerce
    /**
     * @Route("dashboard/ecommerce",name="dashboard-ecommerce")
     */
    public function dashboardEcommerce()
    {
        $pageConfigs = ['pageHeader' => false];

        return $this->render('views/content/dashboard/dashboard-ecommerce.html.twig', ['pageConfigs' => $pageConfigs]);
    }
    // Content Typography
    /**
     * @Route("ui/typography",name="ui-typography")
     */
    public function typography()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "UI"], ['name' => "Typography"]
        ];
        return $this->render('views/content/ui-pages/ui-typography.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Icons Feather
    /**
     * @Route("icons/feather",name="icons-feather")
     */
    public function icons_feather()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "UI"], ['name' => "Feather Icons"]
        ];
        return $this->render('views/content/ui-pages/icons-feather.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }


    /**
     * @Route("card/basic",name="card-basic")
     */
    public function card_basic()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Card"], ['name' => "Basic Card"]
        ];
        return $this->render('views/content/cards/card-basic.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Card Advance
    /**
     * @Route("card/advance",name="card-advance")
     */
    public function card_advance()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Card"], ['name' => "Advance Card"]
        ];
        return $this->render('views/content/cards/card-advance.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Card Statistics
    /**
     * @Route("card/statistics",name="card-statistics")
     */
    public function card_statistics()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Card"], ['name' => "Statistics Cards"]
        ];
        return $this->render('views/content/cards/card-statistics.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Card Analytics
    /**
     * @Route("card/analytics",name="card-analytics")
     */
    public function card_analytics()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Card"], ['name' => "Analytics Cards"]
        ];
        return $this->render('views/content/cards/card-analytics.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Card Actions
    /**
     * @Route("card/actions",name="card-actions")
     */
    public function card_actions()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Card"], ['name' => "Card Actions"]
        ];
        return $this->render('views/content/cards/card-actions.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }
    // Component accordion
    /**
     * @Route("component/accordion",name="component-accordion")
     */
    public function accordion()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Accordion"]
        ];
        return $this->render('views/content/components/component-accordion.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Alert
    /**
     * @Route("component/alert",name="component-alert")
     */
    public function alert()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Alerts"]
        ];
        return $this->render('views/content/components/component-alerts.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component - Avatar
    /**
     * @Route("component/avatar",name="component-avatar")
     */
    public function avatar()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Avatar"]
        ];
        return $this->render('views/content/components/component-avatar.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Badges
    /**
     * @Route("component/badges",name="component-badges")
     */
    public function badges()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Badges"]
        ];
        return $this->render('views/content/components/component-badges.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Breadcrumbs
    /**
     * @Route("component/breadcrumbs",name="component-breadcrumbs")
     */
    public function breadcrumbs()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Breadcrumbs"]
        ];
        return $this->render('views/content/components/component-breadcrumbs.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Buttons

    /**
     * @Route("component/buttons",name="component-buttons")
     */
    public function buttons()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Buttons"]
        ];
        return $this->render('views/content/components/component-buttons.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Carousel
    /**
     * @Route("component/carousel",name="component-carousel")
     */
    public function carousel()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Carousel"]
        ];
        return $this->render('views/content/components/component-carousel.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Collapse
    /**
     * @Route("component/collapse",name="component-collapse")
     */
    public function collapse()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Collapse"]
        ];
        return $this->render('views/content/components/component-collapse.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component - Divider
    /**
     * @Route("component/divider",name="component-divider")
     */
    public function divider()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Divider"]
        ];
        return $this->render('views/content/components/component-divider.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Dropdowns
    /**
     * @Route("component/dropdowns",name="component-dropdowns")
     */
    public function dropdowns()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Dropdowns"]
        ];
        return $this->render('views/content/components/component-dropdowns.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component List Group
    /**
     * @Route("component/list-group",name="component-list-group")
     */
    public function list_group()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "List Group"]
        ];
        return $this->render('views/content/components/component-list-group.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Modals
    /**
     * @Route("component/modals",name="component-modals")
     */
    public function modals()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Modals"]
        ];
        return $this->render('views/content/components/component-modals.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Navs
    /**
     * @Route("component/navs",name="component-navs")
     */
    public function navs()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Navs"]
        ];
        return $this->render('views/content/components/component-navs.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component offcanvas
    /**
     * @Route("component/offcanvas",name="component-offcanvas")
     */
    public function offcanvas()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "OffCanvas"]
        ];
        return $this->render('views/content/components/component-offcanvas.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Pagination
    /**
     * @Route("component/pagination",name="component-pagination")
     */
    public function pagination()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Pagination"]
        ];
        return $this->render('views/content/components/component-pagination.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Pill Badges
    /**
     * @Route("component/pill-badges",name="component-pill-badges")
     */
    public function pill_badges()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Pill Badges"]
        ];
        return $this->render('views/content/components/component-pill-badges.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Pills
    /**
     * @Route("component/pills",name="component-pills")
     */
    public function pills()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Pills"]
        ];
        return $this->render('views/content/components/component-pills.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Tabs
    /**
     * @Route("component/tabs",name="component-tabs")
     */
    public function tabs()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Tabs"]
        ];
        return $this->render('views/content/components/component-tabs.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }


    // Component Tooltips
    /**
     * @Route("component/tooltips",name="component-tooltips")
     */
    public function tooltips()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Tooltips"]
        ];
        return $this->render('views/content/components/component-tooltips.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Popovers
    /**
     * @Route("component/popovers",name="component-popovers")
     */
    public function popovers()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Popovers"]
        ];
        return $this->render('views/content/components/component-popovers.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Progress
    /**
     * @Route("component/progress",name="component-progress")
     */
    public function progress()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Progress"]
        ];
        return $this->render('views/content/components/component-progress.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Spinner
    /**
     * @Route("component/spinner",name="component-spinner")
     */
    public function spinner()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Spinner"]
        ];
        return $this->render('views/content/components/component-spinner.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Timeline
    /**
     * @Route("component/timeline",name="component-timeline")
     */
    public function timeline()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Timeline"]
        ];
        return $this->render('views/content/components/component-timeline.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Component Toast
    /**
     * @Route("component/toast",name="component-toast")
     */
    public function toast()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Components"], ['name' => "Toasts"]
        ];
        return $this->render('views/content/components/component-bs-toast.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Sweet Alert
    /**
     * @Route("ext-component/sweet-alerts",name="ext-component-sweet-alerts")
     */
    public function sweet_alert()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Sweet Alerts"]
        ];
        return $this->render('views/content/extensions/ext-component-sweet-alerts.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // block ui
    /**
     * @Route("ext-component/block-ui",name="ext-component-block-ui")
     */
    public function block_ui()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "BlockUI"]
        ];
        return $this->render('views/content/extensions/ext-component-block-ui.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Toastr
    /**
     * @Route("ext-component/toastr",name="ext-component-toastr")
     */
    public function toastr()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Toastr"]
        ];
        return $this->render('views/content/extensions/ext-component-toastr.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // NoUi Slider
    /**
     * @Route("ext-component/sliders",name="ext-component-sliders")
     */
    public function sliders()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Sliders"]
        ];
        return $this->render('views/content/extensions/ext-component-sliders.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Drag Drop
    /**
     * @Route("ext-component/drag-drop",name="ext-component-drag-drop")
     */
    public function drag_drop()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Drag & Drop"]
        ];
        return $this->render('views/content/extensions/ext-component-drag-drop.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Tour
    /**
     * @Route("ext-component/tour",name="ext-component-tour")
     */
    public function tour()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Tour"]
        ];
        return $this->render('views/content/extensions/ext-component-tour.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Clipboard
    /**
     * @Route("ext-component/clipboard",name="ext-component-tour")
     */
    public function clipboard()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Clipboard"]
        ];
        return $this->render('views/content/extensions/ext-component-clipboard.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Media Player
    /**
     * @Route("ext-component/plyr",name="ext-component-plyr")
     */
    public function plyr()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Media Player"]
        ];
        return $this->render('views/content/extensions/ext-component-media-player.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Context Menu
    /**
     * @Route("ext-component/context-menu",name="ext-component-context-menu")
     */
    public function context_menu()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Context Menu"]
        ];
        return $this->render('views/content/extensions/ext-component-context-menu.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // swiper
    /**
     * @Route("ext-component/swiper",name="ext-component-swiper")
     */
    public function swiper()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Swiper"]
        ];
        return $this->render('views/content/extensions/ext-component-swiper.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // tree
    /**
     * @Route("ext-component/tree",name="ext-component-tree")
     */
    public function tree()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Tree"]
        ];
        return $this->render('views/content/extensions/ext-component-tree.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // ratings
    /**
     * @Route("ext-component/ratings",name="ext-component-ratings")
     */
    public function ratings()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Ratings"]
        ];
        return $this->render('views/content/extensions/ext-component-ratings.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // I18n
    /**
     * @Route("ext-component/locale",name="ext-component-locale")
     */
    public function locale()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Extensions"], ['name' => "Locale"]
        ];
        return $this->render('views/content/locale/locale.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Collapsed Menu Layout
    /**
     * @Route("page-layouts/collapsed-menu",name="page-layouts-collapsed-menu")
     */
    public function layout_collapsed_menu()
    {
        $pageConfigs = ['sidebarCollapsed' => true];
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Layouts"], ['name' => "Collapsed menu"]];
        return $this->render('views/content/page-layouts/layout-collapsed-menu.html.twig', ['pageConfigs' => $pageConfigs, 'breadcrumbs' => $breadcrumbs]);
    }

    // Boxed Layout
    /**
     * @Route("page-layouts/full",name="page-layouts-full")
     */
    public function layout_full()
    {
        $pageConfigs = ['layoutWidth' => 'full'];
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Layouts"], ['name' => "Layout Full"]];
        return $this->render('views/content/page-layouts/layout-full.html.twig', ['pageConfigs' => $pageConfigs, 'breadcrumbs' => $breadcrumbs]);
    }
    // Layout Without Menu
    /**
     * @Route("page-layouts/without-menu",name="page-layouts-without-menu")
     */
    public function layout_without_menu()
    {
        $pageConfigs = ['showMenu' => false];
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Layouts"], ['name' => "Layout without menu"]];
        return $this->render('views/content/page-layouts/layout-without-menu.html.twig', ['pageConfigs' => $pageConfigs, 'breadcrumbs' => $breadcrumbs]);
    }
    // Empty Layout
    /**
     * @Route("page-layouts/empty",name="page-layouts-empty")
     */
    public function layout_empty()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Layouts"], ['name' => "Layout Empty"]];
        return $this->render('views/content/page-layouts/layout-empty.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }
    // Blank Layout
    /**
     * @Route("page-layouts/blank",name="page-layouts-blank")
     */
    public function layout_blank()
    {
        $pageConfigs = ['blankPage' => true];
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Layouts"], ['name' => "Layout Blank"]];
        return $this->render('views/content/page-layouts/layout-blank.html.twig', ['pageConfigs' => $pageConfigs, 'breadcrumbs' => $breadcrumbs]);
    }
    // Form Elements - Input
    /**
     * @Route("form/input",name="form-input")
     */
    public function input()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Input"]
        ];
        return $this->render('views/content/forms/form-elements/form-input.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Elements - Input-groups
    /**
     * @Route("form/input-groups",name="form-input-groups")
     */
    public function input_groups()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Input Groups"]
        ];
        return $this->render('views/content/forms/form-elements/form-input-groups.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Elements - Input-mask
    /**
     * @Route("form/input-mask",name="form-input-mask")
     */
    public function input_mask()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Input Mask"]
        ];
        return $this->render('views/content/forms/form-elements/form-input-mask.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Elements - Textarea
    /**
     * @Route("form/textarea",name="form-textarea")
     */
    public function textarea()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Textarea"]
        ];
        return $this->render('views/content/forms/form-elements/form-textarea.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Elements - Checkbox
    /**
     * @Route("form/checkbox",name="form-checkbox")
     */
    public function checkbox()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Checkbox"]
        ];
        return $this->render('views/content/forms/form-elements/form-checkbox.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Elements - Radio
    /**
     * @Route("form/radio",name="form-radio")
     */
    public function radio()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Radio"]
        ];
        return $this->render('views/content/forms/form-elements/form-radio.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Elements - custom_options
    /**
     * @Route("form/custom-options",name="form-custom-options")
     */
    public function custom_options()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Custom Options"]
        ];
        return $this->render('views/content/forms/form-elements/form-custom-options.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Elements - Switch
    /**
     * @Route("form/switch",name="form-switch")
     */
    public function switch()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Switch"]
        ];
        return $this->render('views/content/forms/form-elements/form-switch.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Elements - Select
    /**
     * @Route("form/select",name="form-select")
     */
    public function select()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Select"]
        ];
        return $this->render('views/content/forms/form-elements/form-select.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Elements - Number Input
    /**
     * @Route("form/number-input",name="form-number-input")
     */
    public function number_input()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Number Input"]
        ];
        return $this->render('views/content/forms/form-elements/form-number-input.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // File Uploader
    /**
     * @Route("form/file-uploader",name="form-file-uploader")
     */
    public function file_uploader()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "File Uploader"]
        ];
        return $this->render('views/content/forms/form-elements/form-file-uploader.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Quill Editor
    /**
     * @Route("form/quill-editor",name="form-quill-editor")
     */
    public function quill_editor()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Quill Editor"]
        ];
        return $this->render('views/content/forms/form-elements/form-quill-editor.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Elements - Date & time Picker
    /**
     * @Route("form/date-time-picker",name="form-date-time-picker")
     */
    public function date_time_picker()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Form Elements"], ['name' => "Date & Time Picker"]
        ];
        return $this->render('views/content/forms/form-elements/form-date-time-picker.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Layouts
    /**
     * @Route("form/layouts",name="form-layouts")
     */
    public function layouts()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Forms"], ['name' => "Form Layouts"]
        ];
        return $this->render('views/content/forms/form-layout.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Wizard
    /**
     * @Route("form/wizard",name="form-wizard")
     */
    public function wizard()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Forms"], ['name' => "Form Wizard"]
        ];
        return $this->render('views/content/forms/form-wizard.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Form Validation
    /**
     * @Route("form/validation",name="form-validation")
     */
    public function validation()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Forms"], ['name' => "Form Validation"]
        ];
        return $this->render('views/content/forms/form-validation.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }
    // Form repeater
    /**
     * @Route("form/form-repeater",name="form-repeater")
     */
    public function form_repeater()
    {
        $breadcrumbs = [
            ['name' => "Home"],
            ['name' => "Forms"],
            ['name' => "Form Repeater"]
        ];
        return $this->render('views/content/forms/form-repeater.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }
    // Bootstrap Table
    /**
     * @Route("table",name="table")
     */
    public function table()
    {
        $breadcrumbs = [['name' => "Home"], ['name' => "Table Bootstrap"]];
        return $this->render('views/content/table/table-bootstrap/table-bootstrap.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }

    // Datatable Basic
    /**
     * @Route("table/datatable/basic",name="datatable-basic")
     */
    public function datatable_basic()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Datatable"], ['name' => "Basic"]];
        return $this->render('views/content/table/table-datatable/table-datatable-basic.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // Datatable Basic
    /**
     * @Route("table/datatable/advance",name="datatable-advance")
     */
    public function datatable_advance()
    {
        $breadcrumbs = [
            ['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Datatable"], ['name' => "Advanced"]
        ];
        return $this->render('views/content/table/table-datatable/table-datatable-advance.html.twig', [
            'breadcrumbs' => $breadcrumbs
        ]);
    }
    // Account Settings account
    /**
     * @Route("page/account-settings-account",name="page-account-settings-account")
     */
    public function account_settings_account()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Account Settings"], ['name' => "Account"]];
        return $this->render('views/content/pages/page-account-settings-account.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // Account Settings security
    /**
     * @Route("page/account-settings-security",name="page-account-settings-security")
     */
    public function account_settings_security()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Account Settings"], ['name' => "Security"]];
        return $this->render('views/content/pages/page-account-settings-security.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // Account Settings billing
    /**
     * @Route("page/account-settings-billing",name="page-account-settings-billing")
     */
    public function account_settings_billing()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Account Settings"], ['name' => "Billing & Plans"]];
        return $this->render('views/content/pages/page-account-settings-billing.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // Account Settings notifications
    /**
     * @Route("page/account-settings-notifications",name="page-account-settings-notifications")
     */
    public function account_settings_notifications()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Account Settings"], ['name' => "Notifications"]];
        return $this->render('views/content/pages/page-account-settings-notifications.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // Account Settings connections
    /**
     * @Route("page/account-settings-connections",name="page-account-settings-connections")
     */
    public function account_settings_connections()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Account Settings"], ['name' => "Connections"]];
        return $this->render('views/content/pages/page-account-settings-connections.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // Profile
    /**
     * @Route("page/profile",name="page-profile")
     */
    public function profile()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Pages"], ['name' => "Profile"]];

        return $this->render('views/content/pages/page-profile.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // FAQ
    /**
     * @Route("page/faq",name="page-faq")
     */
    public function faq()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Pages"], ['name' => "FAQ"]];
        return $this->render('views/content/pages/page-faq.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // Knowledge Base
    /**
     * @Route("page/knowledge-base",name="page-knowledge-base")
     */
    public function knowledge_base()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Pages"], ['name' => "Knowledge Base"]];
        return $this->render('views/content/pages/page-knowledge-base.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // Knowledge Base Category
    /**
     * @Route("page/knowledge-base/category",name="page-knowledge-base")
     */
    public function kb_category()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Pages"], ['link' => "/page/knowledge-base", 'name' => "Knowledge Base"], ['name' => "Category"]];
        return $this->render('views/content/pages/page-kb-category.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // Knowledge Base Question
    /**
     * @Route("page/knowledge-base/category/question",name="page-knowledge-base")
     */
    public function kb_question()
    {
        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Pages"], ['link' => "/page/knowledge-base", 'name' => "Knowledge Base"], ['link' => "/page/knowledge-base/category", 'name' => "Category"], ['name' => "Question"]];
        return $this->render('views/content/pages/page-kb-question.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // pricing
    /**
     * @Route("page/pricing",name="page-pricing")
     */
    public function pricing()
    {
        $pageConfigs = ['pageHeader' => false];
        return $this->render('views/content/pages/page-pricing.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // api key
    /**
     * @Route("page/api-key",name="page-api-key")
     */
    public function api_key()
    {
        $pageConfigs = ['pageHeader' => false];
        return $this->render('views/content/pages/page-api-key.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // blog list
    /**
     * @Route("page/blog-list",name="page-blog-list")
     */
    public function blog_list()
    {
        $pageConfigs = ['contentLayout' => 'content-detached-right-sidebar', 'bodyClass' => 'content-detached-right-sidebar'];

        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Pages"], ['link' => "javascript:void(0)", 'name' => "Blog"], ['name' => "List"]];

        return $this->render('views/content/pages/page-blog-list.html.twig', ['breadcrumbs' => $breadcrumbs, 'pageConfigs' => $pageConfigs]);
    }

    // blog detail
    /**
     * @Route("page/blog-detail",name="page-blog-detail")
     */
    public function blog_detail()
    {
        $pageConfigs = ['contentLayout' => 'content-detached-right-sidebar', 'bodyClass' => 'content-detached-right-sidebar'];

        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Pages"], ['link' => "javascript:void(0)", 'name' => "Blog"], ['name' => "Detail"]];

        return $this->render('views/content/pages/page-blog-detail.html.twig', ['breadcrumbs' => $breadcrumbs, 'pageConfigs' => $pageConfigs]);
    }

    // blog edit
    /**
     * @Route("page/blog-edit",name="page-blog-edit")
     */
    public function blog_edit()
    {

        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Pages"], ['link' => "javascript:void(0)", 'name' => "Blog"], ['name' => "Edit"]];

        return $this->render('views/content/pages/page-blog-edit.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // modal examples
    /**
     * @Route("page/modal-examples",name="modal-examples")
     */
    public function modal_examples()
    {

        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['name' => "Modal Examples"]];

        return $this->render('views/content/pages/modal-examples.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }

    // license
    /**
     * @Route("page/license",name="license")
     */
    public function license()
    {

        $breadcrumbs = [['link' => "/", 'name' => "Home"], ['link' => "javascript:void(0)", 'name' => "Pages"], ['name' => "License"]];

        return $this->render('views/content/pages/page-license.html.twig', ['breadcrumbs' => $breadcrumbs]);
    }
    // Coming Soon
    /**
     * @Route("page/coming-soon",name="coming-soon")
     */
    public function coming_soon()
    {
        $pageConfigs = ['blankPage' => true];

        return $this->render('views/content/miscellaneous/page-coming-soon.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // Error
    /**
     * @Route("page/error",name="error")
     */
    public function error()
    {
        $pageConfigs = ['blankPage' => true];

        return $this->render('views/content/miscellaneous/error.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    // Not-authorized
    /**
     * @Route("page/not-authorized",name="not-authorized")
     */
    public function not_authorized()
    {
        $pageConfigs = ['blankPage' => true];

        return $this->render('views/content/miscellaneous/page-not-authorized.html.twig', ['pageConfigs' => $pageConfigs]);
    }

    /**
     * @Route("auth/register",name="cov")
     */
    public function register_cover()
    {
        $pageConfigs = ['blankPage' => true];

        return $this->render('views/content/authentication/auth-register-cover.html.twig', ['pageConfigs' => $pageConfigs]);
    }
    // Maintenance
    /**
     * @Route("page/maintenance",name="maintenance")
     */
    public function maintenance()
    {
        $pageConfigs = ['blankPage' => true];

        return $this->render('views/content/miscellaneous/page-maintenance.html.twig', [
            'pageConfigs' => $pageConfigs
        ]);
    }
}


?>
