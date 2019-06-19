package SM.dic.iu;

import SM.dic.imagen.TipoEscalado;
import SM.dic.imagen.TipoTransformacion;
import SM.dic.imagen.IntercambioColoresOp;
import SM.dic.imagen.EscalaGrisesOp;
import SM.dic.imagen.SepiaOp;
import SM.dic.imagen.SobelOp;
import SM.dic.imagen.UmbralizacionOp;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBuffer;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import sm.image.BlendOp;
import sm.image.EqualizationOp;
import sm.image.KernelProducer;
import sm.image.LookupTableProducer;
import sm.image.SubtractionOp;
import sm.image.TintOp;

/**
 * Lienzo que contiene una imagen y sobre el que dibujar formas geométricas AtrShape
 * @author David Infante Casas
 */
public class LienzoImagen2D extends Lienzo2D {
    /**
     * Imagen del lienzo
     */
    private BufferedImage imagen;
    /**
     * Copia de la imagen del lienzo
     */
    private BufferedImage copia;
    /**
     * Copia de la imagen del lienzo sin aplicarle rotaciones
     */
    private BufferedImage copiaRotacion; //Evitar perder esquinas
    /**
     * Imagen original sobre la que no se aplica ninguna transformación
     */
    private BufferedImage original;
    /**
     * Nombre de la imagen del lienzo
     */
    private String nombre;
    /**
     * Extensión de la imagen del lienzo
     */
    private String extension;
    /**
     * Valor del escalado que tiene la imagen
     */
    private double tamEscalado;
    /**
     * Valor del ángulo de rotación que tiene la imagen
     */
    private int anguloRotacion;
    /**
     * Valor de la función Sinusoidal
     */
    private ByteLookupTable valorSinusoidal;
    /**
     * Valor de la función aleatoria
     */
    private ByteLookupTable valorAleatorio;
    /**
     * Tipo de filtro aplicado a la imagen
     */
    private int filtro;
    /**
     * Valor del contraste aplicado a la imagen
     */
    private int contraste;
    /**
     * Valor de brillo aplicado a la imagen
     */
    private int brillo;
    /**
     * Valor del scale del brillo
     */
    private float[] scale;
    /**
     * Valor del offset del brillo
     */
    private float[] offset;
    /**
     * Ancho de la imagen
     */
    private double width;
    /**
     * Alto de la imagen
     */
    private double height;

    /**
     * Crea un nuevo LienzoImagen2D
     * @param imagen Imagen que contendrá el lienzo
     */
    public void setImagen(BufferedImage imagen) {
        tamEscalado = 1;
        anguloRotacion = 0;
        valorSinusoidal = null;
        valorAleatorio = null;
        filtro = -1;
        contraste = -1;
        brillo = -1;
        scale = null;
        offset = null;
        
        this.imagen = imagen;
        this.copia = this.copiaImagen(this.imagen);
        this.copiaRotacion = this.copiaImagen(this.imagen);
        this.original = this.copiaImagen(this.imagen);
        
        this.extension = null;
        
        if(this.imagen != null) {
            setPreferredSize(new Dimension(this.imagen.getWidth(), this.imagen.getHeight()));
            this.newClipArea(this.imagen.getWidth()+1, this.imagen.getHeight()+1);
            this.setBordeLienzo(this.imagen.getWidth(), this.imagen.getHeight());
        }
        
        width = this.imagen.getWidth();
        height = this.imagen.getHeight();
    }
    
    /**
     * Asigna una copia al atributo copia
     * @param copia Copia a asignar
     */
    public void setCopia(BufferedImage copia) {
        this.copia = copia;
    }
    
    /**
     * Devuelve una imagen con lo que el lienzo contiene
     * @param drawVector Si es true, la imagen contendrá las formas dibujadas
     * @return Devuelve la imagen con lo que el lienzo contiene
     */
    public BufferedImage getImagen(boolean drawVector) {
        if (drawVector) {
            BufferedImage img = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
            paint(img.createGraphics());
            return img;
        } else return imagen;
    }
    
    /**
     * Devuelve la copia de la imagen
     * @return Devuelve la copia de la imagen
     */
    public BufferedImage getCopia() {
        return copia;
    }

    /**
     * Devuelve la imagen original
     * @return Devuelve la imagen original
     */
    public BufferedImage getOriginal() {
        return original;
    }

    /**
     * Devuelve la extensión de la imagen
     * @return Devuelve la extensión de la imagen
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Asigna una extensión a la imagen
     * @param extension Extensión a asignar
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Devuelve el nombre de la imagen
     * @return Devuelve el nombre de la imagen
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna un nombre a la imagen
     * @param nombre Nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Devuelve el ángulo de rotación de la imagen
     * @return Devuelve el ángulo de rotación de la imagen
     */
    public int getAnguloRotacion() {
        return anguloRotacion;
    }

    /**
     * Devuelve el valor del brillo
     * @return Devuelve el valor del brillo
     */
    public int getBrillo() {
        return brillo;
    }

    /**
     * Dibuja la imagen
     * @param g Graphics con los que dibujar
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(imagen != null) g.drawImage(imagen, 0, 0, this);
    }
    
    /**
     * Crea una copia de la imagen img
     * @param img Imagen sobre la que crear la copia
     * @return Devuelve la copia de la imagen img
     */
    public BufferedImage copiaImagen(BufferedImage img) {
        ColorModel cm = img.getColorModel();
        WritableRaster raster = img.copyData(null);
        boolean alfaPre = img.isAlphaPremultiplied();
        return new BufferedImage(cm, raster, alfaPre, null);
    }
    
    /**
     * Devuelve una imagen de la banda i del atributo imagen
     * @param i Número de banda
     * @return Devuelve una imagen de la banda i del atributo imagen
     */
    public BufferedImage getBanda(int i) {
        ColorSpace cs = new sm.image.color.GreyColorSpace();
        ComponentColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        int bandList[] = {i};
        WritableRaster bandRaster = (WritableRaster)imagen.getRaster().createWritableChild(0, 0, imagen.getWidth(), imagen.getHeight(), 0, 0, bandList);
        
        return new BufferedImage(cm, bandRaster, false, null);
    }
    
    /**
     * Cambia el espacio de color del atributo copia
     * @param espacio String con el espacio de color
     * @return Devuelve una nueva imagen con el espacio de color
     */
    public BufferedImage cambiarEspacioColor(String espacio) {
        ColorSpace rgb = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorSpace ycc = new sm.image.color.YCbCrColorSpace();
        ColorSpace grey = new sm.image.color.GreyColorSpace();
        BufferedImage nueva;
        ColorSpace cs = rgb;
        ColorConvertOp cop;
        
        switch (espacio) {
            case "RGB":
                cs = rgb;
            break;
            case "YCC":
                cs = ycc;
            break;
            case "GREY":
                cs = grey;
            break;
        }
        
        if (copia.getColorModel().getColorSpace().isCS_sRGB() && !espacio.equals("RGB")) {
            cop = new ColorConvertOp(cs, null);
            nueva = cop.filter(copia, null);
            return nueva;
        } else if (copia.getColorModel().getColorSpace().equals(cs) && !espacio.equals("YCC")) {
            cop = new ColorConvertOp(cs, null);
            nueva = cop.filter(copia, null);
            return nueva;
        } else if (copia.getColorModel().getColorSpace().equals(cs) && !espacio.equals("GREY")) {
            cop = new ColorConvertOp(cs, null);
            nueva = cop.filter(copia, null);
            return nueva;
        }
        else return null;
    }
    
    /**
     * Aplica la operacón suma sobre el atributo imagen con imgSuma
     * @param imgSuma Imagen a sumar a imagen
     * @return Imagen resultado de aplicar la operación suma
     */
    public BufferedImage suma(BufferedImage imgSuma) {
        BlendOp op = new BlendOp(imgSuma);
        return op.filter(this.imagen, null);
    }
    
    /**
     * Aplica la operacón resta sobre el atributo imagen con imgResta
     * @param imgResta Imagen a restar a imagen
     * @return Imagen resultado de aplicar la operación restas
     */
    public BufferedImage resta(BufferedImage imgResta) {
        SubtractionOp op = new SubtractionOp(imgResta);
        return op.filter(this.imagen, null);
    }

    /**
     * Aplica el operador Seno a la imagen
     */
    public void sinusoidal() {
        double value = 180f/255f;
        byte[] lt = new byte[256];
        double K = 255.0;

        for (int i = 0; i < 256; ++i)
            lt[i] = (byte) (K * Math.abs(Math.sin(Math.toRadians(value * i))));

        valorSinusoidal = new ByteLookupTable(0, lt);
        
        if (valorSinusoidal != null) {
            LookupOp lop = new LookupOp(valorSinusoidal, null);
            lop.filter(this.imagen, this.imagen);
            lop.filter(this.copiaRotacion, this.copiaRotacion);
            this.copia = this.copiaImagen(this.imagen);
            this.repaint();
        }
    }

    /**
     * Aplica el operador Aleatorio a la imagen
     */
    public void operadorAleatorioOp() {
        byte[] lt = new byte[256];

        for (int i = 0; i < 256; ++i)
            lt[i] = (byte) (Math.random()*255);

        valorAleatorio = new ByteLookupTable(0, lt);
        
        if (valorAleatorio != null) {
            LookupOp lop = new LookupOp(valorAleatorio, null);
            lop.filter(this.imagen, this.imagen);
            lop.filter(this.copiaRotacion, this.copiaRotacion);
            this.copia = this.copiaImagen(this.imagen);
            this.repaint();
        }
    }
    
    /**
     * Aplica el operador TintadoOp a la imagen
     * @param color Color del tintado
     */
    public void tintado(Color color) {
        TintOp tintado = new TintOp(color, 0.5f);
        tintado.filter(this.imagen, this.imagen);
        tintado.filter(this.copiaRotacion, this.copiaRotacion);
        this.copia = this.copiaImagen(this.imagen);
        this.repaint();
    }
    
    /**
     * Aplica el operador EqualizationOp a la imagen
     */
    public void ecualizar() {
        EqualizationOp ecualizacion = new EqualizationOp();
        ecualizacion.filter(this.imagen, this.imagen);
        ecualizacion.filter(this.copiaRotacion, this.copiaRotacion);
        this.copia = this.copiaImagen(this.imagen);
        this.repaint();
    }
    
    /**
     * Aplica el operador SepiaOp a la imagen
     */
    public void sepia() {
        SepiaOp sepia = new SepiaOp();
        sepia.filter(this.imagen, this.imagen);
        sepia.filter(this.copiaRotacion, this.copiaRotacion);
        this.copia = this.copiaImagen(this.imagen);
        this.repaint();
    }
    
    /**
     * Aplica el operador SobelOp a la imagen
     */
    public void sobel() {
        SobelOp sobel = new SobelOp();
        sobel.filter(this.imagen, this.imagen);
        sobel.filter(this.copiaRotacion, this.copiaRotacion);
        this.copia = this.copiaImagen(this.imagen);
        this.repaint();
    }
    
    /**
     * Aplica el operador IntercambioColoresOp a la imagen
     */
    public void intercambioColores() {
        IntercambioColoresOp intercambiocolores = new IntercambioColoresOp();
        intercambiocolores.filter(this.imagen, this.imagen);
        intercambiocolores.filter(this.copiaRotacion, this.copiaRotacion);
        this.copia = this.copiaImagen(this.imagen);
        this.repaint();
    }
    
    /**
     * Aplica el operador EscalaGrisesOp a la imagen
     */
    public void escalaGrises() {
        EscalaGrisesOp escalagrises = new EscalaGrisesOp();
        escalagrises.filter(this.imagen, this.imagen);
        escalagrises.filter(this.copiaRotacion, this.copiaRotacion);
        this.copia = this.copiaImagen(this.imagen);
        this.repaint();
    }

    /**
     * Aplica el operador UmbralizacionOp a la imagen
     * @param umbral Umbral a aplicar
     */
    public void umbralizacion(int umbral) {
        UmbralizacionOp rop = new UmbralizacionOp(umbral);
        rop.filter(this.copia, this.imagen);

        this.repaint();
    }
    
    /**
     * Aplica el operador UmbralizacionOp a la copiaRotacion
     * @param umbral Umbral a aplicar
     */
    public void umbralizacionCopiaRotacion(int umbral) {
        UmbralizacionOp rop = new UmbralizacionOp(umbral);
        this.copiaRotacion = rop.filter(this.copiaRotacion, null);
        this.repaint();
    }
    
    /**
     * Aplica un filtro a la imagen
     * @param transformacion Tipo de filtro a aplicar
     */
    public void setFiltro(TipoTransformacion transformacion) {
        switch (transformacion) {
            case MEDIA:
                filtro = KernelProducer.TYPE_MEDIA_3x3;
            break;
            case BINOMIAL:
                filtro = KernelProducer.TYPE_BINOMIAL_3x3;
            break;
            case ENFOQUE:
                filtro = KernelProducer.TYPE_ENFOQUE_3x3;
            break;
            case RELIEVE:
                filtro = KernelProducer.TYPE_RELIEVE_3x3;
            break;
            case LAPLACIANO:
                filtro = KernelProducer.TYPE_LAPLACIANA_3x3;
            break;
        }
        
        if (filtro != -1) {
            ConvolveOp cop = new ConvolveOp(KernelProducer.createKernel(filtro), ConvolveOp.EDGE_NO_OP, null);
            this.imagen = cop.filter(this.imagen, null);
            this.copiaRotacion = cop.filter(this.copiaRotacion, null);
            this.copia = this.copiaImagen(this.imagen);
            this.repaint();
        } else {
            this.imagen = this.original;
            this.copia = this.copiaImagen(this.imagen);
            this.repaint();
        }
    }
    
    /**
     * Cambia el brillo a la imagen
     * @param brillo Valor del brillo
     */
    public void setBrillo(int brillo) {
        this.brillo = brillo;
        float valorBrillo = (float) 1.0f + this.brillo/100.f;
        float valorTransparencia = (float) Math.abs(this.brillo/100.0f - 1.0f);
        
        if (imagen.getColorModel().getColorSpace().getType() == ColorSpace.TYPE_GRAY) {
            scale = new float[]{valorBrillo};
            offset = new float[]{0f};
        } else {
            scale = new float[]{valorBrillo,valorBrillo,valorBrillo,valorTransparencia};
            offset = new float[]{0f, 0f, 0f, 0f};
        }
        
        if (scale != null && offset != null) {
            RescaleOp rop = new RescaleOp(scale, offset, null);
            rop.filter(this.copia, this.imagen);
            
            this.repaint();
        }
    }
    
    /**
     * Cambia el brillo a la copiarotación a partir del scale y offset
     */
    public void setBrilloCopiaRotacion() {
        if (scale != null && offset != null) {
            RescaleOp rop = new RescaleOp(scale, offset, null);
            this.copiaRotacion = rop.filter(this.copiaRotacion, null);
            this.repaint();
        }
    }
    
    /**
     * Cambia el contraste de la imagen
     * @param transformacion Tipo de contraste a aplicar
     */
    public void setContraste(TipoTransformacion transformacion) {
        switch (transformacion) {
            case CONTRASTE:
                contraste = LookupTableProducer.TYPE_SFUNCION;
            break;
            case ILUMINAR:
                contraste = LookupTableProducer.TYPE_LOGARITHM;
            break;
            case OSCURECER:
                contraste = LookupTableProducer.TYPE_POWER;
            break;
            case NEGATIVO:
                contraste = LookupTableProducer.TYPE_NEGATIVE;
            break;
        }
        
        if (contraste != -1) {
            LookupTable lt = LookupTableProducer.createLookupTable(contraste);
            LookupOp lop = new LookupOp(lt, null);
            lop.filter(this.imagen, this.imagen);
            lop.filter(this.copiaRotacion, this.copiaRotacion);
            this.copia = this.copiaImagen(this.imagen);
            this.repaint();
        }
    }
    
    /**
     * Modifica el valor del tamaño de escalado
     * @param escalado Tipo de escalado
     */
    public void escalarImagen(TipoEscalado escalado) {
        if (escalado == TipoEscalado.AUMENTAR) {
            tamEscalado += 0.1;     
        }
        else if (escalado == TipoEscalado.DISMINUIR) {
            tamEscalado -= 0.1;
        }
    }
    
    /**
     * Modifica el valor de rotación
     * @param rotacion Valor de rotación
     */
    public void rotarImagen(int rotacion) {
        anguloRotacion = rotacion;
    }

    /**
     * Aplica las transformaciones rotación y escalado a la imagen a partir de copiaRotacion
     * para evitar perder las esquinas al rotarla
     */
    public void aplicarTransformaciones() {
        try {
            AffineTransform at = new AffineTransform();
            at.concatenate(AffineTransform.getRotateInstance(Math.toRadians(anguloRotacion), copiaRotacion.getWidth()/2, copiaRotacion.getHeight()/2));
            at.concatenate(AffineTransform.getScaleInstance(tamEscalado, tamEscalado));
            AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            this.imagen = atop.filter(this.copiaRotacion, null);
            this.copia = this.copiaImagen(this.imagen);

            //Marco
            width = this.imagen.getWidth();
            height = this.imagen.getHeight();
            this.newClipArea((int) (width), (int) (height));
            this.setBordeLienzo((int) (width), (int) (height));
            this.repaint();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    
}
