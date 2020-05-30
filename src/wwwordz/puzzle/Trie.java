package wwwordz.puzzle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Trie implements Iterable<String> {
	private TrieNode root;

	public Trie() {
		this.root = new TrieNode();
	}
	
	public Iterator<String> iterator(){
		return new NodeIterator();
	}
	
	public void put(String word) {
		TrieNode current = root;
		for(int i=0;i < word.length();i++) {
			current = current.getChildrens().computeIfAbsent(word.charAt(i), c -> new TrieNode());
		}
		current.setWord(true);
	}
	/**
	 * Get a random large word (a leaf in our trie)
	 * @return String word
	 */
	public String getRandomLargeWord() {
		Random rand = new Random(); 
		TrieNode current = this.root;
		Set<Character> keys;
		StringBuilder largeWord = new StringBuilder();
		int size=1;
		while((keys = current.getChildrens().keySet()).size()>0) {
			int i = 0;
			Character[] children = new Character[keys.size()];
			for(Character s: keys) {
				children[i++]=s;
			}
			int random = rand.nextInt(keys.size());
			current = current.getChildrens().get(children[random]);
			largeWord.append(children[random]);
			size++;
		}
		if(size<=3) return getRandomLargeWord();
		return largeWord.toString();
	}
	public Search startSearch() {
		return new Search(this.root);
	}
	public Search startSearch(Search search) {
		return new Search(search);
	}
	//nested class TrieNode
	class TrieNode {
		private HashMap<Character, TrieNode> childrens;
		private boolean isWord;

		public TrieNode() {
			this.childrens = new HashMap<Character, TrieNode>();
			this.isWord = false;

		}

		public HashMap<Character, TrieNode> getChildrens(){
			return this.childrens;
		}

		public void setWord(boolean bool) {
			this.isWord=bool;
		}
	}
	//Nested class Search
	class Search{
		private TrieNode node;
		//new search starting in the node given
		public Search(TrieNode node) {
			this.node = node;
			
		}
		//create a clone of a given search
		public Search(Search search) {
			this.node = search.node;
		}
		public boolean continueWith(char letter) {
			boolean continueW=node.getChildrens().containsKey(letter);
			if(continueW) {
				this.node=node.getChildrens().get(letter);
			}
			return continueW;
		}
		public boolean isWord() {
			return node.isWord;
		}
	}
	//Nested class NodeIterator, to make out Trie Iterable
	class NodeIterator implements Iterator<String>, Runnable {
		String nextWord;
		boolean terminated;
		Thread thread;
		NodeIterator() {
			thread = new Thread(this,"Node iterator");
			thread.start();
		}
		public void run() {
			terminated = false;

			//percorrer a arvore aqui
			StringBuilder str = new StringBuilder();
			visitWord(root,str);

			synchronized (this) {
				terminated = true;
				handshake();
			}
		}
		/**
		 * iterates over words kept in the trie
		 * @param TrieNode node
		 * @param StringBuilder str
		 */
		private void visitWord(TrieNode node, StringBuilder str) {
			Set<Character> keys = node.getChildrens().keySet();

			for(Character s : keys) {
				StringBuilder rec = new StringBuilder();
				rec.append(str);
				rec.append(s);
				visitWord(node.getChildrens().get(s),rec);

				synchronized (this) {
					if(nextWord != null)
						handshake();
					if(node.getChildrens().get(s).isWord) {
						nextWord = rec.toString();
						handshake();
					}

				}

			}
		}

		@Override
		public boolean hasNext() {
			synchronized (this) {
				if(! terminated)
					handshake();
			}
			return nextWord != null;
		}

		@Override
		public String next() {
			String word= nextWord;

			synchronized (this) {
				nextWord = null;
			}
			return word;
		}

		private void handshake() {
			notify();
			try {
				wait();
			} catch (InterruptedException cause) {
				throw new RuntimeException("Unexpected interruption while waiting",cause);
			}
		}

	}
}
