package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

import main.domain.util.Pair;

/**
 * @author marc.lopez.domenech
 * @class ListExpressionDialog
 * @brief Diàleg per mostrar els documents que compleixen una expressió seleccionada al diàleg
 */
public class ListExpressionDialog extends JDialog {
    /**
     * \brief La instància de controlador de presentació de l'aplicació
     */
    private CtrlPresentation cp;
    /**
     * \brief La instància de subcontradlor de presentació de vistes i diàlegs de l'aplicació
     */
    private CtrlViewsDialogs cvd;
    /**
     * \brief El frame principal del diàleg
     */
    private JFrame exp;
    /**
     * \brief Panell amb el contingut, inicialitzat amb la GUI d'Intellij
     */
    private JPanel contentPane;
    /**
     * \brief Botó OK, per retornar l'expressió seleccionada i caseSensitive
     */
    private JButton buttonOK;
    /**
     * \brief Botó Tornar per tornar
     */
    private JButton buttonCancel;
    /**
     * \brief Llista on es mostraran totes les expressions
     */
    private JList list1;
    /**
     * \brief Botó per seleccionar caseSensitive
     */
    private JRadioButton caseSensitiveRadioButton;
    /**
     * \brief Model que seguirà la llista on es mostraran totes les expressions
     */
    private DefaultListModel listModel1;
    /**
     * \brief Pair usat per emmagatzemar el resultat del diàleg és a dir l'expressió seleccionada i si s'ha seleccionat caseSensitive
     */
    private Pair<String, Boolean> result;

    /**
     * @return ListExpressionDialog
     * @brief Creadora per defecte del diàleg de ListExpressionDialog
     * @details S'inicialitza el diàleg i s'enllacen tots els listeners dels botons, així com la llista d'expressions
     * que només es desbloqueja el botó "OK" quan està seleccionada una expressió.
     */
    public ListExpressionDialog() {
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
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                onList1();
            }
        });
    }

    /**
     * @brief Funció per tractar la pulsació al botó OK
     * @details Agafa el text seleccionat i la pulsació o no de caseSensitive i posar-ho al resultat
     */
    private void onOK() {
        // add your code here
        result = new Pair<String, Boolean>(list1.getSelectedValue().toString(), caseSensitiveRadioButton.isSelected());
        dispose();
    }

    /**
     * @brief Funció per tractar la selecció en la llista d'expressionss
     * @details Agafa el text seleccionat i pulsacio de caseSensitive i el posa com a resultat i habilita el botó OK
     */
    private void onList1() {
        enableButtonIfCorrect();
        result = new Pair<String, Boolean>(list1.getSelectedValue().toString(), caseSensitiveRadioButton.isSelected());
    }

    /**
     * @brief Funció per tractar la pulsació al botó Tornar
     * @details Posa el resultat a (null,null) i retorna
     */
    private void onCancel() {
        // add your code here if necessary

        result = new Pair<String, Boolean>(null, null);
        dispose();
    }

    /**
     * @brief Funció per habilitar o inhabilitar el botó OK
     * @details Comprova si s'ha seleccionat una expressió per habilitar el botó OK o en cas contrari deshabilitar-ho
     */
    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!list1.isSelectionEmpty());
    }

    /**
     * @param reference Frame sobre el que es col·locarà i centrarà el diàleg
     * @return Un Pair que té l'expressió seleccionada i si s'ha seleccionat el botó caseSensitive i null si s'ha retornat pel botó Tornar
     * @brief Mètode per a mostrar el diàleg inicialitzat
     */
    public Pair<String, Boolean> initialize(JFrame reference) {
        setTitle("Llistar per expressió");

        result = new Pair<String, Boolean>(null, null);
        cp = CtrlPresentation.getInstance();
        cvd = CtrlViewsDialogs.getInstance();
        enableButtonIfCorrect();
        listModel1 = new DefaultListModel();
        list1.setModel(listModel1);
        Set<String> expressions = cp.getAllExpressions();
        if (expressions.isEmpty()) {
            cvd.showError(exp, "<html>" +
                    "No has donat d'alta cap expressió.<br><br>" +
                    "Per fer-ho, dirigeix-te a Menú --> Gestió expressions<br>" +
                    "</html>");
            return result;
        }
        for (String ex : expressions) listModel1.addElement(ex);

        pack();
        this.exp = reference;
        setLocationRelativeTo(reference);
        setVisible(true);
        return result;
    }

    /**
     * \cond
     */ {
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
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(350, 300));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Tornar");
        panel1.add(buttonCancel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Expressions:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        list1 = new JList();
        scrollPane1.setViewportView(list1);
        caseSensitiveRadioButton = new JRadioButton();
        caseSensitiveRadioButton.setText("Case Sensitive");
        contentPane.add(caseSensitiveRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
