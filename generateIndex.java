import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;


public class generateIndex
{
	public static void main(String[] args) throws IOException {
		
		File folder = new File("/Users/varunmiranda/Desktop/IUB/Courses/SEMESTER 3/Search/corpus");
		File[] listOfFiles = folder.listFiles();
		
		String indexPath = "/Users/varunmiranda/Desktop/IUB/Courses/SEMESTER 3/Search/Assignment 1";
		
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

		iwc.setOpenMode(OpenMode.CREATE);

		IndexWriter writer = new IndexWriter(dir, iwc);

		int file_count = 0;
		int doc_count = 0;
		for (File file : listOfFiles) 
		{
		    if (file.isFile()) 
		    {
		    	file_count=file_count + 1;
		    	System.out.println("File No." + file_count + " Read");
		    	String FullFile = FileUtils.readFileToString(file); 
				
				String[]doc = StringUtils.substringsBetween(FullFile, "<DOC>", "</DOC>");
				
				for (String i:doc)
				{				
					doc_count = doc_count + 1;
					Document luceneDocument = new Document();
					String[] docno = StringUtils.substringsBetween(i, "<DOCNO>", "</DOCNO>");
					String DOCNO = Arrays.toString(docno);
					if(DOCNO != null)
					{
						luceneDocument.add(new StringField("DOCNO", DOCNO,Field.Store.YES));
					}
					
					String[] head = StringUtils.substringsBetween(i, "<HEAD>", "</HEAD>");
					String HEAD = Arrays.toString(head);
					if(HEAD != null)
					{
						luceneDocument.add(new TextField("HEAD", HEAD,Field.Store.YES));
					}
					
					String[] byline = StringUtils.substringsBetween(i, "<BYLINE>", "</BYLINE>");
					String BYLINE = Arrays.toString(byline);
					if(BYLINE != null)
					{
						luceneDocument.add(new TextField("BYLINE", BYLINE,Field.Store.YES));
					}
					
					String[] dateline = StringUtils.substringsBetween(i, "<DATELINE>", "</DATELINE>");
					String DATELINE = Arrays.toString(dateline);
					if(DATELINE != null)
					{
						luceneDocument.add(new TextField("DATELINE", DATELINE,Field.Store.YES));
					}
					
					String[] text = StringUtils.substringsBetween(i, "<TEXT>", "</TEXT>");
					String TEXT = Arrays.toString(text);
					if(TEXT != null)
					{
						luceneDocument.add(new TextField("TEXT", TEXT,Field.Store.YES));
					}
		            writer.addDocument(luceneDocument);

					System.out.println("Document No." + doc_count + " Indexed");
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
		reader.close();

	}
}