package com.mac.export.naming.impl;

import com.mac.export.naming.HeaderNamingStrategy;

public class CapitalizeCaseStrategy extends BaseHeaderNamingStrategy implements HeaderNamingStrategy {

	@Override
	public String translate(String input) {
		if (input == null)
			return input; // garbage in, garbage out
		int length = input.length();
		StringBuilder result = new StringBuilder(length * 2);
		int resultLength = 0;
		boolean firstLetter = true;
		boolean isPreviousUnderscore = false;
		for (int i = 0; i < length; i++) {
			char c = input.charAt(i);
			if (!firstLetter || Character.isAlphabetic(c)) {
				if (firstLetter) {
					c = Character.toUpperCase(c);
					firstLetter = false;
				} else {
					if (Character.isUpperCase(c) && !isPreviousUnderscore) {
						result.append(' ');
						resultLength++;
					} else if (c == '_' && !isPreviousUnderscore) {
						c = ' ';
						isPreviousUnderscore = true;
					} else if (Character.isLowerCase(c) && isPreviousUnderscore) {
						c = Character.toUpperCase(c);
						isPreviousUnderscore = false;
					} else if (Character.isLowerCase(c) && !isPreviousUnderscore) {
						isPreviousUnderscore = false;
					} else if (c == '_' && isPreviousUnderscore) {
						continue;
					}
				}

				result.append(c);
				resultLength++;
			}
		}
		return resultLength > 0 ? result.toString() : input;
	}

}
