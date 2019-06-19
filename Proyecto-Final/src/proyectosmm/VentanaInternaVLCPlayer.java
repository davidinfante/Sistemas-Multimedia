package proyectosmm;

import java.awt.image.BufferedImage;
import java.io.File;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * Ventana que contiene un reproductor de video VLC
 * @author David Infante Casas
 */
public class VentanaInternaVLCPlayer extends javax.swing.JInternalFrame implements VentanaInterna {

    /**
     * Reproductor VLC
     */
    private EmbeddedMediaPlayer vlcplayer = null;
    /**
     * File que el reproductor leerá
     */
    private File fMedia;
    
    /**
     * Devuelve el tipo de VentanaInterna que es
     * @return Devuelve TipoVentanaInterna.VLCPLAYER
     */
    @Override
    public TipoVentanaInterna getTipo() {
        return TipoVentanaInterna.VLCPLAYER;
    }
    
    /**
     * Crea un nuevo VentanaInternaVLCPlayer
     * @param f Fichero a reproducir
     */
    private VentanaInternaVLCPlayer(File f) {
        initComponents();
        fMedia = f;
        EmbeddedMediaPlayerComponent aVisual = new EmbeddedMediaPlayerComponent();
        getContentPane().add(aVisual,java.awt.BorderLayout.CENTER);
        vlcplayer = aVisual.getMediaPlayer();
    }
    
    /**
     * Devuelve una nueva instancia de la clase
     * @param f File con el archivo que el reproductor leerá
     * @return Devuelve la instancia o null en caso de error
     */
    public static VentanaInternaVLCPlayer getInstance(File f) {
        VentanaInternaVLCPlayer v = new VentanaInternaVLCPlayer(f);
        return (v.vlcplayer != null ? v:null);
    }

    /**
     * Devuelve el fichero fMedia
     * @return Devuelve el fichero fMedia
     */
    public File getfMedia() {
        return fMedia;
    }
    
    /**
     * Reproduce el archivo fMedia
     */
    public void play() {
        if (vlcplayer != null) {
            if (vlcplayer.isPlayable()) {
                //Si se estaba reproduciendo
                vlcplayer.play();
            } else {
                vlcplayer.playMedia(fMedia.getAbsolutePath());
            }
        }
    }
    
    /**
     * Devuelve si el reproductor puede reproducir o no el archivo fMedia
     * @return true si se puede reproducir, false en otro caso
     */
    public boolean isPlayable() {
        return vlcplayer.isPlayable();
    }
    
    /**
     * Devuelve si el reproductor está reproduciendo o no el archivo fMedia
     * @return true si se está reproduciendo, false en otro caso
     */
    public boolean isPlaying() {
        return vlcplayer.isPlaying();
    }
    
    /**
     * Pausa la reproducción del archivo fMedia
     */
    public void pause() {
        if (vlcplayer != null) {
            if (vlcplayer.isPlaying()) {
                vlcplayer.pause();
            }
        }
    }
    
    /**
     * Reinicia y para la reproducción del archivo fMedia
     */
    public void stop() {
        if (vlcplayer != null) {
            if (vlcplayer.isPlaying()) {
                vlcplayer.stop();
            }
        }
    }
    
    /**
     * Toma una instantánea de lo que está siendo reproducido
     * @return Devuelve una BufferedImage si la operación ha tenido éxito, null en otro caso
     */
    public BufferedImage screenshot() {
        if (vlcplayer != null) return vlcplayer.getSnapshot();
        else return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        stop();
    }//GEN-LAST:event_formInternalFrameClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
