/**
 * 
 */
package com.wootag.avatar.ws.auth.services;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author gupsh09
 *
 */
@WebService(name = "authservices")
@Path("/wooauth")
@Produces({MediaType.APPLICATION_JSON})
public interface IAvatarRESTAuthServices {

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes("application/x-www-form-urlencoded")
	@Path("/registeruser")
	public Response registerUser(@FormParam("userName") String userName, @FormParam("userPwd") String userPassword,
			@FormParam("email") String primaryEmail);
	
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/authtoken")
	public Response getAuthToken(@Context HttpServletRequest request);
	
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	@Path("/token")
	public Response getSecurityToken(@Context HttpServletRequest request);
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/registerclient")
	public Response registerClient(@QueryParam("clientName") String clientName, @QueryParam("clientMail") String clientEmail);
}
