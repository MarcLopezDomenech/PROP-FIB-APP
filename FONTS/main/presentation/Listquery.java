package main.presentation;

import javax.swing.*;
import java.awt.event.*;

public class Listquery extends JDialog {
    private CtrlPresentation cp;

    private JFrame quer;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField query_text;
    private JTextField number_text;

    private String query;
    private int k;

    public Listquery() {
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
        number_text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                enableButtonIfCorrect();
            }
        });
    }

    private void onOK() {
        // add your code here
        query=query_text.getText();
        String str = number_text.getText();
        try{
            k = Integer.parseInt(str);
        }
        catch (NumberFormatException ex){
            //LLAMAR DIALOG ERROR
            cp.getInstance().showError(quer, "Número de resultados invàlido");
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!query_text.getText().equals("") && !number_text.getText().equals(""));
    }
    public String initialize(JFrame reference) {
        pack();
        this.quer = reference;
        setLocationRelativeTo(reference);
        setVisible(true);
        //return pair (query,k);
        return query;
    }
    public static void main(String[] args) {
        Listquery dialog = new Listquery();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
