package ec.udla.osgi.saludos.api;

/**
 * Contrato publico del servicio. Es lo unico que otros bundles conocen:
 * nunca ven la clase que lo implementa.
 */
public interface SaludoService {

    /** Devuelve un saludo personalizado. */
    String saludar(String nombre);
}
