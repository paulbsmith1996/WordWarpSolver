/**
 * @author - Paul Baird-Smith    November 9, 2017
 *
 * Word Unscrambler
 *
 * ======================================================================================
 *
 * Unscrambler wants to solve the following problem:
 *
 *
 * Given a target word, we would like to find all the words in the English language
 * that can be made using only the letters from the target word. For example, if
 * our target word is "tree", we would like to print a list of words including
 * "tee", "ret", "ere", and others (the list changes depending on which words you
 * decide to include in the English language). As demonstrated by the example, any
 * repeated letter in the target word can be repeated in a resulting word up to the
 * number of times it appears in the target word (for "tree", we are allowed 2 "e"s). 
 *
 * ======================================================================================
 *
 * We solve this problem with 2 approaches:
 *
 * 1) We can find all of the permutations of the letters in the target word and
 * check for membership in the dictionary. This approach is preferred for shorter
 * words, and becomes less desirable after we pass the number of words in the English
 * language (currently between 100,000 and 150,000).
 *
 * 2) Check all the words in the dictionary to see if they can be made from the letters
 * in the target word. This approach is useful for longer words, with repeat letters. A
 * prime example of this is the word "telephone".
 *
 * ======================================================================================
 *
 * Unscrambler takes at least one argument: the target word. After running, it prints a
 * list of all the words that can be written using its letters. An example of how to run
 * Unscrambler is:
 *
 *           java Unscrambler helicopter
 *
 * Here, the target word is helicopter. The program expects the target word as the first
 * argument, and defaults to "a" if no argument is given.
 *
 * A second argument passed is a mode integer which can be 0, 1, or 2. If mode 0 is used,
 * the program will run approach (1) above. If mode 1 is used, the program will run
 * approach (2). If mode 2 is selected, the program will determine which strategy is the
 * best depending on the target word length and run this solution. The program defaults
 * to mode 2.
 *
 * Email ppb366@cs.utexas.edu with any questions
 */


// Imports
import java.util.Vector;
import java.io.*;
import java.util.Scanner;

public class Unscrambler {

    // Constant for output formatting
    private static final int WORDS_PER_LINE = 10;

    // File path for the dictionary file
    private static final String DICT_FILE = "wordsEn.txt";

    // The length that determines whether we use approach 1 or 2
    private static final int THRESHOLD = 5;

    // Vector holding all words in the English language
    private static Vector<String> enWords;

    /**
     * Main method, where we can try both solutions.
     */
    public static void main(String[] args) {

	long start = System.currentTimeMillis();

	int mode = 2;

	// Get the word we are unscrambling (defaults to "a")
	String given = "a";
	if(args.length > 0) {
	    given = args[0];
	}

	if(args.length > 1) {
	    try {
		mode = Integer.parseInt(args[1]);
	    } catch(Exception e) {
		System.err.println("ERROR: Invalid mode specified.");
	    }
	}

	// Create a vector with all the words in the dictionary
	enWords = generateWordList();

	System.out.println("\nResult words:\n");

	if(mode == 0 || (mode == 2 && given.length() < THRESHOLD)) {
	    // This is the slow way to do it. Get all the permutations of the letters of the
	    // word we are unscrambling and check them in the dictionary. This gets unwieldy
	    // and difficult to manage past 7-8 letters
	    Vector<String> allPerms = getPerms(given);
	    
	    int count = 0;

	    Vector<String> goodWords = new Vector<String>();
	    for(String word: allPerms) {
		if(enWords.contains(word) && !goodWords.contains(word)) {
		    goodWords.add(word);
		    System.out.print(word + ", ");
		    count++;

		    if((count + 1) % WORDS_PER_LINE == 0) {
			System.out.println();
		    }
		}


	    }

	} else {
	    
	    int count = 0;
	    
	    Vector<String> goodWords = checkAllWords(given);
	    for(String word: goodWords) {
		System.out.print(word + ", ");	    
		count++;

		if(count % WORDS_PER_LINE == 0) {
		    System.out.println();
		}
	    }
	}

	long end = System.currentTimeMillis();
	
	System.out.println("\n\nProgram terminated in " + (end - start) + " ms.\n");
	return;
	
    }
    



    /*********************** APPROACH 2 **********************************/


    
    /**
     * Second, more appropriate approach to the word scrambling problem. Check
     * all words in the dictionary, to see if which of them can be made from the
     * letters in our target word. 
     *
     * This becomes very advantagious when our word length exceeds 9 letters. The
     * English language has on the order of 100000 words, whereas 9! = 362880. Generally,
     * this method is bounded by the size of our dictionary, rather than O(e^n) (bound
     * estimated with stirling approximation).
     *
     * We pass our target word to the method.
     */
    public static Vector<String> checkAllWords(String given) {
	
	// Holds all words that can be made from letters in our target word
	Vector<String> validWords = new Vector<String>();

	// Make a vector with the letters of the word we are scrambling
	char[] givenChars = given.toCharArray();
	Vector<Character> givenLetters = new Vector<Character>();
	for(char c: givenChars) {
	    givenLetters.add(c);
	}

	// Check every word in the dictionary against our word
	for(String word: enWords) {

	    // We first guess the word is valid, and we try to prove it is not
	    boolean valid = true;

	    // Make a deep copy of the target word's letters
	    Vector<Character> givenCopy = vecCopy(givenLetters);

	    // Make a vector with the letters of the dict word we are checking
	    char[] wordChars = word.toCharArray();
	    Vector<Character> wordLetters = new Vector<Character>();
	    for(char c: wordChars) {
		wordLetters.add(c);
	    }   

	    
	    // Iteratively remove letters from the target word and the current word
	    // we are looking at in our dictionary vector 
	    while(!givenCopy.isEmpty() && !wordLetters.isEmpty()) {

		// Remove the first letter from the remaining letters in dictionary word
		char letter = wordLetters.elementAt(0);
		wordLetters.remove(0);

		// The dictionary word  has a letter in it that the given word does not
		if(givenCopy.isEmpty() || !givenCopy.remove((Character)letter)) {
		    valid = false;
		    break;
		}
	    }

	    // Check that all letters in dictionary word have been accoutned for, and
	    // check the word is not the empty String, (we choose to ignore this String)
	    if(valid && wordLetters.isEmpty() && !word.equals("")) {
		validWords.add(word);
	    }

	}

	return validWords;
    }




    /*********************** APPROACH 1 **********************************/
    

    /**
     * Returns all the permutations of the letters in the String word.
     */
    public static Vector<String> getPerms(String word) {
	// Make a new empty Vector
	Vector<String> init = new Vector<String>();

	// Get a vector of the letters from our passed word
	Vector<Character> letters = new Vector<Character>();
	char[] wordChars = word.toCharArray();
	for(char c: wordChars) {
	    letters.add(c);
	}
	
	// Call helper method
	return getPermsH(init, "", letters);
    }


    /**
     * Recursvie helper method for finding all the permutations of a String. This takes all the current
     * permutations found to this point, a String holding a prefix, and remaining letters to add to the
     * prefix.
     */
    public static Vector<String> getPermsH(Vector<String> perms, String word, Vector<Character> letters) {
	
	// All letters in target word have been used up
	if(letters.size() == 0) {
	    return perms;
	}

	// Loop through all letters remaining and append to word
	for(char c: letters) {

	    // Add our new permutation
	    perms.add(word + c);

	    // Deep copy of our letter array, without the letter we just appended
	    Vector<Character> newLetters = new Vector<Character>();
	    for(char d: letters) {
		if(d != c) {
		    newLetters.add(d);
		}
	    }

	    // Recursive call
	    perms = getPermsH(perms, word + c, newLetters);
	}

	return perms;
    }




    /*********************** DICTIONARY GENERATOR **********************************/


    /** 
     * Generates a list of all words in the English language, based on the provided
     * dictionary file.
     *
     */
    public static Vector<String> generateWordList() {
	
	// Resultant vector
	Vector<String> dictWords = new Vector<String>();

	// Open and read file
	File f = null;
	Scanner fileScan = null;
	try {
	    f = new File(DICT_FILE);
	    fileScan = new Scanner(f);
	} catch(Exception e) {
	    System.err.println("ERROR: Could not reead dict file.");
	    return null;
	}

	// Add all words to our vector
	while(fileScan.hasNextLine()) {
	    dictWords.add(fileScan.nextLine());
	}
	
	return dictWords;
    }





    /*********************** UTILITY METHODS **********************************/


    /**
     * Returns a deep copy of the passed vector of characters.
     */
    public static Vector<Character> vecCopy(Vector<Character> toCopy) {
	Vector<Character> copy = new Vector<Character>();
	for(char s: toCopy) {
	    copy.add(s);
	}
	return copy;
    }


    // Print a vector of strings, one element per line
    public static void vecPrint(Vector<String> toPrint) {
	for(String s: toPrint) {
	    System.out.println(s);
	}
    }



}