/**
 * 
 */
package com.wootag.oauth2.as.request;

import javax.servlet.http.HttpServletRequest;

import org.apache.oltu.oauth2.as.request.AbstractOAuthTokenRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.common.validators.OAuthValidator;

import com.wootag.oauth2.as.validator.WootagAuthorizationCodeValidator;
import com.wootag.oauth2.as.validator.WootagPasswordValidator;

/**
 * @author gupsh09
 *
 */
public class WootagOAuthTokenRequest extends AbstractOAuthTokenRequest {

	public WootagOAuthTokenRequest(HttpServletRequest request)
			throws OAuthSystemException, OAuthProblemException {
		super(request);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OAuthValidator<HttpServletRequest> initValidator()
			throws OAuthProblemException, OAuthSystemException {
		validators.put(GrantType.PASSWORD.toString(),
				WootagPasswordValidator.class);
		validators.put(GrantType.AUTHORIZATION_CODE.toString(),
				WootagAuthorizationCodeValidator.class);
		return super.initValidator();
	}

	@Override
	public String getPassword() {
		String[] creds = OAuthUtils.decodeClientAuthenticationHeader(request.getHeader(OAuth.HeaderType.AUTHORIZATION));
        if (creds != null) {
            return creds[1];
        }
		return getParam(OAuth.OAUTH_PASSWORD);
	}

	@Override
	public String getUsername() {
		String[] creds = OAuthUtils.decodeClientAuthenticationHeader(request.getHeader(OAuth.HeaderType.AUTHORIZATION));
        if (creds != null) {
            return creds[0];
        }
        return getParam(OAuth.OAUTH_USERNAME);
	}

}
