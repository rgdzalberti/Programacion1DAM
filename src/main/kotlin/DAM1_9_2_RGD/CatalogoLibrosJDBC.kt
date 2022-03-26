package SOBRAS.DAM1_9_2_RGD

class CatalogoLibrosJDBC(jdbc: String, user: String, password: String) : GestorCatalogo {

    val librosDAO = LibrosDAO(jdbc, user, password)
    val books = librosDAO.listOfBooks()

    override fun existeLibro(idLibro: String): Boolean {
        var existeLibro = false
        books.forEach {
            if (it.id == idLibro) {
                existeLibro = true
            }
        }
        return existeLibro
    }

    override fun infoLibro(idLibro: String): Map<String, Any> {

        var mapaLibro = emptyMap<String, Any>()

        books.forEach {
            if (it.id == idLibro) {
                mapaLibro = it.serializeToMap()
            }

        }
        return mapaLibro

    }
}