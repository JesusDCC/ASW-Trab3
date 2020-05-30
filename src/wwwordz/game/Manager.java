package wwwordz.game;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import wwwordz.client.WWWordzService;
import wwwordz.game.Round;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public class Manager implements WWWordzService {
	
	static final long INITIAL_TIME = 0;
	Round round;
	private static java.util.concurrent.ScheduledExecutorService worker = Executors.newScheduledThreadPool(1);	
	private static Manager instance = null;
	
	private Manager() {
		round = new Round();
		worker.scheduleAtFixedRate(()->{
	            System.out.println("hi there at: "+ new java.util.Date());
	            round = new Round();
	        }, Round.getRoundDuration(), Round.getRoundDuration() , TimeUnit.MILLISECONDS);
	}
	public static Manager getInstance() {
		if(instance == null){
			instance = new Manager();
			return instance;		
		}
		return instance;	
	}
	
	@Override
	public long timeToNextPlay() {
		return round.getTimetoNextPlay();
	}
	
	@Override
	public long register(String nick, String password) throws WWWordzException {
		return round.register(nick, password);
	}
	
	@Override
	public Puzzle getPuzzle() throws WWWordzException {
		return round.getPuzzle();	
	}
	
	@Override
	public void setPoints(String nick, int points) throws WWWordzException {
		round.setPoints(nick, points);
	}
	
	@Override
	public List<Rank> getRanking() throws WWWordzException {
		return round.getRanking();
	}

}