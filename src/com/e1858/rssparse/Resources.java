package com.e1858.rssparse;

/**
 * Internal helper class for resource-sensitive objects such as streams.
 * 
 */
final class Resources {

  /* Hide constructor */
  private Resources() {}

  /**
   * Closes stream and suppresses IO faults.
   * 
   * @return {@code null} if stream has been successfully closed,
   *         {@link java.io.IOException} otherwise
   */
  static java.io.IOException closeQuietly(java.io.Closeable stream) {
    if (stream == null) {
      return null;
    }

    try {
      stream.close();
    } catch (java.io.IOException e) {
      return e;
    }

    return null;
  }

}

