package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author pau.duran.manzano
 * @class ConfirmDialog
 * @brief Diàleg per confirmar canvis destructius abans de realitzar les accions associades
 */
public class ConfirmDialog extends JDialog {
    /**
     * \brief Panell principal del diàleg
     */
    private JPanel contentPane;

    /**
     * \brief Text que es vol que l'usuari confirmi
     */
    private JLabel text;

    /**
     * \brief Botó de confirmació
     */
    private JButton buttonSi;

    /**
     * \brief Botó de cancel·lació
     */
    private JButton buttonNo;

    /**
     * \brief Booleà que registra si s'ha confirmat o no per part de l'usuari
     */
    private boolean confirmation;

    /**
     * @return ConfirmDialog
     * @brief Constructora per defecte
     * @details Es construeix el diàleg, assignant valors a atributs i listeners
     */
    public ConfirmDialog() {
        setContentPane(contentPane);
        confirmation = false;

        // Listeners dels dos botons

        buttonSi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // Si tanquem el dialog s'entén com cancel
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // Si apretem ESC s'entén com cancel
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * @param reference Frame de referència per saber on situar el diàleg
     * @param confirm   Missatge de què es vol la confirmació per part de l'usuari
     * @return Si l'usuari ha confirmat o no el missatge que se li mostrava
     * @brief Mètode per inicialitzar el diàleg
     * @details Amb aquest mètode es permet mostrar el diàleg de confirmació de l'aplicatiu
     * @post Es mostra per pantalla el missatge de confirmació en format de diàleg
     */
    public boolean initialize(JFrame reference, String confirm) {
        text.setText(confirm);
        setContentPane(contentPane);
        setModal(true);
        setTitle("Confirmar");
        pack();
        getRootPane().setDefaultButton(buttonNo);
        setLocationRelativeTo(reference);
        setVisible(true);
        return confirmation;
    }

    /**
     * @brief Definició de què succeeix quan premem confirmar
     * @details Quan cliquem confirmar, es registra la confirmació i es retorna el control a initialize
     * @post Es deixa de mostrar el diàleg per pantalla
     */
    private void onOK() {
        confirmation = true;
        dispose();
    }

    /**
     * @brief Definició de què succeeix quan no confirmem
     * @details Es registra que la confirmació és negativa i es retorna el control a initialize
     * @post Es deixa de mostrar el diàleg per pantalla
     */
    private void onCancel() {
        confirmation = false;
        dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSi = new JButton();
        buttonSi.setText("Sí");
        panel2.add(buttonSi, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonNo = new JButton();
        buttonNo.setText("No");
        panel1.add(buttonNo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        text = new JLabel();
        text.setText("Label");
        panel3.add(text, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
