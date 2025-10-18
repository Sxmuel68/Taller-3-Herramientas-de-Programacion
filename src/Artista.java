import java.util.List;

/**
 * Representa a un artista dentro del sistema musical.
 * Contiene su nombre, imagen asociada y lista de canciones.
 */
public class Artista {

    // ==========================
    // Atributos
    // ==========================
    private String nombre;
    private String imagen;
    private List<Cancion> canciones;

    // ==========================
    // Constructores
    // ==========================

    /**
     * Constructor vacío (requerido por librerías de serialización como Jackson).
     */
    public Artista() {
    }

    /**
     * Crea un nuevo artista con su nombre, imagen y lista de canciones.
     * 
     * @param nombre    Nombre del artista
     * @param imagen    Ruta de la imagen del artista
     * @param canciones Lista de canciones asociadas
     */
    public Artista(String nombre, String imagen, List<Cancion> canciones) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.canciones = canciones;
    }

    // ==========================
    // Getters y Setters
    // ==========================

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    // ==========================
    // Métodos sobrescritos
    // ==========================

    @Override
    public String toString() {
        return nombre;
    }
}
