/**
 * 
 */
package net.wisedog.android.whooing.dataset;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BoardItem{

    public BoardItem(final int _id, final String _userName, final int _commentNum, final String _content) {
        super();
        id = _id;
        userName = _userName;
        commentNum = _commentNum;
        content = _content;
    }
    public int id;
    public String content;
    public int commentNum;
    public String userName;
}
