package ec.udla.osgi.saludos.impl;

import java.util.concurrent.atomic.AtomicInteger;

import ec.udla.osgi.saludos.api.SaludoService;

/** Implementacion privada del servicio (no se exporta del bundle). */
public class SaludoServiceImpl implements SaludoService {

    private final AtomicInteger contador = new AtomicInteger();

    @Override
    public String saludar(String nombre) {
        return "Hola, " + nombre + "! (saludo #" + contador.incrementAndGet() + ")";
    }
}
