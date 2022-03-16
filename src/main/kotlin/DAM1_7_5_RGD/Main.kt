package DAM1_7_5_RGD

import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.DriverManager

data class Book(val id: String)

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

    while(result.next()){
    //println(result.getString("id"))
        val id = result.getString("AUTHOR")
        libros.add(Book(id))
    }

    println(libros)

}