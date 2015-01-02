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
package org.riversun.fcm.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * Data model for sending messages to specific objects<br>
 * 
 * <p>
 * Wrapped JSON message like, <br>
 * 
 * <code>
 * { "data":{
 *     "myKey1":"myValue1",
 *     "myKey2":"myValue2"
 *   },
 *   "registration_ids":["your_registration_token1","your_registration_token2]
 * }
 * </code>
 * 
 * {@see https://firebase.google.com/docs/cloud-messaging/http-server-ref}
 * {@see https://firebase.google.com/docs/cloud-messaging/send-message}
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class EntityMessage {

	private final Map<String, Object> mDataMap = new LinkedHashMap<String, Object>();
	private final List<String> mRegistrationTokenList = new ArrayList<String>();

	/**
	 * Pub String value to the payload
	 * 
	 * @param key
	 * @param value
	 */
	public void putStringData(String key, String value) {
		putData(key, value);
	}

	/**
	 * Put boolean value to the payload
	 * 
	 * @param key
	 * @param value
	 */
	public void putBooleanData(String key, boolean value) {
		putData(key, value);
	}

	/**
	 * Put Object
	 * 
	 * @param key
	 * @param value
	 */
	public void putData(String key, Object value) {
		mDataMap.put(key, value);
	}

	/**
	 * Add specified registrationToken
	 * <p>
	 * <@see
	 * "https://firebase.google.com/docs/cloud-messaging/http-server-ref?hl=en">
	 * <p>
	 * "RegistrationToken" specifies a list of entity(mobile devices,browser
	 * front-end apps)s (registration tokens, or IDs) receiving a multicast
	 * message. It must contain at least 1 and at most 1000 registration tokens.
	 * <p>
	 * Use this parameter only for multicast messaging, not for single
	 * recipients. Multicast messages (sending to more than 1 registration
	 * tokens) are allowed using HTTP JSON format only.
	 * 
	 * @param registrationToken
	 */
	public void addRegistrationToken(String registrationToken) {
		mRegistrationTokenList.add(registrationToken);
	}

	/**
	 * Remove specified registrationId
	 * 
	 * @param registrationToken
	 */
	public void removeRegistrationToken(String registrationToken) {
		if (mRegistrationTokenList.contains(registrationToken)) {
			mRegistrationTokenList.remove(registrationToken);
		}

	}

	/**
	 * Set registrationIds in the specified list
	 * 
	 * @param list
	 */
	public void setRegistrationTokenList(List<String> list) {
		mRegistrationTokenList.clear();
		mRegistrationTokenList.addAll(list);
	}

	/**
	 * Remove all registered registrationIds
	 */
	public void clearRegistrationTokens() {
		mRegistrationTokenList.clear();
	}

	/**
	 * Generates JSONObject
	 * 
	 * @return
	 */
	public JSONObject toJsonObject() {

		final JSONObject json = new JSONObject();

		/**
		 * Reference from firebase * <@see
		 * "https://firebase.google.com/docs/cloud-messaging/http-server-ref?hl=en"
		 * >
		 * <p>
		 * This parameter specifies a list of entity(mobile devices,browser
		 * front-end apps)s (registration tokens, or IDs) receiving a multicast
		 * message. It must contain at least 1 and at most 1000 registration
		 * tokens.
		 * 
		 * <p>
		 * Use this parameter only for multicast messaging, not for single
		 * recipients. Multicast messages (sending to more than 1 registration
		 * tokens) are allowed using HTTP JSON format only.
		 * 
		 * <@see
		 * "https://firebase.google.com/docs/cloud-messaging/http-server-ref?hl=en"
		 * >
		 */
		final String[] registrationIds = mRegistrationTokenList.toArray(new String[] {});

		// for multicast
		json.accumulate("registration_ids", registrationIds);

		// payload
		/**
		 * Reference from firebase * <@see
		 * "https://firebase.google.com/docs/cloud-messaging/http-server-ref?hl=en"
		 * >
		 * <p>
		 * his parameter specifies the custom key-value pairs of the message's
		 * payload.
		 * <p>
		 * For example, with data:{"score":"3x1"}:
		 * <p>
		 * On iOS, if the message is sent via APNS, it represents the custom
		 * data fields. If it is sent via FCM connection server, it would be
		 * represented as key value dictionary in AppDelegate
		 * application:didReceiveRemoteNotification:.
		 * <p>
		 * On Android, this would result in an intent extra named score with the
		 * string value 3x1.
		 * <p>
		 * The key should not be a reserved word ("from" or any word starting
		 * with "google" or "gcm"). Do not use any of the words defined in this
		 * table (such as collapse_key).
		 * <p>
		 * Values in string types are recommended. You have to convert values in
		 * objects or other non-string data types (e.g., integers or booleans)
		 * to string
		 * 
		 */
		json.accumulate("data", mDataMap);

		return json;
	}

	/**
	 * Generates JSON text
	 * <p>
	 * To generate JSON message like followings<br>
	 * <code>
	 * { "data":{
	 *     "myKey1":"myValue1",
	 *     "myKey2":"myValue2"
	 *   },
	 *   "registration_ids":["your_registration_token1","your_registration_token2]
	 * }
	 * </code>
	 * 
	 * 
	 * @return
	 */
	public String toJson() {
		return toJsonObject().toString();
	}
}
