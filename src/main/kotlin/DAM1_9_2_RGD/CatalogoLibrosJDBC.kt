package DAM1_9_2_RGD

class CatalogoLibrosJDBC(jdbc:String, user:String, password:String):GestorCatalogo {

    private val books = UserDAO(jdbc,user,password).getBookList()

    override fun existeLibro(idLibro: String): Boolean {
        books.any {it.id == idLibro}
    }

    override fun infoLibro(idLibro: String): Map<String, Any> {
       books.find {it.id == idLibro}.serializeToMap()
    }

}