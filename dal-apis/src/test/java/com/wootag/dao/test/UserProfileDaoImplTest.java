package com.wootag.dao.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wootag.dao.IUserProfileDao;
import com.wootag.entities.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:dal-context.xml"})
public class UserProfileDaoImplTest {

	@Autowired
	private IUserProfileDao userDao;
	
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testInsertUserProfile() {
		User user =  new User();
		user.setUserId("brain.bit");
		user.setPrimaryEmail("brain.bit@gmail.com");
		user.setFirstName("Shekhar");
		user.setLastName("Gupta");
		user.setActive(true);
		
		
		userDao.createProfile(user);
		
		
	}
	
	@Test
	public void testUpdateUserProfile()
	{
		User user =  new User();
		user.setUserId("brain.bit");
		user.setFirstName("Shekhar");
		user.setLastName("Ramesh");
		user.setActive(false);
		
		userDao.updateProfile(user);
	}

	
	@Test
	public void testGetUserProfile()
	{
		User user = userDao.getUserProfile("brain.bit");
		Assert.assertNotNull(user);
		
		Assert.assertEquals(user.getPrimaryEmail(), "brain.bit@gmail.com");
	}
	
	@Test
	public void testUpdateProfilePic()
	{
		try {
			URL resource = this.getClass().getClassLoader().getResource("profile_pic.jpg");
			File file = new File(resource.toURI());
			FileInputStream fis = new FileInputStream(file);
			
			userDao.updateProfilePicture("brain.bit", fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testGetProfilePic() throws IOException
	{
		File image = new File("/Users/gupsh09/Pictures/profile_pic.jpg");
		FileOutputStream fos = new FileOutputStream(image);
		
		
		InputStream is = userDao.getUserProfilePicture("brain.bit");
		
		byte[] buffer = new byte[1];
		
		while(is.read(buffer) > 0)
		{
			fos.write(buffer);
		}
		
		fos.close();
	}
	
}
