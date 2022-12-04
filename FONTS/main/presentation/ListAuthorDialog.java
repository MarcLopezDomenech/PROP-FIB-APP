package main.presentation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.List;

public class ListAuthorDialog extends JDialog {
    private CtrlPresentation cp;
    private JFrame autandtit;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField text_aut;
    private JList list1;
    private DefaultListModel listModel1;

    private String result;

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
    }
    private void onText1() {
        enableButtonIfCorrect();
        String prefix = text_aut.getText();
        List<String> authors = cp.listAuthorsByPrefix(prefix);
        listModel1.clear();
        for (String aut : authors) listModel1.addElement(aut);
    }
    private void onList1() {
        enableButtonIfCorrect();
        result=list1.getSelectedValue().toString();
    }
    private void onOK() {
        // add your code here
        dispose();
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
        List<String> authors = cp.listAuthorsByPrefix("");
        for (String aut : authors) listModel1.addElement(aut);

        pack();
        this.autandtit = reference;
        setLocationRelativeTo(reference);
        setVisible(true);
        return result;
    }
}
