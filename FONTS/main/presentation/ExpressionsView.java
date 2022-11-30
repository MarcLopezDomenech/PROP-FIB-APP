package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import main.excepcions.ExceptionDocumentExists;
import main.excepcions.ExceptionExpressionExists;
import main.excepcions.ExceptionInvalidExpression;
import main.excepcions.ExceptionNoExpression;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.Set;

/**
 * @author pau.duran.manzano
 * @class ExpressionsView
 * @brief Vista per gestionar les expressions del sistema
 */
public class ExpressionsView {
    private CtrlPresentation cp;
    private JFrame frame;
    private JPanel panel;
    private JList list;
    private DefaultListModel listModel;
    private JButton add;
    private JButton delete;
    private JButton modify;
    private JTextField text;
    private JPanel buttons;
    private JMenuBar menuBar;
    private JMenu menuOptions;
    private JMenuItem loadOption;
    private JMenuItem createOption;
    private JMenuItem listOption;
    private JMenuItem help;
    private String selected;

    public ExpressionsView() {
        cp = CtrlPresentation.getInstance();
        frame = new JFrame("Gestio de les expressions");
        selected = null;

        menuBar = new JMenuBar();

        menuOptions = new JMenu("Menu");
        loadOption = new JMenuItem("Carregar document");
        menuOptions.add(loadOption);
        createOption = new JMenuItem("Nou document");
        menuOptions.add(createOption);
        listOption = new JMenuItem("Gestio de documents");
        menuOptions.add(listOption);
        menuBar.add(menuOptions);
        help = new JMenuItem("?");
        menuBar.add(help);
        frame.setJMenuBar(menuBar);

        loadOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.showLoader(frame.getLocation());
            }
        });

        createOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewDocumentDialog dialog = new NewDocumentDialog();
                dialog.pack();
                dialog.setLocationRelativeTo(frame);
                dialog.setVisible(true);
            }
        });

        listOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.showDocuments(frame.getLocation(), frame.getSize());
                frame.dispose();
            }
        });

        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.showHelp(frame.getLocation(), "Per fer funcionar aquesta pantalla, has de ...");
            }
        });

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
                    cp.showError(frame.getLocation(), e.getMessage());
                    list.setSelectedIndex(listModel.indexOf(newExpression));
                } catch (ExceptionInvalidExpression e) {
                    cp.showError(frame.getLocation(), e.getMessage());
                }
            }
        });
        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ExpressionsModifyDialog modifyExpression = new ExpressionsModifyDialog();
                Point point = new Point((int) (frame.getLocation().getX() + 100), (int) (frame.getLocation().getY() + 100));

                String newValue = modifyExpression.initialize(point, selected);
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
                        cp.showError(frame.getLocation(), e.getMessage());
                    } catch (ExceptionNoExpression e) {
                        // No es pot donar el cas, ho garantim per presentació
                        cp.showInternalError(frame.getLocation());
                    }
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean confirm = cp.askConfirmation(frame.getLocation(), "Segur/a que vols esborrar l'expressio " + selected + "?");
                if (confirm) {
                    try {
                        cp.deleteExpression(selected);
                    } catch (ExceptionNoExpression ex) {
                        // No es pot donar, garantit per presentació
                        cp.showInternalError(frame.getLocation());
                    }
                    listModel.removeElement(selected);
                    text.grabFocus();
                }
            }
        });
        panel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (text.hasFocus() && !"".equals(text.getText())) add.doClick();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void initialize(Point location, Dimension size) {
        listModel = new DefaultListModel();
        list.setModel(listModel);
        Set<String> expressions = cp.getAllExpressions();
        for (String expression : expressions) listModel.addElement(expression);

        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setSize(size);
        frame.setLocation(location);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        menuBar = new JMenuBar();
        menuBar.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(menuBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttons = new JPanel();
        buttons.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(buttons, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        delete = new JButton();
        delete.setEnabled(false);
        delete.setText("Esborrar");
        buttons.add(delete, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modify = new JButton();
        modify.setEnabled(false);
        modify.setText("Modificar");
        buttons.add(modify, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        add = new JButton();
        add.setEnabled(false);
        add.setText("Afegir");
        buttons.add(add, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        text = new JTextField();
        panel.add(text, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel.add(scrollPane1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        list = new JList();
        scrollPane1.setViewportView(list);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
