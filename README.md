# Laboratorio 02: Eficiencia y complejidad algorítmica

**Materia:** Algoritmos · Semana 2 · Universidad Central del Ecuador

**Carrera:** Ingeniería en Sistemas de Información · Cuarto semestre

**Autores:** Derlis Caiza ([@goderlis11](https://github.com/goderlis11)) y Juan Naranjo ([@janr016](https://github.com/janr016))

## Problema

Dada una lista de enteros, identificar los valores repetidos y marcarlos con `-1`. Se comparan **dos estrategias correctas** que producen la misma salida, midiendo cuántas operaciones ejecutan y cuánto tardan cuando crece el tamaño de entrada `n`.

Salida común: cada elemento que se vuelve a encontrar más adelante en la lista se marca con `-1` (se conserva solo la última aparición de cada valor).

## Estrategias comparadas

| | Algoritmo A | Algoritmo B |
|---|---|---|
| Nombre | Bucle anidado (doble recorrido) | Tabla de frecuencias |
| Idea | Comparar cada elemento contra los que vienen después | Contar apariciones en un arreglo auxiliar y recorrer restando |
| Memoria extra | Ninguna | Arreglo de tamaño `max + 1` |
| Restricción | Ninguna | Solo enteros no negativos y de rango acotado |

## Hipótesis

El algoritmo B escalará mejor que el A. A debería crecer de forma cuadrática (≈ n²) porque compara cada elemento con los siguientes, mientras que B debería crecer de forma lineal (≈ n) porque procesa cada elemento un número fijo de veces. A cambio, B consumirá memoria adicional.

## Condiciones de prueba

| Condición | Valor |
|---|---|
| Lenguaje | Java |
| Dataset | Aleatorio, valores entre 0 y 9999, generado con `new Random(42)` |
| Dataset compartido | Sí: A y B reciben exactamente los mismos datos en cada prueba |
| Tamaños de entrada (n) | 100 y 1.000 |
| Repeticiones | 3 por cada combinación algoritmo/tamaño; se reporta el promedio |
| Métricas | Operaciones (contadas en el código) y tiempo en ms (`System.nanoTime()`) |
| Verificación | Antes de medir, se comprueba que A y B marcan las mismas posiciones |
| Ambiente | Procesador: _completar_ · RAM: _completar_ · SO: _completar_ · IDE/JDK: _completar_ |

## Resultados

Promedio de 3 repeticiones (ver captura completa en `results/`):

| Algoritmo | n | Operaciones | Tiempo (ms) |
|---|---|---|---|
| A | 100 | 4.989 | 0,01912 |
| A | 1.000 | 479.764 | 5,4678 |
| B | 100 | 300 | 0,0950 |
| B | 1.000 | 3.061 | 0,1984 |

> Las operaciones son siempre iguales entre ejecuciones; los tiempos varían un poco según la máquina y el momento.

## Conclusiones

- Al multiplicar `n` por 10, las operaciones de **A se multiplican por ≈ 96** (crecimiento cuadrático) y las de **B por ≈ 10** (crecimiento lineal).
- Con n = 1.000, A ejecuta **≈ 157 veces más operaciones** que B.
- B escala mejor, pero a costa de memoria extra y de aceptar solo enteros no negativos de rango acotado. A no usa memoria adicional y sirve para cualquier tipo comparable.
- El tiempo es más inestable que las operaciones (efecto de calentamiento de la JVM y ruido del sistema), por lo que las operaciones son la evidencia principal.

El análisis completo está en [`analysis.md`](analysis.md).

## Estructura del repositorio

```
├── README.md      Problema, hipótesis, condiciones de prueba y conclusiones
├── analysis.md    Interpretación técnica de los resultados
├── src/           Implementaciones instrumentadas (ProcesoBenchmarking.java)
└── results/       Tablas n/tiempo/operaciones y capturas de ejecución
```

## Cómo ejecutar

```bash
cd src
javac ProcesoBenchmarking.java
java ProcesoBenchmarking
```

También puede ejecutarse desde NetBeans con *Run File* sobre `ProcesoBenchmarking.java`. Para probar otros tamaños, se modifica el arreglo `tamanios` en el método `main`.
