# Análisis técnico: ¿cuál algoritmo escala mejor y por qué?

## 1. Qué se midió

Se compararon dos algoritmos que marcan con `-1` los valores repetidos de una lista, usando los mismos datos (`Random(42)`, valores de 0 a 9999) y 3 repeticiones por prueba. Antes de medir se verificó que **ambos marcan las mismas posiciones** (`SI` para n = 100 y n = 1.000), de modo que la comparación es entre soluciones equivalentes.

## 2. Resultados experimentales

| Algoritmo | n | Operaciones (promedio) | Tiempo promedio (ms) |
|---|---|---|---|
| A | 100 | 4.989 | 0,0444 |
| A | 1.000 | 479.764 | 2,1950 |
| B | 100 | 300 | 0,0238 |
| B | 1.000 | 3.061 | 0,0672 |

### Crecimiento al pasar de n = 100 a n = 1.000 (n × 10)

| Algoritmo | Operaciones n = 100 | Operaciones n = 1.000 | Factor |
|---|---|---|---|
| A | 4.989 | 479.764 | **× 96** |
| B | 300 | 3.061 | **× 10** |

Si un algoritmo es lineal, al multiplicar `n` por 10 el trabajo se multiplica por ≈ 10. Si es cuadrático, se multiplica por ≈ 100. Los datos coinciden con lo esperado: **A se comporta como O(n²)** y **B como O(n)**.

## 3. Por qué ocurre

**Algoritmo A (bucle anidado).** Para cada elemento `i` recorre los siguientes `j = i + 1 … n - 1` hasta encontrar un repetido. En el peor caso (sin duplicados) hace `n + n(n - 1) / 2` operaciones; para n = 100 serían 5.050. Se observaron 4.989 porque el `break` corta la búsqueda en cuanto aparece un duplicado. Cada elemento puede compararse contra casi todos los demás, de ahí el crecimiento cuadrático.

**Algoritmo B (tabla de frecuencias).** Hace tres pasadas sobre los datos (buscar el máximo, contar apariciones y marcar repetidos), y cada elemento se procesa un número fijo de veces: ≈ 3n operaciones (300 para n = 100; 3.061 para n = 1.000). Consultar `frecuencias[valor]` es acceso directo, sin buscar. Por eso crece de forma lineal.

## 4. Costos y límites de cada estrategia

| Aspecto | Algoritmo A | Algoritmo B |
|---|---|---|
| Crecimiento de operaciones | ≈ n² | ≈ n |
| Memoria adicional | Ninguna | Arreglo de tamaño `max + 1` (aquí 10.000 enteros) |
| Tipo de datos | Cualquiera comparable | Solo enteros ≥ 0 con rango acotado |
| Entrada vacía | Funciona | Requiere validación (`n == 0`) |
| Cuándo conviene | Listas pequeñas o datos sin restricciones | Listas grandes de enteros con rango conocido |

B falla con valores negativos (el índice `frecuencias[-3]` no existe) y se vuelve costoso en memoria si el valor máximo es muy grande (por ejemplo, un solo dato de 2.000.000.000).

## 5. Validez de la medición (limitaciones)

- **El tiempo es menos confiable que las operaciones.** Las operaciones son idénticas en cada ejecución; el tiempo cambia por el calentamiento de la JVM (compilación JIT) y por la carga del sistema. En una ejecución previa, A con n = 1.000 tardó 3,3 ms en el primer intento y ≈ 1,2 ms en los siguientes.
- **Solo 3 repeticiones.** Cumplen el mínimo pedido, pero con más repeticiones el promedio sería más estable.
- **Misma semilla en todas las repeticiones.** Garantiza reproducibilidad y comparabilidad, pero las 3 repeticiones usan datos idénticos.
- **Rango de valores fijo (0 a 9999).** Con n pequeño hay pocos duplicados; con n muy grande casi todos los valores se repiten pronto y el `break` de A corta la búsqueda.

### Extensión opcional: 5 tamaños de entrada

Ejecutando el mismo código con `tamanios = {100, 1000, 10000, 100000, 1000000}` se obtuvieron estas operaciones (los tiempos varían entre ejecuciones y por eso no se comparan aquí):

| n | Operaciones A | Operaciones B | Factor de A al subir n × 10 | Factor de B |
|---|---|---|---|---|
| 100 | 4.989 | 300 | — | — |
| 1.000 | 479.764 | 3.061 | × 96 | × 10 |
| 10.000 | 36.678.149 | 33.648 | × 76 | × 11 |
| 100.000 | 899.970.266 | 389.999 | × 25 | × 12 |
| 1.000.000 | 9.901.770.266 | 3.989.999 | × 11 | × 10 |

Los factores de A bajan de 96 a 11 porque los datos solo tienen 10.000 valores posibles: con n grande, casi cualquier elemento encuentra su duplicado enseguida y el `break` evita el recorrido completo. **Esto no significa que A sea lineal**: es un efecto del dataset. Para observar el peor caso cuadrático de A habría que probar una lista **sin duplicados** (todos los valores únicos).

## 6. Conclusión técnica

**El algoritmo B conviene cuando se procesan muchos enteros no negativos de rango acotado**, porque su trabajo crece de forma lineal (≈ 3n operaciones) frente al crecimiento cuadrático de A (× 96 al multiplicar n por 10). Con n = 1.000, A ejecuta ≈ 157 veces más operaciones que B.

**El algoritmo A conviene cuando no se quiere usar memoria adicional o los datos no cumplen las restricciones de B** (negativos, tipos no enteros o rangos enormes), y para listas pequeñas donde la diferencia es despreciable.

Cada afirmación se respalda en la tabla de operaciones, que es reproducible con `new Random(42)`, y en el razonamiento de cuántas veces se recorre cada elemento.
