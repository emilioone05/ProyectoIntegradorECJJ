import java.sql.{Connection, DriverManager, ResultSet}

object CrudConsultas {
  def main(args: Array[String]): Unit = {
    val url = "jdbc:mysql://localhost:3306/pf2"  // Reemplazar con la URL de la BD
    val user = "root"  // Reemplazar con el usuario de la BD
    val password = "935475"  // Reemplazar con la contraseña de la BD

    // Lista de consultas SQL
    val queries = List(
      "SELECT p.title, COUNT(pg.genre_id) AS cantidad_generos FROM pelicula p JOIN pelicula_genero pg ON p.pelicula_id = pg.pelicula_id GROUP BY p.title HAVING COUNT(pg.genre_id) > 1;",
      "SELECT collection.collection_id, nombre, poster_path, backdrop_path FROM collection WHERE collection_id = 99900;"
      //"SELECT p.title FROM pelicula p WHERE p.pelicula_id IN (SELECT pg.pelicula_id FROM pelicula_genero pg GROUP BY pg.pelicula_id HAVING COUNT(pg.genre_id) > 1);",
      //"SELECT p.title, COUNT(pg.genre_id) AS cantidad_generos FROM pelicula p JOIN pelicula_genero pg ON p.pelicula_id = pg.pelicula_id GROUP BY p.pelicula_id, p.title HAVING COUNT(pg.genre_id) > 1;"
      //"SELECT g.nombre_genero, COUNT(p.pelicula_id) FROM genero g JOIN pelicula_genero pg ON g.genero_id = pg.genero_id JOIN pelicula p ON pg.pelicula_id = p.pelicula_id GROUP BY g.nombre_genero;"
    )
    var connection: Connection = null
    try {
      // Conectar a la base de datos
      connection = DriverManager.getConnection(url, user, password)
      val statement = connection.createStatement()

      // Ejecutar cada consulta
      for ((query, index) <- queries.zipWithIndex) {
        println(s"Consulta ${index + 1}:")
        val resultSet = statement.executeQuery(query)
        printResults(resultSet)
        println("\n------------------------------\n")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (connection != null) connection.close()
    }
  }
  // Función para imprimir los resultados de la consulta
  def printResults(resultSet: ResultSet): Unit = {
    val metaData = resultSet.getMetaData
    val columnCount = metaData.getColumnCount

    while (resultSet.next()) {
      for (i <- 1 to columnCount) {
        print(s"${metaData.getColumnName(i)}: ${resultSet.getString(i)}  ")
      }
      println()
    }
  }
}
