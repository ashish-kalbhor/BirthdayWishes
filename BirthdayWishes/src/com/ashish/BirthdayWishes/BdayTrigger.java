/**
 * Copyright 2012 Ashish Kalbhor (ashish.kalbhor@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.ashish.BirthdayWishes;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.text.format.Time;

/**
 * Trigger class to scan device contacts and match current date with contact birthday.
 * 
 * @author Ashish Kalbhor <ashish.kalbhor@gmail.com>
 * 
 */

public class BdayTrigger extends Service
{
	@Override
	public void onCreate()
	{
		SmsAction smsAction = new SmsAction();

		Cursor cursor = this.getcontactbirthday();
		int birthday = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
		int contactname = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.DISPLAY_NAME);
		int contactid = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.CONTACT_ID);

		Time now = new Time();
		now.setToNow();
		String mtoday = now.toString(); // getting the current date

		// ///Splitting todays date and Re-formatting//////////////////
		String year = mtoday.substring(0, 4);
		String month = mtoday.substring(4, 6);
		String day = mtoday.substring(6, 8);
		String today = year + "-" + month + "-" + day;

		while ( cursor.moveToNext() )
		{
			String bday = cursor.getString(birthday);

			if ( bday.equalsIgnoreCase(today) ) // When the date matches !
			{
				String cName = cursor.getString(contactname);
				String cId = cursor.getString(contactid);
				String cNumber = null;
				// /////Another Cursor for fetching Contact Number//////////

				Cursor mcursor = this.getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI, null,
						CommonDataKinds.Phone.CONTACT_ID + "=?", new String[] { cId }, null);
				int contactnumber = mcursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

				while ( mcursor.moveToNext() )
				{
					cNumber = mcursor.getString(contactnumber);
				}
				// ///////////////////////////////////////////////////////

				smsAction.onCreate(this.getApplicationContext());
				smsAction.perform(cName, cNumber);
			}
		}// End of Loop
	}


	/**
	 * 
	 * @return Cursor with Birthday and Phone Number
	 */

	private Cursor getcontactbirthday()
	{
		Uri uri = ContactsContract.Data.CONTENT_URI;

		String[] projection = new String[] { ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Event.CONTACT_ID, ContactsContract.CommonDataKinds.Event.START_DATE };

		String where = ContactsContract.Data.MIMETYPE + "= ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + "="
				+ ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;

		String[] selectionargs = new String[] { ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE };

		String sortOrder = null;

		return this.getContentResolver().query(uri, projection, where, selectionargs, sortOrder);
	}


	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

}