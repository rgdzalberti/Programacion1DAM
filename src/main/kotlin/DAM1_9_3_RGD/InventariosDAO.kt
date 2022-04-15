package DAM1_9_3_RGD;

import java.sql.DriverManager
import java.sql.SQLException

class InventariosDAO(jdbc:String, user:String, password:String) {

    private val c = DriverManager.getConnection(jdbc,user,password)
    private val inventarios = mutableListOf<Inventarios>()

    init{
        val query = c.prepareStatement("SELECT * FROM INVENTARIOS")
        val result = query.executeQuery()
        while (result.next()){
            val ID_ARTICULO = result.getInt("ID_ARTICULO")
            val NOMBRE = result.getString("NOMBRE")
            val COMENTARIO = result.getString("COMENTARIO")
            val PRECIO = result.getDouble("PRECIO")
            val ID_TIENDA = result.getInt("ID_TIENDA")
            inventarios.add(Inventarios(ID_ARTICULO,NOMBRE,COMENTARIO,PRECIO,ID_TIENDA))
        }
    }

    companion object {
        private const val SCHEMA = "default"
        private const val TABLE = "INVENTARIOS"
        private const val TRUNCATE_TABLE_INVENTARIOS_SQL = "TRUNCATE TABLE INVENTARIOS"
        private const val CREATE_TABLE_INVENTARIOS_SQL =
            "CREATE TABLE INVENTARIOS (ID_ARTICULO NUMBER(10,0) CONSTRAINT PK_ID_ARTICULO PRIMARY KEY, NOMBRE VARCHAR2(50) , COMENTARIO VARCHAR2(200) NOT NULL, PRECIO NUMBER(10,2) CHECK(PRECIO>0), ID_TIENDA NUMBER(10,0) CONSTRAINT FK_ID_TIENDA REFERENCES TIENDAS(ID_TIENDA) )"
        private const val INSERT_INVENTARIOS_SQL = "INSERT INTO INVENTARIOS" + "  (ID_ARTICULO,NOMBRE,COMENTARIO,PRECIO,ID_TIENDA) VALUES " + " (?, ?, ?, ?, ?)"
        private const val SELECT_USER_BY_ID = "select ID_ARTICULO,NOMBRE,COMENTARIO,PRECIO,ID_TIENDA from Inventarios where ID_TIENDA =?"
        private const val SELECT_ALL_INVENTARIOS = "select * from INVENTARIOS"
        private const val DELETE_INVENTARIOS_SQL = "delete from INVENTARIOS where ID_TIENDA = ?"
        private const val UPDATE_INVENTARIOS_SQL = "update INVENTARIOS set ID_ARTICULO = ?,NOMBRE = ?, COMENTARIO = ?, PRECIO = ? where ID_TIENDA = ?"
    }

    fun listOfInventarios() = inventarios.toList()

    fun prepareTable() {
        val metaData = c.metaData
        c.setAutoCommit(false)
        val rs = metaData.getTables(null, SCHEMA, TABLE, null)

        if (!rs.next()) truncateTable() else createTable()
    }

    private fun truncateTable() {
        println(TRUNCATE_TABLE_INVENTARIOS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.createStatement().use { st ->
                st.execute(TRUNCATE_TABLE_INVENTARIOS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    private fun createTable() {
        println(CREATE_TABLE_INVENTARIOS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            //Get and instance of statement from the connection and use
            //the execute() method to execute the sql
            c.createStatement().use { st ->
                //SQL statement to create a table
                st.execute(CREATE_TABLE_INVENTARIOS_SQL)
            }
            //Commit the change to the database
            //linea de abajo eliminar?
            c.setAutoCommit(false)
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun insert(inventario: Inventarios) {
        println(INSERT_INVENTARIOS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.prepareStatement(INSERT_INVENTARIOS_SQL).use { st ->
                st.setInt(1, inventario.id_articulo)
                st.setString(2, inventario.Nombre)
                st.setString(3, inventario.Comentario)
                st.setDouble(4, inventario.Precio)
                st.setInt(5, inventario.id_tienda)
                println(st)
                st.executeUpdate()
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun selectById(id: Int): Inventarios? {
        var inventario: Inventarios? = null
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_USER_BY_ID).use { st ->
                st.setInt(1, id)
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val ID_ARTICULO = rs.getInt("ID_ARTICULO")
                    val NOMBRE = rs.getString("NOMBRE")
                    val COMENTARIO = rs.getString("COMENTARIO")
                    val PRECIO = rs.getDouble("PRECIO")
                    val ID_TIENDA = rs.getInt("ID_TIENDA")
                    inventario = Inventarios(ID_ARTICULO,NOMBRE,COMENTARIO,PRECIO,ID_TIENDA)
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return inventario
    }

    fun selectAll(): List<Inventarios> {

        // using try-with-resources to avoid closing resources (boiler plate code)
        val INVENTARIOS: MutableList<Inventarios> = ArrayList()
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_ALL_INVENTARIOS).use { st ->
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val ID_ARTICULO = rs.getInt("ID_ARTICULO")
                    val NOMBRE = rs.getString("NOMBRE")
                    val COMENTARIO = rs.getString("COMENTARIO")
                    val PRECIO = rs.getDouble("PRECIO")
                    val ID_TIENDA = rs.getInt("ID_TIENDA")
                    INVENTARIOS.add(Inventarios(ID_ARTICULO,NOMBRE,COMENTARIO,PRECIO,ID_TIENDA))
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return INVENTARIOS
    }

    fun deleteById(id: String): Boolean {
        var rowDeleted = false

        try {
            c.prepareStatement(DELETE_INVENTARIOS_SQL).use { st ->
                st.setString(1, id)
                rowDeleted = st.executeUpdate() > 0
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
        return rowDeleted
    }

    fun update(inventario: Inventarios): Boolean {
        var rowUpdated = false

        try {
            c.prepareStatement(UPDATE_INVENTARIOS_SQL).use { st ->
                st.setInt(1, inventario.id_articulo)
                st.setString(2, inventario.Nombre)
                st.setString(3, inventario.Comentario)
                st.setDouble(4, inventario.Precio)
                st.setInt(5, inventario.id_tienda)
                rowUpdated = st.executeUpdate() > 0
            }
            //Commit the change to the database
            c.setAutoCommit(false)
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
        return rowUpdated
    }

    private fun printSQLException(ex: SQLException) {
        for (e in ex) {
            if (e is SQLException) {
                e.printStackTrace(System.err)
                System.err.println("SQLState: " + e.sqlState)
                System.err.println("Error Code: " + e.errorCode)
                System.err.println("Message: " + e.message)
                var t = ex.cause
                while (t != null) {
                    println("Cause: $t")
                    t = t.cause
                }
            }
        }
    }

}
