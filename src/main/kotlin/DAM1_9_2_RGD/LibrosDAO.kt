package SOBRAS.DAM1_9_2_RGD

import DAM1_9_2_RGD.MyUser
import un9.jdbc.ejemplos.jdbcDAO.UserDAO
import java.sql.DriverManager
import java.sql.SQLException

class LibrosDAO(jdbc:String, user:String, password:String) {

    private val connection = DriverManager.getConnection(jdbc, user, password)
    private val books = mutableListOf<Book>()

    init{
        val query = connection.prepareStatement("SELECT * FROM BOOKS")
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

    fun listOfBooks() = books.toList()

    fun prepareTable() {
        val metaData = c.metaData
        val rs = metaData.getTables(null, UserDAO.SCHEMA, UserDAO.TABLE, null)

        if (!rs.next()) createTable() else truncateTable()
    }

    private fun truncateTable() {
        println(UserDAO.TRUNCATE_TABLE_USERS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.createStatement().use { st ->
                st.execute(UserDAO.TRUNCATE_TABLE_USERS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    private fun createTable() {
        println(UserDAO.CREATE_TABLE_USERS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            //Get and instance of statement from the connection and use
            //the execute() method to execute the sql
            c.createStatement().use { st ->
                //SQL statement to create a table
                st.execute(UserDAO.CREATE_TABLE_USERS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun insert(user: MyUser) {
        println(UserDAO.INSERT_USERS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.prepareStatement(UserDAO.INSERT_USERS_SQL).use { st ->
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
            c.prepareStatement(UserDAO.SELECT_USER_BY_ID).use { st ->
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
            c.prepareStatement(UserDAO.SELECT_ALL_USERS).use { st ->
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
            c.prepareStatement(UserDAO.DELETE_USERS_SQL).use { st ->
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
            c.prepareStatement(UserDAO.UPDATE_USERS_SQL).use { st ->
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

}