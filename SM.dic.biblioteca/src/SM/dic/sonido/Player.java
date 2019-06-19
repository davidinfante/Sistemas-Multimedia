package SM.dic.sonido;

import java.io.File;
import sm.sound.SMClipPlayer;

/**
 * Reproductod de sonido
 * @author David Infante Casas
 */
public class Player {
    /**
     * Reproductor de sonido
     */
    private SMClipPlayer player;
    /**
     * Fichero con la canción a reproducir
     */
    private File song;
    /**
     * Estado de la canción
     */
    private SongStatus songStatus;
    
    /**
     * Crea un nuevo Player
     */
    public Player() {}
    
    /**
     * Asigna una nueva canción a song e inicializa el player
     * @param song Canción a asignar
     */
    public void setSong(File song) {
        this.song = song;
        if (this.song != null) {
            player = new SMClipPlayer(this.song);
        }
        songStatus = SongStatus.STOPPED;
    }
    
    /**
     * Devuelve la canción
     * @return Devuelve la canción
     */
    public File getSong() {
        return song;
    }

    /**
     * Devuelve el estado de la canción
     * @return Devuelve el estado de la canción
     */
    public SongStatus getSongStatus() {
        return songStatus;
    }
    
    /**
     * Comienza o continúa la reproducción de la canción
     */
    public void play() {
        if (player != null && songStatus != SongStatus.PLAYING) {
            if (songStatus == SongStatus.STOPPED) {
                player.play();
                songStatus = SongStatus.PLAYING;
            } else if (songStatus == SongStatus.PAUSED) {
                player.resume();
                songStatus = SongStatus.PLAYING;
            }
        }
    }
    
    /**
     * Para y resetea la reproducción de la canción
     */
    public void stop() {
        if (player != null && songStatus != SongStatus.STOPPED) {
            player.stop();
            songStatus = SongStatus.STOPPED;
        }
    }
    
    /**
     * Pausa la reproducción de la canción
     */
    public void pause() {
        if (player != null && songStatus != SongStatus.PAUSED) {
            player.pause();
            songStatus = SongStatus.PAUSED;
        }
    }

}
