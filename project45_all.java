
/**
Authors:		Elaine Martin, Sari Nahmad, Anthony Tockar
Major:			Master of Science in Analytics
Purpose:		Take input file and organize into hash lists (size 100 or 200) based on two different methods of 
				organization (ASCII vs. hashCode) 
General design: The user is first asked to choose a list size. The program then organizes the input file words
				using two distinct methods, scaled to the size of the list. The program removes duplicate words 
				and then outputs the organized words into one of two output files based on the method. 
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

//deal with duplicates

public class project45_all {

	public static void main(String[] args) throws FileNotFoundException {

		//initialize
		int arraySize = getSize();

		ArrayList<String>[] h1Array = (ArrayList<String>[]) new ArrayList[arraySize];
		for (int i = 0; i < arraySize; ++i) {
			h1Array[i] = new ArrayList<String>();
		}

		ArrayList<String>[] h2Array = (ArrayList<String>[]) new ArrayList[arraySize];
		for (int i = 0; i < arraySize; ++i) {
			h2Array[i] = new ArrayList<String>();
		}

		ArrayList<String> words = new ArrayList<String>();
		
		//get input
		final String INPUTFILE = "input.txt";
		
		try {
			words = readInput(INPUTFILE);
		}
		catch (FileNotFoundException except) {
			System.out.println(INPUTFILE+" does not exist. Please ensure "+INPUTFILE+" is in the proper directory.");
			System.exit(1);
		}

		//assign
		h1Array = h1method(arraySize, words);
		h2Array = h2method(arraySize, words);

		//output
		final String H1OUTPUT = "output1.txt"; 
		final String H2OUTPUT = "output2.txt"; 
		writeFile(arraySize, H1OUTPUT, h1Array); 
		writeFile(arraySize, H2OUTPUT, h2Array); 
		System.out.println("The output of the two methods can be found in output1.txt and output2.txt.");

	}
	/**
	GETSIZE method retrieves the user input for the list size
	@return arraySize: size of list 
	 */
	public static int getSize() {
		int arraySize = 0;
		Scanner in = new Scanner(System.in);
		System.out.print("Please enter array size (100 or 200): ");
		do {
			if (!in.hasNextInt()) {
				System.out.println("Please enter an integer (100 or 200): ");
				in.next();
			}
			else {
				arraySize = in.nextInt();
				if (arraySize!=100 && arraySize!=200)
					System.out.print("Please enter 100 or 200: ");
			}
		} while (arraySize!=100 && arraySize!=200);
		return arraySize;
	}
	/**
	READINPUT reads in input from file and removes unwanted delimiters 
	@return words: an array list of the words from the input file 
	 */	
	public static ArrayList<String> readInput(String fileName) throws FileNotFoundException {
		File inputFile = new File(fileName);
		Scanner in = new Scanner(inputFile).useDelimiter("[.,\\[\\]\"()\\s\\t]+");
		ArrayList<String> words = new ArrayList<String>();
		while (in.hasNext()) {
			words.add(in.next());
		}
		in.close();
		return words;
	}
	/**
	H1METHOD organizes words by the following factors: sum of each character c, ASCII of character c, %size (scale to 100/200)
	@return h1Array 
	 */
	public static ArrayList<String>[] h1method(int arraySize, ArrayList<String> input) {
		ArrayList<String>[] h1Array = (ArrayList<String>[]) new ArrayList[arraySize];
		for (int i = 0; i < arraySize; ++i) {
			h1Array[i] = new ArrayList<String>();
		}

		for (int j = 0; j < input.size(); ++j) {
			int h1x = 0;
			String word = input.get(j);
			for(int i = 0; i < word.length(); i++) {
				int ascii = (int) word.charAt(i);
				h1x = h1x + ascii;
			}
			h1x = h1x % arraySize;
			h1Array[h1x].add(word);
		}
		return h1Array;
	}
	/**
	H2METHOD organizes words by the following factors: x.hashCode() method
	@return h2Array 
	 */
	public static ArrayList<String>[] h2method(int arraySize, ArrayList<String> input) {
		ArrayList<String>[] h2Array = (ArrayList<String>[]) new ArrayList[arraySize];
		for (int i = 0; i < arraySize; ++i) {
			h2Array[i] = new ArrayList<String>();
		}

		for (int j = 0; j < input.size(); ++j) {
			String word = input.get(j);
			int h2x = Math.abs(word.hashCode() % arraySize);
			h2Array[h2x].add(word);
		}
		return h2Array;
	}	

	/**
	WRITEFILE method prints out the array
	@param count (100/200); outputfile (1/2 creates); array Array of arraylists
	 */
	public static void writeFile(int count, String outputfile, ArrayList<String>[] array) throws FileNotFoundException{
		
		int COUNT = count; 
		File output = new File(outputfile);
		ArrayList<String>[] ARRAY = array; 

		PrintWriter out = new PrintWriter(output);

		for (int i=0; i<COUNT; i++)
		{
			int number = i+1;
			out.printf("%d ", number);
			
			int rowSize = array[i].size();
			if (rowSize==0)
				out.print("EMPTY LINE...");
			else
			{
				for (int j=0; j<rowSize; j++)
				{
					boolean match = false; 
					for (int k=0; k<j; k++)
					{
						if (ARRAY[i].get(j).equals(ARRAY[i].get(k)))
							match = true; 
					}
					if (!match)
						out.printf("%s, ", ARRAY[i].get(j));
				}
			}
			out.println();
			out.println(); 
		}
		out.close(); 

	}

}
