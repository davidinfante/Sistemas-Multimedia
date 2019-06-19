package SM.dic.imagen;

import java.awt.image.BufferedImage;
import sm.image.BufferedImageOpAdapter;

/**
 * Operación componente a componente que intercambia los valores RGB de los píxeles
 * @author David Infante Casas
 */
public class IntercambioColoresOp extends BufferedImageOpAdapter {
    
    /**
     * Crea un nuevo IntercambioColoresOp
     */
    public IntercambioColoresOp () {}
    
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
                
                int sepiaR = (int) Math.min(255, G*0.5 + B*0.5);
                int sepiaG = (int) Math.min(255,  R*0.5 + B*0.5);
                int sepiaB = (int) Math.min(255,  R*0.5 + G*0.5);
                
                p = (A<<24) | (sepiaR<<16) | (sepiaG<<8) | sepiaB;
                dest.setRGB(x, y, p);
            }
        }
        return dest;
    }
}
