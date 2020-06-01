package wwwordz.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

@RemoteServiceRelativePath("WWWordz")
public interface WWWordzService extends RemoteService {

	long timeToNextPlay();

	long register(String nick, String password) throws WWWordzException;

	Puzzle getPuzzle() throws WWWordzException;

	void setPoints(String nick, int points) throws WWWordzException;

	ArrayList<Rank> getRanking() throws WWWordzException;

}