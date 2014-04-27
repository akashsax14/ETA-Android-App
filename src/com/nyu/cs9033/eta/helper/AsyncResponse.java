package com.nyu.cs9033.eta.helper;

import org.json.JSONObject;

public interface AsyncResponse {
	void processFinish(JSONObject output);
}