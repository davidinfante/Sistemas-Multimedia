package SM.dic.graficos;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Representa una forma geométrica de tipo trazo libre
 * @author David Infante Casas
 */
public class TrazoLibre extends Path2D.Float implements AtrShape {
    
    /**
     * Nombre de la figura
     */
    private String nombre;
    /**
     * Atributos de la forma
     */
    private Atributos atributos;
    /**
     * Lista de puntos que forman el trazo libre
     */
    private ArrayList<Point> puntos = new ArrayList<>();
    
    /**
     * Crea un nuevo TrazoLibre
     * @param p Punto de inicio
     * @param atributos Atributos de la forma
     * @param id Identificador de la forma
     */
    public TrazoLibre(Point2D p, Atributos atributos, int id) {
        super(Path2D.WIND_EVEN_ODD);
        this.nombre = "Trazo Libre - " + id;
        moveTo(p.getX(), p.getY());
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
     * Añade un nuevo punto a la lista de puntos del trazo libre
     * @param x Coordenada X del punto
     * @param y Coordenada Y del punto
     */
    public void addPunto(int x, int y) {
        puntos.add(new Point(x, y));
        super.lineTo(x, y);
    }
    
    /**
     * Ubica la forma en un nuevo lugar 
     * @param p Punto de la ubicación
     */
    @Override
    public void setLocation(Point2D p) {
        super.reset();
        
        if (puntos.size() > 0) {
            int x = (int) (p.getX() - puntos.get(0).x);
            int y = (int) (p.getY() - puntos.get(0).y);
            
            for (Point ps: puntos) {
                ps.translate(x, y);
                if (ps == puntos.get(0)) super.moveTo(ps.x, ps.y);
                else super.lineTo(ps.x, ps.y);
            }
        }
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
