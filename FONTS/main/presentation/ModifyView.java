package main.presentation;

import main.excepcions.ExceptionNoDocument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class ModifyView {
    private CtrlPresentation cp;
    private JFrame modify;
    private JButton guardarButton;
    private JButton exportarButton;
    private JButton cancelButton;
    private JTextField textcontent;
    private JLabel tit_doc;
    private JLabel aut_doc;
    private String content_ini;

    public ModifyView() {
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGuardar();
            }
        });
        exportarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExport();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }

    private void onGuardar() {
        String content_fin = textcontent.getText();
        content_ini=content_fin;
        try {
            cp.updateContentDocument(tit_doc.getText(), aut_doc.getText(), content_fin);
        } catch (ExceptionNoDocument e) {
            cp.getInstance().showError(modify, "No existeix el document");
        }
    }

    private void onCancel() {
        textcontent.setText(content_ini);
    }

    private void onExport() {
        onGuardar();
    }

    public void initialize(String title, String author, String content) {
        tit_doc.setText(title);
        aut_doc.setText(author);
        textcontent.setText(content);

        modify.setVisible(true);
        modify.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                cp.closeApp(modify);
                System.exit(0);
            }
        });
    }
}
