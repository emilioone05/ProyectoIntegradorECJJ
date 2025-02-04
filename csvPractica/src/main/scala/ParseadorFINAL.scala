import play.api.libs.json.*

object ParseadorFINAL {


  def tablaCollection(dataMap: List[Map[String, String]]): List[(Int, String, String, String)] = {
    val tabla = dataMap.flatMap { mapa =>

      val jsValue = Json.parse(mapa("belongs_to_collection"))

      // Verificamos si el valor es un JsArray o un JsObject
      val jsArray = jsValue match {
        case arr: JsArray => arr // Si es un arreglo, lo mantenemos
        case obj: JsObject => JsArray(Seq(obj)) // Si es un objeto, lo convertimos en un array de un solo elemento
        case _ => JsArray() // En caso de otro tipo de datos (por ejemplo, null o algo mal formado), retornamos un arreglo vacío
      }

      // Procesamos los valores del JsArray
      jsArray.value.map { jsObj =>
        (jsObj("id").asOpt[Int].getOrElse(0), // Extraemos el ID, por defecto 0 si no existe
          jsObj("name").asOpt[String].getOrElse("null"), // Extraemos el nombre, por defecto "null"
          jsObj("poster_path").asOpt[String].getOrElse("null"), // Extraemos el poster_path, por defecto "null"
          jsObj("backdrop_path").asOpt[String].getOrElse("null") // Extraemos el backdrop_path, por defecto "null"
        )
      }
    }
    tabla
  }

  def tablaPelicula(dataMap: List[Map[String, String]]): List[(Int, String, Boolean, Long, String, String, String,
    String, Long, String, String, Long, Int, String, String, String, Boolean, Int, Int, Int)] = {
    val tabla = dataMap.map { mapa =>

      val jsValue = Json.parse(mapa("belongs_to_collection"))

      // Verificamos si el valor es un JsArray o un JsObject
      val js = jsValue match {
        case arr: JsArray => if arr.value.nonEmpty then arr("id").asOpt[Int].getOrElse(0) else 0 
        case obj: JsObject => obj("id").asOpt[Int].getOrElse(0) 
      }

      // Procesamos los valores del JsArray

      (mapa("id").toInt,
        mapa("imdb_id"),
        mapa("adult").toBoolean,
        mapa("budget").toLong,
        mapa("homepage"),
        mapa("original_language"),
        mapa("original_title"),
        mapa("overview"),
        mapa("popularity").toLong,
        mapa("poster_path"),
        mapa("release_date"),
        mapa("revenue").toLong,
        mapa("runtime").toInt,
        mapa("status"),
        mapa("tagline"),
        mapa("title"),
        mapa("video").toBoolean,
        mapa("vote_count").toInt,
        mapa("vote_average").toInt,
        js)

    }
    tabla
  }

 

  //------------------------------------------------------------------------------------------------------------------------------------------
  def tablaGenero(dataMap: List[Map[String, String]]): List[(Int, String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("genres"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value // Si es un arreglo, lo procesamos como un array de objetos
        case obj: JsObject => Seq(obj) // Si es un objeto, lo tratamos como un array con un solo objeto
        case _ => Seq() // Si no es ni un JsArray ni un JsObject, retornamos una lista vacía
      }
      jsObjects.map { jsObj =>
        (jsObj("id").as[Int],
          jsObj("name").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  def tablaPeliculaGenero(dataMap: List[Map[String, String]]): List[(Int, Int)] = {
    val tabla = dataMap.flatMap { mapa =>
      val id = mapa("id").toInt
      val jsArray = Json.parse(mapa("genres")).as[JsArray]

      jsArray.value.map { obj =>
        (id,
          obj("id").as[Int])
      }
    }
    tabla

  }
  //------------------------------------------------------------------------------------------------------------------------------------------


  def tablaCompanies(dataMap: List[Map[String, String]]): List[(Int,String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("production_companies"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("id").as[Int]
          ,jsObj("name").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  def tablaPeliculaProductora(dataMap: List[Map[String, String]]): List[(Int, Int)] = {
    val tabla = dataMap.flatMap { mapa =>
      val id = mapa("id").toInt
      val jsArray = Json.parse(mapa("production_companies")).as[JsArray]

      jsArray.value.map { obj =>
        (obj("id").as[Int],
          id)
      }
    }
    tabla.distinctBy(x => (x._1, x._2))

  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def tablaCountries(dataMap: List[Map[String, String]]):List[(String, String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("production_countries"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map {jsObj =>
        (jsObj("iso_3166_1").as[String],
          jsObj("name").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  def tablaPeliculaPais(dataMap: List[Map[String, String]]): List[(String, Int)] = {
    val tabla = dataMap.flatMap { mapa =>
      val id = mapa("id").toInt
      val jsArray = Json.parse(mapa("production_countries")).as[JsArray]

      jsArray.value.map { obj =>
        (obj("iso_3166_1").as[String],
          id)
      }
    }
    tabla.distinctBy(x => (x._1, x._2))

  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def tablaLanguages(dataMap: List[Map[String, String]]): List[(String, String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("spoken_languages"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("iso_639_1").as[String],
          jsObj("name").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  def tablaPeliculaIdioma(dataMap: List[Map[String, String]]): List[(String, Int)] = {
    val tabla = dataMap.flatMap { mapa =>
      val id = mapa("id").toInt
      val jsArray = Json.parse(mapa("spoken_languages")).as[JsArray]

      jsArray.value.map { obj =>
        (obj("iso_639_1").as[String],
          id)
      }
    }
    tabla//.distinctBy(x => (x._1,x._2))

  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def tablaKeywords(dataMap: List[Map[String, String]]): List[(Int, String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("keywords"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("id").as[Int],
          jsObj("name").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  def tablaPeliculaKeywords(dataMap: List[Map[String, String]]): List[(Int, Int)] = {
    val tabla = dataMap.flatMap { mapa =>
      val id = mapa("id").toInt
      val jsArray = Json.parse(mapa("keywords")).as[JsArray]

      jsArray.value.map { obj =>
        (obj("id").as[Int],
          id)
      }
    }
    tabla//.distinctBy(x => (x._1, x._2))

  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def tablaCast(dataMap: List[Map[String, String]]): List[(Int, String, String, Int, Int, String, Int, String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("cast"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("cast_id").as[Int],
          jsObj("character").asOpt[String].getOrElse("null"),
          jsObj("credit_id").asOpt[String].getOrElse("null"),
          jsObj("gender").asOpt[Int].getOrElse(0),
          jsObj("id").as[Int],
          jsObj("name").asOpt[String].getOrElse("null"),
          jsObj("order").as[Int],
          jsObj("profile_path").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  def tablaActor(dataMap: List[Map[String, String]]): List[(Int, String, Int, String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("cast"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("id").as[Int],
          jsObj("name").asOpt[String].getOrElse("null"),
          jsObj("gender").asOpt[Int].getOrElse(0),
          jsObj("profile_path").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  def tablaPeliculaCast(dataMap: List[Map[String, String]]): List[(Int, Int, String, Int, String, Int)] = {
    val tabla = dataMap.flatMap { mapa =>
      val id = mapa("id").toInt
      val jsArray = Json.parse(mapa("cast")).as[JsArray]

      jsArray.value.map { jsObj =>
        (jsObj("id").as[Int],
          id,
          jsObj("character").asOpt[String].getOrElse("null"),
          jsObj("order").as[Int],
          jsObj("credit_id").asOpt[String].getOrElse("null"),
          jsObj("cast_id").as[Int],
        )
      }
    }
    tabla //.distinctBy(x => (x._1, x._2))
  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def tablaCrew(dataMap: List[Map[String, String]]): List[(String, String, Int, Int, String, String, String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("crew"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("credit_id").as[String],
          jsObj("department").asOpt[String].getOrElse("null"),
          jsObj("gender").asOpt[Int].getOrElse(0),
          jsObj("id").as[Int],
          jsObj("job").asOpt[String].getOrElse("null"),
          jsObj("name").asOpt[String].getOrElse("null"),
          jsObj("profile_path").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  def tablaEmpleado(dataMap: List[Map[String, String]]): List[(Int, String, Int, String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("crew"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("id").as[Int],
          jsObj("name").asOpt[String].getOrElse("null"),
          jsObj("gender").asOpt[Int].getOrElse(0),
          jsObj("profile_path").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  def tablaJobDepart(dataMap: List[Map[String, String]]): List[(String, String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("crew"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("job").as[String],
          jsObj("department").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  def tablaPeliculaEmpleado(dataMap: List[Map[String, String]]): List[(Int, Int, String, String)] = {
    val tabla = dataMap.flatMap { mapa =>
      val id = mapa("id").toInt
      val jsArray = Json.parse(mapa("crew"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("id").as[Int],
          id,
          jsObj("credit_id").asOpt[String].getOrElse("null"),
          jsObj("job").asOpt[String].getOrElse("null"))
      }
    }
    tabla
  }

  //------------------------------------------------------------------------------------------------------------------------------------------
  def tablaRatings(dataMap: List[Map[String, String]]): List[(Int, Double, Long)] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("ratings"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("userId").asOpt[Int].getOrElse(0),
          jsObj("rating").asOpt[Double].getOrElse(0.0),
          jsObj("timestamp").asOpt[Long].getOrElse(0L))
      }
    }
    tabla
  }

  def tablaUsuario(dataMap: List[Map[String, String]]): List[Int] = {
    val tabla = dataMap.flatMap { mapa =>
      val jsArray = Json.parse(mapa("ratings"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        jsObj("userId").as[Int]
      }
    }
    tabla
  }

  def tablaCalificacion(dataMap: List[Map[String, String]]): List[(Int,Int,Float, Long)] = {
    val tabla = dataMap.flatMap { mapa =>
      val id = mapa("id").toInt
      val jsArray = Json.parse(mapa("ratings"))

      val jsObjects = jsArray match {
        case arr: JsArray => arr.value 
        case obj: JsObject => Seq(obj) 
        case _ => Seq() 
      }
      jsObjects.map { jsObj =>
        (jsObj("userId").asOpt[Int].getOrElse(0),
          id,
          jsObj("rating").asOpt[Float].getOrElse(0f),
          jsObj("timestamp").asOpt[Long].getOrElse(0L))
      }
    }
    tabla
  }



  //------------------------------------------------------------------------------------------------------------------------------------------


}
