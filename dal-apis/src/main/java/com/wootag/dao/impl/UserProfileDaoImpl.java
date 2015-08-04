/**
 * 
 */
package com.wootag.dao.impl;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wootag.dao.IUserProfileDao;
import com.wootag.entities.User;

/**
 * @author gupsh09
 *
 */
@Repository
public class UserProfileDaoImpl implements IUserProfileDao {

	private final static String INSERT_PROFILE = "INSERT INTO user_profile (user_id, primary_email, first_name,last_name,is_active) "
			+ " values (?,?,?,?,?)";
	
	private final static String UPDATE_PROFILE = "UPDATE user_profile set first_name=?,last_name=?,is_active=? where user_id=?";
	
	private final static String UPDATE_PROFILE_PIC =  "UPDATE user_profile set image = ? where user_id=?";
	
	private final static String GET_PROFILE = "SELECT user_id, primary_email, first_name, last_name, last_updated, is_active FROM user_profile WHERE user_id=?";
	
	private final static String GET_PROFILE_PIC = "SELECT image FROM user_profile WHERE user_id=?";
	
	private JdbcTemplate jdbcTemplate;
	
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	
	/* (non-Javadoc)
	 * @see com.wootag.dao.IUserProfileDao#createProfile(com.wootag.entities.User)
	 */
	@Transactional(readOnly=false)
	public void createProfile(User user) {
		Object[] args = new Object[] {user.getUserId(),user.getPrimaryEmail(),user.getFirstName(),user.getLastName(),user.isActive()};
		int returnedVal =  this.jdbcTemplate.update(INSERT_PROFILE, args);
	}

	/* (non-Javadoc)
	 * @see com.wootag.dao.IUserProfileDao#updateProfile(com.wootag.entities.User)
	 */
	@Transactional(readOnly=false)
	public void updateProfile(User user) {
		Object[] args = new Object[] {user.getFirstName(),user.getLastName(),user.isActive(),user.getUserId()};
		int returnedVal =  this.jdbcTemplate.update(UPDATE_PROFILE, args);

	}

	/* (non-Javadoc)
	 * @see com.wootag.dao.IUserProfileDao#updateProfilePicture(java.lang.String, com.mysql.jdbc.Blob)
	 */
	@Transactional(readOnly=false)
	public void updateProfilePicture(String userId, InputStream fis) {
		
		this.jdbcTemplate.execute(UPDATE_PROFILE_PIC, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setBinaryStream(1, fis);
				ps.setString(2, userId);
				return ps.execute();
			}
		});

	}

	/* (non-Javadoc)
	 * @see com.wootag.dao.IUserProfileDao#getUserProfile(java.lang.String)
	 */
	public User getUserProfile(String userId) {
		Object[] args = new Object[]{userId};
		try {
			User user = this.jdbcTemplate.queryForObject(GET_PROFILE, args, new RowMapper<User>() {

				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setUserId(rs.getString("user_id"));
					user.setPrimaryEmail(rs.getString("primary_email"));
					user.setFirstName(rs.getString("first_name"));
					user.setLastName(rs.getString("last_name"));
					user.setLastUpdated(rs.getTimestamp("last_updated"));
					user.setActive(rs.getBoolean("is_active"));
					return user;
				}
				
			});
			
			return user;
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wootag.dao.IUserProfileDao#getUserProfilePicture(java.lang.String)
	 */
	public InputStream getUserProfilePicture(String userId) {
		Object[] args = new Object[]{userId};
		
		try {
			InputStream imageStream = this.jdbcTemplate.queryForObject(GET_PROFILE_PIC, args, new RowMapper<InputStream>(){

				@Override
				public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getBinaryStream(1);
				}
				
			});
			return imageStream;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
