package com.wootag.avatar.ws.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;

import com.wootag.dao.IClientOAuthAttribDao;
import com.wootag.dao.IUserAuthDao;

public class AuthorizationInterceptor implements ContainerRequestFilter {

	
	@Autowired
	private IUserAuthDao userAuthDao;
	
	@Autowired
	private IClientOAuthAttribDao clientOAuthDao;
	
	@Context 
	private HttpServletRequest request;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		OAuthAccessResourceRequest oauthRequest;
		try {
			oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
			String accessToken = oauthRequest.getAccessToken();
			
			
			String methodType = requestContext.getMethod();
			if (!StringUtils.isBlank(accessToken))
			{
				String base64DecodedToken = new String(Base64.decodeBase64(accessToken));
				
				String[] tokenComp = base64DecodedToken.split(":");
				
				if (tokenComp.length != 3)
					throw new WebApplicationException("Token doesnt have all data", Status.UNAUTHORIZED);
				
				String grantType = tokenComp[0];
				String clientOrUserId = tokenComp[1];
				String token = tokenComp[2];
				if (GrantType.AUTHORIZATION_CODE.name().equalsIgnoreCase(grantType))
				{
					if ("GET".equalsIgnoreCase(methodType))
					{
						if (!isClientAuthorized(clientOrUserId, token))
							throw new WebApplicationException("Client is not authorized to access API",Status.UNAUTHORIZED);
					}
					else
					{
						throw new WebApplicationException("Only Get Operations are allowed for client",Status.UNAUTHORIZED);
					}
				}
				else if (GrantType.PASSWORD.name().equalsIgnoreCase(grantType))
				{
					if (!isUserAuthorized(clientOrUserId, token))
						throw new WebApplicationException("User is not authorized to access API",Status.UNAUTHORIZED);
				}
			}
			else
				throw new WebApplicationException("Access token is not passed",Status.UNAUTHORIZED);
		} catch (OAuthSystemException | OAuthProblemException e) {
			e.printStackTrace();
			throw new WebApplicationException(e.getMessage(),Status.UNAUTHORIZED);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			throw new WebApplicationException(ex.getMessage(),Status.UNAUTHORIZED);
		}
            
            
		
	}

	
	private boolean isUserAuthorized(String userId, String accessToken)
	{
		boolean result = userAuthDao.validateAccessToken(userId, accessToken);
		return result;
	}
	
	private boolean isClientAuthorized(String clientId, String accessToken)
	{
		boolean result = clientOAuthDao.validateClientAccessToken(clientId, accessToken);
		return result;
	}
	
}
