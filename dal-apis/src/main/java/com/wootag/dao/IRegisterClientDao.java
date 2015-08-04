/**
 * 
 */
package com.wootag.dao;

import com.wootag.entities.Client;

/**
 * @author gupsh09
 *
 */
public interface IRegisterClientDao {

	public Client registerClient(Client client);
	public boolean isClientValid (String clientId, String clientSecret);
}
