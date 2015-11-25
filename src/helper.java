import org.apache.lucene.analysis.fa.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Microsoft on 26/10/2015.
 */
public class helper {
    private IndexWriter writer;

    private static boolean searchCore = true;
    private static boolean searchComments = true;
    private static boolean searchCategory = true;
    private static boolean searchLabel = true;


    private static int topDocs = 10;

    private static final int INDEXING_CORE = 0;
    private static final int INDEXING_CATEGORY = 1;
    private static final int INDEXING_COMMENTS = 2;
    private static final int INDEXING_LABALE = 3;

    protected void getDocument(File f) throws Exception {
        Directory indexDir = FSDirectory.open(new File("index\\" + f.getName()).toPath());
        PersianAnalyzer analyzer = new PersianAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(indexDir, cfg);

        int which;
        if (f.getName().equals("core.xls"))
            which = 0;
        else if (f.getName().equals("category.xls"))
            which = 1;
        else if (f.getName().equals("comments.xls"))
            which = 2;
        else
            which = 3;

        FileInputStream inputStream;
        HSSFWorkbook workbook;
        Sheet sheet;

        inputStream = new FileInputStream(f);
        workbook = new HSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt(0);
        Iterator rowIterator = sheet.iterator();

        switch (which) {
            case INDEXING_CORE:
                while (rowIterator.hasNext()) {
                    Row row;
                    Iterator cellIterator;
                    Document doc = new Document();
                    row = (Row) rowIterator.next();
                    cellIterator = row.cellIterator();
                    Cell news_number = (Cell) cellIterator.next();
                    Cell title = (Cell) cellIterator.next();
                    Cell body = (Cell) cellIterator.next();
                    Cell date = (Cell) cellIterator.next();
                    Cell url = (Cell) cellIterator.next();
                    Cell source = (Cell) cellIterator.next();
                    doc.add(new DoubleField("news_number", news_number.getNumericCellValue(), Field.Store.YES));
                    doc.add(new TextField("title", title.getStringCellValue(), Field.Store.YES));
                    doc.add(new TextField("body", body.getStringCellValue(), Field.Store.YES));
                    doc.add(new LongField("date", date.getDateCellValue().getTime(), Field.Store.YES));
                    doc.add(new TextField("url", url.getStringCellValue(), Field.Store.YES));
                    doc.add(new TextField("source", source.getStringCellValue(), Field.Store.YES));
                    doc.add(new StringField("filename", f.getName(), Field.Store.YES));
                    doc.add(new StringField("fullpath", f.getCanonicalPath(), Field.Store.YES));
                    writer.addDocument(doc);
                }
                break;
            case INDEXING_CATEGORY:
                while (rowIterator.hasNext()) {
                    Row row;
                    String categ = " ";
                    Iterator cellIterator;
                    Document doc = new Document();
                    row = (Row) rowIterator.next();
                    cellIterator = row.cellIterator();
                    Cell url = (Cell) cellIterator.next();
                    if (cellIterator.hasNext())
                        categ = ((Cell) cellIterator.next()).getStringCellValue();
                    doc.add(new StringField("url", url.getStringCellValue(), Field.Store.YES));
                    doc.add(new StringField("category", categ, Field.Store.YES));
                    writer.addDocument(doc);

                }
                break;
            case INDEXING_COMMENTS:
                while (rowIterator.hasNext()) {
                    Row row = (Row) rowIterator.next();
                    Iterator cellIterator;
                    Document doc = new Document();
                    cellIterator = row.cellIterator();
                    Cell id = (Cell) cellIterator.next();
                    Cell prentId = (Cell) cellIterator.next();
                    Cell newsURL = (Cell) cellIterator.next();
                    Cell commenter = (Cell) cellIterator.next();
                    Cell location = (Cell) cellIterator.next();
                    Cell date = (Cell) cellIterator.next();
                    Cell likeComment = (Cell) cellIterator.next();
                    Cell dislikeComment = (Cell) cellIterator.next();
                    Cell responsesCount = (Cell) cellIterator.next();
                    Cell body = (Cell) cellIterator.next();

                    doc.add(new DoubleField("id", id.getNumericCellValue(), Field.Store.YES));
                    doc.add(new DoubleField("parentId", prentId.getNumericCellValue(), Field.Store.YES));
                    doc.add(new StringField("url", newsURL.getStringCellValue(), Field.Store.YES));
                    doc.add(new TextField("commenter", commenter.getStringCellValue(), Field.Store.YES));
                    doc.add(new StringField("location", location.getStringCellValue(), Field.Store.YES));
                    doc.add(new LongField("date", date.getDateCellValue().getTime(), Field.Store.YES));
                    doc.add(new DoubleField("likeComment", likeComment.getNumericCellValue(), Field.Store.YES));
                    doc.add(new DoubleField("dislikeComment", dislikeComment.getNumericCellValue(), Field.Store.YES));
                    doc.add(new DoubleField("responsesCount", responsesCount.getNumericCellValue(), Field.Store.YES));
                    doc.add(new StringField("body", body.getStringCellValue(), Field.Store.YES));

                    writer.addDocument(doc);
                }
                break;
            case INDEXING_LABALE:
                while (rowIterator.hasNext()) {
                    Row row;
                    Iterator cellIterator;
                    row = (Row) rowIterator.next();
                    Document doc = new Document();
                    cellIterator = row.cellIterator();
                    Cell url = (Cell) cellIterator.next();
                    Cell source = (Cell) cellIterator.next();
                    Cell label = (Cell) cellIterator.next();
                    doc.add(new StringField("url", url.getStringCellValue(), Field.Store.YES));
                    doc.add(new StringField("source", source.getStringCellValue(), Field.Store.YES));
                    doc.add(new StringField("label", label.getStringCellValue(), Field.Store.YES));
                    writer.addDocument(doc);
                }
                break;

        }

        writer.close();

    }

    public void indexFile(File f) throws Exception {
        getDocument(f);
    }


    public void index(String dataDir, FileFilter filter)
            throws Exception {
        File[] files = new File(dataDir).listFiles();
        for (File f : files) {
            if ((filter == null || filter.accept(f))) {
                indexFile(f);
            }
        }
    }


    public static void search(String q) throws Exception {
        boolean smartSearch = q.contains(":");
        String args[] = q.split(" ");
        String bodies[] = null;
        String comments[] = null;
        String labels[] = null;
        String categories[] = null;
        long startTime = 0;
        long endTime = 0;
        for (String s : args) {
            if (s.contains("body:")) {
                bodies = s.replace("body:", "").split(",");
            } else if (s.contains("comment:")) {
                comments = s.replace("comment:", "").split(",");
            } else if (s.contains("label:")) {
                labels = s.replace("label:", "").split(",");
            } else if (s.contains("category:")) {
                categories = s.replace("category", "").split(",");
            } else if (s.contains("startTime:")) {
                startTime = new Date(s.replace("startTime:", "")).getTime();
            } else if (s.contains("endTime:")) {
                endTime = new Date(s.replace("endTime:", "")).getTime();
            }
        }

        if (smartSearch)
            return;
        if (searchCore) {
            System.out.println("results in core:");
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index\\core.xls").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser parser = new QueryParser("body", new StandardAnalyzer());
            Query query = parser.parse(q);
            TopDocs hits = is.search(query, 10);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = is.doc(scoreDoc.doc);
                System.out.println(doc.get("url"));
            }
        }
        if (searchComments) {
            System.out.println("results in comments:");
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index\\comments.xls").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser parser = new QueryParser("body", new StandardAnalyzer());
            Query query = parser.parse(q);
            TopDocs hits = is.search(query, 10);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = is.doc(scoreDoc.doc);
                System.out.println(doc.get("url"));
            }

        }
        if (searchCategory) {
            System.out.println("results in category:");
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index\\category.xls").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser parser = new QueryParser("category", new StandardAnalyzer());
            Query query = parser.parse(q);
            TopDocs hits = is.search(query, 10);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = is.doc(scoreDoc.doc);
                System.out.println(doc.get("url"));
            }
        }

        if (searchLabel) {
            System.out.println("results in label:");
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index\\label.xls").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser parser = new QueryParser("label", new StandardAnalyzer());
            Query query = parser.parse(q);
            TopDocs hits = is.search(query, 10);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = is.doc(scoreDoc.doc);
                System.out.println(doc.get("url"));
            }
        }

    }


}

