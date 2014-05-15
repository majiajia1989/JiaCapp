package com.e1858.protocol.socket;

import java.nio.ByteBuffer;

import tang.network.packet.IPacket;
import android.util.Log;

import com.e1858.common.Constant;
import com.e1858.utils.JsonUtil;
import com.google.gson.annotations.Expose;

public class Packet implements IPacket
{
	@Expose
	private int			cmd;
	@Expose
	private int			sequence;
	@Expose
	protected String	name;

	private ByteBuffer	byteBuffer;
	
	public Packet()
	{
		name = "SocketPacket";

	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public int getSequence()
	{
		return sequence;
	}

	public void setSequence(int sequence)
	{
		this.sequence = sequence;
	}

	public String getName()
	{
		// TODO Auto-generated method stub
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ByteBuffer getByteBuffer()
	{
		if (null == byteBuffer)
		{
			byteBuffer = ByteBuffer.allocate(8196);
		}
		return byteBuffer;
	}

	public void setByteBuffer(ByteBuffer byteBuffer)
	{
		this.byteBuffer = byteBuffer;
	}
	
	
	public ByteBuffer wrap()
	{
		try
		{

			getByteBuffer().clear();

			//byte[] encodePacket = Encrypt.des3EncodeECB(ZipUtil.compress(JsonUtil.toJson(this)));
			
			//byte[] encodePacket = Encrypt.base64Encode(Encrypt.des3EncodeECB(ZipUtil.compress(JsonUtil.toJson(this)))).getBytes(Constant.ENCODING);
			byte[] encodePacket = JsonUtil.toJson(this).getBytes(Constant.ENCODING);
		
			byteBuffer.putInt(0);
			byteBuffer.put(encodePacket);
			byteBuffer.putInt(0, byteBuffer.position());
			byteBuffer.flip();
		}
		catch (Exception ex)
		{
			// TODO Auto-generated catch block
			Log.w(Constant.TAG_PROTOCOL, ex);
		}
		return byteBuffer;
	}

	public boolean unwrap(ByteBuffer byteBuffer)
	{
		// Encrypt.setDes3Key(Constant.DES3_KEY);
		int packetLen = byteBuffer.getInt(0);
		byteBuffer.position(byteBuffer.position() + packetLen);
		return true;
	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		StringBuilder body = new StringBuilder();
		body.append(getName()).append(":").append(System.getProperty("line.separator"));
		body.append("\t").append("cmd:").append(getCmd()).append(System.getProperty("line.separator"));
		body.append("\t").append("sequence:").append(getSequence()).append(System.getProperty("line.separator"));
		body.append("\t").append("name:").append(name).append(System.getProperty("line.separator"));
		return body.toString();
	}

}
