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

import com.CAT.BuffetAPI.Entities.Reserve;
import com.CAT.BuffetAPI.Services.AuthService;
import com.CAT.BuffetAPI.Services.ReservasService;

@RestController
@RequestMapping("/reserve")
public class ReservationController {
	@Autowired
	private ReservasService reserveService;
	@Autowired
	private AuthService auth;
	
	//Gets
	@RequestMapping(value = "reserves", method = {RequestMethod.GET})
	private List<Reserve> getAllReserves(HttpServletResponse res, @RequestHeader("token") String token,
			@RequestParam (required = false) String appuser_id,
			@RequestParam (required = false) String serv_id ,
			@RequestParam (required = false) String status_reserve_id ,
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
			if(status_reserve_id!=null)
			{
				data.put("status_reserve_id", status_reserve_id);
			}
			
			if(deleted != null)		data.put("deleted", deleted);
			else					data.put("deleted", false);   

			List<Reserve> thereserves = reserveService.getAllReserve(data);

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
}
