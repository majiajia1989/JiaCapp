package com.e1858.rssparse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
final class Dates {

  /**
   * @see <a href="http://www.ietf.org/rfc/rfc0822.txt">RFC 822</a>
   */
  private static final SimpleDateFormat RFC822 = new SimpleDateFormat(
      "EEE, dd MMM yyyy HH:mm:ss Z", java.util.Locale.ENGLISH);

  private Dates() {
	  
  }
  static java.util.Date parseRfc822(String date) {
    try {
    	if(date!=""){
    		return RFC822.parse(date);
    	}
    } catch (ParseException e) {
      throw new RSSFault(e);
    }
	return null;
  }

}

