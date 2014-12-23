package org.riversun.fcm.model;

/*
 * Copyright 2016-2017 Tom Misawa, riversun.org@gmail.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in the 
 * Software without restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
 * Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import static org.junit.Assert.*;

/**
 * 
 * UT for FcmResponse
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class FcmResponseTest {
	@Rule
	public TestName name = new TestName();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_parse_success_single() {
		String jsonText = "{\"multicast_id\":987654321,\"success\":1,\"failure\":0,\"canonical_ids\":0,\"results\":[{\"message_id\":\"0:11111%8111abcdefg\"}]}";
		JSONObject jo = new JSONObject(jsonText);
		FcmResponse o = new FcmResponse(200, jo);
		assertEquals(987654321L, (long) o.getMulticastId());
		assertEquals(200, o.getHttpResponseCode());
		assertEquals(true, o.isEnabled());
		assertEquals(1, (int) o.getSuccess());
		assertEquals(0, (int) o.getFailure());
		assertEquals(0, (int) o.getCanonicalIds());

		assertEquals(1, o.getResult().size());
		assertEquals("0:11111%8111abcdefg", o.getResult().get(0).getMessageId());
		assertNull(o.getResult().get(0).getError());
		assertNull(o.getResult().get(0).getRegistrationId());
	}

	@Test
	public void test_parse_error_single() {

		String jsonText = "{\"multicast_id\":123456,\"success\":0,\"failure\":1,\"canonical_ids\":0,\"results\":[{\"error\":\"InvalidRegistration\"}]}";
		JSONObject jo = new JSONObject(jsonText);
		FcmResponse o = new FcmResponse(200, jo);
		assertEquals(123456L, (long) o.getMulticastId());
		assertEquals(0, (int) o.getSuccess());
		assertEquals(1, (int) o.getFailure());
		assertEquals(0, (int) o.getCanonicalIds());

		assertEquals(1, o.getResult().size());
		assertEquals("InvalidRegistration", o.getResult().get(0).getError());
		assertNull(o.getResult().get(0).getMessageId());
		assertNull(o.getResult().get(0).getRegistrationId());
	}

	@Test
	public void test_parse_error_multi() {

		String jsonText = "{\"multicast_id\":987654,\"success\":0,\"failure\":2,\"canonical_ids\":0,\"results\":[{\"error\":\"InvalidRegistration\"},{\"error\":\"InvalidRegistration\"}]}";
		JSONObject jo = new JSONObject(jsonText);
		FcmResponse o = new FcmResponse(200, jo);
		assertEquals(987654L, (long) o.getMulticastId());
		assertEquals(0, (int) o.getSuccess());
		assertEquals(2, (int) o.getFailure());
		assertEquals(0, (int) o.getCanonicalIds());

		assertEquals(2, o.getResult().size());
		for (int i = 0; i < o.getFailure(); i++) {
			assertEquals("InvalidRegistration", o.getResult().get(i).getError());
			assertNull(o.getResult().get(0).getMessageId());
			assertNull(o.getResult().get(0).getRegistrationId());
		}
	}

	@Test
	public void test_parse_both_success_and_error() {

		String jsonText = "{\"multicast_id\":12121212,\"success\":1,\"failure\":1,\"canonical_ids\":0,\"results\":[{\"message_id\":\"abcdef\"},{\"error\":\"InvalidRegistration\"}]}";
		JSONObject jo = new JSONObject(jsonText);
		FcmResponse o = new FcmResponse(200, jo);
		assertEquals(12121212, (long) o.getMulticastId());
		assertEquals(1, (int) o.getSuccess());
		assertEquals(1, (int) o.getFailure());
		assertEquals(0, (int) o.getCanonicalIds());

		assertEquals(2, o.getResult().size());

		assertEquals("abcdef", o.getResult().get(0).getMessageId());
		assertNull(o.getResult().get(0).getError());
		assertNull(o.getResult().get(0).getRegistrationId());

		assertEquals("InvalidRegistration", o.getResult().get(1).getError());
		assertNull(o.getResult().get(1).getMessageId());
		assertNull(o.getResult().get(1).getRegistrationId());
	}

}
