import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

// Robert Stirling
// COSC 4315
// 4 April 2018
// Reads a directory of text files
// Allows user to search for documents that contain a search word
// The order is relative to the calculated weight of a word in the doc
// The zone weights are:
//			zone1 = 0.2
//			zone2 = 0.3
//			zone3 = 0.5
public class IRSystem
{
	private double zone1 = 0.2;
	private double zone2 = 0.3;
	private double zone3 = 0.5;
			
	private MyLibrary lib = new MyLibrary(zone1, zone2, zone3);

	
	public void start() throws FileNotFoundException, UnsupportedEncodingException
	{
		String inDir,  word;
		Scanner sc = new Scanner(System.in);
		File folder;
		Scanner scan;
		
	
		int wordID;
		int docID;
		int temp;
		
		// User input
		System.out.println("Enter Input Directory: ");
		inDir = sc.nextLine();
		//inDir = "C:/Users/Rob/Desktop/New folder";
		//inDir = "C:/Users/Rob/Desktop/school/COSC4315 - Info Retreval/docs";

		
		// Read in all the documents
		System.out.print("Reading documents...");
		folder = new File(inDir);
		File[] f = folder.listFiles();
		int badFiles = 0;
		for(int i = 0; i < f.length; ++i)
		{
			if(f[i].canRead())
			{
				try
				{
					scan = new Scanner(f[i]);
					docID = lib.addDocID(f[i].getName());
					//System.out.println("***Title***");
					
					
					
					
					// *** Get the first line of the doc - this is the 'title' ***
					
					if(scan.hasNext()) // If there is at least one thing to be read
					{
						
						scan.useDelimiter("[^A-Za-z0-9']+"); 	// Ignore all non-letters, numbers, apostrophes, and new line
																// We don't care about new lines in the first word (since the title needs atleast one word)
						word = scan.next().toLowerCase();		// Read it
						//System.out.println(word);
						wordID = lib.addWordID(word); // Add the next word to the library
						lib.addWordFromDoc(docID, wordID, true, false, false);
						
						scan.useDelimiter("[^A-Za-z0-9'\n]+"); // Ignore all non-letters, numbers, apostrophes, and new line char
						
						
						while( scan.hasNext() ) // Keep scanning if there is something to be read, the loop breaks out elsewhere
						{
							word = scan.next().toLowerCase(); // Scan next valid word ( new lines included )
							
							
							if(word.charAt(0) == '\n')	// Does the next word have a new line? (It will always be the first char)
							{							// We have reached the end of the first line (and the title)
														// There might still be a valid word after the new line (which will be the first word of the subject),
														// so we need to save that word (but get rid of any new line chars)
								
								if(word.length() > 1) 	// Is there at least one more char other than the new line?
								{
									temp = word.lastIndexOf("\n");	// Find the index of the last new line char (if the word contained multiple new lines, we want to get rid of all of them)
									if(temp < word.length())		// Is there valid chars besides the new line char?
									{
										word = word.substring(1, word.length());
										wordID = lib.addWordID(word); // Add the next word to the library
										lib.addWordFromDoc(docID, wordID, false, true, false);
										//System.out.println("Next:" + word);
									}
								}
								break;
							}
							wordID = lib.addWordID(word); // Add the next word to the library
							lib.addWordFromDoc(docID, wordID, true, false, false);
							//System.out.println(word);
						}
					}
					
					scan.useDelimiter("[^A-Za-z0-9'.]+"); // Ignore all non-letters, numbers, apostrophes, and periods
					//System.out.println("***Subject***");

						while(scan.hasNext() )
						{
							word = scan.next().toLowerCase();
							
							
							if(word.charAt( word.length() - 1) == '.')	// Does the next word have a new line? (It will always be the first char)
							{							// We have reached the end of the first line (and the title)
														// There might still be a valid word after the new line (which will be the first word of the subject),
														// so we need to save that word (but get rid of any new line chars)
								
								if(word.length() > 1)				// Is there at least one more char other than the '.'?
								{
									temp = word.indexOf(".");		// Find the index of the first of the '.'
									word = word.substring(0, temp);	// Get everything before the first '.'
									wordID = lib.addWordID(word); // Add the next word to the library
									lib.addWordFromDoc(docID, wordID, false, true, false);
									//System.out.println(word);
								}
								break;
							}
							wordID = lib.addWordID(word); // Add the next word to the library
							
							lib.addWordFromDoc(docID, wordID, false, true, false);
							//System.out.println(word);
						}
					
					
					scan.useDelimiter("[^A-Za-z0-9']+"); // Ignore all non-letters, numbers, and apostrophes
					//System.out.println("***Body***");
					while(scan.hasNext())
					{
						word = scan.next().toLowerCase();
						wordID = lib.addWordID(word); // Add the next word to the library
						//System.out.println(word);
						lib.addWordFromDoc(docID, wordID, false, false, true);

					}

				}
				catch (FileNotFoundException e)
				{
					System.out.println("Error! Cannot read " + f[i].getName() + "!");
					++badFiles;
				}
			}
			else
			{
				System.out.println("Error! Cannot open " + f[i].getName() + "!");
				++badFiles;
			}
		}
		System.out.println("Done");
		System.out.println("\n" + f.length + " files found.");
		System.out.println("\n" + badFiles + " files could not be read.\n");
		
		System.out.print("Creating TF_IDF for all words in each document...");
		lib.calcTF_IDFForAllWords();
		System.out.println("Done");
		
		
		
		// User search
		sc.useDelimiter("[^A-Za-z0-9']+");
		int maxSize = 0;
		while(true)
		{
			
			System.out.print("Enter word or phrase: ");
			
			word = sc.next().toLowerCase();
			
			
			List<Integer> docs = lib.getMostReleventDocsInOrder(lib.getWordID(word));
			if(docs.size() == 0)
			{
				System.out.println("No relevent documents found.");
				continue;
			}
			System.out.println("" + docs.size() + " relevent documents found. How many would you like displayed: ");
			{
				maxSize = sc.nextInt();
			}
		

			if(maxSize > docs.size() || maxSize < 0)
			{
				System.out.println("Invalid size, defaulting to size 5");
				maxSize = 5;
			}
			
			System.out.println("Top  " + maxSize + "  most relevent documents for \"" + word + "\": ");
			for(int i = 0; i < maxSize; i++)
				System.out.println(lib.getDocFromID(docs.get(i)));
		
			if(docs.size() > 5)
				System.out.println(docs.size() - maxSize + " document(s) excluded from the search result");
			System.out.println();
		}
		

		// Done
	}
	

	
}
