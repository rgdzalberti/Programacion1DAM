package SOBRAS.DAM1_9_2_RGD

import DAM1_9_2_RGD.MyBook
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
        private const val TABLE = "BOOKS"
        private const val TRUNCATE_TABLE_BOOKS_SQL = "TRUNCATE TABLE BOOKS"
        private const val CREATE_TABLE_BOOKS_SQL =
            "CREATE TABLE BOOKS (id  number(5) NOT NULL ,author varchar2(50) NOT NULL,title varchar2(75) NOT NULL,genre varchar2(30),price number(4,2),publish_date date,description varchar2(200),PRIMARY KEY (id))"
        //"CREATE TABLE BOOKS (id  number(3) NOT NULL AUTO_INCREMENT,name varchar(120) NOT NULL,email varchar(220) NOT NULL,country varchar(120),PRIMARY KEY (id))"
        private const val INSERT_BOOKS_SQL = "INSERT INTO BOOKS" + "  (id,author,title,genre,price,publish_date,description) VALUES " + " (?, ?, ?, ?, ?, ?, ?);"
        private const val SELECT_USER_BY_ID = "select id,author,title,genre,price,publish_date,description from BOOKS where id =?"
        private const val SELECT_ALL_BOOKS = "select * from BOOKS"
        private const val DELETE_BOOKS_SQL = "delete from BOOKS where id = ?;"
        private const val UPDATE_BOOKS_SQL = "update BOOKS set author = ?,title= ?, genre =?, price =?, publish_date =?,description =? where id = ?;"
    }

    fun listOfBooks() = books.toList()

    fun prepareTable() {
        val metaData = c.metaData
        val rs = metaData.getTables(null, SCHEMA, TABLE, null)

        if (!rs.next()) createTable() else truncateTable()
    }

    private fun truncateTable() {
        println(TRUNCATE_TABLE_BOOKS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.createStatement().use { st ->
                st.execute(TRUNCATE_TABLE_BOOKS_SQL)
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    private fun createTable() {
        println(CREATE_TABLE_BOOKS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            //Get and instance of statement from the connection and use
            //the execute() method to execute the sql
            c.createStatement().use { st ->
                //SQL statement to create a table
                st.execute(CREATE_TABLE_BOOKS_SQL)
            }
            //Commit the change to the database
            //linea de abajo eliminar?
            //c.setAutoCommit(false)
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    fun insert(book: MyBook) {
        println(INSERT_BOOKS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.prepareStatement(INSERT_BOOKS_SQL).use { st ->
                st.setString(1, book.id)
                st.setString(2, book.author)
                st.setString(3, book.title)
                st.setString(4, book.genre)
                st.setString(5, book.price)
                st.setString(6, book.publish_date)
                st.setString(7, book.description)
                println(st)
                st.executeUpdate()
            }
            //Commit the change to the database
            c.commit()
        } catch (e: SQLException) {
            printSQLException(e)
        }
    }

    /*
        fun insert(user: MyUser) {
        println(INSERT_BOOKS_SQL)
        // try-with-resource statement will auto close the connection.
        try {
            c.prepareStatement(INSERT_BOOKS_SQL).use { st ->
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
     */

    fun selectById(id: Int): MyBook? {
        var book: MyBook? = null
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_USER_BY_ID).use { st ->
                st.setInt(1, id)
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val id = rs.getString("12345")
                    val author = rs.getString("Ricardo")
                    val title = rs.getString("El ejercicio de inglés")
                    val genre = rs.getString("Literario")
                    val price = rs.getString("6.00")
                    val publish_date = rs.getString("20/03/2000")
                    val description = rs.getString("Que libro más bueno")
                    book = MyBook(id,author,title,genre,price,publish_date,description)
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return book
    }

    fun selectAll(): List<MyBook> {

        // using try-with-resources to avoid closing resources (boiler plate code)
        val BOOKS: MutableList<MyBook> = ArrayList()
        // Step 1: Establishing a Connection
        try {
            c.prepareStatement(SELECT_ALL_BOOKS).use { st ->
                println(st)
                // Step 3: Execute the query or update query
                val rs = st.executeQuery()

                // Step 4: Process the ResultSet object.
                while (rs.next()) {
                    val id = rs.getString("12345")
                    val author = rs.getString("Ricardo")
                    val title = rs.getString("El ejercicio de inglés")
                    val genre = rs.getString("Literario")
                    val price = rs.getString("6.00")
                    val publish_date = rs.getString("20/03/2000")
                    val description = rs.getString("Que libro más bueno")
                    BOOKS.add(MyBook(id,author,title,genre,price,publish_date,description))
                }
            }

        } catch (e: SQLException) {
            printSQLException(e)
        }
        return BOOKS
    }

    fun deleteById(id: Int): Boolean {
        var rowDeleted = false

        try {
            c.prepareStatement(DELETE_BOOKS_SQL).use { st ->
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

    fun update(book: MyBook): Boolean {
        var rowUpdated = false

        try {
            c.prepareStatement(UPDATE_BOOKS_SQL).use { st ->
                st.setString(1, book.id)
                st.setString(2, book.author)
                st.setString(3, book.title)
                st.setString(4, book.genre)
                st.setString(5, book.price)
                st.setString(6, book.publish_date)
                st.setString(7, book.description)
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