/**
 * 
 */
package com.wootag.entities;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author gupsh09
 *
 */
public class Client {

	private String clientId;
	private String clientSecret;
	private String clientName;
	private String clientMail;
	
	public Client(String name, String email)
	{
		this.clientName = name;
		this.clientMail = email;
		this.clientId = UUID.randomUUID().toString();
		this.clientSecret = RandomStringUtils.randomAlphanumeric(8);
	}
	public String getClientId() {
		return clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientMail() {
		return clientMail;
	}
	public void setClientMail(String clientMail) {
		this.clientMail = clientMail;
	}
	
	
}
