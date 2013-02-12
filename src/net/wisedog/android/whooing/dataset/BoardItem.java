/**
 * 
 */
package net.wisedog.android.whooing.dataset;

/**
 * For board list item
 * @author Wisedog(me@wisedog.net)
 */
public class BoardItem{

    public BoardItem(final int _id, final String _userName, final int _commentNum, final String _subject) {
        super();
        id = _id;
        userName = _userName;
        commentNum = _commentNum;
        subject = _subject;
    }
    public int id;
    public String subject;
    public String contents;
    public int commentNum;
    public int hits;
    public String userName;
    public int recommandation;
    public int userId;
    public int userLevel;
    public String userImage;
    public long timestamp;
	public String category;
    
}
