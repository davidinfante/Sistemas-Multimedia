package SM.dic.graficos;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

/**
 * Representa una forma geométrica de tipo quad con un puntos de control
 * @author David Infante Casas
 */
public class Quad extends QuadCurve2D.Float implements AtrShape {
    
    /**
     * Nombre de la figura
     */
    private String nombre;
    /**
     * Atributos de la forma
     */
    private Atributos atributos;
    /**
     * Estado en el que se encuentra la definición de los puntos de control de la forma
     */
    private EstadoCurva estado;
    
    /**
     * Crea un nuevo Quad
     * @param atributos Atributos de la forma
     * @param id Identificador de la forma
     */
    public Quad(Atributos atributos, int id) {
        super();
        this.nombre = "Quad - " + id;
        this.atributos = new Atributos();
        this.atributos.newAtributos(atributos);
        this.estado = EstadoCurva.CREAR;
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
        return this.getBounds2D().getWidth();
    }
    
    /**
     * Devuelve el alto de la forma
     * @return Devuelve el alto de la figura
     */
    @Override
    public double getHeight() {
        return this.getBounds2D().getHeight();
    }
    
    /**
     * Devuelve la posición X de la forma
     * @return Devuelve la posición X de la forma
     */
    @Override
    public double getX() {
        return this.getBounds2D().getX();
    }
    
    /**
     * Devuelve la posición Y de la forma
     * @return Devuelve la posición Y de la forma
     */
    @Override
    public double getY() {
        return this.getBounds2D().getY();
    }
    
    /**
     * Ubica la forma en un nuevo lugar 
     * @param p Punto de la ubicación
     */
    @Override
    public void setLocation(Point2D p) {
        int x = (int) (p.getX() - getX1());
        int y = (int) (p.getY() - getY1());
        
        super.x1 += x;
        super.y1 += y;
        super.x2 += x;
        super.y2 += y;
        super.ctrlx += x;
        super.ctrly += y;
    }

    /**
     * Define la posición del punto de control
     * @param p Posición del punto de control
     */
    public void setQuadPoint(Point2D p) {
        this.ctrlx = (float) p.getX();
        this.ctrly = (float) p.getY();
    }

    /**
     * Define la posición del punto inicial
     * @param p Posición del punto inicial
     */
    public void setPuntoInicial(Point2D p) {
        this.x1 = (float) p.getX();
        this.y1 = (float) p.getY();
    }
    
    /**
     * Define la posición del punto final
     * @param p Posición del punto final
     */
    public void setPuntoFinal(Point2D p) {
        this.x2 = (float) p.getX();
        this.y2 = (float) p.getY();
    }
    
    /**
     * Devuelve el estado de definición de los puntos de control de la forma
     * @return Devuelve el estado de definición de los puntos de control de la forma
     */
    public EstadoCurva getEstado() {
        return estado;
    }

    /**
     * Asigna el estado de definición de los puntos de control de la forma
     * @param estado Estado de definición de los puntos de control de la forma a asignar
     */
    public void setEstado(EstadoCurva estado) {
        this.estado = estado;
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
