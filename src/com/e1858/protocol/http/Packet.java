package com.e1858.protocol.http;

import android.util.Log;

import com.e1858.common.Constant;
import com.e1858.utils.Encrypt;
import com.e1858.utils.JsonUtil;
import com.google.gson.annotations.Expose;

public abstract class Packet implements IPacket
{
	@Expose
	private int	cmd;

	public int getCmd() {
		return this.cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public byte[] wrap()
	{
		try
		{
			Encrypt.setDes3Key(Constant.DES3_KEY);
//			byte[] aa = Encrypt.des3EncodeECB(ZipUtil.compress(JsonUtil.toJson(this)));
//            String aa1 = Encrypt.base64Encode(aa);
//            byte[] encodePacket = Encrypt.base64Encode(Encrypt.des3EncodeECB(ZipUtil.compress(JsonUtil.toJson(this)))).getBytes(Constant.ENCODING);
			
            //return Encrypt.base64Encode(Encrypt.des3EncodeECB(ZipUtil.compress(JsonUtil.toJson(this)))).getBytes(Constant.ENCODING);
			return JsonUtil.toJson(this).getBytes(Constant.ENCODING);
			//return Encrypt.des3EncodeECB(ZipUtil.compress(JsonUtil.toJson(this)));
		}
		catch (Exception ex)
		{
			Log.w(Constant.TAG_PROTOCOL, ex);
			return null;
		}
	}

	public IPacket unwrap(byte[] packet)
	{
		try
		{
			Encrypt.setDes3Key(Constant.DES3_KEY);
			//String data = ZipUtil.decompress(Encrypt.des3DecodeECB(packet));
			//String data = ZipUtil.decompress(Encrypt.des3DecodeECB(packet));
			//String data = ZipUtil.decompress(Encrypt.des3DecodeECB( Encrypt.base64Decode(new String(packet,Constant.ENCODING))));
			String data = new String(packet,Constant.ENCODING);
			return JsonUtil.fromJson(data, this.getClass());
		}
		catch (Exception ex)
		{
			// TODO: handle exception
			Log.w(Constant.TAG_PROTOCOL, ex);
			return null;
		}
	}

	public String wrapToString()
	{
		return JsonUtil.toJson(this);
	}
}
