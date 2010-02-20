/**
 *
 */
package org.promasi.server.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NullArgumentException;

/**
 * @author m1cRo
 *
 */
public class UserList
{
	private Map<String,ProMaSiClient> _users;

	public UserList()
	{
		_users=new HashMap<String,ProMaSiClient>();
	}

	public void addUser(String clientId,ProMaSiClient client)throws IllegalArgumentException,NullArgumentException
	{
		if(clientId==null)
		{
			throw new NullArgumentException("Wrong argument userId==mull");
		}

		if(client==null)
		{
			throw new NullArgumentException("Wrong argument client==null");
		}
		synchronized(_users)
		{
			if(_users.containsKey(clientId))
			{
				throw new IllegalArgumentException("Wrong argument userId is already in list");
			}
			_users.put(clientId,client);
		}
	}

	public void removeUser(String clientId)throws IllegalArgumentException,NullArgumentException
	{
		if(clientId==null)
		{
			throw new NullArgumentException("Wrong argument userId");
		}
		synchronized(_users)
		{
			if(!_users.containsKey(clientId))
			{
				throw new IllegalArgumentException("Wrong argument userId");
			}
			_users.remove(clientId);
		}
	}

	public boolean isUserInList(String clientId)
	{
		synchronized(this)
		{
			return _users.containsKey(clientId);
		}
	}
}