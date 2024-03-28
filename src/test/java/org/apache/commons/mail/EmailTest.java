package org.apache.commons.mail;


import static org.junit.Assert.*;

import java.sql.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.subethamail.smtp.server.Session;
import javax.mail.internet.MimeMessage;


// mcphailz@umich.edu

public class EmailTest {

	// Declarations
	Email emailObj = null;
	Date dateObj = null;
	

	// Setup
	@Before
	public void Setup() {
		emailObj = new EmailStub();
		dateObj = new Date(0);
	}
	
	
	// Teardown
	@After
	public void Teardown() {
		emailObj = null;
		dateObj = null;
	}
	
	
	// addBcc 
	@Test
	public void testaddBcc() throws EmailException {
		emailObj.addBcc("abc@def");
		List<InternetAddress> bccList = emailObj.getBccAddresses();
		
		assertEquals(1, bccList.size());
	}
	
	
	// addCc 
	@Test
	public void testaddCc() throws EmailException {
		emailObj.addCc("abc@def");
		List<InternetAddress> ccList = emailObj.getCcAddresses();
		
		assertEquals(1, ccList.size());
	}

	
	// addHeader: Failure of parameter 1
	@Test(expected = IllegalArgumentException.class)
	public void testaddHeaderEmptyName() throws EmailException {
		emailObj.addHeader(null, "Value");
	}
	
	// addHeader: Failure of parameter 2
	@Test(expected = IllegalArgumentException.class)
	public void testaddHeaderEmptyValue() throws EmailException {
		emailObj.addHeader("Name", null);
	}
	
	// addHeader: Valid parameters
	@Test
	public void testaddHeader() {
		emailObj.addHeader("mcphailz", "1234");
		assertEquals(emailObj.headers.get("mcphailz"), "1234");
	}
	
	
	// addReplyTo(String email, String name)
	@Test
	public void testaddReplyTo() throws EmailException {
		Email emailObjReply = emailObj.addReplyTo("abc@def", "mcphailz");
		assertEquals(emailObj, emailObjReply);
	}
	
	
	// buildMimeMessage: Email with complete fields (from, to, content, etc)
	// (71.7%)
	@Test()
	public void testbuildMimeMessageFull() throws EmailException, MessagingException {
		emailObj.setHostName("mcphailz");
		emailObj.addBcc("abc@def");
		emailObj.addCc("abc@def");
		emailObj.addReplyTo("abc@def", "mcphailz");
		emailObj.addHeader("mcphailz", "1234");
		emailObj.setFrom("abc@def", "Riley");
		emailObj.addTo("def@ghi");
		emailObj.setContent("Hello, world! Give me an A+.", "String");
		emailObj.setSubject("Super awesome subject");
		emailObj.buildMimeMessage();
		
		assertEquals(emailObj.getMimeMessage().getSubject(), emailObj.getSubject());
	}
	
	// buildMimeMessage: Email with host and missing fields (from, to, content, etc)
	@Test(expected = EmailException.class)
	public void testbuildMimeMessageNoListException() throws EmailException {
		emailObj.setHostName("mcphailz");
		emailObj.buildMimeMessage();
	}
	
	
	// getHostName without host 
	// (70.6%)
	@Test
	public void testgetHostNameNoHost() {
		assertEquals(emailObj.getHostName(), null);
	}
	
	// getHostName with host
	@Test
	public void testgetHostName() {
		emailObj.setHostName("mcphailz");
		assertEquals(emailObj.getHostName(),"mcphailz");
	}
	
	
	// getMailSession w/o host 
	// (72%)
	@Test(expected = EmailException.class)
	public void testgetMailSessionNoHost() throws EmailException {
		emailObj.getMailSession();
	}
	
	// getMailSession with host
	@Test
	public void testgetMailSession() throws EmailException, IOException {
		emailObj.setHostName("mcphailz");
		assertNotNull(emailObj.getMailSession());
	}
	
	
	// getSendDate: Existing Date Object (71.4%)
	@Test
	public void testgetSentDateExisting() {
		emailObj.setSentDate(dateObj);
		java.util.Date dateResponse = emailObj.getSentDate();
		assertEquals(dateObj, dateResponse);
	}
	
	
	// getSocketConnectionTimeout
	@Test
	public void testgetSocketConnectionTimeout() {
		int template = 5;
		emailObj.setSocketConnectionTimeout(template);
		
		int timeout = emailObj.getSocketConnectionTimeout();
		assertEquals(timeout, template);
	}
	
	
	// setFrom
	@Test
	public void testsetFrom() throws EmailException {
		Email emailObj2 = emailObj.setFrom("abc@def");
		assertEquals(emailObj, emailObj2);
	}
}
