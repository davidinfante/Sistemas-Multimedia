package SM.dic.imagen;

import java.awt.image.BufferedImage;
import sm.image.BufferedImageOpAdapter;

/**
 * Operación que aplica un filtro sepia a la imagen
 * @author David Infante Casas
 */
public class SepiaOp extends BufferedImageOpAdapter {
    
    /**
     * Crea un nuevo SepiaOp
     */
    public SepiaOp () {}
    
    /**
     * Aplica la operación sobre la imagen src
     * @param src Imagen sobre la que operar
     * @param dest Imagen con la operación realizada
     * @return Devuelve la imagen con la operación
     */
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        if (src == null) throw new NullPointerException("src image is null");
        if (dest == null) dest = createCompatibleDestImage(src, null);
        
        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                //Obtenemos el RBG del pixel
                int p = src.getRGB(x, y);
                int A = (p>>24) & 0xff; //Alpha
                int R = (p>>16) & 0xff;
                int G = (p>>8) & 0xff;
                int B = p & 0xff;
                
                int sepiaR = (int) Math.min(255 , 0.393*R + 0.769*G + 0.189*B);
                int sepiaG = (int) Math.min(255,  0.349*R + 0.686*G + 0.168*B);
                int sepiaB = (int) Math.min(255,  0.272*R + 0.534*G + 0.131*B);
                
                p = (A<<24) | (sepiaR<<16) | (sepiaG<<8) | sepiaB;
                dest.setRGB(x, y, p);
            }
        }
        return dest;
    }
    
}
