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

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity Class to set trigger according to Alarm Service
 * 
 * @author Ashish Kalbhor <ashish.kalbhor@gmail.com>
 * 
 */

public class BirthdayWishesActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);

		Intent intent = new Intent(BirthdayWishesActivity.this, BdayTrigger.class);
		PendingIntent pIntent = PendingIntent.getService(BirthdayWishesActivity.this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmmgr = (AlarmManager)this.getSystemService(ALARM_SERVICE);
		Calendar midnight = Calendar.getInstance();
		midnight.setTimeInMillis(System.currentTimeMillis());
		midnight.set(Calendar.HOUR_OF_DAY, 0);
		midnight.set(Calendar.MINUTE, 1);
		midnight.set(Calendar.SECOND, 0);
		midnight.set(Calendar.MILLISECOND, 0);
		// Scan time set to Midnight

		/**
		 * BdayTrigger class is Triggered daily according to Alarm Manager Service
		 */
		alarmmgr.setRepeating(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);
	}

}