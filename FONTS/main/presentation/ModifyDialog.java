package main.presentation;

import main.domain.util.Pair;
import main.excepcions.ExceptionNoDocument;

import javax.swing.*;
import java.awt.event.*;

public class ModifyDialog extends JDialog {
    private CtrlPresentation cp;

    private JFrame modify;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel tit_doc;
    private JLabel aut_doc;
    private JTextField textcontent;

    private String tit;
    private String auth;
    private String cont;

    public ModifyDialog() {
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
    }

    private void onOK() {
        String content_fin = textcontent.getText();
        try {
            cp.updateContentDocument(tit, auth, content_fin);
        } catch (ExceptionNoDocument e) {
            cp.getInstance().showError(modify, "No existeix el document");
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    public void initialize(JFrame reference,String title, String author) {
        cp = CtrlPresentation.getInstance();
        buttonOK.setEnabled(false);

        tit=title;
        auth=author;

        try {
            cont=cp.getContentDocument(tit,auth);
        } catch (ExceptionNoDocument e) {
            cp.getInstance().showError(modify, "No existeix el document");
        }
        tit_doc.setText(tit);
        aut_doc.setText(auth);
        textcontent.setText(cont);

        pack();
        this.modify = reference;
        setLocationRelativeTo(reference);
        setVisible(true);
    }
}
