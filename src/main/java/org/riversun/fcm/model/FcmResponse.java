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
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * Fcm downstream response
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class FcmResponse {

	private final JSONObject mJson;

	public FcmResponse(int httpResponseCode, JSONObject json) {
		mEnabled = true;
		mJson = json;
		mHttpResponseCode = httpResponseCode;
		parse(json);
	}

	public FcmResponse(int httpResponseCode, String errorMsg, Exception e) {
		mEnabled = false;
		mJson = null;
		mHttpResponseCode = httpResponseCode;
	}

	// http layer messages
	private final boolean mEnabled;
	private final int mHttpResponseCode;
	private String mErroMessage;

	// service layer messages
	private Long mMulticastId;
	private Integer mSuccess;
	private int mFailure;
	private int mCanonicalIds;

	public static class FcmResult {

		private String messageId;
		private String error;
		private String registrationId;

		public String getMessageId() {
			return messageId;
		}

		public String getError() {
			return error;
		}

		public String getRegistrationId() {
			return registrationId;
		}

	}

	private List<FcmResult> mResultList;

	private void parse(JSONObject json) {

		if (!json.isNull("multicast_id")) {
			mMulticastId = (Long) json.getLong("multicast_id");
		}
		if (!json.isNull("success")) {
			mSuccess = (Integer) json.getInt("success");
		}
		if (!json.isNull("failure")) {
			mFailure = (Integer) json.getInt("failure");
		}
		if (!json.isNull("canonical_ids")) {
			mCanonicalIds = (Integer) json.getInt("canonical_ids");
		}

		JSONArray results = (JSONArray) getn(json, "results");

		if (results != null) {
			for (int i = 0; i < results.length(); i++) {

				if (mResultList == null) {
					mResultList = new ArrayList<FcmResult>();
				}

				FcmResult rslt = new FcmResult();
				mResultList.add(rslt);

				JSONObject obj = (JSONObject) results.get(i);
				if (!obj.isNull("message_id")) {
					rslt.messageId = (String) getn(obj, "message_id");
				}
				if (!obj.isNull("error")) {
					rslt.error = (String) getn(obj, "error");
				}
				if (!obj.isNull("registration_id")) {
					rslt.registrationId = (String) getn(obj, "registration_id");
				}
			}
		}
	}

	private Object getn(JSONObject json, String key) {
		if (json.isNull(key)) {
			return null;
		}
		return json.get(key);
	}

	public JSONObject getJson() {
		return mJson;
	}

	// Getters[begin]
	public Long getMulticastId() {
		return mMulticastId;
	}

	public Integer getSuccess() {
		return mSuccess;
	}

	public int getFailure() {
		return mFailure;
	}

	public int getCanonicalIds() {
		return mCanonicalIds;
	}

	public List<FcmResult> getResult() {
		return mResultList;
	}

	public boolean isEnabled() {
		return mEnabled;
	}

	public int getHttpResponseCode() {
		return mHttpResponseCode;
	}

	public String getErroMessage() {
		return mErroMessage;
	}

	@Override
	public String toString() {
		return "FcmResponse [mEnabled=" + mEnabled + ", mHttpResponseCode=" + mHttpResponseCode + ", mErroMessage=" + mErroMessage + ", mMulticastId=" + mMulticastId + ", mSuccess=" + mSuccess
				+ ", mFailure=" + mFailure + ", mCanonicalIds=" + mCanonicalIds + ", resultList=" + mResultList + "]";
	}

	// Getters[end]
//	@Override
//	public String toString() {
//		return "FcmResponse [multicastId=" + mMulticastId + ", success=" + mSuccess + ", failure=" + mFailure + ", canonicalIds=" + mCanonicalIds + ",result=" + resultList + "]";
//	}
	
}
