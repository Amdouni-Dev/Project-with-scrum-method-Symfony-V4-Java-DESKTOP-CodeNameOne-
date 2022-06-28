<?php

namespace App\Twig;

use Symfony\Component\DependencyInjection\ContainerInterface;
use Twig\Extension\AbstractExtension;
use Twig\TwigFunction;

class ConfigExtension extends AbstractExtension
{
    private $configs;

    public function __construct()
    {
        $this->configs = [
            'custom' => [
                'mainLayoutType' => 'vertical', // Options[String]: vertical(default), horizontal
                'theme' => 'light', // options[String]: 'light'(default), 'dark', 'bordered', 'semi-dark'
                'sidebarCollapsed' => false, // options[Boolean]: true, false(default) (warning:this option only applies to the vertical theme.)
                'navbarColor' => '', // options[String]: bg-primary, bg-info, bg-warning, bg-success, bg-danger, bg-dark (default: '' for #fff)
                'horizontalMenuType' => 'floating', // options[String]: floating(default) / static /sticky (Warning:this option only applies to the Horizontal theme.)
                'verticalMenuNavbarType' => 'floating', // options[String]: floating(default) / static / sticky / hidden (Warning:this option only applies to the vertical theme)
                'footerType' => 'static', // options[String]: static(default) / sticky / hidden
                'layoutWidth' => 'boxed', // options[String]: full / boxed(default),
                'showMenu' => true, // options[Boolean]: true(default), false //show / hide main menu (Warning: if set to false it will hide the main menu)
                'bodyClass' => '', // add custom class
                'pageHeader' => true, // options[Boolean]: true(default), false (Page Header for Breadcrumbs)
                'contentLayout' => 'default', // options[String]: default, content-left-sidebar, content-right-sidebar, content-detached-left-sidebar, content-detached-right-sidebar (warning:use this option if your whole project with sidenav Otherwise override this option as page level )
                'defaultLanguage' => 'en',    //en(default)/de/pt/fr here are four optional language provided in theme
                'blankPage' => false, // options[Boolean]: true, false(default) (warning:only make true if your whole project without navabr and sidebar otherwise override option page wise)
                'direction' => 'ltr', // Options[String]: ltr(default), rtl
            ],
            'demo-1' => [
                'mainLayoutType' => 'vertical', // Options[String]: vertical(default), horizontal
                'theme' => 'light', // options[String]: 'light'(default), 'dark', 'bordered'
                'sidebarCollapsed' => false, // options[Boolean]: true, false(default) (warning:this option only applies to the vertical theme.)
                'navbarColor' => '', // options[String]: bg-primary, bg-info, bg-warning, bg-success, bg-danger, bg-dark (default: '' for #fff)
                'horizontalMenuType' => 'static', // options[String]: floating(default) / static /sticky (Warning:this option only applies to the Horizontal theme.)
                'verticalMenuNavbarType' => 'floating', // options[String]: floating(default) / static / sticky / hidden (Warning:this option only applies to the vertical theme)
                'footerType' => 'static', // options[String]: static(default) / sticky / hidden
                'layoutWidth' => 'full', // options[String]: full(default) / boxed,
                'showMenu' => true, // options[Boolean]: true(default), false //show / hide main menu (Warning: if set to false it will hide the main menu)
                'bodyClass' => '', // add custom class
                'pageHeader' => true, // options[Boolean]: true(default), false (Page Header for Breadcrumbs)
                'contentLayout' => 'default', // options[String]: default, content-left-sidebar, content-right-sidebar, content-detached-left-sidebar, content-detached-right-sidebar (warning:use this option if your whole project with sidenav Otherwise override this option as page level )
                'defaultLanguage' => 'en',    //en(default)/de/pt/fr here are four optional language provided in theme
                'blankPage' => false, // options[Boolean]: true, false(default) (warning:only make true if your whole project without navabr and sidebar otherwise override option page wise)
                'direction' => 'ltr', // Options[String]: ltr(default), rtl
            ],
            'demo-2' => [
                'mainLayoutType' => 'vertical', // Options[String]: vertical(default), horizontal
                'theme' => 'light', // options[String]: 'light'(default), 'dark', 'bordered'
                'sidebarCollapsed' => true, // options[Boolean]: true, false(default) (warning:this option only applies to the vertical theme.)
                'navbarColor' => '', // options[String]: bg-primary, bg-info, bg-warning, bg-success, bg-danger, bg-dark (default: '' for #fff)
                'horizontalMenuType' => 'static', // options[String]: floating(default) / static /sticky (Warning:this option only applies to the Horizontal theme.)
                'verticalMenuNavbarType' => 'floating', // options[String]: floating(default) / static / sticky / hidden (Warning:this option only applies to the vertical theme)
                'footerType' => 'static', // options[String]: static(default) / sticky / hidden
                'layoutWidth' => 'full', // options[String]: full(default) / boxed,
                'showMenu' => true, // options[Boolean]: true(default), false //show / hide main menu (Warning: if set to false it will hide the main menu)
                'bodyClass' => '', // add custom class
                'pageHeader' => true, // options[Boolean]: true(default), false (Page Header for Breadcrumbs)
                'contentLayout' => 'default', // options[String]: default, content-left-sidebar, content-right-sidebar, content-detached-left-sidebar, content-detached-right-sidebar (warning:use this option if your whole project with sidenav Otherwise override this option as page level )
                'defaultLanguage' => 'en',    //en(default)/de/pt/fr here are four optional language provided in theme
                'blankPage' => false, // options[Boolean]: true, false(default) (warning:only make true if your whole project without navabr and sidebar otherwise override option page wise)
                'direction' => 'ltr', // Options[String]: ltr(default), rtl
            ],
            'demo-3' => [
                'mainLayoutType' => 'vertical', // Options[String]: vertical(default), horizontal
                'theme' => 'bordered', // options[String]: 'light'(default), 'dark', 'bordered'
                'sidebarCollapsed' => false, // options[Boolean]: true, false(default) (warning:this option only applies to the vertical theme.)
                'navbarColor' => '', // options[String]: bg-primary, bg-info, bg-warning, bg-success, bg-danger, bg-dark (default: '' for #fff)
                'horizontalMenuType' => 'static', // options[String]: floating(default) / static /sticky (Warning:this option only applies to the Horizontal theme.)
                'verticalMenuNavbarType' => 'floating', // options[String]: floating(default) / static / sticky / hidden (Warning:this option only applies to the vertical theme)
                'footerType' => 'static', // options[String]: static(default) / sticky / hidden
                'layoutWidth' => 'full', // options[String]: full(default) / boxed,
                'showMenu' => true, // options[Boolean]: true(default), false //show / hide main menu (Warning: if set to false it will hide the main menu)
                'bodyClass' => '', // add custom class
                'pageHeader' => true, // options[Boolean]: true(default), false (Page Header for Breadcrumbs)
                'contentLayout' => 'default', // options[String]: default, content-left-sidebar, content-right-sidebar, content-detached-left-sidebar, content-detached-right-sidebar (warning:use this option if your whole project with sidenav Otherwise override this option as page level )
                'defaultLanguage' => 'en',    //en(default)/de/pt/fr here are four optional language provided in theme
                'blankPage' => false, // options[Boolean]: true, false(default) (warning:only make true if your whole project without navabr and sidebar otherwise override option page wise)
                'direction' => 'ltr', // Options[String]: ltr(default), rtl
            ],
            'demo-4' => [
                'mainLayoutType' => 'vertical', // Options[String]: vertical(default), horizontal
                'theme' => 'dark', // options[String]: 'light'(default), 'dark', 'bordered'
                'sidebarCollapsed' => false, // options[Boolean]: true, false(default) (warning:this option only applies to the vertical theme.)
                'navbarColor' => '', // options[String]: bg-primary, bg-info, bg-warning, bg-success, bg-danger, bg-dark (default: '' for #fff)
                'horizontalMenuType' => 'static', // options[String]: floating(default) / static /sticky (Warning:this option only applies to the Horizontal theme.)
                'verticalMenuNavbarType' => 'floating', // options[String]: floating(default) / static / sticky / hidden (Warning:this option only applies to the vertical theme)
                'footerType' => 'static', // options[String]: static(default) / sticky / hidden
                'layoutWidth' => 'full', // options[String]: full(default) / boxed,
                'showMenu' => true, // options[Boolean]: true(default), false //show / hide main menu (Warning: if set to false it will hide the main menu)
                'bodyClass' => '', // add custom class
                'pageHeader' => true, // options[Boolean]: true(default), false (Page Header for Breadcrumbs)
                'contentLayout' => 'default', // options[String]: default, content-left-sidebar, content-right-sidebar, content-detached-left-sidebar, content-detached-right-sidebar (warning:use this option if your whole project with sidenav Otherwise override this option as page level )
                'defaultLanguage' => 'en',    //en(default)/de/pt/fr here are four optional language provided in theme
                'blankPage' => false, // options[Boolean]: true, false(default) (warning:only make true if your whole project without navabr and sidebar otherwise override option page wise)
                'direction' => 'ltr', // Options[String]: ltr(default), rtl
            ],
            'demo-5' => [
                'mainLayoutType' => 'horizontal', // Options[String]: vertical(default), horizontal
                'theme' => 'light', // options[String]: 'light'(default), 'dark', 'bordered'
                'sidebarCollapsed' => false, // options[Boolean]: true, false(default) (warning:this option only applies to the vertical theme.)
                'navbarColor' => '', // options[String]: bg-primary, bg-info, bg-warning, bg-success, bg-danger, bg-dark (default: '' for #fff)
                'horizontalMenuType' => 'floating', // options[String]: floating(default) / static /sticky (Warning:this option only applies to the Horizontal theme.)
                'verticalMenuNavbarType' => 'floating', // options[String]: floating(default) / static / sticky / hidden (Warning:this option only applies to the vertical theme)
                'footerType' => 'static', // options[String]: static(default) / sticky / hidden
                'layoutWidth' => 'full', // options[String]: full(default) / boxed,
                'showMenu' => true, // options[Boolean]: true(default), false //show / hide main menu (Warning: if set to false it will hide the main menu)
                'bodyClass' => '', // add custom class
                'pageHeader' => true, // options[Boolean]: true(default), false (Page Header for Breadcrumbs)
                'contentLayout' => 'default', // options[String]: default, content-left-sidebar, content-right-sidebar, content-detached-left-sidebar, content-detached-right-sidebar (warning:use this option if your whole project with sidenav Otherwise override this option as page level )
                'defaultLanguage' => 'en',    //en(default)/de/pt/fr here are four optional language provided in theme
                'blankPage' => false, // options[Boolean]: true, false(default) (warning:only make true if your whole project without navabr and sidebar otherwise override option page wise)
                'direction' => 'ltr', // Options[String]: ltr(default), rtl
            ],
            'demo-6' => [
                'mainLayoutType' => 'horizontal', // Options[String]: vertical(default), horizontal
                'theme' => 'dark', // options[String]: 'light'(default), 'dark', 'bordered'
                'sidebarCollapsed' => false, // options[Boolean]: true, false(default) (warning:this option only applies to the vertical theme.)
                'navbarColor' => '', // options[String]: bg-primary, bg-info, bg-warning, bg-success, bg-danger, bg-dark (default: '' for #fff)
                'horizontalMenuType' => 'floating', // options[String]: floating(default) / static /sticky (Warning:this option only applies to the Horizontal theme.)
                'verticalMenuNavbarType' => 'floating', // options[String]: floating(default) / static / sticky / hidden (Warning:this option only applies to the vertical theme)
                'footerType' => 'static', // options[String]: static(default) / sticky / hidden
                'layoutWidth' => 'full', // options[String]: full(default) / boxed,
                'showMenu' => true, // options[Boolean]: true(default), false //show / hide main menu (Warning: if set to false it will hide the main menu)
                'bodyClass' => '', // add custom class
                'pageHeader' => true, // options[Boolean]: true(default), false (Page Header for Breadcrumbs)
                'contentLayout' => 'default', // options[String]: default, content-left-sidebar, content-right-sidebar, content-detached-left-sidebar, content-detached-right-sidebar (warning:use this option if your whole project with sidenav Otherwise override this option as page level )
                'defaultLanguage' => 'en',    //en(default)/de/pt/fr here are four optional language provided in theme
                'blankPage' => false, // options[Boolean]: true, false(default) (warning:only make true if your whole project without navabr and sidebar otherwise override option page wise)
                'direction' => 'ltr', // Options[String]: ltr(default), rtl
            ]
        ];
    }

    public function applClasses()
    {
        // Demo
        /*$fullURL = request()->fullurl();
        if (App()->environment() === 'production') {
            for ($i = 1; $i < 7; $i++) {
                $contains = Str::contains($fullURL, 'demo-' . $i);
                if ($contains === true) {
                    $data = config('custom.' . 'demo-' . $i);
                }
            }
        } else {
        }*/

        $data = $this->configs["demo-1"]; //todo: should have a less dumb logic. Maybe session or db?;

        // default data array
        $DefaultData = [
            'mainLayoutType' => 'vertical',
            'theme' => 'light',
            'sidebarCollapsed' => false,
            'navbarColor' => '',
            'horizontalMenuType' => 'floating',
            'verticalMenuNavbarType' => 'floating',
            'footerType' => 'static', //footer
            'layoutWidth' => 'boxed',
            'showMenu' => true,
            'bodyClass' => '',
            'pageClass' => '',
            'pageHeader' => true,
            'contentLayout' => 'default',
            'blankPage' => false,
            'defaultLanguage' => 'en',
            'direction' => 'ltr',
        ];

        // if any key missing of array from custom.php file it will be merge and set a default value from dataDefault array and store in data variable
        $data = array_merge($DefaultData, $data);

        // All options available in the template
        $allOptions = [
            'mainLayoutType' => array('vertical', 'horizontal'),
            'theme' => array('light' => 'light', 'dark' => 'dark-layout', 'bordered' => 'bordered-layout', 'semi-dark' => 'semi-dark-layout'),
            'sidebarCollapsed' => array(true, false),
            'showMenu' => array(true, false),
            'layoutWidth' => array('full', 'boxed'),
            'navbarColor' => array('bg-primary', 'bg-info', 'bg-warning', 'bg-success', 'bg-danger', 'bg-dark'),
            'horizontalMenuType' => array('floating' => 'navbar-floating', 'static' => 'navbar-static', 'sticky' => 'navbar-sticky'),
            'horizontalMenuClass' => array('static' => '', 'sticky' => 'fixed-top', 'floating' => 'floating-nav'),
            'verticalMenuNavbarType' => array('floating' => 'navbar-floating', 'static' => 'navbar-static', 'sticky' => 'navbar-sticky', 'hidden' => 'navbar-hidden'),
            'navbarClass' => array('floating' => 'floating-nav', 'static' => 'navbar-static-top', 'sticky' => 'fixed-top', 'hidden' => 'd-none'),
            'footerType' => array('static' => 'footer-static', 'sticky' => 'footer-fixed', 'hidden' => 'footer-hidden'),
            'pageHeader' => array(true, false),
            'contentLayout' => array('default', 'content-left-sidebar', 'content-right-sidebar', 'content-detached-left-sidebar', 'content-detached-right-sidebar'),
            'blankPage' => array(false, true),
            'sidebarPositionClass' => array('content-left-sidebar' => 'sidebar-left', 'content-right-sidebar' => 'sidebar-right', 'content-detached-left-sidebar' => 'sidebar-detached sidebar-left', 'content-detached-right-sidebar' => 'sidebar-detached sidebar-right', 'default' => 'default-sidebar-position'),
            'contentsidebarClass' => array('content-left-sidebar' => 'content-right', 'content-right-sidebar' => 'content-left', 'content-detached-left-sidebar' => 'content-detached content-right', 'content-detached-right-sidebar' => 'content-detached content-left', 'default' => 'default-sidebar'),
            'defaultLanguage' => array('en' => 'en', 'fr' => 'fr', 'de' => 'de', 'pt' => 'pt'),
            'direction' => array('ltr', 'rtl'),
        ];

        //if mainLayoutType value empty or not match with default options in custom.php config file then set a default value
        foreach ($allOptions as $key => $value) {
            if (array_key_exists($key, $DefaultData)) {
                if (gettype($DefaultData[$key]) === gettype($data[$key])) {
                    // data key should be string
                    if (is_string($data[$key])) {
                        // data key should not be empty
                        if (isset($data[$key]) && $data[$key] !== null) {
                            // data key should not be exist inside allOptions array's sub array
                            if (!array_key_exists($data[$key], $value)) {
                                // ensure that passed value should be match with any of allOptions array value
                                $result = array_search($data[$key], $value, 'strict');
                                if (empty($result) && $result !== 0) {
                                    $data[$key] = $DefaultData[$key];
                                }
                            }
                        } else {
                            // if data key not set or
                            $data[$key] = $DefaultData[$key];
                        }
                    }
                } else {
                    $data[$key] = $DefaultData[$key];
                }
            }
        }

        //layout classes
        $layoutClasses = [
            'theme' => $data['theme'],
            'layoutTheme' => $allOptions['theme'][$data['theme']],
            'sidebarCollapsed' => $data['sidebarCollapsed'],
            'showMenu' => $data['showMenu'],
            'layoutWidth' => $data['layoutWidth'],
            'verticalMenuNavbarType' => $allOptions['verticalMenuNavbarType'][$data['verticalMenuNavbarType']],
            'navbarClass' => $allOptions['navbarClass'][$data['verticalMenuNavbarType']],
            'navbarColor' => $data['navbarColor'],
            'horizontalMenuType' => $allOptions['horizontalMenuType'][$data['horizontalMenuType']],
            'horizontalMenuClass' => $allOptions['horizontalMenuClass'][$data['horizontalMenuType']],
            'footerType' => $allOptions['footerType'][$data['footerType']],
            'sidebarClass' => '',
            'bodyClass' => $data['bodyClass'],
            'pageClass' => $data['pageClass'],
            'pageHeader' => $data['pageHeader'],
            'blankPage' => $data['blankPage'],
            'blankPageClass' => '',
            'contentLayout' => $data['contentLayout'],
            'sidebarPositionClass' => $allOptions['sidebarPositionClass'][$data['contentLayout']],
            'contentsidebarClass' => $allOptions['contentsidebarClass'][$data['contentLayout']],
            'mainLayoutType' => $data['mainLayoutType'],
            'defaultLanguage' => $allOptions['defaultLanguage'][$data['defaultLanguage']],
            'direction' => $data['direction'],
        ];
        // set default language if session hasn't locale value the set default language
        /*if (!session()->has('locale')) {
            app()->setLocale($layoutClasses['defaultLanguage']);
        }*/

        // sidebar Collapsed
        if ($layoutClasses['sidebarCollapsed'] == 'true') {
            $layoutClasses['sidebarClass'] = "menu-collapsed";
        }

        // blank page class
        if ($layoutClasses['blankPage'] == 'true') {
            $layoutClasses['blankPageClass'] = "blank-page";
        }

        return $layoutClasses;
    }

    public function getFunctions()
    {
        return [
            new TwigFunction("applClasses", [$this, "applClasses"])
        ];
    }

}

?>