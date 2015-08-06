/**
 * 
 */
package com.wootag.avatar.ws.auth.services.impl;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wootag.avatar.ws.auth.services.IAvatarRESTAuthServices;
import com.wootag.dao.IClientOAuthAttribDao;
import com.wootag.dao.IRegisterClientDao;
import com.wootag.dao.IUserAuthDao;
import com.wootag.entities.Client;
import com.wootag.oauth2.as.request.WootagOAuthTokenRequest;

/**
 * @author gupsh09
 *
 */
@Service("authServices")
public class AvatarRESTAuthServicesImpl implements IAvatarRESTAuthServices {

	@Autowired
	private IUserAuthDao userAuthDao;

	@Autowired
	private IClientOAuthAttribDao clientOAuthDao;

	@Autowired
	private IRegisterClientDao clientDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.avatar.ws.auth.services.IAvatarRESTAuthServices#registerUser
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Response registerUser(String userName, String userPassword,
			String primaryEmail) {

		try {

			if (StringUtils.isBlank(userName)
					|| StringUtils.isBlank(userPassword)
					|| StringUtils.isBlank(primaryEmail))
				return Response.status(Status.BAD_REQUEST)
						.entity("Mandatory parameters are missing").build();
			boolean opResult = this.userAuthDao.registerUser(userName,
					userPassword, primaryEmail);
			if (opResult)
				return Response.status(Status.CREATED).entity("User created")
						.build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.serverError().entity("Intenal Server Error").build();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.avatar.ws.auth.services.IAvatarRESTAuthServices#authorize(
	 * javax.servlet.http.HttpServletRequest, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Response getAuthToken(HttpServletRequest request) {

		try {

			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(
					new MD5Generator());

			String clientId = oauthRequest.getClientId();
			String clientSecret = oauthRequest.getClientSecret();

			boolean isClientAuthorized = clientDao.isClientValid(clientId,
					clientSecret.trim());

			if (!isClientAuthorized)
				return Response.status(Status.UNAUTHORIZED)
						.entity("Client not registered").build();

			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
					.authorizationResponse(request,
							HttpServletResponse.SC_FOUND);

			final String authorizationCode = oauthIssuerImpl
					.authorizationCode();
			builder.setCode(authorizationCode);

			clientOAuthDao.updateClientAuthCode(clientId, authorizationCode);
			String redirectURI = oauthRequest
					.getParam(OAuth.OAUTH_REDIRECT_URI);
			final OAuthResponse response = builder.location(redirectURI)
					.buildQueryMessage();
			URI url = new URI(response.getLocationUri());
			return Response.status(response.getResponseStatus()).location(url)
					.build();
		} catch (OAuthSystemException | OAuthProblemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.serverError().entity("Internal Server error").build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.avatar.ws.auth.services.IAvatarRESTAuthServices#authorize(
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Response getSecurityToken(HttpServletRequest request) {
		try {
			WootagOAuthTokenRequest oauthRequest = new WootagOAuthTokenRequest(
					request);
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(
					new MD5Generator());

			String accessToken = null;
			StringBuilder bearerToken = new StringBuilder();
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.AUTHORIZATION_CODE.toString())) {
				String clientId = oauthRequest.getClientId();
				String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);

				boolean isAuthCodeValidated = clientOAuthDao
						.validateClientAuthCode(clientId, authCode);

				if (!isAuthCodeValidated)
					return Response.status(Status.UNAUTHORIZED)
							.entity("Auth token is wrong").build();
				accessToken = oauthIssuerImpl.accessToken();
				clientOAuthDao.updateClientAccessToken(clientId, accessToken);
				bearerToken.append(GrantType.AUTHORIZATION_CODE.name())
						.append(":").append(clientId).append(":")
						.append(accessToken);
			} else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.PASSWORD.toString())) {
				String userId = oauthRequest.getUsername();
				String userPwd = oauthRequest.getPassword();

				boolean isUserAuthenticated = userAuthDao.validateUser(userId,
						userPwd);

				if (!isUserAuthenticated)
					return Response.status(Status.UNAUTHORIZED)
							.entity("User is not authenticated").build();

				accessToken = oauthIssuerImpl.accessToken();
				userAuthDao.updateAccessToken(userId, accessToken);
				bearerToken.append(GrantType.PASSWORD.name()).append(":")
						.append(userId).append(":").append(accessToken);
			}

			String base64EncodedBearerToken = Base64
					.encodeBase64String(bearerToken.toString().getBytes());

			OAuthResponse response = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(base64EncodedBearerToken).setExpiresIn("3600")
					.buildJSONMessage();
			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();
		} catch (OAuthSystemException | OAuthProblemException e) {
			e.printStackTrace();
		}
		return Response.serverError().entity("Internal Server error").build();
	}

	@Override
	public Response registerClient(String clientName, String clientEmail) {
		Client client = new Client(clientName, clientEmail);

		try {
			Client clientRet = clientDao.registerClient(client);
			if (clientRet != null)
				return Response.status(Status.CREATED).entity(clientRet)
						.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.serverError().entity("Internal Server error").build();
	}

}
