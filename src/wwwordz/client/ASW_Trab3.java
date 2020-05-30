package wwwordz.client;

import wwwordz.shared.FieldVerifier;
import wwwordz.shared.WWWordzException;
import wwwordz.server.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import wwwordz.client.LoginPanel;
import wwwordz.client.GamePanel;
/**
 * TO DO
 * CRIAR DIV NO HTML P PAINEL COM TODOS OS PAINEIS
 * HA UMA CENA P MOSTRAR UM PAINEL ESPECIFICO
 * CLASS LOGIN
 * CLASS RANKING
 * CLASS GAME
 */




/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ASW_Trab3 implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final WWWordzServiceAsync wwwordzService = GWT.create(WWWordzService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final DeckPanel panels = new DeckPanel(); 
		RootPanel.get("panel").add(panels);
		final LoginPanel login = new LoginPanel(panels,wwwordzService);
		final GamePanel game = new GamePanel(panels,wwwordzService); 
		panels.add(login);
		panels.add(game);
		panels.showWidget(0);
		

	}
}
