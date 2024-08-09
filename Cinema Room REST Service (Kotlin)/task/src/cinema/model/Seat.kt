package cinema.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID

class Seat(
    val row: Int,
    val column: Int,
    var price: Int = 10,
    @JsonIgnore var isAvailable: Boolean = true,
    @JsonIgnore var token: UUID?) {


    constructor(row: Int, column: Int) : this(row, column, 10, true, null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Seat

        if (row != other.row) return false
        if (column != other.column) return false

        return true
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + column
        return result
    }


}