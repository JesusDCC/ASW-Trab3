package wwwordz.puzzle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Table;

public class Generator {
	private static final int TABLESIZE = 4;

	/**
	 * Generates a puzzle with random characters in each Cell
	 * @return Puzzle
	 */
	public Puzzle random()  {
		Table table = new Table();
		for(int i=1;i<=TABLESIZE;i++) {
			for(int k=1;k<=TABLESIZE;k++) {
				Random r = new Random();
				char c = (char)(r.nextInt(26) + 'A');				
				table.setLetter(i, k, c);
			}
		}
		Puzzle puzzle = new Puzzle();
		List<Puzzle.Solution> solutions = getSolutions(table);
		puzzle.setTable(table);
		puzzle.setSolutions(solutions);

		return puzzle;

	}
	/**
	 * Generates a puzzle with random large words 
	 * @return Puzzle
	 * @throws IOException
	 */
	public Puzzle generate()  {
		Dictionary dic = Dictionary.getInstance();
		Table table = new Table();
		List<Table.Cell> emptyCells = table.getEmptyCells();
		while(emptyCells.size()>0) {
			Random rand = new Random(); 
			int random = rand.nextInt(emptyCells.size());
			Table.Cell cell = emptyCells.get(random);
			String word = dic.getRandomLargeWord();
			cell.setLetter(word.charAt(0));
			for(int i=1;i<word.length();i++) {
				List<Table.Cell> emptyNeighbors = getEmptyNeighbors(table.getNeighbors(cell));
				if(emptyNeighbors.size()==0)break;
				random = rand.nextInt(emptyNeighbors.size());
				cell = emptyNeighbors.get(random);
				cell.setLetter(word.charAt(i));
			}
			emptyCells = table.getEmptyCells();
		}
		Puzzle puzzle = new Puzzle();
		List<Puzzle.Solution> solutions = getSolutions(table);
		puzzle.setTable(table);
		puzzle.setSolutions(solutions);

		return puzzle;

	}
	
	/**
	 * Get the empty cells of a list of cells
	 * @param neighbors
	 * @return List<Table.Cell>
	 */
	public List<Table.Cell> getEmptyNeighbors(List<Table.Cell> neighbors ){
		ArrayList<Table.Cell> emptyNeighbors = new ArrayList<Table.Cell>(); 
		for(Table.Cell cell: neighbors) {
			if(cell.isEmpty()) {
				emptyNeighbors.add(cell);
			}
		}
		return emptyNeighbors;
	}

	/**
	 * get the solutions for a table
	 * calls searchCell for each position of the table
	 * @param table
	 * @return
	 */
	public List<Puzzle.Solution> getSolutions(Table table) {
		ArrayList<Puzzle.Solution> solutions = new ArrayList<Puzzle.Solution>(); 
		Dictionary dic = Dictionary.getInstance();
		Trie trie = dic.getTrie();
		for(Table.Cell cell : table) {
			Trie.Search search = trie.startSearch();
			StringBuilder str = new StringBuilder();
			ArrayList<Table.Cell> cellList = new ArrayList<>();
			if(search.continueWith(cell.getLetter())) {
				searchCell(cell,solutions,search,table,str.append(cell.getLetter()),cellList,trie);
			}
		}
		return solutions;
	}
	
	/**
	 * recursively search for all solutions starting in a Cell
	 * @param cell
	 * @param solutions
	 * @param search
	 * @param table
	 * @param str
	 * @param cellList
	 * @param trie
	 */
	public void searchCell(Table.Cell cell, List<Puzzle.Solution> solutions, Trie.Search search,Table table,StringBuilder str,List<Table.Cell> cellList,Trie trie) {
		List<Table.Cell> neighbors = table.getNeighbors(cell);
		cellList.add(cell);

		if(search.isWord() && str.toString().length()>2) {
			Puzzle.Solution sol = new Puzzle.Solution(str.toString(), cellList);
			solutions.add(sol);
		}

		for(Table.Cell neigh : neighbors) {
			Trie.Search newSearch = trie.startSearch(search);

			if(newSearch.continueWith(neigh.getLetter())) {
				StringBuilder current = new StringBuilder();
				current.append(str);
				current.append(neigh.getLetter());

				searchCell(neigh,solutions,newSearch,table,current,cellList,trie);
			}

		}

	}
}
