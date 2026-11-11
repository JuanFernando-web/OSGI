package ec.udla.osgi.cliente;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import ec.udla.osgi.saludos.api.SaludoService;

/**
 * Cliente desacoplado: no conoce la implementacion ni cuando existe.
 * Un ServiceTracker le avisa cuando el servicio aparece o desaparece, y cada
 * 5 segundos intenta usarlo. Si no esta, lo informa y sigue vivo.
 */
public class Activator implements BundleActivator {

    private ServiceTracker<SaludoService, SaludoService> tracker;
    private ScheduledExecutorService planificador;

    @Override
    public void start(BundleContext context) {
        tracker = new ServiceTracker<>(context, SaludoService.class,
                new ServiceTrackerCustomizer<SaludoService, SaludoService>() {
                    @Override
                    public SaludoService addingService(ServiceReference<SaludoService> ref) {
                        System.out.println("[CLIENTE] Servicio de saludos DISPONIBLE");
                        return context.getService(ref);
                    }

                    @Override
                    public void modifiedService(ServiceReference<SaludoService> ref, SaludoService servicio) {
                        // sin accion
                    }

                    @Override
                    public void removedService(ServiceReference<SaludoService> ref, SaludoService servicio) {
                        System.out.println("[CLIENTE] Servicio de saludos RETIRADO, se esperara a que vuelva");
                        context.ungetService(ref);
                    }
                });
        tracker.open();

        planificador = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "cliente-saludos");
            t.setDaemon(true);
            return t;
        });
        planificador.scheduleWithFixedDelay(this::tick, 2, 5, TimeUnit.SECONDS);
        System.out.println("[CLIENTE] Bundle iniciado");
    }

    private void tick() {
        try {
            SaludoService servicio = tracker.getService();
            if (servicio == null) {
                System.out.println("[CLIENTE] (sin servicio de saludos, el sistema sigue funcionando)");
            } else {
                System.out.println("[CLIENTE] " + servicio.saludar("UDLA"));
            }
        } catch (RuntimeException e) {
            System.out.println("[CLIENTE] Error al usar el servicio: " + e);
        }
    }

    @Override
    public void stop(BundleContext context) {
        if (planificador != null) {
            planificador.shutdownNow();
        }
        if (tracker != null) {
            tracker.close();
        }
        System.out.println("[CLIENTE] Bundle detenido");
    }
}
