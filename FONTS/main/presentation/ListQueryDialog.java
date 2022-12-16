package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import main.domain.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author marc.lopez.domenech
 * @class ListQueryDialog
 * @brief Diàleg per mostrar els k documents segons la query inserida
 */

public class ListQueryDialog extends JDialog {
    /**
     * \brief La instància de subcontradlor de presentació de vistes i diàlegs de l'aplicació
     */
    private CtrlViewsDialogs cvd;
    /**
     * \brief El frame principal del diàleg
     */
    private JFrame quer;
    /**
     * \brief Panell amb el contingut, inicialitzat amb la GUI d'Intellij
     */
    private JPanel contentPane;
    /**
     * \brief Botó OK, per retornar la query i k omplert
     */
    private JButton buttonOK;
    /**
     * \brief Botó Tornar per tornar
     */
    private JButton buttonCancel;
    /**
     * \brief Camp per omplir la query
     */
    private JTextField query_text;
    /**
     * \brief Spinner per seleccionar el nombre de documents que es vol
     */
    private JSpinner spinner1;
    /**
     * \brief String que guarda la query
     */
    private String query;
    /**
     * \brief Int que guarda el nombre de documents que es vol
     */
    private int k;
    /**
     * \brief Pair usat per emmagatzemar el resultat del diàleg és a dir la query i el nombre de documents
     */
    private Pair<String, Integer> result;

    public ListQueryDialog() {
        cvd = CtrlViewsDialogs.getInstance();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        query_text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                enableButtonIfCorrect();
            }
        });

    }
    /**
     * @brief Funció per tractar la pulsació del botó OK
     * @details Agafa els texts dels diferents camps i retorna la funció amb aquestes dades ficant-les al resultat
     */
    private void onOK() {
        query = query_text.getText();
        boolean err = true;
        spinner1.getValue();
        try {
            k = Integer.parseInt(spinner1.getValue().toString());
        } catch (NumberFormatException ex) {
            //LLAMAR DIALOG ERROR
            cvd.showError(quer, "Nombre de resultats invalid");
            err = false;
        }
        if (err) {
            if (k < 0) {
                cvd.showError(quer, "Nombre de resultats invalid");
            } else {
                result = new Pair<String, Integer>(query, k);
                dispose();
            }
        }
    }
    /**
     * @brief Funció per tractar la pulsació al botó Tornar
     * @details Posa el resultat a (null,null) i retorna
     */
    private void onCancel() {
        // add your code here if necessary
        result = new Pair<String, Integer>(null, null);
        dispose();
    }
    /**
     * @brief Funció per habilitar o inhabilitar el botó OK
     * @details Comprova si s'ha introduit una query per habilitar el botó OK o en cas contrari deshabilitar-ho
     */
    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!query_text.getText().equals(""));
    }
    /**
     * @param reference Frame sobre el que es col·locarà i centrarà el diàleg
     * @return Un pair que té la query i el nombre de documents que es volen
     * @brief Mètode per a mostrar el diàleg inicialitzat
     */
    public Pair<String, Integer> initialize(JFrame reference) {
        setTitle("Llistar per query");

        SpinnerModel sm = new SpinnerNumberModel(1, 1, 1000, 1);
        spinner1.setModel(sm);

        result = new Pair<String, Integer>(null, null);
        buttonOK.setEnabled(false);
        pack();
        this.quer = reference;
        setLocationRelativeTo(reference);
        setVisible(true);
        return result;
    }


    /**
     * \cond
     */
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
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(350, 150));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel1.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel1.add(buttonOK, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Nombre de resultats:");
        contentPane.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Query:");
        contentPane.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        query_text = new JTextField();
        query_text.setText("");
        contentPane.add(query_text, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        spinner1 = new JSpinner();
        contentPane.add(spinner1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    /**
     * \endcond
     */

}
