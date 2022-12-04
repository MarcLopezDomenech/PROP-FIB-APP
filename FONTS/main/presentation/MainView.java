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

/**
 * @author marc.valls.camps i pau.duran.manzano
 * @class MainView
 * @brief Vista principal de l'aplicatiu, que serveix per gestionar els documents
 */
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
    private Object[][] data;
    private String[] headers;

    public MainView() {
        cp = CtrlPresentation.getInstance();
        frame = new JFrame("Gestió de documents");
        selectedIndex = -1;

        $$$setupUI$$$();
        //Image bobby = new ImageIcon()
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
                String title = (String) documents.getValueAt(selectedIndex, 1);
                String author = (String) documents.getValueAt(selectedIndex, 2);
                cp.showModify(frame, title, author);
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = (String) documents.getValueAt(selectedIndex, 1);
                String author = (String) documents.getValueAt(selectedIndex, 2);
                boolean confirm = cp.askConfirmation(frame, "Segur/a que vols esborrar el " +
                        "document amb títol '" + title + "' i autor '" + author + "'?");
                if (confirm) {
                    try {
                        cp.deleteDocument(title, author);
                        documentsModel.removeRow(selectedIndex);
                    } catch (ExceptionNoDocument ex) {
                        // No és possible
                        cp.showInternalError(frame);
                    }
                }
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.showLoader(frame);
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
                String title = (String) documents.getValueAt(selectedIndex, 1);
                String author = (String) documents.getValueAt(selectedIndex, 2);
                cp.showDownloader(frame, title, author);
            }
        });

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.showNewDocument(frame);
            }
        });

        similars.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = (String) documents.getValueAt(selectedIndex, 1);
                String author = (String) documents.getValueAt(selectedIndex, 2);
                Object[][] res = cp.showListKSimilars(frame, title, author);
                documentsModel.setDataVector(res, headers);
            }
        });
    }

    public void initialize(Point location, Dimension size) {
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setSize(size);
        frame.setLocation(location);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                cp.closeApp(frame);
                System.exit(0);
            }
        });
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
        similars.setText("Llistar similars");
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
        modify.setText("Modificar");
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
        data = cp.listAllDocuments();
        headers = new String[]{"", "Titol", "Autor"};
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

        tc.setMinWidth(30);
        tc.setMaxWidth(30);
    }
}
