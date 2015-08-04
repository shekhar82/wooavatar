/**
 * 
 */
package com.wootag.oauth2.as.validator;

import javax.servlet.http.HttpServletRequest;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.validators.AbstractValidator;

/**
 * @author gupsh09
 *
 */
public class WootagPasswordValidator extends AbstractValidator<HttpServletRequest> 
{
	public WootagPasswordValidator() {
        requiredParams.add(OAuth.OAUTH_GRANT_TYPE);
        enforceClientAuthentication = true;
    }
}
