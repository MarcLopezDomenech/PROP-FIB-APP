package main.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import main.domain.util.Pair;
import main.excepcions.ExceptionNoDocument;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author marc.valls.camps i pau.duran.manzano
 * @class MainView
 * @brief Vista principal de l'aplicatiu, que serveix per gestionar els documents
 */
public class MainView {
    /**
     * \brief La instància de controlador de presentació de l'aplicació
     */
    private CtrlPresentation cp;
    /**
     * \brief La instància de subcontradlor de presentació de vistes i diàlegs de l'aplicació
     */
    private CtrlViewsDialogs cvd;
    /**
     * \brief El frame principal de la vista
     */
    private JFrame frame;
    /**
     * \brief El panell de contingut de la vista
     */
    private JPanel panel;
    /**
     * \brief Botó per a modificar un document seleccionat d'entre els que es mostren
     */
    private JButton modify;
    /**
     * \brief Botó per a llistar documents similars a un de seleccionat d'entre els que es mostren
     */
    private JButton similars;
    /**
     * \brief Botó per a descarregar un document seleccionat d'entre els que es mostren
     */
    private JButton export;
    /**
     * \brief Botó per a esborrar del sistema un document seleccionat d'entre els que es mostren
     */
    private JButton delete;
    /**
     * \brief Barra de menús de la vista
     */
    private JMenuBar menubar;
    /**
     * \brief Taula on es mostren tots els documents del sistema, o una selecció d'aquests si s'ha fet alguna consulta
     */
    private JTable documents;
    /**
     * \brief Model de la taula de la vista
     */
    private DefaultTableModel documentsModel;
    /**
     * \brief Menú que mostra les eines més genèriques
     */
    private JMenu menuOptions;
    /**
     * \brief Item del menú d'opcions que servirà per carregar fitxers externs al sistema com a documents
     */
    private JMenuItem load;
    /**
     * \brief Item del menú d'opcions que servirà per crear nous documents sense contingut
     */
    private JMenuItem create;
    /**
     * \brief Item del menú d'opcions que es oferirà la possibilitat de canviar a la vista ExpressionsView
     */
    private JMenuItem expressions;
    /**
     * \brief Menú que mostra les eines de les quals l'usuari disposa per llista i filtrar documents del sistema
     */
    private JMenu menuList;
    /**
     * \brief Item del menú de llistar que servirà per llistar els documents segons una query
     */
    private JMenuItem listByQuery;
    /**
     * \brief Item del menú de llistar que servirà per llistar els documents segons si verifiquen una expressió booleana
     */
    private JMenuItem listByExpression;
    /**
     * \brief Item del menú de llistar que es servirà per llistar els documents d'un autor
     */
    private JMenuItem listByAuthor;
    /**
     * \brief Item del menú de llistar que es mostrarà quan s'hagin fet servir filtres, per tornar a veure tots els documents del sistema
     */
    private JMenuItem listByNothing;
    /**
     * \brief Opció de la barra de menús de la vista per mostrar l'ajuda a l'usuari
     */
    private JMenuItem help;
    /**
     * \brief Opció de la barra de menús per fer reset del sistema
     */
    private JMenuItem reset;
    /**
     * @brief índex de la fila actualment seleccionada de la taula
     * @invariant índex de la fila seleccionada a la taula (diferent de l'índex de les dades), o -1 si no s'ha seleccionat cap fila
     */
    private int selectedIndex;

    /**
     * @return MainView
     * @brief Creadora per defecte de la vista
     * @details S'inicialitza la barra de menús i s'enllacen tots els listeners de botons, events de mouse i altres a les funcionalitats corresponents
     */
    public MainView() {
        cp = CtrlPresentation.getInstance();
        cvd = CtrlViewsDialogs.getInstance();
        frame = new JFrame("Gestió de documents");
        selectedIndex = -1;

        $$$setupUI$$$();

        // Inicialització dels menús
        menubar = new JMenuBar();

        menuOptions = new JMenu("Menú");
        load = new JMenuItem("Carregar document");
        menuOptions.add(load);
        create = new JMenuItem("Nou document");
        menuOptions.add(create);
        expressions = new JMenuItem("Gestió d'expressions");
        menuOptions.add(expressions);
        menubar.add(menuOptions);

        menuList = new JMenu("Llistar per");
        listByQuery = new JMenuItem("Query");
        menuList.add(listByQuery);
        listByExpression = new JMenuItem("Expressió");
        menuList.add(listByExpression);
        listByAuthor = new JMenuItem("Autor");
        menuList.add(listByAuthor);
        listByNothing = new JMenuItem("Esborrar filtres");
        listByNothing.setVisible(false);        // Al principi no hi ha cap filtre aplicat
        menuList.add(listByNothing);
        menubar.add(menuList);

        help = new JMenuItem("     ?");
        help.setMaximumSize(new Dimension(50, 30));
        menubar.add(help);

        JMenuItem spacing = new JMenuItem("");
        spacing.setFocusable(false);
        menubar.add(spacing);

        reset = new JMenuItem("Reset");
        reset.setMaximumSize(new Dimension(100, 30));
        menubar.add(reset);

        frame.setJMenuBar(menubar);

        // Opcions del menú general
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cvd.showLoader(frame)) {
                    Object[][] newData = cp.listAllDocuments();
                    updateData(newData);
                    listByNothing.setVisible(false);
                }
            }
        });

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cvd.showNewDocument(frame)) {
                    Object[][] newData = cp.listAllDocuments();
                    updateData(newData);
                    listByNothing.setVisible(false);
                }
            }
        });

        expressions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cvd.showExpressions(frame.getLocation(), frame.getSize());
                frame.dispose();

            }
        });


        // Opcions del menú de llistar
        listByQuery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[][] newData = cvd.showListByQuery(frame);
                if (newData != null) {
                    updateData(newData);
                    listByNothing.setVisible(true);
                }
            }
        });

        listByExpression.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[][] newData = cvd.showListByExpression(frame);
                if (newData != null) {
                    updateData(newData);
                    listByNothing.setVisible(true);
                }
            }
        });

        listByAuthor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[][] newData = cvd.showListByAuthor(frame);
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
                cvd.showHelp(frame,
                        "<html>" +
                                "Aquesta és la pantalla de gestió de documents.<br><br>" +
                                "Un cop seleccionat un document, disposes de diferents opcions en els botons superiors.<br><br>" +
                                "A més, tens la opció de llistar per diferents criteris usant el menú 'Llistar per'.<br><br>" +
                                "La llista obtinguda la pots ordenar emprant les capcaleres de la taula." +
                                "</html>");
            }
        });


        // Reset
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean confirm = cvd.askConfirmation(frame, "ATENCIÓ! Estàs a punt d'esborrar tot el contingut del sistema. Aquesta acció és irreversible. Estàs segur que vols fer reset?");
                if (confirm) {
                    CtrlApplication.getInstance().reset();
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
                int indexModel = documents.convertRowIndexToModel(selectedIndex);
                String title = (String) documentsModel.getValueAt(indexModel, 1);
                String author = (String) documentsModel.getValueAt(indexModel, 2);
                Pair<String, String> newIdentifier = cvd.showModify(frame, title, author);
                if (newIdentifier != null) {            // Vol dir que s'ha modificat títol i/o autor
                    String newTitle = newIdentifier.getFirst();
                    String newAuthor = newIdentifier.getSecond();
                    documentsModel.setValueAt(newTitle, indexModel, 1);
                    documentsModel.setValueAt(newAuthor, indexModel, 2);
                }
            }
        });

        similars.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = (String) documents.getValueAt(selectedIndex, 1);
                String author = (String) documents.getValueAt(selectedIndex, 2);
                Object[][] result = cvd.showListKSimilars(frame, title, author);
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
                boolean confirm = cvd.askConfirmation(frame, "Segur/a que vols esborrar el " +
                        "document amb títol '" + title + "' i autor '" + author + "'?");
                if (confirm) {
                    try {
                        cp.deleteDocument(title, author);
                        documentsModel.removeRow(documents.convertRowIndexToModel(selectedIndex));
                    } catch (ExceptionNoDocument ex) {
                        // No és possible
                        cvd.showInternalError(frame);
                    }
                }
            }
        });

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = (String) documents.getValueAt(selectedIndex, 1);
                String author = (String) documents.getValueAt(selectedIndex, 2);
                cvd.showDownloader(frame, title, author);
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
                    String author = (String) documents.getValueAt(row, 2);
                    try {
                        cp.updateFavouriteDocument(title, author, newFav);
                    } catch (ExceptionNoDocument ex) {
                        // No pot ser que no trobem el document
                        cvd.showInternalError(frame);
                    }
                }
            }
        });


        // Doble click per modificar un document
        documents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);
                if (documents.getSelectedColumn() != 0 && event.getClickCount() == 2) modify.doClick();
            }
        });

        // Esborrar documents prement Supr
        panel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete.doClick();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * @brief Mètode per inicialitzar la vista
     * @details Aquest mètode inicialitza de manera bàsica el marc que dona sentit a la vista
     */
    private void initialize_basic(Dimension size) {
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setSize(size);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                CtrlApplication.getInstance().closeApp(frame);
                System.exit(0);
            }
        });
    }

    /**
     * @param location Posició de la pantalla on volem situar la vista
     * @param size     Mida de la vista
     * @brief Mètode per inicialitzar la vista, i fer persistents els canvis fets en tancar l'aplicació
     * @details Amb aquesta funció es mostra la vista principal, i afegeix un listener per fer que quan es tanca la finestra,
     * es facin persistents els canvis fets, i tot seguit s'aturi l'execució de l'aplicació
     */
    public void initialize(Point location, Dimension size) {
        initialize_basic(size);
        frame.setLocation(location);
    }

    /**
     * @param size Mida de la vista
     * @brief Mètode per inicialitzar la vista, i fer persistents els canvis fets en tancar l'aplicació
     * @details Amb aquesta funció es mostra la vista principal, i afegeix un listener per fer que quan es tanca la finestra,
     * es facin persistents els canvis fets, i tot seguit s'aturi l'execució de l'aplicació
     */
    public void initialize(Dimension size) {
        initialize_basic(size);
        frame.setLocationRelativeTo(null);
    }

    /**
     * @param data Matriu de les dades a veure a la taula de la vista
     * @brief Mètode per substituir les dades que es veuen a la taula de la vista per noves dades, i configurar la taula
     * per aconseguir que es renderitzi correctament
     */
    private void updateData(Object[][] data) {
        String[] headers = new String[]{"", "Títol", "Autor"};

        documentsModel.setDataVector(data, headers);

        documents.setDefaultRenderer(Boolean.class, new FavBooleanCellRenderer(frame));
        documents.setDefaultEditor(Boolean.class, new DefaultCellEditor(new FavCheckBox(frame)));

        documents.getTableHeader().setReorderingAllowed(false);
        documents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumn tc = documents.getColumnModel().getColumn(0);
        tc.setMinWidth(25);
        tc.setMaxWidth(25);
    }

    /**
     * @brief Mètode que crea la taula de la vista, la formateja per a que es vegi com desitgem, i la inicialitza amb
     * les dades de tots els documents del sistema
     */
    private void createUIComponents() {
        documentsModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Class getColumnClass(int column) {
                for (int row = 0; row < getRowCount(); row++) {
                    Object o = getValueAt(row, column);

                    if (o != null)
                        return o.getClass();
                }

                return Object.class;
            }
        };
        documents = new JTable(documentsModel);
        updateData(cp.listAllDocuments());

        JTableHeader header = documents.getTableHeader();
        Font font = new Font("Arial", Font.BOLD, 14);
        header.setFont(font);
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
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        similars = new JButton();
        similars.setEnabled(false);
        similars.setText("Llistar similars");
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
        modify.setText("Modificar");
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

    /**
     * \endcond
     */
}
