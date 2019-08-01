# IR-Document-Ranking-TF-IDF
# Uses a combination of the TF-IDF approach and weighted zone scoring to process user queries. 


Each document should contain three “zones”.

The first zone is the first line of the document. For the example documents, this contains the course area, number, and title.

The second zone should be the first sentence after the title line. This contains the "description" of the document.

The final zone is the remainder of the document.

Each zone has a separate weight associated with it, thus a word appearing in the title zone is more important than one appearing in the body zone, making a document rank higher on the search.
