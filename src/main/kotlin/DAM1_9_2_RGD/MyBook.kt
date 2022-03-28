package DAM1_9_2_RGD

import java.sql.Date

data class MyBook(
    var id: String,
    var author: String,
    var title: String,
    var genre: String,
    var price: Double,
    var publish_date: Date,
    var description: String
    )
