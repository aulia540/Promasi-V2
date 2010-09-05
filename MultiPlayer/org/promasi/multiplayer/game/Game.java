/**
 *
 */
package org.promasi.multiplayer.game;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NullArgumentException;
import org.promasi.multiplayer.ProMaSiClient;
import org.promasi.network.protocol.dtos.GameDto;

/**
 * @author m1cRo
 *
 */
public class Game implements Runnable
{
	/**
	 *
	 */
	private String _gameId;

	/**
	 *
	 */
	private ProMaSiClient _gameMaster;

	/**
	 *
	 */
	private Map<ProMaSiClient,GameModel> _gameModels;
	
	/**
	 * 
	 */
	private GameModel _gameModel;
	
	/**
	 * 
	 */
	private Thread _gameThread;
	
	/**
	 * 
	 */
	private boolean _isRunning;
	
	/**
	 *
	 * @param gameId
	 * @param gameMaster
	 * @param promasiModel
	 * @throws NullArgumentException
	 */
	public Game(String gameId,ProMaSiClient gameMaster,GameModel gameModel)throws NullArgumentException,IllegalArgumentException
	{
		if(gameId==null)
		{
			throw new NullArgumentException("Wrong argument gameId==null");
		}

		if(gameMaster==null)
		{
			throw new NullArgumentException("Wrong argument gameMaster==null");
		}

		if(gameModel==null)
		{
			throw new NullArgumentException("Wrong argument gameModel==null");
		}
		
		_isRunning=false;
		_gameId=gameId;
		_gameMaster=gameMaster;
		_gameModels=new HashMap<ProMaSiClient,GameModel>();
		_gameModel=gameModel;
	}

	/**
	 *
	 * @return
	 */
	public synchronized String getGameId()
	{
		return _gameId;
	}

	/**
	 * @return 
	 * 
	 */
	public synchronized boolean isRunning()
	{
		return _isRunning;
	}
	
	/**
	 *
	 * @param playerId
	 * @throws NullArgumentException
	 * @throws IllegalArgumentException
	 */
	public synchronized boolean addPlayer(ProMaSiClient player)throws NullArgumentException,IllegalArgumentException
	{
		if(player==null)
		{
			throw new NullArgumentException("Wrong argument playerId==null");
		}

		if(_gameModels.containsKey(player) || _gameMaster.getClientId()==player.getClientId())
		{
			throw new IllegalArgumentException("Wrong argument playerId is already in game");
		}
		
		if(_isRunning)
		{
			return false;
		}
		
		//Convert to xml and back to clone object.
		XMLDecoder decoder=new XMLDecoder(new ByteArrayInputStream(_gameModel.toXmlString().getBytes()));
		GameModel gameModel=(GameModel) decoder.readObject();
		_gameModels.put(player, gameModel);
		return true;
	}

	/**
	 *
	 * @return
	 */
	public synchronized ProMaSiClient getGameMaster()
	{
		return _gameMaster;
	}

	/**
	 *
	 * @param client
	 * @return
	 */
	public synchronized boolean isGameMaster(ProMaSiClient client)
	{
		return client==_gameMaster;
	}

	/**
	 *
	 * @param message
	 * @return
	 */
	public synchronized boolean startGame(String message)
	{
		if(_gameModels.size()<2)
		{
			return false;
		}
			
		for(Map.Entry<ProMaSiClient,GameModel> entry:_gameModels.entrySet())
		{
			entry.getKey().onReceiveData(message);
		}

		_isRunning=true;
		_gameThread=new Thread(this);
		_gameThread.start();
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized boolean stopGame()
	{
		if(!_isRunning)
		{
			return false;
		}
		
		_isRunning=false;
		
		return true;
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	public synchronized HashMap<String,Double> setGameValues(HashMap<String,Double> values)
	{
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized HashMap<String,Double> getGameValues()
	{
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized GameDto getGameDto()
	{
		return new GameDto(_gameModel.getCompany(),_gameId,_gameModel.getGameDescription(),_gameModels.size());
	}

	@Override
	public void run() 
	{
		while(_isRunning)
		{
			try 
			{
				for(Map.Entry<ProMaSiClient,GameModel> entry:_gameModels.entrySet())
				{
					entry.getValue().executeStep();
				}
				
				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
