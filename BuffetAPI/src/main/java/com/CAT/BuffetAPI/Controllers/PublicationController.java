package com.CAT.BuffetAPI.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CAT.BuffetAPI.Entities.App_user;
import com.CAT.BuffetAPI.Entities.Booking_restriction;
import com.CAT.BuffetAPI.Entities.Product;
import com.CAT.BuffetAPI.Entities.Public_status;
import com.CAT.BuffetAPI.Entities.Publication;
import com.CAT.BuffetAPI.Entities.PublicationExt;
import com.CAT.BuffetAPI.Entities.Sale;
import com.CAT.BuffetAPI.Entities.User_status;
import com.CAT.BuffetAPI.Entities.User_type;
import com.CAT.BuffetAPI.Repositories.Public_statusRepository;
import com.CAT.BuffetAPI.Repositories.PublicationRepository;
import com.CAT.BuffetAPI.Services.App_UserService;
import com.CAT.BuffetAPI.Services.AuthService;
import com.CAT.BuffetAPI.Services.PublicationService;

@RestController
@RequestMapping("/pub-adm")
public class PublicationController {
	@Autowired
	private PublicationService pub;
	@Autowired
	private PublicationRepository pubrepo;
	@Autowired
	private Public_statusRepository statusRepo;

	@Autowired
	private AuthService auth;
	@Autowired
	private App_UserService app;

	@RequestMapping("/publications")
	private List<PublicationExt> getAllPublications(HttpServletResponse res,
			@RequestParam(required = false) String comuna
			,@RequestParam(required = false) String public_status_id
			,@RequestParam(required = false) String bussiness_name
			,@RequestParam(required = false) String title
			,@RequestParam(required = false) String deleted)
	{
		try {
			// Get the all the Users
			HashMap<String,Object> data = new HashMap<>();

			if(comuna!= null)
			{
				data.put("comuna", comuna);
			}
			if(public_status_id!=null)
			{
				data.put("public_status_id", public_status_id);
			}
			if(title != null)
			{
				data.put("title",title);
			}
			if(bussiness_name != null)
			{
				data.put("bussiness_name",bussiness_name);
			}
			if(deleted != null)
			{
				data.put("deleted",deleted);
			}
			else
			{
				data.put("deleted",0);
			}


			// Get ALL PUBLICATIONS
			List<Publication> pubList = pubrepo.getData(data);
			if(pubList == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			
			// Get ALL PUBLICATION STATUS
			List<Public_status> statusList = statusRepo.findAll();
			if(statusList == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// GET ALL USERS
			List<App_user> userList = app.getAllUsers();
			if(userList == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			
			// ArrayList porque inicializar una List de la nada en Java es terrible
			ArrayList<PublicationExt> sendPubList = new ArrayList<PublicationExt>();

			// Procesar para extender le modelo
			for (Publication pub : pubList) {
				// Inicilizar el modelo extendido
				PublicationExt sendPub = new PublicationExt();

				// Copia las propiedades del modelo base a la extensión usando BeanUtils
				BeanUtils.copyProperties(sendPub, pub);

				// Procesa la Publicación para agregar la data secundaria
				sendPub = processPub(sendPub, userList, statusList);

				// Agrega el modelo extendido a la lista que se enviará
				sendPubList.add(sendPub);
			}

			// 200 OK
			res.setStatus(200);
			return sendPubList;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	
	@RequestMapping(value = "/publications", method = {RequestMethod.POST})
	public String addPublication(@RequestBody Publication publication , HttpServletResponse resp, @RequestHeader("token") String token) {

		if(token.isEmpty()){
			// 400 Bad Request
			resp.setStatus(400);
			return null;
		}

		// Check for authorization
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("MEC");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			resp.setStatus(401);
			return null;
		}

		try {
			if(auth.publicationValidation(publication))
			{
				// Setea datos generales
				publication.setUpdated_at(new Date());
				publication.setCreated_at(new Date());



				Publication newPub = pub.UpdatePublication(publication);
				// Status 200 y retorna el Id del APP_USER nuevo
				resp.setStatus(200);
				return newPub.getPublic_id();
			}
			else
			{
				// 409 Conflict
				resp.setStatus(409);
				return "Publicación ya existe";
			}
		}
		catch(Exception e)
		{
			resp.setStatus(500);		
			return "Error interno";
		}

	}


	@RequestMapping(value="/publications/{Id}", method = {RequestMethod.GET})
	private PublicationExt getSpecificPub(HttpServletResponse res, @PathVariable("Id") String id)
	{
		if(id.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		try {
			// Get the User
			Optional<Publication> publication = pub.getOnePublication(id);

			// If there is no matching User
			if(!publication.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			

			// Get ALL PUBLICATION STATUS
			List<Public_status> statusList = statusRepo.findAll();
			if(statusList == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// GET ALL USERS
			List<App_user> userList = app.getAllUsers();
			if(userList == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			

			// Inicilizar el modelo extendido
			PublicationExt sendPub = new PublicationExt();

			// Copia las propiedades del modelo base a la extensión usando BeanUtils
			BeanUtils.copyProperties(sendPub, publication.get());

			// Procesa la Publicación para agregar la data secundaria
			sendPub = processPub(sendPub, userList, statusList);


			// 200 OK
			res.setStatus(200);
			return sendPub;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}


	@RequestMapping(value= "/publications/{Id}", method = {RequestMethod.DELETE})
	private ResponseEntity<JsonObject> DeletePub(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}
		HttpHeaders errorHeaders = new HttpHeaders();
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
//			res.setStatus(401);
//			return null;
		}
		List<Publication> publicaciones = new ArrayList<Publication>();
		boolean existe = false;
		publicaciones = pub.getAllPublications();
		Publication publication = null;
		for(Publication u : publicaciones)
		{
			if(u.getPublic_id().equals(Id))
			{
				existe = true;
				publication = u;
				break;
			}
		}

		if(existe) {
			publication.setDeleted(true);
			pub.UpdatePublication(publication);
			return new ResponseEntity<JsonObject>(errorHeaders, HttpStatus.OK); 

		}
		else
		{
			errorHeaders.set("error-code", "ERR-AUTH-004");
			errorHeaders.set("error-desc", "Publicacion no existe");
			return new ResponseEntity<JsonObject>(errorHeaders, HttpStatus.UNAUTHORIZED); 
		}


	}


	@RequestMapping(value= "/publications/{Id}/restore", method = {RequestMethod.PUT})
	private String RestorePub(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
		if(token.isEmpty()){
			// 400 Bad Request
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
			Optional<Publication> publication = pub.getOnePublication(Id);

			// If there is no matching Publication
			if(!publication.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Publication resPub = publication.get();

			if(!resPub.isDeleted()){
				// 409 Conflict
				res.setStatus(409);
				return "La Publicación no está Eliminada";
			}
			
			resPub.setDeleted(false);
			pub.UpdatePublication(resPub);

			// 200 OK
			res.setStatus(200);
			return "Publicación Restaurada Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}


	@RequestMapping(value = "/publications/{Id}/change-status", method = {RequestMethod.POST})
	private ResponseEntity<JsonObject> ChangeStatus(HttpServletResponse res, @PathVariable String Id ,@RequestParam("public_status_id")String public_status_id,@RequestHeader("token") String token)
	{
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}
		HttpHeaders errorHeaders = new HttpHeaders();
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
//			res.setStatus(401);
//			return null;
		}
		Public_status theStatus = null;
		for(Public_status status : statusRepo.findAll())
		{
			if(status.getPublic_status_id().equals(public_status_id))
			{
				theStatus = status;
			}
		}
		Publication publication;
		publication = pub.getOnePublication(Id).get();

		if(publication!= null)
		{
			if(theStatus != null)
			{
				publication.setPublic_status_id(theStatus.getPublic_status_id());
				pub.UpdatePublication(publication);
				return new ResponseEntity<JsonObject>(errorHeaders, HttpStatus.OK); 
			}
			else
			{
				errorHeaders.set("error-code", "ERR-AUTH-002");
				errorHeaders.set("error-desc", "estatus no existe");
				return new ResponseEntity<JsonObject>(errorHeaders, HttpStatus.UNAUTHORIZED); 	
			}


		}
		else
		{
			errorHeaders.set("error-code", "ERR-AUTH-001");
			errorHeaders.set("error-desc", "Publicacion no existe");
			return new ResponseEntity<JsonObject>(errorHeaders, HttpStatus.UNAUTHORIZED); 	
		}

	}


	@RequestMapping("/public-status")
	private List<Public_status> getAllStatus(HttpServletResponse res){

		try {
			// Get the all the Users
			List<Public_status> status = statusRepo.findAll();

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

	
	@RequestMapping(value= "/publications/{Id}", method = {RequestMethod.POST})
	private ResponseEntity<JsonObject> UpdatePub(HttpServletResponse res,@PathVariable String Id, @RequestBody Publication publication,@RequestHeader("token") String token)
	{

		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}
		HttpHeaders errorHeaders = new HttpHeaders();
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
//
//			res.setStatus(401);
//			return null;
		}
		List<Publication> allUsers = new ArrayList<Publication>();
		boolean existe = false;
		allUsers = pub.getAllPublications();
		for(Publication u : allUsers)
		{
			if(u.getPublic_id().equals(Id))
			{
				existe = true;
				break;
			}
		}

		if(existe) {

			pub.UpdatePublication(publication);
			return new ResponseEntity<JsonObject>(errorHeaders, HttpStatus.OK); 

		}
		else
		{

			errorHeaders.set("error-code", "ERR-AUTH-001");
			errorHeaders.set("error-desc", "Publicacion no existe");
			return new ResponseEntity<JsonObject>(errorHeaders, HttpStatus.UNAUTHORIZED); 
		}
	}


	// Agrega la información secundaria del modelo extendido
	private PublicationExt processPub(PublicationExt pub, List<App_user> userList, List<Public_status> statusList){
		
		if(pub == null || userList == null || statusList == null){
			return null;
		}

		// Consigue el Status de esta Publicación
		Public_status thisStatus = statusList.stream()
			.filter(item -> item.getPublic_status_id().equals(pub.getPublic_status_id()))
			.findFirst()
			.get();

		// Consigue el Mecánico de esta Publicación
		App_user thisMech = userList.stream()
			.filter(x -> x.getAppuser_id().equals(pub.getAppuser_id()))
			.findFirst()
			.get();
		
		// Setea los datos en el modelo extendido
		pub.setStatus(thisStatus);
		pub.setUser(thisMech);

		return pub;
	}
}
