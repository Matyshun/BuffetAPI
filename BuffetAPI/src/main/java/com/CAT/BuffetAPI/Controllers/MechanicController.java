package com.CAT.BuffetAPI.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CAT.BuffetAPI.Entities.App_user;
import com.CAT.BuffetAPI.Entities.User_type;
import com.CAT.BuffetAPI.Repositories.userTypeRepository;
import com.CAT.BuffetAPI.Services.App_UserService;
import com.CAT.BuffetAPI.Services.AuthService;


//Este controlador es igual al de app_user aunque esta pensado especificamente para los mecanicos.
//ya que estos tienen funciones especificas.

@RestController
@RequestMapping("/mech-adm")
public class MechanicController {
	@Autowired
	App_UserService app;
	
	@Autowired
	private AuthService auth;
	
	private void log(String msg) {
		System.out.println(msg);
	}
	private void logLine() {
		System.out.println("----------------------------------------------------------------");
	}
	
	//Lista de mecanicos
	
	@RequestMapping("/mechanics")
	private List<App_user> getAllMec(HttpServletResponse res, @RequestHeader("token") String token,
													@RequestParam (required = false) String username,
													@RequestParam (required = false) String rut,
													@RequestParam (required = false) String status_id,
													@RequestParam (required = false) String deleted)
	{
	
		logLine();
		log("iniciando lista de mecanicos...");
		if(token.isEmpty()){
			log("token vacio");
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
//		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
//			res.setStatus(401);
//			return null;
//		}

		try {

				// Get the all the Users
				HashMap<String,Object> data = new HashMap<>();
				
				//Filtros
				if(username!= null)
				{
					data.put("username", username);
				}
				if(rut!=null)
				{
					data.put("rut", rut);
				}
				if(status_id!=null)
				{
					data.put("status_id", status_id);
				}
				if(deleted != null)
				{
					data.put("deleted", deleted);
				}
				else
				{
					data.put("deleted", false);
				}
				
				List<App_user> userList = app.getData(data);

				if(userList == null){
					log("no se encuentran usuarios");
					// 404 Not Found
					res.setStatus(404);
					return null;
				}
				//se filtran unicamente los mecanicos
				List<App_user> onlyMec = new ArrayList<App_user>();
				for (App_user app_user : userList) {
					app_user.setHash("");
					if(app_user.getUser_type_id().equals("MEC"))
						onlyMec.add(app_user);
				}
				log("OK");
				// 200 OK
				res.setStatus(200);
				return onlyMec;
			
		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	
	}

	//GET mecanico en especifico
	
	@RequestMapping(value="/mechanics/{Id}", method = {RequestMethod.GET})
	private Optional<App_user> getSpecificMec(HttpServletResponse res, @PathVariable("Id") String id, @RequestHeader("token") String token)
	{
		logLine();
		log("Request usuario especifico");
		if(id.isEmpty() || token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		// Check for authorization
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
//		if(!auth.Authorize(token, typesAllowed)){
//			 401 Unauthorized
//			res.setStatus(401);
//			return null;
//		}

		try {
			// Get the User
			Optional<App_user> user = app.getAppUser(id);

			// If there is no matching Mechanic
			if(!user.isPresent()){
				// 404 Not Found
				log("usuario no encontrado");
				res.setStatus(404);
				return null;
			}
			if(!user.get().getUser_type_id().equals("MEC")) {
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return user;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	//Modificar usuario especifico
	@RequestMapping(value= "/mechanics/{Id}", method = {RequestMethod.POST})
	private String UpdateMec(HttpServletResponse res,@PathVariable String Id, @RequestBody App_user reqUser,@RequestHeader("token") String token)
	{
		logLine();
		log("Modificacion de usuario");
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {
			String userId = reqUser.getAppuser_id();
			Optional<App_user> optUser = app.getAppUser(userId);
	
			// If there is no matching User
			if(!optUser.isPresent()){
				log("usuario no encontrado");
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
	
			App_user oldUser = optUser.get();
			
			reqUser.setAppuser_id(oldUser.getAppuser_id());
			reqUser.setLastlogin(oldUser.getLastlogin());
			reqUser.setCreated_at(oldUser.getCreated_at());
			reqUser.setHash(oldUser.getHash());
			reqUser.setUpdated_at(new Date());
			app.updateUser(reqUser);
	
			log("OK");
			// 200 OK
			res.setStatus(200);
			return "Mecanico actualizado exitosamente";
		
		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	//cambio de estatus
	@RequestMapping(value = "/mechanics/{Id}/change-status", method = {RequestMethod.POST})
	private String ChangeStatus(HttpServletResponse res, @PathVariable String Id ,@RequestParam("status")String status,@RequestHeader("token") String token)
	{
		logLine();
		log("Cambiando estatus");
		if(token.isEmpty()){
			log("token vacio");
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
			log("no autorizado");
			// 401 Unauthorized
//			res.setStatus(401);
//			return null;
		}

		try {
			// Get the User
			Optional<App_user> user = app.getAppUser(Id);

			// If there is no matching User
			if(!user.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			App_user updateUser = user.get();

			// Revisa si el Usuario es un Mecánico
			if(!updateUser.getUser_type_id().equals("MEC")){
				// 409 Conflict
				res.setStatus(409);
				return null;
			}
			
			updateUser.setStatus_id(status);
			app.updateUser(updateUser);
			log("OK");
			// 200 OK
			res.setStatus(200);
			return "Status actualizado exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	//Delete mecanico especifico
	@RequestMapping(value= "/mechanics/{Id}", method = {RequestMethod.DELETE})
	private String DeleteMec(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
		logLine();
		log("eliminando mecanico");
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<App_user> user = app.getAppUser(Id);

			// If there is no matching Mechanic
			if(!user.isPresent()){
				log("mecanico no encontrado");

				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			App_user delUser = user.get();
			
			delUser.setDeleted(true);
			app.updateUser(delUser);
			log("OK");

			// 200 OK
			res.setStatus(200);
			return "Mecánico Eliminado Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}
	
	//restaurar Mecanico
	@RequestMapping(value= "/mechanics/{Id}/restore", method = {RequestMethod.PUT})
	private String RestoreMec(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
		logLine();
		log("Restaurando mecanico");
		if(token.isEmpty()){
			log("token vacio");
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
			log("no autorizado");
//			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<App_user> user = app.getAppUser(Id);

			// If there is no matching User
			if(!user.isPresent()){
				log("no se encuentra usuario");
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			App_user resUser = user.get();

			if(!resUser.isDeleted()){
				log("el mecanico no esta eliminado");
				// 409 Conflict
				res.setStatus(409);
				return "El Mecánico no está Eliminado";
			}
			
			resUser.setDeleted(false);
			app.updateUser(resUser);
			log("OK");
			// 200 OK
			res.setStatus(200);
			return "Mecánico Restaurado Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

}
