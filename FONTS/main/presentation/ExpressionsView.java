package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import main.excepcions.ExceptionExpressionExists;
import main.excepcions.ExceptionInvalidExpression;
import main.excepcions.ExceptionNoExpression;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

/**
 * @author pau.duran.manzano
 * @class ExpressionsView
 * @brief Vista per gestionar les expressions del sistema
 */
public class ExpressionsView {
    /**
     * \brief Instància del controlador de presentació
     */
    private CtrlPresentation cp;

    /**
     * \brief Instància del subcontrolador de vistes i dàlegs
     */
    private CtrlViewsDialogs cvd;

    /**
     * \brief Marc de la vista
     */
    private JFrame frame;

    /**
     * \brief Panell principal de la vista
     */
    private JPanel panel;

    /**
     * \brief Llista on es mostraran totes les expressions del sistema
     */
    private JList list;

    /**
     * \brief Model que seguirà la llista on es mostraran totes les expressions
     */
    private DefaultListModel listModel;

    /**
     * \brief Espai de text per introduir noves expressions
     */
    private JTextField text;

    /**
     * \brief Panell on se situen els botons per gestionar expressions
     */
    private JPanel buttons;

    /**
     * \brief Botó per afegir una nova expressió
     * \invariant Només estarà actiu si el text no és buit
     */
    private JButton add;

    /**
     * \brief Botó per esborrar una expressió seleccionada
     * \invariant Només estarà actiu si hi ha una expressió seleccionada
     */
    private JButton delete;

    /**
     * \brief Botó per modificar una expressió seleccionada
     * \invariant Només estarà actiu si hi ha una expressió seleccionada
     */
    private JButton modify;

    /**
     * \brief Espai de menú de la vista
     */
    private JMenuBar menuBar;

    /**
     * \brief Etiqueta del menú principal
     */
    private JMenu menuOptions;

    /**
     * \brief Opció del menú principal per carregar un nou document
     */
    private JMenuItem loadOption;

    /**
     * \brief Opció del menú principal per crear un nou document
     */
    private JMenuItem createOption;

    /**
     * \brief Opció del menú principal per anar a la vista de gestió de documents
     */
    private JMenuItem listOption;

    /**
     * \brief Opció del menú per mostrar l'ajuda a l'usuari
     */
    private JMenuItem help;

    /**
     * \brief Opció del menú per fer reset del sistema
     */
    private JMenuItem reset;

    /**
     * \brief Identificador de l'expressió seleccionada, null si no hi ha cap expressió seleccionada
     */
    private String selected;

    /**
     * @return ExpressionsView
     * @brief Creadora per defecte de la vista
     * @details S'inicialitza el menú i s'enllacen tots els listeners de botons, events de mouse i altres a les funcionalitats corresponents
     */
    public ExpressionsView() {
        // Aconseguim la instància dels controladors de presentació que usem i inicialitzem el frame
        cp = CtrlPresentation.getInstance();
        cvd = CtrlViewsDialogs.getInstance();
        frame = new JFrame("Gestió de les expressions");
        selected = null;

        // Definició del menú
        menuBar = new JMenuBar();

        menuOptions = new JMenu("Menú");
        loadOption = new JMenuItem("Carregar document");
        menuOptions.add(loadOption);
        createOption = new JMenuItem("Nou document");
        menuOptions.add(createOption);
        listOption = new JMenuItem("Gestió de documents");
        menuOptions.add(listOption);
        menuBar.add(menuOptions);
        help = new JMenuItem("     ?");
        help.setMaximumSize(new Dimension(50, 30));
        menuBar.add(help);
        JMenuItem spacing = new JMenuItem("");
        spacing.setFocusable(false);
        menuBar.add(spacing);
        reset = new JMenuItem("Reset");
        reset.setMaximumSize(new Dimension(100, 30));
        menuBar.add(reset);
        frame.setJMenuBar(menuBar);


        // Listeners pel menú

        loadOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cvd.showLoader(frame);
            }
        });

        createOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cvd.showNewDocument(frame);
            }
        });

        listOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cvd.showDocuments(frame.getLocation(), frame.getSize());
                frame.dispose();
            }
        });

        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cvd.showHelp(frame,
                        "<html>" +
                                "Aquesta és la pantalla de gestió d'expressions.<br><br>" +
                                "Pots crear expressions en l'espai de text superior, i confirmant-ho amb el botó d'Afegir<br><br>" +
                                "A més, pots consultar les expressions que has donat d'alta en la llista que et presentem<br><br>" +
                                "Si en selecciones una, disposes de diferents opcions en els botons de la part superior dreta." +
                                "</html>");
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean confirm = cvd.askConfirmation(frame, "ATENCIÓ! Estàs a punt d'esborrar tot el contingut del sistema. Aquesta acció és irreversible. Estàs segur que vols fer reset?");
                if (confirm) {
                    CtrlApplication.getInstance().reset();
                    listModel.clear();
                }
            }
        });


        // Listeners de la llista d'expressions

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                try {
                    selected = list.getSelectedValue().toString();
                    modify.setEnabled(true);
                    delete.setEnabled(true);
                } catch (Exception e) {     // Quan no podem convertir-ho a string --> el canvi és que es desselecciona
                    selected = null;
                    modify.setEnabled(false);
                    delete.setEnabled(false);
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);
                if (event.getClickCount() == 2) modify.doClick();
            }
        });


        // Listeners de l'espai de text i el botó per afegir noves expressions

        text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                list.clearSelection();

            }
        });

        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                add.setEnabled(!"".equals(text.getText()));
            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String newExpression = text.getText();
                try {
                    cp.createExpression(newExpression);
                    listModel.add(0, newExpression);
                    //listModel.addElement(newExpression);
                    list.setSelectedIndex(listModel.indexOf(newExpression));
                    list.grabFocus();
                    text.setText("");
                    add.setEnabled(false);
                } catch (ExceptionExpressionExists e) {
                    cvd.showError(frame, e.getMessage());
                    list.setSelectedIndex(listModel.indexOf(newExpression));
                } catch (ExceptionInvalidExpression e) {
                    cvd.showError(frame, e.getMessage());
                }
            }
        });


        // Listeners dels botons quan tenim una expressió seleccionada

        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String newValue = cvd.showModifyExpression(frame, selected);
                if (newValue != null && !"".equals(newValue)) {
                    try {
                        cp.modifyExpression(selected, newValue);
                        int index = listModel.indexOf(selected);
                        listModel.removeElement(selected);
                        listModel.add(index, newValue);
                        selected = newValue;
                        list.grabFocus();
                        list.setSelectedIndex(listModel.indexOf(newValue));
                    } catch (ExceptionInvalidExpression | ExceptionExpressionExists e) {
                        cvd.showError(frame, e.getMessage());
                    } catch (ExceptionNoExpression e) {
                        // No es pot donar el cas, ho garantim per presentació
                        cvd.showInternalError(frame);
                    }
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean confirm = cvd.askConfirmation(frame, "Segur/a que vols esborrar l'expressió " + selected + "?");
                if (confirm) {
                    try {
                        cp.deleteExpression(selected);
                    } catch (ExceptionNoExpression ex) {
                        // No es pot donar, garantit per presentació
                        cvd.showInternalError(frame);
                    }
                    listModel.removeElement(selected);
                    text.grabFocus();
                }
            }
        });


        // Permetem modificar si fem doble click en una expressió

        panel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (text.hasFocus() && !"".equals(text.getText())) add.doClick();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        // Permetem esborrar si cliquem suprimir

        panel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete.doClick();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    /**
     * @param location Posició de la pantalla on volem situar la vista
     * @param size     Tamany de la vista
     * @brief Mètode per inicialitzar la vista
     * @details Amb aquesta funció es mostra la vista de gestió de les expressions
     * @post Es mostra la vista de gestió d'expressions per pantalla
     */
    public void initialize(Point location, Dimension size) {
        listModel = new DefaultListModel();
        list.setModel(listModel);
        Set<String> expressions = cp.getAllExpressions();
        for (String expression : expressions) listModel.addElement(expression);

        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setSize(size);
        frame.setLocation(location);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                CtrlApplication.getInstance().closeApp(frame);
                System.exit(0);
            }
        });
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
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        menuBar = new JMenuBar();
        menuBar.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(menuBar, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttons = new JPanel();
        buttons.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(buttons, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        delete = new JButton();
        delete.setEnabled(false);
        delete.setText("Esborrar");
        buttons.add(delete, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modify = new JButton();
        modify.setEnabled(false);
        modify.setText("Modificar");
        buttons.add(modify, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        add = new JButton();
        add.setEnabled(false);
        add.setText("Afegir");
        buttons.add(add, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        text = new JTextField();
        panel.add(text, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        list = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        list.setModel(defaultListModel1);
        scrollPane1.setViewportView(list);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

    /**
     * \endcond
     */

}
