package main.presentation;

import main.excepcions.ExceptionDocumentExists;
import main.excepcions.ExceptionExpressionExists;
import main.excepcions.ExceptionInvalidExpression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @class CtrlApplication
 * @brief Subcontrolador de la capa de presentació de l'aplicatiu. S'encarrega de la gestió de la posada en marxa, aturada i reset de l'aplicatiu.
 * @author pau.duran.manzano
 */
public class CtrlApplication {
    /**
     * \brief Objecte singleton que guarda la única instància del CtrlApplication
     */
    private static CtrlApplication singletonObject;

    /**
     * \brief Instància del controlador de presentació (CtrlPresentation)
     */
    private static CtrlPresentation cp;

    /**
     * @brief Constructora per defecte de CtrlApplication
     * @details Es defineix com a privada perquè CtrlApplication és singleton
     * @pre No existeix cap instància de CtrlApplication ja creada
     * @return CtrlApplication
     */
    private CtrlApplication() {
        cp = CtrlPresentation.getInstance();
    }

    /**
     * @brief Funció per aconseguir la instància de CtrlApplication
     * @details Com que CtrlApplication és singleton, cal aquesta funció per retornar la seva única instància
     * @return CtrlApplication: Retorna la instància de CtrlApplication
     */
    public static CtrlApplication getInstance() {
        if (singletonObject == null) singletonObject = new CtrlApplication();
        return singletonObject;
    }

    /**
     * @brief Funció que inicialitza l'aplicatiu amb la còpia de seguretat i programa un backup periòdic
     * @details Amb aquest mètode es permet inicialitzar l'aplicatiu a partir de l'última còpia de seguretat de què es disposi,
     * i a més programa l'autosalvat del sistema cada 30 segons, tot i que es fa al tancar l'apliació, per seguretat
     * @pre Si existeix una còpia de seguretat, aquestà és vàlida
     * @post El sistema queda inicialitzat amb la darrera còpia de seguretat del sistema, i a partir d'ara cada 30 segons el
     * sistema s'autoguardarà
     */
    public void initiateApp() {
        // Marc temporal per posicionar diàlegs d'error
        JFrame reference = new JFrame();
        reference.setLocation(new Point(600, 300));

        // Restaurem l'estat del sistema
        try {
            cp.restoreSystem();
        } catch (FileNotFoundException e) {
            // Vol dir que és el primer cop que encenem l'aplicatiu, no tenim còpia de seguretat, no passa res.
        } catch (ExceptionDocumentExists | ExceptionInvalidExpression | ExceptionExpressionExists e) {
            // No hauría de passar mai
            CtrlViewsDialogs.getInstance().showInternalError(reference);
        }

        // Programem el guardat del sistema periòdicament cada 30 segons (per seguretat)
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                try {
                    cp.saveSystem();
                } catch (IOException e) {
                    // Tenim un problema greu
                    CtrlViewsDialogs.getInstance().showInternalError(reference);
                }
            }
        };
        t.scheduleAtFixedRate(tt, 30000, 30000);


        // Obrim la vista principal
        CtrlViewsDialogs.getInstance().showDocuments(new Point(600, 300), new Dimension(600, 600));
    }

    /**
     * @brief Funció que deixa l'estat del sistema en una còpia de seguretat
     * @details Aquest mètode s'ha de cridar abans de tancar l'aplicatiu per tal de salvar-ne el sistema localment i poder-lo restablir en la següent posada en marxa
     * @param reference Frame de referència per saber on posicionar el dialog en cas d'error
     * @post El sistema no queda alterat, però es genera una còpia de seguretat a partir de l'estat actual, que queda emmagatzemada
     */
    public void closeApp(JFrame reference) {
        try {
            cp.saveSystem();
        } catch (IOException e) {
            // Tenim un problema greu
            CtrlViewsDialogs.getInstance().showInternalError(reference);
        }
    }

    /**
     * @brief Operació per tal de resetejar el sistema
     * @details Amb aquest mètode podem esborrar tota la informació del sistema, deixant-lo buit
     * @post El sistema queda buit, és a dir, deixa de tenir documents i expressions donades d'alta
     */
    public void reset() {
        cp.resetSystem();
    }
}
