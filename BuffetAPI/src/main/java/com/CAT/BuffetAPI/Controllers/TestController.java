package com.CAT.BuffetAPI.Controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.CAT.BuffetAPI.Repositories.App_UserRepository;
import com.CAT.BuffetAPI.Repositories.ProductRepository;
import com.CAT.BuffetAPI.Repositories.SaleRepository;
import com.CAT.BuffetAPI.Repositories.Sale_provisionRepository;
import com.CAT.BuffetAPI.Repositories.Sale_statusRepository;
import com.CAT.BuffetAPI.Repositories.ServiceRepository;
import com.CAT.BuffetAPI.Services.App_UserService;
import com.CAT.BuffetAPI.Services.SaleService;
import com.fasterxml.jackson.annotation.JsonFormat;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private App_UserService app;

	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@RequestMapping(value="test1", method = {RequestMethod.GET})
	private int Test(@RequestBody List<App_user> usuarios)

	{
		return usuarios.size();
	}

	@RequestMapping(value = "test2", method = {RequestMethod.GET})
	private HashMap<String, Object> getHashMap()
	{
		testClass test = new testClass();
		List<String> palabras = new ArrayList<String>();
		List<App_user> usuarios = app.getAllUsers();
		palabras.add("hola");
		palabras.add("chao");
		test.uno.put("uno", 1);
		test.uno.put("dos", palabras);
		test.uno.put("usuarios", usuarios);
		test.dos.put("dos",2);

		return test.uno;
	}



	public class testClass
	{
		HashMap<String, Object> uno = new HashMap<String, Object>();
		HashMap<String, Object> dos = new HashMap<String, Object>();
	}

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

	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	@RequestMapping(value = "report_test", method = {RequestMethod.GET} )
	private SaleReport salesReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha_inicio,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha_final)
	{
		try {
			List<Sale> allSales = new ArrayList<Sale>();
			List<Sale_provision> provisiones = new ArrayList<Sale_provision>();
			SaleReport report = new SaleReport();
			report.setDate_start(fecha_inicio);
			report.setDate_end(fecha_final);
			sale.findAll().forEach(s -> {
				if(s.getSale_date().compareTo(fecha_final)<=0 && s.getSale_date().compareTo(fecha_inicio)>=0 )
				{
					allSales.add(s);
				}
			});

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
			System.out.println(e);
			return null;

		}
	}

}