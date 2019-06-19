package SM.dic.imagen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import sm.image.BufferedImageOpAdapter;
import sm.image.BufferedImagePixelIterator;
import sm.image.KernelProducer;

/**
 * Operación que aplica un filtro sobel a la imagen
 * @author David Infante Casas
 */
public class SobelOp extends BufferedImageOpAdapter {
    
    /**
     * Crea un nuevo SobelOp
     */
    public SobelOp() {}
    
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

        Kernel kernel = KernelProducer.createKernel(KernelProducer.TYPE_SOBELX_3x3);
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage grad = convOp.filter(src, null);
        BufferedImagePixelIterator iterator = new BufferedImagePixelIterator(grad);
        int sobel;

        while (iterator.hasNext()) {
            BufferedImagePixelIterator.PixelData pixel = iterator.next();
            
            sobel = 0;
            sobel += pixel.sample[0];
            sobel += pixel.sample[1];
            sobel += pixel.sample[2];

            int magnitud = (int) Math.hypot(sobel, sobel);
            int value = sm.image.ImageTools.clampRange(magnitud, 0, 255);

            dest.setRGB(pixel.col, pixel.row, new Color(value, value, value).getRGB());
        }

        return dest;
    }
    
}
