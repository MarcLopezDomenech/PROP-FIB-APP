package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import main.excepcions.ExceptionNoDocument;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
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
    private JMenuItem help;
    private JMenuItem reset;
    private JMenu menuList;
    private JMenuItem listByQuery;
    private JMenuItem listByExpression;
    private JMenuItem listByAuthor;
    private JMenuItem listByNothing;
    private int selectedIndex;

    public MainView() {
        cp = CtrlPresentation.getInstance();
        frame = new JFrame("Gestio de documents");
        selectedIndex = -1;

        $$$setupUI$$$();

        // Inicialització dels menús
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
        listByAuthor = new JMenuItem("Autor");
        menuList.add(listByAuthor);
        listByNothing = new JMenuItem("Esborrar filtres");
        listByNothing.setVisible(false);        // Al principi no hi ha cap filtre aplicat
        menuList.add(listByNothing);
        menubar.add(menuList);

        help = new JMenuItem("?");
        menubar.add(help);

        reset = new JMenuItem("Reset");
        menubar.add(reset);

        frame.setJMenuBar(menubar);

        // Opcions del menú general
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[][] new_data = cp.showLoader(frame);
                if (new_data != null) for (Object[] r : new_data) documentsModel.addRow(r);
            }
        });

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] new_data = cp.showNewDocument(frame);
                if (new_data != null) documentsModel.addRow(new_data);
            }
        });

        expressions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.showExpressions(frame.getLocation(), frame.getSize());
                frame.dispose();

            }
        });


        // Opcions del menú de llistar
        listByQuery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[][] newData = cp.showListByQuery(frame);
                if (newData != null) {
                    updateData(newData);
                    listByNothing.setVisible(true);
                }
            }
        });

        listByExpression.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[][] newData = cp.showListByExpression(frame);
                if (newData != null) {
                    updateData(newData);
                    listByNothing.setVisible(true);
                }
            }
        });

        listByAuthor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[][] newData = cp.showListByAuthor(frame);
                if (newData != null) {
                    updateData(newData);
                    listByNothing.setVisible(true);
                }
            }
        });

        listByNothing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[][] newData = cp.listAllDocuments();
                updateData(newData);
                listByNothing.setVisible(false);
            }
        });


        // Help
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.showHelp(frame, "Per fer funcionar aquesta pantalla, has de ...");
            }
        });


        // Reset
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean confirm = cp.askConfirmation(frame, "ATENCIO! Estas a punt de esborrar tot el contingut del sistema. Aquesta accio es irreversible. Estas segur que vols fer reset?");
                if (confirm) {
                    cp.reset();
                    documentsModel.setRowCount(0);
                }
            }
        });


        // Gestió de la selecció d'un document
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


        // Opcions quan tenim un document seleccionat
        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modify.grabFocus();
                String title = (String) documents.getValueAt(selectedIndex, 1);
                String author = (String) documents.getValueAt(selectedIndex, 2);
                cp.showModify(frame, title, author);
            }
        });

        similars.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = (String) documents.getValueAt(selectedIndex, 1);
                String author = (String) documents.getValueAt(selectedIndex, 2);
                Object[][] result = cp.showListKSimilars(frame, title, author);
                if (result != null) {
                    updateData(result);
                    listByNothing.setVisible(true);
                }
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
                        documentsModel.removeRow(documents.convertRowIndexToModel(selectedIndex));
                    } catch (ExceptionNoDocument ex) {
                        // No és possible
                        cp.showInternalError(frame);
                    }
                }
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

        documentsModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == 0) {
                    boolean newFav = (boolean) documents.getValueAt(row, 0);
                    String title = (String) documents.getValueAt(row, 1);
                    String author = (String) documents.getValueAt(row, 2).toString();
                    try {
                        cp.updateFavouriteDocument(title, author, newFav);
                    } catch (ExceptionNoDocument ex) {
                        // No pot ser que no trobem el document
                        cp.showInternalError(frame);
                    }
                }
            }
        });


        // Doble click per modificar un document
        documents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);
                if (event.getClickCount() == 2) modify.doClick();
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

    private void updateData(Object[][] data) {
        String[] headers = new String[]{"<3", "Titol", "Autor"};

        documentsModel.setDataVector(data, headers);

        TableColumn tc = documents.getColumnModel().getColumn(0);
        tc.setCellEditor(documents.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(documents.getDefaultRenderer(Boolean.class));

        documents.getTableHeader().setReorderingAllowed(false);

        tc.setMinWidth(30);
        tc.setMaxWidth(30);
    }

    private void createUIComponents() {
        documentsModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        documents = new JTable(documentsModel);
        updateData(cp.listAllDocuments());

        JTableHeader header = documents.getTableHeader();
        Font font = new Font("Arial", Font.BOLD, 14);
        header.setFont(font);
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

}
