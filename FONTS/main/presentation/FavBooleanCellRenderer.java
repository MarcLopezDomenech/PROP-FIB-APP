package main.presentation;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author marc.valls.camps
 * @class FavBooleanCellRenderer
 * @brief Herència de la classe FavCheckBox, que implementa la interfície de SWING TableCellRenderer, de manera
 * que es pugui renderitzar FavCheckBox en una JTable en camps booleans
 */
public class FavBooleanCellRenderer extends FavCheckBox implements TableCellRenderer {
    /**
     * @brief Constructora que únicament crida a la constructora de la superclasse
     * @param reference Frame respecte del qual es posicionarà el diàleg d'error si falla la constructora de la superclasse
     */
    public FavBooleanCellRenderer(JFrame reference) {
        super(reference);
    }

    /**
     * @brief Implementació del mètode de renderitzat, usat per una JTable quan vol dibuixar una certa cel·la
     * @param table Taula que usa aquest mètode per pintar la cel·la
     * @param value Valor de la cel·la, un booleà si aquesta classe s'aplica com cal
     * @param isSelected Booleà que indica si la fila de la cel·la està seleccionada a la taula
     * @param hasFocus Booleà que indica si la cel·la té el focus
     * @param row La fila corresponent de la cel·la, o bé -1 si es tracta d'una cel·la de la capçalera
     * @param column La columna corresponent a la cel·la
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Boolean) setSelected((boolean) value);
        return this;
    }
}