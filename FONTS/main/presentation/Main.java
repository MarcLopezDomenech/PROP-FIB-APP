package main.presentation;

import java.awt.*;

/**
 * @mainpage Gestor de documents
 @brief Projecte de PROP Quadrimestre de Tardor 2022 - 2023 <br>
        Grup 33.1: Ariadna Cortés, Pau Duran, Marc López i Marc Valls <br>
        <br>
        Descripció de les classes del projecte.
 */

/**
 * @author marc.valls.camps
 * @class Main
 * @brief Classe que instancia el controlador de presentació i arrenca l'aplicació
 */
public class Main {
    /**
     * @brief Funció que encua un Runnable per fer que l'Event Dispatching Thread arrenqui l'aplicació asíncronament
     */
    public static void main (String[] args) {
        javax.swing.SwingUtilities.invokeLater (new Runnable() {
            public void run()
            {
                CtrlApplication.getInstance().initiateApp();
            }
        });
    }
}
