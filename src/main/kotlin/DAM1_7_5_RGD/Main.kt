package DAM1_7_5_RGD

import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.DriverManager

data class User(val id: Int, val name: String)


fun main() {

    val dataSource = HikariDataSource()
    val jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"
    val username = "programacion"
    val password = "programacion"

    dataSource.jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"
    dataSource.username = "programacion"
    dataSource.password = "programacion"

    val connection = DriverManager
        .getConnection(jdbcUrl, username, password)

    //println(connection.isValid(0))



}