package com.e1858.protocol.socket;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tang.network.core.Session;
import tang.network.packet.IPacket;
import tang.network.packet.IPacketAnalyser;
import android.util.Log;

import com.e1858.common.Constant;
import com.e1858.utils.JsonUtil;

public class Analyzer implements IPacketAnalyser
{
	public IPacket analyze(Session session, ByteBuffer byteBuffer)
	{
		
		Log.v("byteBuffer", byteBuffer.toString());
		
		IPacket packet = null;
		int packetLen;
		int command = 0;
		
		String data;
		byte[] dataPacket;
		Pattern pattern = Pattern.compile("\"cmd\":(\\d+)");
		Matcher matcher;
		if (byteBuffer.remaining() > SocketDefine.HEADER_LENGTH && byteBuffer.remaining() >= byteBuffer.get(0))
		{
			packetLen = byteBuffer.getInt();
			dataPacket = new byte[packetLen - SocketDefine.HEADER_LENGTH];
			byteBuffer.get(dataPacket);
			try
			{
				// dataPacket = Encrypt.des3DecodeECB(Encrypt.base64Decode(new
				// String(dataPacket,Constant.ENCODING)));

				// data = ZipUtil.decompress(dataPacket);
				data = new String(dataPacket, Constant.ENCODING);
				Log.i(Constant.TAG_PROTOCOL_ANALYZE, data);
				matcher = pattern.matcher(data);
				if (matcher.find())
				{
					command = Integer.parseInt(matcher.group(1), 10);
					switch (command)
					{
						case SocketDefine.CONNECT:
							packet = JsonUtil.fromJson(data, Connect.class);
							break;
						case SocketDefine.CONNECT_RESP:
							packet = JsonUtil.fromJson(data, ConnectResp.class);
							break;
						case SocketDefine.DISCONNECT:
							packet = JsonUtil.fromJson(data, Disconnect.class);
							break;
						case SocketDefine.DISCONNECT_RESP:
							packet = JsonUtil.fromJson(data, DisconnectResp.class);
							break;
						case SocketDefine.PUSH_MSG:
							packet=JsonUtil.fromJson(data, PushMsg.class);
							break;
						case SocketDefine.PUSH_MSG_RESP:
							packet=JsonUtil.fromJson(data, PushMsgResp.class);
							break;
						case SocketDefine.ACTIVE_TEST:
							packet = JsonUtil.fromJson(data, ActiveTest.class);
							break;
						case SocketDefine.ACTIVE_TEST_RESP:
							packet = JsonUtil.fromJson(data, ActiveTestResp.class);
							break;
					}
					}
				else
				{
					session.close();
				}
			}
			catch (Exception ex)
			{
				Log.e(Constant.TAG_PROTOCOL_ANALYZE, ex.getMessage(), ex);
			}
		}
		return packet;
	}

}
