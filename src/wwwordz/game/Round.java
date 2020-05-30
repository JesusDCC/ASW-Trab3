package wwwordz.game;

import wwwordz.puzzle.Generator;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;
import java.util.*;

public class Round {
	
	long startTime = System.currentTimeMillis();
	
	Date end;
	static Date join;
	static Date play;
	static Date report;
	static Date ranking;
	
	Map<String, Player> roundPlayers = new HashMap<>();
	List<Rank> lista = new ArrayList<Rank>();
	
	Generator generator = new Generator();
	Puzzle puzzle = generator.random();

	public static long getJoinStageDuration() {
		return join.getTime();
	}
	
	public static void setJoinStageDuration(long joinStageDuration) {
		join = new Date(joinStageDuration);
	}
	
	public static long getPlayStageDuration() {
		return play.getTime();
	}
	
	public static void setPlayStageDuration(long playStageDuration) {
		play = new Date(playStageDuration);
	}
	
	public static long getReportStageDuration() {
		return report.getTime();
	}
	
	public static void setReportStageDuration(long reportStageDuration) {
		report = new Date(reportStageDuration);
	}
	
	public static long getRankingStageSuration() {
		return ranking.getTime();
	}
	
	public static void setRankingStageSuration(long rankingStageDuration) {
		ranking = new Date(rankingStageDuration);
	}
	
	public static long getRoundDuration() {
		long total = getJoinStageDuration() + getPlayStageDuration() + getReportStageDuration() + getRankingStageSuration();
		return total;
	}

	public long getTimetoNextPlay() {
		
		long curTime = System.currentTimeMillis();
		 
		if(curTime < getPlayStageDuration() + startTime) {
			return (getPlayStageDuration() + startTime) - curTime;
		}
		
		return startTime + getRoundDuration() - curTime +  getJoinStageDuration();
	}

	public long register(String nick, String password) throws WWWordzException {
		long aux = System.currentTimeMillis();
		Players p = Players.getInstance();
		
		if(aux > getJoinStageDuration() + startTime) {
			throw new WWWordzException("  nao esta na fase de join ");
		}
		
		if(p.verify(nick, password) != true) {
			throw new WWWordzException(" nick e password nao correspondem ");
		}
		
		Player paux = new Player(nick, password);
		roundPlayers.put(nick, paux);
		
		return startTime + getRoundDuration() - aux;
	}

	public Puzzle getPuzzle() throws WWWordzException {
		
		long aux = System.currentTimeMillis();
		
		if(aux < startTime + getJoinStageDuration() || aux > startTime + getJoinStageDuration() + getPlayStageDuration()) {
			throw new WWWordzException(" not in play stage ");
		} 
		
		return puzzle;
	}

	public void setPoints(String nick, int i) throws WWWordzException{
		long aux = System.currentTimeMillis();
		
		if(aux > startTime + getJoinStageDuration() + getPlayStageDuration() +  getReportStageDuration() || aux < startTime + getJoinStageDuration() + getPlayStageDuration()) {
			throw new WWWordzException("  not in report stage ");
		}
		
		Player paux = roundPlayers.get(nick);
		roundPlayers.remove(nick);
		paux.points = i;
		roundPlayers.put(nick, paux);	
		
		Rank rank = new Rank(paux.nick, paux.points, paux.accumulated);
		lista.add(rank);

		Collections.sort(lista, Collections.reverseOrder());
		
	}

	public List<Rank> getRanking() throws WWWordzException{
		long aux = System.currentTimeMillis();

		if(aux < startTime + getJoinStageDuration() + getPlayStageDuration() + getReportStageDuration() ) {
			throw new WWWordzException("  not in ranking stage ");
		}
		
		return lista;
	}
	
	
}
