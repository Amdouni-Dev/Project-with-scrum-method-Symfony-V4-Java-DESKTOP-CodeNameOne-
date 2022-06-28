<?php

namespace App\Twig;

use Symfony\Component\DependencyInjection\ContainerInterface;
use Twig\Extension\AbstractExtension;
use Twig\TwigFunction;

class MenuExtension extends AbstractExtension
{
    private string $template_directory_path;

    function __construct(ContainerInterface $container)
    {
        $this->template_directory_path = $container->getParameter("twig.default_path");
    }

    public function getFunctions(): array
    {
        return [
            new TwigFunction("horizontalMenuData", [$this, "horizontalMenuData"]),
            new TwigFunction("verticalMenuData", [$this, "verticalMenuData"]),
        ];
    }

    public function horizontalMenuData()
    {
        $path = $this->template_directory_path . DIRECTORY_SEPARATOR . "menu-data". DIRECTORY_SEPARATOR . "horizontalMenu.json";
        return json_decode(file_get_contents($path), true);
    }
    public function verticalMenuData()
    {
        $path = $this->template_directory_path . DIRECTORY_SEPARATOR . "menu-data". DIRECTORY_SEPARATOR . "verticalMenu.json";
        return json_decode(file_get_contents($path), true);
    }
}

?>