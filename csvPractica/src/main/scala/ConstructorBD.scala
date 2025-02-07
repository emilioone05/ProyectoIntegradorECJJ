import doobie.*
import doobie.implicits.*
import cats.*
import cats.effect.*
import cats.implicits.*
import cats.effect.unsafe.implicits.global

object ConstructorBD {
  def construir(): Unit = {
    val xa = Transactor.fromDriverManager[IO](
      driver = "com.mysql.cj.jdbc.Driver", // JDBC driver
      url = "jdbc:mysql://localhost:3306", // URL de conexión
      user = "root", // Nombre de la base de datos
      password = "935475", // Password
      logHandler = None // Manejo de la información de Log
    )
    val x = crear().transact(xa).unsafeRunSync()
    
  }

  def crear(): ConnectionIO[Int] =(

    sql"""CREATE SCHEMA IF NOT EXISTS presentacionFinal
         |COLLATE = utf8_general_ci;"""
      .stripMargin.update.run *>
      sql"""USE presentacionFinal;"""
        .stripMargin
        .update.run *>

      sql"""CREATE TABLE IF NOT EXISTS collection (
           |    collection_id INTEGER PRIMARY KEY COMMENT 'Identificador único de la colección',
           |    nombre VARCHAR(80) COMMENT 'Nombre de la colección',
           |    poster_path VARCHAR(100) COMMENT 'Ruta de la imagen del póster de la colección',
           |    backdrop_path VARCHAR(100) COMMENT 'Ruta de la imagen de fondo de la colección'
           |)
           |COMMENT = 'Recordables de la película los cuales pueden ser recomendados a los usuarios en caso de que el rating y aceptación de ellos sea elevado.';
           |"""
        .stripMargin.update.run *>
      sql"""CREATE TABLE IF NOT EXISTS pelicula (
           |    pelicula_id INTEGER PRIMARY KEY COMMENT 'Identificador único de la película',
           |    imdb_id VARCHAR(20) COMMENT 'Identificador de la película en IMDB',
           |    adult BOOLEAN COMMENT 'Indica si la película es para adultos (true/false)',
           |    budget BIGINT COMMENT 'Presupuesto de la película en dólares',
           |    homepage VARCHAR(100) COMMENT 'Página web oficial de la película',
           |    original_language CHAR(2) COMMENT 'Código de dos letras del idioma original (ISO 639-1)',
           |    original_title VARCHAR(150) COMMENT 'Título original de la película',
           |    overview VARCHAR(2000) COMMENT 'Resumen o sinopsis de la película',
           |    popularity BIGINT COMMENT 'Índice de popularidad de la película',
           |    poster_path VARCHAR(70) COMMENT 'Ruta del póster de la película',
           |    release_date DATE COMMENT 'Fecha de estreno de la película',
           |    revenue BIGINT COMMENT 'Ingresos generados por la película en dólares',
           |    runtime INTEGER COMMENT 'Duración de la película en minutos',
           |    estado VARCHAR(20) COMMENT 'Estado de la película (ej. "Released", "Post Production")',
           |    tagline VARCHAR(200) COMMENT 'Frase promocional de la película',
           |    title VARCHAR(150) COMMENT 'Título de la película en el idioma de visualización',
           |    video BOOLEAN COMMENT 'Indica si tiene contenido en video disponible',
           |    vote_count INTEGER COMMENT 'Número de votos recibidos',
           |    vote_average INTEGER COMMENT 'Promedio de votos recibidos',
           |    collection_id INTEGER NULL COMMENT 'Referencia a la colección a la que pertenece la película',
           |    FOREIGN KEY (collection_id) REFERENCES collection(collection_id)
           |) COMMENT = 'Tabla que almacena información detallada de las películas.';
           |"""
        .stripMargin.update.run *>

      sql"""CREATE TABLE IF NOT EXISTS genero (
           |    genre_id INTEGER PRIMARY KEY COMMENT 'Identificador único del género',
           |    nombre VARCHAR(30) COMMENT 'Nombre del género de la película'
           |)
           |COMMENT = 'Tipo de película escogida para nuestra película, generando una etiqueta para que los usuarios puedan encontrarla fácilmente.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS pelicula_genero (
           |    genre_id INTEGER,
           |    pelicula_id INTEGER,
           |    PRIMARY KEY (genre_id, pelicula_id),
           |    FOREIGN KEY (genre_id) REFERENCES genero(genre_id),
           |    FOREIGN KEY (pelicula_id) REFERENCES pelicula(pelicula_id)
           |);"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS idioma (
           |    iso_639_1_code CHAR(2) PRIMARY KEY COMMENT 'Código ISO 639-1 del idioma (dos letras)',
           |    nombre VARCHAR(30) COMMENT 'Nombre del idioma'
           |)
           |COMMENT = 'Diferentes lenguas subtituladas y habladas para la diferente audiencia en todo el mundo para la cual va dirigida la película.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS pelicula_idioma (
           |    iso_639_1_code CHAR(2) COMMENT 'Código del idioma relacionado con la película',
           |    pelicula_id INTEGER COMMENT 'Identificador de la película asociada al idioma',
           |    PRIMARY KEY (iso_639_1_code, pelicula_id),
           |    FOREIGN KEY (iso_639_1_code) REFERENCES idioma(iso_639_1_code),
           |    FOREIGN KEY (pelicula_id) REFERENCES pelicula(pelicula_id)
           |)
           |COMMENT = 'Método principal del cual se derivan el resto de métodos y sus relaciones. Posee características clave que hacen única a cada película como su ID, su título y su estudio.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS pais (
           |    iso_3166_1_code CHAR(3) PRIMARY KEY COMMENT 'Código ISO 3166-1 del país (tres    letras)',
           |    nombre VARCHAR(30) COMMENT 'Nombre del país'
           |)
           |COMMENT = "Tabla relacionada a la república donde se graba la pelicula";
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS pelicula_pais (
           |    iso_3166_1_code CHAR(3) COMMENT 'Código del país relacionado con la película',
           |    pelicula_id INTEGER COMMENT 'Identificador de la película asociada al país',
           |    PRIMARY KEY (iso_3166_1_code, pelicula_id),
           |    FOREIGN KEY (iso_3166_1_code) REFERENCES pais(iso_3166_1_code),
           |    FOREIGN KEY (pelicula_id) REFERENCES pelicula(pelicula_id)
           |) COMMENT = 'Relación entre películas y los países en los que se distribuyen o producen.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS productora (
           |    productora_id INTEGER PRIMARY KEY COMMENT 'Identificador único de la productora',
           |    nombre VARCHAR(50) COMMENT 'Nombre de la productora'
           |) COMMENT = 'Estudio encargado de rodar, editar y publicar la película al público.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS pelicula_productora (
           |    productora_id INTEGER COMMENT 'Identificador de la productora asociada a la película',
           |    pelicula_id INTEGER COMMENT 'Identificador de la película asociada a la productora',
           |    PRIMARY KEY (productora_id, pelicula_id),
           |    FOREIGN KEY (productora_id) REFERENCES productora(productora_id),
           |    FOREIGN KEY (pelicula_id) REFERENCES pelicula(pelicula_id)
           |) COMMENT = 'Relación entre las películas y las productoras encargadas de su producción.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS empleado (
           |    empleado_id INTEGER PRIMARY KEY COMMENT 'Identificador único del empleado',
           |    nombre VARCHAR(50) COMMENT 'Nombre del empleado',
           |    gender INTEGER COMMENT 'Género del empleado (1 = Masculino, 2 = Femenino, 0 = Otro)',
           |    profile_path VARCHAR(100) COMMENT 'Ruta de la imagen de perfil del empleado'
           |) COMMENT = 'Persona que realiza labores dentro del ámbito de desarrollo de la película.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS job_depart (
           |    job_name VARCHAR(50) PRIMARY KEY  COMMENT 'Nombre del trabajo o rol',
           |    department_id VARCHAR(50) COMMENT 'Identificador del departamento del trabajo'
           |) COMMENT = 'Tabla que describe los trabajos y departamentos asociados a los empleados en la producción de la película.';"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS pelicula_empleado (
           |    empleado_id INTEGER COMMENT 'Identificador del empleado en la película',
           |    pelicula_id INTEGER COMMENT 'Identificador de la película en la que el empleado trabaja',
           |    credit_id VARCHAR(50) COMMENT 'Identificador único de crédito del empleado en la película',
           |    job_name VARCHAR(50) COMMENT 'Identificador del trabajo que realiza el empleado en la película',
           |    PRIMARY KEY (empleado_id, pelicula_id),
           |    FOREIGN KEY (empleado_id) REFERENCES empleado(empleado_id),
           |    FOREIGN KEY (pelicula_id) REFERENCES pelicula(pelicula_id),
           |    FOREIGN KEY (job_name) REFERENCES job_depart(job_name)
           |) COMMENT = 'Relación entre los empleados y las películas en las que trabajan, incluyendo su rol en la producción.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS actor (
           |    actor_id INTEGER PRIMARY KEY COMMENT 'Identificador único del actor',
           |    nombre VARCHAR(50) COMMENT 'Nombre del actor',
           |    gender INTEGER COMMENT 'Género del actor (1 = Masculino, 2 = Femenino, 0 = Otro)',
           |    profile_path VARCHAR(100) COMMENT 'Ruta de la imagen de perfil del actor'
           |) COMMENT = 'Persona que interpreta papeles en la película, dependiendo de la trama, situación y carisma del actor.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS pelicula_actor (
           |    pelicula_actor_id INTEGER PRIMARY KEY AUTO_INCREMENT COMMENT 'Identificador único de la relación película-actor',
           |    actor_id INTEGER COMMENT 'Identificador del actor en la película',
           |    pelicula_id INTEGER COMMENT 'Identificador de la película en la que actúa el actor',
           |    personaje VARCHAR(200) COMMENT 'Nombre del personaje interpretado por el actor',
           |    credit_id VARCHAR(50) COMMENT 'Identificador único de crédito del actor en la película',
           |    cast_id INTEGER COMMENT 'Identificador del elenco de la película',
           |    orden INTEGER COMMENT 'Orden del actor en los créditos de la película',
           |    FOREIGN KEY (actor_id) REFERENCES actor(actor_id),
           |    FOREIGN KEY (pelicula_id) REFERENCES pelicula(pelicula_id))
           |COMMENT = 'Relación entre actores y películas, incluyendo los personajes que interpretan y su orden en los créditos.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS key_word (
           |    id_KW INTEGER PRIMARY KEY COMMENT 'Identificador único de la palabra clave',
           |  nombre VARCHAR(50) COMMENT 'Nombre de la palabra clave asociada a la película') COMMENT = 'Palabras clave que describen características o temas que hacen que la película sea fácilmente diferenciable de otras.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS pelicula_keyWord (
           |    id_KW INTEGER COMMENT 'Identificador de la palabra clave asociada a la película',
           |    pelicula_id INTEGER COMMENT 'Identificador de la película asociada a la palabra clave',
           |    PRIMARY KEY (id_KW, pelicula_id),
           |    FOREIGN KEY (id_KW) REFERENCES key_word(id_KW),
           |    FOREIGN KEY (pelicula_id) REFERENCES pelicula(pelicula_id)
           |) COMMENT = 'Relación entre películas y palabras clave que las describen para facilitar su búsqueda y clasificación.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS Usuario (
           |    user_id INTEGER PRIMARY KEY COMMENT 'Identificador único del usuario'
           |) COMMENT = 'Consumidor de la película, seguidor de los atributos de la película como director y su estudio, capaz de calificar la película y compartir su opinión.';
           |"""
        .stripMargin
        .update.run *>
      sql"""CREATE TABLE IF NOT EXISTS calificacion (
           |    user_id INTEGER COMMENT 'Identificador del usuario que realiza la calificación',
           |    pelicula_id INTEGER COMMENT 'Identificador de la película calificada',
           |    rating FLOAT COMMENT 'Calificación dada por el usuario a la película',
           |    timestamps BIGINT COMMENT 'Marca temporal de cuando se realizó la calificación',
           |    PRIMARY KEY (user_id, pelicula_id),
           |    FOREIGN KEY (user_id) REFERENCES Usuario(user_id),
           |    FOREIGN KEY (pelicula_id) REFERENCES pelicula(pelicula_id)
           |) COMMENT = 'Tabla para almacenar las calificaciones que los usuarios otorgan a las películas.';
           |"""
        .stripMargin
        .update.run
    )

}


/*
sql"""USE PELICULAS_PROYECTO_INTEGRADOR;"""
        .stripMargin
        .update.run
      sql"""USE PELICULAS_PROYECTO_INTEGRADOR;"""
        .stripMargin
        .update.run
      sql"""USE PELICULAS_PROYECTO_INTEGRADOR;"""
        .stripMargin
        .update.run
* */
