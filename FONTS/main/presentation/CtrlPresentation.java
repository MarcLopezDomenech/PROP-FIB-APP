package main.presentation;

/**
 * @class CtrlPresentation
 * @brief Controlador de la capa de presentació de l'aplicatiu. S'encarrega de la part gràfica de l'aplicatiu, és a dir, de la gestió de les vistes. Alhora, però, requereix de la comunicació amb el controlador de domini per resoldre peticions i mostrar informació.
 * @author pau.duran.manzano
 */
public class CtrlPresentation {
    /**
     * \brief Objecte singleton que guarda la única instància del CtrlDomain
     */
    private static CtrlPresentation singletonObject;

    /**
     * @brief Constructora per defecte de CtrlPresentation
     * @details Es defineix com a privada perquè CtrlPresentation és singleton
     * @pre No existeix cap instància de CtrlPresentation ja creada
     * @return CtrlPresentation
     */
    private CtrlPresentation() {
    }

    /**
     * @brief Funció per aconseguir la instància de CtrlPresentation
     * @details Com que CtrlPresentation és singleton, cal aquesta funció per retornar la seva única instància
     * @return CtrlPresentation: Retorna la instància de CtrlPresentation
     */
    public static CtrlPresentation getInstance() {
        if (singletonObject == null) singletonObject = new CtrlPresentation();
        return singletonObject;
    }

    public static void main(String[] args) {
        DialogError dialog = new DialogError("Hoooola");
        dialog.pack();
        dialog.setVisible(true);
    }

}
