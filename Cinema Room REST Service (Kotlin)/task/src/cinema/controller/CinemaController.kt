package cinema.controller

import cinema.exception.SeatIsAlreadyBookedException
import cinema.exception.SeatNotFoundException
import cinema.model.Mapper
import cinema.model.Seat
import cinema.repository.CinemaRepository
import cinema.service.CinemaService
import jakarta.annotation.Nullable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

const val SEATS = "/seats"
const val PURCHASE = "/purchase"
const val RETURN = "/return"
const val STATS = "/stats"
@RestController
class CinemaController {
    @Autowired
    val cinemaRepository = CinemaRepository()
    @Autowired
    val cinemaService = CinemaService()



    @GetMapping(SEATS)
    fun getSeats(): ResponseEntity<Mapper> {
        val mappedResponse = Mapper(cinemaRepository.getAvailableSeats(), cinemaRepository.totalRows, cinemaRepository.totalColumns)
        return ResponseEntity.ok(mappedResponse)
    }

    @PostMapping(PURCHASE)
    fun purchase(@RequestBody requestedSeat: Seat): ResponseEntity<Any> {
        try {
            val seat = cinemaService.purchaseTicket(requestedSeat)
            return ResponseEntity.ok().body(mapOf("token" to seat.token, "ticket" to seat))
        } catch (e: SeatNotFoundException) {
            return ResponseEntity.status(400).body(mapOf("error" to e.message))
        } catch (e: SeatIsAlreadyBookedException) {
            return ResponseEntity.status(400).body(mapOf("error" to e.message))
        }

    }

    @PostMapping(RETURN)
    fun refund(@RequestBody request: RefundRequest): ResponseEntity<Any> {
        try {
            val seat = cinemaService.returnTicket(request.token)
            return ResponseEntity.ok().body(mapOf("returned_ticket" to seat))
        } catch (e: SeatNotFoundException) {
            return ResponseEntity.status(400).body(mapOf("error" to "Wrong token!"))
        }
    }


    @GetMapping(STATS)
    fun getStats(@RequestParam(value = "password", defaultValue = "") password: String): ResponseEntity<Any> {
        if (password != "super_secret") return ResponseEntity
            .status(401)
            .body(mapOf("error" to "The password is wrong!"))

        val response: StatsResponse = StatsResponse(
            cinemaRepository.getCurrentIncome(),
            cinemaRepository.getAvailableSeats().size,
            cinemaRepository.getAllSeats().size - cinemaRepository.getAvailableSeats().size)

        return ResponseEntity.ok(response)
    }


    data class RefundRequest (val token: UUID)
    data class StatsResponse (
        val current_income: Int,
        val number_of_available_seats: Int,
        val number_of_purchased_tickets: Int)



}