/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com> (2014 Version)
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.g_node.gca.abstracts.Abstracts;
import com.g_node.gca.abstracts.FavoriteAbstracts;
import com.g_node.gca.map.MapActivity;
import com.g_node.gca.newsroom.NewsRoomActivity;
import com.g_node.gca.schedule.ScheduleMainActivity;
import com.g_node.ni17.R;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setTitle(R.string.app_name);
		//getActionBar().setIcon(getResources().getDrawable(R.drawable.icon_brain));	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.genInfo:
            Intent genInfo = new Intent(this, GeneralActivity.class);
            this.startActivity(genInfo);
            return true;		
		case R.id.abstracts_menue:
			Intent abstractIntent = new Intent(MainActivity.this, Abstracts.class);
			startActivity(abstractIntent);
            return true;
		case R.id.schedule_menue:
			Intent scheduleIntent = new Intent(MainActivity.this, ScheduleMainActivity.class);
			startActivity(scheduleIntent);
            return true;
		case R.id.favorites_menue:
			Intent favIntent = new Intent(MainActivity.this, FavoriteAbstracts.class);
			startActivity(favIntent);
            return true;
		case R.id.abtApp:
		{
			Builder aboutDialog = new AlertDialog.Builder(MainActivity.this);
			aboutDialog.setTitle("About the App:")
			.setMessage(Html.fromHtml("The <b>G-Node Conference Application</b> for Android serves as an electronic conference guide for participants with included proceedings. <br><br> &#169; <b>German Neuroinformatics Node</b><br><br>Created By: <b>Shumail Mohy-ud-Din</b> and <b>Christian Garbers</b><br>(as part of GSoC 2014)"))
			.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			}).setIcon(getResources().getDrawable(R.drawable.launcher))
			 .show();
		}	
	        return true;
		
		}
			
		return false;
		
	}
}



