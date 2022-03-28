import DAM1_9_2_RGD.MyBook
import SOBRAS.DAM1_9_2_RGD.LibrosDAO
import java.sql.Date
import java.sql.DriverManager


fun main() {
    val c = DriverManager
        .getConnection("jdbc:oracle:thin:@localhost:1521:XE", "programacion", "programacion")
    //val c = LibrosDAO("jdbc:oracle:thin:@localhost:1521:XE", "programacion", "programacion")
    println("conectando.....")


    if (c.isValid(10)) {
        println("Conexión válida")

        c.use {
            val h2DAO = LibrosDAO("jdbc:oracle:thin:@localhost:1521:XE", "programacion", "programacion")

            // Creamos la tabla o la vaciamos si ya existe
            h2DAO.prepareTable()

            // Insertamos 4 libros
            repeat(4)
            {
                h2DAO.insert(MyBook(id = "23456", author = "Ricardo", title = "LibrosDAO", genre = "Genero2", price = 6.66, publish_date = Date(2000,1,30),"LibrosDAO"))
            }  // Buscar un usuario
            var b = h2DAO.selectById("1")

            // Actualizar un usuario
            if (b!=null)
            {
                b.author = "Camila"
                h2DAO.update(b)
            }
            // Borrar un usuario
            h2DAO.deleteById("2")

            // Seleccionar todos los usuarios
            println(h2DAO.selectAll())


        }
    } else
        println("Conexión ERROR")
}