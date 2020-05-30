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
import com.CAT.BuffetAPI.Entities.Sale;
import com.CAT.BuffetAPI.Entities.Sale_provision;
import com.CAT.BuffetAPI.Entities.Sale_status;
import com.CAT.BuffetAPI.Entities.User_type;
import com.CAT.BuffetAPI.Repositories.userTypeRepository;
import com.CAT.BuffetAPI.Services.App_UserService;
import com.CAT.BuffetAPI.Services.AuthService;
import com.CAT.BuffetAPI.Services.SaleService;
import com.fasterxml.jackson.annotation.JsonFormat;

@RestController
@RequestMapping("/sale")
public class SaleController {
	@Autowired
	SaleService saleServ;

	@Autowired
	AuthService auth;

	@RequestMapping("/sales")
	private List<Sale> getAllsales(HttpServletResponse res, @RequestHeader("token") String token,
			@RequestParam (required = false) String appuser_id,
			@RequestParam (required = false) String cashier_id,
			@RequestParam (required = false) String seller_id,
			@RequestParam (required = false) String sale_status_id,
			@RequestParam (required = false) String deleted)
	{

		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		//typesAllowed.add("VEN");
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
			if(cashier_id!=null)
			{
				data.put("cashier_id", cashier_id);
			}
			if(seller_id!=null)
			{
				data.put("seller_id", seller_id);
			}
			if(sale_status_id!= null)
			{
				data.put("sale_status_id", sale_status_id);
			}
			if(deleted != null)		data.put("deleted", deleted);
			else					data.put("deleted", false);   

			List<Sale> thesales = saleServ.getAllSale(data);

			if(thesales == null)
			{
				//404 not found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return thesales;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}

	}

	@RequestMapping(value = "/sales", method = {RequestMethod.POST})
	public String addProduct(@RequestBody Sale sale , HttpServletResponse resp, @RequestHeader("token") String token) {

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
			// 401 Unauthorized
			resp.setStatus(401);
			return null;
		}
		try {
			if(auth.SaleValidation(sale))
			{
				// Setea datos generales
				sale.setUpdated_at(new Date());
				sale.setCreated_at(new Date());



				saleServ.updateSale(sale);

				// Status 200 y retorna el Id del APP_USER nuevo
				resp.setStatus(200);
				return "venta agregada correctamente";
			}
			else
			{
				// 409 Conflict
				resp.setStatus(409);
				return "Producto ya existe";
			}
		}
		catch(Exception e)
		{
			resp.setStatus(500);		
			return "Error interno";
		}

	}

	@RequestMapping(value="/sales/{Id}", method = {RequestMethod.GET})
	private Optional<Sale> getSpecificSale(HttpServletResponse res, @PathVariable("Id") String id, @RequestHeader("token") String token)
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
			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {
			// Get the Sale
			Optional<Sale> sale = saleServ.getOneSale(id);

			// If there is no matching Sale
			if(!sale.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return sale;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}


	@RequestMapping(value= "/sales/{Id}", method = {RequestMethod.POST})
	private String UpdateSale(HttpServletResponse res,@PathVariable String Id, @RequestBody Sale reqSale,@RequestHeader("token") String token)
	{
		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		//typesAllowed.add("ADM");
		typesAllowed.add("VEN");
		typesAllowed.add("CAJ");
		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {
			String saleId = reqSale.getSale_id();
			Optional<Sale> optSale = saleServ.getOneSale(saleId);

			// If there is no matching User
			if(!optSale.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			reqSale.setSale_id(optSale.get().getSale_id());
			reqSale.setCreated_at(optSale.get().getCreated_at());
			reqSale.setUpdated_at(new Date());
			saleServ.updateSale(reqSale);

			// 200 OK
			res.setStatus(200);
			return "Compra actualizada exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}


	@RequestMapping(value = "/sales/{Id}/change-status", method = {RequestMethod.POST})
	private String ChangeStatus(HttpServletResponse res, @PathVariable String Id ,@RequestParam("status")String status,@RequestHeader("token") String token)
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
			// Get the Sale
			Optional<Sale> sale = saleServ.getOneSale(Id);

			// If there is no matching User
			if(!sale.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}
			List<Sale_status> theStatus = saleServ.getAllStatus();
			Sale updateSale = sale.get();

			updateSale.setSale_status_id(status);
			saleServ.updateSale(updateSale);

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


	@RequestMapping(value= "/sales/{Id}", method = {RequestMethod.DELETE})
	private String DeleteSale(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
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
			// Get the Sale
			Optional<Sale> sale = saleServ.getOneSale(Id);

			// If there is no matching Sale
			if(!sale.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Sale delSale = sale.get();

			List<Sale_provision> provisiones = saleServ.getAllProvision();

			for(Sale_provision s : provisiones)
			{
				if(s.getSale_id().equals(delSale.getSale_id()))
				{
					s.setDeleted(true);
					saleServ.deleteSaleProvision(s);
				}
			}

			delSale.setDeleted(true);
			saleServ.updateSale(delSale);

			// 200 OK
			res.setStatus(200);
			return "Compra Eliminada Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}

	@RequestMapping(value= "/sales/{Id}/restore", method = {RequestMethod.PUT})
	private String RestoreMec(HttpServletResponse res,@PathVariable String Id,@RequestHeader("token") String token)
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
			Optional<Sale> sale = saleServ.getOneSale(Id);

			// If there is no matching Sale
			if(!sale.isPresent()){
				// 404 Not Found
				res.setStatus(404);
				return null;
			}

			Sale resSale = sale.get();

			if(!resSale.isDeleted()){
				// 409 Conflict
				res.setStatus(409);
				return "La venta no está Eliminada";
			}

			List<Sale_provision> provisiones = saleServ.getAllProvision();

			for(Sale_provision s : provisiones)
			{
				if(s.getSale_id().equals(Id))
				{
					s.setDeleted(false);
					saleServ.deleteSaleProvision(s);
				}
			}

			resSale.setDeleted(false);
			saleServ.updateSale(resSale);

			// 200 OK
			res.setStatus(200);
			return "Venta Restaurada Exitosamente";

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}
	}


	@RequestMapping("/provisions/{Id}")//Id de la venta
	private List<Sale_provision> getAllsalesProvisions(HttpServletResponse res, @RequestHeader("token") String token,@PathVariable String Id)
	{

		if(token.isEmpty()){
			// 400 Bad Request
			res.setStatus(400);
			return null;
		}

		List<String> typesAllowed = new ArrayList<String>();
		typesAllowed.add("ADM");
		typesAllowed.add("CAJ");
		typesAllowed.add("VEN");

		if(!auth.Authorize(token, typesAllowed)){
			// 401 Unauthorized
			res.setStatus(401);
			return null;
		}

		try {

			List<Sale_provision> theProvisions = saleServ.getAllProvision();
			List<Sale_provision> theProvisionFiltered = new ArrayList<Sale_provision>();

			for(Sale_provision s : theProvisions)
			{
				if(s.getSale_id().equals(Id))
					theProvisionFiltered.add(s);
			}

			if(theProvisions == null)
			{
				//404 not found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return theProvisionFiltered;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}

	}

	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@RequestMapping(value="/provisions", method = {RequestMethod.PUT})
	private String AddProvisions(HttpServletResponse res,@RequestBody List<Sale_provision> provisiones)
	{
		try
		{
			for(Sale_provision s : provisiones)
			{
				if(saleServ.getAllProvision().contains(s)){
					res.setStatus(409); //409 conflict
					return "Provision ya existe con codigo "+ s.getProduct_id();
				}

			}

			for(Sale_provision s : provisiones)
			{
				saleServ.updateSaleProvision(s);
			}
			res.setStatus(200); //OK
			return "Todas las provisiones añadidas correctamente";

		}
		catch(Exception e)
		{
			res.setStatus(500); //500 server error
			return "Error interno";
		}
	}

	@RequestMapping(value = "/sale_status", method = {RequestMethod.GET})
	private List<Sale_status> getAllStatus(HttpServletResponse res, @RequestHeader("token") String token)
	{
		try {
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

			List<Sale_status> theStatus = saleServ.getAllStatus();

			if(theStatus == null)
			{
				//404 not found
				res.setStatus(404);
				return null;
			}

			// 200 OK
			res.setStatus(200);
			return theStatus;

		} catch (Exception e) {
			// If There was an error connecting to the server
			// 500 Internal Server Error
			res.setStatus(500);
			return null;
		}

	}

}
