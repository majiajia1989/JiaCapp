package com.e1858.protocol.socket;

public class SocketDefine
{
	public static final int	SOCKET_RESPONSE_SUCCESS		= 0;
	public static final int	HEADER_LENGTH				= 4;

	public static final int CONNECT=5001;
	public static final int CONNECT_RESP=95001;
	
	public static final int PUSH_MSG=5002;
	public static final int PUSH_MSG_RESP=95002;
	
	public static final int DISCONNECT=5999;
	public static final int DISCONNECT_RESP=95999;
	
	
	public static final int ACTIVE_TEST=5901;
	public static final int ACTIVE_TEST_RESP=95901;

}
