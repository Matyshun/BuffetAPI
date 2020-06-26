package com.CAT.BuffetAPI.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CAT.BuffetAPI.Entities.Product;
import com.CAT.BuffetAPI.Entities.Product_status;
import com.CAT.BuffetAPI.Entities.Service;
import com.CAT.BuffetAPI.Entities.ServiceExt;
import com.CAT.BuffetAPI.Entities.Service_status;
import com.CAT.BuffetAPI.Entities.Unit;
import com.CAT.BuffetAPI.Services.AuthService;
import com.CAT.BuffetAPI.Services.PrestacionesService;

@RestController
@RequestMapping("/supply-adm")
public class PrestacionController {
	@Autowired
	private PrestacionesService pre;

	@Autowired
	private AuthService auth;


	@RequestMapping(value = "/product",method = {RequestMethod.GET})
	private List<Product> getAllProducts(HttpServletResponse res, @RequestHeader("token") String token,
			@RequestParam (required = false) String brand,
			@RequestParam (required = false) String name,
			@RequestParam (required = false) String product_status,
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

			if(brand!= null) 			data.put("brand", brand);
			if(name!=null) 				data.put("name", name);
			if(product_status!=null) 	data.put("product_status", product_status);
			if(deleted != null) 		data.put("deleted", deleted);
			else 						data.put("deleted", false);


			List<Product> productos = pre.getDataProduct(data);

			if(productos == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return productos;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}

	}
	
	@PostMapping(value = "/product")
	public String addProduct(@RequestBody Product product , HttpServletResponse resp) {

		if(auth.ProductValidation(product)) {
			// Setea datos generales
			product.setUpdated_at(new Date());
			product.setCreated_at(new Date());
			
			product = pre.UpdateProducto(product);

			// Status 200 y retorna el Id del APP_USER nuevo
			resp.setStatus(200);
			return product.getProduct_id();
		}
		else
		{
			
			// 409 Conflict
			resp.setStatus(409);
			return "Producto ya existe";
		}
	}


	@RequestMapping(value="/product/{Id}", method = {RequestMethod.GET})
	private Optional<Product> getSpecificProduct(HttpServletResponse res, @PathVariable("Id") String id, @RequestHeader("token") String token)
	{
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
//			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<Product> product = pre.getOneProduct(id);

			// If there is no matching Product
			if(!product.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return product;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}


	@RequestMapping(value= "/product/{Id}", method = {RequestMethod.POST})
	private String UpdateProduct(HttpServletResponse res,@PathVariable String Id, @RequestBody Product product,@RequestHeader("token") String token)
	{
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {
			String productId = product.getProduct_id();
			Optional<Product> optProduct = pre.getOneProduct(productId);

			// If there is no matching Product
			if(!optProduct.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Product oldProduct = optProduct.get();

			product.setCreated_at(oldProduct.getCreated_at());
			product.setUpdated_at(new Date());
			pre.UpdateProducto(product);

			// 200 OK
			res.setStatus(200);
			return "producto actualizado exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}


	@RequestMapping(value = "/product/{Id}/change-status", method = {RequestMethod.POST})
	private String ChangeStatus(HttpServletResponse res, @PathVariable String Id ,@RequestParam("product_status")String product_status,@RequestHeader("token") String token)
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
			res.setStatus(401);
			return null;
		}

		try {
			// Get the Product
			Optional<Product> producto = pre.getOneProduct(Id);

			// If there is no matching Product
			if(!producto.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Product updatedProduct = producto.get();


			updatedProduct.setProduct_status(product_status);
			pre.UpdateProducto(updatedProduct);

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


	@RequestMapping(value= "/product/{Id}", method = {RequestMethod.DELETE})
	private String DeleteProduct(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
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
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<Product> producto = pre.getOneProduct(Id);

			// If there is no matching User
			if(!producto.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Product delProduct = producto.get();

			delProduct.setDeleted(true);
			pre.UpdateProducto(delProduct);

			// 200 OK
			res.setStatus(200);
			return "producto Eliminado Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	@RequestMapping(value= "/product/{Id}/restore", method = {RequestMethod.PUT})
	private String RestoreProduct(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
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
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<Product> user = pre.getOneProduct(Id);

			// If there is no matching User
			if(!user.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Product resProduct = user.get();

			if(!resProduct.isDeleted()){
				// 409 Conflict
				res.setStatus(409);
				return "El Producto no está Eliminado";
			}

			resProduct.setDeleted(false);
			pre.UpdateProducto(resProduct);

			// 200 OK
			res.setStatus(200);
			return "Producto Restaurado Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	//===============================================================================================================================================================================//
	
	@RequestMapping(value = "/service", method = {RequestMethod.GET})
	private List<ServiceExt> getAllServices(HttpServletResponse res,
			@RequestParam (required = false) String name,
			@RequestParam (required = false) String serv_status,
			@RequestParam (required = false) String deleted)
	{
		try {

			// Get the all the Services
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
				res.setStatus(404);
				return null;
			}


			List<Service_status> statusList = pre.getAllservStatus();
			if(statusList == null){
				// 404 Not Found
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
	
	@PostMapping(value = "/service")
	public String addServices(@RequestBody Service service , HttpServletResponse resp) {
		
		if(auth.ServicetValidation(service)) {
			
			service.setUpdated_at(new Date());
			service.setCreated_at(new Date());
			service = pre.UpdateService(service);

			// Status 200 y retorna el Id del APP_USER nuevo
			resp.setStatus(200);
			return service.getServ_id();
		}
		else
		{
			// 409 Conflict
			resp.setStatus(409);
			return "Servicio ya existe";
		}
	}

	@RequestMapping(value="/service/{Id}", method = {RequestMethod.GET})
	private ServiceExt getSpecificService(HttpServletResponse res, @PathVariable("Id") String id)
	{
		if(id.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		try {
			// Get the User
			Optional<Service> serv = pre.getOneService(id);

			// If there is no matching User
			if(!serv.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}


			List<Service_status> statusList = pre.getAllservStatus();
			if(statusList == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// Inicilizar el modelo extendido
			ServiceExt sendServ = new ServiceExt();

			// Copia las propiedades del modelo base a la extensión usando BeanUtils
			BeanUtils.copyProperties(sendServ, serv.get());

			// Procesa la Publicación para agregar la data secundaria
			sendServ = processServ(sendServ, statusList);


			// 200 OK
			res.setStatus(200);
			return sendServ;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	@RequestMapping(value= "/service/{Id}", method = {RequestMethod.POST})
	private String UpdateService(HttpServletResponse res,@PathVariable String Id, @RequestBody Service service,@RequestHeader("token") String token)
	{
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {
			String service_id = service.getServ_id();
			Optional<Service> optService = pre.getOneService(service_id);

			// If there is no matching User
			if(!optService.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Service oldService = optService.get();

			service.setCreated_at(oldService.getCreated_at());
			service.setUpdated_at(new Date());
			pre.UpdateService(service);

			// 200 OK
			res.setStatus(200);
			return "servicio actualizado exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	@RequestMapping(value = "/service/{Id}/change-status", method = {RequestMethod.POST})
	private String ChangeStatusService(HttpServletResponse res, @PathVariable String Id ,@RequestParam("serv_status")String serv_status,@RequestHeader("token") String token)
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
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<Service> servicio = pre.getOneService(Id);

			// If there is no matching Service
			if(!servicio.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Service updatedServicio = servicio.get();


			updatedServicio.setServ_status(serv_status);
			pre.UpdateService(updatedServicio);

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

	@RequestMapping(value= "/service/{Id}", method = {RequestMethod.DELETE})
	private String DeleteService(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
	{
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
			Optional<Service> servicio = pre.getOneService(Id);

			// If there is no matching Service
			if(!servicio.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Service delService = servicio.get();

			delService.setDeleted(true);
			pre.UpdateService(delService);

			// 200 OK
			res.setStatus(200);
			return "servicio Eliminado Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	@RequestMapping(value= "/service/{Id}/restore", method = {RequestMethod.PUT})
	private String RestoreService(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
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
			res.setStatus(401);
			return null;
		}

		try {
			// Get the User
			Optional<Service> user = pre.getOneService(Id);

			// If there is no matching Service
			if(!user.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Service resService = user.get();

			if(!resService.isDeleted()){
				// 409 Conflict
				res.setStatus(409);
				return "El Servicio no está Eliminado";
			}

			resService.setDeleted(false);
			pre.UpdateService(resService);

			// 200 OK
			res.setStatus(200);
			return "Servicio Restaurado Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
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

	@RequestMapping("/serv_status")
	private List<Service_status> getServStatus(HttpServletResponse res,@RequestHeader("token") String token){
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

			if(typeList == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return typeList;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}
	@RequestMapping("/product-status")
	private List<Product_status> getAllProductStatus(HttpServletResponse res,@RequestHeader("token") String token){
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}
		try {
			// Get the all the Product status
			List<Product_status> typeList = pre.getAllProductStatus();

			if(typeList == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return typeList;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}
	@RequestMapping("/units")
	private List<Unit> getAllUnits(HttpServletResponse res,@RequestHeader("token") String token){
		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
//			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}
		try {
			// Get the all the Units
			List<Unit> typeList = pre.getAllUnits();

			if(typeList == null){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return typeList;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}
	
	//@Scheduled(fixedRate = 300000)
	@RequestMapping("/product/low-stock")
	private List<Product> getLowStockProducts(HttpServletResponse res, @RequestHeader("token") String token)
	{
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
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			for(Product p : productos)
			{
				if(p.getStock_alert()<= p.getStock())
				{
					lowStock.add(p);
				}
			}
			// 200 OK
			res.setStatus(200);
			return lowStock;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}

	}

}

//Changes 22-05-2020
//Changed lowStock to return based on stock_alert instead of raw parameters.
//Minor grammatical errors