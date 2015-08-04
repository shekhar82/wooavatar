/**
 * 
 */
package com.wootag.dao;

/**
 * @author gupsh09
 *
 */
public interface IUserAuthDao {

	public boolean registerUser(String userId, String userPwd, String email);
	
	public boolean validateUser(String userId, String userPwd);
	
	public void updateAccessToken(String userId, String accessToken);
	
}
