package DAM1_9_3_RGD;

import DAM1_9_2_RGD.MyBook
import java.sql.DriverManager
import java.sql.SQLException

class TiendasDAO(jdbc:String, user:String, password:String) {

    private val c = DriverManager.getConnection(jdbc,user,password)
    private val tiendas = mutableListOf<Tiendas>()

    init{
        val query = c.prepareStatement("SELECT * FROM TIENDAS")
        val result = query.executeQuery()
        while (result.next()){
            val id = result.getInt("id_tienda")
            val nombre = result.getString("nombre_tienda")
            val direccion = result.getString("direccion_tienda")

            tiendas.add(Tiendas(id,nombre,direccion))
        }
    }

    companion object {
        private const val SCHEMA = "default"
        private const val TABLE = "TIENDAS"
        private const val TRUNCATE_TABLE_TIENDAS_SQL = "TRUNCATE TABLE TIENDAS"
        private const val CREATE_TABLE_TIENDAS_SQL =
            "CREATE TABLE TIENDAS (ID_TIENDA NUMBER(10,0) CONSTRAINT PK_ID_TIENDA PRIMARY KEY, NOMBRE_TIENDA VARCHAR2(40), DIRECCION_TIENDA VARCHAR2(200) )"
        private const val INSERT_TIENDAS_SQL = "INSERT INTO TIENDAS" + "  (id,nombre,direccion) VALUES " + " (?, ?, ?)"
        private const val SELECT_USER_BY_ID = "select id_tienda,nombre_tienda,direccion_tienda from TIENDAS where id_tienda =?"
        private const val SELECT_ALL_TIENDAS = "select * from TIENDAS"
        private const val DELETE_TIENDAS_SQL = "delete from TIENDAS where id_tienda = ?"
        private const val UPDATE_TIENDAS_SQL = "update TIENDAS set nombre_tienda = ?,direccion_tienda= ? where id_tienda = ?"
    }

    fun listOfTiendas() = tiendas.toList()

    fun prepareTable() {
        val metaData = c.metaData
        c.setAutoCommit(false)
        val rs = metaData.getTables(null, SCHEMA, TABLE, null)

        if (!rs.next()) truncateTable() else createTable()
    }

    private fun truncateTable() {
        println(TRUNCATE_TABLE_TIENDAS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.createStatement().use { st ->
                st.execute(TRUNCATE_TABLE_TIENDAS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    private fun createTable() {
        println(CREATE_TABLE_TIENDAS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            //Get and instance of statement from the connection and use
            //the execute() method to execute the sql
            c.createStatement().use { st ->
                //SQL statement to create a table
                st.execute(CREATE_TABLE_TIENDAS_SQL)
            }
            //Commit the change to the database
            //linea de abajo eliminar?
            c.setAutoCommit(false)
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun insert(tienda: Tiendas) {
        println(INSERT_TIENDAS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.prepareStatement(INSERT_TIENDAS_SQL).use { st ->
                st.setInt(1, tienda.id)
                st.setString(2, tienda.NOMBRE)
                st.setString(3, tienda.DIRECCION)
                println(st)
                st.executeUpdate()
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun selectById(id: Int): Tiendas? {
        var tienda: Tiendas? = null
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_USER_BY_ID).use { st ->
                st.setInt(1, id)
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val id = rs.getInt("ID_TIENDA")
                    val nombre = rs.getString("NOMBRE_TIENDA")
                    val direccion = rs.getString("DIRECCION_TIENDA")
                    tienda = Tiendas(id,nombre,direccion)
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return tienda
    }

    fun selectAll(): List<Tiendas> {

        // using try-with-resources to avoid closing resources (boiler plate code)
        val TIENDAS: MutableList<Tiendas> = ArrayList()
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_ALL_TIENDAS).use { st ->
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val id = rs.getInt("ID_TIENDA")
                    val nombre = rs.getString("NOMBRE_TIENDA")
                    val direccion = rs.getString("DIRECCION_TIENDA")
                    TIENDAS.add(Tiendas(id,nombre,direccion))
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return TIENDAS
    }

    fun deleteById(id: String): Boolean {
        var rowDeleted = false

        try {
            c.prepareStatement(DELETE_TIENDAS_SQL).use { st ->
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

    fun update(tienda: Tiendas): Boolean {
        var rowUpdated = false

        try {
            c.prepareStatement(UPDATE_TIENDAS_SQL).use { st ->
                st.setInt(1, tienda.id)
                st.setString(2, tienda.NOMBRE)
                st.setString(3, tienda.DIRECCION)
                rowUpdated = st.executeUpdate() > 0
            }
            //Commit the change to the database
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
