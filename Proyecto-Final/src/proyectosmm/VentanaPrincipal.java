package proyectosmm;

import SM.dic.graficos.AtrShape;
import SM.dic.graficos.ModoEditar;
import SM.dic.graficos.TipoDibujo;
import SM.dic.graficos.TipoRelleno;
import SM.dic.imagen.TipoEscalado;
import SM.dic.imagen.TipoTransformacion;
import SM.dic.sonido.Player;
import SM.dic.sonido.Recorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Ventana con el programa principal que administra todos los componentes
 * @author David Infante Casas
 */
public class VentanaPrincipal extends javax.swing.JFrame {
    
    /**
     * Permite obtener las fuentes de texto del sistema
     */
    private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    /**
     * Obtiene las fuentes de texto del sistema
     */
    private String[] fuentesSistema = ge.getAvailableFontFamilyNames();
    /**
     * Array de colores básicos
     */
    private final Color[] colores = {Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.YELLOW, Color.GREEN};
    /**
     * FileFilters con todas las extensiones seleccionables en los diálogos del programa
     */
    private final FileFilter extensionPNG = new FileNameExtensionFilter("png","png");
    private final FileFilter extensionJPG = new FileNameExtensionFilter("jpg","jpg");
    private final FileFilter extensionJPEG = new FileNameExtensionFilter("jpeg","jpeg");
    private final FileFilter extensionWAV = new FileNameExtensionFilter("wav", "wav");
    private final FileFilter extensionAU = new FileNameExtensionFilter("au", "au");
    private final FileFilter extensionMP4 = new FileNameExtensionFilter("mp4", "mp4");
    private final FileFilter extensionAVI = new FileNameExtensionFilter("avi", "avi");
    /**
     * Posición x en la que se creará la siguiente ventana interna
     */
    private int posx_ventanaInterna;
    /**
     * Posición y en la que se creará la siguiente ventana interna
     */
    private int posy_ventanaInterna;
    /**
     * Instancia del reproductor de sonido
     */
    private Player player;
    /**
     * Instancia del grabador de sonido
     */
    private Recorder recorder;
    /**
     * Instancia del Diálogo que permite seleccionar el tamaño del lienzo
     */
    private TamLienzo tamLienzo;
    /**
     * Evita que se actualice la lista de formas al hacer click cuando no es necesario
     */
    private boolean cbListaFigurasSelected;
    
    /**
     * Crea un nuevo VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
        posx_ventanaInterna = 0;
        posy_ventanaInterna = 0;
        player = new Player();
        recorder = new Recorder();
        tamLienzo = new TamLienzo(this, true);
        
        this.setSize(1900, 900);
        this.setTitle("Aplicación Multimedia David Infante Casas");
        this.jPanelDegradado.setVisible(false);
        
        jButtonPaletaTrazo.setBackground(Color.BLACK);
        jButtonPaletaRelleno.setBackground(Color.BLACK);
        jButtonPaletaDegradado.setBackground(Color.BLACK);
        
        cbListaFigurasSelected = false;
    }
    
    /**
     * Crea un lienzo nuevo con las dimensiones especificadas
     */
    public void nuevo() {
        cbListaFigurasSelected = false;
        String dim[] = null;
        try {
            dim = tamLienzo.obtenerDimensiones().split("-", 2);
        } catch (Exception ex) {
            System.out.println("Error al obtener las dimensiones");
        }
        VentanaInternaLienzoImagen vi = new VentanaInternaLienzoImagen(this);
        jDesktopPaneEscritorio.add(vi);
        vi.setSize(Integer.parseInt(dim[0])+50, Integer.parseInt(dim[1])+50);
        vi.setLocation(posx_ventanaInterna, posy_ventanaInterna);
        vi.setVisible(true);
        
        BufferedImage img = new BufferedImage(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setPaint(new Color (255, 255, 255));
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
        vi.getLienzo().setImagen(img);
        vi.getLienzo().setTipoDibujo(TipoDibujo.PUNTO);
        jToggleButtonPunto.setSelected(true);
        
        vi.setTitle("Nueva");
        posx_ventanaInterna += 10;
        posy_ventanaInterna += 10;
        jLabelEstado.setText("Punto");
        vi.getLienzo().repaint();
    }
    
    /**
     * Abre el archivo especificado y:
     * - Si es una imagen, crea un lienzo nuevo que la contiene
     * - Si es un archivo de sonido, lo carga en la playlist
     * - Si es un archivo de video, lo carga en la playlist
     */
    public void abrir() {
        JFileChooser dlg = new JFileChooser();
        dlg.setFileFilter(extensionPNG);
        dlg.addChoosableFileFilter(extensionJPG);
        dlg.addChoosableFileFilter(extensionJPEG);
        dlg.addChoosableFileFilter(extensionWAV);
        dlg.addChoosableFileFilter(extensionAU);
        dlg.addChoosableFileFilter(extensionMP4);
        dlg.addChoosableFileFilter(extensionAVI);
        int resp = dlg.showOpenDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            try {
                File f = dlg.getSelectedFile();
                String ext = f.getName().split("\\.")[1].toLowerCase();
                if (ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg")) {
                    BufferedImage img = ImageIO.read(f);
                    VentanaInternaLienzoImagen vi = new VentanaInternaLienzoImagen(this);
                    vi.getLienzo().setImagen(img);
                    vi.getLienzo().setExtension((f.getName().split("\\.")[1]).toLowerCase());
                    vi.getLienzo().setTipoDibujo(TipoDibujo.PUNTO);
                    jToggleButtonPunto.setSelected(true);
                    this.jDesktopPaneEscritorio.add(vi);
                    vi.getLienzo().setNombre(f.getName());
                    vi.setTitle(vi.getLienzo().getNombre());
                    vi.setVisible(true);
                } else if (ext.equals("wav") || ext.equals("au")) {
                    File song = new File(f.getPath()) {
                        @Override
                        public String toString() {
                            return this.getName();
                        }
                    };
                    jComboListaReproduccion.addItem(song);
                } else if (ext.equals("mp4") || ext.equals("avi")) {
                    File video = new File(f.getPath()) {
                        @Override
                        public String toString() {
                            return this.getName();
                        }
                    };
                    jComboListaReproduccion.addItem(video);
                } else JOptionPane.showMessageDialog(null, "Error al abrir el archivo");
            } catch(Exception ex) {
                System.err.println("Error al abrir el archivo");
                JOptionPane.showMessageDialog(null, "Error al abrir el archivo");
            }
        } 
    }
    
    /**
     * Guarda la imagen del lienzo seleccionado en la ruta especificada
     */
    public void guardar() {
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                JFileChooser dlg = new JFileChooser();
                dlg.setFileFilter(extensionPNG);
                dlg.addChoosableFileFilter(extensionJPG);
                dlg.addChoosableFileFilter(extensionJPEG);
                int resp = dlg.showSaveDialog(this);
                if (resp == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedImage img = vi.getLienzo().getImagen(true);
                        if (img != null) {
                            File f = new File(dlg.getSelectedFile() + "." + dlg.getFileFilter().getDescription());
                            ImageIO.write(img, dlg.getFileFilter().getDescription(), f);
                        }
                    } catch (Exception ex) {
                        System.err.println("Error al guardar la imagen");
                    }
                }
            }
        }
    }
    
    /**
     * Crea un duplicado del lienzo seleccionado
     * @param original VentanaInternaLienzoImagen original sobre la que se hará el duplicado
     * @param img Imagen que cargar en el nuevo lienzo
     * @param tipo Tipo de duplicado que se quiere hacer, copia, banda, espacio de color...
     */
    public void duplicado(VentanaInternaLienzoImagen original, BufferedImage img, String tipo) {
        VentanaInternaLienzoImagen vi = new VentanaInternaLienzoImagen(this);
        try {
            posx_ventanaInterna += 10;
            posy_ventanaInterna += 10;
            vi.setSize(original.getSize());
            vi.setLocation(posx_ventanaInterna, posy_ventanaInterna);
            if (tipo.equals("0") || tipo.equals("1") || tipo.equals("2") || tipo.equals("RGB") || tipo.equals("YCC") || tipo.equals("GREY")) vi.getLienzo().setImagen(img);
            else vi.getLienzo().setImagen(vi.getLienzo().copiaImagen(img));
            vi.getLienzo().setvShape(original.getLienzo().getvShape());
            switch(tipo) {
                case "Copia":
                    if (original.getLienzo().getNombre() != null)
                        vi.getLienzo().setNombre(original.getLienzo().getNombre() + " - Copia");
                    else
                        vi.getLienzo().setNombre("DUPLICADO");
                break;
                case "0":
                    vi.getLienzo().setNombre(original.getLienzo().getNombre() + " [Banda0]");
                break;
                case "1":
                    vi.getLienzo().setNombre(original.getLienzo().getNombre() + " [Banda1]");
                break;
                case "2":
                    vi.getLienzo().setNombre(original.getLienzo().getNombre() + " [Banda2]");
                break;
                case "RGB":
                    vi.getLienzo().setNombre(original.getLienzo().getNombre() + " [RGB]");
                break;
                case "YCC":
                    vi.getLienzo().setNombre(original.getLienzo().getNombre() + " [YCC]");
                break;
                case "GREY":
                    vi.getLienzo().setNombre(original.getLienzo().getNombre() + " [GREY]");
                break;
                case "SUMA":
                    vi.getLienzo().setNombre(original.getLienzo().getNombre() + " - SUMA");
                break;
                case "RESTA":
                    vi.getLienzo().setNombre(original.getLienzo().getNombre() + " - RESTA");
                break;
            }
            vi.setTitle(vi.getLienzo().getNombre());
            vi.getLienzo().setExtension(original.getLienzo().getExtension());
            jDesktopPaneEscritorio.add(vi);
            vi.setVisible(true);
            vi.getLienzo().repaint();
        } catch(Exception ex) {
            System.err.println("Error al leer la imagen");
        }
    }
    
    /**
     * Asigna los valores de la posición del ratón en el jLabelPosicionCursor
     * @param x Coordenada X del ratón
     * @param y Coordenada Y del ratón
     */
    public void setJLabelEstado(int x, int y) {
        this.jLabelPosicionCursor.setText(" - " + Integer.toString(x) + "x, " + Integer.toString(y) + "y");
    }
    
    /**
     * Vacía la lista de figuras
     */
    public void listaFigurasClear() {
        ((DefaultComboBoxModel) jComboBoxListaFiguras.getModel()).removeAllElements();
    }
    
    /**
     * Añade un elemento a la lista de figuras
     * @param nombre Elemento a añadir
     */
    public void setListaFiguras(String nombre) {
        ((DefaultComboBoxModel) jComboBoxListaFiguras.getModel()).addElement(nombre);
    }
    
    /**
     * Resalta el elemento seleccionado de la lista de figuras
     * @param nombre Elemento a resaltar
     */
    public void setSelectedItemListaFormas(String nombre) {
        ((DefaultComboBoxModel) jComboBoxListaFiguras.getModel()).setSelectedItem(nombre);
    }

    /**
     * Hace setSelected(...) de los botones a partir de los atributos del lienzo seleccionado
     * @param td Tipo de dibujo seleccionado en el lienzo
     * @param modoEditar modo editar seleccionado en el lienzo
     * @param colorTrazo color trazo seleccionado en el lienzo
     * @param tam_trazo tamaño de trazo seleccionado en el lienzo
     * @param rotacion valor del slider rotación
     * @param trazoDiscontinuo valor trazo discontinuo seleccionado en el lienzo
     * @param indice_tipoRelleno tipo de relleno seleccionado en el lienzo
     * @param colorRelleno color de relleno seleccionado en el lienzo
     * @param colorDegradado color de degradado seleccionado en el lienzo
     * @param alisar tipo alisado seleccionado en el lienzo
     * @param indice_fuente tipo de fuente seleccionada en el lienzo
     * @param brillo valor del brillo en el lienzo
     */
    public void setEstadoBotones(TipoDibujo td, ModoEditar modoEditar, Color colorTrazo, int tam_trazo,
            boolean trazoDiscontinuo, int indice_tipoRelleno, Color colorRelleno, Color colorDegradado, boolean alisar, int indice_fuente, int brillo, int rotacion) {
        
        if (td != TipoDibujo.NULL) {
            switch (td) {
                case PUNTO:
                    jToggleButtonPunto.setSelected(true);
                break;
                case LINEA:
                    jToggleButtonLinea.setSelected(true);
                break;
                case RECTANGULO:
                    jToggleButtonRectangulo.setSelected(true);
                break;
                case ELIPSE:
                    jToggleButtonElipse.setSelected(true);
                break;
                case REC_REDONDEADO:
                    jToggleButtonRectanguloRedondeado.setSelected(true);
                break;
                case ARCO:
                    jToggleButtonArco.setSelected(true);
                break;
                case QUAD:
                    jToggleButtonQuad.setSelected(true);
                break;
                case CUBIC:
                    jToggleButtonCubic.setSelected(true);
                break;
                case TRAZO_LIBRE:
                    jToggleButtonTrazoLibre.setSelected(true);
                break;
            }
        }
        
        if (modoEditar != ModoEditar.NULL) {
            switch (modoEditar) {
                case MOVER:
                    jToggleButtonMover.setSelected(true);
                break;
                case BORRAR:
                    jToggleButtonBorrar.setSelected(true);
                break;
                case SELECCIONAR:
                    jToggleButtonSeleccionar.setSelected(true);
                break;
                case NULL:
                    jToggleButtonSeleccionar.setSelected(false);
                    jToggleButtonMover.setSelected(false);
                    jToggleButtonBorrar.setSelected(false);
                break;
            }
        }
        
        jSpinnerGrosor.setValue(tam_trazo);
        jToggleButtonLineaDiscontinua.setSelected(trazoDiscontinuo);
        jComboBoxRelleno.setSelectedIndex(indice_tipoRelleno);
        jToggleButtonAlisar.setSelected(alisar);
        jComboBoxListaFuentes.setSelectedIndex(indice_fuente);
        jSliderBrillo.setValue(brillo);
        jSliderRotacion.setValue(rotacion);
        
        jButtonPaletaTrazo.setBackground(colorTrazo);
        jButtonPaletaRelleno.setBackground(colorRelleno);
        jButtonPaletaDegradado.setBackground(colorDegradado);
    }
    
    /**
     * Hace setSelected(...) de los botones según la figura seleccionada en el lienzo
     * @param colorTrazo color de trazo seleccionado en el lienzo
     * @param tamTrazo tamaño de trazo seleccionado en el lienzo
     * @param trDiscontinuo valor de trazo dicontinuo seleccionado en el lienzo
     * @param tipoRelleno tipo de relleno seleccionado en el lienzo
     * @param colorRelleno color de relleno seleccionado en el lienzo
     * @param alisar tipo de alisado seleccionado en el lienzo
     * @param colorDegradado color de degradado seleccionado en el lienzo
     */
    public void setEstadoBotonesFigura(Color colorTrazo, int tamTrazo,
                boolean trDiscontinuo, TipoRelleno tipoRelleno, Color colorRelleno, Color colorDegradado, boolean alisar) {
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                jButtonPaletaTrazo.setBackground(colorTrazo);
                jSpinnerGrosor.setValue(tamTrazo);
                jToggleButtonLineaDiscontinua.setSelected(trDiscontinuo);
                if (tipoRelleno == TipoRelleno.NO_RELLENAR) jComboBoxRelleno.setSelectedIndex(0);
                else if (tipoRelleno == TipoRelleno.COLOR_LISO) jComboBoxRelleno.setSelectedIndex(1);
                else if (tipoRelleno == TipoRelleno.DEGRADADO) jComboBoxRelleno.setSelectedIndex(2);
                jButtonPaletaRelleno.setBackground(colorRelleno);
                jButtonPaletaDegradado.setBackground(colorDegradado);
                jToggleButtonAlisar.setSelected(alisar);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupFormasYEditar = new javax.swing.ButtonGroup();
        jToolBarSuperior = new javax.swing.JToolBar();
        jPanelArchivo = new javax.swing.JPanel();
        jButtonNuevo = new javax.swing.JButton();
        jButtonAbrir = new javax.swing.JButton();
        jButtonGuardar = new javax.swing.JButton();
        jButtonDuplicar = new javax.swing.JButton();
        jPanelFiguras = new javax.swing.JPanel();
        jToggleButtonPunto = new javax.swing.JToggleButton();
        jToggleButtonLinea = new javax.swing.JToggleButton();
        jToggleButtonRectangulo = new javax.swing.JToggleButton();
        jToggleButtonElipse = new javax.swing.JToggleButton();
        jToggleButtonRectanguloRedondeado = new javax.swing.JToggleButton();
        jToggleButtonArco = new javax.swing.JToggleButton();
        jToggleButtonQuad = new javax.swing.JToggleButton();
        jToggleButtonCubic = new javax.swing.JToggleButton();
        jToggleButtonTrazoLibre = new javax.swing.JToggleButton();
        jPanelEditar = new javax.swing.JPanel();
        jToggleButtonSeleccionar = new javax.swing.JToggleButton();
        jComboBoxListaFiguras = new javax.swing.JComboBox<>();
        jToggleButtonMover = new javax.swing.JToggleButton();
        jLabelPosicionX = new javax.swing.JLabel();
        jTextFieldPosicionX = new javax.swing.JTextField();
        jLabelPosicionY = new javax.swing.JLabel();
        jTextFieldPosicionY = new javax.swing.JTextField();
        jToggleButtonBorrar = new javax.swing.JToggleButton();
        jPanelTrazo = new javax.swing.JPanel();
        jComboBoxColorTrazo = new javax.swing.JComboBox(colores);
        jButtonPaletaTrazo = new javax.swing.JButton();
        jSpinnerGrosor = new javax.swing.JSpinner();
        jToggleButtonLineaDiscontinua = new javax.swing.JToggleButton();
        jPanelRelleno = new javax.swing.JPanel();
        jComboBoxRelleno = new javax.swing.JComboBox<>();
        jComboBoxColorRelleno = new javax.swing.JComboBox(colores);
        jButtonPaletaRelleno = new javax.swing.JButton();
        jPanelDegradado = new javax.swing.JPanel();
        jComboBoxTipoDegradado = new javax.swing.JComboBox<>();
        jComboBoxColorDegradado = new javax.swing.JComboBox(colores);
        jButtonPaletaDegradado = new javax.swing.JButton();
        jSliderTransparencia = new javax.swing.JSlider();
        jToggleButtonAlisar = new javax.swing.JToggleButton();
        jComboBoxListaFuentes = new javax.swing.JComboBox(fuentesSistema);
        jPanelSonido = new javax.swing.JPanel();
        jButtonPlay = new javax.swing.JButton();
        jButtonPause = new javax.swing.JButton();
        jButtonStop = new javax.swing.JButton();
        jButtonGrabar = new javax.swing.JButton();
        jComboListaReproduccion = new javax.swing.JComboBox<>();
        jButtonWebcam = new javax.swing.JButton();
        jButtonScreenshot = new javax.swing.JButton();
        jPanelInferior = new javax.swing.JPanel();
        jPanelBarrasEstado = new javax.swing.JPanel();
        jLabelEstado = new javax.swing.JLabel();
        jLabelPosicionCursor = new javax.swing.JLabel();
        jToolBarHerramientasImagen = new javax.swing.JToolBar();
        jPanelBrillo = new javax.swing.JPanel();
        jSliderBrillo = new javax.swing.JSlider();
        jPanelFiltro = new javax.swing.JPanel();
        jComboBoxFiltro = new javax.swing.JComboBox<>();
        jPanelContraste = new javax.swing.JPanel();
        jButtonContraste = new javax.swing.JButton();
        jButtonIluminado = new javax.swing.JButton();
        jButtonOscurecido = new javax.swing.JButton();
        jButtonNegativo = new javax.swing.JButton();
        jPanelOperadores = new javax.swing.JPanel();
        jButtonSinusoidal = new javax.swing.JButton();
        jButtonAleatorioOp = new javax.swing.JButton();
        jButtonTintado = new javax.swing.JButton();
        jButtonEcualizar = new javax.swing.JButton();
        jButtonSepia = new javax.swing.JButton();
        jButtonSobel = new javax.swing.JButton();
        jButtonIntercambioColores = new javax.swing.JButton();
        jButtonEscalaGrises = new javax.swing.JButton();
        jPanelColor = new javax.swing.JPanel();
        jButtonBandas = new javax.swing.JButton();
        jComboBoxEspacioColor = new javax.swing.JComboBox<>();
        jPanelRotacion = new javax.swing.JPanel();
        jSliderRotacion = new javax.swing.JSlider();
        jButtonRotacion90 = new javax.swing.JButton();
        jButtonRotacion180 = new javax.swing.JButton();
        jButtonRotacion270 = new javax.swing.JButton();
        jPanelEscala = new javax.swing.JPanel();
        jButtonAumentar = new javax.swing.JButton();
        jButtonDisminuir = new javax.swing.JButton();
        jPanelBinarios = new javax.swing.JPanel();
        jButtonSuma = new javax.swing.JButton();
        jButtonResta = new javax.swing.JButton();
        jPanelUmbralizacion = new javax.swing.JPanel();
        jSliderUmbralizacion = new javax.swing.JSlider();
        jDesktopPaneEscritorio = new javax.swing.JDesktopPane();
        jMenuBarMenu = new javax.swing.JMenuBar();
        jMenuArchivo = new javax.swing.JMenu();
        jMenuItemNuevo = new javax.swing.JMenuItem();
        jMenuItemAbrir = new javax.swing.JMenuItem();
        jMenuItemGuardar = new javax.swing.JMenuItem();
        jMenuItemDuplicar = new javax.swing.JMenuItem();
        jMenuVer = new javax.swing.JMenu();
        jCheckBoxMenuItemBarradeEstado = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemBarradeFormas = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItemBarraAtributos = new javax.swing.JCheckBoxMenuItem();
        jMenuEditar = new javax.swing.JMenu();
        jMenuItemDeshacer = new javax.swing.JMenuItem();
        jMenuItemRehacer = new javax.swing.JMenuItem();
        jMenuOpciones = new javax.swing.JMenu();
        jMenuItemTamLienzo = new javax.swing.JMenuItem();
        jMenuAyuda = new javax.swing.JMenu();
        jMenuItemAcercaDe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBarSuperior.setRollover(true);
        jToolBarSuperior.setToolTipText("ToolBar Superior");

        jPanelArchivo.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivo"));
        jPanelArchivo.setToolTipText("Archivo");
        jPanelArchivo.setLayout(new javax.swing.BoxLayout(jPanelArchivo, javax.swing.BoxLayout.LINE_AXIS));

        jButtonNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevo.png"))); // NOI18N
        jButtonNuevo.setToolTipText("Nuevo");
        jButtonNuevo.setFocusable(false);
        jButtonNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNuevo.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonNuevo.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoActionPerformed(evt);
            }
        });
        jPanelArchivo.add(jButtonNuevo);

        jButtonAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/abrir.png"))); // NOI18N
        jButtonAbrir.setToolTipText("Abrir");
        jButtonAbrir.setFocusable(false);
        jButtonAbrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAbrir.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonAbrir.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonAbrir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAbrirActionPerformed(evt);
            }
        });
        jPanelArchivo.add(jButtonAbrir);

        jButtonGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardar.png"))); // NOI18N
        jButtonGuardar.setToolTipText("Guardar");
        jButtonGuardar.setFocusable(false);
        jButtonGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonGuardar.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonGuardar.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });
        jPanelArchivo.add(jButtonGuardar);

        jButtonDuplicar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/duplicar.png"))); // NOI18N
        jButtonDuplicar.setToolTipText("Duplicar");
        jButtonDuplicar.setFocusable(false);
        jButtonDuplicar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDuplicar.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonDuplicar.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonDuplicar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDuplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDuplicarActionPerformed(evt);
            }
        });
        jPanelArchivo.add(jButtonDuplicar);

        jToolBarSuperior.add(jPanelArchivo);

        jPanelFiguras.setBorder(javax.swing.BorderFactory.createTitledBorder("Figuras"));
        jPanelFiguras.setToolTipText("Figuras");
        jPanelFiguras.setLayout(new javax.swing.BoxLayout(jPanelFiguras, javax.swing.BoxLayout.LINE_AXIS));

        buttonGroupFormasYEditar.add(jToggleButtonPunto);
        jToggleButtonPunto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/punto.png"))); // NOI18N
        jToggleButtonPunto.setSelected(true);
        jToggleButtonPunto.setToolTipText("Punto");
        jToggleButtonPunto.setFocusable(false);
        jToggleButtonPunto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonPunto.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonPunto.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonPunto.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonPunto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonPunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonPuntoActionPerformed(evt);
            }
        });
        jPanelFiguras.add(jToggleButtonPunto);

        buttonGroupFormasYEditar.add(jToggleButtonLinea);
        jToggleButtonLinea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/linea.png"))); // NOI18N
        jToggleButtonLinea.setToolTipText("Linea");
        jToggleButtonLinea.setFocusable(false);
        jToggleButtonLinea.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonLinea.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonLinea.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonLinea.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonLinea.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonLinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonLineaActionPerformed(evt);
            }
        });
        jPanelFiguras.add(jToggleButtonLinea);

        buttonGroupFormasYEditar.add(jToggleButtonRectangulo);
        jToggleButtonRectangulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rectangulo.png"))); // NOI18N
        jToggleButtonRectangulo.setToolTipText("Rectángulo");
        jToggleButtonRectangulo.setFocusable(false);
        jToggleButtonRectangulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonRectangulo.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonRectangulo.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonRectangulo.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonRectangulo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonRectangulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonRectanguloActionPerformed(evt);
            }
        });
        jPanelFiguras.add(jToggleButtonRectangulo);

        buttonGroupFormasYEditar.add(jToggleButtonElipse);
        jToggleButtonElipse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/elipse.png"))); // NOI18N
        jToggleButtonElipse.setToolTipText("Elipse");
        jToggleButtonElipse.setFocusable(false);
        jToggleButtonElipse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonElipse.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonElipse.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonElipse.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonElipse.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonElipse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonElipseActionPerformed(evt);
            }
        });
        jPanelFiguras.add(jToggleButtonElipse);

        buttonGroupFormasYEditar.add(jToggleButtonRectanguloRedondeado);
        jToggleButtonRectanguloRedondeado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/roundRectangle.png"))); // NOI18N
        jToggleButtonRectanguloRedondeado.setToolTipText("Rectangulo Redondeado");
        jToggleButtonRectanguloRedondeado.setFocusable(false);
        jToggleButtonRectanguloRedondeado.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonRectanguloRedondeado.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonRectanguloRedondeado.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonRectanguloRedondeado.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonRectanguloRedondeado.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonRectanguloRedondeado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonRectanguloRedondeadoActionPerformed(evt);
            }
        });
        jPanelFiguras.add(jToggleButtonRectanguloRedondeado);

        buttonGroupFormasYEditar.add(jToggleButtonArco);
        jToggleButtonArco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Arco.png"))); // NOI18N
        jToggleButtonArco.setToolTipText("Arco");
        jToggleButtonArco.setFocusable(false);
        jToggleButtonArco.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonArco.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonArco.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonArco.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonArco.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonArco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonArcoActionPerformed(evt);
            }
        });
        jPanelFiguras.add(jToggleButtonArco);

        buttonGroupFormasYEditar.add(jToggleButtonQuad);
        jToggleButtonQuad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Quad.png"))); // NOI18N
        jToggleButtonQuad.setToolTipText("Quad");
        jToggleButtonQuad.setFocusable(false);
        jToggleButtonQuad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonQuad.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonQuad.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonQuad.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonQuad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonQuad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonQuadActionPerformed(evt);
            }
        });
        jPanelFiguras.add(jToggleButtonQuad);

        buttonGroupFormasYEditar.add(jToggleButtonCubic);
        jToggleButtonCubic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Cubic.png"))); // NOI18N
        jToggleButtonCubic.setToolTipText("Cubic");
        jToggleButtonCubic.setFocusable(false);
        jToggleButtonCubic.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonCubic.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonCubic.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonCubic.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonCubic.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonCubic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonCubicActionPerformed(evt);
            }
        });
        jPanelFiguras.add(jToggleButtonCubic);

        buttonGroupFormasYEditar.add(jToggleButtonTrazoLibre);
        jToggleButtonTrazoLibre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/trazoLibre.png"))); // NOI18N
        jToggleButtonTrazoLibre.setToolTipText("Trazo Libre");
        jToggleButtonTrazoLibre.setFocusable(false);
        jToggleButtonTrazoLibre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonTrazoLibre.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonTrazoLibre.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonTrazoLibre.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonTrazoLibre.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonTrazoLibre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonTrazoLibreActionPerformed(evt);
            }
        });
        jPanelFiguras.add(jToggleButtonTrazoLibre);

        jToolBarSuperior.add(jPanelFiguras);

        jPanelEditar.setBorder(javax.swing.BorderFactory.createTitledBorder("Editar"));
        jPanelEditar.setToolTipText("Editar");
        jPanelEditar.setLayout(new javax.swing.BoxLayout(jPanelEditar, javax.swing.BoxLayout.LINE_AXIS));

        buttonGroupFormasYEditar.add(jToggleButtonSeleccionar);
        jToggleButtonSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/seleccion.png"))); // NOI18N
        jToggleButtonSeleccionar.setToolTipText("Seleccionar");
        jToggleButtonSeleccionar.setFocusable(false);
        jToggleButtonSeleccionar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonSeleccionar.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonSeleccionar.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonSeleccionar.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonSeleccionar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSeleccionarActionPerformed(evt);
            }
        });
        jPanelEditar.add(jToggleButtonSeleccionar);

        jComboBoxListaFiguras.setModel(new DefaultComboBoxModel());
        jComboBoxListaFiguras.setToolTipText("Lista Figuras");
        jComboBoxListaFiguras.setMaximumSize(new java.awt.Dimension(150, 34));
        jComboBoxListaFiguras.setMinimumSize(new java.awt.Dimension(150, 34));
        jComboBoxListaFiguras.setPreferredSize(new java.awt.Dimension(150, 34));
        jComboBoxListaFiguras.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboBoxListaFigurasFocusLost(evt);
            }
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBoxListaFigurasFocusGained(evt);
            }
        });
        jComboBoxListaFiguras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxListaFigurasActionPerformed(evt);
            }
        });
        jPanelEditar.add(jComboBoxListaFiguras);

        buttonGroupFormasYEditar.add(jToggleButtonMover);
        jToggleButtonMover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Mover.png"))); // NOI18N
        jToggleButtonMover.setToolTipText("Mover");
        jToggleButtonMover.setFocusable(false);
        jToggleButtonMover.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonMover.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonMover.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonMover.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonMover.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonMover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonMoverActionPerformed(evt);
            }
        });
        jPanelEditar.add(jToggleButtonMover);

        jLabelPosicionX.setText("X:");
        jLabelPosicionX.setToolTipText("Posicion X");
        jLabelPosicionX.setMaximumSize(new java.awt.Dimension(15, 34));
        jLabelPosicionX.setMinimumSize(new java.awt.Dimension(15, 34));
        jLabelPosicionX.setPreferredSize(new java.awt.Dimension(15, 34));
        jPanelEditar.add(jLabelPosicionX);

        jTextFieldPosicionX.setToolTipText("Posicion X");
        jTextFieldPosicionX.setMaximumSize(new java.awt.Dimension(50, 34));
        jTextFieldPosicionX.setMinimumSize(new java.awt.Dimension(50, 34));
        jTextFieldPosicionX.setPreferredSize(new java.awt.Dimension(50, 34));
        jTextFieldPosicionX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPosicionXActionPerformed(evt);
            }
        });
        jPanelEditar.add(jTextFieldPosicionX);

        jLabelPosicionY.setText("Y:");
        jLabelPosicionY.setToolTipText("Posicion Y");
        jLabelPosicionY.setMaximumSize(new java.awt.Dimension(15, 34));
        jLabelPosicionY.setMinimumSize(new java.awt.Dimension(15, 34));
        jLabelPosicionY.setPreferredSize(new java.awt.Dimension(15, 34));
        jPanelEditar.add(jLabelPosicionY);

        jTextFieldPosicionY.setToolTipText("Posicion Y");
        jTextFieldPosicionY.setMaximumSize(new java.awt.Dimension(50, 34));
        jTextFieldPosicionY.setMinimumSize(new java.awt.Dimension(50, 34));
        jTextFieldPosicionY.setPreferredSize(new java.awt.Dimension(50, 34));
        jTextFieldPosicionY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPosicionYActionPerformed(evt);
            }
        });
        jPanelEditar.add(jTextFieldPosicionY);

        buttonGroupFormasYEditar.add(jToggleButtonBorrar);
        jToggleButtonBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Papelera.png"))); // NOI18N
        jToggleButtonBorrar.setToolTipText("Borrar");
        jToggleButtonBorrar.setFocusable(false);
        jToggleButtonBorrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonBorrar.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonBorrar.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonBorrar.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonBorrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonBorrarActionPerformed(evt);
            }
        });
        jPanelEditar.add(jToggleButtonBorrar);

        jToolBarSuperior.add(jPanelEditar);

        jPanelTrazo.setBorder(javax.swing.BorderFactory.createTitledBorder("Trazo"));
        jPanelTrazo.setToolTipText("Trazo");
        jPanelTrazo.setMaximumSize(new java.awt.Dimension(32942, 56));
        jPanelTrazo.setLayout(new javax.swing.BoxLayout(jPanelTrazo, javax.swing.BoxLayout.LINE_AXIS));

        jComboBoxColorTrazo.setForeground(new java.awt.Color(255, 255, 255));
        jComboBoxColorTrazo.setMaximumRowCount(6);
        jComboBoxColorTrazo.setToolTipText("Color Trazo");
        jComboBoxColorTrazo.setRenderer(new SM.dic.iu.ListaColores());
        jComboBoxColorTrazo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxColorTrazoActionPerformed(evt);
            }
        });
        jPanelTrazo.add(jComboBoxColorTrazo);

        jButtonPaletaTrazo.setToolTipText("Paleta Trazo");
        jButtonPaletaTrazo.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonPaletaTrazo.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonPaletaTrazo.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonPaletaTrazo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPaletaTrazoActionPerformed(evt);
            }
        });
        jPanelTrazo.add(jButtonPaletaTrazo);

        jSpinnerGrosor.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));
        jSpinnerGrosor.setToolTipText("Tamaño trazo");
        jSpinnerGrosor.setMaximumSize(new java.awt.Dimension(44, 32767));
        jSpinnerGrosor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerGrosorStateChanged(evt);
            }
        });
        jPanelTrazo.add(jSpinnerGrosor);

        jToggleButtonLineaDiscontinua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/linea_discontinua.png"))); // NOI18N
        jToggleButtonLineaDiscontinua.setToolTipText("Linea Discontinua");
        jToggleButtonLineaDiscontinua.setFocusable(false);
        jToggleButtonLineaDiscontinua.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonLineaDiscontinua.setMaximumSize(new java.awt.Dimension(34, 34));
        jToggleButtonLineaDiscontinua.setMinimumSize(new java.awt.Dimension(34, 34));
        jToggleButtonLineaDiscontinua.setPreferredSize(new java.awt.Dimension(34, 34));
        jToggleButtonLineaDiscontinua.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonLineaDiscontinua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonLineaDiscontinuaActionPerformed(evt);
            }
        });
        jPanelTrazo.add(jToggleButtonLineaDiscontinua);

        jToolBarSuperior.add(jPanelTrazo);

        jPanelRelleno.setBorder(javax.swing.BorderFactory.createTitledBorder("Relleno"));
        jPanelRelleno.setToolTipText("Relleno");
        jPanelRelleno.setLayout(new javax.swing.BoxLayout(jPanelRelleno, javax.swing.BoxLayout.LINE_AXIS));

        jComboBoxRelleno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No rellenar", "Color liso", "Degrado" }));
        jComboBoxRelleno.setToolTipText("Tipo Relleno");
        jComboBoxRelleno.setMaximumSize(new java.awt.Dimension(90, 40));
        jComboBoxRelleno.setMinimumSize(new java.awt.Dimension(90, 40));
        jComboBoxRelleno.setPreferredSize(new java.awt.Dimension(90, 40));
        jComboBoxRelleno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxRellenoActionPerformed(evt);
            }
        });
        jPanelRelleno.add(jComboBoxRelleno);

        jComboBoxColorRelleno.setForeground(new java.awt.Color(255, 255, 255));
        jComboBoxColorRelleno.setMaximumRowCount(6);
        jComboBoxColorRelleno.setToolTipText("Color Relleno");
        jComboBoxColorRelleno.setMaximumSize(new java.awt.Dimension(45, 45));
        jComboBoxColorRelleno.setMinimumSize(new java.awt.Dimension(45, 45));
        jComboBoxColorRelleno.setPreferredSize(new java.awt.Dimension(45, 45));
        jComboBoxColorRelleno.setRenderer(new SM.dic.iu.ListaColores());
        jComboBoxColorRelleno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxColorRellenoActionPerformed(evt);
            }
        });
        jPanelRelleno.add(jComboBoxColorRelleno);

        jButtonPaletaRelleno.setToolTipText("Paleta Relleno");
        jButtonPaletaRelleno.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonPaletaRelleno.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonPaletaRelleno.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonPaletaRelleno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPaletaRellenoActionPerformed(evt);
            }
        });
        jPanelRelleno.add(jButtonPaletaRelleno);

        jPanelDegradado.setBorder(javax.swing.BorderFactory.createTitledBorder("Degradado"));
        jPanelDegradado.setToolTipText("Degradado");
        jPanelDegradado.setLayout(new javax.swing.BoxLayout(jPanelDegradado, javax.swing.BoxLayout.LINE_AXIS));

        jComboBoxTipoDegradado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Vertical", "Horizontal" }));
        jComboBoxTipoDegradado.setToolTipText("Tipo Degradado");
        jComboBoxTipoDegradado.setMaximumSize(new java.awt.Dimension(76, 24));
        jComboBoxTipoDegradado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipoDegradadoActionPerformed(evt);
            }
        });
        jPanelDegradado.add(jComboBoxTipoDegradado);

        jComboBoxColorDegradado.setForeground(new java.awt.Color(255, 255, 255));
        jComboBoxColorDegradado.setMaximumRowCount(6);
        jComboBoxColorDegradado.setToolTipText("Color Degradado");
        jComboBoxColorDegradado.setMaximumSize(new java.awt.Dimension(45, 45));
        jComboBoxColorDegradado.setMinimumSize(new java.awt.Dimension(45, 45));
        jComboBoxColorDegradado.setPreferredSize(new java.awt.Dimension(45, 45));
        jComboBoxColorDegradado.setRenderer(new SM.dic.iu.ListaColores());
        jComboBoxColorDegradado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxColorDegradadoActionPerformed(evt);
            }
        });
        jPanelDegradado.add(jComboBoxColorDegradado);

        jButtonPaletaDegradado.setToolTipText("Paleta Degradado");
        jButtonPaletaDegradado.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonPaletaDegradado.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonPaletaDegradado.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonPaletaDegradado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPaletaDegradadoActionPerformed(evt);
            }
        });
        jPanelDegradado.add(jButtonPaletaDegradado);

        jPanelRelleno.add(jPanelDegradado);

        jToolBarSuperior.add(jPanelRelleno);

        jSliderTransparencia.setToolTipText("Transparencia");
        jSliderTransparencia.setValue(100);
        jSliderTransparencia.setBorder(javax.swing.BorderFactory.createTitledBorder("Transparencia"));
        jSliderTransparencia.setMaximumSize(new java.awt.Dimension(200, 38));
        jSliderTransparencia.setPreferredSize(new java.awt.Dimension(100, 50));
        jSliderTransparencia.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderTransparenciaStateChanged(evt);
            }
        });
        jToolBarSuperior.add(jSliderTransparencia);

        jToggleButtonAlisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/alisar.png"))); // NOI18N
        jToggleButtonAlisar.setToolTipText("Alisar");
        jToggleButtonAlisar.setFocusable(false);
        jToggleButtonAlisar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonAlisar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonAlisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonAlisarActionPerformed(evt);
            }
        });
        jToolBarSuperior.add(jToggleButtonAlisar);

        jComboBoxListaFuentes.setModel(new javax.swing.DefaultComboBoxModel<>(fuentesSistema));
        jComboBoxListaFuentes.setToolTipText("Tipo Fuente");
        jComboBoxListaFuentes.setMaximumSize(new java.awt.Dimension(70, 24));
        jComboBoxListaFuentes.setMinimumSize(new java.awt.Dimension(70, 24));
        jComboBoxListaFuentes.setPreferredSize(new java.awt.Dimension(70, 24));
        jComboBoxListaFuentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxListaFuentesActionPerformed(evt);
            }
        });
        jToolBarSuperior.add(jComboBoxListaFuentes);

        jPanelSonido.setBorder(javax.swing.BorderFactory.createTitledBorder("Sonido y Video"));
        jPanelSonido.setToolTipText("Sonido y Video");
        jPanelSonido.setLayout(new javax.swing.BoxLayout(jPanelSonido, javax.swing.BoxLayout.LINE_AXIS));

        jButtonPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/play24x24.png"))); // NOI18N
        jButtonPlay.setToolTipText("Play");
        jButtonPlay.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonPlay.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonPlay.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayActionPerformed(evt);
            }
        });
        jPanelSonido.add(jButtonPlay);

        jButtonPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/pausa24x24.png"))); // NOI18N
        jButtonPause.setToolTipText("Pausa");
        jButtonPause.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonPause.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonPause.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPauseActionPerformed(evt);
            }
        });
        jPanelSonido.add(jButtonPause);

        jButtonStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/stop24x24.png"))); // NOI18N
        jButtonStop.setToolTipText("Stop");
        jButtonStop.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonStop.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonStop.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });
        jPanelSonido.add(jButtonStop);

        jButtonGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/record24x24.png"))); // NOI18N
        jButtonGrabar.setToolTipText("Grabar");
        jButtonGrabar.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonGrabar.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonGrabar.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGrabarActionPerformed(evt);
            }
        });
        jPanelSonido.add(jButtonGrabar);

        jComboListaReproduccion.setToolTipText("Lista Reproducción");
        jComboListaReproduccion.setMaximumSize(new java.awt.Dimension(100, 24));
        jComboListaReproduccion.setMinimumSize(new java.awt.Dimension(100, 24));
        jComboListaReproduccion.setPreferredSize(new java.awt.Dimension(100, 24));
        jPanelSonido.add(jComboListaReproduccion);

        jButtonWebcam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Camara.png"))); // NOI18N
        jButtonWebcam.setToolTipText("Webcam");
        jButtonWebcam.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonWebcam.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonWebcam.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonWebcam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWebcamActionPerformed(evt);
            }
        });
        jPanelSonido.add(jButtonWebcam);

        jButtonScreenshot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Capturar.png"))); // NOI18N
        jButtonScreenshot.setToolTipText("Screenshot");
        jButtonScreenshot.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonScreenshot.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonScreenshot.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonScreenshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScreenshotActionPerformed(evt);
            }
        });
        jPanelSonido.add(jButtonScreenshot);

        jToolBarSuperior.add(jPanelSonido);

        getContentPane().add(jToolBarSuperior, java.awt.BorderLayout.PAGE_START);

        jPanelInferior.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, new java.awt.Color(102, 102, 102), new java.awt.Color(204, 204, 204)));
        jPanelInferior.setToolTipText("Panel Inferior");
        jPanelInferior.setLayout(new java.awt.BorderLayout());

        jPanelBarrasEstado.setToolTipText("Barra Estado");
        jPanelBarrasEstado.setLayout(new javax.swing.BoxLayout(jPanelBarrasEstado, javax.swing.BoxLayout.LINE_AXIS));

        jLabelEstado.setText("Punto");
        jLabelEstado.setToolTipText("Tipo Figura");
        jPanelBarrasEstado.add(jLabelEstado);

        jLabelPosicionCursor.setToolTipText("Posición Cursor");
        jPanelBarrasEstado.add(jLabelPosicionCursor);

        jPanelInferior.add(jPanelBarrasEstado, java.awt.BorderLayout.CENTER);

        jToolBarHerramientasImagen.setRollover(true);
        jToolBarHerramientasImagen.setToolTipText("ToolBar Inferior");

        jPanelBrillo.setBorder(javax.swing.BorderFactory.createTitledBorder("Brillo"));
        jPanelBrillo.setToolTipText("Brillo");
        jPanelBrillo.setMaximumSize(new java.awt.Dimension(170, 70));
        jPanelBrillo.setMinimumSize(new java.awt.Dimension(170, 70));
        jPanelBrillo.setPreferredSize(new java.awt.Dimension(170, 70));
        jPanelBrillo.setLayout(new javax.swing.BoxLayout(jPanelBrillo, javax.swing.BoxLayout.LINE_AXIS));

        jSliderBrillo.setMinimum(-100);
        jSliderBrillo.setToolTipText("Brillo");
        jSliderBrillo.setValue(0);
        jSliderBrillo.setMaximumSize(new java.awt.Dimension(150, 70));
        jSliderBrillo.setMinimumSize(new java.awt.Dimension(150, 70));
        jSliderBrillo.setPreferredSize(new java.awt.Dimension(150, 70));
        jSliderBrillo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderBrilloStateChanged(evt);
            }
        });
        jSliderBrillo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSliderBrilloFocusLost(evt);
            }
        });
        jPanelBrillo.add(jSliderBrillo);

        jToolBarHerramientasImagen.add(jPanelBrillo);

        jPanelFiltro.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro"));
        jPanelFiltro.setToolTipText("Filtro");
        jPanelFiltro.setMaximumSize(new java.awt.Dimension(200, 70));
        jPanelFiltro.setMinimumSize(new java.awt.Dimension(200, 70));
        jPanelFiltro.setPreferredSize(new java.awt.Dimension(200, 70));
        jPanelFiltro.setLayout(new java.awt.BorderLayout());

        jComboBoxFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Emborronamiento media", "Emborronamiento binomial", "Enfoque", "Relieve", "Detector de fronteras laplaciano" }));
        jComboBoxFiltro.setSelectedItem("Ninguno");
        jComboBoxFiltro.setToolTipText("Filtro");
        jComboBoxFiltro.setPreferredSize(new java.awt.Dimension(183, 50));
        jComboBoxFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFiltroActionPerformed(evt);
            }
        });
        jPanelFiltro.add(jComboBoxFiltro, java.awt.BorderLayout.CENTER);

        jToolBarHerramientasImagen.add(jPanelFiltro);

        jPanelContraste.setBorder(javax.swing.BorderFactory.createTitledBorder("Contraste"));
        jPanelContraste.setToolTipText("Contraste");
        jPanelContraste.setMaximumSize(new java.awt.Dimension(160, 70));
        jPanelContraste.setMinimumSize(new java.awt.Dimension(160, 70));
        jPanelContraste.setPreferredSize(new java.awt.Dimension(160, 70));
        jPanelContraste.setLayout(new javax.swing.BoxLayout(jPanelContraste, javax.swing.BoxLayout.LINE_AXIS));

        jButtonContraste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/contraste.png"))); // NOI18N
        jButtonContraste.setToolTipText("Contraste");
        jButtonContraste.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonContraste.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonContraste.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonContraste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonContrasteActionPerformed(evt);
            }
        });
        jPanelContraste.add(jButtonContraste);

        jButtonIluminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/iluminar.png"))); // NOI18N
        jButtonIluminado.setToolTipText("Iluminar");
        jButtonIluminado.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonIluminado.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonIluminado.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonIluminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIluminadoActionPerformed(evt);
            }
        });
        jPanelContraste.add(jButtonIluminado);

        jButtonOscurecido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/oscurecer.png"))); // NOI18N
        jButtonOscurecido.setToolTipText("Oscurecer");
        jButtonOscurecido.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonOscurecido.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonOscurecido.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonOscurecido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOscurecidoActionPerformed(evt);
            }
        });
        jPanelContraste.add(jButtonOscurecido);

        jButtonNegativo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/negativo.png"))); // NOI18N
        jButtonNegativo.setToolTipText("Negativo");
        jButtonNegativo.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonNegativo.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonNegativo.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonNegativo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNegativoActionPerformed(evt);
            }
        });
        jPanelContraste.add(jButtonNegativo);

        jToolBarHerramientasImagen.add(jPanelContraste);

        jPanelOperadores.setBorder(javax.swing.BorderFactory.createTitledBorder("Operadores"));
        jPanelOperadores.setToolTipText("Operadores");
        jPanelOperadores.setMaximumSize(new java.awt.Dimension(300, 70));
        jPanelOperadores.setMinimumSize(new java.awt.Dimension(300, 70));
        jPanelOperadores.setPreferredSize(new java.awt.Dimension(300, 70));
        jPanelOperadores.setLayout(new javax.swing.BoxLayout(jPanelOperadores, javax.swing.BoxLayout.LINE_AXIS));

        jButtonSinusoidal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/sinusoidal.png"))); // NOI18N
        jButtonSinusoidal.setToolTipText("Sinusoidal");
        jButtonSinusoidal.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonSinusoidal.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonSinusoidal.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonSinusoidal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSinusoidalActionPerformed(evt);
            }
        });
        jPanelOperadores.add(jButtonSinusoidal);

        jButtonAleatorioOp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Aleatorio.png"))); // NOI18N
        jButtonAleatorioOp.setToolTipText("Aleatorio");
        jButtonAleatorioOp.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonAleatorioOp.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonAleatorioOp.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonAleatorioOp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAleatorioOpActionPerformed(evt);
            }
        });
        jPanelOperadores.add(jButtonAleatorioOp);

        jButtonTintado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/tintar.png"))); // NOI18N
        jButtonTintado.setToolTipText("Tintado");
        jButtonTintado.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonTintado.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonTintado.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonTintado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTintadoActionPerformed(evt);
            }
        });
        jPanelOperadores.add(jButtonTintado);

        jButtonEcualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/ecualizar.png"))); // NOI18N
        jButtonEcualizar.setToolTipText("Ecualizar");
        jButtonEcualizar.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonEcualizar.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonEcualizar.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonEcualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEcualizarActionPerformed(evt);
            }
        });
        jPanelOperadores.add(jButtonEcualizar);

        jButtonSepia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/sepia.png"))); // NOI18N
        jButtonSepia.setToolTipText("Sepia");
        jButtonSepia.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonSepia.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonSepia.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonSepia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSepiaActionPerformed(evt);
            }
        });
        jPanelOperadores.add(jButtonSepia);

        jButtonSobel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/sobel.png"))); // NOI18N
        jButtonSobel.setToolTipText("Sobel");
        jButtonSobel.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonSobel.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonSobel.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonSobel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSobelActionPerformed(evt);
            }
        });
        jPanelOperadores.add(jButtonSobel);

        jButtonIntercambioColores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/IntercambioColores.png"))); // NOI18N
        jButtonIntercambioColores.setToolTipText("Intercambio Colores");
        jButtonIntercambioColores.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonIntercambioColores.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonIntercambioColores.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonIntercambioColores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIntercambioColoresActionPerformed(evt);
            }
        });
        jPanelOperadores.add(jButtonIntercambioColores);

        jButtonEscalaGrises.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/escalaGrises.png"))); // NOI18N
        jButtonEscalaGrises.setToolTipText("Escala Grises");
        jButtonEscalaGrises.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonEscalaGrises.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonEscalaGrises.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonEscalaGrises.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEscalaGrisesActionPerformed(evt);
            }
        });
        jPanelOperadores.add(jButtonEscalaGrises);

        jToolBarHerramientasImagen.add(jPanelOperadores);

        jPanelColor.setBorder(javax.swing.BorderFactory.createTitledBorder("Color"));
        jPanelColor.setToolTipText("Color");
        jPanelColor.setMaximumSize(new java.awt.Dimension(120, 70));
        jPanelColor.setMinimumSize(new java.awt.Dimension(120, 70));
        jPanelColor.setPreferredSize(new java.awt.Dimension(120, 70));
        jPanelColor.setLayout(new javax.swing.BoxLayout(jPanelColor, javax.swing.BoxLayout.LINE_AXIS));

        jButtonBandas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/bandas.png"))); // NOI18N
        jButtonBandas.setToolTipText("Bandas");
        jButtonBandas.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonBandas.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonBandas.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonBandas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBandasActionPerformed(evt);
            }
        });
        jPanelColor.add(jButtonBandas);

        jComboBoxEspacioColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RGB", "YCC", "GREY" }));
        jComboBoxEspacioColor.setToolTipText("Espacio Color");
        jComboBoxEspacioColor.setMaximumSize(new java.awt.Dimension(60, 34));
        jComboBoxEspacioColor.setMinimumSize(new java.awt.Dimension(60, 34));
        jComboBoxEspacioColor.setPreferredSize(new java.awt.Dimension(60, 34));
        jComboBoxEspacioColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxEspacioColorActionPerformed(evt);
            }
        });
        jPanelColor.add(jComboBoxEspacioColor);

        jToolBarHerramientasImagen.add(jPanelColor);

        jPanelRotacion.setBorder(javax.swing.BorderFactory.createTitledBorder("Rotación"));
        jPanelRotacion.setToolTipText("Rotación");
        jPanelRotacion.setMaximumSize(new java.awt.Dimension(250, 70));
        jPanelRotacion.setMinimumSize(new java.awt.Dimension(250, 70));
        jPanelRotacion.setPreferredSize(new java.awt.Dimension(250, 70));
        jPanelRotacion.setLayout(new javax.swing.BoxLayout(jPanelRotacion, javax.swing.BoxLayout.LINE_AXIS));

        jSliderRotacion.setMaximum(360);
        jSliderRotacion.setToolTipText("Rotación");
        jSliderRotacion.setValue(0);
        jSliderRotacion.setMaximumSize(new java.awt.Dimension(120, 38));
        jSliderRotacion.setMinimumSize(new java.awt.Dimension(120, 16));
        jSliderRotacion.setPreferredSize(new java.awt.Dimension(120, 16));
        jSliderRotacion.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderRotacionStateChanged(evt);
            }
        });
        jPanelRotacion.add(jSliderRotacion);

        jButtonRotacion90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rotacion90.png"))); // NOI18N
        jButtonRotacion90.setToolTipText("Rotación90");
        jButtonRotacion90.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonRotacion90.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonRotacion90.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonRotacion90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotacion90ActionPerformed(evt);
            }
        });
        jPanelRotacion.add(jButtonRotacion90);

        jButtonRotacion180.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rotacion180.png"))); // NOI18N
        jButtonRotacion180.setToolTipText("Rotación180");
        jButtonRotacion180.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonRotacion180.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonRotacion180.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonRotacion180.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotacion180ActionPerformed(evt);
            }
        });
        jPanelRotacion.add(jButtonRotacion180);

        jButtonRotacion270.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rotacion270.png"))); // NOI18N
        jButtonRotacion270.setToolTipText("Rotación270");
        jButtonRotacion270.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonRotacion270.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonRotacion270.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonRotacion270.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotacion270ActionPerformed(evt);
            }
        });
        jPanelRotacion.add(jButtonRotacion270);

        jToolBarHerramientasImagen.add(jPanelRotacion);

        jPanelEscala.setBorder(javax.swing.BorderFactory.createTitledBorder("Escala"));
        jPanelEscala.setToolTipText("Escala");
        jPanelEscala.setMaximumSize(new java.awt.Dimension(100, 70));
        jPanelEscala.setMinimumSize(new java.awt.Dimension(100, 70));
        jPanelEscala.setPreferredSize(new java.awt.Dimension(100, 70));
        jPanelEscala.setLayout(new javax.swing.BoxLayout(jPanelEscala, javax.swing.BoxLayout.LINE_AXIS));

        jButtonAumentar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/aumentar.png"))); // NOI18N
        jButtonAumentar.setToolTipText("Aumentar");
        jButtonAumentar.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonAumentar.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonAumentar.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonAumentar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAumentarActionPerformed(evt);
            }
        });
        jPanelEscala.add(jButtonAumentar);

        jButtonDisminuir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/disminuir.png"))); // NOI18N
        jButtonDisminuir.setToolTipText("Disminuir");
        jButtonDisminuir.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonDisminuir.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonDisminuir.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonDisminuir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisminuirActionPerformed(evt);
            }
        });
        jPanelEscala.add(jButtonDisminuir);

        jToolBarHerramientasImagen.add(jPanelEscala);

        jPanelBinarios.setBorder(javax.swing.BorderFactory.createTitledBorder("Binarios"));
        jPanelBinarios.setToolTipText("Binarios");
        jPanelBinarios.setMaximumSize(new java.awt.Dimension(100, 70));
        jPanelBinarios.setMinimumSize(new java.awt.Dimension(100, 70));
        jPanelBinarios.setPreferredSize(new java.awt.Dimension(100, 70));
        jPanelBinarios.setLayout(new javax.swing.BoxLayout(jPanelBinarios, javax.swing.BoxLayout.LINE_AXIS));

        jButtonSuma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/suma.png"))); // NOI18N
        jButtonSuma.setToolTipText("Suma");
        jButtonSuma.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonSuma.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonSuma.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonSuma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSumaActionPerformed(evt);
            }
        });
        jPanelBinarios.add(jButtonSuma);

        jButtonResta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/resta.png"))); // NOI18N
        jButtonResta.setToolTipText("Resta");
        jButtonResta.setMaximumSize(new java.awt.Dimension(34, 34));
        jButtonResta.setMinimumSize(new java.awt.Dimension(34, 34));
        jButtonResta.setPreferredSize(new java.awt.Dimension(34, 34));
        jButtonResta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRestaActionPerformed(evt);
            }
        });
        jPanelBinarios.add(jButtonResta);

        jToolBarHerramientasImagen.add(jPanelBinarios);

        jPanelUmbralizacion.setBorder(javax.swing.BorderFactory.createTitledBorder("Umbralización"));
        jPanelUmbralizacion.setToolTipText("Umbralización");
        jPanelUmbralizacion.setMaximumSize(new java.awt.Dimension(180, 70));
        jPanelUmbralizacion.setMinimumSize(new java.awt.Dimension(180, 70));
        jPanelUmbralizacion.setPreferredSize(new java.awt.Dimension(180, 70));
        jPanelUmbralizacion.setLayout(new javax.swing.BoxLayout(jPanelUmbralizacion, javax.swing.BoxLayout.LINE_AXIS));

        jSliderUmbralizacion.setMaximum(255);
        jSliderUmbralizacion.setToolTipText("Umbralización");
        jSliderUmbralizacion.setValue(128);
        jSliderUmbralizacion.setMaximumSize(new java.awt.Dimension(150, 16));
        jSliderUmbralizacion.setMinimumSize(new java.awt.Dimension(150, 16));
        jSliderUmbralizacion.setPreferredSize(new java.awt.Dimension(150, 16));
        jSliderUmbralizacion.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderUmbralizacionStateChanged(evt);
            }
        });
        jSliderUmbralizacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSliderUmbralizacionFocusLost(evt);
            }
        });
        jPanelUmbralizacion.add(jSliderUmbralizacion);

        jToolBarHerramientasImagen.add(jPanelUmbralizacion);

        jPanelInferior.add(jToolBarHerramientasImagen, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanelInferior, java.awt.BorderLayout.PAGE_END);

        jDesktopPaneEscritorio.setToolTipText("Escritorio");
        getContentPane().add(jDesktopPaneEscritorio, java.awt.BorderLayout.CENTER);

        jMenuBarMenu.setToolTipText("Menú");

        jMenuArchivo.setText("Archivo");
        jMenuArchivo.setToolTipText("Archivo");

        jMenuItemNuevo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
        jMenuItemNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevo.png"))); // NOI18N
        jMenuItemNuevo.setText("Nuevo");
        jMenuItemNuevo.setToolTipText("Nuevo");
        jMenuItemNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNuevoActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemNuevo);

        jMenuItemAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        jMenuItemAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/abrir.png"))); // NOI18N
        jMenuItemAbrir.setText("Abrir");
        jMenuItemAbrir.setToolTipText("Abrir");
        jMenuItemAbrir.setPreferredSize(new java.awt.Dimension(193, 25));
        jMenuItemAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAbrirActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemAbrir);

        jMenuItemGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK));
        jMenuItemGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardar.png"))); // NOI18N
        jMenuItemGuardar.setText("Guardar");
        jMenuItemGuardar.setToolTipText("Guardar");
        jMenuItemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGuardarActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemGuardar);

        jMenuItemDuplicar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        jMenuItemDuplicar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/duplicar.png"))); // NOI18N
        jMenuItemDuplicar.setText("Duplicar");
        jMenuItemDuplicar.setToolTipText("Duplicar");
        jMenuItemDuplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDuplicarActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemDuplicar);

        jMenuBarMenu.add(jMenuArchivo);

        jMenuVer.setText("Ver");
        jMenuVer.setToolTipText("Ver");

        jCheckBoxMenuItemBarradeEstado.setSelected(true);
        jCheckBoxMenuItemBarradeEstado.setText("Barra de estado");
        jCheckBoxMenuItemBarradeEstado.setToolTipText("Barra de estado");
        jCheckBoxMenuItemBarradeEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemBarradeEstadoActionPerformed(evt);
            }
        });
        jMenuVer.add(jCheckBoxMenuItemBarradeEstado);

        jCheckBoxMenuItemBarradeFormas.setSelected(true);
        jCheckBoxMenuItemBarradeFormas.setText("Barra de formas");
        jCheckBoxMenuItemBarradeFormas.setToolTipText("Barra de formas");
        jCheckBoxMenuItemBarradeFormas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemBarradeFormasActionPerformed(evt);
            }
        });
        jMenuVer.add(jCheckBoxMenuItemBarradeFormas);

        jCheckBoxMenuItemBarraAtributos.setSelected(true);
        jCheckBoxMenuItemBarraAtributos.setText("Barra de atributos");
        jCheckBoxMenuItemBarraAtributos.setToolTipText("Barra de atributos");
        jCheckBoxMenuItemBarraAtributos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemBarraAtributosActionPerformed(evt);
            }
        });
        jMenuVer.add(jCheckBoxMenuItemBarraAtributos);

        jMenuBarMenu.add(jMenuVer);

        jMenuEditar.setText("Editar");
        jMenuEditar.setToolTipText("Editar");

        jMenuItemDeshacer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemDeshacer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Deshacer.png"))); // NOI18N
        jMenuItemDeshacer.setText("Deshacer");
        jMenuItemDeshacer.setToolTipText("Deshacer");
        jMenuItemDeshacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeshacerActionPerformed(evt);
            }
        });
        jMenuEditar.add(jMenuItemDeshacer);

        jMenuItemRehacer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemRehacer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Rehacer.png"))); // NOI18N
        jMenuItemRehacer.setText("Rehacer");
        jMenuItemRehacer.setToolTipText("Rehacer");
        jMenuItemRehacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRehacerActionPerformed(evt);
            }
        });
        jMenuEditar.add(jMenuItemRehacer);

        jMenuBarMenu.add(jMenuEditar);

        jMenuOpciones.setText("Opciones");
        jMenuOpciones.setToolTipText("Opciones");

        jMenuItemTamLienzo.setText("Tamaño Lienzo");
        jMenuItemTamLienzo.setToolTipText("Tamaño Lienzo");
        jMenuItemTamLienzo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTamLienzoActionPerformed(evt);
            }
        });
        jMenuOpciones.add(jMenuItemTamLienzo);

        jMenuBarMenu.add(jMenuOpciones);

        jMenuAyuda.setText("Ayuda");
        jMenuAyuda.setToolTipText("Ayuda");

        jMenuItemAcercaDe.setText("Acerca De");
        jMenuItemAcercaDe.setToolTipText("Acerca De");
        jMenuItemAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcercaDeActionPerformed(evt);
            }
        });
        jMenuAyuda.add(jMenuItemAcercaDe);

        jMenuBarMenu.add(jMenuAyuda);

        setJMenuBar(jMenuBarMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNuevoActionPerformed
        this.nuevo();
    }//GEN-LAST:event_jMenuItemNuevoActionPerformed

    private void jCheckBoxMenuItemBarradeEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemBarradeEstadoActionPerformed
        if (jCheckBoxMenuItemBarradeEstado.isSelected()) {
            jPanelBarrasEstado.setVisible(true);
        } else {
            jPanelBarrasEstado.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxMenuItemBarradeEstadoActionPerformed

    private void jToggleButtonPuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonPuntoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) vi.getLienzo().setTipoDibujo(TipoDibujo.PUNTO);
            cbListaFigurasSelected = false;

            jLabelEstado.setText("Punto");
        }
    }//GEN-LAST:event_jToggleButtonPuntoActionPerformed

    private void jToggleButtonLineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonLineaActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) vi.getLienzo().setTipoDibujo(TipoDibujo.LINEA);
            cbListaFigurasSelected = false;

            jLabelEstado.setText("Línea");
        }
    }//GEN-LAST:event_jToggleButtonLineaActionPerformed

    private void jToggleButtonRectanguloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonRectanguloActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) vi.getLienzo().setTipoDibujo(TipoDibujo.RECTANGULO);
            cbListaFigurasSelected = false;

            jLabelEstado.setText("Rectángulo");
        }
    }//GEN-LAST:event_jToggleButtonRectanguloActionPerformed

    private void jToggleButtonElipseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonElipseActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) vi.getLienzo().setTipoDibujo(TipoDibujo.ELIPSE);
            cbListaFigurasSelected = false;

            jLabelEstado.setText("Elipse");
        }
    }//GEN-LAST:event_jToggleButtonElipseActionPerformed

    private void jMenuItemAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAbrirActionPerformed
        this.abrir();
    }//GEN-LAST:event_jMenuItemAbrirActionPerformed

    private void jMenuItemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGuardarActionPerformed
        this.guardar();
    }//GEN-LAST:event_jMenuItemGuardarActionPerformed

    private void jComboBoxListaFuentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxListaFuentesActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            vi.getLienzo().getAtributos().setTipoFuente((String) this.jComboBoxListaFuentes.getSelectedItem());
            vi.getLienzo().cambiarFuente(jComboBoxListaFuentes.getSelectedIndex());
        }
    }//GEN-LAST:event_jComboBoxListaFuentesActionPerformed

    private void jToggleButtonMoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonMoverActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();        
            if (vi != null) {
                if (jToggleButtonMover.isSelected()) {
                    vi.getLienzo().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                    vi.getLienzo().setModoEditar(ModoEditar.MOVER);
                }
                else {
                    vi.getLienzo().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    vi.getLienzo().setModoEditar(ModoEditar.NULL);
                }
            }
        }
    }//GEN-LAST:event_jToggleButtonMoverActionPerformed

    private void jButtonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoActionPerformed
        this.nuevo();
    }//GEN-LAST:event_jButtonNuevoActionPerformed

    private void jButtonAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAbrirActionPerformed
        this.abrir();
    }//GEN-LAST:event_jButtonAbrirActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        this.guardar();
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jCheckBoxMenuItemBarradeFormasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemBarradeFormasActionPerformed
        if (jCheckBoxMenuItemBarradeFormas.isSelected()) {
            jPanelFiguras.setVisible(true);
            jPanelEditar.setVisible(true);
        } else {
            jPanelFiguras.setVisible(false);
            jPanelEditar.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxMenuItemBarradeFormasActionPerformed

    private void jCheckBoxMenuItemBarraAtributosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemBarraAtributosActionPerformed
        if (jCheckBoxMenuItemBarraAtributos.isSelected()) {
            jPanelTrazo.setVisible(true);
            jPanelRelleno.setVisible(true);
            jSliderTransparencia.setVisible(true);
            jToggleButtonAlisar.setVisible(true);
            jComboBoxListaFuentes.setVisible(true);
        } else {
            jPanelTrazo.setVisible(false);
            jPanelRelleno.setVisible(false);
            jSliderTransparencia.setVisible(false);
            jToggleButtonAlisar.setVisible(false);
            jComboBoxListaFuentes.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxMenuItemBarraAtributosActionPerformed

    private void jComboBoxColorTrazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxColorTrazoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            switch (jComboBoxColorTrazo.getSelectedIndex()) {
                case (int) 0: vi.getLienzo().getAtributos().setColorTrazo(Color.BLACK); break;
                case (int) 1: vi.getLienzo().getAtributos().setColorTrazo(Color.RED); break;
                case (int) 2: vi.getLienzo().getAtributos().setColorTrazo(Color.BLUE); break;
                case (int) 3: vi.getLienzo().getAtributos().setColorTrazo(Color.WHITE); break;
                case (int) 4: vi.getLienzo().getAtributos().setColorTrazo(Color.YELLOW); break;
                case (int) 5: vi.getLienzo().getAtributos().setColorTrazo(Color.GREEN); break;
            }

            vi.getLienzo().aplicarAtributos();
        }
    }//GEN-LAST:event_jComboBoxColorTrazoActionPerformed

    private void jSpinnerGrosorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerGrosorStateChanged
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            vi.getLienzo().getAtributos().setTamTrazo((int) jSpinnerGrosor.getValue());
            vi.getLienzo().aplicarAtributos();
        }
    }//GEN-LAST:event_jSpinnerGrosorStateChanged

    private void jToggleButtonAlisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonAlisarActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            if (vi != null) {
                if (jToggleButtonAlisar.isSelected()) vi.getLienzo().getAtributos().setAlisar(true);
                else vi.getLienzo().getAtributos().setAlisar(false);
                vi.getLienzo().aplicarAtributos();
            }
        }
    }//GEN-LAST:event_jToggleButtonAlisarActionPerformed

    private void jSliderBrilloStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderBrilloStateChanged
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().setBrillo(jSliderBrillo.getValue());
                        this.repaint();
                    } catch(IllegalArgumentException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jSliderBrilloStateChanged

    private void jSliderBrilloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSliderBrilloFocusLost
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                vi.getLienzo().setCopia(vi.getLienzo().copiaImagen(vi.getLienzo().getImagen(false)));
                vi.getLienzo().setBrilloCopiaRotacion();
            }
        }
    }//GEN-LAST:event_jSliderBrilloFocusLost

    private void jComboBoxFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFiltroActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getCopia() != null) {
                    try {
                        switch (jComboBoxFiltro.getSelectedIndex()) {
                            case 0: vi.getLienzo().setFiltro(TipoTransformacion.MEDIA); break;
                            case 1: vi.getLienzo().setFiltro(TipoTransformacion.BINOMIAL); break;
                            case 2: vi.getLienzo().setFiltro(TipoTransformacion.ENFOQUE); break;
                            case 3: vi.getLienzo().setFiltro(TipoTransformacion.RELIEVE); break;
                            case 4: vi.getLienzo().setFiltro(TipoTransformacion.LAPLACIANO); break;
                        }
                        this.repaint();
                    } catch(IllegalArgumentException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jComboBoxFiltroActionPerformed

    private void jComboBoxRellenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxRellenoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            if (vi != null) {
                switch (jComboBoxRelleno.getSelectedIndex()) {
                    case 0:
                        vi.getLienzo().getAtributos().setTipoRelleno(TipoRelleno.NO_RELLENAR); vi.getLienzo().setIndice_TipoRelleno(0);
                        this.jPanelDegradado.setVisible(false);
                    break;
                    case 1:
                        vi.getLienzo().getAtributos().setTipoRelleno(TipoRelleno.COLOR_LISO); vi.getLienzo().setIndice_TipoRelleno(1);
                        this.jPanelDegradado.setVisible(false);
                    break;
                    case 2:
                        vi.getLienzo().getAtributos().setTipoRelleno(TipoRelleno.DEGRADADO); vi.getLienzo().setIndice_TipoRelleno(2);
                        this.jPanelDegradado.setVisible(true);
                    break;
                }
                vi.getLienzo().aplicarAtributos();
            }
        }
    }//GEN-LAST:event_jComboBoxRellenoActionPerformed

    private void jSliderTransparenciaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderTransparenciaStateChanged
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            if (vi != null) {
                vi.getLienzo().getAtributos().setTransparencia((float) jSliderTransparencia.getValue()/100);
                vi.getLienzo().aplicarAtributos();
            }
        }
    }//GEN-LAST:event_jSliderTransparenciaStateChanged

    private void jComboBoxColorRellenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxColorRellenoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            switch (jComboBoxColorRelleno.getSelectedIndex()) {
                case (int) 0: vi.getLienzo().getAtributos().setColorRelleno(Color.BLACK); break;
                case (int) 1: vi.getLienzo().getAtributos().setColorRelleno(Color.RED); break;
                case (int) 2: vi.getLienzo().getAtributos().setColorRelleno(Color.BLUE); break;
                case (int) 3: vi.getLienzo().getAtributos().setColorRelleno(Color.WHITE); break;
                case (int) 4: vi.getLienzo().getAtributos().setColorRelleno(Color.YELLOW); break;
                case (int) 5: vi.getLienzo().getAtributos().setColorRelleno(Color.GREEN); break;
            }

            vi.getLienzo().aplicarAtributos();
        }
    }//GEN-LAST:event_jComboBoxColorRellenoActionPerformed

    private void jToggleButtonLineaDiscontinuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonLineaDiscontinuaActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            if (vi != null) {
                if (jToggleButtonLineaDiscontinua.isSelected()) {
                    vi.getLienzo().getAtributos().setTrazoDiscontinuo(true);
                } else {
                    vi.getLienzo().getAtributos().setTrazoDiscontinuo(false);
                }
                vi.getLienzo().aplicarAtributos();
            }
        }
    }//GEN-LAST:event_jToggleButtonLineaDiscontinuaActionPerformed

    private void jButtonPaletaTrazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPaletaTrazoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            if (vi != null) {
                JColorChooser colorChooser = new JColorChooser();
                Color c = colorChooser.showDialog(null, "Seleccione un color", null);
                if (c != null) {
                    vi.getLienzo().getAtributos().setColorTrazo(c);
                    vi.getLienzo().aplicarAtributos();
                    jButtonPaletaTrazo.setBackground(c);
                }
            }
        }
    }//GEN-LAST:event_jButtonPaletaTrazoActionPerformed

    private void jButtonPaletaRellenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPaletaRellenoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            if (vi != null) {
                JColorChooser colorChooser = new JColorChooser();
                Color c = colorChooser.showDialog(null, "Seleccione un color", null);
                if (c != null) {
                    vi.getLienzo().getAtributos().setColorRelleno(c);
                    vi.getLienzo().aplicarAtributos();
                    jButtonPaletaRelleno.setBackground(c);
                }
            }
        }
    }//GEN-LAST:event_jButtonPaletaRellenoActionPerformed

    private void jToggleButtonRectanguloRedondeadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonRectanguloRedondeadoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) vi.getLienzo().setTipoDibujo(TipoDibujo.REC_REDONDEADO);
            cbListaFigurasSelected = false;

            jLabelEstado.setText("Rectangulo Redondeado");
        }
    }//GEN-LAST:event_jToggleButtonRectanguloRedondeadoActionPerformed

    private void jToggleButtonTrazoLibreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonTrazoLibreActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) vi.getLienzo().setTipoDibujo(TipoDibujo.TRAZO_LIBRE);
            cbListaFigurasSelected = false;

            jLabelEstado.setText("Trazo Libre");
        }
    }//GEN-LAST:event_jToggleButtonTrazoLibreActionPerformed

    private void jToggleButtonArcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonArcoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) vi.getLienzo().setTipoDibujo(TipoDibujo.ARCO);
            cbListaFigurasSelected = false;

            jLabelEstado.setText("Arco");
        }
    }//GEN-LAST:event_jToggleButtonArcoActionPerformed

    private void jToggleButtonQuadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonQuadActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) vi.getLienzo().setTipoDibujo(TipoDibujo.QUAD);
            cbListaFigurasSelected = false;

            jLabelEstado.setText("Quad");
        }
    }//GEN-LAST:event_jToggleButtonQuadActionPerformed

    private void jToggleButtonCubicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonCubicActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) vi.getLienzo().setTipoDibujo(TipoDibujo.CUBIC);
            cbListaFigurasSelected = false;

            jLabelEstado.setText("Cubic");
        }
    }//GEN-LAST:event_jToggleButtonCubicActionPerformed

    private void jToggleButtonBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonBorrarActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (jToggleButtonBorrar.isSelected()) {
                    vi.getLienzo().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    vi.getLienzo().setModoEditar(ModoEditar.BORRAR);
                }
                else {
                    vi.getLienzo().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    vi.getLienzo().setModoEditar(ModoEditar.NULL);
                }
            }
        }
    }//GEN-LAST:event_jToggleButtonBorrarActionPerformed

    private void jComboBoxTipoDegradadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipoDegradadoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            if (vi != null) {
                switch (jComboBoxTipoDegradado.getSelectedIndex()) {
                    case 0: vi.getLienzo().getAtributos().setDireccion(true); break;
                    case 1: vi.getLienzo().getAtributos().setDireccion(false); break;
                }
                vi.getLienzo().aplicarAtributos();
            }
        }
    }//GEN-LAST:event_jComboBoxTipoDegradadoActionPerformed

    private void jComboBoxColorDegradadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxColorDegradadoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            switch (jComboBoxColorDegradado.getSelectedIndex()) {
                case (int) 0: vi.getLienzo().getAtributos().setColorDegradado(Color.BLACK); break;
                case (int) 1: vi.getLienzo().getAtributos().setColorDegradado(Color.RED); break;
                case (int) 2: vi.getLienzo().getAtributos().setColorDegradado(Color.BLUE); break;
                case (int) 3: vi.getLienzo().getAtributos().setColorDegradado(Color.WHITE); break;
                case (int) 4: vi.getLienzo().getAtributos().setColorDegradado(Color.YELLOW); break;
                case (int) 5: vi.getLienzo().getAtributos().setColorDegradado(Color.GREEN); break;
            }

            vi.getLienzo().aplicarAtributos();
        }
    }//GEN-LAST:event_jComboBoxColorDegradadoActionPerformed

    private void jButtonPaletaDegradadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPaletaDegradadoActionPerformed
        JColorChooser colorChooser = new JColorChooser();
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            if (vi != null) {
                Color c = colorChooser.showDialog(null, "Seleccione un color", null);
                if (c != null) {
                    vi.getLienzo().getAtributos().setColorDegradado(c);
                    vi.getLienzo().aplicarAtributos();
                    jButtonPaletaDegradado.setBackground(c);
                }
            }
        }
    }//GEN-LAST:event_jButtonPaletaDegradadoActionPerformed

    private void jMenuItemDeshacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeshacerActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            if (vi != null) {
                vi.getLienzo().borrarUltimaForma();
                vi.setNombreFormas();
            }
        }
    }//GEN-LAST:event_jMenuItemDeshacerActionPerformed

    private void jMenuItemRehacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRehacerActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();

            if (vi != null) {
                vi.getLienzo().rehacer();
                vi.setNombreFormas();
            }
        }
    }//GEN-LAST:event_jMenuItemRehacerActionPerformed

    private void jToggleButtonSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSeleccionarActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();        
            if (vi != null) {
                if (jToggleButtonSeleccionar.isSelected()) {
                    vi.getLienzo().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    vi.getLienzo().setModoEditar(ModoEditar.SELECCIONAR);
                }
                else {
                    vi.getLienzo().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    vi.getLienzo().setModoEditar(ModoEditar.NULL);
                }
            }
        }
    }//GEN-LAST:event_jToggleButtonSeleccionarActionPerformed

    private void jButtonAumentarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAumentarActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().escalarImagen(TipoEscalado.AUMENTAR);
                        vi.getLienzo().aplicarTransformaciones();
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonAumentarActionPerformed

    private void jButtonDisminuirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisminuirActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().escalarImagen(TipoEscalado.DISMINUIR);
                        vi.getLienzo().aplicarTransformaciones();
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonDisminuirActionPerformed

    private void jButtonContrasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonContrasteActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().setContraste(TipoTransformacion.CONTRASTE);
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonContrasteActionPerformed

    private void jButtonIluminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIluminadoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().setContraste(TipoTransformacion.ILUMINAR);
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonIluminadoActionPerformed

    private void jButtonOscurecidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOscurecidoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().setContraste(TipoTransformacion.OSCURECER);
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonOscurecidoActionPerformed

    private void jButtonRotacion90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotacion90ActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().rotarImagen(90);
                        vi.getLienzo().aplicarTransformaciones();
                        jSliderRotacion.setValue(90);
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonRotacion90ActionPerformed

    private void jButtonRotacion180ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotacion180ActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().rotarImagen(180);
                        vi.getLienzo().aplicarTransformaciones();
                        jSliderRotacion.setValue(180);
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonRotacion180ActionPerformed

    private void jButtonRotacion270ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotacion270ActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().rotarImagen(270);
                        vi.getLienzo().aplicarTransformaciones();
                        jSliderRotacion.setValue(270);
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonRotacion270ActionPerformed

    private void jSliderRotacionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderRotacionStateChanged
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().rotarImagen(jSliderRotacion.getValue());
                        vi.getLienzo().aplicarTransformaciones();
                        this.repaint();
                    } catch(IllegalArgumentException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jSliderRotacionStateChanged

    private void jButtonSinusoidalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSinusoidalActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().sinusoidal();
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonSinusoidalActionPerformed

    private void jButtonNegativoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNegativoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().setContraste(TipoTransformacion.NEGATIVO);
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonNegativoActionPerformed

    private void jButtonDuplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDuplicarActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen original = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            this.duplicado(original, original.getLienzo().getImagen(true), "Copia");
        }
    }//GEN-LAST:event_jButtonDuplicarActionPerformed

    private void jMenuItemDuplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDuplicarActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen original = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            this.duplicado(original, original.getLienzo().getImagen(true), "Copia");
        }
    }//GEN-LAST:event_jMenuItemDuplicarActionPerformed

    private void jButtonBandasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBandasActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        VentanaInternaLienzoImagen original = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
                        for (int i = 0; i < 3; ++i) {
                            BufferedImage banda = vi.getLienzo().getBanda(i);
                            this.duplicado(original, banda, Integer.toString(i));
                        }
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonBandasActionPerformed

    private void jComboBoxEspacioColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxEspacioColorActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        VentanaInternaLienzoImagen original = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
                        switch (jComboBoxEspacioColor.getSelectedIndex()) {
                            case 0:
                                this.duplicado(original, vi.getLienzo().cambiarEspacioColor("RGB"), "RGB");
                            break;
                            case 1:
                                this.duplicado(original, vi.getLienzo().cambiarEspacioColor("YCC"), "YCC");
                            break;
                            case 2:
                                this.duplicado(original, vi.getLienzo().cambiarEspacioColor("GREY"), "GREY");
                            break;
                        }
                    } catch(IllegalArgumentException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jComboBoxEspacioColorActionPerformed

    private void jButtonTintadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTintadoActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().tintado(vi.getLienzo().getAtributos().getColorTrazo());
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonTintadoActionPerformed

    private void jButtonEcualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEcualizarActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().ecualizar();
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonEcualizarActionPerformed

    private void jButtonSepiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSepiaActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().sepia();
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonSepiaActionPerformed

    private void jSliderUmbralizacionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderUmbralizacionStateChanged
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().umbralizacion(jSliderUmbralizacion.getValue());
                        this.repaint();
                    } catch(IllegalArgumentException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jSliderUmbralizacionStateChanged

    private void jSliderUmbralizacionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSliderUmbralizacionFocusLost
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                vi.getLienzo().setCopia(vi.getLienzo().copiaImagen(vi.getLienzo().getImagen(false)));
                vi.getLienzo().umbralizacionCopiaRotacion(jSliderUmbralizacion.getValue());
            }
        }
    }//GEN-LAST:event_jSliderUmbralizacionFocusLost

    private void jButtonSumaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSumaActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                VentanaInternaLienzoImagen viNext = (VentanaInternaLienzoImagen) jDesktopPaneEscritorio.selectFrame(false);
                if (viNext != null) {
                    BufferedImage imgSuma = viNext.getLienzo().getImagen(false);
                    if (vi.getLienzo().getImagen(false) != null && imgSuma != null) {
                        try {
                            this.duplicado(viNext, vi.getLienzo().suma(imgSuma), "SUMA");
                        } catch (IllegalArgumentException e) {
                            System.err.println("Error: "+e.getLocalizedMessage());
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonSumaActionPerformed

    private void jButtonRestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRestaActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                VentanaInternaLienzoImagen viNext = (VentanaInternaLienzoImagen) jDesktopPaneEscritorio.selectFrame(false);
                if (viNext != null) {
                    BufferedImage imgResta = viNext.getLienzo().getImagen(false);
                    if (vi.getLienzo().getImagen(false) != null && imgResta != null) {
                        try {
                            this.duplicado(viNext, vi.getLienzo().resta(imgResta), "RESTA");
                        } catch (IllegalArgumentException e) {
                            System.err.println("Error: "+e.getLocalizedMessage());
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonRestaActionPerformed

    private void jButtonSobelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSobelActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().sobel();
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonSobelActionPerformed

    private void jButtonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayActionPerformed
        File f = (File) jComboListaReproduccion.getSelectedItem();
        
        if (f.getAbsolutePath().endsWith("wav") || f.getAbsolutePath().endsWith("au")) {
            if (player.getSong() == null) {
                player.setSong((File) jComboListaReproduccion.getSelectedItem());
            }
            else if (!player.getSong().getName().equals(f.getName())) {
                player.stop();
                player.setSong((File) jComboListaReproduccion.getSelectedItem());
            }
            player.play();
        }
        
        boolean video_done = false;
        try {
            VentanaInternaVLCPlayer vlc = (VentanaInternaVLCPlayer) this.jDesktopPaneEscritorio.getSelectedFrame();
            VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (v.getTipo().equals(TipoVentanaInterna.VLCPLAYER) && !vlc.isPlaying()) {
                vlc.play();
                video_done = true;
            }
        } catch (Exception ex) {
            System.err.println("La ventana no es de tipo VLC");
        }
        if ((f.getAbsolutePath().endsWith("mp4") || f.getAbsolutePath().endsWith("avi")) && !video_done) {
            VentanaInternaVLCPlayer vlc = VentanaInternaVLCPlayer.getInstance(f);
            jDesktopPaneEscritorio.add(vlc);
            vlc.setLocation(posx_ventanaInterna, posy_ventanaInterna);
            vlc.setSize(600, 400);
            vlc.setVisible(true);
            vlc.setTitle(f.getName());
            vlc.play();
        }
    }//GEN-LAST:event_jButtonPlayActionPerformed

    private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopActionPerformed
        if (player.getSong() == jComboListaReproduccion.getSelectedItem()) player.stop();
        
        recorder.parar();
        
        if (!recorder.isRecording() && !recorder.isSongAdded()) {
            jComboListaReproduccion.addItem(recorder.getAudio());
            recorder.setSongAdded(true);
        }
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (this.jDesktopPaneEscritorio.getSelectedFrame() != null && v.getTipo().equals(TipoVentanaInterna.VLCPLAYER)) {
            VentanaInternaVLCPlayer vlc = (VentanaInternaVLCPlayer) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vlc.getfMedia() == jComboListaReproduccion.getSelectedItem()) vlc.stop();
        }
    }//GEN-LAST:event_jButtonStopActionPerformed

    private void jButtonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPauseActionPerformed
        if (player.getSong() == jComboListaReproduccion.getSelectedItem()) player.pause();
        
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (this.jDesktopPaneEscritorio.getSelectedFrame() != null && v.getTipo().equals(TipoVentanaInterna.VLCPLAYER)) {
            VentanaInternaVLCPlayer vlc = (VentanaInternaVLCPlayer) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vlc.getfMedia() == jComboListaReproduccion.getSelectedItem()) vlc.pause();
        }
    }//GEN-LAST:event_jButtonPauseActionPerformed

    private void jButtonGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGrabarActionPerformed
        JFileChooser dlg = new JFileChooser();
        dlg.setFileFilter(extensionWAV);
        dlg.addChoosableFileFilter(extensionAU);
        int resp = dlg.showSaveDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            try {
                File f = new File(dlg.getSelectedFile() + "." + dlg.getFileFilter().getDescription());
                recorder.setAudio(f);
                recorder.grabar();
            } catch (Exception ex) {
                System.err.println("Error al crear el audio");
            }
        }
    }//GEN-LAST:event_jButtonGrabarActionPerformed

    private void jMenuItemTamLienzoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTamLienzoActionPerformed
        tamLienzo.obtenerDimensiones();
    }//GEN-LAST:event_jMenuItemTamLienzoActionPerformed

    private void jMenuItemAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcercaDeActionPerformed
        JOptionPane.showMessageDialog(null, "Aplicación Multimedia\nDesarrollado por: David Infante Casas\nVer. 1.1", "Acerca De", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItemAcercaDeActionPerformed

    private void jComboBoxListaFigurasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxListaFigurasActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null && cbListaFigurasSelected) {
                jToggleButtonSeleccionar.setSelected(true);
                vi.getLienzo().setModoEditar(ModoEditar.SELECCIONAR);
                AtrShape formaSeleccionada = vi.getLienzo().getFormaPorNombre(jComboBoxListaFiguras.getSelectedItem().toString());
                if (formaSeleccionada != null) {
                    vi.getLienzo().setFormaSeleccionada(formaSeleccionada);
                    vi.getLienzo().setAtributos(formaSeleccionada.getAtributos());
                    vi.setEstadoBotonesFigura(formaSeleccionada);
                    this.repaint();
                }
            }
        }
    }//GEN-LAST:event_jComboBoxListaFigurasActionPerformed

    private void jTextFieldPosicionXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPosicionXActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                jToggleButtonMover.setSelected(true);
                vi.getLienzo().setModoEditar(ModoEditar.MOVER);
                if (vi.getLienzo().getFormaSeleccionada() != null) {
                    int posx = Integer.parseInt(jTextFieldPosicionX.getText());
                    Point p = new Point(posx, (int) vi.getLienzo().getFormaSeleccionada().getY());
                    vi.getLienzo().getFormaSeleccionada().setLocation(p);
                }
            }
        }
    }//GEN-LAST:event_jTextFieldPosicionXActionPerformed

    private void jTextFieldPosicionYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPosicionYActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                jToggleButtonMover.setSelected(true);
                vi.getLienzo().setModoEditar(ModoEditar.MOVER);
                if (vi.getLienzo().getFormaSeleccionada() != null) {
                    int posy = Integer.parseInt(jTextFieldPosicionY.getText());
                    Point p = new Point((int) vi.getLienzo().getFormaSeleccionada().getX(), posy);
                    vi.getLienzo().getFormaSeleccionada().setLocation(p);
                }
            }
        }
    }//GEN-LAST:event_jTextFieldPosicionYActionPerformed

    private void jButtonIntercambioColoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIntercambioColoresActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().intercambioColores();
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonIntercambioColoresActionPerformed

    private void jButtonAleatorioOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAleatorioOpActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().operadorAleatorioOp();
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonAleatorioOpActionPerformed

    private void jButtonEscalaGrisesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEscalaGrisesActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.LIENZO)) {
            VentanaInternaLienzoImagen vi = (VentanaInternaLienzoImagen) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vi != null) {
                if (vi.getLienzo().getImagen(false) != null) {
                    try {
                        vi.getLienzo().escalaGrises();
                        this.repaint();
                    } catch (Exception e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonEscalaGrisesActionPerformed

    private void jButtonWebcamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWebcamActionPerformed
        VentanaInternaCamara vic = VentanaInternaCamara.getInstance();
        jDesktopPaneEscritorio.add(vic);
        vic.setLocation(posx_ventanaInterna, posy_ventanaInterna);
        vic.setVisible(true);
        vic.setTitle("Webcam");
        
        posx_ventanaInterna += 10;
        posy_ventanaInterna += 10;
    }//GEN-LAST:event_jButtonWebcamActionPerformed

    private void jButtonScreenshotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScreenshotActionPerformed
        VentanaInterna v = (VentanaInterna) this.jDesktopPaneEscritorio.getSelectedFrame();
        if (v.getTipo().equals(TipoVentanaInterna.CAMARA)) {
            VentanaInternaCamara vic = (VentanaInternaCamara) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vic != null) {
                BufferedImage img = vic.screenshot();
                if (img != null) {
                    VentanaInternaLienzoImagen vi = new VentanaInternaLienzoImagen(this);
                    vi.getLienzo().setImagen(img);
                    this.jDesktopPaneEscritorio.add(vi);
                    vi.getLienzo().setNombre(vic.getTitle() + " - Captura");
                    vi.getLienzo().setExtension(".png");
                    vi.setTitle(vi.getLienzo().getNombre());
                    vi.setVisible(true);
                }
            }
        } else if (v.getTipo().equals(TipoVentanaInterna.VLCPLAYER)) {
            VentanaInternaVLCPlayer vlcp = (VentanaInternaVLCPlayer) this.jDesktopPaneEscritorio.getSelectedFrame();
            if (vlcp != null) {
                BufferedImage img = vlcp.screenshot();
                if (img != null) {
                    VentanaInternaLienzoImagen vi = new VentanaInternaLienzoImagen(this);
                    vi.getLienzo().setImagen(img);
                    this.jDesktopPaneEscritorio.add(vi);
                    vi.getLienzo().setNombre(vlcp.getTitle() + " - Captura");
                    vi.getLienzo().setExtension(".png");
                    vi.setTitle(vi.getLienzo().getNombre());
                    vi.setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_jButtonScreenshotActionPerformed

    private void jComboBoxListaFigurasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxListaFigurasFocusGained
        cbListaFigurasSelected = true;
    }//GEN-LAST:event_jComboBoxListaFigurasFocusGained

    private void jComboBoxListaFigurasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxListaFigurasFocusLost
        cbListaFigurasSelected = false;
    }//GEN-LAST:event_jComboBoxListaFigurasFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupFormasYEditar;
    private javax.swing.JButton jButtonAbrir;
    private javax.swing.JButton jButtonAleatorioOp;
    private javax.swing.JButton jButtonAumentar;
    private javax.swing.JButton jButtonBandas;
    private javax.swing.JButton jButtonContraste;
    private javax.swing.JButton jButtonDisminuir;
    private javax.swing.JButton jButtonDuplicar;
    private javax.swing.JButton jButtonEcualizar;
    private javax.swing.JButton jButtonEscalaGrises;
    private javax.swing.JButton jButtonGrabar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonIluminado;
    private javax.swing.JButton jButtonIntercambioColores;
    private javax.swing.JButton jButtonNegativo;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JButton jButtonOscurecido;
    private javax.swing.JButton jButtonPaletaDegradado;
    private javax.swing.JButton jButtonPaletaRelleno;
    private javax.swing.JButton jButtonPaletaTrazo;
    private javax.swing.JButton jButtonPause;
    private javax.swing.JButton jButtonPlay;
    private javax.swing.JButton jButtonResta;
    private javax.swing.JButton jButtonRotacion180;
    private javax.swing.JButton jButtonRotacion270;
    private javax.swing.JButton jButtonRotacion90;
    private javax.swing.JButton jButtonScreenshot;
    private javax.swing.JButton jButtonSepia;
    private javax.swing.JButton jButtonSinusoidal;
    private javax.swing.JButton jButtonSobel;
    private javax.swing.JButton jButtonStop;
    private javax.swing.JButton jButtonSuma;
    private javax.swing.JButton jButtonTintado;
    private javax.swing.JButton jButtonWebcam;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemBarraAtributos;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemBarradeEstado;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemBarradeFormas;
    private javax.swing.JComboBox jComboBoxColorDegradado;
    private javax.swing.JComboBox jComboBoxColorRelleno;
    private javax.swing.JComboBox jComboBoxColorTrazo;
    private javax.swing.JComboBox<String> jComboBoxEspacioColor;
    private javax.swing.JComboBox<String> jComboBoxFiltro;
    private javax.swing.JComboBox<String> jComboBoxListaFiguras;
    private javax.swing.JComboBox<String> jComboBoxListaFuentes;
    private javax.swing.JComboBox<String> jComboBoxRelleno;
    private javax.swing.JComboBox<String> jComboBoxTipoDegradado;
    private javax.swing.JComboBox<File> jComboListaReproduccion;
    private javax.swing.JDesktopPane jDesktopPaneEscritorio;
    private javax.swing.JLabel jLabelEstado;
    private javax.swing.JLabel jLabelPosicionCursor;
    private javax.swing.JLabel jLabelPosicionX;
    private javax.swing.JLabel jLabelPosicionY;
    private javax.swing.JMenu jMenuArchivo;
    private javax.swing.JMenu jMenuAyuda;
    private javax.swing.JMenuBar jMenuBarMenu;
    private javax.swing.JMenu jMenuEditar;
    private javax.swing.JMenuItem jMenuItemAbrir;
    private javax.swing.JMenuItem jMenuItemAcercaDe;
    private javax.swing.JMenuItem jMenuItemDeshacer;
    private javax.swing.JMenuItem jMenuItemDuplicar;
    private javax.swing.JMenuItem jMenuItemGuardar;
    private javax.swing.JMenuItem jMenuItemNuevo;
    private javax.swing.JMenuItem jMenuItemRehacer;
    private javax.swing.JMenuItem jMenuItemTamLienzo;
    private javax.swing.JMenu jMenuOpciones;
    private javax.swing.JMenu jMenuVer;
    private javax.swing.JPanel jPanelArchivo;
    private javax.swing.JPanel jPanelBarrasEstado;
    private javax.swing.JPanel jPanelBinarios;
    private javax.swing.JPanel jPanelBrillo;
    private javax.swing.JPanel jPanelColor;
    private javax.swing.JPanel jPanelContraste;
    private javax.swing.JPanel jPanelDegradado;
    private javax.swing.JPanel jPanelEditar;
    private javax.swing.JPanel jPanelEscala;
    private javax.swing.JPanel jPanelFiguras;
    private javax.swing.JPanel jPanelFiltro;
    private javax.swing.JPanel jPanelInferior;
    private javax.swing.JPanel jPanelOperadores;
    private javax.swing.JPanel jPanelRelleno;
    private javax.swing.JPanel jPanelRotacion;
    private javax.swing.JPanel jPanelSonido;
    private javax.swing.JPanel jPanelTrazo;
    private javax.swing.JPanel jPanelUmbralizacion;
    private javax.swing.JSlider jSliderBrillo;
    private javax.swing.JSlider jSliderRotacion;
    private javax.swing.JSlider jSliderTransparencia;
    private javax.swing.JSlider jSliderUmbralizacion;
    private javax.swing.JSpinner jSpinnerGrosor;
    private javax.swing.JTextField jTextFieldPosicionX;
    private javax.swing.JTextField jTextFieldPosicionY;
    private javax.swing.JToggleButton jToggleButtonAlisar;
    private javax.swing.JToggleButton jToggleButtonArco;
    private javax.swing.JToggleButton jToggleButtonBorrar;
    private javax.swing.JToggleButton jToggleButtonCubic;
    private javax.swing.JToggleButton jToggleButtonElipse;
    private javax.swing.JToggleButton jToggleButtonLinea;
    private javax.swing.JToggleButton jToggleButtonLineaDiscontinua;
    private javax.swing.JToggleButton jToggleButtonMover;
    private javax.swing.JToggleButton jToggleButtonPunto;
    private javax.swing.JToggleButton jToggleButtonQuad;
    private javax.swing.JToggleButton jToggleButtonRectangulo;
    private javax.swing.JToggleButton jToggleButtonRectanguloRedondeado;
    private javax.swing.JToggleButton jToggleButtonSeleccionar;
    private javax.swing.JToggleButton jToggleButtonTrazoLibre;
    private javax.swing.JToolBar jToolBarHerramientasImagen;
    private javax.swing.JToolBar jToolBarSuperior;
    // End of variables declaration//GEN-END:variables
}
