// Importamos la biblioteca necesaria para trabajar con archivos CSV
import com.github.tototoshi.csv.{CSVReader, DefaultCSVFormat}
import java.io.File

// Configuramos el formato del archivo CSV con el delimitador correspondiente
// Esto asegura que los valores dentro del CSV se separen correctamente por ';'
implicit object CSVFormatter extends DefaultCSVFormat {
  override val delimiter: Char = ';'
}

// Ruta del archivo CSV que contiene los datos
val rutaArchivo: String = "C:\\Users\\emili\\OneDrive\\Escritorio\\small_pi_movies.csv\\small_pi_movies.csv"

// Abrimos el archivo CSV utilizando la ruta especificada
val lectorCSV = CSVReader.open(new File(rutaArchivo))

// Cargamos todos los datos del archivo como una lista de mapas
// Cada fila del archivo se mapea como un diccionario (clave-valor), donde las claves son los encabezados
val datos: List[Map[String, String]] = lectorCSV.allWithHeaders()

/*
  Proceso para extraer y trabajar con la columna "budget" (presupuesto):
  - A veces, los valores en esta columna no son números válidos o están vacíos. Esto puede causar errores.
  - Para evitar esto, usamos un proceso de validación:
    1. Obtenemos los valores de la columna "budget".
    2. Intentamos convertir esos valores a números largos (Long).
    3. Ignoramos valores no válidos o que sean cero.
*/
val presupuestos: List[Long] = datos
  .flatMap(fila => fila.get("budget") // Obtenemos los valores de la columna "budget" como Option[String]
    .flatMap(valor => scala.util.Try(valor.toLong) // Intentamos convertirlos a Long
      .toOption)) // Si falla, lo ignoramos con .toOption
  .filter(_ != 0) // Filtramos los presupuestos que sean cero

// Imprimimos los valores únicos de la columna "adult" (que indica si la película es para adultos o no)
val valoresAdult: List[String] = datos.flatMap(fila => fila.get("adult")) // Obtenemos los valores de la columna "adult"
valoresAdult.distinct.foreach(println) // Imprimimos los valores únicos

/*
  Agrupamos los valores de la columna "adult" para contar cuántas veces aparece cada uno.
  Básicamente, estamos haciendo un resumen de cuántas películas son para adultos y cuántas no.
  - groupBy(identity): Agrupa por el propio valor.
  - map(x => (x._1, x._2.size)): Crea pares (valor, cantidad de veces que aparece).
*/
val resumenAdult: Map[String, Int] = valoresAdult
  .groupBy(identity)
  .map { case (valor, lista) => (valor, lista.size) }

// Imprimimos el resumen
resumenAdult.foreach { case (valor, cantidad) =>
  println(s"$valor: $cantidad")
}

/*
  EXPLICACIÓN DE ALGUNOS CONCEPTOS USADOS:
  - Option: Representa un valor que puede existir o no (es como un "tal vez").
  - flatMap: Se utiliza para "aplanar" estructuras anidadas y eliminar valores vacíos.
  - Try: Es una forma de manejar errores en Scala, similar a un try-catch en otros lenguajes.
    Aquí lo usamos para intentar convertir valores a números sin que el programa se rompa si algo falla.
 */
