///////////////////////////////////////////////////////////////////////////////////////////////////
import com.github.tototoshi.csv.*
import java.io.File
object LectorCSV {
  def lectura(): List[Map[String, String]] = {
    implicit object MiFormato extends DefaultCSVFormat {
      override val delimiter: Char = ';'
    }
    val pathMoviesBIG: String = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Data\\pi_movies_complete.csv"
    val reader = CSVReader.open(new File(pathMoviesBIG))
    val dataMap: List[Map[String, String]] = reader.allWithHeaders()
    dataMap
  }

}

///////////////////////////////////////////////////////////////////////////////////////////////////