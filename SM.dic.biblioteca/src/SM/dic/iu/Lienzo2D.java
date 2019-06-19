package SM.dic.iu;

import SM.dic.graficos.Arco;
import SM.dic.graficos.AtrShape;
import SM.dic.graficos.Atributos;
import SM.dic.graficos.Cubic;
import SM.dic.graficos.Elipse;
import SM.dic.graficos.EstadoCurva;
import SM.dic.graficos.Linea;
import SM.dic.graficos.ModoEditar;
import SM.dic.graficos.Punto;
import SM.dic.graficos.Quad;
import SM.dic.graficos.Rectangulo;
import SM.dic.graficos.RectanguloRedondeado;
import SM.dic.graficos.TipoDibujo;
import SM.dic.graficos.TrazoLibre;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Lienzo sobre el que dibujar formas geométricas AtrShape
 * @author David Infante Casas
 */
public class Lienzo2D extends javax.swing.JPanel {
    /**
     * Identificador de cada figura
     */
    private static int idFigura = 0;
    /**
     * Atributos del liezo que se aplican a la siguiente forma dibujada
     */
    private Atributos atributos;
    /**
     * Forma Punto
     */
    private Punto punto;
    /**
     * Forma Línea
     */
    private Linea linea;
    /**
     * Forma Rectángulo
     */
    private Rectangulo rectangulo;
    /**
     * Forma Elipse
     */
    private Elipse elipse;
    /**
     * Forma RectánguloRedondeado
     */
    private RectanguloRedondeado recRedondeado;
    /**
     * Forma Arco
     */
    private Arco arco;
    /**
     * Forma Quad
     */
    private Quad quad;
    /**
     * Forma Cubic
     */
    private Cubic cubic;
    /**
     * Forma TrazoLibre
     */
    private TrazoLibre trazoLibre;
    /**
     * Punto auxiliar para tomar puntos de inicio al dibujar formas o editarlas
     */
    private Point pAux;
    /**
     * Lista de formas que contiene el lienzo
     */
    private List<AtrShape> vShape = new ArrayList();
    /**
     * Buffer de formas para poder restaurar formas borradas
     */
    private List<AtrShape> bufferRehacer = new ArrayList();
    /**
     * Forma seleccionada
     */
    private AtrShape formaSeleccionada;
    /**
     * Cursor
     */
    private Cursor cursor;
    /**
     * Posición X para que el cursor se encuentre en el centro de la figura al moverla
     */
    private int posXIniMover;
    /**
     * Posición Y para que el cursor se encuentre en el centro de la figura al moverla
     */
    private int posYIniMover;
    /**
     * Índice de la fuente seleccionada en la VentanaPrincipal
     */
    private int indice_fuente;
    /**
     * Índice del tipo de relleno seleccionado en la VentanaPrincipal
     */
    private int indice_tipoRelleno;
    /**
     * ModoEditar que tiene el lienzo
     */
    private ModoEditar modoEditar;
    /**
     * TipoDibujo que tiene el lienzo
     */
    private TipoDibujo tipoDibujo;
    /**
     * ClipArea del lienzo
     */
    private Rectangle clipArea;
    /**
     * Borde del lienzo
     */
    private Rectangle bordeLienzo;
    
    /**
     * Crea un nuevo Lienzo
     */
    public Lienzo2D() {
        super();
        initComponents();
        this.setBackground(Color.WHITE);
        
        this.atributos = new Atributos();

        indice_fuente = 0;
        indice_tipoRelleno = 0;
        
        modoEditar = ModoEditar.NULL;
        tipoDibujo = TipoDibujo.PUNTO;
        clipArea = new Rectangle();
        bordeLienzo = new Rectangle();
        
        formaSeleccionada = null;
    }

    // Getters y Setters
    
    /**
     * Asigna unos atributos a los del lienzo
     * @param atributos Atributos a asignar
     */
    
    public void setAtributos(Atributos atributos) {
        this.atributos.newAtributos(atributos);
    }
    
    /**
     * Devuelve los atributos del lienzo
     * @return Devuelve los atributos del lienzo
     */
    public Atributos getAtributos() {
        return atributos;
    }

    /**
     * Asigna un tipo de dibujo al lienzo
     * @param tipoDibujo Tipo de dibujo a asignar
     */
    public void setTipoDibujo(TipoDibujo tipoDibujo) {
        this.tipoDibujo = tipoDibujo;
        if (this.tipoDibujo != TipoDibujo.NULL) modoEditar = ModoEditar.NULL;
        this.formaSeleccionada = null;
        this.repaint();
    }

    /**
     * Devuelve el tipo de dibujo
     * @return Devuelve el tipo de dibujo
     */
    public TipoDibujo getTipoDibujo() {
        return tipoDibujo;
    }

    /**
     * Devuelve el modo editar del lienzo
     * @return Devuelve el modo editar del lienzo
     */
    public ModoEditar getModoEditar() {
        return modoEditar;
    }

    /**
     * Asigna un modo editar al lienzo
     * @param modoEditar Modo editar a asignar
     */
    public void setModoEditar(ModoEditar modoEditar) {
        this.modoEditar = modoEditar;
        if (this.modoEditar != ModoEditar.NULL) tipoDibujo = TipoDibujo.NULL;
        this.repaint();
    }

    /**
     * Devuelve el índice de la fuente seleccionada
     * @return Devuelve el índice de la fuente seleccionada
     */
    public int getIndice_fuente() {
        return indice_fuente;
    }
    
    /**
     * Devuelve el índice del tipo de relleno seleccionado
     * @return Devuelve el índice del tipo de relleno seleccionado
     */
    public int getIndice_tipoRelleno() {
        return indice_tipoRelleno;
    }
    
    /**
     * Asigna el índice del tipo de relleno seleccionado
     * @param indice_tipoRelleno Índice del tipo de relleno a asignar
     */
    public void setIndice_TipoRelleno(int indice_tipoRelleno) {
        this.indice_tipoRelleno = indice_tipoRelleno;
    }

    /**
     * Devuelve la lista de formas que están dibujadas en el lienzo
     * @return Devuelve la lista de formas que están dibujadas en el lienzo
     */
    public List<AtrShape> getvShape() {
        return vShape;
    }
    
    /**
     * Devuelve el nomvre la figura en la posición i del lienzo
     * @param i Posición de la figura cuyo nombre se quiere obtener
     * @return Devuelve el nomvre la figura en la posición i del lienzo
     */
    public String getNombreFigura(int i) {
        return vShape.get(i).getNombre();
    }

    /**
     * Devuelve la forma seleccionada
     * @return Devuelve la forma seleccionada
     */
    public AtrShape getFormaSeleccionada() {
        return formaSeleccionada;
    }

    /**
     * Asigna una forma a la forma seleccionada
     * @param formaSeleccionada Forma seleccionada a asignar
     */
    public void setFormaSeleccionada(AtrShape formaSeleccionada) {
        this.formaSeleccionada = formaSeleccionada;
    }
    
    /**
     * Devuelve una figura identificada por su nombre
     * @param nombre Nombre de la figura que se quiere obtener
     * @return Devuelve la figura identificada por su nombre
     */
    public AtrShape getFormaPorNombre(String nombre) {
        if (vShape.size() > 0) {
            for (int i = 0; i < vShape.size(); ++i) {
                if (vShape.get(i).getNombre().equals(nombre)) return vShape.get(i);
            }
        }
        return null;
    }
    
    /**
     * Dibuja todas las figuras del lienzo
     * @param g Graphics con los que dibujar
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        
        //Borde lienzo
        g2d.clip(clipArea);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.BLACK);
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        g2d.setComposite(c);
        g2d.draw(bordeLienzo);
        
        for (AtrShape s: vShape) {
            s.dibujar(g2d, formaSeleccionada, modoEditar);
        }
    }
    
    /**
     * Devuelve la forma que contiene el punto p
     * @param p Punto en el que buscar la forma
     * @return Devuelve la forma que contiene el punto p si la hay
     */
    private AtrShape getSelectedShape(Point2D p) {
        for (AtrShape s: vShape)
            if (s.contains(p)) return s;
        return null;
    }
    
    /**
     * Asigna la fuente de texto
     * @param indice_fuente Índice de la nueva fuente a asignar
     */
    public void cambiarFuente(int indice_fuente) {
        this.atributos.setFuente(new Font(atributos.getTipoFuente(), Font.BOLD, atributos.getTamFuente()));
        this.indice_fuente = indice_fuente;
        this.repaint();
    }

    /**
     * Asigna un vector de formas al lienzo
     * @param vShape Vector de formas a asignar
     */
    public void setvShape(List<AtrShape> vShape) {
        this.vShape = vShape;
    }
    
    /**
     * Establece una nueva ClipArea
     * @param w Ancho del ClipArea
     * @param h Alto del ClipArea
     */
    public void newClipArea(int w, int h) {
        clipArea = new Rectangle(w+1, h+1);
    }
    
    /**
     * Establece el borce del lienzo
     * @param w Ancho del lienzo
     * @param h Alto del lienzo
     */
    public void setBordeLienzo(int w, int h) {
        this.bordeLienzo = new Rectangle(w, h);
    }
    
    /**
     * Aplica los atributos del lienzo a la formaSeleccionada
     */
    public void aplicarAtributos() {
        if (formaSeleccionada != null) formaSeleccionada.setAtributos(this.atributos);
        this.repaint();
    }
    
    /**
     * Crea una nueva forma geométrica y la añade al lienzo
     */
    public void crearForma() {
        cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        this.setCursor(cursor);
        
        if (modoEditar == ModoEditar.NULL) {
            switch (tipoDibujo) {
                case PUNTO:
                    punto = new Punto(pAux, this.atributos, idFigura);
                    vShape.add(punto);
                    idFigura += 1;
                break;
                case LINEA:
                    linea = new Linea(this.atributos, idFigura);
                    vShape.add(linea);
                    idFigura += 1;
                break;
                case RECTANGULO:
                    rectangulo = new Rectangulo(pAux, this.atributos, idFigura);
                    vShape.add(rectangulo);
                    idFigura += 1;
                break;
                case ELIPSE:
                    elipse = new Elipse(pAux, this.atributos, idFigura);
                    vShape.add(elipse);
                    idFigura += 1;
                break;
                case REC_REDONDEADO:
                    recRedondeado = new RectanguloRedondeado(pAux, this.atributos, idFigura);
                    vShape.add(recRedondeado);
                    idFigura += 1;
                break;
                case ARCO:
                    arco = new Arco(pAux, this.atributos, idFigura);
                    vShape.add(arco);
                    idFigura += 1;
                break;
                case QUAD:
                    if (quad == null) {
                        quad = new Quad(this.atributos, idFigura);
                        vShape.add(quad);
                        idFigura += 1;
                    } else if (quad.getEstado() == EstadoCurva.CREAR) {
                        quad = new Quad(this.atributos, idFigura);
                        vShape.add(quad);
                        idFigura += 1;
                    }
                break;
                case CUBIC:
                    if (cubic == null) {
                        cubic = new Cubic(this.atributos, idFigura);
                        vShape.add(cubic);
                        idFigura += 1;
                    } else if (cubic.getEstado() == EstadoCurva.CREAR) {
                        cubic = new Cubic(this.atributos, idFigura);
                        vShape.add(cubic);
                        idFigura += 1;
                    }
                break;
                case TRAZO_LIBRE:
                    trazoLibre = new TrazoLibre(pAux, this.atributos, idFigura);
                    vShape.add(trazoLibre);
                    idFigura += 1;
                break;
            }
        }
    }
    
    /**
     * Actualiza una forma en el caso de que sea necesario modificar su dimensión o puntos de control
     * @param p Punto de actulalización
     */
    public void actualizarForma(Point p) {
        cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        this.setCursor(cursor);
        if (modoEditar == ModoEditar.NULL) {
            switch (tipoDibujo) {
                case LINEA: linea.setLine(pAux, p); break;
                case RECTANGULO: rectangulo.setFrameFromDiagonal(pAux, p); break;
                case ELIPSE: elipse.setFrameFromDiagonal(pAux, p); break;
                case REC_REDONDEADO: recRedondeado.setFrameFromDiagonal(pAux, p); break;
                case ARCO:
                    arco.setFrameFromDiagonal(arco.getPuntoInicial(), p);
                    arco.setAngleExtent((double) p.getX()%360);
                break;
                case QUAD:
                    switch (quad.getEstado()) {
                        case CREAR:
                            quad.setPuntoInicial(pAux);
                            quad.setPuntoFinal(p);
                        break;
                        case PUNTO1:
                            quad.setQuadPoint(p);
                        break;
                    }
                break;
                case CUBIC:
                    switch (cubic.getEstado()) {
                        case CREAR:
                            cubic.setPuntoInicial(pAux);
                            cubic.setPuntoFinal(p);
                        break;
                        case PUNTO1:
                            cubic.setCubicPoint1(p);
                        break;
                        case PUNTO2:
                            cubic.setCubicPoint2(p);
                        break;
                    }
                break;
                case TRAZO_LIBRE: trazoLibre.addPunto(p.x, p.y); break;
            }
        }
    }
    
    /**
     * Borra la formaSeleccionada
     */
    public void borrarForma() {
        vShape.remove(formaSeleccionada);
    }
    
    /**
     * Borra la última forma dibujada
     */
    public void borrarUltimaForma() {
        if (vShape.size() > 0) {
            vShape.remove(vShape.size()-1);
            this.repaint();
        }
    }
    
    /**
     * Actualiza el estado de definición de los puntos de control de las formas que los tengan
     */
    public void estadoCurvas() {
        if (quad != null && tipoDibujo == TipoDibujo.QUAD) {
            if (quad.getEstado() == EstadoCurva.CREAR) quad.setEstado(EstadoCurva.PUNTO1);
            else quad.setEstado(EstadoCurva.CREAR);
        }
        
        if (cubic != null && tipoDibujo == TipoDibujo.CUBIC) {
            switch (cubic.getEstado()) {
                case CREAR: cubic.setEstado(EstadoCurva.PUNTO1); break;
                case PUNTO1: cubic.setEstado(EstadoCurva.PUNTO2); break;
                case PUNTO2: cubic.setEstado(EstadoCurva.CREAR); break;
            }
        }
    }
    
    /**
     * Obtiene la posición inicial de una forma para moverla
     */
    public void editarCoordIni() {
        if (modoEditar == ModoEditar.MOVER) {
            if (formaSeleccionada instanceof Linea) {
                posXIniMover = (int) ((Linea) formaSeleccionada).getX1() - (int) pAux.getX();
                posYIniMover = (int) ((Linea) formaSeleccionada).getY1() - (int) pAux.getY();
            } else if (formaSeleccionada instanceof Rectangle) {
                posXIniMover = (int) ((Rectangle) formaSeleccionada).getX() - (int) pAux.getX();
                posYIniMover = (int) ((Rectangle) formaSeleccionada).getY() - (int) pAux.getY();
            } else if (formaSeleccionada instanceof Ellipse2D) {
                posXIniMover = (int) ((Ellipse2D) formaSeleccionada).getX() - (int) pAux.getX();
                posYIniMover = (int) ((Ellipse2D) formaSeleccionada).getY() - (int) pAux.getY();
            } else if (formaSeleccionada instanceof RectanguloRedondeado) {
                posXIniMover = (int) ((RectanguloRedondeado) formaSeleccionada).getX() - (int) pAux.getX();
                posYIniMover = (int) ((RectanguloRedondeado) formaSeleccionada).getY() - (int) pAux.getY();
            } else if (formaSeleccionada instanceof Arco) {
                posXIniMover = (int) ((Arco) formaSeleccionada).getX() - (int) pAux.getX();
                posYIniMover = (int) ((Arco) formaSeleccionada).getY() - (int) pAux.getY();
            }
        }
    }
    
    /**
     * Mueve la formaSeleccionada a la posición p
     * @param p Posición a la que mover la formaSeleccionada
     */
    public void moverForma(Point p) {
        if (formaSeleccionada != null && modoEditar == ModoEditar.MOVER) {
            cursor = new Cursor(Cursor.MOVE_CURSOR);
            this.setCursor(cursor);

            if (formaSeleccionada instanceof Punto) ((Punto) formaSeleccionada).setLocation(p);
            else if (formaSeleccionada instanceof Linea) {
                Point p1 = new Point(posXIniMover + (int) p.getX(), posYIniMover + (int) p.getY());
                ((Linea) formaSeleccionada).setLocation(p1);
            }
            else if (formaSeleccionada instanceof Rectangulo) ((Rectangulo) formaSeleccionada).setLocation(p, posXIniMover, posYIniMover);
            else if (formaSeleccionada instanceof Elipse) ((Elipse) formaSeleccionada).setLocation(p, posXIniMover, posYIniMover);
            else if (formaSeleccionada instanceof RectanguloRedondeado) ((RectanguloRedondeado) formaSeleccionada).setLocation(p, posXIniMover, posYIniMover);
            else if (formaSeleccionada instanceof Arco) ((Arco) formaSeleccionada).setLocation(p, posXIniMover, posYIniMover);
            else if (formaSeleccionada instanceof Quad) ((Quad) formaSeleccionada).setLocation(p);
            else if (formaSeleccionada instanceof Cubic) ((Cubic) formaSeleccionada).setLocation(p);
            else if (formaSeleccionada instanceof TrazoLibre) ((TrazoLibre) formaSeleccionada).setLocation(p);
        }
    }
    
    /**
     * Actualiza el BufferRehacer cuando se añade una nueva forma
     */
    public void actualizarBufferRehacer() {
        if (vShape.size() > bufferRehacer.size() && vShape.size() > 0) bufferRehacer.add(vShape.get(vShape.size()-1));
    }
    
    /**
     * Restaura la última forma borrada
     */
    public void rehacer() {
        if (vShape.size() < bufferRehacer.size()) vShape.add(bufferRehacer.get(vShape.size()));
        this.repaint();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        pAux = evt.getPoint();
        
        /**
         * IF ModoEditar
         */
        if (modoEditar == ModoEditar.SELECCIONAR || modoEditar == ModoEditar.MOVER ||
                modoEditar == ModoEditar.BORRAR) {
            AtrShape formaSeleccionada_temp = (AtrShape) getSelectedShape(pAux);
            if (formaSeleccionada_temp != null) {
                formaSeleccionada = formaSeleccionada_temp;
                this.atributos = formaSeleccionada.getAtributos();
            }
        }
        else formaSeleccionada = null;
        
        this.editarCoordIni();
        if (modoEditar == ModoEditar.BORRAR) this.borrarForma();
        
        /**
         * IF ModoDibujar
         */
        this.crearForma();
        
        if (modoEditar == ModoEditar.NULL) {
            formaSeleccionada = null;
            this.actualizarBufferRehacer();
        }
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        this.moverForma(evt.getPoint());
        this.actualizarForma(evt.getPoint());
        
        if (modoEditar == ModoEditar.NULL) {
            this.actualizarBufferRehacer();
        }

        this.repaint();
    }//GEN-LAST:event_formMouseDragged

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        this.formMouseDragged(evt);

        this.estadoCurvas();
    }//GEN-LAST:event_formMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
