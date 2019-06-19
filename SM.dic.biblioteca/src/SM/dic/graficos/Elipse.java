package SM.dic.graficos;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Representa una forma geométrica de tipo elipse
 * @author David Infante Casas
 */
public class Elipse extends Ellipse2D.Float implements AtrShape {
    
    /**
     * Nombre de la figura
     */
    private String nombre;
    /**
     * Atributos de la forma
     */
    private Atributos atributos;
    
    /**
     * Crea una nueva Elipse
     * @param p Punto de creación
     * @param atributos Atributos de la forma
     * @param id Identificador de la forma
     */
    public Elipse(Point2D p, Atributos atributos, int id) {
        super((int) p.getX(), (int) p.getY(), 1, 1);
        this.nombre = "Elipse - " + id;
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
     * Ubica la forma en un nuevo lugar 
     * @param p Punto de la ubicación
     */
    @Override
    public void setLocation(Point2D p) {
        super.setFrame(p.getX(), p.getY(), getWidth(), getHeight());
    }
    
    /**
     * Ubica la forma en un nuevo lugar 
     * @param p Punto de la ubicación
     * @param posXIniMover Posición X para que el cursor se encuentre en el centro de la figura al moverla
     * @param posYIniMover Posición Y para que el cursor se encuentre en el centro de la figura al moverla
     */
    public void setLocation(Point2D p, int posXIniMover, int posYIniMover) {
        super.setFrame(posXIniMover + (int) p.getX(), posYIniMover + (int) p.getY(), getWidth(), getHeight());
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
