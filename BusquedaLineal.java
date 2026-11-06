import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

/** tiempo de ejecución con 10.000 datos mock. */
public class BusquedaLineal {

    static final int TAMANO = 10_000;
    static final int NUM_BUSQUEDAS = 1_000;
    static final long SEMILLA = 42;

    static int busquedaLineal(int[] datos, int objetivo) {
        for (int i = 0; i < datos.length; i++) {
            if (datos[i] == objetivo) {
                return i;
            }
        }
        return -1;
    }

    static int[] generarDatos(Random random) {
        Set<Integer> unicos = new LinkedHashSet<>();
        while (unicos.size() < TAMANO) {
            unicos.add(random.nextInt(TAMANO * 10) + 1);
        }
        return unicos.stream().mapToInt(Integer::intValue).toArray();
    }

    static int[] elegirObjetivos(int[] datos, Random random) {
        int[] objetivos = new int[NUM_BUSQUEDAS];
        for (int i = 0; i < NUM_BUSQUEDAS; i++) {
            objetivos[i] = datos[random.nextInt(datos.length)];
        }
        return objetivos;
    }

    public static void main(String[] args) {
        Random random = new Random(SEMILLA);
        int[] datos = generarDatos(random);
        int[] objetivos = elegirObjetivos(datos, random);

        // Calentamiento de la JVM (no se mide)
        for (int objetivo : objetivos) {
            busquedaLineal(datos, objetivo);
        }

        long inicio = System.nanoTime();
        for (int objetivo : objetivos) {
            busquedaLineal(datos, objetivo);
        }
        long total = System.nanoTime() - inicio;

        System.out.println("Busqueda lineal");
        System.out.printf(Locale.US, "Datos: %,d | Busquedas: %,d%n", TAMANO, NUM_BUSQUEDAS);
        System.out.printf(Locale.US, "Tiempo total:        %.3f ms%n", total / 1e6);
        System.out.printf(Locale.US, "Tiempo por busqueda: %.3f us%n", total / 1e3 / NUM_BUSQUEDAS);
    }
}
