/**
 * 
 */
package net.wisedog.android.whooing.dataset;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class PostItItem{

    /*
{
            "post_it_id" : 13,
            "page" : "_dashboard",
            "everywhere" : "n",
            "contents" : "포스트잇의 내용1"
        },
     * */
    public PostItItem(final int _id, final String _page, final String _everywhere, final String _content) {
        super();
        id = _id;
        page = _page;
        everywhere = _everywhere;
        content = _content;
    }
    public int id;
    public String page;
    public String everywhere;
    public String content;    
}
