/*
import DAM1_9_2_RGD.MyUser
import un9.jdbc.ejemplos.jdbcDAO.ConnectionBuilder
import un9.jdbc.ejemplos.jdbcDAO.UserDAO

fun main() {
    val c = ConnectionBuilder()
    println("conectando.....")

    if (c.connection.isValid(10)) {
        println("Conexión válida")

        c.connection.use {
            val h2DAO = UserDAO(c.connection)

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


 */