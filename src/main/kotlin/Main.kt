package SOBRAS

import java.sql.Date
import java.sql.DriverManager

data class Book(
    val id: String,
    val AUTHOR: String,
    val TITLE: String,
    val GENRE: String,
    val PRICE: Int,
    val PUBLISH_DATE: Date,
    val DESCRIPTION: String
)

fun main() {

    //Me da error el dataSource
    //val dataSource = HikariDataSource()

    val jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"
    val username = "programacion"
    val password = "programacion"

    /*

    dataSource.jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"
    dataSource.username = "programacion"
    dataSource.password = "programacion"

     */

    val connection = DriverManager
        .getConnection(jdbcUrl, username, password)

    //println(connection.isValid(0))

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


}