import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

/** tiempo de ejecución con 10.000 datos mock. */
public class ExponentialSearch {

    static final int TAMANO = 10_000;
    static final int NUM_BUSQUEDAS = 1_000;
    static final long SEMILLA = 42;

    static int busquedaExponencial(int[] datos, int objetivo) {
        int n = datos.length;
        if (n == 0) {
            return -1;
        }
        if (datos[0] == objetivo) {
            return 0;
        }

        int limite = 1;
        while (limite < n && datos[limite] <= objetivo) {
            limite *= 2;
        }

        int bajo = limite / 2;
        int alto = Math.min(limite, n - 1);
        while (bajo <= alto) {
            int medio = (bajo + alto) >>> 1;
            if (datos[medio] == objetivo) {
                return medio;
            }
            if (datos[medio] < objetivo) {
                bajo = medio + 1;
            } else {
                alto = medio - 1;
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
        Arrays.sort(datos);

        // Calentamiento de la JVM (no se mide)
        for (int objetivo : objetivos) {
            busquedaExponencial(datos, objetivo);
        }

        long inicio = System.nanoTime();
        for (int objetivo : objetivos) {
            busquedaExponencial(datos, objetivo);
        }
        long total = System.nanoTime() - inicio;

        System.out.println("Exponential Search");
        System.out.printf(Locale.US, "Datos: %,d | Busquedas: %,d%n", TAMANO, NUM_BUSQUEDAS);
        System.out.printf(Locale.US, "Tiempo total:        %.3f ms%n", total / 1e6);
        System.out.printf(Locale.US, "Tiempo por busqueda: %.3f us%n", total / 1e3 / NUM_BUSQUEDAS);
    }
}
