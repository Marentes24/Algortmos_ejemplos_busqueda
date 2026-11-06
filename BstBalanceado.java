import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

/** tiempo de ejecución con 10.000 datos mock. */
public class BstBalanceado {

    static final int TAMANO = 10_000;
    static final int NUM_BUSQUEDAS = 1_000;
    static final long SEMILLA = 42;

    static class Nodo {
        int valor;
        Nodo izq;
        Nodo der;
        int altura = 1;

        Nodo(int valor) {
            this.valor = valor;
        }
    }

    static int altura(Nodo nodo) {
        return nodo == null ? 0 : nodo.altura;
    }

    static void actualizarAltura(Nodo nodo) {
        nodo.altura = 1 + Math.max(altura(nodo.izq), altura(nodo.der));
    }

    static int balance(Nodo nodo) {
        return nodo == null ? 0 : altura(nodo.izq) - altura(nodo.der);
    }

    static Nodo rotarDerecha(Nodo y) {
        Nodo x = y.izq;
        y.izq = x.der;
        x.der = y;
        actualizarAltura(y);
        actualizarAltura(x);
        return x;
    }

    static Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.der;
        x.der = y.izq;
        y.izq = x;
        actualizarAltura(x);
        actualizarAltura(y);
        return y;
    }

    static Nodo insertar(Nodo nodo, int valor) {
        if (nodo == null) {
            return new Nodo(valor);
        }
        if (valor < nodo.valor) {
            nodo.izq = insertar(nodo.izq, valor);
        } else if (valor > nodo.valor) {
            nodo.der = insertar(nodo.der, valor);
        } else {
            return nodo;
        }

        actualizarAltura(nodo);
        int factor = balance(nodo);

        if (factor > 1 && valor < nodo.izq.valor) {
            return rotarDerecha(nodo);
        }
        if (factor < -1 && valor > nodo.der.valor) {
            return rotarIzquierda(nodo);
        }
        if (factor > 1 && valor > nodo.izq.valor) {
            nodo.izq = rotarIzquierda(nodo.izq);
            return rotarDerecha(nodo);
        }
        if (factor < -1 && valor < nodo.der.valor) {
            nodo.der = rotarDerecha(nodo.der);
            return rotarIzquierda(nodo);
        }
        return nodo;
    }

    static boolean buscar(Nodo raiz, int objetivo) {
        Nodo actual = raiz;
        while (actual != null) {
            if (objetivo == actual.valor) {
                return true;
            }
            actual = objetivo < actual.valor ? actual.izq : actual.der;
        }
        return false;
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
        Nodo raiz = null;
        for (int valor : datos) {
            raiz = insertar(raiz, valor);
        }
        long construccion = System.nanoTime() - inicioConstruccion;

        // Calentamiento de la JVM (no se mide)
        for (int objetivo : objetivos) {
            buscar(raiz, objetivo);
        }

        long inicio = System.nanoTime();
        for (int objetivo : objetivos) {
            buscar(raiz, objetivo);
        }
        long total = System.nanoTime() - inicio;

        System.out.println("BST balanceado (AVL)");
        System.out.printf(Locale.US, "Datos: %,d | Busquedas: %,d%n", TAMANO, NUM_BUSQUEDAS);
        System.out.printf(Locale.US, "Tiempo de construccion: %.3f ms%n", construccion / 1e6);
        System.out.printf(Locale.US, "Tiempo total:        %.3f ms%n", total / 1e6);
        System.out.printf(Locale.US, "Tiempo por busqueda: %.3f us%n", total / 1e3 / NUM_BUSQUEDAS);
    }
}
