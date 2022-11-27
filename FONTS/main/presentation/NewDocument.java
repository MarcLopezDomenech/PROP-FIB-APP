package main.presentation;

import main.excepcions.ExceptionDocumentExists;
import main.excepcions.ExceptionInvalidLanguage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NewDocument extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField titleDoc;
    private JTextField authorDoc;
    private JRadioButton cat;
    private JRadioButton esp;
    private JRadioButton eng;

    public NewDocument() {
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

        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(cat);
        bgroup.add(esp);
        bgroup.add(eng);

        titleDoc.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                enableButtonIfCorrect();
            }
        });

        authorDoc.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                enableButtonIfCorrect();
            }
        });
        cat.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableButtonIfCorrect();
            }
        });
        esp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableButtonIfCorrect();
            }
        });
        eng.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableButtonIfCorrect();
            }
        });
    }

    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!titleDoc.getText().equals("") && !authorDoc.getText().equals("") && (cat.isSelected() || esp.isSelected() || eng.isSelected()));
    }

    private void onOK() {
        String lang;
        if (cat.isSelected()) lang = "ca";
        else if (esp.isSelected()) lang = "es";
        else lang = "en";

        try {
            CtrlPresentation.getInstance().createEmptyDocument(titleDoc.getText(), authorDoc.getText(), lang);
        } catch (ExceptionDocumentExists e) {
            CtrlPresentation.showError("Ja existeix el document al sistema!");
        } catch (ExceptionInvalidLanguage e) {
            // NO hauria de passar
            CtrlPresentation.showError("El llenguatge no és vàlid!");
        }
        // reenviar a la vista de modificar
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        NewDocument dialog = new NewDocument();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
