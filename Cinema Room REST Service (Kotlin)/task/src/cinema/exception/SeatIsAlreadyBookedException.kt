package cinema.exception

class SeatIsAlreadyBookedException: Exception() {
    override val message = "The ticket has been already purchased!"
}