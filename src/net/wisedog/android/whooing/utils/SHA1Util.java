package net.wisedog.android.whooing.utils;
/* 
Copyright 2010 Cesar Valiente Gordo

This file is part of QuiteSleep.

 QuiteSleep is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 QuiteSleep is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with QuiteSleep.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* 
* @author Cesar Valiente Gordo
* @mail cesar.valiente@gmail.com
*
*/
public class SHA1Util {
    public static String SHA1(String data) {
        try {
            byte[] b = data.getBytes();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(b);
            byte messageDigest[] = md.digest();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                result.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
            }

            return result.toString();

        } catch (NoSuchAlgorithmException e) {

            // Log.e("ARTags", "SHA1 is not a supported algorithm");
        }
        return null;
    }

}
