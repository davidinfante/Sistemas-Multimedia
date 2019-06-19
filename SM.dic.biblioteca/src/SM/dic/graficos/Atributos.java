package SM.dic.graficos;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * Lista de atributos que contiene una forma
 * @author David Infante Casas
 */
public class Atributos {
    
    /**
     * Trazo de la forma
     */
    private BasicStroke trazo;
    /**
     * Tipo de trazo de la forma
     */
    private boolean trazoDiscontinuo;
    /**
     * Tramaño de trazo de la forma
     */
    private int tamTrazo;
    /**
     * Color del trazo de la forma
     */
    private Color colorTrazo;
    /**
     * Tipo de relleno de la forma
     */
    private TipoRelleno tipoRelleno;
    /**
     * Color de relleno de la forma
     */
    private Color colorRelleno;
    /**
     * Color de degradado de la forma
     */
    private Color colorDegradado;
    /**
     * Dirección de degradado de la forma
     */
    private boolean direccion; //true vertical false horizontal
    /**
     * Tipo alisado de la forma
     */
    private boolean alisar;
    /**
     * Transparencia de la forma
     */
    private float transparencia; //1 no transparencia
    /**
     * Tipo de fuente de la forma
     */
    private String tipoFuente;
    /**
     * Fuente de la forma
     */
    private Font fuente;
    /**
     * Tamaño de la fuente de la forma
     */
    private int tamFuente;
    /**
     * Texto de la forma
     */
    private String texto;
    /**
     * Render de la forma
     */
    private RenderingHints render;
    
    /**
     * Crea un nuevo Atributos
     */
    public Atributos() {
        trazoDiscontinuo = false;
        tamTrazo = 1;
        colorTrazo = Color.BLACK;
        trazo = new BasicStroke(tamTrazo);
        
        tipoRelleno = TipoRelleno.NO_RELLENAR;
        colorRelleno = Color.BLACK;
        colorDegradado = Color.BLACK;
        direccion = true;
        
        alisar = false;
        
        transparencia = 1;
        
        tipoFuente = "Arial";
        tamFuente = 20;
        fuente = new Font(tipoFuente, Font.BOLD, tamFuente);
        texto = "Hola";
    }
    
    /**
     * Asigna valores a los atributos a partir de otros atributos
     * @param atributos Atributos cuyos valores se quieren asignar
     */
    public void newAtributos(Atributos atributos) {
        this.trazoDiscontinuo = atributos.isTrazoDiscontinuo();
        this.tamTrazo = atributos.getTamTrazo();
        this.colorTrazo = atributos.getColorTrazo();
        this.trazo = new BasicStroke(this.tamTrazo);
        
        this.tipoRelleno = atributos.getTipoRelleno();
        this.colorRelleno = atributos.getColorRelleno();
        this.colorDegradado = atributos.getColorDegradado();
        this.direccion = atributos.isDireccion();
        
        this.alisar = atributos.isAlisar();
        
        this.transparencia = atributos.getTransparencia();
        
        this.tipoFuente = atributos.getTipoFuente();
        this.tamFuente = atributos.getTamFuente();
        this.fuente = new Font(this.tipoFuente, Font.BOLD, tamFuente);
        this.texto = atributos.getTexto();
    }
    
    /**
     * Asignación de parámetros de dibujo sobre la forma
     * @param g2d Graphics2D con los que dibujar
     * @param s La figura que se quiere dibujar
     * @param figuraSelec Figura seleccionada en el lienzo
     * @param modoEditar Modo editar del lienzo
     */
    public void atributosDibujo(Graphics2D g2d, AtrShape s, AtrShape figuraSelec, ModoEditar modoEditar) {
        //Marco de selección
        if (s == figuraSelec && modoEditar == ModoEditar.SELECCIONAR) {
            final float dash[] = {15.0f, 15.0f};
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));
            g2d.setColor(Color.BLACK);
            g2d.draw(new Rectangle((int) s.getX()-5, (int) s.getY()-5, (int) s.getWidth()+10, (int) s.getHeight()+10));
        }
        
        //Transparencia
        if (s.getAtributos().getTransparencia() <= 1.0f) {
            Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, s.getAtributos().getTransparencia());
            g2d.setComposite(c);
        }
        
        //Antialiasing
        if (s.getAtributos().isAlisar()) {
            render = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(render);
            render.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        }
        
        //Relleno
        if (s.getAtributos().getTipoRelleno() == TipoRelleno.COLOR_LISO) {
            g2d.setColor(s.getAtributos().getColorRelleno());
            g2d.fill(s);
        } else if (s.getAtributos().getTipoRelleno() == TipoRelleno.DEGRADADO) {
            Paint relleno;
            if (s.getAtributos().isDireccion()) {//Vertical
                relleno = new GradientPaint(new Point(0, (int) s.getBounds2D().getMinX()), s.getAtributos().getColorRelleno(), new Point(0, (int) s.getBounds2D().getMaxX()), s.getAtributos().getColorDegradado());
            } else {//Horizontal
                relleno = new GradientPaint(new Point((int) s.getBounds2D().getMinX(), 0), s.getAtributos().getColorRelleno(), new Point((int) s.getBounds2D().getMaxX(), 0), s.getAtributos().getColorDegradado());
            }
            
            g2d.setPaint(relleno);
            g2d.fill(s);
        }
        
        //Color trazo
        g2d.setColor(s.getAtributos().getColorTrazo());
        
        //Trazo
        if (s.getAtributos().isTrazoDiscontinuo()) {
            final float dash[] = {15.0f, 15.0f};
            g2d.setStroke(new BasicStroke(s.getAtributos().getTamTrazo(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));
        } else {
            g2d.setStroke(new BasicStroke(s.getAtributos().getTamTrazo()));
        }
        
    }
    
    /**
     * Devuelve el trazo
     * @return Devuelve el trazo
     */
    public BasicStroke getTrazo() {
        return trazo;
    }

    /**
     * Devuelve el color del trazo
     * @return Devuelve el color del trazo
     */
    public Color getColorTrazo() {
        return colorTrazo;
    }

    /**
     * Devuelve el tipo de relleno
     * @return Devuelve el tipo de relleno
     */
    public TipoRelleno getTipoRelleno() {
        return tipoRelleno;
    }

    /**
     * Devuelve el color de relleno
     * @return Devuelve el color de relleno
     */
    public Color getColorRelleno() {
        return colorRelleno;
    }

    /**
     * Devuelve el color de degradado
     * @return Devuelve el color de degradado
     */
    public Color getColorDegradado() {
        return colorDegradado;
    }

    /**
     * Devuelve la dirección del degradado
     * @return Devuelve la dirección del degradado
     */
    public boolean isDireccion() {
        return direccion;
    }

    /**
     * Devuelve el tipo de alisado
     * @return Devuelve el tipo de alisado
     */
    public boolean isAlisar() {
        return alisar;
    }

    /**
     * Devuelve el valor de la transparencia
     * @return Devuelve el valor de la transparencia
     */
    public float getTransparencia() {
        return transparencia;
    }

    /**
     * Devuelve el tipo de fuente
     * @return Devuelve el tipo de fuente
     */
    public Font getFuente() {
        return fuente;
    }

    /**
     * Devuelve el tamaño de la fuente
     * @return Devuelve el tamaño de la fuente
     */
    public int getTamFuente() {
        return tamFuente;
    }

    /**
     * Devuelve el tamaño del trazo
     * @return Devuelve el tamaño del trazo
     */
    public int getTamTrazo() {
        return tamTrazo;
    }

    /**
     * Devuelve el tipo de la fuente
     * @return Devuelve el tipo de la fuente
     */
    public String getTipoFuente() {
        return tipoFuente;
    }

    /**
     * Asigna el tamaño del trazo
     * @param tamTrazo Tamaño del trazo a asignar
     */
    public void setTamTrazo(int tamTrazo) {
        this.tamTrazo = tamTrazo;
    }

    /**
     * Asigna el tipo de fuente
     * @param tipoFuente Tipo de fuente a asignar
     */
    public void setTipoFuente(String tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    /**
     * Asigna el tipo de trazo
     * @param trazo Tipo de trazo a asignar
     */
    public void setTrazo(BasicStroke trazo) {
        this.trazo = trazo;
    }

    /**
     * Asigna el color de trazo
     * @param colorTrazo Color de trazo a asignar
     */
    public void setColorTrazo(Color colorTrazo) {
        this.colorTrazo = colorTrazo;
    }

    /**
     * Asigna el tipo de relleno
     * @param tipoRelleno Tipo de relleno a asignar
     */
    public void setTipoRelleno(TipoRelleno tipoRelleno) {
        this.tipoRelleno = tipoRelleno;
    }

    /**
     * Asigna el color de relleno
     * @param colorRelleno Color de relleno a asignar
     */
    public void setColorRelleno(Color colorRelleno) {
        this.colorRelleno = colorRelleno;
    }

    /**
     * Asigna el color de degradado
     * @param degradado Color de degradado a asignar
     */
    public void setColorDegradado(Color degradado) {
        this.colorDegradado = degradado;
    }

    /**
     * Asigna la dirección de degradado
     * @param direccion Dirección de degradado a asignar
     */
    public void setDireccion(boolean direccion) {
        this.direccion = direccion;
    }

    /**
     * Asigna el tipo de alisado
     * @param alisar Tipo de alisado a asignar
     */
    public void setAlisar(boolean alisar) {
        this.alisar = alisar;
    }

    /**
     * Asigna la transparencia
     * @param transparencia Transparencia a asignar
     */
    public void setTransparencia(float transparencia) {
        this.transparencia = transparencia;
    }

    /**
     * Asigna el tipo de fuente
     * @param fuente Tipo de fuente a asignar
     */
    public void setFuente(Font fuente) {
        this.fuente = fuente;
    }

    /**
     * Asigna el tamaño de la fuente
     * @param tamFuente Tamaño de la fuente a asignar
     */
    public void setTamFuente(int tamFuente) {
        this.tamFuente = tamFuente;
    }

    /**
     * Devuelve si el trazo es discontinuo
     * @return Devuelve si el trazo es discontinuo
     */
    public boolean isTrazoDiscontinuo() {
        return trazoDiscontinuo;
    }

    /**
     * Asigna si el trazo es discontinuo
     * @param trazoDiscontinuo Trazo discontinuo a asignar
     */
    public void setTrazoDiscontinuo(boolean trazoDiscontinuo) {
        this.trazoDiscontinuo = trazoDiscontinuo;
    }

    /**
     * Asigna el texto
     * @param texto Texto a asignar
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Devuelve el texto
     * @return Devuelve el texto
     */
    public String getTexto() {
        return texto;
    }
    
}
