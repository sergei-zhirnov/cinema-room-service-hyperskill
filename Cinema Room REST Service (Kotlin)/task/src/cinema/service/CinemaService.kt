package cinema.service

import cinema.model.Seat
import cinema.repository.CinemaRepository
import org.springframework.stereotype.Service
import cinema.exception.SeatNotFoundException
import cinema.exception.SeatIsAlreadyBookedException
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

@Service
class CinemaService {
    @Autowired
    val cinemaRepository = CinemaRepository()

    fun purchaseTicket(seat: Seat): Seat {
        if (seat !in cinemaRepository.getAllSeats()) {
            throw SeatNotFoundException()
        } else if (seat !in cinemaRepository.getAvailableSeats()) {
            throw SeatIsAlreadyBookedException()
        }

        return cinemaRepository.bookSeat(seat)
    }

    fun returnTicket(token: UUID): Seat {
        val seat: Seat = cinemaRepository.returnSeatByToken(token) ?: throw SeatNotFoundException()
        return seat
    }

}