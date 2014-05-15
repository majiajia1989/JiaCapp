package com.e1858.protocol;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Server implements Serializable
{
	
    @Expose
    private String  ip;
    
    @Expose
    private int    port;
    
    @Expose
    private String key;
     
    @Expose 
    private int  activeInterval;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getActiveInterval() {
		return activeInterval;
	}

	public void setActiveInterval(int activeInterval) {
		this.activeInterval = activeInterval;
	}
	
	

	
  
}
