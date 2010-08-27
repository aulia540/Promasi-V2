package org.promasi.multiplayer;

import org.apache.commons.lang.NullArgumentException;
import org.promasi.multiplayer.server.ProMaSi;
import org.promasi.network.tcp.ITcpEventHandler;
import org.promasi.network.tcp.TcpClient;

/**
 * @author m1cRo
 *
 */
public class TcpEventHandler implements ITcpEventHandler
{
	/**
	 * 
	 */
	private ProMaSi _promasi;

	/**
	 * 
	 */
	public TcpEventHandler()
	{
		_promasi=new ProMaSi();
	}

	/* (non-Javadoc)
	 * @see org.promasi.server.ITCPEventHandler#OnConnect(org.promasi.server.TCPClient)
	 */
	@Override
	public boolean onConnect(TcpClient tcpClient)throws NullArgumentException
	{
		if(tcpClient==null)
		{
			throw new NullArgumentException("Wrong argument tcpClient==null");
		}
		System.out.print("Connected \n");
		_promasi.onConnect(tcpClient);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.promasi.server.ITCPEventHandler#OnConnectionError(org.promasi.server.TCPClient)
	 */
	@Override
	public void onConnectionError(TcpClient tcpClient)throws NullArgumentException
	{
		if(tcpClient==null)
		{
			throw new NullArgumentException("Wrong argument tcpClient==null");
		}

		try
		{
			System.out.print("Connection error \n");
			_promasi.onConnectionError(tcpClient);
		}
		catch(IllegalArgumentException e)
		{
			System.out.print(e.getMessage()+"\n");
		}
	}

	/* (non-Javadoc)
	 * @see org.promasi.server.ITCPEventHandler#OnDisconnect(org.promasi.server.TCPClient)
	 */
	@Override
	public void onDisconnect(TcpClient tcpClient)throws NullArgumentException
	{
		if(tcpClient==null)
		{
			throw new NullArgumentException("Wrong argument tcpClient==null");
		}

		try
		{
			System.out.print("Disconnected \n");
			_promasi.onDisconnect(tcpClient);
		}
		catch(IllegalArgumentException e)
		{
			System.out.print(e.getMessage()+"\n");
		}
	}

	/* (non-Javadoc)
	 * @see org.promasi.server.ITCPEventHandler#OnReceive(org.promasi.server.TCPClient, java.lang.String)
	 */
	@Override
	public boolean onReceive(TcpClient tcpClient, String line)throws NullArgumentException
	{
		if(tcpClient==null)
		{
			throw new NullArgumentException("Wrong argument tcpClient==null");
		}

		if(line==null)
		{
			throw new NullArgumentException("Wrong argument line==null");
		}

		try
		{
			System.out.print("Received - "+line+"\n");
			_promasi.onReceiveData(tcpClient,line);
		}
		catch(IllegalArgumentException e)
		{
			System.out.print(e.getMessage());
		}
		return true;
	}
}