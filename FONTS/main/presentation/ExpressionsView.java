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
 * @class ExpressionsView
 * @brief Vista per gestionar les expressions del sistema
 * @author pau.duran.manzano
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
    private String selected;

    public ExpressionsView() {
        cp = CtrlPresentation.getInstance();
        frame = new JFrame("Gestió de les expressions");
        selected = null;

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
                    listModel.addElement(newExpression);
                    list.setSelectedIndex(listModel.indexOf(newExpression));
                    list.grabFocus();
                    text.setText("");
                    add.setEnabled(false);
                } catch (ExceptionExpressionExists e) {
                    cp.showError(e.getMessage());
                    list.setSelectedIndex(listModel.indexOf(newExpression));
                } catch (ExceptionInvalidExpression e) {
                    cp.showError(e.getMessage());
                }
            }
        });
        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ExpressionsModifyDialog modifyExpression = new ExpressionsModifyDialog();
                String newValue = modifyExpression.initialize(selected);
                if (newValue != null) {
                    try {
                        cp.modifyExpression(selected, newValue);
                        int index = listModel.indexOf(selected);
                        listModel.removeElement(selected);
                        listModel.add(index, newValue);
                        selected = newValue;
                        list.grabFocus();
                        list.setSelectedIndex(listModel.indexOf(newValue));
                    } catch (ExceptionInvalidExpression e) {
                        cp.showError(e.getMessage());
                    } catch (ExceptionNoExpression e) {
                        // No es pot donar el cas, ho garantim per presentació
                    } catch (ExceptionExpressionExists e) {
                        cp.showError(e.getMessage());
                    }
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cp.deleteExpression(selected);
                } catch (ExceptionNoExpression ex) {
                    // No es pot donar
                }
                listModel.removeElement(selected);
            }
        });
        panel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (text.hasFocus() && !"".equals(text.getText())) add.doClick();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void initialize() {
        listModel = new DefaultListModel();
        list.setModel(listModel);
        Set<String> expressions = cp.getAllExpressions();
        for (String expression : expressions) listModel.addElement(expression);

        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setSize(600, 400);
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
