package wwwordz.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public interface WWWordzServiceAsync {

	void timeToNextPlay(AsyncCallback<Long> callback) ;

	void register(String nick, String password, AsyncCallback<Long> callback) ;	
	
	void getPuzzle(AsyncCallback<Puzzle> callback) ;

	void setPoints(String nick, int points, AsyncCallback<Void> callback) ;

	void getRanking(AsyncCallback<ArrayList<Rank>> callback);

}