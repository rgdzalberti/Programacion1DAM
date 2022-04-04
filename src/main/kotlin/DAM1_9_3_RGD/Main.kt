package DAM1_9_3_RGD

import java.sql.Date
import java.sql.DriverManager

data class Tiendas(
    val id: Int,
    val NOMBRE: String,
    val DIRECCION: String
)

data class Inventarios(
    val id_articulo: Int,
    val Nombre: String,
    val Comentario: String,
    val Precio: Double,
    val id_tienda: Int,
)

fun main() {

    val jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"
    val username = "programacion"
    val password = "programacion"

    val connection = DriverManager
        .getConnection(jdbcUrl, username, password)


    /*
    val query = connection.prepareStatement("SELECT * FROM BOOKS")
    val result = query.executeQuery()
    val libros = mutableListOf<Book>()

    //idLibro es la id del libro a buscar
    val idLibro = "bk101"
    //Y esta es la variable que voy a usar que indicará si al final del bucle ha de imprimirse la información del libro
    var imprimir: Boolean = false

    while (result.next()) {

        val id = result.getString("ID")
        val author = result.getString("AUTHOR")
        val title = result.getString("TITLE")
        val genre = result.getString("GENRE")
        val price = result.getInt("PRICE")
        val publish_date = result.getDate("PUBLISH_DATE")
        val description = result.getString("DESCRIPTION")

        if (id == idLibro) {
            imprimir = true
        }

        libros.add(Book(id, author, title, genre, price, publish_date, description))
    }

    if (imprimir == true) {
        println("Existe el libro y su información es la siguiente: ")
        println(libros)
    } else {
        println("El libro no existe")
    }

     */


}