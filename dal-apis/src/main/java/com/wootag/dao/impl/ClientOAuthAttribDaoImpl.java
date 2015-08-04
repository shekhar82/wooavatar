/**
 * 
 */
package com.wootag.dao.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wootag.dao.IClientOAuthAttribDao;

/**
 * @author gupsh09
 *
 */
@Repository
public class ClientOAuthAttribDaoImpl implements IClientOAuthAttribDao {

	private static final String UPDATE_CLIENT_AUTH_CODE = "UPDATE client_oauth_attr set auth_code=?, auth_code_issued=? where client_id=?";
	private static final String GET_CLIENT_AUTH_CODE = "SELECT auth_code FROM client_oauth_attr where client_id=?";
	private static final String UPDATE_CLIENT_ACCESS_TOKEN = "UPDATE client_oauth_attr set access_token=?, access_token_issued=? where client_id=?";
	private static final String GET_CLIENT_ACCESS_TOKEN = "SELECT access_token FROM client_oauth_attr where client_id=?";
	
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.dao.IClientOAuthAttribDao#updateClientAuthCode(java.lang.String
	 * , java.lang.String)
	 */
	@Transactional(readOnly=false)
	@Override
	public boolean updateClientAuthCode(String clientId, String authCode) {
		Object[] args = new Object[] {authCode, new Timestamp(new Date().getTime()), clientId};
		try {
			this.jdbcTemplate.update(UPDATE_CLIENT_AUTH_CODE, args);
			return true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.dao.IClientOAuthAttribDao#validateClientAuthCode(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public boolean validateClientAuthCode(String clientId, String authCode) {
		try {
			String authCodeReturned = this.jdbcTemplate.queryForObject(GET_CLIENT_AUTH_CODE, new Object[] {clientId}, String.class);
			
			if (StringUtils.equals(authCode, authCodeReturned))
				return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.dao.IClientOAuthAttribDao#updateClientAccessToken(java.lang
	 * .String, java.lang.String)
	 */
	@Transactional(readOnly=false)
	@Override
	public boolean updateClientAccessToken(String clientId, String accessToken) {
		Object[] args = new Object[] {accessToken, new Timestamp(new Date().getTime()), clientId};
		try {
			this.jdbcTemplate.update(UPDATE_CLIENT_ACCESS_TOKEN, args);
			return true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wootag.dao.IClientOAuthAttribDao#validateClientAccessToken(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public boolean validateClientAccessToken(String clientId, String accessToken) {
		try {
			String accessTokenReturned = this.jdbcTemplate.queryForObject(GET_CLIENT_ACCESS_TOKEN, new Object[] {clientId}, String.class);
			
			if (StringUtils.equals(accessToken, accessTokenReturned))
				return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
