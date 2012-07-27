package gdw.network.client;

import gdw.network.NETCONSTANTS;
import gdw.network.RESPONSECODES;
import gdw.network.server.GDWServerLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

public class ConnectionResponceThread extends Thread
{

	private final SocketChannel tcpSocket;

	private final DatagramChannel udpSocket;

	private final ByteBuffer buf;

	private final ServerInfo info;

	private boolean pending;
	
	private final ServerInfo  server;
	

	public ConnectionResponceThread(SocketChannel tcpSocket,
			DatagramChannel udpSocket, ByteBuffer buf, ServerInfo info)
	{
		this.tcpSocket = tcpSocket;
		try
		{
			this.tcpSocket.socket().setTcpNoDelay(true);
			this.tcpSocket.socket().setKeepAlive(true);
		} catch (SocketException e)
		{

		}
		this.udpSocket = udpSocket;
		this.buf = buf;
		this.info = info;
		this.pending = true;
		this.server = info;
		new ConnetionResponceThreadTimeoutHelper(this);
		this.start();
	}
	


	@Override
	public void run()
	{
		IBasicClientListener lis = BasicClient.getListener();
		try
		{

			// tcp blocking
			tcpSocket.configureBlocking(true);
			tcpSocket.connect(new InetSocketAddress(info.address, info.port));
			lis.connectionUpdate(RESPONSECODES.HANDSHAKE);

			// send buf
			while(this.buf.hasRemaining())
				tcpSocket.write(this.buf);

			// wait for responce
			ByteBuffer responce = ByteBuffer
					.allocate(NETCONSTANTS.PACKAGELENGTH);

			try
			{
				tcpSocket.read(responce);
			} catch (IOException e)
			{
				lis.connectionUpdate(RESPONSECODES.TIMEOUT);
				return;
			}

			// get responce
			responce.position(0);
			byte resCode = responce.get();
			lis.connectionUpdate(resCode);
			if (resCode != RESPONSECODES.OK)
			{
				// clean up
				lis.connectionUpdate(resCode);
				this.tcpSocket.close();
				this.udpSocket.close();
				return;
			}

			tcpSocket.configureBlocking(false);
			
			int udpPort = responce.getInt();
			int id = responce.getInt();
			int secret = responce.getInt();

			GDWServerLogger.logMSG("Client soll zu UDPPort: "+udpPort+" vebinden");
			
			//udp socket
			udpSocket.connect(new InetSocketAddress(tcpSocket.socket()
					.getInetAddress(), udpPort));

			
			// create client
			this.pending = false;
			BasicClient.registerClient(tcpSocket, udpSocket, id, secret, server );
		} catch (IOException e)
		{
			lis.connectionUpdate(RESPONSECODES.UNREACHABLE);
			// clean up

			try
			{
				this.tcpSocket.close();
				this.udpSocket.close();
			} catch (IOException e1)
			{

			}
			
			return;
		}
	}

	public boolean isPending()
	{
		return pending;
	}

}
