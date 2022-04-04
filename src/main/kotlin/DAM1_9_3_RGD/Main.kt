package DAM1_9_3_RGD

import SOBRAS.DAM1_9_2_RGD.LibrosDAO
import java.sql.Date
import java.sql.DriverManager

data class Tiendas(
    val id: Int, val NOMBRE: String, val DIRECCION: String
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
    val inventariosDAO = InventariosDAO(jdbcUrl, username, password)

    /*
    var contador = 0
    inventariosDAO.selectAll().forEach { contador++ }
    println(contador)
    */


    val connection = DriverManager.getConnection(jdbcUrl, username, password)

    if (connection.isValid(10)) {
        println("Conexión válida")


        connection.use {
            val inventariosDAO = InventariosDAO(jdbcUrl, username, password)
            val tiendasDAO = TiendasDAO(jdbcUrl, username, password)

            //Hago 2 contadores para determinar cuantas filas tiene mis objetos DAO para los bucles
            var limiteInventarios = 0
            var limiteTiendas = 0

            inventariosDAO.selectAll().forEach { limiteInventarios++ }
            tiendasDAO.selectAll().forEach { limiteTiendas++ }

            //Hago un bucle en el que reviso cada fila y compruebo que cumplan la condicion para actualizarlo
            /*
            for (i in 1..limiteInventarios) {
                var b = inventariosDAO.selectById(i)

                if (b != null && b.Precio > 2000) {
                    b.Precio = b.Precio * 1.15
                    inventariosDAO.update(b)
                }
            }
             */
            //Intento hacerlo de otra manera para que la excepción no salte pero sigue
            inventariosDAO.selectAll().forEach {
                if (it.Precio > 2000) {
                    it.Precio = it.Precio * 1.15; inventariosDAO.update(it)
                }
            }

            //Muestro los inventarios por tiendas
            for (k in 1..limiteTiendas) {
                var a = tiendasDAO.selectById(k)

                //Para cada fila de tiendasDAO imprimo todas las filsa de Inventario que correspondan con su ID
                println("=======================================")
                println(a)
                for (i in 1..6) {
                    var b = inventariosDAO.selectById(i)
                    if (b != null) {
                        if (b.id_tienda == k) {
                            println("---------------------------------------")
                            println(b)
                            println("---------------------------------------")
                        }
                    }
                }
                println("=======================================")
            }
        }
    } else println("Conexión ERROR")


}



