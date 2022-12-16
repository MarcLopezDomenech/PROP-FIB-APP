package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * @author marc.lopez.domenech
 * @class ListAuthorDialog
 * @brief Diàleg per mostrar els documents d'un autor, mitjançant seleccionar l'autor pel seu prefix
 */
public class ListAuthorDialog extends JDialog {
    /**
     * \brief La instància de controlador de presentació de l'aplicació
     */
    private CtrlPresentation cp;
    /**
     * \brief Panell amb el contingut, inicialitzat amb la GUI d'Intellij
     */
    private JPanel contentPane;
    /**
     * \brief Botó OK, per retornar l'autor seleccionat
     */
    private JButton buttonOK;
    /**
     * \brief Botó Tornar per tornar
     */
    private JButton buttonCancel;
    /**
     * \brief Camp per omplir el prefix del nom de l'autor
     */
    private JTextField text_aut;
    /**
     * \brief Llista on es mostraran tots els autors que compleixen el prefix
     */
    private JList list1;
    /**
     * \brief Model que seguirà la llista on es mostraran tots els autors que compleixen el prefix
     */
    private DefaultListModel listModel1;
    /**
     * \brief String que guarda l'autor final seleccionat
     */
    private String result;
    /**
     * \brief Booleà usat per evitar una selecció mentre s'està modificant o carregant els autors
     * \invariant Fals si no s'ha està modificant i True si sí
     */
    private Boolean mod;

    /**
     * @return ListAuthorDialog
     * @brief Creadora per defecte del diàleg de ListAuthorDialog
     * @details S'inicialitza el diàleg i s'enllacen tots els listeners dels botons, així com dels camps a omplir
     * de manera que només es desbloqueja el botó OK quan s'ha seleccionat un autor.
     */
    public ListAuthorDialog() {
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

        text_aut.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                onText1();
            }
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                onList1();
            }
        });
        text_aut.addKeyListener(new KeyAdapter() {
        });
        text_aut.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mod = true;
                list1.clearSelection();
            }
        });
    }

    /**
     * @brief Funció per tractar l'escriptura del prefix
     * @details Agafa el text escrit, busca tots els autors que compleixen el prefix i els mostra a la llista
     */
    private void onText1() {
        buttonOK.setEnabled(false);
        String prefix = text_aut.getText();
        listModel1.clear();
        List<String> authors = cp.listAuthorsByPrefix(prefix);
        for (String aut : authors) listModel1.addElement(aut);
        mod = false;
    }

    /**
     * @brief Funció per tractar la selecció en la llista
     * @details Agafa el text seleccionat i el posa com a resultat i habilita el botó OK
     */
    private void onList1() {
        if (!mod) {
            enableButtonIfCorrect();
            result = list1.getSelectedValue().toString();
        }
    }

    /**
     * @brief Funció per tractar la pulsació al botó OK
     * @details simplement retorna i acaba l'execució
     */
    private void onOK() {
        // add your code here
        dispose();
    }

    /**
     * @brief Funció per tractar la pulsació al botó Tornar
     * @details Posa el resultat a null i retorna
     */
    private void onCancel() {
        // add your code here if necessary
        result = null;
        dispose();
    }

    /**
     * @brief Funció per habilitar o inhabilitar el botó OK
     * @details Comprova si s'ha seleccionat un autor de la llista per habilitar el botó Guardar, si no és així
     * l'inhabilita.
     */
    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!list1.isSelectionEmpty());
    }

    /**
     * @param reference Frame sobre el que es col·locarà i centrarà el diàleg
     * @return Un String que té el nom de l'autor seleccionat i null si res s'ha seleccionat
     * @brief Mètode per a mostrar el diàleg inicialitzat
     */
    public String initialize(JFrame reference) {
        setTitle("Llistar per autor");

        mod = false;
        result = null;
        cp = CtrlPresentation.getInstance();
        enableButtonIfCorrect();
        listModel1 = new DefaultListModel();
        list1.setModel(listModel1);
        List<String> authors = cp.listAuthorsByPrefix("");
        for (String aut : authors) listModel1.addElement(aut);

        pack();
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
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(350, 300));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Tornar");
        panel1.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Autor:");
        panel3.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        text_aut = new JTextField();
        text_aut.setText("");
        panel3.add(text_aut, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        list1 = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        list1.setModel(defaultListModel1);
        scrollPane1.setViewportView(list1);
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
