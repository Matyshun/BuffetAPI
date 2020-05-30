package com.CAT.BuffetAPI.Controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.CAT.BuffetAPI.Entities.App_user;
import com.fasterxml.jackson.annotation.JsonFormat;

@RestController
@RequestMapping("/test")
public class TestController {
	
	
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	@RequestMapping(value="test1", method = {RequestMethod.GET})
	private int Test(@RequestBody List<App_user> usuarios)
		
	{
		return usuarios.size();
	}



	
}
