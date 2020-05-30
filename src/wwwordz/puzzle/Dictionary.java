package wwwordz.puzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;

import wwwordz.puzzle.Trie;

public class Dictionary {
	private static Dictionary dictionary=null;
	private Trie trie;
	// Singleton class constructor
	private Dictionary()   {
		Trie trie = new Trie();
		this.trie=trie;
		final String  DIC_FILE = "wwwordz/puzzle/pt-PT-AO.dic";

		InputStream in = ClassLoader.getSystemResourceAsStream(DIC_FILE);
		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String line;
		try {
			while((line = reader.readLine()) != null) {
				String[] splitted = line.split("\\W");
				String word = splitted[0];
				String wordUpper = Normalizer.normalize(word.toUpperCase(Locale.ENGLISH),Form.NFD).
						replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				trie.put(wordUpper);			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	public Trie getTrie() {
		return this.trie;
	}
	
	public static Dictionary getInstance()  {
		if(dictionary==null) {
			return dictionary = new Dictionary();
		}
		else 
			return dictionary;
	}
	/**
	 * Calls trie method to get a random large word
	 * @return String largeWord
	 */
	public String getRandomLargeWord() {
		Trie trie = this.getTrie();
		String largeWord = trie.getRandomLargeWord();

		return largeWord;
	}
	/**
	 * Calls Trie startSearch method 
	 * @return Trie.Search
	 */
	public Trie.Search startSearch(){
		Trie trie = this.getTrie();
		return trie.startSearch();
	}
}
