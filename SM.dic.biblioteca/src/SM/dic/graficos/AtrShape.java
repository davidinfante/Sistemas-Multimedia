package SM.dic.graficos;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 * Interfaz que representa una forma geométrica
 * @author David Infante Casas
 */
public interface AtrShape extends Shape {
    
    /**
     * Devuelve el nombre de la figura
     * @return Devuelve el nombre de la figura
     */
    public String getNombre();
    
    /**
     * Devuelve los atributos de la forma
     * @return Devuelve los atributos de la forma
     */
    public Atributos getAtributos();

    /**
     * Asigna unos atributos a la forma
     * @param atr Atributos a asignar
     */
    public void setAtributos(Atributos atr);

    /**
     * Devuelve el ancho de la forma
     * @return Devuelve el ancho de la figura
     */
    public double getWidth();

    /**
     * Devuelve el alto de la forma
     * @return Devuelve el alto de la figura
     */
    public double getHeight();

    /**
     * Devuelve la posición X de la forma
     * @return Devuelve la posición X de la forma
     */
    public double getX();

    /**
     * Devuelve la posición Y de la forma
     * @return Devuelve la posición Y de la forma
     */
    public double getY();

    /**
     * Ubica la forma en un nuevo lugar 
     * @param p Punto de la ubicación
     */
    public void setLocation(Point2D p);

    /**
     * Dibujo de la forma mediante Graphics2D
     * @param g2d Graphics2D con los que dibujar
     * @param figuraSelec Figura seleccionada en el lienzo
     * @param modoEditar Modo de Edutar del lienzo
     */
    public void dibujar(Graphics2D g2d,AtrShape figuraSelec, ModoEditar modoEditar);
    
}
