package com.e1858.rssparse;

/**
 * Non-recoverable runtime exception.
 */
public class RSSFault extends RuntimeException {

  /**
   * Unsupported serialization
   */
  private static final long serialVersionUID = 1L;

  public RSSFault(String message) {
    super(message);
  }

  public RSSFault(Throwable cause) {
    super(cause);
  }

  public RSSFault(String message, Throwable cause) {
    super(message, cause);
  }

}

