package com.e1858.rssparse;

public class RSSReaderException extends RSSException {

  /**
   * Unsupported serialization
   */
  private static final long serialVersionUID = 1L;

  private final int status;

  public RSSReaderException(int status, String message) {
    super(message);
    this.status = status;
  }

  public RSSReaderException(int status, Throwable cause) {
    super(cause);
    this.status = status;
  }

  public RSSReaderException(int status, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  /**
   * Return the HTTP status which caused the error.
   * 
   * @return HTTP error status code
   */
  public int getStatus() {
    return status;
  }
}

