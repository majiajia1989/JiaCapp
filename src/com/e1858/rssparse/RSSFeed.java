
package com.e1858.rssparse;

/**
 * Data about an RSS feed and its RSS items.
 */
public class RSSFeed extends RSSBase {

  private final java.util.List<RSSItem> items;
	private java.util.Date lastBuildDate;
	private Integer ttl;

  RSSFeed() {
    super(/* initial capacity for category names */ (byte) 3);
    items = new java.util.LinkedList<RSSItem>();
  }

  /**
   * Returns an unmodifiable list of RSS items.
   */
  public java.util.List<RSSItem> getItems() {
    return java.util.Collections.unmodifiableList(items);
  }

  void addItem(RSSItem item) {
    items.add(item);
  }

	void setLastBuildDate(java.util.Date date) {
		lastBuildDate = date;
	}

	public java.util.Date getLastBuildDate() {
		return lastBuildDate;
	}

	void setTTL(Integer value) {
		ttl = value;
	}

	public Integer getTTL() {
		return ttl;
	}

}

