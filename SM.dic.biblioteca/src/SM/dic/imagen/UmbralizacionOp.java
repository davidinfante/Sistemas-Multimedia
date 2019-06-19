package SM.dic.imagen;

import java.awt.image.BufferedImage;
import sm.image.BufferedImageOpAdapter;

/**
 * Operación que aplica el filtro de umbralización a la imagen
 * @author David Infante Casas
 */
public class UmbralizacionOp extends BufferedImageOpAdapter {
    
    /**
     * Valor del umbral
     */
    private int umbral; 
 
    /**
     * Crea un nuevo UmbralizaciónOp
     * @param umbral Valor del umbral
     */
    public UmbralizacionOp(int umbral) {
        this.umbral = umbral;
    }
 
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
                
                if (R+G+B > umbral) {
                    p = (A<<24) | (255<<16) | (255<<8) | 255;
                } else {
                    p = (A<<24) | (0<<16) | (0<<8) | 0;
                }
                dest.setRGB(x, y, p);
            }
        }
        return dest;
    }
}
