package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import main.excepcions.ExceptionNoDocument;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FileChooserUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class MainView {
    private CtrlPresentation cp;
    private JFrame frame;
    private JPanel panel;
    private JButton similars;
    private JButton export;
    private JButton delete;
    private JMenuBar menubar;
    private JButton modify;
    private JTable documents;
    private DefaultTableModel documentsModel;
    private JMenu menuOptions;
    private JMenuItem load;
    private JMenuItem create;
    private JMenuItem expressions;
    private JMenu menuList;
    private JMenuItem listByQuery;
    private JMenuItem listByExpression;
    private JMenuItem listByTitleAuthor;
    private int selectedIndex;
    private JFileChooser loader;
    private JFileChooser exporter;

    public MainView() {
        cp = CtrlPresentation.getInstance();
        frame = new JFrame("Gestió de documents");
        selectedIndex = -1;

        UIManager.put("FileChooser.cancelButtonText", "Cancel·lar");
        UIManager.put("FileChooser.lookInLabelText", "Buscar en");
        UIManager.put("FileChooser.directoryOpenButtonText", "Obrir");
        UIManager.put("FileChooser.fileNameLabelText", "Nom del document:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipus");
        // TODO: traduir tots els tooltips, es poden customitzar icones!

        loader = new JFileChooser(".");
        loader.setApproveButtonText("Carregar");
        loader.setDialogTitle("Carregar document");
        loader.setDialogType(JFileChooser.OPEN_DIALOG);
        FileFilter xml = new FileNameExtensionFilter("XML", "xml");
        FileFilter txt = new FileNameExtensionFilter("Text pla", "txt");
        FileFilter fp = new FileNameExtensionFilter("Format propietari", "fp");
        loader.setAcceptAllFileFilterUsed(false);
        loader.setFileFilter(txt);
        loader.addChoosableFileFilter(xml);
        loader.addChoosableFileFilter(fp);
        loader.setMultiSelectionEnabled(true);

        UIManager.put("FileChooser.directoryOpenButtonText", "Seleccionar");
        exporter = new JFileChooser(".");
        exporter.setApproveButtonText("Descarregar");
        exporter.setDialogTitle("Descarregar document");
        exporter.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        exporter.setDialogType(JFileChooser.SAVE_DIALOG);
        exporter.setAcceptAllFileFilterUsed(false);
        exporter.setFileFilter(txt);
        exporter.addChoosableFileFilter(xml);
        exporter.addChoosableFileFilter(fp);
        exporter.setMultiSelectionEnabled(true);


        $$$setupUI$$$();
        menubar = new JMenuBar();

        menuOptions = new JMenu("Opcions");
        load = new JMenuItem("Carregar document");
        menuOptions.add(load);
        create = new JMenuItem("Nou document");
        menuOptions.add(create);
        expressions = new JMenuItem("Gestió expressions");
        menuOptions.add(expressions);
        menubar.add(menuOptions);

        menuList = new JMenu("Llistar per");
        listByQuery = new JMenuItem("Query");
        menuList.add(listByQuery);
        listByExpression = new JMenuItem("Expressió");
        menuList.add(listByExpression);
        listByTitleAuthor = new JMenuItem("Autor i títol");
        menuList.add(listByTitleAuthor);
        menubar.add(menuList);

        frame.setJMenuBar(menubar);

        ListSelectionModel selection = documents.getSelectionModel();
        selection.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                selectedIndex = documents.getSelectedRow();
                modify.setEnabled(selectedIndex != -1);
                similars.setEnabled(selectedIndex != -1);
                delete.setEnabled(selectedIndex != -1);
                export.setEnabled(selectedIndex != -1);
            }
        });

        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modify.grabFocus();
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String title = (String) documentsModel.getValueAt(selectedIndex, 1);
                    String author = (String) documentsModel.getValueAt(selectedIndex, 2);
                    cp.deleteDocument(title, author);
                } catch (ExceptionNoDocument ex) {
                    // No és possible
                }
                documentsModel.removeRow(selectedIndex);
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = loader.showOpenDialog(MainView.this.$$$getRootComponent$$$());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] files = loader.getSelectedFiles();
                    for (File f : files) {
                        System.out.println(f.getAbsolutePath());
                    }
                }
            }
        });

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = exporter.showOpenDialog(MainView.this.$$$getRootComponent$$$());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] files = exporter.getSelectedFiles();
                    for (File f : files) {
                        System.out.println(f.getAbsolutePath());
                    }
                }
            }
        });
    }

    public void initialize() {


        //documents.setFillsViewportHeight(true);
        //panel = new JPanel();
        //panel.add(documents);
        //documents.setAutoCreateRowSorter(true);

        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setSize(600, 400);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        similars = new JButton();
        similars.setEnabled(false);
        similars.setText("List similars");
        panel1.add(similars, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        export = new JButton();
        export.setEnabled(false);
        export.setText("Exportar");
        panel1.add(export, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        delete = new JButton();
        delete.setEnabled(false);
        delete.setText("Esborrar");
        panel1.add(delete, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        modify = new JButton();
        modify.setEnabled(false);
        modify.setText("Modify");
        panel1.add(modify, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        menubar = new JMenuBar();
        menubar.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(menubar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        documents.setAutoCreateRowSorter(true);
        documents.setFillsViewportHeight(true);
        documents.setShowVerticalLines(false);
        scrollPane1.setViewportView(documents);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

    private void createUIComponents() {
        Object[][] data = cp.getDocumentsData();
        String[] headers = new String[]{"favs", "Títol", "Autor"};
        documentsModel = new DefaultTableModel(data, headers);
        documents = new JTable(documentsModel);

        JTableHeader header = documents.getTableHeader();
        Font font = new Font("Arial", Font.BOLD, 14);
        header.setFont(font);
    }
}
