package com.e1858.protocol.socket;

import tang.network.core.SocketSession;

import com.e1858.CEappApp;

public class ClientSession extends SocketSession
{
    private static int sequenceID = 1;
    private CEappApp cEappApp;

    public ClientSession()
    {
        
    }

    public static int getSequenceID()
    {
        return sequenceID++;
    }

	public CEappApp getcEappApp() {
		return cEappApp;
	}

	public void setcEappApp(CEappApp cEappApp) {
		this.cEappApp = cEappApp;
	}

   
}
