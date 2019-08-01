// Each doc will have a DocData. This keeps track of how many words are in a doc, as well as which zone they are in
// This is used to calc TF_IDF
public class DocData
{
	private int totalWords, totalTitle, totalSubject, totalBody;

	DocData()
	{
		totalWords = 0;
		totalTitle = 0;
		totalSubject = 0;
		totalBody = 0;
	}
	
	public int getTotalNumOfWords()
	{
		return totalWords;
	}
	
	public int getTotalWordsInTitle()
	{
		return totalTitle;
	}
	public int getTotalWordsInSubject()
	{
		return totalSubject;
	}
	public int getTotalWordsInBody()
	{
		return totalBody;
	}
	
	public void addWord(boolean title, boolean subject, boolean body)
	{
		totalWords++;
		if(title)
			totalTitle++;
		else if(subject)
			totalSubject++;
		else// if(body)
			totalBody++;
	}
}
