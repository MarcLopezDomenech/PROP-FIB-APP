package main.presentation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.Set;

public class ListExpression extends JDialog {

    private CtrlPresentation cp;

    private JFrame exp;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList list1;
    private DefaultListModel listModel1;

    private String result;

    public ListExpression() {
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

    private void onOK() {
        // add your code here
        dispose();
    }
    private void onList1() {
        enableButtonIfCorrect();
        result=list1.getSelectedValue().toString();
    }
    private void onCancel() {
        // add your code here if necessary
        result=null;
        dispose();
    }
    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!list1.isSelectionEmpty());
    }
    public String initialize(JFrame reference) {
        result=null;
        cp=CtrlPresentation.getInstance();
        enableButtonIfCorrect();
        listModel1 = new DefaultListModel();
        list1.setModel(listModel1);
        Set<String> expressions = cp.getAllExpressions();
        for (String ex : expressions) listModel1.addElement(exp);

        pack();
        this.exp = reference;
        setLocationRelativeTo(reference);
        setVisible(true);
        return result;
    }
}
