import doobie.implicits.*
import doobie.util.transactor.Transactor
import cats.effect.IO
import doobie.ConnectionIO

import java.time.LocalDate
import java.time.ZoneId

object CRUD {
  // CRUD para la tabla 'collection'
  def consultaXcolection(x: String): doobie.ConnectionIO[List[(Int, String, Boolean, Long, Option[String], Option[String], Option[String], Option[String], Long, Option[String], Option[String], Long, Int, Option[String], Option[String], Option[String], Boolean, Int, Int, Option[Int])]] = {
    sql"""
              SELECT pelicula_id, imdb_id, adult, budget, homepage, original_language, original_title,
                     overview, popularity, poster_path, release_date, revenue, runtime, estado,
                     tagline, title, video, vote_count, vote_average, collection_id
              FROM pelicula
              WHERE collection_id = $x
            """.query[(Int, String, Boolean, Long, Option[String], Option[String], Option[String], Option[String], Long, Option[String], Option[String], Long, Int, Option[String], Option[String], Option[String], Boolean, Int, Int, Option[Int])]
      .to[List]
  }
  def consultaXid(x: Int): doobie.ConnectionIO[List[(Int, String, Boolean, Long, Option[String], Option[String], Option[String], Option[String], Long, Option[String], Option[String], Long, Int, Option[String], Option[String], Option[String], Boolean, Int, Int, Option[Int])]] = {
    sql"""
              SELECT pelicula_id, imdb_id, adult, budget, homepage, original_language, original_title,
                     overview, popularity, poster_path, release_date, revenue, runtime, estado,
                     tagline, title, video, vote_count, vote_average, collection_id
              FROM pelicula
              WHERE pelicula_id = $x
            """.query[(Int, String, Boolean, Long, Option[String], Option[String], Option[String], Option[String], Long, Option[String], Option[String], Long, Int, Option[String], Option[String], Option[String], Boolean, Int, Int, Option[Int])]
      .to[List]
  }
  def consultaXtitulo(x: String): doobie.ConnectionIO[List[(Int, String, Boolean, Long, Option[String], Option[String], Option[String], Option[String], Long, Option[String], Option[String], Long, Int, Option[String], Option[String], Option[String], Boolean, Int, Int, Option[Int])]] = {
    sql"""
            SELECT pelicula_id, imdb_id, adult, budget, homepage, original_language, original_title,
                   overview, popularity, poster_path, release_date, revenue, runtime, estado,
                   tagline, title, video, vote_count, vote_average, collection_id
            FROM pelicula
            WHERE title = $x
          """.query[(Int, String, Boolean, Long, Option[String], Option[String], Option[String], Option[String], Long, Option[String], Option[String], Long, Int, Option[String], Option[String], Option[String], Boolean, Int, Int, Option[Int])]
      .to[List]
  }
  def consultaXgenero(x: Int): doobie.ConnectionIO[List[(String, String, Int)]] = {
    sql"""
          SELECT g.nombre AS "Genero", pe.original_title AS "Película", pe.pelicula_id AS "ID"
          FROM pelicula pe
          JOIN pelicula_genero pg ON pe.pelicula_id = pg.pelicula_id
          JOIN genero g ON pg.genre_id = g.genre_id
          WHERE g.genre_id = $x;
        """.query[(String, String, Int)]
      .to[List]
  }
  def insertCollection(collection: (Int, String, String, String)): ConnectionIO[Int] = {
    sql"""
    INSERT INTO collection (collection_id, nombre, poster_path, backdrop_path)
    VALUES (${collection._1}, ${collection._2}, ${collection._3}, ${collection._4})
  """.update.run
  }
  def updateCollection(collection: (Int, String, String, String)): doobie.Update0 = {
    sql"""
      UPDATE collection
      SET nombre = ${collection._2}, poster_path = ${collection._3}, backdrop_path = ${collection._4}
      WHERE collection_id = ${collection._1}
    """.update
  }
  // CRUD para la tabla 'pelicula'
  def insertPelicula1(): ConnectionIO[Int] = {
    sql"""
            INSERT INTO pelicula (
              pelicula_id, imdb_id, adult, budget, homepage, original_language, original_title,
              overview, popularity, poster_path, release_date, revenue, runtime, estado,
              tagline, title, video, vote_count, vote_average, collection_id
            ) VALUES (35012,'tt0093448',false,100000,'http://movies.uip.de/corellismandoline/',
            'en', 'Planes, Trains and Automobiles','A man must struggle to travel home for Thanksgiving,
             with an obnoxious slob of a shower ring salesman his only companion.',9,'/t7Rrs11VNjWAi8loMLhRe94gesE.jpg',
             '1987-11-26',49230280,93,'Released','What he really wanted was to spend Thanksgiving with his family.',
             'Planes, Trains and Automobiles',false,567,7,666)
          """.update.run
  }

  def insertPelicula2(): ConnectionIO[Int] = {
    sql"""
            INSERT INTO pelicula (
              pelicula_id, imdb_id, adult, budget, homepage, original_language, original_title,
              overview, popularity, poster_path, release_date, revenue, runtime, estado,
              tagline, title, video, vote_count, vote_average, collection_id
            ) VALUES (92013,'tt0093448',false,100000,'http://movies.uip.de/corellismandoline/',
            'en', 'Planes, Trains and Automobiles','A man must struggle to travel home for Thanksgiving,
             with an obnoxious slob of a shower ring salesman his only companion.',9,'/t7Rrs11VNjWAi8loMLhRe94gesE.jpg',
             '1987-11-26',49230280,93,'Released','What he really wanted was to spend Thanksgiving with his family.',
             'Planes, Trains and Automobiles',false,567,7,666)
          """.update.run
  }

  def insertPelicula3(): ConnectionIO[Int] = {
    sql"""
            INSERT INTO pelicula (
              pelicula_id, imdb_id, adult, budget, homepage, original_language, original_title,
              overview, popularity, poster_path, release_date, revenue, runtime, estado,
              tagline, title, video, vote_count, vote_average, collection_id
            ) VALUES (606913,'tt0093448',false,100000,'http://movies.uip.de/corellismandoline/',
            'en', 'Planes, Trains and Automobiles','A man must struggle to travel home for Thanksgiving,
             with an obnoxious slob of a shower ring salesman his only companion.',9,'/t7Rrs11VNjWAi8loMLhRe94gesE.jpg',
             '1987-11-26',49230280,93,'Released','What he really wanted was to spend Thanksgiving with his family.',
             'Planes, Trains and Automobiles',false,567,7,666)
          """.update.run
  }
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
  def updatePelicula(pelicula: (Int, String, Boolean, Long, String, String, String, String, Long, String, String, Long, Int, String, String, String, Boolean, Int, Int, Int)): doobie.Update0 = {
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
}
