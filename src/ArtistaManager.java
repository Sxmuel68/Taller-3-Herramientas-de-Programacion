import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Gestiona la carga y acceso a la lista de artistas desde un archivo JSON.
 * Usa la librería Jackson para la deserialización de datos.
 */
public class ArtistaManager {

    // ==========================
    // Atributos
    // ==========================
    private List<Artista> artistas;

    // ==========================
    // Constructor
    // ==========================

    /**
     * Crea un administrador de artistas cargando los datos desde el archivo JSON especificado.
     *
     * @param rutaJson Ruta del archivo JSON con la información de los artistas
     * @throws IOException Si ocurre un error al leer o procesar el archivo
     */
    public ArtistaManager(String rutaJson) throws IOException {
        cargarArtistas(rutaJson);
    }

    // ==========================
    // Métodos privados
    // ==========================

    /**
     * Carga la lista de artistas desde el archivo JSON.
     *
     * @param rutaJson Ruta del archivo JSON
     * @throws IOException Si ocurre un error al leer o convertir los datos
     */
    private void cargarArtistas(String rutaJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        artistas = mapper.readValue(
            new File(rutaJson),
            new TypeReference<List<Artista>>() {}
        );
    }

    // ==========================
    // Métodos públicos
    // ==========================

    /**
     * Retorna la lista completa de artistas cargados.
     *
     * @return Lista de objetos {@link Artista}
     */
    public List<Artista> getArtistas() {
        return artistas;
    }
}
