package wwwordz.server;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import wwwordz.client.WWWordzService;
import wwwordz.game.Manager;
import wwwordz.game.Round;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;


public class WWWordzServiceImpl extends RemoteServiceServlet implements WWWordzService {
	static {
	    Round.setJoinStageDuration(Configs.JOIN_STAGE_DURATION);
	    Round.setPlayStageDuration(Configs.PLAY_STAGE_DURATION);
	    Round.setRankingStageSuration(Configs.RANKING_STAGE_DURATION);
	    Round.setReportStageDuration(Configs.REPORT_STAGE_DURATION);
	  }
	Manager manager = Manager.getInstance();
	

	
	@Override
	public long timeToNextPlay() {
		return manager.timeToNextPlay();
	}
	
	@Override
	public long register(String nick, String password) throws WWWordzException {
		return manager.register(nick, password);
	}
	
	@Override
	public Puzzle getPuzzle() throws WWWordzException {
		return manager.getPuzzle();	
	}
	
	@Override
	public void setPoints(String nick, int points) throws WWWordzException {
		manager.setPoints(nick, points);
	}
	
	@Override
	public List<Rank> getRanking() throws WWWordzException {
		return manager.getRanking();
	}
}
