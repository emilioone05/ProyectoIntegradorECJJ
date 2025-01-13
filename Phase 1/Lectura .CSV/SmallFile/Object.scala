// Importamos las bibliotecas necesarias para trabajar con archivos CSV
import com.github.tototoshi.csv.{CSVReader, DefaultCSVFormat}
import java.io.File

// Definimos el objeto principal que contiene el programa
object Proyecto {

  // Punto de entrada principal del programa
  @main
  def main(): Unit = {
    // Configuramos el formato del CSV con el delimitador correcto (';')
    implicit object CSVFormatter extends DefaultCSVFormat {
      override val delimiter: Char = ';'
    }

    // Definimos la ruta del archivo CSV
    val rutaArchivo: String = "C:\\Users\\emili\\OneDrive\\Escritorio\\small_pi_movies.csv\\small_pi_movies.csv"

    // Creamos un lector para abrir el archivo CSV
    val lectorCSV = CSVReader.open(new File(rutaArchivo))

    // Cargamos los datos del archivo como una lista de mapas con encabezados
    // Cada fila es un mapa donde las claves son los nombres de las columnas
    val datos: List[Map[String, String]] = lectorCSV.allWithHeaders()

    /*Procesamiento de la columna "budget" (presupuesto):
       - Extraemos los valores de presupuesto como una lista de números largos (Long)
    val presupuestos: List[Long] = extraerColumnaNumerica("budget", datos)
    println(s"Presupuestos: $presupuestos")

    // Calculamos algunos valores estadísticos de la columna "budget"
    println(s"Promedio de presupuesto: ${promedio(presupuestos)}")
    println(s"Presupuesto mínimo: ${minimo("budget", datos)}")
    println(s"Presupuesto máximo: ${maximo("budget", datos)}")
    */
    // Cerramos el lector de CSV (buena práctica para liberar recursos)
    lectorCSV.close()
  }

  /*
   * Función para calcular el promedio de una lista de valores
   * - Se ignoran valores negativos o cero.
   * - Utiliza `foldLeft` para acumular la suma y el conteo de elementos válidos.
   */
  def promedio(valores: List[Long]): Double = {
    val (suma, cantidad) = valores.filter(_ > 0).foldLeft((0.0, 0)) { case ((sumaAcumulada, contador), valor) =>
      (sumaAcumulada + valor, contador + 1)
    }
    if (cantidad > 0) suma / cantidad else 0.0 // Evitamos divisiones por cero
  }

  /*
   * Función para calcular el valor mínimo de una columna numérica
   * - Convierte los valores de la columna especificada a Double.
   * - Usa `minOption` para manejar listas vacías (retorna 0.0 si no hay datos válidos).
   */
  def minimo(columna: String, datos: List[Map[String, String]]): Double = {
    datos
      .flatMap(fila => fila.get(columna).flatMap(valor => scala.util.Try(valor.toDouble).toOption)) // Validamos y convertimos a Double
      .minOption // Obtenemos el valor mínimo (si existe)
      .getOrElse(0.0) // Retornamos 0.0 si no hay valores válidos
  }

  /*
   * Función para calcular el valor máximo de una columna numérica
   * - Similar a la función `minimo`, pero calcula el valor máximo.
   */
  def maximo(columna: String, datos: List[Map[String, String]]): Double = {
    datos
      .flatMap(fila => fila.get(columna).flatMap(valor => scala.util.Try(valor.toDouble).toOption)) // Validamos y convertimos a Double
      .maxOption // Obtenemos el valor máximo (si existe)
      .getOrElse(0.0) // Retornamos 0.0 si no hay valores válidos
  }

  /*
   * Función auxiliar para extraer valores numéricos de una columna
   * - Convierte valores de una columna específica en números largos (Long).
   * - Filtra los valores no válidos o que sean cero.
   
  def extraerColumnaNumerica(columna: String, datos: List[Map[String, String]]): List[Long] = {
    datos
      .flatMap(fila => fila.get(columna).flatMap(valor => scala.util.Try(valor.toLong).toOption)) // Validamos y convertimos a Long
      .filter(_ != 0) // Filtramos valores igual a cero
  }
  
   */
}
