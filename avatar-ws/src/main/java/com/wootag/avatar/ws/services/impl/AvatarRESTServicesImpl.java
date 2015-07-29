/**
 * 
 */
package com.wootag.avatar.ws.services.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wootag.avatar.ws.services.IAvatarRESTServices;
import com.wootag.dao.IUserProfileDao;
import com.wootag.entities.User;

/**
 * @author gupsh09
 *
 */

@Service("avatarApis")
public class AvatarRESTServicesImpl implements IAvatarRESTServices {

	@Autowired
	private IUserProfileDao userDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.avatar.ws.services.IAvatarRESTServices#getUserProfile(java
	 * .lang.String)
	 */
	@Override
	public Response getUserProfile(String userId) {
		try {
			User user = userDao.getUserProfile(userId);

			if (user == null)
				return Response.status(Status.NOT_FOUND)
						.entity("User profile dose not exist").build();
			else {
				
				return Response.ok(user).build();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError()
					.entity("Please check catalina.out for error").build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.avatar.ws.services.IAvatarRESTServices#createUserProfile(javax
	 * .ws.rs.core.HttpHeaders, java.lang.String, java.lang.String)
	 */
	@Override
	public Response createUserProfile(HttpHeaders headers, String userId,
			String profileJson) {

		Gson gson = new Gson();
		User user = null;

		try {
			user = gson.fromJson(profileJson, User.class);
			user.setUserId(userId);
			userDao.createProfile(user);
			return Response.status(Status.CREATED)
					.entity("User Profile created").build();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST)
					.entity("User Profile JSON is not correct").build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.serverError()
					.entity("Please check catalina.out for error").build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.avatar.ws.services.IAvatarRESTServices#updateUserProfile(javax
	 * .ws.rs.core.HttpHeaders, java.lang.String, java.lang.String)
	 */
	@Override
	public Response updateUserProfile(HttpHeaders headers, String userId,
			String profileJson) {
		Gson gson = new Gson();
		User user = null;

		try {
			user = gson.fromJson(profileJson, User.class);
			user.setUserId(userId);
			userDao.updateProfile(user);
			return Response.status(Status.ACCEPTED)
					.entity("User Profile updated").build();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST)
					.entity("User Profile JSON is not correct").build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.serverError()
					.entity("Please check catalina.out for error").build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.avatar.ws.services.IAvatarRESTServices#updateUserProfilePicture
	 * (java.lang.String, org.apache.cxf.jaxrs.ext.multipart.MultipartBody)
	 */
	@Override
	public Response updateUserProfilePicture(String userId,
			MultipartBody p_attachment) {
		try {
			this.userDao.updateProfilePicture(userId, p_attachment
					.getRootAttachment().getDataHandler().getInputStream());
			return Response.status(Status.ACCEPTED).entity("Profile Image updated").build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError()
					.entity("Please check catalina.out for error").build();
		}
	}

	@Override
	public Response getUserProfilePic(String userId) {
		InputStream imageStream = userDao.getUserProfilePicture(userId);
		
		if (imageStream == null)
			return Response.status(Status.NOT_FOUND).entity("No Image available").build();
		try {
			byte[] imageBytes = IOUtils.toByteArray(imageStream);
			return Response.ok(imageBytes).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError()
					.entity("Please check catalina.out for error").build();
		}
		
	}

}
