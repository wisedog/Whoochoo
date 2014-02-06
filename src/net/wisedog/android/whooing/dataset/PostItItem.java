/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing.dataset;

/**
 * Data set class for PostIt items
 */
public class PostItItem{
    public PostItItem(final int _id, final String _page, final String _everywhere, final String _content) {
        super();
        id = _id;
        page = _page;
        everywhere = _everywhere;
        content = _content;
    }

    /* Below are sample data of post it item
		{
            "post_it_id" : 13,
            "page" : "_dashboard",
            "everywhere" : "n",
            "contents" : "포스트잇의 내용1"
        },*/

    
    /** Post It id*/
    public int id;
    /** Attached page */
    public String page;
    /** Flag for showing everywhere */
    public String everywhere;
    /** Contents of Post It!*/
    public String content;    
}
