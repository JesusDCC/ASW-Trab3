package wwwordz.client;

import com.google.gwt.thirdparty.javascript.rhino.head.ast.Label;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPanel extends Composite{
	
	final VerticalPanel loginPanel = new VerticalPanel(); // IMPORTANTE
	final Button loginButton = new Button("Login");
	final TextBox nameField = new TextBox();
	final PasswordTextBox passField = new PasswordTextBox();
	final Label errorLabel = new Label();
	final Label waitingLabel = new Label();
	public String user = "";
	public String pass = "";
	
	public LoginPanel(final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
		loginPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); // CENTRAR NA PAGINA
		errorLabel.addStyleName("gwt-error");
		
		loginPanel.add(new HTML("username"));
		loginPanel.add(nameField);
		loginPanel.add(new HTML("password"));
		loginPanel.add(passField);
		loginPanel.add(loginButton);
		loginPanel.add(errorLabel);
		loginPanel.add(waitingLabel);

		initWidget(loginPanel); // IMPORTANTE
		
		loginButton.addClickHandler(loginClick(panels,wwwwordzService));
		nameField.addKeyUpHandler(loginEnter(panels,wwwordzService));
		passField.addKeyUpHandler(loginEnter(panels,wwwordzService));
	}
	

}