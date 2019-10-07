# Generate-Lucene-Index
A corpus of many trectext files are used where there is a grand total of 84474 documents (I have only uploaded 10 files in GitHub which will contain approximately 300 documents). Our task is to create a Lucene index for all these documents where we need to take care of merging content from the same tag in the same document.

For instance:
<DOC>
<DOCNO> 123142 </DOCNO>
<HEAD> Our task is to generate a Lucene index. </HEAD>
<HEAD> Our task is to also test different analyzers. </HEAD>
</DOC>

will become:
<DOC>
<DOCNO> 123142 </DOCNO>
<HEAD> Our task is to generate a Lucene index. Our task is to also test different analyzers. </HEAD>
</DOC>

Once we generate the index, we test different analyzers that performs different functionalities, some analyzers may remove stop words and/or stemming and some do not.
