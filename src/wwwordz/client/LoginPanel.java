package wwwordz.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.thirdparty.javascript.rhino.head.ast.Label;
import com.google.gwt.user.client.Timer;	
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.shared.WWWordzException;

public class LoginPanel extends Composite{
	
	final VerticalPanel loginPanel = new VerticalPanel(); // IMPORTANTE
	final Button loginButton = new Button("Login");
	final TextBox nameField = new TextBox();
	final PasswordTextBox passField = new PasswordTextBox();
	//final Label errorLabel = new Label();
	//final Label waitingLabel = new Label();
	public String user = "";
	public String pass = "";
	
/*
	public String getUser() {
		return this.user;
	}
	public String getPass() {
		return this.pass;
	}
	*/
	
	public LoginPanel(final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
		loginPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); // CENTRAR NA PAGINA
		
		loginPanel.add(new HTML("username"));
		loginPanel.add(nameField);
		loginPanel.add(new HTML("password"));
		loginPanel.add(passField);
		loginPanel.add(loginButton);
		//loginPanel.add(errorLabel);
		
		initWidget(loginPanel); // IMPORTANTE
		
		loginButton.addClickHandler(loginClick(panels,wwwordzService));
		//nameField.addKeyUpHandler(loginEnter(panels,wwwordzService));
		//passField.addKeyUpHandler(loginEnter(panels,wwwordzService));
	}
	
	private ClickHandler loginClick(final DeckPanel panels,final WWWordzServiceAsync wwwordzService) {
		ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loginPanel.add(new HTML("Waiting"));
					loginToServer(panels,wwwordzService);  
			}
		};
		return handler;
	}
	
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
				Timer t = new Timer() {
					@Override
					public void run() {
						((GamePanel) panels.getWidget(1)).fillGame(user,panels,wwwordzSerivce);
						panels.showWidget(1); 
					}
				};

				// Schedule the timer to run once in 5 seconds.
				t.schedule(result.intValue());
			}	

		});
	}

}