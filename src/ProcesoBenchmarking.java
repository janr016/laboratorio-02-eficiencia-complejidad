import java.util.Random;

public class ProcesoBenchmarking {
    public static void main(String[] args) {
        int[] tamanios = {100, 1000};
        int repeticiones = 3;

        System.out.println("==================================================================================");
        System.out.println("          INICIANDO PROCESO EXPERIMENTAL (3 REPETICIONES POR PRUEBA)          ");
        System.out.println("==================================================================================\n");

        // Verificación previa: A y B deben producir la MISMA salida con los mismos datos
        System.out.println("--- Verificación de salida (mismo dataset para A y B) ---");
        for (int n : tamanios) {
            int[] datosA = generarDataset(n);
            int[] datosB = generarDataset(n);
            ejecutarAlgoritmoA(datosA);
            ejecutarAlgoritmoB(datosB);
            boolean mismaSalida = true;
            for (int i = 0; i < n; i++) {
                if (datosA[i] != datosB[i]) {
                    mismaSalida = false;
                }
            }
            System.out.println("  n = " + n + " -> A y B marcan las mismas posiciones: " + (mismaSalida ? "SI" : "NO"));
        }
        System.out.println();

        // Almacenamiento para la tabla final: una fila por tamaño, columnas {n, Ops, Tiempo}
        double[][] tablaA = new double[tamanios.length][3];
        double[][] tablaB = new double[tamanios.length][3];

        for (int k = 0; k < tamanios.length; k++) {
            int n = tamanios[k];

            // ==========================================
            // PROCESO PARA ALGORITMO A
            // ==========================================
            System.out.println("--- Ejecutando ALGORITMO A con n = " + n + " ---");
            long sumaOpsA = 0;
            double sumaTiempoA = 0;

            for (int r = 1; r <= repeticiones; r++) {
                int[] datos = generarDataset(n);
                Resultado res = ejecutarAlgoritmoA(datos);

                sumaOpsA += res.operaciones;
                sumaTiempoA += res.tiempoMs;

                System.out.println("  [Intento " + r + "] -> Operaciones: " + res.operaciones + " | Tiempo: " + res.tiempoMs + " ms");
            }

            double promOpsA = (double) sumaOpsA / repeticiones;
            double promTiempoA = sumaTiempoA / repeticiones;
            tablaA[k][0] = n;
            tablaA[k][1] = promOpsA;
            tablaA[k][2] = promTiempoA;

            System.out.println("  >> PROMEDIO Algoritmo A (n=" + n + ") -> Ops: " + promOpsA + " | Tiempo: " + promTiempoA + " ms\n");

            // ==========================================
            // PROCESO PARA ALGORITMO B
            // ==========================================
            System.out.println("--- Ejecutando ALGORITMO B con n = " + n + " ---");
            long sumaOpsB = 0;
            double sumaTiempoB = 0;

            for (int r = 1; r <= repeticiones; r++) {
                int[] datos = generarDataset(n);
                Resultado res = ejecutarAlgoritmoB(datos);

                sumaOpsB += res.operaciones;
                sumaTiempoB += res.tiempoMs;

                System.out.println("  [Intento " + r + "] -> Operaciones: " + res.operaciones + " | Tiempo: " + res.tiempoMs + " ms");
            }

            double promOpsB = (double) sumaOpsB / repeticiones;
            double promTiempoB = sumaTiempoB / repeticiones;
            tablaB[k][0] = n;
            tablaB[k][1] = promOpsB;
            tablaB[k][2] = promTiempoB;

            System.out.println("  >> PROMEDIO Algoritmo B (n=" + n + ") -> Ops: " + promOpsB + " | Tiempo: " + promTiempoB + " ms\n");
        }

        // ==========================================
        // IMPRESIÓN DE LA TABLA FINAL DE RESULTADOS
        // ==========================================
        System.out.println("==================================================================================");
        System.out.println("                         TABLA EXPERIMENTAL FINAL                                 ");
        System.out.println("==================================================================================");
        System.out.println(String.format("| %-12s | %-10s | %-22s | %-20s |", "Algoritmo", "n", "operaciones", "tiempo"));
        System.out.println("----------------------------------------------------------------------------------");

        for (int k = 0; k < tamanios.length; k++) {
            System.out.println(String.format("| %-12s | %-10.0f | %-22.2f | %-17.4f ms |", "A", tablaA[k][0], tablaA[k][1], tablaA[k][2]));
        }
        for (int k = 0; k < tamanios.length; k++) {
            System.out.println(String.format("| %-12s | %-10.0f | %-22.2f | %-17.4f ms |", "B", tablaB[k][0], tablaB[k][1], tablaB[k][2]));
        }
        System.out.println("==================================================================================");
    }

    // Generador de dataset aleatorio (misma semilla: mismos datos en cada repetición)
    public static int[] generarDataset(int n) {
        int[] datos = new int[n];
        Random rand = new Random(42);
        for (int i = 0; i < n; i++) {
            datos[i] = rand.nextInt(10000);
        }
        return datos;
    }

    static class Resultado {
        long operaciones;
        double tiempoMs;
        public Resultado(long op, double t) {
            this.operaciones = op;
            this.tiempoMs = t;
        }
    }

    // ALGORITMO A (Bucle Anidado)
    public static Resultado ejecutarAlgoritmoA(int[] datos) {
        int n = datos.length;
        long operaciones = 0;
        long inicio = System.nanoTime();

        for (int i = 0; i < n; i++) {
            operaciones++;
            if (datos[i] == -1) continue;

            int actual = datos[i];
            boolean repite = false;

            for (int j = i + 1; j < n; j++) {
                operaciones++;
                if (datos[j] == actual) {
                    repite = true;
                    break;
                }
            }
            if (repite) {
                operaciones++;
                datos[i] = -1;
            }
        }

        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;
        return new Resultado(operaciones, tiempoMs);
    }

    // ALGORITMO B (Tabla de Frecuencias)
    public static Resultado ejecutarAlgoritmoB(int[] datos) {
        int n = datos.length;
        if (n == 0) {
            return new Resultado(0, 0);
        }
        long operaciones = 0;
        long inicio = System.nanoTime();

        int max = datos[0];
        for (int i = 1; i < n; i++) {
            operaciones++;
            if (datos[i] > max) max = datos[i];
        }

        int[] frecuencias = new int[max + 1];
        for (int i = 0; i < n; i++) {
            operaciones++;
            frecuencias[datos[i]]++;
        }

        // Se recorre de nuevo: frecuencias[val] pasa a indicar cuántas apariciones FALTAN por ver.
        // Si todavía faltan (> 0), este elemento se repite más adelante y se marca, igual que en A.
        for (int i = 0; i < n; i++) {
            operaciones++;
            int val = datos[i];
            frecuencias[val]--;
            if (frecuencias[val] > 0) {
                operaciones++;
                datos[i] = -1;
            }
        }

        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;
        return new Resultado(operaciones, tiempoMs);
    }
}
