package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import main.excepcions.ExceptionInvalidFormat;
import main.excepcions.ExceptionNoDocument;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * @author marc.valls.camps
 * @class DownloaderDialog
 * @brief Diàleg per a exportar un document del sistema
 */
public class DownloaderDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField path;
    private JButton browse;
    private JLabel titleDoc;
    private JLabel authorDoc;
    private JRadioButton ftxt;
    private JRadioButton fxml;
    private JRadioButton ffp;
    private JTextField nom;
    private JFileChooser fc;
    private boolean okPressed = false;

    public DownloaderDialog() {
        setTitle("Descarregar un document");
        setResizable(false);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        UIManager.put("FileChooser.cancelButtonText", "Cancel.lar");
        UIManager.put("FileChooser.openButtonText", "Seleccionar");
        UIManager.put("FileChooser.lookInLabelText", "Buscar en");
        UIManager.put("FileChooser.folderNameLabelText", "Nom de la carpeta");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipus");
        fc = new JFileChooser(".");
        fc.setApproveButtonText("Descarregar");
        fc.setDialogTitle("Descarregar document");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setAcceptAllFileFilterUsed(false);
        FileFilter xml = new FileNameExtensionFilter("XML", "xml");
        FileFilter txt = new FileNameExtensionFilter("Text pla", "txt");
        FileFilter fp = new FileNameExtensionFilter("Format propietari", "fp");
        fc.setFileFilter(txt);
        fc.addChoosableFileFilter(xml);
        fc.addChoosableFileFilter(fp);

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

        ButtonGroup format = new ButtonGroup();
        format.add(this.ftxt);
        format.add(this.fxml);
        format.add(this.ffp);

        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(getRootPane());
                path.setText("");

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    path.setText(f.getAbsolutePath() + "/");

                    if (fc.getFileFilter().equals(txt)) ftxt.setSelected(true);
                    else if (fc.getFileFilter().equals(xml)) fxml.setSelected(true);
                    else ffp.setSelected(true);
                }

                enableButtonIfCorrect();
            }
        });
        path.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableButtonIfCorrect();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableButtonIfCorrect();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableButtonIfCorrect();
            }
        });
        nom.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableButtonIfCorrect();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableButtonIfCorrect();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableButtonIfCorrect();
            }
        });
        this.ftxt.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableButtonIfCorrect();
            }
        });
        this.fxml.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableButtonIfCorrect();
            }
        });
        this.ffp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableButtonIfCorrect();
            }
        });
    }

    public String initialize(JFrame reference, String title, String author) {
        pack();
        setLocationRelativeTo(reference);
        titleDoc.setText(title);
        authorDoc.setText(author);
        setVisible(true);

        if (!okPressed) return null;

        String extension;
        if (ftxt.isSelected()) extension = ".txt";
        else if (fxml.isSelected()) extension = ".xml";
        else extension = ".fp";

        return path.getText().strip() + nom.getText().strip() + extension;
    }

    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!path.getText().isEmpty() && (ftxt.isSelected() || fxml.isSelected() || ffp.isSelected()) && !nom.getText().isEmpty());
    }

    private void onOK() {
        okPressed = true;
        dispose();
    }

    private void onCancel() {
        dispose();
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setMinimumSize(new Dimension(450, 230));
        contentPane.setPreferredSize(new Dimension(450, 230));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setEnabled(false);
        buttonOK.setText("Descarregar");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setLabel("Cancel.lar");
        buttonCancel.setText("Cancel.lar");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Format:");
        panel4.add(label1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Carpeta:");
        panel4.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Autor:");
        panel4.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Titol:");
        panel4.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Nom de l'arxiu:");
        panel4.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1, false, true));
        panel3.add(panel5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        path = new JTextField();
        panel5.add(path, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        authorDoc = new JLabel();
        authorDoc.setText("autor");
        panel5.add(authorDoc, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(71, 18), null, 0, false));
        titleDoc = new JLabel();
        titleDoc.setText("titol");
        panel5.add(titleDoc, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(71, 18), null, 0, false));
        browse = new JButton();
        browse.setText("Busca");
        panel5.add(browse, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ftxt = new JRadioButton();
        ftxt.setText("Text pla");
        panel5.add(ftxt, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(71, 22), null, 0, false));
        fxml = new JRadioButton();
        fxml.setText("XML");
        panel5.add(fxml, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(44, 22), null, 0, false));
        ffp = new JRadioButton();
        ffp.setText("Propietari");
        panel5.add(ffp, new GridConstraints(4, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nom = new JTextField();
        panel5.add(nom, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
