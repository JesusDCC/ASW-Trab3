package wwwordz.shared;

import java.io.Serializable;

public class Rank implements Serializable, Comparable<Rank>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int accumulated;
	private String nick;
	private int points;
	
	public Rank() {
		
	}
	public Rank(String nick, int points, int accumulated) {
		this.accumulated = accumulated;
		this.nick = nick;
		this.points = points;
	}
	public int getAccumulated() {
		return this.accumulated;
	}
	public String getNick() {
		return this.nick;
	}
	public Integer getPoints() {
		return this.points;
	}
	public void setAccumulated(int accumulated) {
		this.accumulated = accumulated;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	@Override
	public int compareTo(Rank o) {
		return this.getPoints().compareTo(o.getPoints());
	}
}