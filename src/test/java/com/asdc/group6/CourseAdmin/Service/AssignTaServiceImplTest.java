package com.asdc.group6.CourseAdmin.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import com.asdc.group6.Models.User;

public class AssignTaServiceImplTest {

	AssignTaServiceImpl assignTaServiceImplMock = mock(AssignTaServiceImpl.class);
	
	@Test
	void testGetAllUsers() {
		
		ArrayList<User> list = new ArrayList<>();
		
		User user = new User();
		user.setBannerId("B00851234");
		user.setFirstName("Bob");
		user.setLastName("Builder");
		user.setPassword("bobpassword");
		user.setUserType("S");
		
		list.add(user);
		
		when(assignTaServiceImplMock.getAllUsers()).thenReturn(list);
		assertEquals(assignTaServiceImplMock.getAllUsers(), list,"No user data present in database");
		verify(assignTaServiceImplMock).getAllUsers();
	}
	
	@Test
	void testAssignTa() {
		
		when(assignTaServiceImplMock.assignTa("1", 2)).thenReturn(true);
		assertTrue(assignTaServiceImplMock.assignTa("1", 2), "Ta not assigned");
		verify(assignTaServiceImplMock).assignTa("1", 2);

	}
}
