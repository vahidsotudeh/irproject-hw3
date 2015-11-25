import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Taghizadeh on 10/30/2015.
 */
public abstract class CommonUI {

    // this function remove the old Index if exists
    public abstract void cleanIndex();

    // this function create the Index and put it in indexPath
    public abstract void makeIndex(String indexPath, String corePath,String labelPath, String categoryPath, String commentPath);

    // this function return list of URLs by search in title of the news
    public abstract ArrayList<String> getURLListByTitleSearch(String query);

    // this function return list of URLs by search in title of the news
    public abstract ArrayList<String> getURLListByBodySearch(String query);

    // this function return list of URLs by search in body of the news and Date filter on News
    public abstract void addToFavorite(String userID, String NewsURL);
    public abstract ArrayList<String> getTopRelatedNews(String userID);
    public abstract ArrayList<String> getURLListByBodySearchAndDateRange(String query, Date start, Date end);
    public abstract Map<String,String> getallsetting(String q);
    public abstract String getURLByCommentID(String q);

    // this function return id of comments by search in body of the comment and Date filter on News
    public abstract ArrayList<String> getCommentIDByCommentBodySearchAndDateRange(String query, Date start, Date end);

    // this function return list of URLS by search in body of the comment and Date filter on Comments
    public abstract ArrayList<String> getURLListByCommentBodySearchAndDateRange(String query, Date start, Date end);

    // List of URLS by label
    public abstract ArrayList<String> getURLListByLabel(ArrayList<String> labels);

    // search Comments by commenter Wild card query should be supported
    public abstract ArrayList<String> getCommentIDByCommneterWildCardQuery(String wildCardQuery);

    // return URL list by applying filter on
    // 1) labels (if labels is null then the function ignores the label of news)
    // 2) categories (if categories is null then the function ignores the category of news)
    // 3) newsBodyQuery (if NewsbodyQuery is null then the function ignores the body filtering
    // 4)commentBodyQuery (if NewsbodyQuery is null then the function ignores the body filtering
    // 5) startNewsDate (if startNewsDate is null then the function return all urls with publish date before than endNewsDate
    // 6) endNewsDate (if endNewsDate is null then the function return all urls with publish date after than endNewsDate
    // 7) if all filters are null the function should throw an Exception

    public abstract ArrayList<String> getURLListByCommpoundSearch(ArrayList<String> labels, ArrayList<String> categories,
                                                                  String NewsbodyQuery, String commentBodyQuery, Date startNewsDate, Date endNewsDate);


}