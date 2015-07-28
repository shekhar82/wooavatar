/**
 * 
 */
package com.wootag.dao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;

import com.wootag.entities.User;

/**
 * @author gupsh09
 *
 */
public interface IUserProfileDao {

	
	public void createProfile(User user);

	public void updateProfile(User user);

	public void updateProfilePicture(String userId, FileInputStream fis);

	public User getUserProfile(String userId);

	public InputStream getUserProfilePicture(String userId);

}
