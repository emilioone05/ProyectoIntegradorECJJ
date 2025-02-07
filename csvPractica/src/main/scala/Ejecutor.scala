import CRUD.*
import Limpiador.*
import ParseadorFINAL.*
import PobladorFINAL.*
import ConstructorBD.*
import LectorCSV.lectura
import cats.effect.IO
import doobie.util.transactor.Transactor
import doobie.implicits.toConnectionIOOps
import doobie.syntax.all.toConnectionIOOps
import doobie.syntax.connectionio.toConnectionIOOps
import cats.effect.unsafe.implicits.global

import scala.util.{Failure, Success, Try}
import java.sql.SQLException
import scala.io.StdIn
object Ejecutor {
  @main
  def main(): Unit = {
    
    val dataMap: List[Map[String, String]] = lectura()

    //=========LIMPIAR DATOS========
    println("Total de peliculas sin limpiar: " + dataMap.length)



    //JSONs
    val columnasJson: List[String] = List(
      "belongs_to_collection",
      "genres",
      "production_companies",
      "production_countries",
      "spoken_languages",
      "keywords",
      "cast",
      "crew",
      "ratings"
    )
    //NUMERICAS
    val columnasNumericas: List[String] = List(
      "budget",
      "id",
      "popularity",
      "revenue",
      "runtime",
      "vote_average",
      "vote_count"
    )
    //NO REPETIBLES
    val columnasNotNull: List[String] = List(
      "imdb_id",
      "id",
      "title")

    //%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@
    //                             LIMPIEZA DATOS NORMALES
    //%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@
    //---------------------------------------------------------------------------------------------
    //==Borrar espacios en los textos
    val dm1 = trimeador(dataMap)
    println("\nMAPEO = Espacios al inicio y final eliminados...")
    //---------------------------------------------------------------------------------------------
    //==Borrar peliculas sin id, imdb_id ni title
    val dm2 = borrarDatosVacios(dm1, columnasNotNull)
    println("\nFILTRO = Peliculas sin id, imdb_id, title borradas...")
    println("Total de peliculas con id, imdb_id, title: " + dm2.length)
    //---------------------------------------------------------------------------------------------
    //==Borrar peliculas con id repetido
    val dm3 = eliminarRepetidos("id", dm2)
    println("\nFILTRO =Borrando peliculas con ids repetidos...")
    println("Total de peliculas sin ids repetidos: " + dm3.length)
    //---------------------------------------------------------------------------------------------
    //==Borrar peliculas con imdb_id repetido
    val dm4 = eliminarRepetidos("imdb_id", dm3)
    println("\nFILTRO = Borrando peliculas con imdb_id repetidos...")
    println("Total de peliculas sin imdb_id repetidos: " + dm4.length)
    //---------------------------------------------------------------------------------------------
    //==Cambiar numeros negativos por "NULL"
    val dm5 = numerosNegativos(dm4, columnasNumericas)
    println("\nMAPEO = Numeros negativos reemplazados por 0...")
    //---------------------------------------------------------------------------------------------
    //==Numeros con notacion redondeados
    val dm6 = simplificarNumeros(dm5, columnasNumericas)
    println("\nMAPEO = Numeros redondeados...")
    //---------------------------------------------------------------------------------------------
    //==Espacios vacios llenados con NULL
    val dm7 = nulleador(dm6)
    println("\nMAPEO = Espacios vacios llenados con NULL...")
    //%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@
    //                             LIMPIEZA DATOS JSONs
    //%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@%%@@
    //---------------------------------------------------------------------------------------------
    //==Jsons adaptados para parseo adecuado
    val dm8 = limpiezaJson(dm7, columnasJson)
    println("\nMAPEO = Columnas Json corregidas y listas para parseo...")
    //---------------------------------------------------------------------------------------------

    //  CREACION DE TABLAS COMO LISTAS DE TUPLAS
    val collection = tablaCollection(dm8).distinctBy(_._1)

    val pelicula = tablaPelicula(dm8).distinctBy(_._1)
    val genero = tablaGenero(dm8).distinctBy(_._1)
    val pelicula_genero = tablaPeliculaGenero(dm8).distinctBy(x => (x._1, x._2))

    val idioma = tablaLanguages(dm8).distinctBy(_._1)
    val pelicula_idioma = tablaPeliculaIdioma(dm8).distinctBy(x => (x._1, x._2))

    val pais = tablaCountries(dm8).distinctBy(_._1)
    val pelicula_pais = tablaPeliculaPais(dm8).distinctBy(x => (x._1, x._2))

    val productora = tablaCompanies(dm8).distinctBy(_._1)
    val pelicula_productora = tablaPeliculaProductora(dm8).distinctBy(x => (x._1, x._2))

    val empleado = tablaEmpleado(dm8).distinctBy(_._1)
    val job_depart = tablaJobDepart(dm8).distinctBy(_._1)
    val pelicula_empleado = tablaPeliculaEmpleado(dm8).distinctBy(x => (x._1, x._2))

    val actor = tablaActor(dm8).distinctBy(x => x._1)
    val pelicula_actor = tablaPeliculaCast(dm8).distinctBy(x => (x._1, x._2))

    val keyword = tablaKeywords(dm8).distinctBy(_._1)
    val pelicula_keyword = tablaPeliculaKeywords(dm8).distinctBy(x => (x._1, x._2))

    val usuario = tablaUsuario(dm8).distinct
    val calificacion = tablaCalificacion(dm8).distinctBy(x => (x._1, x._2))




    // CONECCION CON LA DB PARA CREAR SU ESTRUCTURA
    construir()

    //GENERACION DE SCRIPTS SQL
    insertarCollectionDB(collection)
    insertarPeliculaDB(pelicula)

    insertarGeneroDB(genero)
    insertarPeliculaGeneroDB(pelicula_genero)

    insertarIdiomaDB(idioma)
    insertarPeliculaIdiomaDB(pelicula_idioma)

    insertarPaisDB(pais)
    insertarPeliculaPaisDB(pelicula_pais)

    insertarProdcutoraDB(productora)
    insertarPeliculaProductoraDB(pelicula_productora)

    insertarEmpleadoDB(empleado)
    insertarJobDeparDB(job_depart)
    insertarPeliculaEmpleadoDB(pelicula_empleado)

    insertarActorDB(actor)
    insertarPeliculaActorDB(pelicula_actor)

    insertarKeyWordDB(keyword)
    insertarPeliculaKeyWordDB(pelicula_keyword)

    insertarUsuarioDB(usuario)
    insertarCalificacionDB(calificacion)
    
    val xa = Transactor.fromDriverManager[IO](
      driver = "com.mysql.cj.jdbc.Driver",
      url = "jdbc:mysql://localhost:3306/pf2",
      user = "root",
      password = "935475",
      logHandler = None
    )
    while (true) {
      println(
        """====== BIENVENIDO ======
          |(1) Consultar pelicula segun id
          |(2) Consultar pelicula segun titulo
          |(3) Consultar peliculas segun genero
          |(4) Consultar peliculas segun collection_id
          |(5) Insertar Colection
          |(6) Insertar Pelicula
          |(0) Salir
          |""".stripMargin)

      print("Ingrese una opcion: ")
      var opcion = StdIn.readInt()
      opcion match
        case 1 =>
          println("Inserte el id a consultar")
          var consultaID = StdIn.readInt()
          var x = consultaXid(consultaID).transact(xa).unsafeRunSync()
          println("Todas las películas:")
          x.foreach(println)

        case 2 =>
          println("Inserte el titulo a consultar")
          var consultaTITLE = StdIn.readLine()
          var x = consultaXtitulo(consultaTITLE).transact(xa).unsafeRunSync()
          println("Todas las películas:")
          x.foreach(println)

        case 3 =>
          println("Inserte el genero a consultar")
          var consultaGENRE = StdIn.readInt()
          var x = consultaXgenero(consultaGENRE).transact(xa).unsafeRunSync()
          println("Todas las películas:")
          x.foreach(println)

        case 4 =>
          println("Inserte el id a consultar")
          var consultaCOLID = StdIn.readLine()
          var x = consultaXcolection(consultaCOLID).transact(xa).unsafeRunSync()
          println("Todas las películas:")
          x.foreach(println)
        case 5 =>
          print("Ingrese una collection_id: ")
          var collection_idCOL = StdIn.readInt()
          print("Ingrese un nombre: ")
          var nombreCOL = StdIn.readLine()
          print("Ingrese un poster_path: ")
          var poster_pathCOL = StdIn.readLine()
          print("Ingrese un backdrop_path: ")
          var backdrop_pathCOL = StdIn.readLine()
          insertCollection(collection_idCOL,nombreCOL,poster_pathCOL,backdrop_pathCOL).transact(xa).unsafeRunSync()
          println("--COLLECTION INSERTADA--")
        case 6 =>
          println(
            """====== ESCOGA QUE PELICULA DESEA INGRESAR ======
              |(1) Pelicula "1"
              |(2) Pelicula "2"
              |(3) Pelicula "3"
              |""".stripMargin)
          print("Ingrese una opcion: ")
          var opcion2 = StdIn.readInt()
          opcion2 match
            case 1 =>
              insertPelicula1().transact(xa).unsafeRunSync()
            case 2=>
              insertPelicula2().transact(xa).unsafeRunSync()
            case 3 =>
              insertPelicula3().transact(xa).unsafeRunSync()

        case _  =>
          println("FINALIZANDO PROGRAMA")
          return
    }



  }


    /*
  def moda(columna: String, dataMap: List[Map[String, String]]): (String, Int) = {
    val lista: List[String] =
      dataMap
        .flatMap(row => row.get(columna))

    val moda: (String, Int) = lista
      .groupBy(identity)
      .map(entrada => (entrada._1, entrada._2.length))
      .maxBy(_._2)

    moda
  }


  def max(data: List[Map[String, String]], column: String): Option[Double] = {
    data.flatMap(_.get(column).map(_.toDouble)).maxOption
  }


  def min(data: List[Map[String, String]], column: String): Option[Double] = {
    data.flatMap(_.get(column).map(_.toDouble)).minOption
  }
  def valoresDiferentes(columna: String, dataMap: List[Map[String, String]]): Map[String, Int] = {
    val lista: List[String] =
      dataMap
        .flatMap(row => row.get(columna))

    val valoresDiferentes: Map[String, Int] = lista
      .groupBy(identity)
      .map(entrada => (entrada._1, entrada._2.length))

    valoresDiferentes
  }
*/


}