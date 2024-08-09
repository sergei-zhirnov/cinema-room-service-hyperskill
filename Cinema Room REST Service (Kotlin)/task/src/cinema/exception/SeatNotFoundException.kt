package cinema.exception

class SeatNotFoundException: Exception() {
    override val message = "The number of a row or a column is out of bounds!"
}