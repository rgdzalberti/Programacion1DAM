package SOBRAS.DAM1_9_2_RGD

import DAM1_9_2_RGD.MyUser
import java.sql.DriverManager
import java.sql.SQLException


class LibrosDAO(jdbc:String, user:String, password:String) {

    private val c = DriverManager.getConnection(jdbc, user, password)
    private val books = mutableListOf<Book>()

    init{
        val query = c.prepareStatement("SELECT * FROM BOOKS")
        val result = query.executeQuery()
        while (result.next()){
            val id = result.getString("id")
            val author = result.getString("author")
            val title = result.getString("title")
            val genre = result.getString("genre")
            val price = result.getDouble("price")
            val publishDate = result.getDate("publish_date")
            val description = result.getString("description")

            books.add(Book(id,author, title, genre, price, publishDate, description))
        }
    }

    companion object {
        private const val SCHEMA = "default"
        private const val TABLE = "USERS"
        private const val TRUNCATE_TABLE_USERS_SQL = "TRUNCATE TABLE USERS"
        private const val CREATE_TABLE_USERS_SQL =
            "CREATE TABLE USERS (id  number(3) NOT NULL AUTO_INCREMENT,name varchar(120) NOT NULL,email varchar(220) NOT NULL,country varchar(120),PRIMARY KEY (id))"
        private const val INSERT_USERS_SQL = "INSERT INTO USERS" + "  (name, email, country) VALUES " + " (?, ?, ?);"
        private const val SELECT_USER_BY_ID = "select id,name,email,country from USERS where id =?"
        private const val SELECT_ALL_USERS = "select * from USERS"
        private const val DELETE_USERS_SQL = "delete from USERS where id = ?;"
        private const val UPDATE_USERS_SQL = "update USERS set name = ?,email= ?, country =? where id = ?;"
    }

    fun listOfBooks() = books.toList()

    fun prepareTable() {
        val metaData = c.metaData
        val rs = metaData.getTables(null, SCHEMA, TABLE, null)

        if (!rs.next()) createTable() else truncateTable()
    }

    private fun truncateTable() {
        println(TRUNCATE_TABLE_USERS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.createStatement().use { st ->
                st.execute(TRUNCATE_TABLE_USERS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    private fun createTable() {
        println(CREATE_TABLE_USERS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            //Get and instance of statement from the connection and use
            //the execute() method to execute the sql
            c.createStatement().use { st ->
                //SQL statement to create a table
                st.execute(CREATE_TABLE_USERS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun insert(user: MyUser) {
        println(INSERT_USERS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.prepareStatement(INSERT_USERS_SQL).use { st ->
                st.setString(1, user.name)
                st.setString(2, user.email)
                st.setString(3, user.country)
                println(st)
                st.executeUpdate()
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun selectById(id: Int): MyUser? {
        var user: MyUser? = null
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_USER_BY_ID).use { st ->
                st.setInt(1, id)
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val name = rs.getString("name")
                    val email = rs.getString("email")
                    val country = rs.getString("country")
                    user = MyUser(id, name, email, country)
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return user
    }

    fun selectAll(): List<MyUser> {

        // using try-with-resources to avoid closing resources (boiler plate code)
        val users: MutableList<MyUser> = ArrayList()
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_ALL_USERS).use { st ->
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val id = rs.getInt("id")
                    val name = rs.getString("name")
                    val email = rs.getString("email")
                    val country = rs.getString("country")
                    users.add(MyUser(id, name, email, country))
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return users
    }

    fun deleteById(id: Int): Boolean {
        var rowDeleted = false

        try {
            c.prepareStatement(DELETE_USERS_SQL).use { st ->
                st.setInt(1, id)
                rowDeleted = st.executeUpdate() > 0
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
        return rowDeleted
    }

    fun update(user: MyUser): Boolean {
        var rowUpdated = false

        try {
            c.prepareStatement(UPDATE_USERS_SQL).use { st ->
                st.setString(1, user.name)
                st.setString(2, user.email)
                st.setString(3, user.country)
                st.setInt(4, user.id)
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