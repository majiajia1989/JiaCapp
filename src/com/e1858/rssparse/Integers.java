package com.e1858.rssparse;
final class Integers {
	
	private Integers() {
	}
	static Integer parseInteger(String value) {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			throw new RSSFault(e);
		}
	}

}
