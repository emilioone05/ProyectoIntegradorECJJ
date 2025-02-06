import doobie.implicits.*
import doobie.util.transactor.Transactor
import cats.effect.IO
import doobie.ConnectionIO

import java.time.LocalDate
import java.time.ZoneId

object CRUD {
  // CRUD para la tabla 'collection'

  def insertCollection(collection: (Int, String, String, String)): ConnectionIO[Int] = {
    sql"""
    INSERT INTO collection (collection_id, nombre, poster_path, backdrop_path)
    VALUES (${collection._1}, ${collection._2}, ${collection._3}, ${collection._4})
  """.update.run
  }

  def listAllCollections(): doobie.ConnectionIO[List[(Int, String,  Option[String],  Option[String])]] = {
    sql"SELECT collection_id, nombre, poster_path, backdrop_path FROM collection"
      .query[(Int, String,  Option[String],  Option[String])]
      .to[List]
  }

  def updateCollection(collection: (Int, String, String, String)): doobie.Update0 = {
    sql"""
      UPDATE collection
      SET nombre = ${collection._2}, poster_path = ${collection._3}, backdrop_path = ${collection._4}
      WHERE collection_id = ${collection._1}
    """.update
  }

  def deleteCollection(id: Int): doobie.Update0 = {
    sql"DELETE FROM collection WHERE collection_id = $id".update
  }
  // CRUD para la tabla 'pelicula'
  def insertPelicula(pelicula: (Int, String, Boolean, Long, String, String, String, String, Long, String, String, Long, Int, String, String, String, Boolean, Int, Int, Int)): doobie.Update0 = {
    sql"""
      INSERT INTO pelicula (
        pelicula_id, imdb_id, adult, budget, homepage, original_language, original_title,
        overview, popularity, poster_path, release_date, revenue, runtime, estado,
        tagline, title, video, vote_count, vote_average, collection_id
      ) VALUES (
        ${pelicula._1}, ${pelicula._2}, ${pelicula._3}, ${pelicula._4}, ${pelicula._5},
        ${pelicula._6}, ${pelicula._7}, ${pelicula._8}, ${pelicula._9}, ${pelicula._10},
        ${pelicula._11}, ${pelicula._12}, ${pelicula._13}, ${pelicula._14}, ${pelicula._15},
        ${pelicula._16}, ${pelicula._17}, ${pelicula._18}, ${pelicula._19}, ${pelicula._20}
      )
    """.update
  }

  def listAllPeliculas(): doobie.ConnectionIO[List[(Int, String, Boolean, Long, Option[String], Option[String], Option[String], Option[String], Long, Option[String], Option[String], Long, Int, Option[String], Option[String], Option[String], Boolean, Int, Int, Option[Int])]] = {
    sql"""
      SELECT pelicula_id, imdb_id, adult, budget, homepage, original_language, original_title,
             overview, popularity, poster_path, release_date, revenue, runtime, estado,
             tagline, title, video, vote_count, vote_average, collection_id
      FROM pelicula
    """.query[(Int, String, Boolean, Long, Option[String], Option[String], Option[String], Option[String], Long, Option[String], Option[String], Long, Int, Option[String], Option[String], Option[String], Boolean, Int, Int, Option[Int])]
      .to[List]
  }
  def
  updatePelicula(pelicula: (Int, String, Boolean, Long, String, String, String, String, Long, String, String, Long, Int, String, String, String, Boolean, Int, Int, Int)): doobie.Update0 = {
    sql"""
        UPDATE pelicula
        SET imdb_id = ${pelicula._2}, adult = ${pelicula._3}, budget = ${pelicula._4}, homepage = ${pelicula._5},
            original_language = ${pelicula._6}, original_title = ${pelicula._7}, overview = ${pelicula._8},
            popularity = ${pelicula._9}, poster_path = ${pelicula._10}, release_date = ${pelicula._11},
            revenue = ${pelicula._12}, runtime = ${pelicula._13}, estado = ${pelicula._14},
            tagline = ${pelicula._15}, title = ${pelicula._16}, video = ${pelicula._17},
            vote_count = ${pelicula._18}, vote_average = ${pelicula._19}, collection_id = ${pelicula._20}
        WHERE pelicula_id = ${pelicula._1}
      """.update
  }
  

  def deletePelicula(id: Int): doobie.Update0 = {
    sql"DELETE FROM pelicula WHERE pelicula_id = $id".update
  }
}
