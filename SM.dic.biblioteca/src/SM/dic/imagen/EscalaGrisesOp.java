package SM.dic.imagen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import sm.image.BufferedImageOpAdapter;
import sm.image.BufferedImagePixelIterator;

/**
 * Operación pixel a pixel que hace una escala de grises de la imagen
 * @author David Infante Casas
 */
public class EscalaGrisesOp extends BufferedImageOpAdapter {
    
    /**
     * Crea un nuevo EscalaGrisesOp
     */
    public EscalaGrisesOp() {}
    
    /**
     * Aplica la operación sobre la imagen src
     * @param src Imagen sobre la que operar
     * @param dest Imagen con la operación realizada
     * @return Devuelve la imagen con la operación
     */
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        
        if (src == null) throw new NullPointerException("src image is null");
        if (dest == null) throw new NullPointerException("dest image is null");

        BufferedImagePixelIterator iterator = new BufferedImagePixelIterator(src);
        int valorGris;
        
        while (iterator.hasNext()) {
            BufferedImagePixelIterator.PixelData pixel = iterator.next();
            
            valorGris = 0;
            valorGris += pixel.sample[0];
            valorGris += pixel.sample[1];

            int magnitud = (int) Math.hypot(Math.abs(valorGris-255), Math.abs(valorGris-255));
            int value = sm.image.ImageTools.clampRange(magnitud, 0, 255);

            dest.setRGB(pixel.col, pixel.row, new Color(value, value, value).getRGB());
        }

        return dest;
    }
    
}
