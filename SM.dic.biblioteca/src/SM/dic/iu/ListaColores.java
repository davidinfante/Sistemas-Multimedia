package SM.dic.iu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * Lista de elementos a partir de Color
 * @author David Infante Casas
 */
public class ListaColores extends JPanel implements ListCellRenderer<Color> {
    /**
     * Botón asociado a un color 
     */
    private JButton boton;
    
    /**
     * Crea una nueva ListaColores
     */
    public ListaColores() {
        boton = new JButton();
        boton.setPreferredSize(new Dimension(24, 24));
        add(boton);
    }

    /**
     * Crea el component de la lista para cada elemento
     * @param list Lista de colores
     * @param value Valor del color
     * @param index Índice del color en la lista
     * @param isSelected Boolean si está seleccionado
     * @param cellHasFocus Si tiene focus un elemento
     * @return Devuelve la lista de colores
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Color> list, Color value, int index, boolean isSelected, boolean cellHasFocus) {
        boton.setBackground(value);
        return this;
    }
}
