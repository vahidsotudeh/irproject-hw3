import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        Indexer indexer = new Indexer();
//        indexer.cleanIndex();
//        indexer.makeIndex("index", "data/core.xls", "data/label.xls", "data/category.xls", "data/comments.xls");
//        long startTime = System.currentTimeMillis();
//        ArrayList<String>urls = indexer.getCommentIDByCommentBodySearchAndDateRange("استیضاح", new Date("2000/01/01"), new Date("2016/01/01"));

//        ArrayList<String>urls = indexer.getURLListByBodySearchAndDateRange("حیثیت", new Date("2000/01/01"), new Date("2016/01/01"));

        ArrayList<String> labels = new ArrayList<>();
        labels.add("شیراز");
        ArrayList<String>urls = indexer.getURLListByLabel(labels);
//        Map<String,String> mp=indexer.getallsetting(urls.get(0));
//        System.out.println(mp.toString());
        JSONObject jsonObject=new JSONObject();
//        jsonObject.put(urls.get(0),mp);
//        ArrayList<String>urls = indexer.getURLListByCommentBodySearchAndDateRange("مستحجن", new Date("2000/01/01"), new Date("2016/01/01"));


//        ArrayList<String> categories = new ArrayList<>();
//        categories.add("سیاسی");

//        ArrayList<String> urls = indexer.getURLListByCategory(categories);

//        ArrayList<String> urls = indexer.getURLListByCommpoundSearch(labels, categories, "حیثیت", "مستحجن ", new Date("2000/01/01"), new Date("2016/01/01"));

//        ArrayList<String> urls = indexer.getCommentIDByCommneterWildCardQuery("mar*");
//        long endTime = System.currentTimeMillis();
//        System.out.println("found in: " + (endTime - startTime) + " ms");
//        System.out.println(urls.toString());


    }
}
