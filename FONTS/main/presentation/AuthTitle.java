package main.presentation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AuthTitle extends JDialog {

    private CtrlPresentation cp;

    private JFrame autandtit;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField text_aut;
    private JList list1;
    private DefaultListModel listModel1;
    private JList list2;
    private DefaultListModel listModel2;

    private String author;
    private String title;

    public AuthTitle() {
        cp = CtrlPresentation.getInstance();
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
        list2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                onList2();
            }
        });
    }

    private void onOK() {
        // add your code here
        if(author!="" && title!="") {
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessarysuper.keyReleased(e);
        dispose();
    }
    private void onText1() {
        //initialize(autandtit);
        enableButtonIfCorrect();
        // add your code here if necessary
        String prefix = text_aut.getText();
        List<String> authors = cp.listAuthorsByPrefix(prefix);
        for (String aut : authors) listModel1.addElement(aut);
        //dispose();
    }
    private void onList1() {
        enableButtonIfCorrect();
        // add your code here if necessary
        list2.clearSelection();
        author=list1.getSelectedValue().toString();
        Object[][] titles = cp.listTitlesOfAuthor(author);
        for (Object[] tit : titles) listModel2.addElement(tit);
    }
    private void onList2() {
        enableButtonIfCorrect();
        // add your code here if necessary
        title=list2.getSelectedValue().toString();
    }
    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!list1.isSelectionEmpty() && !list2.isSelectionEmpty());
        boolean err=list1.isSelectionEmpty();
        list2.setEnabled(!err);
        if(err){
            list2.clearSelection();
            //listModel2.clear();
        }
    }
    public String[] initialize(JFrame reference) {
        //cp = CtrlPresentation.getInstance();
        enableButtonIfCorrect();
        listModel1 = new DefaultListModel();
        list1.setModel(listModel1);
        listModel2 = new DefaultListModel();
        list2.setModel(listModel2);

        List<String> authors = cp.listAuthorsByPrefix("");
        for (String aut : authors) listModel1.addElement(aut);

        pack();
        this.autandtit = reference;
        setLocationRelativeTo(reference);
        setVisible(true);
        String[] result = {author,title};
        return result;
    }
    public static void main(String[] args) {
        AuthTitle dialog = new AuthTitle();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
