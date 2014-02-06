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
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BillMonthlyItem{

	/*Date like this : 
	 {"end_use_date":20130131,"money":0,"pay_date":28,"account_id":"x76",
	     * "start_use_date":20130101}*/
    public BillMonthlyItem(int startUseDate, int endUseDate, 
    		String accountName, int paymentDate, double amount) {
        super();
        this.startUseDate = startUseDate;
        this.endUseDate = endUseDate;
        this.accountName = accountName;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }
    public int startUseDate;
    public int endUseDate;
    public String accountName;
    public int paymentDate;
    public double amount;    
}
