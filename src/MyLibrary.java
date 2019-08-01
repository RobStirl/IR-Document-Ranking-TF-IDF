import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;



// Stores all the words, documents, the bitset mapping a word to a doc, the wordInDocInfo for each word
// and the list of soundexes for each word
class MyLibrary
{

	// 			   < key, value >
	private TreeMap<String, Integer> wTree;			// Stores the words in alphabetical order with value = wordID
	private TreeMap<String, Integer> dTree; 		// Stores the documents in alphabetical order with value = docID
	private List<WordData> wordDataArray;			// A list of all WordData about each word
	private List<DocData> docDataArray;				// A list of the each doc's data
	
	public List<BitSet> truthArray;					// Each word will have a BitSet. Each bit represents a document and whether that word 
													// is in that document. The first doc loaded in will be pos(0), the next pos(1), etc
	
	private int m_wordID = 0;	// Used to generate a unique word id when a word is added
	private int m_docID = 0;	// Used to generate a unique doc id when a doc is added
	
	private double titleWeight, subjectWeight, bodyWeight;
	
	MyLibrary(double tWeight, double sWeight, double bWeight)
	{
		wTree = new TreeMap<String, Integer>();
		dTree = new TreeMap<String, Integer>();
		wordDataArray = new ArrayList<WordData>();
		docDataArray = new ArrayList<DocData>();
		
		truthArray = new ArrayList<BitSet>();
		
		titleWeight		= tWeight;
		subjectWeight	= sWeight;
		bodyWeight		= bWeight;
	}
	
	// Get the id of a word in wTree, if it doesn't exist in
	// the tree yet, add it to the tree and return the new id
	public Integer addWordID(String w)
	{
		String temp = "";
		Integer id = wTree.get(w);
		if(id == null)
		{
			id = m_wordID;
			wTree.put(w, m_wordID);
			
			m_wordID++;
		}
		return id;
	}
	
	
	// Get the id of a doc in dTree, if it doesn't exist in
	// the tree yet, add it to the tree and return the new id
	public Integer addDocID(String d)
	{
		Integer id = dTree.get(d);	
		if(id == null)				
		{
			id = m_docID;
			dTree.put(d, m_docID);
			
			m_docID++;
		}	
		return id;
	}
	
	// Gets the id of a word, returns -1 if that word is not in the library
	public Integer getWordID(String w)
	{
		Integer id = wTree.get(w);
		if(id == null)
		{
			id = -1;
		}
		return id;
	}
	
	// Gets the id of a doc, returns -1 if that doc is not in the library
	public Integer getDocID(String d)
	{
		Integer id = dTree.get(d);
		if(id == null)
		{
			id = -1;
		}
		return id;
	}

	
	// Map a new word to a document, pass in the ids by using getWordID(String w)
	// and getDocID(String w). If the word or doc doesn't exist in the trees,
	// getWordID and getDocID will add them
	// Also add that word previous to this word. This will be this word's bi word.
	// Other appearances of this word in this doc may have different bi words (which will also be added)
	public void addWordFromDoc(int _docID, int _wordID, boolean title, boolean subject, boolean body)
	{
		// Bad id check (would never happen if getWordID and getDocID are used)
		if(_docID >= m_docID || _wordID >= m_wordID)
		{
			System.out.println("ERROR_BAD_ID");
			return;
		}
		BitSet d = getDocTruthArray(_wordID);
		d.set(m_docID);	
		WordData wData = getAddWordData(_wordID);
		wData.addDoc(_docID, title, subject, body);
		
		DocData dData = getAddDocData(_docID);
		dData.addWord(title, subject, body);
	}
	
	// Gets the BitSet of a word.
	// Each word will have a BitSet. Each bit represents a document and whether that word 
	// is in that document. The first doc loaded in will be pos(0), the next pos(1), etc
	public BitSet getDocTruthArray(int wordID)
	{
		if(wordID >= truthArray.size())
		{
			int numDocs = wordID - truthArray.size() + 1;
			for(int i = 0; i < numDocs; ++i)
				truthArray.add(new BitSet());
		}

		return truthArray.get(wordID);
	}
	
	// Gets or Adds the WordData of a wordID
	public WordData getAddWordData(Integer wID)
	{
		if(wID >= wordDataArray.size())	// If the word id is greater than the size of infoArray
		{								// then it doesn't exist yet
			
			int numDocs = wID - wordDataArray.size() + 1;
			for(int i = 0; i < numDocs; ++i)
				wordDataArray.add(new WordData());
		}
		
		return wordDataArray.get(wID);
	}
	
	
	// Gets or Adds the DocData of a docID
	public DocData getAddDocData(Integer dID)
	{
		if(dID >= docDataArray.size() )
		{
			docDataArray.add(new DocData());
			//int numDocs = dID - docDataArray.size() + 1;
			//for(int i = 0; i < numDocs; ++i)
			//	docDataArray.add(new DocData());
			
		}
		
		return docDataArray.get(dID);
	}
	


	// Returns the number of words in library
	public int getWordCount()
	{
		return m_wordID;
	}
	
	// Returns the number of documents in library
	public int getDocCount()
	{
		return m_docID;
	}
	
	// Returns true if a word is in the library
	public boolean containsWord(String w)
	{
		return wTree.containsKey(w);
	}
	// Returns true if a doc is in the library
	public boolean containsDoc(String d)
	{
		return dTree.containsKey(d);
	}
	// Returns true if a word is in the library
	public boolean containsWord(int wID)
	{
		return wTree.containsValue(wID);
	}
	// Returns true if a doc is in the library
	public boolean containsDoc(int dID)
	{
		return dTree.containsValue(dID);
	}
	
	// Returns the string name associated with the word id
	public String getWordFromID(int id)
	{
		
		for (Entry<String, Integer> entry : wTree.entrySet())
		{
			if(entry.getValue() == id)
			{
				return entry.getKey();
			}
		}
		return "ERROR_WORD_NOT_FOUND_BAD_ID"; // An invalid id was passed
	}
	
	// Returns the string name associated with the doc id
	public String getDocFromID(int id)
	{
		
		for (Entry<String, Integer> entry : dTree.entrySet())
		{
			if(entry.getValue() == id)
			{
				return entry.getKey();
			}
		}
		return "ERROR_DOCUMENT_NOT_FOUND_BAD_ID"; // An invalid id was passed
	}
	
	// Gets wTree, which contains all the words in the library
	public TreeMap<String, Integer> getWordTree()
	{
		return wTree;
	}
	
	// Returns a list of all the docs that contain word
	public List<Integer> getDocsOfWord(int word)
	{
		List<Integer> docs = new ArrayList<Integer>();
		BitSet truth = truthArray.get(word);
		for(int i = 0; i <= m_docID; i++)
		{
			if(truth.get(i))
			{
				docs.add(i - 1);
			}
		}
		
		return docs;
	}
	
	// Calculates and stores the TF_IDF for all words in the library
	public void calcTF_IDFForAllWords()
	{
		List<Integer> docs;
		int curDoc;
		double temp;
		for(int curWord = 0; curWord < m_wordID; curWord++)
		{
			//System.out.println("Word: " + getWordFromID(curWord));
			docs = getAddWordData(curWord).getDocsOfWord();
			for(int j = 0; j < docs.size(); j++)
			{
				curDoc = getAddWordData(curWord).getDocsOfWord().get(j);
				temp = createTF_IDF(curWord,	curDoc);
				//System.out.println("	Doc: " + getDocFromID(curDoc) + "	TF_IDF: " + temp);
				getAddWordData(curWord).setTF_IDF(curDoc, temp);
			}
			
		}
	}
	
	
	// Returns a list of doc ids in the order of relevance
	public List<Integer> getMostReleventDocsInOrder(int wID)
	{
		List<Integer> docs = new ArrayList<Integer>();
		
		if(containsWord(wID))
		{
			WordData data = getAddWordData(wID);
			return data.getDocsInOrderOfTF_IDF();
		}
		
		return docs;
	}
	
	// Returns a TF_IDF of a word in a doc
	public double createTF_IDF(int wID, int dID)
	{	
		
		double total = 0;
		
		// Title Zone
		double nd	 = getAddDocData(dID).getTotalWordsInTitle();		// n(d)    : the number of terms in the document's zone
		double ndt	 = getAddWordData(wID).getTitleFreq(dID); 			// n(d,t)  : the number of occurrences of term t in the document d's zone
		double TF	 = 0;									  			// TF(d,t) = log(1 + n(d,t)/n(d))
		if(nd != 0)
			TF = Math.log10(1 + (ndt / nd));
		double DF	 = getAddWordData(wID).getNumOfDocsThatHaveTitle(); // DF(t)  : the number of document's for a particular zone that contain the term t
		double N	 = m_docID;						  		  			// N      : the number of documents in the collection
		double IDF 	 = 0;
		if(DF != 0.0)
			IDF   = Math.log10(N / DF); 								// IDF(t) = log(N/DF(t))
		total += titleWeight * (TF * IDF);							  	// TF-IDF(d,t) = TF(d,t) * IDF(t) * weight
		
		
		
		// Subject Zone
		nd	 = getAddDocData(dID).getTotalWordsInSubject();
		ndt	 = getAddWordData(wID).getSubjectFreq(dID); 		
		TF	 = 0;									  	
		if(nd != 0)
			TF = Math.log10(1 + (ndt / nd));
		DF	 = getAddWordData(wID).getNumOfDocsThatHaveSubject(); 
		N	 = m_docID;						  		  			
		IDF 	 = 0;
		if(DF != 0.0)
			IDF   = Math.log10(N / DF); 							
		total += subjectWeight * (TF * IDF);							  		

		
		
		// Body Zone
		nd	 = getAddDocData(dID).getTotalWordsInBody();
		ndt	 = getAddWordData(wID).getBodyFreq(dID); 		
		TF	 = 0;									  	
		if(nd != 0)
			TF = Math.log10(1 + (ndt / nd));
		DF	 = getAddWordData(wID).getNumOfDocsThatHaveBody(); 
		N	 = m_docID;						  		  			
		IDF 	 = 0;
		if(DF != 0.0)
			IDF   = Math.log10(N / DF); 							
		total += bodyWeight * (TF * IDF);							  		
		

		
		return total;
	}
	

	
	
}