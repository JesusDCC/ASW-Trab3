package wwwordz.client.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;	
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.client.WWWordzServiceAsync;
import wwwordz.shared.*;
import wwwordz.shared.Configs;

import wwwordz.shared.WWWordzException;
/**
 * Login panel to register to the round 
 *
 */
public class LoginPanel extends Composite{
	
	final VerticalPanel loginPanel = new VerticalPanel(); // IMPORTANTE
	final Button loginButton = new Button("Login");
	final TextBox nameField = new TextBox();
	final PasswordTextBox passField = new PasswordTextBox();
	public String user = "";
	public String pass = "";
	
	public LoginPanel(final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
		loginPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); // CENTRAR NA PAGINA
		
		loginPanel.add(new HTML("username"));
		loginPanel.add(nameField);
		loginPanel.add(new HTML("password"));
		loginPanel.add(passField);
		loginPanel.add(loginButton);
		
		initWidget(loginPanel); // IMPORTANTE
		
		loginButton.addClickHandler(loginClick(panels,wwwordzService));
		
	}
	
	/**
	 * Handler for clicking login button
	 * @param panels
	 * @param wwwordzService
	 * @return
	 */
	private ClickHandler loginClick(final DeckPanel panels,final WWWordzServiceAsync wwwordzService) {
		ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Waiting");
				loginButton.setEnabled(false);
				Timer timer = new Timer() {

					@Override
					public void run() {
						loginButton.setEnabled(true);
					}
					
				};
				timer.schedule((int) Configs.JOIN_STAGE_DURATION*2);
				loginToServer(panels,wwwordzService);  
			}
		};
		return handler;
	}
	/**
	 * send login to the server and start the game
	 * @param panels
	 * @param wwwordzSerivce
	 */
	public void loginToServer(final DeckPanel panels,final WWWordzServiceAsync wwwordzSerivce){
		this.user = nameField.getText();
		this.pass = passField.getText();
		wwwordzSerivce.register(user,pass, new AsyncCallback<Long>() {
			public void onFailure(Throwable caught) {
				//loginToServer(panels,wwwordzSerivce);
				Window.alert(caught.toString());	


			}
			@Override
			public void onSuccess(Long result) {
				Long time = Configs.PLAY_STAGE_DURATION;
				final int reportTiming = time.intValue();
				Timer t = new Timer() {
					@Override
					public void run() {
						((GamePanel) panels.getWidget(1)).fillGame(user,panels,wwwordzSerivce);
						panels.showWidget(1);
						Timer timer = new Timer() {

							@Override
							public void run() {
								((GamePanel) panels.getWidget(1)).initReport(user,panels,wwwordzSerivce);
							}
							
						};
						
						timer.schedule(reportTiming);
					}
				};

				// Schedule the timer to run once in 5 seconds.
				t.schedule(result.intValue());
			}	

		});
	}

}