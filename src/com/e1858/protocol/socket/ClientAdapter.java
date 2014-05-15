package com.e1858.protocol.socket;

import java.nio.channels.SocketChannel;

import tang.network.core.Session;
import tang.network.event.IEventAdapter;
import tang.network.packet.IPacket;
import android.content.ContentValues;
import android.os.Message;
import android.util.Log;
import com.e1858.CEappApp;
import com.e1858.common.Constant;
import com.e1858.common.IntentValue;
import com.e1858.common.MessageWhat;
import com.e1858.common.ModuleInteger;
import com.e1858.database.TableName;
import com.e1858.network.NetUtil;
import com.e1858.protocol.Msg;
import com.e1858.protocol.UserBase;

public class ClientAdapter implements IEventAdapter
{
	private CEappApp	cEappApp;
	private int			activeLostCount		= 0;
	private boolean		loginSucess			= false;
	private int			lastPacketCommand	= -1;
	
	public ClientAdapter(CEappApp cEappApp)
	{
		this.cEappApp = cEappApp;
	}

	public void clientAccepted(Session session, SocketChannel socketChannel)
	{
		// TODO Auto-generated method stub

	}

	public void exceptionCaught(Session session, Throwable throwable)
	{
		// TODO Auto-generated method stub
		session.close();

		String exString = "";
		if (null != throwable.getMessage())
		{
			exString = throwable.getMessage();
		}
		else
		{
			exString = "";
		}
		Log.e(Constant.TAG_NETWORK_SOCKET, exString, throwable);
		Message message = cEappApp.getCurrentHandler().obtainMessage(MessageWhat.NETWORK_SOCKET_EXCEPTION, exString);
		cEappApp.getCurrentHandler().sendMessage(message);
	}

	public void packetReceived(Session session, IPacket iPacket)
	{
		ClientSession clientSession = (ClientSession) session;
		Packet packet = (Packet) iPacket;
		Message message = null;
		activeLostCount = 0;
		//收到包
		switch (packet.getCmd())
		{
			case SocketDefine.CONNECT:
				break;
			case SocketDefine.CONNECT_RESP://收到连接返回包
				message = cEappApp.getCurrentHandler().obtainMessage(packet.getCmd(), packet);
				cEappApp.getCurrentHandler().sendMessage(message);
				ConnectResp connectResp = (ConnectResp) packet;
				if (connectResp.getStatus() == 0)
				{
				
					if(0!=connectResp.getMsgCnt()){
						int msgCnt=connectResp.getMsgCnt();
						Log.v("msgCnt", "="+msgCnt);
						
					}
					loginSucess = true;
				}
				else
				{
					session.close();
				}
				break;
			case SocketDefine.PUSH_MSG://收到推送消息包
				
				Log.v("推送消息","推送消息");
				message=cEappApp.getCurrentHandler().obtainMessage(packet.getCmd(),packet);
			
				PushMsg pushMsg=(PushMsg)packet;
				
				if(pushMsg.getMsg().getModule()==ModuleInteger.MESSAGE){
					ContentValues contentValues=new ContentValues();
					contentValues.put("id", pushMsg.getMsg().getMsgID());
					UserBase p_sender=pushMsg.getMsg().getSender();				
					StringBuilder p_sender_sb=new StringBuilder();				
					p_sender_sb.append(p_sender.getId()).append(",").append(p_sender.getNickname()).append(",")
					.append(p_sender.getRealName()).append(",").append(p_sender.getUserName());					
					contentValues.put("sender", p_sender_sb.toString());		
					contentValues.put("contentType", pushMsg.getMsg().getContentType());				
					contentValues.put("contentCode", pushMsg.getMsg().getContentCode());			
					contentValues.put("content", pushMsg.getMsg().getContent());		
					contentValues.put("time", pushMsg.getMsg().getTime());				
					contentValues.put("unread",IntentValue.UNREAD);				
					cEappApp.dbManager.insertData(TableName.SHORT_MSG, contentValues);	
				}
				
				
				
				
				
				cEappApp.getCurrentHandler().sendMessage(message);					
				
				PushMsgResp pushMsgResp=new PushMsgResp();
				pushMsgResp.setSequence(pushMsg.getSequence());
				pushMsgResp.setMsgID(pushMsg.getMsg().getMsgID());
				pushMsgResp.setStatus(0);
				pushMsgResp.setErrText("成功");
				session.write(pushMsgResp);
				
				break;
			case SocketDefine.PUSH_MSG_RESP://收到推送响应包	
				message=cEappApp.getCurrentHandler().obtainMessage(packet.getCmd(),packet);	
				cEappApp.getCurrentHandler().sendMessage(message);			
				PushMsgResp pushmsgResp=(PushMsgResp)packet;	
				if(clientSession.getAttachment() instanceof Msg){
					Msg msg = (Msg) clientSession.getAttachment();
					if (null != msg)
					{
						msg.setMsgID(pushmsgResp.getMsgID());			
						/**
						if(msg.getModule()==ModuleInteger.MESSAGE){
							ContentValues content=new ContentValues();
							content.put("id", msg.getMsgID());
							UserBase sender=msg.getSender();
							StringBuilder sender_sb=new StringBuilder();
							sender_sb.append(sender.getId()).append(",").append(sender.getNickname()).append(",")
							.append(sender.getRealName()).append(",").append(sender.getUserName());	
							content.put("sender", sender_sb.toString());
							content.put("contentType", msg.getContentType());
							content.put("contentCode", msg.getContentCode());
							content.put("content", msg.getContent());
							content.put("time", msg.getTime());
							content.put("unread",IntentValue.UNREAD);
							cEappApp.dbManager.insertData(TableName.SHORT_MSG, content);
						}
						*/					
					}
				}	
				break;
			case SocketDefine.DISCONNECT:
				break;
			case SocketDefine.DISCONNECT_RESP:
				message = cEappApp.getCurrentHandler().obtainMessage(packet.getCmd(), packet);
				cEappApp.getCurrentHandler().sendMessage(message);
				session.close();
				break;
			case SocketDefine.ACTIVE_TEST:
				ActiveTestResp activeTestResp = new ActiveTestResp();
				activeTestResp.setSequence(ClientSession.getSequenceID());
				session.write(activeTestResp);
				break;
			case SocketDefine.ACTIVE_TEST_RESP:
				break;
			
		}
	}

	public void packetSent(Session session, IPacket iPacket)
	{
		// TODO Auto-generated method stub
		Log.i(Constant.TAG_NETWORK_SOCKET, "packet Sent:".concat(iPacket.getName()));
	
		lastPacketCommand = ((Packet) iPacket).getCmd();
		
		iPacket = null;
	}

	public void sessionClosed(Session session)
	{
		// TODO Auto-generated method stub
		Log.i(Constant.TAG_NETWORK_SOCKET, "session Closed");

		ClientSession clientSession = (ClientSession) session;

		if (SocketDefine.DISCONNECT == lastPacketCommand || !NetUtil.checkNetWorkStatus(clientSession.getcEappApp().getCurrentActivity()))
		{
			return;
		}
		else
		{
			try
			{
				Thread.sleep(Constant.SOCKET_RECONNECT_INTERVAL);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try{
				clientSession.open();	
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}

	public void sessionIDLE(Session session)
	{
	}

	public void sessionOpened(Session session)
	{
		ClientSession clientSession = (ClientSession) session;
	
		Log.i(Constant.TAG_NETWORK_SOCKET, "session Opened");
		
		Connect connect = new Connect();
		connect.setUser(cEappApp.getUser());
		connect.setPass(cEappApp.getPass());		
		connect.setSequence(ClientSession.getSequenceID());
		
		session.write(connect);
	}

	public void sessionTimeout(Session session)
	{
		// TODO Auto-generated method stub
		if (SocketDefine.DISCONNECT == lastPacketCommand)
		{
			session.close();
		}
		else if (activeLostCount > 2)
		{
			session.close();
		}
		else if (loginSucess)
		{
			ActiveTest activeTest = new ActiveTest();
			activeTest.setSequence(ClientSession.getSequenceID());
			session.write(activeTest);
			activeLostCount++;
		}
	}

}
