/**
 * 
 */
package com.wootag.dao;

/**
 * @author gupsh09
 *
 */
public interface IClientOAuthAttribDao {

	public boolean updateClientAuthCode(String clientId, String authCode);
	
	public boolean validateClientAuthCode(String clientId, String authCode);
	
	public boolean updateClientAccessToken(String clientId, String accessToken);
	
	public boolean validateClientAccessToken(String clientId, String accessToken);
	
}
