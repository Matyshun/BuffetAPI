package com.CAT.BuffetAPI.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CAT.BuffetAPI.Entities.App_user;
import com.CAT.BuffetAPI.Entities.Booking;
import com.CAT.BuffetAPI.Entities.Booking_restriction;
import com.CAT.BuffetAPI.Entities.Publication;
import com.CAT.BuffetAPI.Entities.Sale;
import com.CAT.BuffetAPI.Entities.Sale_provision;
import com.CAT.BuffetAPI.Entities.Status_booking;
import com.CAT.BuffetAPI.Entities.Store_schedule;
import com.CAT.BuffetAPI.Entities.Unit;
import com.CAT.BuffetAPI.Services.AuthService;
import com.CAT.BuffetAPI.Services.BookingService;

@RestController
@RequestMapping("/booking")
public class ReservationController {
	@Autowired
	private BookingService reserveService;
	@Autowired
	private AuthService auth;
	
	private void log(String msg) {
		System.out.println(msg);
	}
	private void logLine() {
		System.out.println("----------------------------------------------------------------");
	}
	
	//Create restriccion
	
	@RequestMapping(value = "/booking_restrictions", method = {RequestMethod.POST})
	public String addBookingRestriction(@RequestBody Booking_restriction restriction , HttpServletResponse resp, @RequestHeader("token") String token) {
		logLine();
		log("creando restriccion de reserva");
		if(token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			resp.setStatus(400);
			return null;
		}

		// Check for authorization
		List<String> typesAllowed = new ArrayList<String>();
		//typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			resp.setStatus(401);
			log("no autorizado");
			return null;
		}
		try {
			if(auth.bookingRestrictionValidation(restriction))
			{
				// Setea datos generales
				restriction.setUpdated_at(new Date());
				restriction.setCreated_at(new Date());



				reserveService.updateBookingRestriction(restriction);
				// Status 200 y retorna el Id del APP_USER nuevo
				resp.setStatus(200);
				log("OK");
				return "Restriccion agregada correctamente";
			}
			else
			{
				// 409 Conflict
				resp.setStatus(409);
				log("restriccion ya existe");
				return "Restriccion ya existe";
			}
		}
		catch(Exception e)
		{
			log(e.toString());
			resp.setStatus(500);		
			return "Error interno";
		}

	}
	
	//create reserva
	@RequestMapping(value = "/bookings", method = {RequestMethod.POST})
	public String addBooking(@RequestBody Booking booking , HttpServletResponse resp, @RequestHeader("token") String token) {
		logLine();
		log("Creando reserva");
		if(token.isEmpty()){
			// 400 Bad Request
			resp.setStatus(400);
			return null;
		}

		// Check for authorization
		List<String> typesAllowed = new ArrayList<String>();
		//typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			log("no autorizado");
			resp.setStatus(401);
			return null;
		}
		try {
			if(auth.bookingValidation(booking))
			{
				// Setea datos generales
				booking.setUpdated_at(new Date());
				booking.setCreated_at(new Date());



				reserveService.updateBooking(booking);
				// Status 200 y retorna el Id del APP_USER nuevo
				resp.setStatus(200);
				log("OK");
				return "Reserva agregada correctamente";
			}
			else
			{
				log("Reserva ya existe");
				// 409 Conflict
				resp.setStatus(409);
				return "Reserva ya existe";
			}
		}
		catch(Exception e)
		{
			log(e.toString());
			resp.setStatus(500);		
			return "Error interno";
		}

	}
	
	//Gets
	@RequestMapping(value = "/bookings", method = {RequestMethod.GET})
	private List<Booking> getAllReserves(HttpServletResponse res, @RequestHeader("token") String token,
			@RequestParam (required = false) String appuser_id,
			@RequestParam (required = false) String serv_id ,
			@RequestParam (required = false) String status_booking_id ,
			@RequestParam (required = false) String deleted)
	{
		logLine();
		log("Solicitando listado de reservas");
		if(token.isEmpty()){
			log("token vacio");
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {

			// Get the all the Users (Filtros)
			HashMap<String,Object> data = new HashMap<>();

			if(appuser_id!= null)
			{
				data.put("appuser_id", appuser_id);
			}
			if(serv_id!=null)
			{
				data.put("serv_id", serv_id);
			}
			if(status_booking_id!=null)
			{
				data.put("status_booking_id", status_booking_id);
			}
			
			if(deleted != null)		data.put("deleted", deleted);
			else					data.put("deleted", false);   

			List<Booking> thereserves = reserveService.getAllBooking(data);

			if(thereserves == null)
			{
				//404 not found
				log("lista de reservas vacia");
				res.setStatus(404);
				return null;
			}

			// 200 OK
			log("OK");
			res.setStatus(200);
			return thereserves;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}

	}
	
	@RequestMapping(value = "/booking_restrictions", method = {RequestMethod.GET})
	private List<Booking_restriction> getAllReserveRestrictions(HttpServletResponse res, @RequestHeader("token") String token,
			@RequestParam (required = false) String serv_id ,
			@RequestParam (required = false) String deleted)
	{
		
		logLine();
		log("Solicitando listado de restricciones de reservas");
		if(token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {

			// Get the all the restrictions
			HashMap<String,Object> data = new HashMap<>();

			
			if(serv_id!=null)
			{
				data.put("serv_id", serv_id);
			}
			
			if(deleted != null)		data.put("deleted", deleted);
			else					data.put("deleted", false);   

			List<Booking_restriction> thereserveres = reserveService.getAllBookingRestrictions(data);

			if(thereserveres == null)
			{
				//404 not found
				log("listado de restricciones vacio");
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			log("OK");
			return thereserveres;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}

	}
	
	@RequestMapping("/status_booking")
	private List<Status_booking> getAllBookingRestrictions(HttpServletResponse res,@RequestHeader("token") String token){
		logLine();
		log("solicitando listado de estados de reserva");
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			res.setStatus(401);
			log("no autorizado");
			return null;
		}
		try {
			// Get the all the Units
			List<Status_booking> status = reserveService.getAllStatusBooking();

			if(status == null){
				// 404 Not Found
				log("listado de estados vacio");
				res.setStatus(404);
				return null;
			}

			// 200 OK
			log("OK");
			res.setStatus(200);
			return status;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}
	
	@RequestMapping("/store_schedule")
	private List<Store_schedule> getAllStoreSchedule(HttpServletResponse res,@RequestHeader("token") String token){
		logLine();
		log("solicitando listado de horarios de tienda");
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}
		try {
			// Get the all the Units
			List<Store_schedule> schedules = reserveService.getAllStoreSchedule();

			if(schedules == null){
				log("lista de horarios vacia");
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			log("OK");
			// 200 OK
			res.setStatus(200);
			return schedules;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}
	
	//Get one
	
	@RequestMapping(value="/bookings/{Id}", method = {RequestMethod.GET})
	private Optional<Booking> getSpecificBooking(HttpServletResponse res, @PathVariable("Id") String id, @RequestHeader("token") String token)
	{
		logLine();
		log("solicitando reserva");
		if(id.isEmpty() || token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		// Check for authorization
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<Booking> booking = reserveService.getOneBooking(id);

			// If there is no matching Booking
			if(!booking.isPresent()){
				// 404 Not Found
				log("lista de reservas vacia");
				res.setStatus(404);
				return null;
			}

			// 200 OK
			log("OK");
			res.setStatus(200);
			return booking;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}
	}
	
	@RequestMapping(value="/booking_restrictions/{Id}", method = {RequestMethod.GET})
	private Optional<Booking_restriction> getSpecificBookingRestriction(HttpServletResponse res, @PathVariable("Id") String id, @RequestHeader("token") String token)
	{
		logLine();
		log("Solicitando restriccion de reserva");
		if(id.isEmpty() || token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		// Check for authorization
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<Booking_restriction> restriction = reserveService.getOneBookingRestriction(id);

			// If there is no matching Restriction
			if(!restriction.isPresent()){
				log("lista de restricciones vacia");
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			log("OK");
			res.setStatus(200);
			return restriction;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}
	
	//Update
	
	@RequestMapping(value= "/bookings/{Id}", method = {RequestMethod.POST})
	private String UpdateBooking(HttpServletResponse res,@PathVariable String Id, @RequestBody Booking booking,@RequestHeader("token") String token)
	{
		logLine();
		log("Modificando reserva");
		if(token.isEmpty()){
			log("token vacio");
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			res.setStatus(401);
			log("no autorizado");
			return null;
		}

		try {
			Optional<Booking> optBooking = reserveService.getOneBooking(Id);
	
			// If there is no matching booking
			if(!optBooking.isPresent()){
				// 404 Not Found
				log("lista de reservas vacia");
				res.setStatus(404);
				return null;
			}
	
			Booking oldBooking = optBooking.get();
			
			
			booking.setCreated_at(oldBooking.getCreated_at());
			booking.setUpdated_at(new Date());
			reserveService.updateBooking(booking);
	
			// 200 OK
			log("OK");
			res.setStatus(200);
			return "Reserva actualizada exitosamente";
		
		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}
	
	@RequestMapping(value= "/store_schedule/update", method = {RequestMethod.POST})
	private String UpdateStoreSchedule(HttpServletResponse res, @RequestBody Store_schedule schedule,@RequestHeader("token") String token)
	{
		logLine();
		log("Modificando horario de la tienda");
		if(token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			List<Store_schedule> theSchedules = reserveService.getAllStoreSchedule();
	
			// If there is no matching booking
			if(theSchedules.isEmpty()){
				// 404 Not Found
				log("Listado de horarios vacia");
				res.setStatus(404);
				return null;
			}
	
			Store_schedule theSchedule = theSchedules.get(0);
			
			
			schedule.setCreated_at(theSchedule.getCreated_at());
			schedule.setUpdated_at(new Date());
			reserveService.updateStoreSchedule(schedule);
			log("OK");
			// 200 OK
			res.setStatus(200);
			return "Reserva actualizada exitosamente";
		
		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}
	}
	
	@RequestMapping(value= "/booking_restrictions/{Id}", method = {RequestMethod.POST})
	private String UpdateBookingRestrictions(HttpServletResponse res,@PathVariable String Id, @RequestBody Booking_restriction bookingRestriction,@RequestHeader("token") String token)
	{
		logLine();
		log("Modificando restriccion de reserva");
		if(token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			res.setStatus(401);
			log("no autorizado");
			return null;
		}

		try {
			Optional<Booking_restriction> optBookingRestriction = reserveService.getOneBookingRestriction(Id);
	
			// If there is no matching booking restriction
			if(!optBookingRestriction.isPresent()){
				// 404 Not Found
				log("listado de restricciones vacia");
				res.setStatus(404);
				return null;
			}
	
			Booking_restriction oldBookingRestriction = optBookingRestriction.get();
			
			
			bookingRestriction.setCreated_at(oldBookingRestriction.getCreated_at());
			bookingRestriction.setUpdated_at(new Date());
			reserveService.updateBookingRestriction(bookingRestriction);
	
			// 200 OK
			log("OK");
			res.setStatus(200);
			return "Restriccion actualizada exitosamente";
		
		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}
	
	//Change status
	
	@RequestMapping(value = "/bookings/{Id}/change-status", method = {RequestMethod.POST})
	private String ChangeStatus(HttpServletResponse res, @PathVariable String Id ,@RequestParam("status")String status,@RequestHeader("token") String token)
	{
		logLine();
		if(token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<Booking> booking = reserveService.getOneBooking(Id);

			// If there is no matching reserve
			if(!booking.isPresent()){
				// 404 Not Found
				log("listado de reservas vacia");
				res.setStatus(404);
				return null;
			}

			Booking updateBooking = booking.get();

			
			updateBooking.setStatus_booking_id(status);
			reserveService.updateBooking(updateBooking);

			// 200 OK
			log("OK");
			res.setStatus(200);
			return "Status actualizado exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}
	}
	
	//Delete
	
	@RequestMapping(value= "/bookings/{Id}", method = {RequestMethod.DELETE})
	private String DeleteBooking(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
		logLine();
		log("eliminando reserva");
		if(token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			// Get the Sale
			Optional<Booking> booking = reserveService.getOneBooking(Id);

			// If there is no matching Booking
			if(!booking.isPresent()){
				// 404 Not Found
				log("listado de reservas vacia");
				res.setStatus(404);
				return null;
			}

			Booking delbooking = booking.get();
			delbooking.setDeleted(true);
			reserveService.updateBooking(delbooking);
			
			// 200 OK
			res.setStatus(200);
			log("OK");
			return "Reserva Eliminada Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}
	
	@RequestMapping(value= "/booking_restrictions/{Id}", method = {RequestMethod.DELETE})
	private String DeleteBookingRestrictions(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
		logLine();
		log("Eliminando restriccion de reserva");
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			log("token vacio");
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			// Get the Restriction
			Optional<Booking_restriction> bookingRestriction = reserveService.getOneBookingRestriction(Id);

			// If there is no matching Booking restrictions
			if(!bookingRestriction.isPresent()){
				// 404 Not Found
				log("listado de restricciones vacia");
				res.setStatus(404);
				return null;
			}

			Booking_restriction delbookingrestriction = bookingRestriction.get();
			delbookingrestriction.setDeleted(true);
			reserveService.updateBookingRestriction(delbookingrestriction);
			
			// 200 OK
			res.setStatus(200);
			log("OK");
			return "Restriccion Eliminada Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}
	
	//Restore
	
	@RequestMapping(value= "/bookings/{Id}/restore", method = {RequestMethod.PUT})
	private String RestoreBookings(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
		log("restaurando reserva");
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			log("token vacio");
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
//			res.setStatus(401);
//			return null;
		}

		try {
			// Get the Publication
			Optional<Booking> booking = reserveService.getOneBooking(Id);

			// If there is no matching reserve
			if(!booking.isPresent()){
				// 404 Not Found
				log("listado de reservas vacia");
				res.setStatus(404);
				return null;
			}

			Booking resBoking = booking.get();

			if(!resBoking.isDeleted()){
				// 409 Conflict
				res.setStatus(409);
				log("La reserva no esta eliminada");
				return "La Reserva no está Eliminada";
			}
			
			resBoking.setDeleted(false);
			reserveService.updateBooking(resBoking);

			// 200 OK
			log("OK");
			res.setStatus(200);
			return "Reserva Restaurada Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}
	
	@RequestMapping(value= "/booking_restrictions/{Id}/restore", method = {RequestMethod.PUT})
	private String RestoreBookingRestrictions(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
		log("Restaurando restriccion de reserva");
		if(token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
//			res.setStatus(401);
//			return null;
		}

		try {
			// Get the Publication
			Optional<Booking_restriction> bookingRestriction = reserveService.getOneBookingRestriction(Id);

			// If there is no matching restriction
			if(!bookingRestriction.isPresent()){
				// 404 Not Found
				log("listado de restricciones vacia");
				res.setStatus(404);
				return null;
			}

			Booking_restriction resRestriction = bookingRestriction.get();

			if(!resRestriction.isDeleted()){
				// 409 Conflict
				log("La restriccion no esta eliminada");
				res.setStatus(409);
				return "La Restriccion no está Eliminada";
			}
			
			resRestriction.setDeleted(false);
			reserveService.updateBookingRestriction(resRestriction);

			// 200 OK
			log("OK");
			res.setStatus(200);
			return "Restriccion Restaurada Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}
	}
}
