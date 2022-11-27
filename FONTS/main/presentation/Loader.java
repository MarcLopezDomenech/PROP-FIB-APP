package main.presentation;

import main.excepcions.ExceptionDocumentExists;
import main.excepcions.ExceptionInvalidFormat;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Loader extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField path;
    private JButton browse;
    private JRadioButton cat;
    private JRadioButton esp;
    private JRadioButton eng;
    private JFileChooser fc;

    public Loader() {
        setTitle("Carregar documents");
        setResizable(false);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        UIManager.put("FileChooser.cancelButtonText", "Cancel.lar");
        UIManager.put("FileChooser.lookInLabelText", "Buscar en");
        UIManager.put("FileChooser.directoryOpenButtonText", "Obrir");
        UIManager.put("FileChooser.fileNameLabelText", "Nom del document:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipus");
        // TODO: traduir tots els tooltips, es poden customitzar icones!

        fc = new JFileChooser(".");
        fc.setApproveButtonText("Seleccionar");
        fc.setDialogTitle("Seleccionar documents");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        FileFilter xml = new FileNameExtensionFilter("XML", "xml");
        FileFilter txt = new FileNameExtensionFilter("Text pla", "txt");
        FileFilter fp = new FileNameExtensionFilter("Format propietari", "fp");
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(txt);
        fc.addChoosableFileFilter(xml);
        fc.addChoosableFileFilter(fp);
        fc.setMultiSelectionEnabled(true);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (ExceptionInvalidFormat ex) {
                    throw new RuntimeException(ex);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (ExceptionDocumentExists ex) {
                    throw new RuntimeException(ex);
                }
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

        ButtonGroup languages = new ButtonGroup();
        languages.add(cat);
        languages.add(esp);
        languages.add(eng);

        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(getRootPane());
                path.setText("");

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] files = fc.getSelectedFiles();
                    for (File f : files) {
                        path.setText(path.getText() + f.getAbsolutePath() + "; ");
                        System.out.println(f.getAbsolutePath());
                    }
                    path.setText(path.getText().substring(0,path.getText().length()-2));
                    enableButtonIfCorrect();
                }
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
        buttonOK.setEnabled(fc.getSelectedFiles().length > 0 && (cat.isSelected() || esp.isSelected() || eng.isSelected()));
    }

    private void onOK() throws ExceptionInvalidFormat, FileNotFoundException, ExceptionDocumentExists {
        String lang;
        if (cat.isSelected()) lang = "ca";
        else if (esp.isSelected()) lang = "es";
        else lang = "en";

        for (File f : fc.getSelectedFiles()) {
            CtrlPresentation.getInstance().importDocument(f.getAbsolutePath(), lang);
        }

        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
