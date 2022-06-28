package edu.esprit.gui.Feed.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class WebMap extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        /* Création de la WebView et du moteur */
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        /* Charge la carte HTML avec Leafletjs */
        webEngine.loadContent("<!DOCTYPE html>\n"
                + "<html lang=\"fr\">\n"
                + "\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>Carte</title>\n"
                + "    <!-- leafletjs CSS -->\n"
                + "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/leaflet.min.css\" />\n"
                + "    <!-- leafletjs JS -->\n"
                + "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/leaflet.min.js\"></script>\n"
                + "    <style>\n"
                + "        /* Carte plein écran */\n"
                + "        html,\n"
                + "        body {\n"
                + "            margin: 0;\n"
                + "            height: 100%;\n"
                + "        }\n"
                + "\n"
                + "        #map {\n"
                + "            width: 100%;\n"
                + "            height: 100%;\n"
                +"              border: none;              \n "
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + "\n"
                +"        <iframe id=\"map\" src=\"https://www.openstreetmap.org/export/embed.html?bbox=2.1203613281250004%2C48.75550973660054%2C2.5735473632812504%2C48.961961958462446&amp;layer=mapnik\"></iframe>\n"
                + "    <!-- L'endroit ou la carte va s'afficher -->\n"
                + "    <div id=\"map\"></div>\n"
                + "\n"
                + "    <script>\n"
                + "        /* Les options pour afficher la France */\n"
                + "        const mapOptions = {\n"
                + "            center: [46.225, 0.132],\n"
                + "            zoom: 5\n"
                + "        }\n"
                + "\n"
                + "        /* Les options pour affiner la localisation */\n"
                + "        const locationOptions = {\n"
                + "            maximumAge: 10000,\n"
                + "            timeout: 5000,\n"
                + "            enableHighAccuracy: true\n"
                + "        };\n"
                + "\n"
                + "        /* Création de la carte */\n"
                + "        var map = new L.map(\"map\", mapOptions);\n"
                + "\n"
                + "        /* Création de la couche OpenStreetMap */\n"
                + "        var layer = new L.TileLayer(\"http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png\",\n"
                + "            { attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors' });\n"
                + "\n"
                + "        /* Ajoute la couche de la carte */\n"
                + "        map.addLayer(layer);\n"
                + "\n"
                + "        /* Verifie que le navigateur est compatible avec la géolocalisation */\n"
                + "        if (\"geolocation\" in navigator) {\n"
                + "            navigator.geolocation.getCurrentPosition(handleLocation, handleLocationError, locationOptions);\n"
                + "        } else {\n"
                + "            /* Le navigateur n'est pas compatible */\n"
                + "            alert(\"Géolocalisation indisponible\");\n"
                + "        }\n"
                + "\n"
                + "        function handleLocation(position) {\n"
                + "            /* Zoom avant de trouver la localisation */\n"
                + "            map.setZoom(16);\n"
                + "            /* Centre la carte sur la latitude et la longitude de la localisation de l'utilisateur */\n"

                + "            map.panTo(new L.LatLng(position.coords.latitude, position.coords.longitude));\n"
                + "        }\n"
                + "\n"
                + "        function handleLocationError(msg) {\n"
                + "            alert(\"Erreur lors de la géolocalisation\");\n"
                + "        }\n"
                + "\n"
                + "    </script>\n"
                + "\n"
                + "</body>\n"
                + "\n"
                + "</html>");

        Scene scene = new Scene(webView, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}