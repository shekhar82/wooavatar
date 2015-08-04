package com.wootag.dao.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wootag.dao.IRegisterClientDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:dal-context.xml"})
public class RegisterClientDaoImplTest {

	@Autowired
	private IRegisterClientDao registerClientDao;
	
	
	@Test
	public void testValidateClient() {
		String clientId = "2e527cf1-2d66-4558-b81a-44125669adb0";
		String clientSecret = "T2T28qI9";
		
		boolean result = registerClientDao.isClientValid(clientId, clientSecret);
		assertTrue(result);
	}

}
