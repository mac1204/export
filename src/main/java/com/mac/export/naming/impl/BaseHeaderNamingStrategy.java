package com.mac.export.naming.impl;

import com.mac.export.naming.HeaderNamingStrategy;

public class BaseHeaderNamingStrategy implements HeaderNamingStrategy {

	public String translate(String input) {
		return input;
	}
}
