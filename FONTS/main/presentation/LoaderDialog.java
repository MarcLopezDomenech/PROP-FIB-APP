package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import main.domain.util.Pair;
import main.excepcions.ExceptionDocumentExists;
import main.excepcions.ExceptionInvalidFormat;
import main.excepcions.ExceptionInvalidLanguage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author marc.valls.camps
 * @class LoaderDialog
 * @brief Diàleg per a carregar un document extern al sistema
 */
public class LoaderDialog extends JDialog {
    /**
     * \brief Panell amb el contingut, inicialitzat amb la GUI d'Intellij
     */
    private JPanel contentPane;
    /**
     * \brief Botó "Carregar"
     */
    private JButton buttonOK;
    /**
     * \brief Botó "Tornar"
     */
    private JButton buttonCancel;
    /**
     * \brief Camp on s'han d'introduir separades per punt i coma les rutes als fitxers a carregar
     */
    private JTextField path;
    /**
     * \brief Botó que ensenya un altre diàleg que permet navegar per trobar els fitxers a carregar
     */
    private JButton browse;
    /**
     * \brief Botó a seleccionar quan el contingut dels documents a carregar seran tots en català
     */
    private JRadioButton cat;
    /**
     * \brief Botó a seleccionar quan el contingut dels documents a carregar seran tots en espanyol
     */
    private JRadioButton esp;
    /**
     * \brief Botó a seleccionar quan el contingut dels documents a carregar seran tots en anglès
     */
    private JRadioButton eng;
    /**
     * \brief Booleà usat quan cal retornar resultats
     * \invariant True si i només si s'ha premut el botó "Carregar"
     */
    private boolean okPressed = false;

    /**
     * @return LoaderDialog
     * @brief Creadora per defecte del diàleg de càrrega de documents
     * @details S'inicialitza el diàleg i s'enllacen tots els listeners dels botons, així com dels camps a omplir
     * de manera que només es desbloqueja el botó "Carregar" quan estan totes les dades.
     * Addicionalment, es prepara un JFileChooser amb la configuració adequada per a navegar per si es desitja així
     */
    public LoaderDialog() {
        setTitle("Carregar documents");
        setResizable(false);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        UIManager.put("FileChooser.cancelButtonText", "Tornar");
        UIManager.put("FileChooser.lookInLabelText", "Buscar en:");
        UIManager.put("FileChooser.directoryOpenButtonText", "Obrir");
        UIManager.put("FileChooser.fileNameLabelText", "Nom del document:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipus:");

        JFileChooser fc = new JFileChooser(".");
        fc.setApproveButtonText("Seleccionar");
        fc.setDialogTitle("Seleccionar document(s) a carregar");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        FileFilter xml = new FileNameExtensionFilter("XML", "xml");
        FileFilter txt = new FileNameExtensionFilter("Text pla", "txt");
        FileFilter fp = new FileNameExtensionFilter("Format propietari", "fp");
        FileFilter tots = new FileNameExtensionFilter("Tots els formats", "xml", "txt", "fp");
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(txt);
        fc.addChoosableFileFilter(xml);
        fc.addChoosableFileFilter(fp);
        fc.addChoosableFileFilter(tots);
        fc.setMultiSelectionEnabled(true);

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
                    for (File f : files) path.setText(path.getText() + f.getAbsolutePath() + ";");
                    path.setText(path.getText().substring(0, path.getText().length() - 1));
                }
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

        ChangeListener cl = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                enableButtonIfCorrect();
            }
        };

        cat.addChangeListener(cl);
        esp.addChangeListener(cl);
        eng.addChangeListener(cl);
    }

    /**
     * @param reference Frame sobre el que es col·locarà i centrarà el diàleg
     * @return Un parell on la primera component és la llengua en la qual estan els documents a carregar,
     * i la segona component consisteix en l'array de les rutes als fitxers a carregar
     * @brief Mètode per a mostrar el diàleg que retorna les dades dels fitxers a carregar al sistema
     */
    public Pair<String, String[]> initialize(JFrame reference) {
        pack();
        setLocationRelativeTo(reference);
        setVisible(true);

        if (!okPressed) return null;

        Pair<String, String[]> res = new Pair<>();

        if (cat.isSelected()) res.setFirst("ca");
        else if (esp.isSelected()) res.setFirst("es");
        else res.setFirst("en");

        res.setSecond(path.getText().split(";[ ]*"));

        return res;
    }

    /**
     * @brief Mètode que bloqueja o desbloqueja el botó "Carregar" en funció de si s'han introduït totes les dades necessàries
     * @post Si falten camps per omplir es bloqueja el botó "Carregar", i si no, es desbloqueja
     */
    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!path.getText().isEmpty() && (cat.isSelected() || esp.isSelected() || eng.isSelected()));
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
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setMinimumSize(new Dimension(400, 300));
        contentPane.setPreferredSize(new Dimension(400, 300));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setLabel("Tornar");
        buttonCancel.setText("Tornar");
        panel1.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setEnabled(false);
        buttonOK.setText("Carregar");
        panel1.add(buttonOK, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        contentPane.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1, false, true));
        panel2.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Llengua:");
        panel3.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(39, 18), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Rutes:");
        panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(39, 18), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1, false, true));
        panel2.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        path = new JTextField();
        panel4.add(path, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        browse = new JButton();
        browse.setText("Busca");
        panel4.add(browse, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cat = new JRadioButton();
        cat.setText("Català");
        panel4.add(cat, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        esp = new JRadioButton();
        esp.setText("Espanyol");
        panel4.add(esp, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eng = new JRadioButton();
        eng.setText("Anglès");
        panel4.add(eng, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
