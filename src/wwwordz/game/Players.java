package wwwordz.game;

import java.io.*;
import java.util.*;

import wwwordz.shared.WWWordzException;

public class Players implements java.io.Serializable {

	private static Players instance = null;
	ArrayList<Player> lista = new ArrayList<Player>();
	private static final long serialVersionUID = 1L;
	static File home = new File(System.getProperty("user.dir"));
	static File ficheiroSer = new File("dados.ser");
	
	public Players(){
		
	}

	public Player getPlayer(String nick) {
		instance = restore();
		Player aux = null;
		for (int i = 0; i < lista.size(); i++) {
			aux = lista.get(i);
			if (aux.nick == nick) {
				return aux;
			}
			aux = null;
		}
		return aux;
	}

	public void addPoints(String nick, int points) throws WWWordzException {
		Player aux;
		int flag = 0;
		for (int i = 0; i < lista.size(); i++) {
			aux = lista.get(i);
			if (aux.nick == nick) {
				flag = 1;
				lista.remove(i);
				aux.points = aux.points + points;
				lista.add(aux);
			}
		}
		
		if(flag == 0) //player nao encontrado
			throw new WWWordzException(" Player nao encontrado ");
		
		backup(instance);
	}

	public static File getHome() {
		String txt = System.getProperty("user.dir");
		File home = new File(txt);
		return home;
	}

	public static void setHome(File Home) {
		home = Home;
	}

	public void cleanup() {
		lista.clear();
		instance = restore();
	}

	public static Players getInstance() {
		if(instance == null){
			instance = new Players();
			return instance;		
		}
		return instance;
	}

	public void resetPoints(String nick) throws WWWordzException {
		Player aux;
		int flag = -1;
		for (int i = 0; i < lista.size(); i++) {
			aux = lista.get(i);
			if (aux.nick == nick) {
				lista.remove(i);
				aux.points = 0;
				lista.add(aux);
				flag = 1;
			}
		}
		
		if(flag == -1) //player nao encontrado
			throw new WWWordzException(" Player nao encontrado ");
		
		backup(instance);
		
		instance = restore();
	}

	public boolean verify(String nick, String password) {
		Player aux;
		int flag = -1;
		for (int i = 0; i < lista.size(); i++) {
			aux = lista.get(i);
			if (aux.nick.equals(nick)) {
				flag = 1;
				if (aux.password.equals(password)) {
					return true;
				} else {
					return false;
				}
			}
		}

		if(flag == -1) {
			Player novo = new Player(nick, password);
			lista.add(novo);
			return true;
		}
		return true;
	}

	private static Players restore() {
		Players players = null;

		if (ficheiroSer.canRead()) {
			try (FileInputStream stream = new FileInputStream(ficheiroSer);
					ObjectInputStream deserializer = new ObjectInputStream(stream);) {
				players = (Players) deserializer.readObject();
			} catch (IOException | ClassNotFoundException cause) {
				cause.printStackTrace();
			}
		} else {
			players = new Players();
		}
		return players;
	}

	private static void backup(Players instance) {
		try (
				FileOutputStream stream = new FileOutputStream(ficheiroSer);
				ObjectOutputStream serializer = new ObjectOutputStream(stream);
				) {
				serializer.writeObject(instance);
		} catch (IOException cause) {
			cause.printStackTrace();
		}
	}

}