package SM.dic.sonido;

import java.io.File;
import sm.sound.SMSoundRecorder;

/**
 * Grabador de audio
 * @author David Infante Casas
 */
public class Recorder {
    /**
     * Grabador de audio
     */
    private SMSoundRecorder recorder;
    /**
     * Fichero con el audio grabado
     */
    private File audio;
    /**
     * Booleano a true si se está grabando
     */
    private boolean recording;
    /**
     * Booleano a true si el audio se ha añadido a la lista de reproducción
     */
    private boolean songAdded;
    
    /**
     * Crea un nuevo Recorder
     */
    public Recorder() {
        recording = false;
        songAdded = true;
    }
    
    /**
     * Inicializa el recorder y comienza a grabar
     */
    public void grabar() {
        recorder = new SMSoundRecorder(audio);
        recorder.record();
        recording = true;
    }
    
    /**
     * Detiene la grabación
     */
    public void parar() {
        if (recorder != null && recording) {
            recorder.stop();
            recording = false;
            songAdded = false;
        }
    }
    
    /**
     * Devuelve la pista de audio
     * @return Devuelve la pista de audio
     */
    public File getAudio() {
        return audio;
    }

    /**
     * Devuelve si se está grabando
     * @return Devuelve si se está grabando
     */
    public boolean isRecording() {
        return recording;
    }

    /**
     * Devuelve si el audio ha sido añadido a la lista de reproducción
     * @return Devuelve si el audio ha sido añadido a la lista de reproducción
     */
    public boolean isSongAdded() {
        return songAdded;
    }

    /**
     * Asigna si el audio ha sido añadida a la lista de reproducción
     * @param songAdded Si el audio ha sido añadido o no
     */
    public void setSongAdded(boolean songAdded) {
        this.songAdded = songAdded;
    }

    /**
     * Asigna el audio de recording a audio
     * @param recording El audio a asignar
     */
    public void setAudio(File recording) {
        this.audio = recording;
    }
    
}
