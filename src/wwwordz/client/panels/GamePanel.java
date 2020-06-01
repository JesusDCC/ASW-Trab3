package wwwordz.client.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.WWWordzException;
import wwwordz.client.WWWordzServiceAsync;
import wwwordz.puzzle.*;


/**
 * GamePanel class, the panel containing the gameplay stuff
 *
 */
public class GamePanel extends Composite {
	final int defaultValue = 100;
	public int roundPoints = 0;
	public String currentWord = "";
	public ArrayList<Button> buttonsOfWord = new ArrayList<>();
	public ArrayList<Button> buttons = new ArrayList<>();
	public int lastRow = defaultValue;
	public int lastCol = defaultValue;
	public int clickCount=0;

	
	final Grid g = new Grid(4,4);

	final VerticalPanel gamePanel = new VerticalPanel();

	public GamePanel(final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
		gamePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); // CENTRAR NA PAGINA
		gamePanel.add(new HTML("Game Board"));
		initWidget(gamePanel);
		gamePanel.add(g);
	   		
	}
	/**
	 * Create the grid with the letters from the puzzle on it
	 * @param nick
	 * @param panels
	 * @param wwwordzService
	 */
	public void fillGame(final String nick,final DeckPanel panels, final WWWordzServiceAsync wwwordzService) {
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
						buttons.add(cell);
						cell.addDoubleClickHandler(letterDoubleClick(nick,wwwordzService,puzzle,cell,i,k));
						cell.addClickHandler(letterClick(cell,i,k));
						cell.setPixelSize(50, 50);
						g.setWidget(i, k, cell);
					}
				}
			}
		});
	}
	
	/**
	 * handler for single clicking in the grid cells
	 * @param cell
	 * @param row
	 * @param col
	 * @return
	 */
	private ClickHandler letterClick(final Button cell,final int row, final int col) {
		ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clickCount++;
				if(clickCount==1) {
					Timer timer = new Timer() {
						@Override
						public void run() {
							onClickAction(cell,row,col);
						}
					};
					timer.schedule(200);
				}
				
			}
		};
		return handler;
	}
	/**
	 * actions to do when single clicking grid cells
	 * @param cell
	 * @param row
	 * @param col
	 */
	public void onClickAction(final Button cell, final int row, final int col) {
		if(clickCount==1) {
			if(isNeighbour(row,col)) {
				buttonsOfWord.add(cell);
				cell.setEnabled(false);
				currentWord += cell.getText();
				lastCol = col;
				lastRow = row;
				clickCount = 0;
			}
			else {
				Window.alert("No cheating");
			}
		}
		
	}
	/**
	 * handler for double clicking grid cells
	 * @param nick
	 * @param wwwordzService
	 * @param puzzle
	 * @param cell
	 * @param row
	 * @param col
	 * @return
	 */
	private DoubleClickHandler letterDoubleClick(final String nick,final WWWordzServiceAsync wwwordzService,final Puzzle puzzle,final Button cell, final int row, final int col) {
		DoubleClickHandler handler = new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(isNeighbour(row,col)) {
					currentWord += cell.getText();
					lastCol = defaultValue;
					lastRow = defaultValue;
					int points = checkWord(puzzle);
					Window.alert("You found a " + points+" word");
					roundPoints += points;
					currentWord = "";
					for(Button b : buttonsOfWord) {
						b.setEnabled(true);
					}
					clickCount = 0;
				}
				else {
					Window.alert("No cheating");
				}
				
			}
		};
		return handler;
	}
	
	/**
	 * start the report stage 
	 * @param nick
	 * @param panels
	 * @param wwwordzService
	 */
	public void initReport(final String nick,final DeckPanel panels,final WWWordzServiceAsync wwwordzService) {
		lastCol = defaultValue;
		lastRow = defaultValue;
		Window.alert("Reporting");
		for(Button b : buttons) {
			b.setEnabled(false);
		}
		setNewPoints(nick,wwwordzService,roundPoints);
		Long rT = Configs.REPORT_STAGE_DURATION;
		int reportTime = rT.intValue();
		Timer reportTimer = new Timer() {

			@Override
			public void run() {
				panels.showWidget(2);
				((RankingPanel) panels.getWidget(2)).getRanking(panels,wwwordzService);
			}
			
		};
		reportTimer.schedule(reportTime);
	}
	
	
	
	/**
	 * set new points obtained in the round
	 * @param nick
	 * @param wwwordzService
	 * @param points
	 */
	public void setNewPoints(final String nick,final WWWordzServiceAsync wwwordzService,final int points) {
		wwwordzService.setPoints(nick,points, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("Congratulations! You got more "+ points + " points");
				
			}
			
		});

	}
	/**
	 * check worth of the word found
	 * @param puzzle
	 * @return
	 */
	public int checkWord(final Puzzle puzzle) {
		List<Solution> solutions = puzzle.getSolutions();
		for(Solution s : solutions) {
			if(s.getWord().equals(currentWord)) {
				return s.getPoints();
			}
		}
		return 0;
	}
	
	/**
	 * verify if the cell clicked is adjacent to the last cell clicked
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isNeighbour(int row,int col) {
		if(lastRow==defaultValue) return true;
		if(row == lastRow) {
			if(col == lastCol + 1 || col == lastCol-1) {
				return true;
			}
		}
		else if(col == lastCol ) {
			if(row == lastRow + 1 || row == lastRow - 1) {
				return true;
			}
		}
		else if(col == lastCol + 1) {
			if(row == lastRow + 1 || row == lastRow - 1) {
				return true;
			}
		}
		else if(col == lastCol - 1) {
			if(row == lastRow + 1 || row == lastRow - 1) {
				return true;
			}
		} 
		
		return false;
	}
	
}
