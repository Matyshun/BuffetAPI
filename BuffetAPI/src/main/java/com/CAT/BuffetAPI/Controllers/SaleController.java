package com.CAT.BuffetAPI.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.CAT.BuffetAPI.Entities.Product;
import com.CAT.BuffetAPI.Entities.ProductReport;
import com.CAT.BuffetAPI.Entities.Sale;
import com.CAT.BuffetAPI.Entities.SaleExt;
import com.CAT.BuffetAPI.Entities.SaleReport;
import com.CAT.BuffetAPI.Entities.Sale_provision;
import com.CAT.BuffetAPI.Entities.Sale_status;
import com.CAT.BuffetAPI.Entities.Service;
import com.CAT.BuffetAPI.Entities.ServiceReport;
import com.CAT.BuffetAPI.Entities.User_type;
import com.CAT.BuffetAPI.Repositories.ProductRepository;
import com.CAT.BuffetAPI.Repositories.SaleRepository;
import com.CAT.BuffetAPI.Repositories.Sale_provisionRepository;
import com.CAT.BuffetAPI.Repositories.Sale_statusRepository;
import com.CAT.BuffetAPI.Repositories.ServiceRepository;
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
	private SaleRepository sale;
	@Autowired
	private Sale_provisionRepository prov;
	@Autowired
	private Sale_statusRepository stat;
	@Autowired
	private ProductRepository prod;
	@Autowired
	private ServiceRepository serv;
	@Autowired
	private App_UserService app;
	@Autowired
	AuthService auth;

	@RequestMapping("/sales")
	private List<Sale> getAllsales(HttpServletResponse res, @RequestHeader("token") String token,
			@RequestParam (required = false) String appuser_id,
			@RequestParam (required = false) String cashier_id,
			@RequestParam (required = false) String seller_id,
			@RequestParam (required = false) String sale_status_id,
			@RequestParam (required = false) String code,
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
//			// 401 Unauthorized
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
			if(!code.isEmpty())
			{
				data.put("code", code);
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
			System.out.println(e);
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
//			// 401 Unauthorized
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
//			// 401 Unauthorized
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
//			// 401 Unauthorized
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
//			// 401 Unauthorized
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
//			// 401 Unauthorized
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
					saleServ.updateSaleProvision(s);
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
//			// 401 Unauthorized
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
					saleServ.updateSaleProvision(s);
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
			typesAllowed.add("CAJ");
			if(!auth.Authorize(token, typesAllowed)){
				// 401 Unauthorized
				
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
	
	//Reporte de ventas
	
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	@RequestMapping(value = "sale_report", method = {RequestMethod.GET} )
	private SaleReport salesReport(HttpServletResponse res,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha_inicio,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha_final)
	{
		try {
			List<Sale> allSales = new ArrayList<Sale>();
			List<Sale_provision> provisiones = new ArrayList<Sale_provision>();
			SaleReport report = new SaleReport();
			report.setDate_start(fecha_inicio);
			report.setDate_end(fecha_final);
			
			sale.findAll().forEach(s -> {
				if(s.getSale_date().compareTo(fecha_final)<=0 && s.getSale_date().compareTo(fecha_inicio)>=0 && s.getSale_status_id().equals("PAG"))
				{
					allSales.add(s);
				}
			});

			if(allSales.isEmpty())
			{
				res.setStatus(404);
				return null;
			}
			
			for(Sale aSale : allSales)
			{
				report.setTotal(report.getTotal()+aSale.getTotal());
				SaleExt theSale = new SaleExt();
				BeanUtils.copyProperties(theSale, aSale);
				report.sale_list.add(theSale);

				App_user thisCashier = app.getAllUsers().stream()
						.filter(x -> x.getAppuser_id().equals(aSale.getCashier_id()))
						.findFirst()
						.get();

				App_user thisUser = app.getAllUsers().stream()
						.filter(x -> x.getAppuser_id().equals(aSale.getAppuser_id()))
						.findFirst()
						.get();

				App_user thisSeller = app.getAllUsers().stream()
						.filter(x -> x.getAppuser_id().equals(aSale.getSeller_id()))
						.findFirst()
						.get();

				Sale_status thisStatus = stat.findAll().stream()
						.filter(x -> x.getSale_status_id().equals(aSale.getSale_status_id()))
						.findFirst()
						.get();

				
				theSale.setSeller(thisSeller);
				theSale.setUser(thisUser);
				theSale.setStatus(thisStatus);
				theSale.setCashier(thisCashier);
				
				prov.findAll().forEach(p ->{
					if(p.getSale_id().equals(aSale.getSale_id())){
						provisiones.add(p);
					}
				});
				
				for(Sale_provision p : provisiones) {
					if(p.getProduct_id() != null && p.getServ_id() == null)
					{
						int index = -1;
						Product product = new Product();
						ProductReport prodRep = new ProductReport();
						product = prod.getOne(p.getProduct_id());
						BeanUtils.copyProperties(prodRep, product);
						prodRep.setProd_n(prodRep.getProd_n() + p.getQuantity());
						prodRep.setProd_total(prodRep.getProd_total()+p.getTotal());
						if (!report.prod_sold_list.isEmpty()) {
							for (Product aux : report.prod_sold_list) {
								

								if (aux.getProduct_id().equals(prodRep.getProduct_id())) {
									index = report.prod_sold_list.indexOf(aux);
									break;
								}
							} 
						}
						if(index == -1)
						{
							report.prod_sold_list.add(prodRep);
						}
						else
						{
							report.prod_sold_list.get(index).setProd_n(report.prod_sold_list.get(index).getProd_n()+prodRep.getProd_n());
							report.prod_sold_list.get(index).setProd_total(report.prod_sold_list.get(index).getProd_total()+prodRep.getProd_total());
						}
						
					}
					else
					{
						Service servicio = new Service();
						ServiceReport servRep = new ServiceReport();
						servicio = serv.getOne(p.getServ_id());
						BeanUtils.copyProperties(servRep, servicio);
						servRep.setServ_n(servRep.getServ_n()+p.getQuantity());
						servRep.setServ_total(servRep.getServ_total()+p.getTotal());

						int index = -1;
						if (!report.prod_sold_list.isEmpty()) {
							for (Service aux : report.serv_sold_list) {
								

								if (aux.getServ_id().equals(servRep.getServ_id())) {
									index  = report.serv_sold_list.indexOf(aux);
									break;
								}
							} 
						}
						if(index == -1)
						{
							report.serv_sold_list.add(servRep);
						}
						else
						{
							report.serv_sold_list.get(index).setServ_n(report.serv_sold_list.get(index).getServ_n()+servRep.getServ_n());
							report.serv_sold_list.get(index).setServ_total(report.serv_sold_list.get(index).getServ_total()+servRep.getServ_total());
						}
						
					}
				}
			}
			return report;
		} catch (Exception e)
		{
			res.setStatus(500);
			System.out.println(e);
			return null;

		}
	}

}
