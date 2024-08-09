package cinema.repository

import cinema.model.Seat
import org.springframework.stereotype.Repository
import java.util.UUID

const val TOTAL_ROWS = 9
const val TOTAL_COLUMNS = 9

@Repository
class CinemaRepository {
    final val seats: MutableSet<Seat> = mutableSetOf()
    val totalRows = TOTAL_ROWS
    val totalColumns = TOTAL_COLUMNS

    init {
        for (i in 1..TOTAL_ROWS) {
            for (j in 1..TOTAL_COLUMNS) {
                val price = if (i <=4) 10 else 8
                val token = UUID.randomUUID()
                seats.add(Seat(i, j, price, true, token))
            }
        }
    }

    fun getAllSeats(): List<Seat> {
        return seats.toList()
    }

    fun getAvailableSeats(): List<Seat> {
        return seats.filter { x -> x.isAvailable }
    }

    fun bookSeat(requestedSeat: Seat): Seat {
        var foundSeat: Seat = Seat(0,0)
        for (seat in seats) {

            if (requestedSeat == seat) {
                seat.isAvailable = false
                foundSeat = seat
                break
            };
        }
        return foundSeat
    }

    fun returnSeatByToken(token: UUID): Seat? {
        var foundSeat: Seat? = null
        for (seat in seats) {
            if (token == seat.token && !seat.isAvailable) {
                seat.isAvailable = true
                foundSeat = seat
                break
            }
        }
        return foundSeat
    }

    fun getCurrentIncome(): Int {
        var income = 0
        for (seat in seats) {
            if (!seat.isAvailable) income += seat.price
        }
        return income
    }





}