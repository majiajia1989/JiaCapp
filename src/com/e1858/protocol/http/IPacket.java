package com.e1858.protocol.http;

import java.io.Serializable;

public interface IPacket extends Serializable
{
	byte[] wrap();

	IPacket unwrap(byte[] data);
}
