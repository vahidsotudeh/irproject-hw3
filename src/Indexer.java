import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by Taghizadeh on 10/30/2015.
 */


public class Indexer extends CommonUI {
    @Override
    public void cleanIndex() {
        new File("index").delete();
    }

    @Override
    public void makeIndex(String indexPath, String corePath, String labelPath, String categoryPath, String commentPath) {
        PersianAnalyzer analyzer = new PersianAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter writer;
        Directory indexDir;
        FileInputStream inputStream;
        HSSFWorkbook workbook;
        Sheet sheet;
        Iterator rowIterator;

        try {

            indexDir = FSDirectory.open(new File(indexPath).toPath());
            writer = new IndexWriter(indexDir, cfg);

            //indexing core
            inputStream = new FileInputStream(corePath);
            workbook = new HSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
            rowIterator = sheet.iterator();

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
                doc.add(new TextField("coreBody", body.getStringCellValue(), Field.Store.YES));
                doc.add(new LongField("coreDate", date.getDateCellValue().getTime(), Field.Store.YES));
                doc.add(new TextField("coreUrl", url.getStringCellValue(), Field.Store.YES));
                doc.add(new TextField("source", source.getStringCellValue(), Field.Store.YES));
                writer.addDocument(doc);
            }

            //indexing category
            inputStream = new FileInputStream(categoryPath);
            workbook = new HSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
            rowIterator = sheet.iterator();

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
                doc.add(new StringField("categoryUrl", url.getStringCellValue(), Field.Store.YES));
                doc.add(new TextField("category", categ, Field.Store.YES));
                writer.addDocument(doc);

            }

            //indexing label
            inputStream = new FileInputStream(labelPath);
            workbook = new HSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
            rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row;
                Iterator cellIterator;
                row = (Row) rowIterator.next();
                Document doc = new Document();
                cellIterator = row.cellIterator();
                Cell url = (Cell) cellIterator.next();
                Cell source = (Cell) cellIterator.next();
                Cell label = (Cell) cellIterator.next();
                doc.add(new StringField("labelUrl", url.getStringCellValue(), Field.Store.YES));
                doc.add(new TextField("label", label.getStringCellValue(), Field.Store.YES));
                writer.addDocument(doc);
            }

            //indexing comment
            inputStream = new FileInputStream(commentPath);
            workbook = new HSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
            rowIterator = sheet.iterator();

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
                doc.add(new StringField("commentUrl", newsURL.getStringCellValue(), Field.Store.YES));
                doc.add(new TextField("commenter", commenter.getStringCellValue(), Field.Store.YES));
                doc.add(new StringField("location", location.getStringCellValue(), Field.Store.YES));
                doc.add(new LongField("commentDate", date.getDateCellValue().getTime(), Field.Store.YES));
                doc.add(new DoubleField("likeComment", likeComment.getNumericCellValue(), Field.Store.YES));
                doc.add(new DoubleField("dislikeComment", dislikeComment.getNumericCellValue(), Field.Store.YES));
                doc.add(new DoubleField("responsesCount", responsesCount.getNumericCellValue(), Field.Store.YES));
                doc.add(new TextField("commentBody", body.getStringCellValue(), Field.Store.YES));

                writer.addDocument(doc);
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Map<String,String> getallsetting(String q) {
        try {
            Map<String,String> alls=new HashMap<>();
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser parser = new QueryParser("coreUrl", new PersianAnalyzer());
            Query query = parser.parse(q);
            TopDocs hits = is.search(query, 1);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                alls.put("title",is.doc(scoreDoc.doc).get("title"));
                alls.put("news_number",is.doc(scoreDoc.doc).get("news_number"));
                alls.put("coreBody",is.doc(scoreDoc.doc).get("coreBody"));
                alls.put("coreDate",String.valueOf(is.doc(scoreDoc.doc).get("coreDate")));
            }
            return alls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getURLListByTitleSearch(String q) {
        try {
            ArrayList<String> urls = new ArrayList<>();
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser parser = new QueryParser("title", new PersianAnalyzer());
            Query query = parser.parse(q);
            TopDocs hits = is.search(query, 1000);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                urls.add(is.doc(scoreDoc.doc).get("coreUrl"));
            }

            return urls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getURLByCommentID(String q) {
        try {
            String res="";
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            Double id= Double.parseDouble(q);
            NumericRangeQuery query = NumericRangeQuery.newDoubleRange("id", id, id, true, true);
            TopDocs hits = is.search(query, 2);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                res=is.doc(scoreDoc.doc).get("commentUrl");
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    @Override
    public ArrayList<String> getURLListByBodySearch(String q) {
        try {
            ArrayList<String> urls = new ArrayList<>();
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser parser = new QueryParser("coreBody", new PersianAnalyzer());
            Query query = parser.parse(q);
            TopDocs hits = is.search(query, 1000);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                urls.add(is.doc(scoreDoc.doc).get("coreUrl"));
            }

            return urls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getURLListByBodySearchAndDateRange(String q, Date start, Date end) {
        try {
            ArrayList<String> urls = new ArrayList<>();
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser bodyParser = new QueryParser("coreBody", new PersianAnalyzer());
            BooleanQuery query = new BooleanQuery();
            Query bodyQuery = bodyParser.parse(q);
            Query dateQuery = NumericRangeQuery.newLongRange("coreDate", start.getTime(), end.getTime(), true, true);
            query.add(bodyQuery , BooleanClause.Occur.MUST);
            query.add(dateQuery , BooleanClause.Occur.MUST);
            TopDocs hits = is.search(query, 1000);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                urls.add(is.doc(scoreDoc.doc).get("coreUrl"));
            }

            return urls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getCommentIDByCommentBodySearchAndDateRange(String q, Date start, Date end) {
        try {
            ArrayList<String> ids  = new ArrayList<>();
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser bodyParser = new QueryParser("commentBody", new PersianAnalyzer());
            BooleanQuery query = new BooleanQuery();
            Query commentQuery = bodyParser.parse(q);
            Query dateQuery = NumericRangeQuery.newLongRange("commentDate", start.getTime(), end.getTime(), true, true);
            query.add(commentQuery , BooleanClause.Occur.MUST);
            query.add(dateQuery , BooleanClause.Occur.MUST);
            TopDocs hits = is.search(query, 1000);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                ids.add(is.doc(scoreDoc.doc).get("id"));
            }

            return ids;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getURLListByCommentBodySearchAndDateRange(String q, Date start, Date end) {
        try {
            ArrayList<String> urls  = new ArrayList<>();
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser bodyParser = new QueryParser("commentBody", new PersianAnalyzer());
            BooleanQuery query = new BooleanQuery();
            Query commentQuery = bodyParser.parse(q);
            Query dateQuery = NumericRangeQuery.newLongRange("commentDate", start.getTime(), end.getTime(), true, true);
            query.add(commentQuery , BooleanClause.Occur.MUST);
            query.add(dateQuery , BooleanClause.Occur.MUST);
            TopDocs hits = is.search(query, 1000);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                urls.add(is.doc(scoreDoc.doc).get("commentUrl"));
            }

            return urls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getURLListByLabel(ArrayList<String> labels) {
        try {
            ArrayList<String> urls = new ArrayList<>();
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser parser = new QueryParser("label", new PersianAnalyzer());
            BooleanQuery query = new BooleanQuery();
            for (String s : labels){
                Query q = parser.parse(s);
                query.add(q, BooleanClause.Occur.SHOULD);
            }
            TopDocs hits = is.search(query, 1000);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                urls.add(is.doc(scoreDoc.doc).get("labelUrl"));
            }

            return urls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getURLListByCategory(ArrayList<String> categories) {
        try {
            ArrayList<String> urls = new ArrayList<>();
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            QueryParser parser = new QueryParser("category", new PersianAnalyzer());
            BooleanQuery query = new BooleanQuery();
            for (String s : categories){
                Query q = parser.parse(s);
                query.add(q, BooleanClause.Occur.SHOULD);
            }
            TopDocs hits = is.search(query, 1000);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                urls.add(is.doc(scoreDoc.doc).get("categoryUrl"));
            }

            return urls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getCommentIDByCommneterWildCardQuery(String wildCardQuery) {

        try {
            ArrayList<String> urls = new ArrayList<>();
            IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File("index").toPath()));
            IndexSearcher is = new IndexSearcher(rdr);
            WildcardQuery query = new WildcardQuery(new Term("commenter",wildCardQuery));
            TopDocs hits = is.search(query, 1000);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                urls.add(is.doc(scoreDoc.doc).get("id"));
            }
            return urls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getURLListByCommpoundSearch(ArrayList<String> labels,
                                                         ArrayList<String> categories,
                                                         String NewsbodyQuery,
                                                         String commentBodyQuery, Date startNewsDate, Date endNewsDate) {
        try{
            ArrayList<String> urls;


            //labels
            ArrayList<String> labelUrls = getURLListByLabel(labels);

            //categories
            ArrayList<String> categoryUrls = getURLListByCategory(categories);

            //news
            ArrayList<String> newsUrls = getURLListByBodySearchAndDateRange(NewsbodyQuery, startNewsDate, endNewsDate);

            //comments
            ArrayList<String> commentsUrls = getURLListByCommentBodySearchAndDateRange(commentBodyQuery,startNewsDate,endNewsDate);


            urls = intersection(newsUrls, commentsUrls, labelUrls, categoryUrls);

            return urls;

        }catch (Exception e){
            e.printStackTrace();

        }


        return null;
    }

    @Override
    public void addToFavorite(String userID, String NewsURL) {

    }

    @Override
    public ArrayList<String> getTopRelatedNews(String userID) {
        return null;
    }

    public ArrayList<String> intersection( List<String> list1, List<String> list2, List<String> list3, List<String> list4) {
        ArrayList<String> result = new ArrayList<String>(list1);
        result.retainAll(list2);
        result.retainAll(list2);
        result.retainAll(list3);
        result.retainAll(list4);

        return result;
    }

}
