import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

/**tiempo de ejecución con 10.000 datos mock. */
public class InterpolationSearch {

    static final int TAMANO = 10_000;
    static final int NUM_BUSQUEDAS = 1_000;
    static final long SEMILLA = 42;

    static int interpolationSearch(int[] datos, int objetivo) {
        int bajo = 0;
        int alto = datos.length - 1;

        while (bajo <= alto && objetivo >= datos[bajo] && objetivo <= datos[alto]) {
            if (datos[alto] == datos[bajo]) {
                return datos[bajo] == objetivo ? bajo : -1;
            }
            int pos = bajo + (int) (((long) (objetivo - datos[bajo]) * (alto - bajo))
                    / (datos[alto] - datos[bajo]));
            if (datos[pos] == objetivo) {
                return pos;
            }
            if (datos[pos] < objetivo) {
                bajo = pos + 1;
            } else {
                alto = pos - 1;
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
            interpolationSearch(datos, objetivo);
        }

        long inicio = System.nanoTime();
        for (int objetivo : objetivos) {
            interpolationSearch(datos, objetivo);
        }
        long total = System.nanoTime() - inicio;

        System.out.println("Interpolation Search");
        System.out.printf(Locale.US, "Datos: %,d | Busquedas: %,d%n", TAMANO, NUM_BUSQUEDAS);
        System.out.printf(Locale.US, "Tiempo total:        %.3f ms%n", total / 1e6);
        System.out.printf(Locale.US, "Tiempo por busqueda: %.3f us%n", total / 1e3 / NUM_BUSQUEDAS);
    }
}