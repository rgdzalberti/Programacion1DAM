package DAM1_9_3_RGD

import java.sql.DriverManager

data class Tiendas(
    val id: Int, val NOMBRE: String, val DIRECCION: String
)

data class Inventarios(
    var id_articulo: Int,
    var Nombre: String,
    var Comentario: String,
    var Precio: Double,
    var id_tienda: Int,
)

fun main() {

    val jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"
    val username = "programacion"
    val password = "programacion"
    val inventariosDAO = InventariosDAO(jdbcUrl, username, password)

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

            //Compruebo si el precio es mayor al indicado para aplicarle el porcentaje
            inventariosDAO.selectAll().forEach {
                if (it.Precio > 2000) {
                    it.Precio = it.Precio * 1.15; inventariosDAO.update(it)
                }
            }

            //Muestro los inventarios por tiendas
            for (k in 1..limiteTiendas) {
                var objetoTiendaPorID = tiendasDAO.selectById(k)

                //Para cada fila de tiendasDAO imprimo todas las filsa de Inventario que correspondan con su ID
                println("=======================================")
                println(objetoTiendaPorID)
                for (i in 1..6) {
                    var objetoInventarioPorID = inventariosDAO.selectById(i)
                    if (objetoInventarioPorID != null) {
                        if (objetoInventarioPorID.id_tienda == k) {
                            println("---------------------------------------")
                            println(objetoInventarioPorID)
                            println("---------------------------------------")
                        }
                    }
                }
                println("=======================================")
            }
        }
    } else println("Conexión ERROR")


}



