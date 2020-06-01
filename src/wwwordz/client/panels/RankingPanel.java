package wwwordz.client.panels;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import wwwordz.client.WWWordzServiceAsync;
import wwwordz.shared.Configs;
import wwwordz.shared.Rank;
/**
 * Ranking panel containing the ranking table 
 *
 */
public class RankingPanel extends Composite {
	final VerticalPanel rankingPanel = new VerticalPanel();
	final Button restart = new Button("Join again");

	
	public RankingPanel(final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
		rankingPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); // CENTRAR NA PAGINA
		
		initWidget(rankingPanel);
	}
	/**
	 * get ranking from server and create the table
	 * @param panels
	 * @param wwwordzService
	 */
	public void getRanking(final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
		wwwordzService.getRanking(new AsyncCallback<ArrayList<Rank>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
			}

			@Override
			public void onSuccess(ArrayList<Rank> result) {
				int rankingSize = result.size();
				Grid g = new Grid(rankingSize,3);
				rankingPanel.add(g);
				for(int i=0; i<rankingSize; i++) {
					Rank rank = result.get(i);
					final Label name = new Label(rank.getNick());
					final Label roundPoints = new Label(rank.getPoints()+"");
					final Label accumulatedPoints = new Label(rank.getAccumulated()+"");
					name.setPixelSize(50, 50);
					roundPoints.setPixelSize(30, 50);
					accumulatedPoints.setPixelSize(30, 50);
					g.setWidget(i, 0, name);
					g.setWidget(i,1,roundPoints);
					g.setWidget(i,2,accumulatedPoints);
					restart(panels, wwwordzService);
					
				}
			}
			
			
		});
	}
	/**
	 * Enable the player to join the new round
	 * @param panels
	 * @param wwwordzService
	 */
	public void restart(final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
		restart.setEnabled(false);
		rankingPanel.add(restart);
		Timer timer = new Timer() {

			@Override
			public void run() {
				restart.setEnabled(true);
				restart.addClickHandler(restartClick(panels,wwwordzService));
			}
			
		};
		timer.schedule((int) Configs.RANKING_STAGE_DURATION);
	}
	
	/**
	 * Handler for the restart button
	 * Resets the panels
	 * @param panels
	 * @param wwwordzService
	 * @return
	 */
	private ClickHandler restartClick(final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
		ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				rankingPanel.clear();
				panels.remove(1);
				panels.insert(new GamePanel(panels,wwwordzService), 1);
				panels.showWidget(0);
			}
		};
		return handler;
	}
	
}
