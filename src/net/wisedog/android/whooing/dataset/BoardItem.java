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
 * A class for holding board list item
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
