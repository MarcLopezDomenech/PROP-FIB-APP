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
    /**
     * \brief Panell amb el contingut, inicialitzat amb la GUI d'Intellij
     */
    private JPanel contentPane;
    /**
     * \brief Botó "Descarregar"
     */
    private JButton buttonOK;
    /**
     * \brief Botó "Tornar"
     */
    private JButton buttonCancel;
    /**
     * \brief Etiqueta que mostra el títol del document que s'ha seleccionat per exportar
     */
    private JLabel titleDoc;
    /**
     * \brief Etiqueta que mostra l'autor del document que s'ha seleccionat per exportar
     */
    private JLabel authorDoc;
    /**
     * \brief Camp on s'ha d'introduir la ruta del directori a on cal exportar el document
     */
    private JTextField path;
    /**
     * \brief Botó que ensenya un altre diàleg que permet navegar per indicar el directori a on es vol descarregar el document
     */
    private JButton browse;
    /**
     * \brief Botó a seleccionar quan es vol exportar el document en format de text pla
     */
    private JRadioButton ftxt;
    /**
     * \brief Botó a seleccionar quan es vol exportar el document en format XML
     */
    private JRadioButton fxml;
    /**
     * \brief Botó a seleccionar quan es vol exportar el document en format propietari
     */
    private JRadioButton ffp;
    /**
     * \brief Camp que cal omplir amb el nom que es desitja donar al fitxer que es crearà en la descàrrega
     */
    private JTextField name;
    /**
     * \brief Booleà usat quan cal retornar resultats
     * \invariant True si i només si s'ha premut el botó "Descarregar"
     */
    private boolean okPressed = false;

    /**
     * @return DownloaderDialog
     * @brief Creadora per defecte del diàleg de descàrrega de documents
     * @details S'inicialitza el diàleg i s'enllacen tots els listeners dels botons, així com dels camps a omplir
     * de manera que només es desbloqueja el botó "Descarregar" quan estan totes les dades.
     * Addicionalment, es prepara un JFileChooser amb la configuració adequada per a navegar per si es desitja així
     */
    public DownloaderDialog() {
        setTitle("Descarregar un document");
        setResizable(false);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        UIManager.put("FileChooser.cancelButtonText", "Tornar");
        UIManager.put("FileChooser.openButtonText", "Seleccionar");
        UIManager.put("FileChooser.lookInLabelText", "Buscar en:");
        UIManager.put("FileChooser.folderNameLabelText", "Nom de la carpeta:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipus:");
        JFileChooser fc = new JFileChooser(".");
        fc.setDialogTitle("Seleccionar directori on exportar");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setAcceptAllFileFilterUsed(false);
        FileFilter xml = new FileNameExtensionFilter("XML", "xml");
        FileFilter txt = new FileNameExtensionFilter("Text pla", "txt");
        FileFilter fp = new FileNameExtensionFilter("Format propietari", "fp");
        fc.addChoosableFileFilter(txt);
        fc.addChoosableFileFilter(xml);
        fc.addChoosableFileFilter(fp);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okPressed = true;
                dispose();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

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
            }
        });
        DocumentListener dl = new DocumentListener() {
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
        };

        path.getDocument().addDocumentListener(dl);
        name.getDocument().addDocumentListener(dl);

        ChangeListener cl = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableButtonIfCorrect();
            }
        };

        this.ftxt.addChangeListener(cl);
        this.fxml.addChangeListener(cl);
        this.ffp.addChangeListener(cl);
    }

    /**
     * @param reference Frame sobre el que es col·locarà i centrarà el diàleg
     * @param title     Títol del document que es vol exportar
     * @param author    Autor del document que es vol exportar
     * @return Una String que conté la ruta de l'arxiu a crear al descarregar el document seleccionat
     * @brief Mètode per a mostrar el diàleg inicialitzat amb el títol i l'autor donats, i que retorna la ruta a on cal
     * descarregar el document
     */
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

        return path.getText().strip() + name.getText().strip() + extension;
    }

    /**
     * @brief Mètode que bloqueja o desbloqueja el botó "Descarregar" en funció de si s'han introduït totes les dades necessàries
     * @post Si falten camps per omplir es bloqueja el botó "Descarregar", i si no, es desbloqueja
     */
    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!path.getText().isEmpty() && (ftxt.isSelected() || fxml.isSelected() || ffp.isSelected()) && !name.getText().isEmpty());
    }

    /**
     * \cond
     */
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
        contentPane.setMinimumSize(new Dimension(400, 300));
        contentPane.setPreferredSize(new Dimension(400, 300));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setLabel("Tornar");
        buttonCancel.setText("Tornar");
        panel1.add(buttonCancel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setEnabled(false);
        buttonOK.setText("Descarregar");
        panel1.add(buttonOK, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Format:");
        panel3.add(label1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Carpeta:");
        panel3.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Autor:");
        panel3.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Títol:");
        panel3.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Nom de l'arxiu:");
        panel3.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1, false, true));
        panel2.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        path = new JTextField();
        panel4.add(path, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        authorDoc = new JLabel();
        authorDoc.setText("author");
        panel4.add(authorDoc, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(71, 18), null, 0, false));
        titleDoc = new JLabel();
        titleDoc.setText("title");
        panel4.add(titleDoc, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(71, 18), null, 0, false));
        browse = new JButton();
        browse.setText("Busca");
        panel4.add(browse, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ftxt = new JRadioButton();
        ftxt.setText("Text pla");
        panel4.add(ftxt, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(71, 22), null, 0, false));
        fxml = new JRadioButton();
        fxml.setText("XML");
        panel4.add(fxml, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(44, 22), null, 0, false));
        ffp = new JRadioButton();
        ffp.setText("Propietari");
        panel4.add(ffp, new GridConstraints(4, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        name = new JTextField();
        panel4.add(name, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    /**
     * \endcond
     */
}
