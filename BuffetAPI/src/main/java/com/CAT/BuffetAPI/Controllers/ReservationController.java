package com.CAT.BuffetAPI.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CAT.BuffetAPI.Entities.Booking;
import com.CAT.BuffetAPI.Entities.Booking_restriction;
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
	
	//Gets
	@RequestMapping(value = "/bookings", method = {RequestMethod.GET})
	private List<Booking> getAllReserves(HttpServletResponse res, @RequestHeader("token") String token,
			@RequestParam (required = false) String appuser_id,
			@RequestParam (required = false) String serv_id ,
			@RequestParam (required = false) String status_booking_id ,
			@RequestParam (required = false) String deleted)
	{

		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {

			// Get the all the Users
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
				data.put("status_reserve_id", status_booking_id);
			}
			
			if(deleted != null)		data.put("deleted", deleted);
			else					data.put("deleted", false);   

			List<Booking> thereserves = reserveService.getAllBooking(data);

			if(thereserves == null)
			{
				//404 not found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return thereserves;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			System.out.println(e);
			res.setStatus(500);
			return null;
		}

	}
	
	@RequestMapping(value = "/booking_restrictions", method = {RequestMethod.GET})
	private List<Booking_restriction> getAllReserveRestrictions(HttpServletResponse res, @RequestHeader("token") String token,
			@RequestParam (required = false) String serv_id ,
			@RequestParam (required = false) String deleted)
	{

		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
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
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return thereserveres;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			System.out.println(e);
			res.setStatus(500);
			return null;
		}

	}
	
	@RequestMapping("/status_booking")
	private List<Status_booking> getAllBookingRestrictions(HttpServletResponse res,@RequestHeader("token") String token){
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}
		try {
			// Get the all the Units
			List<Status_booking> status = reserveService.getAllStatusBooking();

			if(status == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return status;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}
	
	@RequestMapping("/store_schedule")
	private List<Store_schedule> getAllStoreSchedule(HttpServletResponse res,@RequestHeader("token") String token){
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}
		try {
			// Get the all the Units
			List<Store_schedule> schedules = reserveService.getAllStoreSchedule();

			if(schedules == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return schedules;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}
}
