package main.presentation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * @author marc.valls.camps
 * @class FavCheckBox
 * @brief Herència de la classe JCheckBox de SWING per implementar que l'estat seleccionat es correspongui a un cor vermell,
 * i el no seleccionat a un cor blanc
 */
public class FavCheckBox extends JCheckBox {
    /**
     * \brief Icona d'un cor vermell que representa "favorit"
     */
    private ImageIcon fav;
    /**
     * \brief Icona d'un cor blanc que representa "no favorit"
     */
    private ImageIcon nofav;

    /**
     * @brief Constructora que inicialitza les ImageIcon, carregant les dues imatges i redimensionant-les
     * @param reference Frame respecte del qual es posicionarà el diàleg d'error si falla la lectura de les imatges
     */
    public FavCheckBox(JFrame reference) {
        setHorizontalAlignment(CENTER);
        try {
            fav = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("images/red.png"))));
            fav = new ImageIcon(fav.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));

            nofav = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("images/white.png"))));
            nofav = new ImageIcon(nofav.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            // NO hauria de passar, si no s'esborren les imatges
            CtrlPresentation.getInstance().showInternalError(reference);
        }
    }

    /**
     * @brief Reimplementació de la funció de JCheckBox, per definir la imatge que s'ha de mostrar segons l'estat
     * @param selected Booleà que indica si s'ha de passar a l'estat "seleccionat" o al "no seleccionat"
     */
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) setIcon(fav);
        else setIcon(nofav);
    }

}