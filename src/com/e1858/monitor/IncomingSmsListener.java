package com.e1858.monitor;

public interface IncomingSmsListener
{
	public boolean onIncomingSms(String number, String message);
}
