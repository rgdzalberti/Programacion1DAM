import DAM1_9_2_RGD.MyUser
import SOBRAS.DAM1_9_2_RGD.CatalogoLibrosJDBC
import SOBRAS.DAM1_9_2_RGD.LibrosDAO
import java.sql.ConnectionBuilder
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

            // Insertamos 4 usuarios
            repeat(4)
            {
                h2DAO.insert(MyUser(name = "Edu", email = "falto", country = "spain"))
            }  // Buscar un usuario
            var u = h2DAO.selectById(1)

            // Actualizar un usuario
            if (u!=null)
            {
                u.name = "Nuevo usuario"
                h2DAO.update(u)
            }
            // Borrar un usuario
            h2DAO.deleteById(2)

            // Seleccionar todos los usuarios
            println(h2DAO.selectAll())
        }
    } else
        println("Conexión ERROR")
}