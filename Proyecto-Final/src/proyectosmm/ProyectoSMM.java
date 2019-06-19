package proyectosmm;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Clase main del proyecto que ejecuta el programa
 * @author David Infante Casas
 */
public class ProyectoSMM {
    static String VLCLIBPATH32 = "C:\\Program Files (x86)\\VideoLAN\\VLC";
    static String VLCLIBPATH64 = "C:\\Program Files\\VideoLAN\\VLC";
    static boolean vlc_loaded = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLCLIBPATH64);
            Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
            vlc_loaded = true;
        } catch (UnsatisfiedLinkError  e) {
            System.err.println("Biblioteca VLC de 64 bits no encontrada");
        }
        if (!vlc_loaded) {
            try {
                NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLCLIBPATH32);
                Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
            } catch (UnsatisfiedLinkError  e) {
                System.err.println("Biblioteca VLC de 32 bits no encontrada");
            }
        }
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }
    
}
