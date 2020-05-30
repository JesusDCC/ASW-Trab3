package wwwordz.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import wwwordz.shared.Puzzle;
import wwwordz.shared.WWWordzException;



public class GamePanel extends Composite {
	
	final Grid g = new Grid(4,4);

	final VerticalPanel gamePanel = new VerticalPanel();

	public GamePanel(final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
		gamePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); // CENTRAR NA PAGINA
		gamePanel.add(new HTML("Game Board"));
		initWidget(gamePanel);
		gamePanel.add(g);
	   		
	}
	
	public void fillGame(final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
		wwwordzService.getPuzzle(new AsyncCallback<Puzzle>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.toString());

			}
			@Override
			public void onSuccess(Puzzle puzzle) {
				for(int i=0;i<4;i++) {
					for(int k=0;k<4;k++) {
						String c = String.valueOf(puzzle.getTable().getLetter(i+1, k+1));
						final Button cell = new Button(c);
						cell.setPixelSize(20, 20);
						g.setWidget(i, k, cell);
					}
				}


			}
		});
	}
	
}
