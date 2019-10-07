import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;
import java.util.Arrays;

public class indexComparison
{
	
		private static void AnalyzerFunction(Analyzer analyzer, String name) throws IOException
		{
		
		System.out.println(name);
		
		File folder = new File("/Users/varunmiranda/Desktop/corpus");
		File[] listOfFiles = folder.listFiles();
			
		String indexPath = "/Users/varunmiranda/Desktop/IUB/Courses/SEMESTER 3/Search/Assignment 1";

		Directory dir = FSDirectory.open(Paths.get(indexPath));
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

		iwc.setOpenMode(OpenMode.CREATE);

		IndexWriter writer = new IndexWriter(dir, iwc);

		for (File file : listOfFiles) 
		{
		    if (file.isFile()) 
		    {

		    	String FullFile = FileUtils.readFileToString(file); 
				
				String[]doc = StringUtils.substringsBetween(FullFile, "<DOC>", "</DOC>");
				
				for (String i:doc)
				{				
					Document luceneDocument = new Document();
					
					String[] text = StringUtils.substringsBetween(i, "<TEXT>", "</TEXT>");
					String TEXT = Arrays.toString(text);
					if(TEXT != null)
					{
						luceneDocument.add(new TextField("TEXT", TEXT,Field.Store.YES));
					}
		            writer.addDocument(luceneDocument);

				}

		    }
		}
		writer.close();
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("/Users/varunmiranda/Desktop/IUB/Courses/SEMESTER 3/Search/Assignment 1")));		
		System.out.println("Total number of documents in the corpus:"+ reader.maxDoc());
		Terms vocabulary = MultiFields.getTerms(reader, "TEXT");		
		System.out.println("Number of documents that have at least one term for this field: "+ vocabulary.getDocCount());
		System.out.println("Number of tokens for this field: "+ vocabulary.getSumTotalTermFreq());
		System.out.println("Number of postings for this field: "+ vocabulary.getSumDocFreq());

		TermsEnum iterator = vocabulary.iterator();
		BytesRef byteRef = null;
		System.out.println("\n****Vocabulary-Start***");
		int c = 0;
		while ((byteRef = iterator.next()) != null) {
			c+=1;
		}
		System.out.println("\n****Vocabulary-End***");
		System.out.println(name+" Vocabulary Size: "+c);
		reader.close();

	}

		public static void main(String[] args) throws IOException {
	
			Analyzer analyzer1 = new StandardAnalyzer();
			Analyzer analyzer2 = new SimpleAnalyzer();
			Analyzer analyzer3 = new StopAnalyzer();
			Analyzer analyzer4 = new KeywordAnalyzer();
			
			AnalyzerFunction(analyzer1,"Standard Analyzer");
			AnalyzerFunction(analyzer2, "Simple Analyzer");
			AnalyzerFunction(analyzer3, "Stop Analyzer");
			AnalyzerFunction(analyzer4, "Keyword Analyzer");
			
		}
}
