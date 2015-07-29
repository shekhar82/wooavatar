/**
 * 
 */
package com.wootag.avatar.ws.services;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

/**
 * @author gupsh09
 *
 */
@WebService(name = "wooavatar")
@Path("/wooavatar")
@Produces({MediaType.APPLICATION_JSON})
public interface IAvatarRESTServices {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{userId}")
	public Response getUserProfile(@PathParam("userId") String userId);
	
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/{userId}")
	public Response createUserProfile(@Context HttpHeaders headers,
			@PathParam("userId") String userId,
			String profileJson);
	
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/{userId}")
	public Response updateUserProfile(@Context HttpHeaders headers,
			@PathParam("userId") String userId,
			String profileJson);
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/uploadimage/{userId}")
	public Response updateUserProfilePicture(@PathParam("userId") String userId,
			@Multipart("file") MultipartBody p_attachment);
	
	
	
}
