import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
/**
 * A model for the game of 20 questions
 * 
 * @author Rick Mercer
 */
public class GameTree {

	/**
	 * Constructor needed to create the game.
	 *
	 * @param fileName
	 *          this is the name of the file we need to import the game questions
	 *          and answers from.
	 */

	private BSTNode root;
	private Scanner console;
	private BSTNode temp;
	private String outputFileName;

	private class BSTNode {
		String str;
		BSTNode left, right;

		public BSTNode(String str) {
			this.str = str;
			left = null;
			right = null;
		}
		
		@Override
		public String toString() {
			return "" + this.str;
		}
	}

	/**
	 * Constructor needed to create the game.
	 * 
	 * @param name
	 *            this is the name of the file we need to import the game
	 *            questions and answers from.
	 */
	public GameTree(String name) {
		outputFileName = name;
		try {
			console = new Scanner(new File(outputFileName));
		} catch (FileNotFoundException e) {
			console = null;
			root = null;
		}
		root = buildTree();
		console.close();
		temp = root;

	}

	private BSTNode buildTree() {
		if (console != null && console.hasNextLine()) {
			String str = console.nextLine().trim();
			if (str.charAt(str.length() - 1) == '?') {
				BSTNode temp = new BSTNode(str);
				temp.left = buildTree();
				temp.right = buildTree();
				return temp;
			}
			return new BSTNode(str);
		} else {
			return null;
		}
	}

	/*
	 * Add a new question and answer to the currentNode. If the current node has
	 * the answer chicken, theGame.add("Does it swim?", "goose"); should change
	 * that node like this:
	 */
	// -----------Feathers?-----------------Feathers?------
	// -------------/----\------------------/-------\------
	// ------- chicken horse-----Does it swim?-----horse--
	// -----------------------------/------\---------------
	// --------------------------goose--chicken-----------
	
	/**
	 * @param newQuestion
	 *            The question to add where the old answer was.
	 * @param newAnswer
	 *            The new Yes answer for the new question.
	 */
	public void add(String newQ, String newA) {
		String str = temp.str;
		temp.str = newQ;
		temp.left = new BSTNode(newA);
		temp.right = new BSTNode(str);
	}

	/**
	 * True if getCurrent() returns an answer rather than a question.
	 * 
	 * @return False if the current node is an internal node rather than an
	 *         answer at a leaf.
	 */
	public boolean foundAnswer() {
		if (temp != null && temp.str.charAt(temp.str.trim().length() - 1) != '?') {
			return true;
		}
		return false;
	}

	/**
	 * Return the data for the current node, which could be a question or an
	 * answer.
	 * 
	 * @return The current question or answer.
	 */
	public String getCurrent() {
		if (temp != null) {
			return temp.str;
		}
		return "";
	}

	/**
	 * Ask the game to update the current node by going left for Choice.yes or
	 * right for Choice.no Example code: theGame.playerSelected(Choice.Yes);
	 * 
	 * @param yesOrNo
	 */
	public void playerSelected(Choice yesOrNo) {
		if (yesOrNo == Choice.Yes) {
			temp = temp.left;
		} else if (yesOrNo == Choice.No) {
			temp = temp.right;
		}
	}

	/**
	 * Begin a game at the root of the tree. getCurrent should return the
	 * question at the root of this GameTree.
	 */
	public void reStart() {
		temp = root;
	}

	@Override
	public String toString() {
		return toString(root, "");
	}

	private String toString(BSTNode temp, String str) {
		if (temp != null) {
			return toString(temp.right, str + "-") + str + temp.str + '\n' + toString(temp.left, str + "-");
		}
		return "";
	}
	
	/**
	 * Overwrite the old file for this gameTree with the current state that may
	 * have new questions added since the game started.
	 * 
	 */
	public void saveGame() {
		PrintWriter diskFile = null;
		try {
			diskFile = new PrintWriter(new FileOutputStream(outputFileName));
		} catch (IOException io) {
			System.out.println("Could not create file: " + outputFileName);
		}
		add(diskFile, root);
		diskFile.close();
	}

	private void add(PrintWriter diskFile, BSTNode temp) {
		if (temp != null) {
			diskFile.println(temp.str);
			add(diskFile, temp.left);
			add(diskFile, temp.right);
		}
	}
}