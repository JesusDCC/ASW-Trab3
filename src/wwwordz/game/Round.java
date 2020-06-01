package wwwordz.game;

import wwwordz.puzzle.Generator;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;
import java.util.*;

public class Round {
	
	long startTime = System.currentTimeMillis();
	private java.util.Date joinn = new Date();
	private java.util.Date playy = new Date(joinn.getTime()   + getJoinStageDuration());
	private java.util.Date reportt = new Date(playy.getTime()   + getPlayStageDuration());
	private java.util.Date rankingg = new Date(reportt.getTime() + getReportStageDuration());
	private java.util.Date endd = new Date(rankingg.getTime()+ getRankingStageSuration());
	static Date join;
	static Date play;
	static Date report;
	static Date ranking;
	
	Map<String, Player> roundPlayers = new HashMap<>();
	ArrayList<Rank> lista = new ArrayList<Rank>();
	
	Generator generator = new Generator();
	Puzzle puzzle = generator.generate();
	
	public Round() {
		puzzle = generator.generate();
	}

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
		Date temp = new Date(); 
		if(temp.before(playy)) {
			return playy.getTime()-temp.getTime();
		}
		
		return endd.getTime()-temp.getTime()+ getJoinStageDuration();
	}

	public long register(String nick, String password) throws WWWordzException {
		long aux = System.currentTimeMillis();
		Players p = Players.getInstance();
		Date temp = new Date();
		
		if(temp.after(playy)) {
			throw new WWWordzException("  nao esta na fase de join ");
		}
		
		if(p.verify(nick, password) != true) {
			throw new WWWordzException(" nick e password nao correspondem ");
		}
		
		Player paux = new Player(nick, password);
		roundPlayers.put(nick, paux);
		
		return playy.getTime()-temp.getTime();
	}

	public Puzzle getPuzzle() throws WWWordzException {
		
		long aux = System.currentTimeMillis();
		Date temp = new Date();
		if(temp.before(playy) || temp.after(reportt)) {
			throw new WWWordzException(" not in play stage ");
		} 
		
		return puzzle;
	}

	public void setPoints(String nick, int i) throws WWWordzException{
		long aux = System.currentTimeMillis();
		Date temp = new Date();
		if(temp.before(reportt) || temp.after(rankingg)) {
			throw new WWWordzException("  not in report stage ");
		}
		
		Player paux = roundPlayers.get(nick);
		roundPlayers.remove(nick);
		paux.points = i;
		paux.accumulated += i;
		roundPlayers.put(nick, paux);	
		
		Rank rank = new Rank(paux.nick, paux.points, paux.accumulated);
		lista.add(rank);

		Collections.sort(lista, Collections.reverseOrder());
		
	}

	public ArrayList<Rank> getRanking() throws WWWordzException{
		long aux = System.currentTimeMillis();
		Date temp = new Date();
		if( temp.before(rankingg)) {
			throw new WWWordzException("  not in ranking stage ");
		}
		
		return lista;
	}
	
	
}
