package com.e1858.rssparse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.StrictMode;
import android.util.Log;

public class RSSReader implements java.io.Closeable {

  /**
   * Thread-safe {@link HttpClient} implementation.
   */
  private final HttpClient httpclient;

  /**
   * Thread-safe RSS parser SPI.
   */
  private final RSSParserSPI parser;

  /**
   * Instantiate a thread-safe HTTP client to retrieve RSS feeds. The injected
   * {@link HttpClient} implementation must be thread-safe.
   * 
   * @param httpclient thread-safe HTTP client implementation
   * @param parser thread-safe RSS parser SPI implementation
   */
  public RSSReader(HttpClient httpclient, RSSParserSPI parser) {
    this.httpclient = httpclient;
    this.parser = parser;
  }

  /**
   * Instantiate a thread-safe HTTP client to retrieve RSS feeds. The injected
   * {@link HttpClient} implementation must be thread-safe. Internal memory
   * consumption and load performance can be tweaked with {@link RSSConfig}.
   * 
   * @param httpclient thread-safe HTTP client implementation
   * @param config RSS configuration
   */
  public RSSReader(HttpClient httpclient, RSSConfig config) {
    this(httpclient, new RSSParser(config));
  }

  /**
   * Instantiate a thread-safe HTTP client to retrieve and parse RSS feeds.
   * Internal memory consumption and load performance can be tweaked with
   * {@link RSSConfig}.
   */
  public RSSReader(RSSConfig config) {
    this(new DefaultHttpClient(), new RSSParser(config));
  }

  /**
   * Instantiate a thread-safe HTTP client to retrieve and parse RSS feeds.
   * Default RSS configuration capacity values are used.
   */
  public RSSReader() {
    this(new DefaultHttpClient(), new RSSParser(new RSSConfig()));
  }

  /**
   * Send HTTP GET request and parse the XML response to construct an in-memory
   * representation of an RSS 2.0 feed.
   * 
   * @param uri RSS 2.0 feed URI
   * @return in-memory representation of downloaded RSS feed
   * @throws RSSReaderException if RSS feed could not be retrieved because of
   *           HTTP error
   * @throws RSSFault if an unrecoverable IO error has occurred
   */
  public RSSFeed load(String uri) throws RSSReaderException {
	/**
	  if (android.os.Build.VERSION.SDK_INT > 9) {			
		  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		  StrictMode.setThreadPolicy(policy);  
  		} 
  		*/
    final HttpGet httpget = new HttpGet(uri);
    RSSFeed feed = null;
    InputStream feedStream = null;
    try {
      // Send GET request to URI
      final HttpResponse response = httpclient.execute(httpget);

      // Check if server response is valid
      final StatusLine status = response.getStatusLine();
      if (status.getStatusCode() != HttpStatus.SC_OK) {
        throw new RSSReaderException(status.getStatusCode(),
            status.getReasonPhrase());
      }

      // Extract content stream from HTTP response
      //从http响应中提取内容流
      HttpEntity entity = response.getEntity();
      if(entity!=null){
    	  Log.v("entity","===="+entity.getContentType().getValue());
    	  String type=entity.getContentType().getValue().toString().trim();
    	  if(!(type.equals("text/html"))){
    		  
    		  feedStream = entity.getContent();
        	
    		  feed= parser.parse(feedStream);

              if (feed.getLink() == null) {
                feed.setLink(Uri.parse(uri));
              }

             
    	  } 
      }
     
    } catch (ClientProtocolException e) {
      throw new RSSFault(e);
    } catch (IOException e) {
      throw new RSSFault(e);
    } finally {
      Resources.closeQuietly(feedStream);
    }
	return feed;
  }

  public static byte[] readInput(InputStream in ) throws IOException{
	   ByteArrayOutputStream out=new ByteArrayOutputStream();
	   int len=0;
	   byte[] buffer=new byte[1024];
	   while((len=in.read(buffer))>0){
	    out.write(buffer,0,len);
	   }
	   out.close();
	   in.close();
	   return out.toByteArray();
	}


	public static InputStream getStringStream(String sInputString){
	   ByteArrayInputStream tInputStringStream=null;
	   if (sInputString != null && !sInputString.trim().equals("")){
	   tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
	   }
	   return tInputStringStream;
	   }
  /**
   * Release all HTTP client resources.
   */
  public void close() {
    httpclient.getConnectionManager().shutdown();
  }

}

