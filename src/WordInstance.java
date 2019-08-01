
import java.util.ArrayList;
import java.util.List;

// Each instance of a word will keep track of a document and the number of times
// that word appears in that doc (in the title, subject, and/or body).
// It also contains an TF_IDF which represents the weight of that word in for that doc
public class WordInstance
{
	public int docID;					// The doc associated with the frequency counter for the word
	public int titleFreq, subjectFreq, bodyFreq, wordFreq;
										// The frequency of the word in this document
	public double TF_IDF;
	
	WordInstance(Integer d, boolean t, boolean s, boolean b)
	{
		docID = d;
		titleFreq = 0;
		subjectFreq = 0;
		bodyFreq = 0;
		wordFreq = 1;
		TF_IDF = -1;
		updateZoneData(t, s, b);
	}
	
	// Add another instance of the word to the doc, specifying what zone it is in
	public void updateZoneData(boolean t, boolean s, boolean b)
	{
		wordFreq++;
		
		if(b)
			bodyFreq++;
		else if(t)
			titleFreq++;
		else if(s)
			subjectFreq++;
	}
	
	
	
	
	
	
	
}

