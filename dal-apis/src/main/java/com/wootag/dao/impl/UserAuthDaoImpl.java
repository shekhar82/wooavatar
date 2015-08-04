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
import org.springframework.transaction.annotation.Transactional;

import com.wootag.dao.IUserAuthDao;

/**
 * @author gupsh09
 *
 */
@Repository
public class UserAuthDaoImpl implements IUserAuthDao {

	private JdbcTemplate jdbcTemplate;
	
	private static final String REGISTER_USER = "INSERT INTO user_auth (user_id, user_pwd, primary_email) "
			+ " VALUES (?,?,?)";
	
	
	private static final String GET_USER = "SELECT user_pwd FROM user_auth WHERE user_id=?";
	
	private static final String UPDATE_ACCESS_TOKEN = "UPDATE user_auth set access_token=? WHERE user_id=?";
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	/* (non-Javadoc)
	 * @see com.wootag.dao.IUserAuthDao#registerUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(readOnly=false)
	@Override
	public boolean registerUser(String userId, String userPwd, String email) {
		Object[] args = new Object[] {userId,userPwd,email};
		try {
			this.jdbcTemplate.update(REGISTER_USER, args);
			return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wootag.dao.IUserAuthDao#validateUser(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean validateUser(String userId, String userPwd) {
		try {
			String userPassword = this.jdbcTemplate.queryForObject(GET_USER, new Object[]{userId}, String.class);
			
			if (StringUtils.equals(userPwd, userPassword))
				return true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	@Override
	public void updateAccessToken(String userId, String accessToken) {
		Object[] args = new Object[]{accessToken, userId};
		
		this.jdbcTemplate.update(UPDATE_ACCESS_TOKEN,args);
		
	}

}
