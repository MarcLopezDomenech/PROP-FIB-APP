package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import main.domain.util.Pair;
import main.excepcions.ExceptionDocumentExists;
import main.excepcions.ExceptionInvalidLanguage;
import main.excepcions.ExceptionNoDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

/**
 * @author marc.lopez.domenech
 * @class ModifyDialog
 * @brief Diàleg per modificar un document, tant el seu títol, idioma o el contingut i guardar-ho o exportar-ho
 */
public class ModifyDialog extends JDialog {
    /**
     * \brief La instància de controlador de presentació de l'aplicació
     */
    private CtrlPresentation cp;
    /**
     * \brief La instància de subcontradlor de presentació de vistes i diàlegs de l'aplicació
     */
    private CtrlViewsDialogs cvd;
    /**
     * \brief El frame principal del diàleg
     */
    private JFrame modify;
    /**
     * \brief Panell amb el contingut, inicialitzat amb la GUI d'Intellij
     */
    private JPanel contentPane;
    /**
     * \brief Botó Guardar, per guardar el document modificat
     */
    private JButton buttonOK;
    /**
     * \brief Botó Tornar per tornar
     */
    private JButton buttonCancel;
    /**
     * \brief Camp per omplir el contingut del document
     */
    private JTextArea textcont;
    /**
     * \brief Botó per exportar el document
     */
    private JButton exportarButton;
    /**
     * \brief Botó per seleccionar l'idioma català
     */
    private JRadioButton cat;
    /**
     * \brief Botó per seleccionar l'idioma castellà
     */
    private JRadioButton es;
    /**
     * \brief Botó per seleccionar l'idioma anglès
     */
    private JRadioButton an;
    /**
     * \brief Camp per omplir el títol del document
     */
    private JTextField tit_field;
    /**
     * \brief Camp per omplir l'autor del document
     */
    private JTextField aut_field;
    /**
     * \brief String que guarda el títol del document
     * \invariant Mai pot estar buit
     */
    private String tit;
    /**
     * \brief String que guarda  de l'autor del document
     * \invariant Mai pot estar buit
     */
    private String auth;
    /**
     * \brief String que guarda  el contingut del document
     */
    private String cont;
    /**
     * \brief String que guarda la llengua del document
     * \invariant Mai pot estar buit
     */
    private String lang;
    /**
     * \brief String que guarda la llengua seleccionada en el formulari
     * \invariant Mai pot estar buit
     */
    private String setlang;
    /**
     * \brief Booleà usat per veure si ha ocorregut un error en l'execució d'alguna funció
     * \invariant Fals si no hi ha hagut cap error i True si sí
     */
    private Boolean err;
    /**
     * \brief Pair usat per emmagatzemar el resultat del diàleg és a dir si s'ha modificat el títol o autor s'envien
     * els finals si no s'envia null i null
     */
    private Pair<String, String> result;

    /**
     * @return ModifyDialog
     * @brief Creadora per defecte del diàleg de ModifyDialog
     * @details S'inicialitza el diàleg i s'enllacen tots els listeners dels botons, així com dels camps a omplir
     * de manera que només es desbloqueja el botó "Guardar" quan estan totes les dades.
     */
    public ModifyDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(cat);
        bgroup.add(es);
        bgroup.add(an);

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
        exportarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExport();
            }
        });
        textcont.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                enableButtonIfCorrect();
                //pack();
            }
        });

        an.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setlang = "en";
                enableButtonIfCorrect();
            }
        });
        es.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setlang = "es";
                enableButtonIfCorrect();
            }
        });
        cat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setlang = "ca";
                buttonOK.setEnabled(true);
            }
        });
        tit_field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                enableButtonIfCorrect();
            }
        });
        aut_field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                enableButtonIfCorrect();
            }
        });
    }

    /**
     * @brief Funció per tractar la pulsació al botó Guardar
     * @details Agafa els texts dels diferents camps i només actualitza aquells que s'han modificat
     */
    private void onOK() {
        String content_fin = textcont.getText();
        err = false;
        try {
            if (!Objects.equals(cont, content_fin)) {
                cp.updateContentDocument(tit, auth, content_fin);
            }
            if (!Objects.equals(lang, setlang)) {
                cp.updateLanguageDocument(tit, auth, setlang);
            }
        } catch (ExceptionNoDocument e) {
            cvd.showError(modify, "No existeix el document 2");
            err = true;
        } catch (ExceptionInvalidLanguage e) {
            cvd.showError(modify, "No existeix la llengua");
            err = true;
        }
        if (!err) {
            try {
                if (!Objects.equals(tit, tit_field.getText()) || !Objects.equals(auth, aut_field.getText())) {
                    String nout = tit_field.getText();
                    String nouat = aut_field.getText();
                    cp.updateTitleAndAuthorDocument(tit, auth, nout, nouat);
                }
            } catch (ExceptionNoDocument e) {
                cvd.showError(modify, "No existeix el document");
                err = true;
            } catch (ExceptionDocumentExists e) {
                cvd.showError(modify, "Ja existeix un document amb aquest títol i autor");
                err = true;
            }
        }
        if (!err) {
            lang = setlang;
            cont = content_fin;
            tit = tit_field.getText();
            auth = aut_field.getText();
            buttonOK.setEnabled(false);
        }
    }

    /**
     * @brief Funció per tractar la pulsació al botó Tornar
     * @details Comprova si hi ha modificacions, si és així retorna un diàleg de confirmació i
     * si no posa el resultat a (null,null) i retorna.
     */
    private void onCancel() {
        // add your code here if necessary
        if (buttonOK.isEnabled()) {
            if (cvd.askConfirmation(modify, "Segur que vols sortir sense guardar")) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    /**
     * @brief Funció per tractar la pulsació al botó Exportar
     * @details Comprova si hi ha modificacions, si és així retorna un diàleg de confirmació per
     * exportar amb o sense les modificacions, si no crida directament el diàleg d'exportar (DownloaderDialog).
     */
    private void onExport() {
        if (!buttonOK.isEnabled() || cvd.askConfirmation(modify, "Vols guardar i exportar?")) {
            onOK();
        }
        if (!err) {
            cvd.showDownloader(modify, tit, auth);
        }
    }

    /**
     * @brief Funció per habilitar o inhabilitar el botó Guardar
     * @details Comprova si tots els camps necessaris per a un document han sigut introduïts per habilitar el botó Guardar, si no és així
     * l'inhabilita.
     */
    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!(tit_field.getText().equals("") || aut_field.getText().equals("")));
        exportarButton.setEnabled(!(tit_field.getText().equals("") || aut_field.getText().equals("")));
    }

    /**
     * @param reference Frame sobre el que es col·locarà i centrarà el diàleg
     * @param title     Títol del document que es vol modificar
     * @param author    Autor del document que es vol modificar
     * @return Un pair que té títol i autor si s'ha modificat el seu camp, si no null
     * @brief Mètode per a mostrar el diàleg inicialitzat amb el títol i l'autor donats, la llengua del document
     * i el seu contingut.
     */
    public Pair<String, String> initialize(JFrame reference, String title, String author) {
        setTitle("Modificar document");
        result = new Pair<String, String>(null, null);
        buttonOK.setEnabled(false);
        cp = CtrlPresentation.getInstance();
        cvd = CtrlViewsDialogs.getInstance();
        err = false;

        tit = title;
        auth = author;

        try {
            cont = cp.getContentDocument(tit, auth);
        } catch (ExceptionNoDocument e) {
            cvd.showError(modify, "No existeix el document");
        }
        tit_field.setText(tit);
        aut_field.setText(auth);
        textcont.setText(cont);
        lang = "";
        String lan = "";
        try {
            lan = cp.getLanguageDocument(title, author);
        } catch (ExceptionNoDocument e) {
            cvd.showError(modify, "No existeix el document");
        }
        if (lan.equals("ca")) {
            cat.setSelected(true);
            lang = "ca";

        } else if (Objects.equals(lan, "es")) {
            es.setSelected(true);
            lang = "es";
        } else if (Objects.equals(lan, "en")) {
            an.setSelected(true);
            lang = "en";
        } else {
            cvd.showError(modify, "Document sense llengua");
        }
        setlang = lang;
        if (!(cat.isSelected() || es.isSelected() || an.isSelected())) {
            exportarButton.setEnabled(false);
            buttonOK.setEnabled(false);
        }
        pack();
        this.modify = reference;
        setLocationRelativeTo(reference);
        setVisible(true);
        if ((Objects.equals(title, tit) && Objects.equals(auth, author)) || err) {
            return null;
        } else {
            result = new Pair<String, String>(tit, auth);
            return result;
        }
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
        contentPane.setPreferredSize(new Dimension(400, 300));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Guardar");
        panel2.add(buttonOK, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportarButton = new JButton();
        exportarButton.setText("Exportar");
        panel2.add(exportarButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Tornar");
        panel1.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPane.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textcont = new JTextArea();
        scrollPane1.setViewportView(textcont);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Titol:");
        panel4.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Autor:");
        panel4.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cat = new JRadioButton();
        cat.setText("Català");
        panel4.add(cat, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        es = new JRadioButton();
        es.setText("Espanyol");
        panel4.add(es, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        an = new JRadioButton();
        an.setText("Anglès");
        panel4.add(an, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Llengua:");
        panel4.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tit_field = new JTextField();
        panel4.add(tit_field, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        aut_field = new JTextField();
        panel4.add(aut_field, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    /**
     * \endcond
     */
}