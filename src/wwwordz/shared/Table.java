package wwwordz.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Table implements Serializable, Iterable<Table.Cell>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int TABLESIZE = 4;
	private Cell[][] table;
	/**
	 * constructors for table
	 */
	//crete an empty table
	public Table() {
		this.table = new Cell[TABLESIZE+1][TABLESIZE+1];
		for(int i=1;i<=TABLESIZE;i++) {
			for(int k=1;k<=TABLESIZE;k++) {
				this.table[i][k] = new Table.Cell(i,k);
			}
		}
	}
	//create a table with data
	public Table(String[] data) {
		this.table = new Cell[TABLESIZE+1][TABLESIZE+1];
		for(int i=1;i<=TABLESIZE;i++) {
			for(int k=1;k<=TABLESIZE;k++) {
				this.table[i][k] = new Cell(i,k,data[i-1].charAt(k-1));
			}
		}
	}
	public 	Iterator<Table.Cell> iterator(){
		return new CellIterator();
	}
	public Cell getCell(int row, int column) {
		return this.table[row][column];
	}
	public char getLetter(int row, int column) {
		return getCell(row,column).getLetter();
	}
	public void setLetter(int row, int column, char letter) {
		getCell(row,column).setLetter(letter);
	}
	public ArrayList<Cell> getNeighbors(Cell cell){
		ArrayList<Cell> neighborList = new ArrayList<Cell>();
		int row = cell.getRow();
		int column = cell.getColumn();
		if(row+1<=TABLESIZE) {
			Cell up = getCell(row+1,column);
			neighborList.add(up);
			if(column+1<=TABLESIZE) {
				Cell dUpR = getCell(row+1,column+1);
				neighborList.add(dUpR);
			}
			if(column-1>=1) {
				Cell dUpL = getCell(row+1,column-1);
				neighborList.add(dUpL);
			}
		}
		if(row-1>=1) {
			Cell down = getCell(row-1,column);
			neighborList.add(down);
			if(column+1<=TABLESIZE) {
				Cell dDownR = getCell(row-1,column+1);
				neighborList.add(dDownR);
			}
			if(column-1>=1) {
				Cell dDownL = getCell(row-1,column-1);
				neighborList.add(dDownL);
			}
		}
		if(column-1>=1) {
			Cell left = getCell(row,column-1);
			neighborList.add(left);
		}
		if(column+1<=TABLESIZE) {
			Cell right = getCell(row,column+1);
			neighborList.add(right);
		}
		
		return neighborList;
	}
	
	public ArrayList<Cell> getEmptyCells(){
		ArrayList<Cell> emptyCells = new ArrayList<Cell>();
		for(int i=1;i<=TABLESIZE;i++) {
			for(int k=1;k<=TABLESIZE;k++) {
				Cell cell = getCell(i,k);
				if(cell.isEmpty()) {
					emptyCells.add(cell);
				}
			}
		}
		return emptyCells;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(table);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (!Arrays.deepEquals(table, other.table))
			return false;
		return true;
	}
	//NESTED CLASS CELL
	public class Cell{
		private int column;
		private char letter;
		private int row;
		/**
		 *Constructors for cell 
		 */
		public Cell() {
			
		}
		//create an empty cell
		public Cell(int row,int column) {
			this.column = column;
			this.row = row;
		}
		//create a cell with a letter
		public Cell(int row, int column, char letter) {
			this.column = column;
			this.row = row;
			this.letter = letter;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + column;
			result = prime * result + letter;
			result = prime * result + row;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (column != other.column)
				return false;
			if (letter != other.letter)
				return false;
			if (row != other.row)
				return false;
			return true;
		}
		public char getLetter() {
			return this.letter;
		}
		public int getRow() {
			return this.row;
		}
		public int getColumn() {
			return this.column;
		}
		public void setLetter(char ch) {
			this.letter = ch;
		}
		public boolean isEmpty() {
			if(this.letter=='\u0000') {
				return true;
			}
			else return false;
		}
		@Override
		public String toString() {
			return String.valueOf(this.letter);
		}
	}
	class CellIterator implements Iterator<Cell> {
		private int column;
		private int row;
		public CellIterator() {
			this.column = 0;
			this.row=0;
		}
		
		public void remove() {
			
		}
		
		@Override
		public boolean hasNext() {
			if(column==TABLESIZE && row == TABLESIZE) {
				return false;
			}
			else
				return true;
			}
		@Override
		public Cell next() {
			if(row==0 && column==0) {
				row=1;
				column=1;
			}
			else if(column==TABLESIZE) {
				row += 1;
				column = 1;
			}
			else if(column<TABLESIZE) {
				column += 1;
			}
			
			return getCell(row,column);
		}
	}
}
