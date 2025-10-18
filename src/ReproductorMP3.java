import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Clase que permite reproducir archivos MP3 utilizando la librería JLayer.
 * Implementa funcionalidades básicas de reproducción, pausa, reanudación y detención.
 */
public class ReproductorMP3 {

    // ==========================
    // Atributos
    // ==========================
    private Player player;
    private Thread playThread;
    private boolean isPaused = false;
    private FileInputStream currentStream;
    private String currentFile;
    private long pausePosition = 0;

    private static final Logger logger = Logger.getLogger(ReproductorMP3.class.getName());

    // ==========================
    // Métodos públicos
    // ==========================

    /**
     * Reproduce el archivo MP3 indicado en una nueva hebra (thread).
     *
     * @param rutaArchivo Ruta del archivo MP3 a reproducir.
     */
    public void play(String rutaArchivo) {
        stop();
        isPaused = false;
        currentFile = rutaArchivo;
        pausePosition = 0;

        playThread = new Thread(() -> {
            try {
                currentStream = new FileInputStream(rutaArchivo);
                player = new Player(currentStream);
                player.play();
            } catch (Exception e) {
                logger.warning("Error al reproducir MP3: " + e.getMessage());
            }
        });

        playThread.start();
    }

    /**
     * Pausa la reproducción actual y guarda la posición de reproducción.
     */
    public void pause() {
        if (player != null && !isPaused) {
            try {
                pausePosition = currentStream.getChannel().position();
                player.close();
                isPaused = true;
            } catch (IOException e) {
                logger.warning("Error al pausar MP3: " + e.getMessage());
            }
        }
    }

    /**
     * Reanuda la reproducción desde el punto donde fue pausada.
     */
    public void resume() {
        if (isPaused && currentFile != null) {
            playThread = new Thread(() -> {
                try {
                    currentStream = new FileInputStream(currentFile);
                    currentStream.getChannel().position(pausePosition);
                    player = new Player(currentStream);
                    player.play();
                } catch (Exception e) {
                    logger.warning("Error al reanudar MP3: " + e.getMessage());
                }
            });

            playThread.start();
            isPaused = false;
        }
    }

    /**
     * Detiene la reproducción actual y libera los recursos utilizados.
     */
    public void stop() {
        if (player != null) {
            player.close();
        }
        if (playThread != null && playThread.isAlive()) {
            playThread.interrupt();
        }
    }
}
