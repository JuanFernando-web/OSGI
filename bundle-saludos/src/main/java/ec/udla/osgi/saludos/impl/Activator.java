package ec.udla.osgi.saludos.impl;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import ec.udla.osgi.saludos.api.SaludoService;

/**
 * Al iniciar el bundle publica el servicio en el registro de servicios de OSGi;
 * al detenerlo lo retira. Nadie le "llama" directamente: los clientes lo buscan
 * en el registro por su interfaz.
 */
public class Activator implements BundleActivator {

    private ServiceRegistration<SaludoService> registro;

    @Override
    public void start(BundleContext context) {
        Dictionary<String, Object> propiedades = new Hashtable<>();
        propiedades.put("idioma", "es");
        registro = context.registerService(SaludoService.class, new SaludoServiceImpl(), propiedades);
        System.out.println("[SALUDOS] Bundle iniciado: servicio SaludoService REGISTRADO");
    }

    @Override
    public void stop(BundleContext context) {
        if (registro != null) {
            registro.unregister();
            registro = null;
        }
        System.out.println("[SALUDOS] Bundle detenido: servicio SaludoService RETIRADO");
    }
}
