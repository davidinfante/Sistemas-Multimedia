package SM.dic.graficos;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/**
 * Representa una forma geométrica de tipo arco
 * @author David Infante Casas
 */
public class Arco extends Arc2D.Float implements AtrShape {
    
    /**
     * Nombre de la figura
     */
    private String nombre;
    /**
     * Atributos de la forma
     */
    private Atributos atributos;
    /**
     * Punto de inicio del arco
     */
    private Point2D puntoInicial;
    
    /**
     * Crea un nuevo Arco
     * @param p Punto inicial
     * @param atributos Atributos de la forma
     * @param id Identificador de la forma
     */
    public Arco(Point2D p, Atributos atributos, int id) {
        super(Arc2D.OPEN);
        this.nombre = "Arco - " + id;
        this.puntoInicial = p;
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
     * @param posXIniMover Posición X para que el cursor se encuentre en el centro de la figura al moverla
     * @param posYIniMover Posición Y para que el cursor se encuentre en el centro de la figura al moverla
     */
    public void setLocation(Point2D p, int posXIniMover, int posYIniMover) {
        super.setFrame((int)posXIniMover + (int) p.getX(), (int)posYIniMover + (int) p.getY(), getWidth(), getHeight());
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
     * Devuelve el punto inicial del arco
     * @return Devuelve el punto inicial del arco
     */
    public Point2D getPuntoInicial() {
        return puntoInicial;
    }
    
    /**
     * Dibujo de la forma mediante Graphics2D
     * @param g2d Graphics2D con los que dibujar
     * @param figuraSelec Figura seleccionada en el lienzo
     * @param modoEditar Modo de Edutar del lienzo
     */
    @Override
    public void dibujar(Graphics2D g2d, AtrShape figuraSelec, ModoEditar modoEditar) {
        atributos.atributosDibujo(g2d, this, figuraSelec, modoEditar);
        g2d.draw(this);
    }
    
}
