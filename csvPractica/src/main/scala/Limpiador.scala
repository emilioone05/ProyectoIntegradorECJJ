import play.api.libs.json.Json

object Limpiador {
  
  def eliminarIncompletos(json: String): String = {
    if (json.contains('{') && json.contains('[')) {
      val ultimo = json.lastIndexOf('}')
      val arreglado = json.substring(0, ultimo).appended('}').appended(']')
      arreglado
    }
    else
      json
  }
  def limpiezaJson(dataMap: List[Map[String, String]], columnas: List[String]): List[Map[String, String]] = {
    val dataLimpia = dataMap.map { mapa =>
      mapa.map((llave, valor) =>
        if columnas.contains(llave) then
          val json = formatoJson(valor)
          (llave, json)
        else
          (llave, valor)
      )
    }
    dataLimpia
  }
  def formatoJson(json: String): String = {
    if json.isEmpty then "[]"
    else
      try {
        val cleanedJson = json
          .replaceAll("''","'null'")
          .replaceAll("'", "\"") // Cambia comillas simples por dobles
          .replaceAll("None", "null") // Cambia None por null
          .replaceAll("\\\\", "") // Elimina barras invertidas dobles
          .replace("\\", "") // Elimina barras invertidas dobles
          .replaceAll("\r?\n", "") // Elimina saltos de línea
        val clean2 = eliminarIncompletos(cleanedJson)
        // Intentar parsear para validar el JSON
        val parsedJson = Json.parse(cleanedJson)
        val retornoJSon = Json.stringify(parsedJson) // Devuelve el JSON como String validado
        retornoJSon
      } catch {
        case _: Exception =>
          "[]"
      }
  }
  

  def borrarDatosVacios(dataMap: List[Map[String, String]], columnas: List[String]): List[Map[String, String]] = {
    val dataLimpia = dataMap.filter(fila => columnas.forall(col => fila.get(col).exists(_.nonEmpty)))

    dataLimpia
  }

  def simplificarNumeros(dataMap: List[Map[String, String]], columnas :List[String]): List[Map[String, String]] = {
    val dataLimpia = dataMap.map(mapa =>
      mapa.map{
        (llave, valor) =>
          if (valor.contains(".")||valor.contains("-")) && columnas.contains(llave) then{
            val num = valor.toDouble
            val numRedondeado = Math.round(num)
            (llave,""+numRedondeado)
          }
          else (llave,valor)
          
      }
    )
    dataLimpia
  }

  def numerosNegativos(dataMap: List[Map[String, String]], columnas: List[String]): List[Map[String, String]] = {
    val dataLimpia = dataMap.map { mapa =>
      mapa.map { (llave, valor) =>
        if (columnas.contains(llave) && valor.isEmpty) (llave, "0")
        else if (columnas.contains(llave) && (valor.toDouble < 0)) (llave, "0")
        else (llave,valor)
      }
    }
    dataLimpia
  }
  def trimeador(dataMap: List[Map[String, String]]): List[Map[String, String]] = {
    val dataLimpia = dataMap.map { mapa =>
      mapa.map (
        (llave, valor) => (llave, valor.trim)
      )
    }
    dataLimpia
  }

  def nulleador(dataMap: List[Map[String, String]]): List[Map[String, String]] = {
    val dataLimpia = dataMap.map { mapa =>
      mapa.map(
        (llave, valor) =>  if (valor.isEmpty) (llave, "NULL") else (llave, valor)
      )
    }
    dataLimpia
  }


  def eliminarRepetidos( columna: String,dataMap: List[Map[String, String]]): List[Map[String, String]] = {
    val dataLimpia = dataMap.groupBy(_.get(columna)).values.map(_.head).toList
    dataLimpia

  }

}
