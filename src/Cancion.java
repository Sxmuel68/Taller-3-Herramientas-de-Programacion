/**
 * Representa una canción con su título y ruta de archivo asociada.
 * Clase utilizada por Jackson para la deserialización desde JSON.
 */
public class Cancion {

    // ==========================
    // Atributos
    // ==========================
    private String titulo;
    private String archivo;

    // ==========================
    // Constructores
    // ==========================

    /**
     * Constructor vacío requerido por Jackson.
     */
    public Cancion() {}

    /**
     * Crea una canción con los datos especificados.
     *
     * @param titulo  Título de la canción
     * @param archivo Ruta del archivo de audio
     */
    public Cancion(String titulo, String archivo) {
        this.titulo = titulo;
        this.archivo = archivo;
    }

    // ==========================
    // Métodos Getters y Setters
    // ==========================

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    // ==========================
    // Métodos sobrescritos
    // ==========================

    /**
     * Retorna el título de la canción al representarla como texto.
     *
     * @return El título de la canción
     */
    @Override
    public String toString() {
        return titulo;
    }
}
