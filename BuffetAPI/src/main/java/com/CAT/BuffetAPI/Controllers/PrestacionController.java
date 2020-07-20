package com.CAT.BuffetAPI.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CAT.BuffetAPI.Entities.App_user;
import com.CAT.BuffetAPI.Entities.Product;
import com.CAT.BuffetAPI.Entities.Product_status;
import com.CAT.BuffetAPI.Entities.Service;
import com.CAT.BuffetAPI.Entities.ServiceExt;
import com.CAT.BuffetAPI.Entities.Service_status;
import com.CAT.BuffetAPI.Entities.Unit;
import com.CAT.BuffetAPI.Services.App_UserService;
import com.CAT.BuffetAPI.Services.AuthService;
import com.CAT.BuffetAPI.Services.EmailSenderService;
import com.CAT.BuffetAPI.Services.PrestacionesService;

//Controlador que se encarga de los productos y servicios

@RestController
@RequestMapping("/supply-adm")
public class PrestacionController {
	@Autowired
	private PrestacionesService pre;
	@Autowired
	private App_UserService app;
	@Autowired
	private AuthService auth;
	@Autowired
	private EmailSenderService mailSender;

	private void log(String msg) {
		System.out.println(msg);
	}
	private void logLine() {
		System.out.println("----------------------------------------------------------------");
	}
	
	//lista de productos
	@RequestMapping(value = "/product",method = {RequestMethod.GET})
	private List<Product> getAllProducts(HttpServletResponse res, @RequestHeader("token") String token,
			@RequestParam (required = false) String brand,
			@RequestParam (required = false) String name,
			@RequestParam (required = false) String product_status,
			@RequestParam (required = false) String deleted)
	{

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
			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {

			// Get the all the Users
			HashMap<String,Object> data = new HashMap<>();

			if(brand!= null) 			data.put("brand", brand);
			if(name!=null) 				data.put("name", name);
			if(product_status!=null) 	data.put("product_status", product_status);
			if(deleted != null) 		data.put("deleted", deleted);
			else 						data.put("deleted", false);


			List<Product> productos = pre.getDataProduct(data);

			if(productos == null){
				log("lista de productos vacia");
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			log("OK");
			// 200 OK
			res.setStatus(200);
			return productos;

		} catch (Exception e) {
			log(e.toString());

			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}

	}
	
	//Añadir producto
	@PostMapping(value = "/product")
	public String addProduct(@RequestBody Product product , HttpServletResponse resp) {

		logLine();
		log("añadiendo producto");
		if(auth.ProductValidation(product)) {
			log("Pasa validacion");
			// Setea datos generales
			product.setUpdated_at(new Date());
			product.setCreated_at(new Date());

			product = pre.UpdateProducto(product);
			log("OK");
			// Status 200 y retorna el Id del APP_USER nuevo
			resp.setStatus(200);
			return product.getProduct_id();
		}
		else
		{
			log("producto ya existe");
			// 409 Conflict
			resp.setStatus(409);
			return "Producto ya existe";
		}
	}

	//Regresar producto especifico (ID)
	@RequestMapping(value="/product/{Id}", method = {RequestMethod.GET})
	private Optional<Product> getSpecificProduct(HttpServletResponse res, @PathVariable("Id") String id, @RequestHeader("token") String token)
	{
		logLine();
		log("Recuperando producto especifico");
		if(id.isEmpty() || token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		// Check for authorization
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
			log("no autorizado");
			//			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<Product> product = pre.getOneProduct(id);

			// If there is no matching Product
			if(!product.isPresent()){
				log("producto no existe");
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			log("OK");
			// 200 OK
			res.setStatus(200);
			return product;

		} catch (Exception e) {
			log(e.toString());

			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	//Modificar producto segun id
	@RequestMapping(value= "/product/{Id}", method = {RequestMethod.POST})
	private String UpdateProduct(HttpServletResponse res,@PathVariable String Id, @RequestBody Product product,@RequestHeader("token") String token)
	{
		logLine();		
		log("Modificando producto");
		if(token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
			//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			String productId = product.getProduct_id();
			Optional<Product> optProduct = pre.getOneProduct(productId);

			// If there is no matching Product
			if(!optProduct.isPresent()){
				// 404 Not Found
				log("producto no existe");
				res.setStatus(404);
				return null;
			}

			Product oldProduct = optProduct.get();

			product.setCreated_at(oldProduct.getCreated_at());
			product.setUpdated_at(new Date());
			pre.UpdateProducto(product);
			log("OK");
			// 200 OK
			res.setStatus(200);
			return "producto actualizado exitosamente";

		} catch (Exception e) {
			log(e.toString());
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	//Cambiar estado producto
	@RequestMapping(value = "/product/{Id}/change-status", method = {RequestMethod.POST})
	private String ChangeStatus(HttpServletResponse res, @PathVariable String Id ,@RequestParam("product_status")String product_status,@RequestHeader("token") String token)
	{
		logLine();
		log("Cambiando estado de un producto");
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
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			// Get the Product
			Optional<Product> producto = pre.getOneProduct(Id);

			// If there is no matching Product
			if(!producto.isPresent()){
				// 404 Not Found
				log("producto no existe");
				res.setStatus(404);
				return null;
			}

			Product updatedProduct = producto.get();


			updatedProduct.setProduct_status(product_status);
			pre.UpdateProducto(updatedProduct);
			log("OK");
			// 200 OK
			res.setStatus(200);
			return "Status actualizado exitosamente";

		} catch (Exception e) {
			log(e.toString());
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	//Eliminar producto
	@RequestMapping(value= "/product/{Id}", method = {RequestMethod.DELETE})
	private String DeleteProduct(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
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
			Optional<Product> producto = pre.getOneProduct(Id);

			// If there is no matching User
			if(!producto.isPresent()){
				// 404 Not Found
				log("producto no existe");
				res.setStatus(404);
				return null;
			}

			Product delProduct = producto.get();

			delProduct.setDeleted(true);
			pre.UpdateProducto(delProduct);
			log("OK");
			// 200 OK
			res.setStatus(200);
			return "producto Eliminado Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log("OK");
			return null;
		}
	}

	//Restaurar producto
	@RequestMapping(value= "/product/{Id}/restore", method = {RequestMethod.PUT})
	private String RestoreProduct(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
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
			Optional<Product> user = pre.getOneProduct(Id);

			// If there is no matching User
			if(!user.isPresent()){
				// 404 Not Found
				log("producto no existe");
				res.setStatus(404);
				return null;
			}

			Product resProduct = user.get();

			if(!resProduct.isDeleted()){
				// 409 Conflict
				log("producto no esta eliminado");
				res.setStatus(409);
				return "El Producto no está Eliminado";
			}

			resProduct.setDeleted(false);
			pre.UpdateProducto(resProduct);

			// 200 OK
			log("OK");
			res.setStatus(200);
			return "Producto Restaurado Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}
	}

	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	
	//Desde este punto se encarga de los servicios
	
	//Lista de todos los servicios
	@RequestMapping(value = "/service", method = {RequestMethod.GET})
	private List<ServiceExt> getAllServices(HttpServletResponse res,
			@RequestParam (required = false) String name,
			@RequestParam (required = false) String serv_status,
			@RequestParam (required = false) String deleted)
	{
		
		try {
			logLine();
			log("Pidiendo lista de servicios");
			// Get the all the Services
			//Filtros
			HashMap<String,Object> data = new HashMap<>();

			if(name!=null)
				data.put("name", name);
			if(serv_status!=null)
				data.put("serv_status", serv_status);
			if(deleted != null)
				data.put("deleted", deleted);
			else
				data.put("deleted", false);

			List<Service> servList = pre.getDataService(data);
			if(servList == null){
				// 404 Not Found
				log("lista de servicios vacia");
				res.setStatus(404);
				return null;
			}


			List<Service_status> statusList = pre.getAllservStatus();
			if(statusList == null){
				// 404 Not Found
				log("servicio no existe");
				res.setStatus(404);
				return null;
			}

			// ArrayList porque inicializar una List de la nada en Java es terrible
			ArrayList<ServiceExt> sendPubList = new ArrayList<ServiceExt>();

			// Procesar para extender le modelo
			for (Service serv : servList) {
				// Inicilizar el modelo extendido
				ServiceExt sendServ = new ServiceExt();

				// Copia las propiedades del modelo base a la extensión usando BeanUtils
				BeanUtils.copyProperties(sendServ, serv);

				// Procesa la Publicación para agregar la data secundaria
				sendServ = processServ(sendServ, statusList);

				// Agrega el modelo extendido a la lista que se enviará
				sendPubList.add(sendServ);
			}

			log("OK");
			// 200 OK
			res.setStatus(200);
			return sendPubList;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}

	}

	//Añadir servicio
	@PostMapping(value = "/service")
	public String addServices(@RequestBody Service service , HttpServletResponse resp) {
		logLine();		
		log("añadiendo servicio");
		if(auth.ServicetValidation(service)) {
			log("paso validacion");
			service.setUpdated_at(new Date());
			service.setCreated_at(new Date());
			service = pre.UpdateService(service);
			log("OK");
			// Status 200 y retorna el Id del APP_USER nuevo
			resp.setStatus(200);
			return service.getServ_id();
		}
		else
		{
			// 409 Conflict
			log("servicio ya existe");
			resp.setStatus(409);
			return "Servicio ya existe";
		}
	}

	//Datos de servicio especifico
	@RequestMapping(value="/service/{Id}", method = {RequestMethod.GET})
	private ServiceExt getSpecificService(HttpServletResponse res, @PathVariable("Id") String id)
	{
		logLine();		
		log("recuperando servicio especifico");
		if(id.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		try {
			// Get the User
			Optional<Service> serv = pre.getOneService(id);

			// If there is no matching Service
			if(!serv.isPresent()){
				// 404 Not Found
				log("servicio no existe");
				res.setStatus(404);
				return null;
			}


			List<Service_status> statusList = pre.getAllservStatus();
			if(statusList == null){
				// 404 Not Found
				log("lista de estados vacia");
				res.setStatus(404);
				return null;
			}

			// Inicilizar el modelo extendido
			ServiceExt sendServ = new ServiceExt();

			// Copia las propiedades del modelo base a la extensión usando BeanUtils
			BeanUtils.copyProperties(sendServ, serv.get());

			// Procesa la Publicación para agregar la data secundaria
			sendServ = processServ(sendServ, statusList);

			log("OK");
			// 200 OK
			res.setStatus(200);
			return sendServ;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}
	}

	//Modificar servicio especifico
	@RequestMapping(value= "/service/{Id}", method = {RequestMethod.POST})
	private String UpdateService(HttpServletResponse res,@PathVariable String Id, @RequestBody Service service,@RequestHeader("token") String token)
	{
		logLine();		
		log("modificando servicio");
		if(token.isEmpty()){
			log("token vacio");
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			String service_id = service.getServ_id();
			Optional<Service> optService = pre.getOneService(service_id);

			// If there is no matching User
			if(!optService.isPresent()){
				// 404 Not Found
				log("servicio no existe");
				res.setStatus(404);
				return null;
			}

			Service oldService = optService.get();

			service.setCreated_at(oldService.getCreated_at());
			service.setUpdated_at(new Date());
			pre.UpdateService(service);

			// 200 OK
			log("OK");
			res.setStatus(200);
			return "servicio actualizado exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}

	//Cambiar estado de un servicio
	@RequestMapping(value = "/service/{Id}/change-status", method = {RequestMethod.POST})
	private String ChangeStatusService(HttpServletResponse res, @PathVariable String Id ,@RequestParam("serv_status")String serv_status,@RequestHeader("token") String token)
	{
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
			// Get the Service
			Optional<Service> servicio = pre.getOneService(Id);

			// If there is no matching Service
			if(!servicio.isPresent()){
				// 404 Not Found
				log("servicio no existe");
				res.setStatus(404);
				return null;
			}

			Service updatedServicio = servicio.get();


			updatedServicio.setServ_status(serv_status);
			pre.UpdateService(updatedServicio);
			log("OK");
			// 200 OK
			res.setStatus(200);
			return "Status actualizado exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}
	
	//Eliminar un servicio
	@RequestMapping(value= "/service/{Id}", method = {RequestMethod.DELETE})
	private String DeleteService(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
		logLine();		
		log("eliminando servicio");
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<Service> servicio = pre.getOneService(Id);

			// If there is no matching Service
			if(!servicio.isPresent()){
				// 404 Not Found
				log("servicio no existe");
				res.setStatus(404);
				return null;
			}

			Service delService = servicio.get();

			delService.setDeleted(true);
			pre.UpdateService(delService);

			// 200 OK
			log("OK");
			res.setStatus(200);
			return "servicio Eliminado Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		
		}
	}

	//Restaurar un servicio
	@RequestMapping(value= "/service/{Id}/restore", method = {RequestMethod.PUT})
	private String RestoreService(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
		logLine();		
		log("restaurando servicio");
		if(token.isEmpty()){
			// 400 Bad Request
			log("token vacio");
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {
			// Get the Service
			Optional<Service> service = pre.getOneService(Id);

			// If there is no matching Service
			if(!service.isPresent()){
				// 404 Not Found
				log("servicio no existe");
				res.setStatus(404);
				return null;
			}

			Service resService = service.get();

			if(!resService.isDeleted()){
				// 409 Conflict
				res.setStatus(409);
				log("servicio no esta eliminado");
				return "El Servicio no está Eliminado";
			}

			resService.setDeleted(false);
			pre.UpdateService(resService);
			log("OK");
			// 200 OK
			res.setStatus(200);
			return "Servicio Restaurado Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}
	}

	// Agrega la información secundaria del modelo extendido
	private ServiceExt processServ(ServiceExt serv, List<Service_status> statusList){


		// Consigue el Status de esta Publicación
		Service_status thisStatus = statusList.stream()
				.filter(item -> item.getStatus_id().equals(serv.getServ_status()))
				.findFirst()
				.get();

		serv.setStatus(thisStatus);

		return serv;
	}

	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//

	//De aqui en adelante se retornan los listados de estatus y otros
	
	//listado de estado de servicios
	@RequestMapping("/serv_status")
	private List<Service_status> getServStatus(HttpServletResponse res,@RequestHeader("token") String token){
		logLine();		
		log("Enviando todos los estados de servicio");
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		//		if(!auth.Authorize(token, typesAllowed)){
		////			// 401 Unauthorized
		//			res.setStatus(401);
		//			return null;
		//		}
		try {
			// Get the all the Service status
			List<Service_status> typeList = pre.getAllservStatus();

			if(typeList == null)
			{
				log("listado de servicios vacio");
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			log("OK");
			// 200 OK
			res.setStatus(200);
			return typeList;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}
	}
	
	//listado de estatus de producto
	@RequestMapping("/product-status")
	private List<Product_status> getAllProductStatus(HttpServletResponse res,@RequestHeader("token") String token){
		logLine();		
		log("enviando listado de productos");
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
			log("no autorizado");
			//			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}
		try {
			// Get the all the Product status
			List<Product_status> typeList = pre.getAllProductStatus();

			if(typeList == null){
				log("listado de productos esta vacio");
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			log("OK");
			// 200 OK
			res.setStatus(200);
			return typeList;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			log(e.toString());
			return null;
		}
	}
	
	//listado de unidades de medida
	@RequestMapping("/units")
	private List<Unit> getAllUnits(HttpServletResponse res,@RequestHeader("token") String token){
		logLine();		
		log("enviando todas las unidades de medida");
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
			List<Unit> typeList = pre.getAllUnits();

			if(typeList == null){
				log("listado de unidades de medida vacio");
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			log("OK");
			// 200 OK
			res.setStatus(200);
			return typeList;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}
	}

	//Envia todos los productos con bajo stock
	@RequestMapping("/product/low-stock")
	private List<Product> getLowStockProducts(HttpServletResponse res, @RequestHeader("token") String token)
	{
		logLine();		
		log("enviando todos los productos con bajo stock ");
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		if(!auth.Authorize(token, typesAllowed)){
			//			// 401 Unauthorized
			log("no autorizado");
			res.setStatus(401);
			return null;
		}

		try {

			// Get the all the products with low stock
			HashMap<String,Object> data = new HashMap<>();
			data.put("deleted", false);
			List<Product> productos = pre.getDataProduct(data);
			List<Product> lowStock = new ArrayList<Product>();
			if(productos == null){
				log("listado de productos vacio");
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			for(Product p : productos)
			{
				if(p.getStock_alert()>= p.getStock())
				{
					lowStock.add(p);
				}
			}
			log("OK");
			// 200 OK
			res.setStatus(200);
			return lowStock;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			log(e.toString());
			res.setStatus(500);
			return null;
		}

	}



	//Auto sender low stock
	//Anotacion de cron indica cuando debe mandarse, en este caso a las 10 de la mañana de todos los jueves
	@Scheduled(cron = "0 0 10 * * 4")
	private void getLowStockProductsAuto()
	{

		try {
			
			// Get the all the products with low stock
			HashMap<String,Object> data = new HashMap<>();
			data.put("deleted", false);
			List<Product> productos = pre.getDataProduct(data);
			if(productos == null){
				// 404 Not Found
			}
			
			//Se crea una linea de texto donde se van añadiendo los productos para luego enviarlos.
			String text = "";

			for(Product p : productos)
			{
				if(p.getStock() <= p.getStock_alert())
				{
					text = text + p.getName() + "   "+ p.getStock() + p.getUnit_id() +"\n";
				}
			}
			
			//Se crea una lista con todos los supervisores.
			List<App_user> all = app.getAllUsers();
			List<App_user> sup = new ArrayList<App_user>();
			all.forEach(x->{
				if(x.getUser_type_id().equals("SUP"))
				{

					sup.add(x);
				}
			});


			//Por cada supervisor se envia un email
			for(App_user u : sup)
			{
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				Date today = new Date();
				mailMessage.setTo(u.getEmail());
				mailMessage.setSubject("Bajo Stock");
				mailMessage.setFrom("userconfimationjaramillo@gmail.com");
				mailMessage.setText("Este mensaje ha sido creado automaticamente el dia "
						+ today + " Para informar de los productos con bajo stock, a continuación se detallaran los productos: \n\n" + text);

				mailSender.sendEmail(mailMessage);

			}

		} catch (Exception e) {
			System.out.println("Error: "+ e);
		}

	}

}