package main.presentation;

import java.awt.*;

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
                CtrlPresentation cp = CtrlPresentation.getInstance();
                cp.initiateApp();
                cp.showDocuments(new Point(600, 300), new Dimension(600, 600));
            }
        });
    }
}
