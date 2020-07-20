package com.CAT.BuffetAPI.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.CAT.BuffetAPI.Entities.App_user;
import com.CAT.BuffetAPI.Entities.Booking;
import com.CAT.BuffetAPI.Entities.Booking_restriction;
import com.CAT.BuffetAPI.Entities.Product;
import com.CAT.BuffetAPI.Entities.Publication;
import com.CAT.BuffetAPI.Entities.Sale;
import com.CAT.BuffetAPI.Repositories.App_UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;




@Service
public class AuthService {


	@Autowired
	private App_UserRepository appUserRepository;
	@Autowired
	private App_UserService appService;
	@Autowired
	private PublicationService pubService;
	@Autowired
	private PrestacionesService presService;
	@Autowired
	private SaleService salesServ;
	@Autowired
	private BookingService resService;

	@Value ("${secretKey}")
	private String SecretKey;

	@Value ("${secretKeyPassword}")
	private String KeyForRecovery;

	@Autowired
	private EmailSenderService mailSender;

	public Claims getClaims(String jwt) {
		Claims claims = Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary(SecretKey))
				.parseClaimsJws(jwt).getBody();
		return claims;
	}

	public String extractId(String token){
		Claims claims = getClaims(token);
		String id = claims.get("userId", String.class);

		return id;
	}

	public boolean Authorize(String token,List<String> typesAllowed)
	{
		try {
			Claims claims = getClaims(token);
			return true;
//			if(typesAllowed.contains(claims.get("Usertype",String.class)))
//			{
//				return true;
//			}
//			else
//			{	
//				return false;
//			}
		}
		catch(Exception e)
		{
			return false;
		}


	}

	public boolean Validate(String id, String passwd) {

		App_user finalUser = null;

		finalUser = appUserRepository.getOne(id);


		if(finalUser != null && finalUser.getHash().equals(passwd))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean RegisterValidation(App_user user) {
		boolean sameUsername = false;
		boolean sameEmail = false;

		List<App_user> users = appUserRepository.findAll();

		for(App_user u : users)
		{
			if(u.getUsername().equals(user.getUsername()))
			{
				sameUsername = true;
			}
			if(u.getEmail().equals(user.getEmail()))
			{
				sameEmail = true;
			}	
		}
		if(sameUsername || sameEmail)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	//funcion para recuperacion de contraseña
	public boolean RecoverPassword(String email)
	{
		App_user user;
		String newPassword;

		user = appService.getByEmail(email);

		//se valida el usuario
		if(user != null)
		{
			try {
				//Se crea ña nueva contraseña
				newPassword = UUID.randomUUID().toString();
				String hashedPassword;
				hashedPassword = encode(KeyForRecovery, newPassword);
				user.setHash(new String(hashedPassword));
				//Se envia el email
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				mailMessage.setTo(user.getEmail());
				mailMessage.setSubject("Nueva Contraseña!");
				mailMessage.setFrom("userconfimationjaramillo@gmail.com");
				mailMessage.setText("Ya que olvidaste tu contraseña hemos creado una nueva para ti :3\n\nTu nueva contraseña es "+newPassword+"\nPodras volver a cambiarla desde la aplicación");


				mailSender.sendEmail(mailMessage);
				appService.updateUser(user);
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
		}
		else
		{
			return false;
		}

	}
	
	//Codificacion SHA-256
	public static String encode(String key, String data) throws Exception {
		  Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		  SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		  sha256_HMAC.init(secret_key);

		  return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
		}

	public boolean ProductValidation(Product product) {
		List<Product> productos = new ArrayList<Product>();
		productos = presService.getAllProducts();
		
		for(Product p : productos)
		{
			if(product.getName().equals(p.getName()))
			{
				return false;
				
			}
			System.out.println("paso prueba de nombre");
			
		}
		return true;
	}
	public boolean ServicetValidation(com.CAT.BuffetAPI.Entities.Service service) {
		List<com.CAT.BuffetAPI.Entities.Service> servicios = new ArrayList<com.CAT.BuffetAPI.Entities.Service>();
		servicios = presService.getAllServices();
		
		for(com.CAT.BuffetAPI.Entities.Service s : servicios)
		{
			if(service.getName().equals(s.getName()))
			{
				return false;
			}
			System.out.println("paso prueba de nombre");
			
		}
		return true;
	}
	
	public boolean publicationValidation(Publication publication) {
		List<Publication> publications = new ArrayList<Publication>();
		publications = pubService.getAllPublications();
		
		for(Publication p : publications)
		{
			if(p.getPublic_id().equals(publication.getPublic_id()))
			{
				return false;
			}
			System.out.println("paso prueba de nombre");
			
		}
		return true;
	}
	
	public boolean SaleValidation(Sale sale) {
		HashMap <String, Object> data = new HashMap<String, Object>();
		List<Sale> sales = new ArrayList<Sale>();
		sales = salesServ.getAllSale(data);
		
		for(Sale s : sales)
		{
			if(sale.getSale_id().equals(s.getSale_id()))
			{
				return false;
			}
			System.out.println("paso prueba de Id");
			
		}
		return true;
	}
	
	public boolean bookingRestrictionValidation(Booking_restriction bookingRestriction) {
		HashMap <String, Object> data = new HashMap<String, Object>();
		List<Booking_restriction> restrictions = new ArrayList<Booking_restriction>();
		restrictions = resService.getAllBookingRestrictions(data);
		
		for(Booking_restriction r : restrictions)
		{
			if(r.getRestriction_id().equals(bookingRestriction.getRestriction_id()))
			{
				return false;
			}
			System.out.println("paso prueba de Id");
			
		}
		return true;
	}
	
	public boolean bookingValidation(Booking booking) {
		HashMap <String, Object> data = new HashMap<String, Object>();
		List<Booking> bookings = new ArrayList<Booking>();
		bookings = resService.getAllBooking(data);
		
		for(Booking b : bookings)
		{
			if(b.getBooking_id().equals(booking.getBooking_id()))
			{
				return false;
			}
			System.out.println("paso prueba de Id");
		}
		return true;
	}
}
