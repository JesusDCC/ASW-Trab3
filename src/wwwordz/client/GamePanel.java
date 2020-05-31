package wwwordz.client;

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
import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.WWWordzException;



public class GamePanel extends Composite {
	final int defaultValue = 100;
	public int roundPoints = 0;
	public String currentWord = "";
	public ArrayList<Button> buttonsOfWord = new ArrayList<>();
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
						cell.addDoubleClickHandler(letterDoubleClick(nick,wwwordzService,puzzle,cell,i,k));
						cell.addClickHandler(letterClick(cell,i,k));
						cell.setPixelSize(50, 50);
						g.setWidget(i, k, cell);
					}
				}
			}
		});
	}
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
	
	//função p fazer setPoints
	public void setNewPoints(final String nick,final WWWordzServiceAsync wwwordzService,final int points) {
		wwwordzService.setPoints(nick,points, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("Congratulations! You got more "+ points + "points");
				
			}
			
		});

	}
	
	public int checkWord(final Puzzle puzzle) {
		List<Solution> solutions = puzzle.getSolutions();
		for(Solution s : solutions) {
			if(s.getWord().equals(currentWord)) {
				return s.getPoints();
			}
		}
		return 0;
	}
	
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
