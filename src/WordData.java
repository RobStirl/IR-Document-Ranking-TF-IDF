import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Each word will have a WordData. This contains a list of WordInstance, each one containing a
// document the word is in and the frequency the word appears in said document (in title, subject, and/or body). The list will have
// a WordInstance for every document that contains the word

public class WordData
{
	private List<WordInstance> wordInstanceList;	// Stores the word's instance corresponding to the document
	private int freq;								// The number of docs containing this word
	
	WordData()
	{
		wordInstanceList = new ArrayList<WordInstance>();
		freq = 0;
	}
	
	// Get the list containing all the word's WordInDocsInfo
	public List<WordInstance> getWordData()
	{
		return wordInstanceList;
	}
	
	// Get the global frequency of the word appearing across all documents
	// If 5 documents contain this word, it will return 5
	public int getNumberOfDocsContainingWord()
	{
		
		return freq;
	}
	
	// Get the total number of times this word is used
	// If 5 documents contain this word, and each doc has the word
	// twice, it will return 10
	public int getTotalAccurancesOfWord()
	{
		int tot = 0;
		for(int i = 0; i < wordInstanceList.size(); ++i)
		{
			tot += wordInstanceList.get(i).wordFreq;
		}
		return tot;
	}
	
	// Returns an unsorted list of doc ids that contain this word
	public List<Integer> getDocsOfWord()
	{
		List<Integer> docs = new ArrayList<Integer>();
		for(int i = 0; i < freq; i++)
		{
			docs.add(wordInstanceList.get(i).docID);
		}
		return docs;
	}
	
	// Add a new document to the list of documents that have this word
	public void addDoc(int doc, boolean title, boolean subject, boolean body)
	{
		for(WordInstance f : wordInstanceList)	// Search for the document in wordFreq
		{
			if(f.docID == doc)		// When found, add 1 count to the word frequency
			{
				f.updateZoneData(title, subject, body);
				f.wordFreq++;
				return;
			}
		}
		freq++;	// Increment global frequency
		
		// If it was not found in wordInstanceList, it doesn't exist yet so add a new one
		wordInstanceList.add(new WordInstance(doc, title, subject, body));
	}
	
	
	
	// Gets the total amount of times this word appears in the title of a doc
	public int getTitleFreq(int docID)
	{
		for(WordInstance f : wordInstanceList)	// Search for the document in wordFreq
		{
			if(f.docID == docID)		
				return f.titleFreq;
		}
		return 0;
	}
	
	// Gets the total amount of times this word appears in the subject of a doc
	public int getSubjectFreq(int docID)
	{
		for(WordInstance f : wordInstanceList)	// Search for the document in wordFreq
		{
			if(f.docID == docID)		
				return f.subjectFreq;
		}
		return 0;
	}
	
	// Gets the total amount of times this word appears in the body of a doc
	public int getBodyFreq(int docID)
	{
		for(WordInstance f : wordInstanceList)	// Search for the document in wordFreq
		{
			if(f.docID == docID)		
				return f.bodyFreq;
		}
		return 0;
	}
	
	// Gets the total amount of times this word appears in a doc
	public int getTotalFreq(int docID)
	{
		for(WordInstance f : wordInstanceList)	// Search for the document in wordFreq
		{
			if(f.docID == docID)		
				return f.wordFreq ;
		}
		return 0;
	}
	
	
	// Searches through all the docs that have this word and returns the how many of them have the word in the 'title' zone
	public int getNumOfDocsThatHaveTitle()
	{
		int total = 0;
		for(WordInstance f : wordInstanceList)
		{
			if(f.titleFreq > 0)
				total++;
		}
		return total;
	}
	
	// Searches through all the docs that have this word and returns the how many of them have the word in the 'subject' zone
	public int getNumOfDocsThatHaveSubject()
	{
		int total = 0;
		for(WordInstance f : wordInstanceList)
		{
			if(f.subjectFreq > 0)
				total++;
		}
		return total;
	}
		
	// Searches through all the docs that have this word and returns the how many of them have the word in the 'body' zone
	public int getNumOfDocsThatHaveBody()
	{
		int total = 0;
		for(WordInstance f : wordInstanceList)
		{
			if(f.bodyFreq > 0)
				total++;
		}
		return total;
	}
	
	
	// Set a TF_IDF for a particular document
	public void setTF_IDF(int dID, double tf_idf)
	{
		for(WordInstance f : wordInstanceList)
		{
			if(f.docID == dID)		
				f.TF_IDF = tf_idf;
		}
	}
	
	// Returns a list of doc ids that contain this word, sorted by TF_IDF values
	public List<Integer> getDocsInOrderOfTF_IDF()
	{
		List<Integer> docs = new ArrayList<Integer>();
		List<Integer> temp = new ArrayList<Integer>();
		double largestTF = 0;
		int index = 0;
		
		
		for(int all = 0; all < wordInstanceList.size(); all++)
		{
			for(int i = 0; i < wordInstanceList.size(); i++)
			{
				if(wordInstanceList.get(i).TF_IDF > largestTF && !docs.contains(wordInstanceList.get(i).docID))
				{
					largestTF = wordInstanceList.get(i).TF_IDF;
					index = i;
				}
			}
			largestTF = 0;
			
			docs.add(wordInstanceList.get(index).docID);
			temp.add(index);
		}

		return docs;
	}
	
	
}
