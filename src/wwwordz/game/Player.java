package wwwordz.game;

public class Player {
	
	String nick;
	String password;
	int points;
	int accumulated;
	
	Player(String nick, String password){
		this.nick = nick;
		this.password = password;
	}
	
	public int getAccumulated() {
		return this.accumulated + this.points;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public String getNick() {
		return this.nick;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public void setAccumulated(int accumulated) {
		this.accumulated = accumulated;
	}

}

















