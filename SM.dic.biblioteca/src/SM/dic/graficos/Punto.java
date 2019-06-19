package SM.dic.graficos;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Representa una forma geométrica de tipo punto
 * @author David Infante Casas
 */
public class Punto extends java.awt.geom.Line2D.Float implements AtrShape {
    
    /**
     * Nombre de la figura
     */
    private String nombre;
    /**
     * Atributos de la forma
     */
    private Atributos atributos;
    /**
     * Localización del punto
     */
    private Point2D punto;
    
    /**
     * Crea un nuevo Punto
     * @param p Localización del punto
     * @param atributos Atributos de la forma
     * @param id Identificador de la forma
     */
    public Punto(Point2D p, Atributos atributos, int id) {
        super((int) p.getX(), (int) p.getY(), (int) p.getX(), (int) p.getY());
        this.nombre = "Punto - " + id;
        punto = p;
        this.atributos = new Atributos();
        this.atributos.newAtributos(atributos);
    }
    
    /**
     * Devuelve los atributos de la forma
     * @return Devuelve los atributos de la forma
     */
    @Override
    public Atributos getAtributos() {
        return this.atributos;
    }
    
    /**
     * Devuelve el nombre de la figura
     * @return Devuelve el nombre de la figura
     */
    @Override
    public String getNombre() {
        return this.nombre;
    }
    
    /**
     * Asigna unos atributos a la forma
     * @param atributos Atributos a asignar
     */
    @Override
    public void setAtributos(Atributos atributos) {
        this.atributos.newAtributos(atributos);
    }
    
    /**
     * Devuelve el ancho de la forma
     * @return Devuelve el ancho de la figura
     */
    @Override
    public double getWidth() {
        return 5;
    }
    
    /**
     * Devuelve el alto de la forma
     * @return Devuelve el alto de la figura
     */
    @Override
    public double getHeight() {
        return 5;
    }
    
    /**
     * Devuelve la posición X de la forma
     * @return Devuelve la posición X de la forma
     */
    @Override
    public double getX() {
        return punto.getX();
    }
    
    /**
     * Devuelve la posición Y de la forma
     * @return Devuelve la posición Y de la forma
     */
    @Override
    public double getY() {
        return punto.getY();
    }
    
    /**
     * Devuelve true si el Punto p está cerca de la posición de la forma
     * @param p Punto a comprobar
     * @return True si está cerca, false en otro caso
     */
    public boolean isNear(Point2D p) {
        return punto.distance(p) <= 4.0;
    }
    
    /**
     * Devuelve true si el Punto p está cerca de la posición de la forma
     * @param p Punto a comprobar
     * @return True si está cerca, false en otro caso
     */
    @Override
    public boolean contains(Point2D p) {
        return isNear(p);
    }
    
    /**
     * Ubica la forma en un nuevo lugar 
     * @param p Punto de la ubicación
     */
    @Override
    public void setLocation(Point2D p) {
        double dx = p.getX() - this.getX1();
        double dy = p.getY() - this.getY1();
        Point2D newp2 = new Point2D.Double(this.getX2() + dx, this.getY2() + dy);
        this.setLine(p, newp2);
        punto = p;
    }
    
    /**
     * Dibujo de la forma mediante Graphics2D
     * @param g2d Graphics2D con los que dibujar
     * @param figuraSelec Figura seleccionada en el lienzo
     * @param modoEditar Modo de Edutar del lienzo
     */
    @Override
    public void dibujar(Graphics2D g2d,AtrShape figuraSelec, ModoEditar modoEditar) {
        atributos.atributosDibujo(g2d, this, figuraSelec, modoEditar);
        g2d.draw(this);
    }
}
