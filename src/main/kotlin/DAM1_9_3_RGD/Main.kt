package DAM1_9_3_RGD

import SOBRAS.DAM1_9_2_RGD.LibrosDAO
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
    var Precio: Double,
    val id_tienda: Int,
)

fun main() {

    val jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"
    val username = "programacion"
    val password = "programacion"

    val connection = DriverManager
        .getConnection(jdbcUrl, username, password)

    if (connection.isValid(10)) {
        println("Conexión válida")

        connection.use {
            val inventariosDAO = InventariosDAO(jdbcUrl, username, password)
            val tiendasDAO = TiendasDAO(jdbcUrl, username, password)

            //Hago un bucle en el que reviso cada fila y compruebo que cumplan la condicion para actualizarlo
            //Meter un until to las id de la tabla < - QUITAR
            for (i in 1..6) {
                var b = inventariosDAO.selectById(i)

                if (b != null) {
                    if (b.Precio > 2000) {
                        b.Precio = b.Precio * 1.15
                        inventariosDAO.update(b)
                    }
                }
            }

            //Muestro los inventarios por tiendas
            for (k in 1..5) {
                var a = tiendasDAO.selectById(k)

                //Para cada fila de tiendasDAO imprimo todas las filsa de Inventario que correspondan con su ID
                println(a)
                for (i in 1..6) {
                    var b = inventariosDAO.selectById(i)
                    if (b != null) {
                        if (b.id_tienda == k) {
                            println(b)
                        }
                    }
                }
            }
        }
    } else
        println("Conexión ERROR")
}