import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/** tiempo de ejecución con 10.000 datos mock. */
public class HashTable {

    static final int TAMANO = 10_000;
    static final int NUM_BUSQUEDAS = 1_000;
    static final long SEMILLA = 42;

    static int hashBuscar(Map<Integer, Integer> tabla, int objetivo) {
        return tabla.getOrDefault(objetivo, -1);
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

        long inicioConstruccion = System.nanoTime();
        Map<Integer, Integer> tabla = new HashMap<>();
        for (int i = 0; i < datos.length; i++) {
            tabla.put(datos[i], i);
        }
        long construccion = System.nanoTime() - inicioConstruccion;

        // Calentamiento de la JVM (no se mide)
        for (int objetivo : objetivos) {
            hashBuscar(tabla, objetivo);
        }

        long inicio = System.nanoTime();
        for (int objetivo : objetivos) {
            hashBuscar(tabla, objetivo);
        }
        long total = System.nanoTime() - inicio;

        System.out.println("Hash Table");
        System.out.printf(Locale.US, "Datos: %,d | Busquedas: %,d%n", TAMANO, NUM_BUSQUEDAS);
        System.out.printf(Locale.US, "Tiempo de construccion: %.3f ms%n", construccion / 1e6);
        System.out.printf(Locale.US, "Tiempo total:        %.3f ms%n", total / 1e6);
        System.out.printf(Locale.US, "Tiempo por busqueda: %.3f us%n", total / 1e3 / NUM_BUSQUEDAS);
    }
}
