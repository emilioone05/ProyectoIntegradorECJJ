import java.io.{BufferedWriter, FileWriter}
object PobladorFINAL {

  def escapeSQL(valor: String): String = {
    if (valor.isEmpty) "null"
    else valor
      .replaceAll("'", "''") // Escapar comillas simples
      .replaceAll("\"", "\\\"") // Escapar comillas dobles
      .replaceAll("\b", "\\b") // Escapar backspace
      .replaceAll("\n", "\\n") // Escapar nueva línea
      .replaceAll("\r", "\\r") // Escapar retorno de carro
      .replaceAll("\t", "\\t") // Escapar tabulación
      .replaceAll("\u0000", "") // Eliminar caracteres nulos
      .replaceAll("\u001a", "\\Z") // Escapar fin de archivos
  }

  def insertarCollectionDB(listaTuplas: List[(Int, String, String, String)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\1colection.sql"
    def insertar(tupla: (Int, String, String, String)): String = {
      val collection_id = escapeSQL(tupla._1.toString)
      val nombre = escapeSQL(tupla._2)
      val poster_path = escapeSQL(tupla._3)
      val backdrop_path = escapeSQL(tupla._4)
      s"INSERT INTO collection (collection_id, nombre, poster_path,backdrop_path) VALUES (" +
        s"$collection_id," +
        s"CASE WHEN '$nombre' = 'NULL' THEN NULL ELSE '$nombre' END," +
        s"CASE WHEN '$poster_path' = 'NULL' THEN NULL ELSE '$poster_path' END," +
        s"CASE WHEN '$backdrop_path' = 'NULL' THEN NULL ELSE '$backdrop_path' END);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  //------------------------------------------------------------------------------------------------------------------------------------------

  def insertarPeliculaDB(listaTuplas: List[(Int, String, Boolean, Long, String, String, String, String, Long, String, String, Long, Int, String, String, String, Boolean, Int, Int, Int)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\2peliculas.sql"
    def insertar(tupla: (Int, String, Boolean, Long, String, String, String, String, Long, String, String, Long, Int, String, String, String, Boolean, Int, Int, Int)): String = {
      val pelicula_id = tupla._1
      val imdb_id = escapeSQL(tupla._2)
      val adult = tupla._3
      val budget = tupla._4
      val homepage = escapeSQL(tupla._5)
      val og_lang = escapeSQL(tupla._6)
      val og_title = escapeSQL(tupla._7)
      val overview = escapeSQL(tupla._8)
      val popularity = tupla._9
      val poster_pt = escapeSQL(tupla._10)
      val release_dt = escapeSQL(tupla._11)
      val revenue = tupla._12
      val runtime = tupla._13
      val status = escapeSQL(tupla._14)
      val tagline = escapeSQL(tupla._15)
      val title = escapeSQL(tupla._16)
      val video = tupla._17
      val vote_cnt = tupla._18
      val vote_avg = tupla._19
      val coll_id = tupla._20
      s"INSERT INTO pelicula (pelicula_id, imdb_id, adult, budget, homepage, " +
        s"original_language,original_title, overview, popularity, poster_path, " +
        s"release_date, revenue, runtime, estado, tagline, title, video, " +
        s"vote_count, vote_average, collection_id)VALUES (" +
        s"$pelicula_id," +
        s"CASE WHEN '$imdb_id' = 'NULL' THEN NULL ELSE '$imdb_id' END," +
        s"$adult," +
        s"$budget," +
        s"CASE WHEN '$homepage' = 'NULL' THEN NULL ELSE '$homepage' END," +
        s"CASE WHEN LENGTH('$og_lang') > 2 THEN NULL ELSE '$og_lang' END," +
        s"CASE WHEN '$og_title' = 'NULL' THEN NULL ELSE '$og_title' END," +
        s"CASE WHEN '$overview' = 'NULL' THEN NULL ELSE '$overview' END," +
        s"$popularity," +
        s"CASE WHEN '$poster_pt' = 'NULL' THEN NULL ELSE '$poster_pt' END," +
        s"CASE WHEN '$release_dt' = 'NULL' THEN NULL ELSE '$release_dt' END," +
        s"$revenue," +
        s"$runtime," +
        s"CASE WHEN '$status' = 'NULL' THEN NULL ELSE '$status' END," +
        s"CASE WHEN '$tagline' = 'NULL' THEN NULL ELSE '$tagline' END," +
        s"CASE WHEN '$title' = 'NULL' THEN NULL ELSE '$title' END," +
        s"$video," +
        s"$vote_cnt," +
        s"$vote_avg," +
        s"CASE WHEN $coll_id = 0 THEN NULL ELSE $coll_id END);".stripMargin
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def insertarGeneroDB(listaTuplas: List[(Int, String)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\3genero.sql"
    def insertar(tupla: (Int, String)): String = {
      val genre_id = escapeSQL(tupla._1.toString)
      val nombre = escapeSQL(tupla._2)
      s"INSERT INTO genero(genre_id, nombre) VALUES (" +
        s"$genre_id," +
        s"CASE WHEN '$nombre' = 'NULL' THEN NULL ELSE '$nombre' END);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  def insertarPeliculaGeneroDB(listaTuplas: List[(Int, Int)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\4peliculaGen.sql"
    def insertar(tupla: (Int, Int)): String = {
      val genre_id = escapeSQL(tupla._2.toString)//
      val pelicula_id = escapeSQL(tupla._1.toString)
      s"INSERT INTO pelicula_genero(genre_id, pelicula_id) VALUES ($genre_id," +
        s"$pelicula_id);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def insertarProdcutoraDB(listaTuplas: List[(Int, String)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\5productora.sql"
    def insertar(tupla: (Int, String)): String = {
      val productora_id = escapeSQL(tupla._1.toString)
      val nombre = escapeSQL(tupla._2)
      s"INSERT INTO productora(productora_id, nombre) VALUES (" +
        s"$productora_id," +
        s"CASE WHEN '$nombre' = 'NULL' THEN NULL ELSE '$nombre' END);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  def insertarPeliculaProductoraDB(listaTuplas: List[(Int, Int)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\6peliculaProductora.sql"
    def insertar(tupla: (Int, Int)): String = {
      val productora_id = escapeSQL(tupla._1.toString)
      val pelicula_id = escapeSQL(tupla._2.toString)
      s"INSERT INTO pelicula_productora(productora_id, pelicula_id) VALUES ($productora_id," +
        s"$pelicula_id);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  //------------------------------------------------------------------------------------------------------------------------------------------

  def insertarPaisDB(listaTuplas: List[(String, String)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\7pais.sql"
    def insertar(tupla: (String, String)): String = {
      val iso_3166_1_code = escapeSQL(tupla._1)
      val nombre = escapeSQL(tupla._2)
      s"INSERT INTO pais(iso_3166_1_code, nombre) VALUES (" +
        s"'$iso_3166_1_code'," +
        s"CASE WHEN '$nombre' = 'NULL' THEN NULL ELSE '$nombre' END);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  def insertarPeliculaPaisDB(listaTuplas: List[(String, Int)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\8peliculapPais.sql"
    def insertar(tupla: (String, Int)): String = {
      val iso_3166_1_code = escapeSQL(tupla._1)
      val pelicula_id = escapeSQL(tupla._2.toString)
      s"INSERT INTO pelicula_pais(iso_3166_1_code, pelicula_id) VALUES (" +
        s"'$iso_3166_1_code'," +
        s"$pelicula_id);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def insertarIdiomaDB(listaTuplas: List[(String, String)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\9idioma.sql"
    def insertar(tupla: (String, String)): String = {
      val iso_639_1_code = escapeSQL(tupla._1)
      val nombre = escapeSQL(tupla._2)
      s"INSERT INTO idioma(iso_639_1_code, nombre) VALUES (" +
        s"'$iso_639_1_code'," +
        s"CASE WHEN '$nombre' = 'NULL' THEN NULL ELSE '$nombre' END);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  def insertarPeliculaIdiomaDB(listaTuplas: List[(String, Int)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\10peliculaIdioma.sql"
    def insertar(tupla: (String, Int)): String = {
      val iso_639_1_code = escapeSQL(tupla._1)
      val pelicula_id = escapeSQL(tupla._2.toString)
      s"INSERT INTO pelicula_idioma(iso_639_1_code, pelicula_id) VALUES (" +
        s"'$iso_639_1_code'," +
        s"$pelicula_id);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def insertarKeyWordDB(listaTuplas: List[(Int, String)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\11keyword.sql"
    def insertar(tupla: (Int, String)): String = {
      val id_KW = escapeSQL(tupla._1.toString)
      val nombre = escapeSQL(tupla._2)
      s"INSERT INTO key_word(id_KW, nombre) VALUES (" +
        s"$id_KW," +
        s"CASE WHEN '$nombre' = 'NULL' THEN NULL ELSE '$nombre' END);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  def insertarPeliculaKeyWordDB(listaTuplas: List[(Int, Int)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\12peliculaKeyword.sql"
    def insertar(tupla: (Int, Int)): String = {
      val id_KW = escapeSQL(tupla._1.toString)
      val pelicula_id = escapeSQL(tupla._2.toString)
      s"INSERT INTO pelicula_keyWord(id_KW, pelicula_id) VALUES (" +
        s"$id_KW," +
        s"$pelicula_id);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def insertarActorDB(listaTuplas: List[(Int, String, Int, String)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\13actor.sql"
    def insertar(tupla: (Int, String, Int, String)): String = {
      val actor_id = escapeSQL(tupla._1.toString)
      val nombre = escapeSQL(tupla._2)
      val gender = escapeSQL(tupla._3.toString)
      val profile_path = escapeSQL(tupla._4)
      s"INSERT INTO actor(actor_id, nombre, gender,profile_path) VALUES (" +
        s"$actor_id," +
        s"CASE WHEN '$nombre' = 'NULL' THEN NULL ELSE '$nombre' END," +
        s"$gender," +
        s"CASE WHEN '$nombre' = 'NULL' THEN NULL ELSE '$nombre' END);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  def insertarPeliculaActorDB(listaTuplas: List[(Int, Int, String, Int, String, Int)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\14peliculaActor.sql"
    def insertar(tupla: (Int, Int, String, Int, String, Int)): String = {
      val actor_id = escapeSQL(tupla._1.toString)
      val pelicula_id = escapeSQL(tupla._2.toString)
      val personaje = escapeSQL(tupla._3)
      val orden = escapeSQL(tupla._4.toString)
      val credit_id = escapeSQL(tupla._5)
      val cast_id = escapeSQL(tupla._6.toString)
      s"INSERT INTO pelicula_actor(actor_id, pelicula_id, personaje,orden,credit_id,cast_id) VALUES (" +
        s"$actor_id," +
        s"$pelicula_id," +
        s"CASE WHEN '$personaje' = 'NULL' THEN NULL ELSE '$personaje' END," +
        s"$orden," +
        s"CASE WHEN '$credit_id' = 'NULL' THEN NULL ELSE '$credit_id' END," +
        s"$cast_id);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def insertarEmpleadoDB(listaTuplas: List[(Int, String, Int, String)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\15empleado.sql"
    def insertar(tupla: (Int, String, Int, String)): String = {
      val empleado_id = escapeSQL(tupla._1.toString)
      val nombre = escapeSQL(tupla._2)
      val gender = escapeSQL(tupla._3.toString)
      val profile_path = escapeSQL(tupla._4)
      s"INSERT INTO empleado(empleado_id, nombre, gender,profile_path) VALUES (" +
        s"$empleado_id," +
        s"CASE WHEN '$nombre' = 'NULL' THEN NULL ELSE '$nombre' END," +
        s"$gender," +
        s"CASE WHEN '$profile_path' = 'NULL' THEN NULL ELSE '$profile_path' END);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  def insertarJobDeparDB(listaTuplas: List[(String,String)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\16jobDepart.sql"
    def insertar(tupla: (String,String)): String = {
      val job_name = tupla._1
      val department_id = escapeSQL(tupla._2)

      s"INSERT INTO job_depart(job_name, department_id ) VALUES (" +
        s"'$job_name'," +
        s"CASE WHEN '$department_id' = 'NULL' THEN NULL ELSE '$department_id' END);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.distinctBy(_._1).foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  def insertarPeliculaEmpleadoDB(listaTuplas: List[(Int, Int, String, String)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\17peliculaEmpleado.sql"
    def insertar(tupla: (Int, Int, String, String)): String = {
      val empleado_id = escapeSQL(tupla._1.toString)
      val pelicula_id = escapeSQL(tupla._2.toString)
      val credit_id = escapeSQL(tupla._3)
      val job_name = escapeSQL(tupla._4)
      s"INSERT INTO pelicula_empleado(empleado_id, pelicula_id, credit_id,job_name) VALUES (" +
        s"$empleado_id," +
        s"$pelicula_id," +
        s"CASE WHEN '$credit_id' = 'NULL' THEN NULL ELSE '$credit_id' END," +
        s"CASE WHEN '$job_name' = 'NULL' THEN NULL ELSE '$job_name' END);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }
  //------------------------------------------------------------------------------------------------------------------------------------------

  def insertarUsuarioDB(listaTuplas: List[(Int)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\18usuario.sql"
    def insertar(tupla: (Int)): String = {
      val user_id = escapeSQL(tupla.toString)
      s"INSERT INTO usuario(user_id) VALUES ($user_id);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

  def insertarCalificacionDB(listaTuplas: List[(Int, Int, Float, Long)]): Unit = {
    val filePath = "C:\\Users\\ASUS FLOW\\Desktop\\proyectoIntegraFinal\\Scripts\\19calificacione.sql"
    def insertar(tupla: (Int, Int, Float, Long)): String = {
      val user_id = escapeSQL(tupla._1.toString)
      val pelicula_id = escapeSQL(tupla._2.toString)
      val rating = escapeSQL(tupla._3.toString)
      val timestamps = escapeSQL(tupla._4.toString)
      s"INSERT INTO calificacion(user_id, pelicula_id, rating,timestamps) VALUES (" +
        s"$user_id," +
        s"$pelicula_id," +
        s"$rating," +
        s"$timestamps);"
    }

    val file = new BufferedWriter(new FileWriter(filePath))
    listaTuplas.foreach { fila =>
      file.write(insertar(fila))
      file.newLine()
    }
    file.close()
  }

}