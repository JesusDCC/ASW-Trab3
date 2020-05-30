package wwwordz.shared;

import java.io.Serializable;
import java.util.List;

public class Puzzle implements Serializable {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private List<Solution> solutions;
	private Table table;

	public Puzzle() {
		
	}
	
	public List<Solution> getSolutions(){
		return this.solutions;
	}
	public Table getTable() {
		return this.table;
	}
	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	//Nested class Solution
	public static class Solution implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<Table.Cell> cells;
		private String word;
		public Solution() {
		
		}
		public Solution(String word,List<Table.Cell> cells) {
			this.word = word;
			this.cells = cells;
		}
		public List<Table.Cell> getCells(){
			return this.cells;
		}
		public String getWord() {
			return this.word;
		}
		public int getPoints() {
			int points=1;
			int minSizeWord=3;
			int relationToMinWord = this.word.length() - minSizeWord;
			for(int i=1;i<=relationToMinWord;i++) {
				points = 2*points +1;
			}
			return points;
		}
	}
}
