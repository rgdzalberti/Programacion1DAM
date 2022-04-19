package DAM1_9_3_RGD

import java.sql.DriverManager

data class Tiendas(
    val ID: Int, val NOMBRE: String, val DIRECCION: String
)

data class Inventarios(
    var id_articulo: Int,
    var Nombre: String,
    var Comentario: String,
    var Precio: Double,
    var id_tienda: Int,
)

fun main() {

    val jdbcUrl = "jdbc:h2:mem:default"
    val username = ""
    val password = ""

    val connection = DriverManager.getConnection(jdbcUrl, username, password)

    val c = ConnectionBuilder()

    if (c.connection.isValid(10)) {
        println("Conexión válida")


        c.connection.use {
            val inventariosDAO = InventariosDAO(it)
            val tiendasDAO = TiendasDAO(it)

            //Hago 2 contadores para determinar cuantas filas tiene mis objetos DAO para los bucles
            var limiteInventarios = 0
            var limiteTiendas = 0

            tiendasDAO.prepareTable()
            with(tiendasDAO) {
                insert(Tiendas(1,"La Nena","Callejon de la Nena #123, Colonia Dulce Amor"))
                insert(Tiendas(2,"La Virgen","Calle Rosa de Guadalupe #2, Colonia Bajo del Cerro"))
                insert(Tiendas(3,"La Piscina","Avenida de los Charcos #78, Colonia El Mojado"))
                insert(Tiendas(4,"El Churro","Calle el Pason #666, Colonia El Viaje"))
                insert(Tiendas(5,"Don Pancho","Avenida del Reboso #1521, Colonia El Burro"))
            }

            inventariosDAO.prepareTable()
            with(inventariosDAO) {
                insert(Inventarios(1,"CD-DVD","900 MB DE ESPACIO",35.50,5))
                insert(Inventarios(2,"USB-HP","32GB, USB 3.0",155.90,4))
                insert(Inventarios(3,"Laptop SONY","4GB RAM, 300 HDD, i5 2.6 GHz.",13410.07,3))
                insert(Inventarios(4,"Mouse Optico","700 DPI",104.40,2))
                insert(Inventarios(5,"Disco Duro","200 TB, HDD, USB 3.0",2300.00,1))
                insert(Inventarios(6,"Proyector TSHB","TOSHIBA G155",5500.00,5))
            }


            inventariosDAO.selectAll().forEach { limiteInventarios++ }
            tiendasDAO.selectAll().forEach { limiteTiendas++ }

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