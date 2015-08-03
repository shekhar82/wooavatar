/**
 * 
 */
package com.wootag.dao.impl;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wootag.dao.IRegisterClientDao;
import com.wootag.entities.Client;

/**
 * @author gupsh09
 *
 */

@Repository
public class RegisterClientDaoImpl implements IRegisterClientDao {

	private static final String REGISTER_CLIENT = "INSERT INTO client_registry(client_id,client_secret,client_name,client_email)"
			+ " VALUES (?,?,?,?)";
	
	private static final String CHECK_CLIENT = "SELECT client_secret from client_registry where client_id=?";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.dao.IRegisterClientDao#registerClient(com.wootag.entities.
	 * Client)
	 */
	@Override
	public boolean registerClient(Client client) 
	{
		Object[] args = new Object[] { client.getClientId(),
				client.getClientSecret(), client.getClientName(),
				client.getClientMail() };
		try {
			int returnedVal = this.jdbcTemplate.update(REGISTER_CLIENT, args);
			
			if (returnedVal == 1)
				return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean isClientValid(String clientId, String clientSecret) {
		Object[] args = new Object[]{clientId};
		
		try {
			String secret = this.jdbcTemplate.queryForObject(CHECK_CLIENT, args, String.class);
			if (StringUtils.equals(clientSecret, secret))
				return true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}

}
