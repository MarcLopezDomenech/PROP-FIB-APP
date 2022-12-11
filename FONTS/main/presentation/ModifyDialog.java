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
    private CtrlPresentation cp;

    private JFrame modify;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textcont;
    private JButton exportarButton;
    private JRadioButton cat;
    private JRadioButton es;
    private JRadioButton an;
    private JTextField tit_field;
    private JTextField aut_field;

    private String tit;
    private String auth;
    private String cont;

    private String lang;

    private Boolean err;

    private Pair<String, String> result;

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
                pack();
            }
        });

        an.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lang = "en";
                enableButtonIfCorrect();
            }
        });
        es.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lang = "es";
                enableButtonIfCorrect();
            }
        });
        cat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lang = "ca";
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

    private void onOK() {
        err = false;
        try {
            if (!Objects.equals(tit, tit_field.getText()) || !Objects.equals(auth, aut_field.getText())) {
                String nout=tit_field.getText();
                String nouat=aut_field.getText();
                cp.updateTitleAndAuthorDocument(tit, auth, nout, nouat);
            }
        } catch (ExceptionNoDocument e) {
            cp.showError(modify, "No existeix el document");
            err = true;
        } catch (ExceptionDocumentExists e) {
            cp.showError(modify, "Ja existeix un document amb aquest títol i autor");
            err = true;
        }
        try{
            String content_fin = textcont.getText();
            cp.updateContentDocument(tit_field.getText(), aut_field.getText(), content_fin);
            cp.updateLanguageDocument(tit_field.getText(), aut_field.getText(), lang);
        } catch (ExceptionNoDocument e) {
            cp.showError(modify, "No existeix el document 2");
            err = true;
        } catch (ExceptionInvalidLanguage e) {
            cp.showError(modify, "No existeix la llengua");
            err = true;
        }
        if (!err) {
            tit = tit_field.getText();
            auth = aut_field.getText();
            buttonOK.setEnabled(false);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        if (buttonOK.isEnabled()) {
            if (cp.askConfirmation(modify, "Segur que vols sortir sense guardar")) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    private void onExport() {
        if (!buttonOK.isEnabled() || cp.askConfirmation(modify, "Vols guardar i exportar?")) {
            onOK();
        }
        if (!err) {
            cp.showDownloader(modify, tit, auth);
        }
    }

    private void enableButtonIfCorrect() {
        buttonOK.setEnabled(!(tit_field.getText().equals("") || aut_field.getText().equals("")));
    }

    public Pair<String, String> initialize(JFrame reference, String title, String author) {
        setTitle("Modificar document");
        result = new Pair<String, String>(null, null);
        buttonOK.setEnabled(false);
        cp = CtrlPresentation.getInstance();
        err = false;

        tit = title;
        auth = author;

        try {
            cont = cp.getContentDocument(tit, auth);
        } catch (ExceptionNoDocument e) {
            cp.showError(modify, "No existeix el document");
        }
        tit_field.setText(tit);
        aut_field.setText(auth);
        textcont.setText(cont);
        lang = "";
        String lan = "";
        try {
            lan = cp.getLanguageDocument(title, author);
        } catch (ExceptionNoDocument e) {
            cp.showError(modify, "No existeix el document");
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
            cp.showError(modify, "Document sense llengua");
        }
        if (!(cat.isSelected() || es.isSelected() || an.isSelected())) {
            exportarButton.setEnabled(false);
            buttonOK.setEnabled(false);
        }
        pack();
        this.modify = reference;
        setLocationRelativeTo(reference);
        setVisible(true);
        if ((Objects.equals(title, tit) && Objects.equals(auth, author)) || err) {
            result = new Pair<String, String>(null, null);
        } else {
            result = new Pair<String, String>(tit, auth);
        }
        return result;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }
    /**
     * \cond
     */

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
        contentPane.setPreferredSize(new Dimension(400, 300));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Guardar");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportarButton = new JButton();
        exportarButton.setText("Exportar");
        panel2.add(exportarButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Tornar");
        panel1.add(buttonCancel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPane.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        scrollPane1.setViewportView(scrollPane2);
        textcont = new JTextArea();
        scrollPane2.setViewportView(textcont);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Titol:");
        panel4.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Autor:");
        panel4.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cat = new JRadioButton();
        cat.setText("Catala");
        panel4.add(cat, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        es = new JRadioButton();
        es.setText("Espanyol");
        panel4.add(es, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        an = new JRadioButton();
        an.setText("Angles");
        panel4.add(an, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Llengua:");
        panel4.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tit_field = new JTextField();
        panel4.add(tit_field, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        aut_field = new JTextField();
        panel4.add(aut_field, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
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