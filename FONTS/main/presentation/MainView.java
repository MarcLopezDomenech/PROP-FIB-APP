package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import main.excepcions.ExceptionNoDocument;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;

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
    private JFileChooser exporter;

    public MainView() {
        cp = CtrlPresentation.getInstance();
        frame = new JFrame("Gestió de documents");
        selectedIndex = -1;

        UIManager.put("FileChooser.cancelButtonText", "Cancel.lar");
        UIManager.put("FileChooser.lookInLabelText", "Buscar en");
        UIManager.put("FileChooser.directoryOpenButtonText", "Obrir");
        UIManager.put("FileChooser.fileNameLabelText", "Nom del document:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipus");
        UIManager.put("FileChooser.directoryOpenButtonText", "Seleccionar");
        exporter = new JFileChooser(".");
        exporter.setApproveButtonText("Descarregar");
        exporter.setDialogTitle("Descarregar document");
        exporter.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        exporter.setDialogType(JFileChooser.SAVE_DIALOG);
        exporter.setAcceptAllFileFilterUsed(false);
        FileFilter xml = new FileNameExtensionFilter("XML", "xml");
        FileFilter txt = new FileNameExtensionFilter("Text pla", "txt");
        FileFilter fp = new FileNameExtensionFilter("Format propietari", "fp");
        exporter.setFileFilter(txt);
        exporter.addChoosableFileFilter(xml);
        exporter.addChoosableFileFilter(fp);
        exporter.setMultiSelectionEnabled(true);


        $$$setupUI$$$();
        menubar = new JMenuBar();

        menuOptions = new JMenu("Menu");
        load = new JMenuItem("Carregar document");
        menuOptions.add(load);
        create = new JMenuItem("Nou document");
        menuOptions.add(create);
        expressions = new JMenuItem("Gestio expressions");
        menuOptions.add(expressions);
        menubar.add(menuOptions);

        menuList = new JMenu("Llistar per");
        listByQuery = new JMenuItem("Query");
        menuList.add(listByQuery);
        listByExpression = new JMenuItem("Expressio");
        menuList.add(listByExpression);
        listByTitleAuthor = new JMenuItem("Autor i titol");
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
                cp.showLoader(frame.getLocation());
            }
        });

        expressions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.showExpressions(frame.getLocation(), frame.getSize());
                frame.dispose();

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

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.showNewDocument(frame.getLocation());
            }
        });
    }

    public void initialize(Point location, Dimension size) {
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setSize(size);
        frame.setLocation(location);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        similars = new JButton();
        similars.setEnabled(false);
        similars.setText("List similars");
        panel1.add(similars, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        export = new JButton();
        export.setEnabled(false);
        export.setText("Exportar");
        panel1.add(export, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        delete = new JButton();
        delete.setEnabled(false);
        delete.setText("Esborrar");
        panel1.add(delete, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        modify = new JButton();
        modify.setEnabled(false);
        modify.setText("Modify");
        panel1.add(modify, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        menubar = new JMenuBar();
        menubar.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(menubar, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
        Object[][] data = cp.listAllDocuments();
        String[] headers = new String[]{"Fav", "Titol", "Autor"};
        documentsModel = new DefaultTableModel(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        documents = new JTable(documentsModel);

        TableColumn tc = documents.getColumnModel().getColumn(0);
        tc.setCellEditor(documents.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(documents.getDefaultRenderer(Boolean.class));

        JTableHeader header = documents.getTableHeader();
        Font font = new Font("Arial", Font.BOLD, 14);
        header.setFont(font);
    }
}
