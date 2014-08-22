/**
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * Copyright (c) 2013, Yasir Adnan <adnan.ayon@gmail.com> - (old version)
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.abstracts;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.g_node.gcaa.R;

public class AbstractCursorAdapter extends CursorAdapter {
    Cursor cursorOne;

    String getName;

    @SuppressWarnings("deprecation")
    public AbstractCursorAdapter(Context context, Cursor c) {
        super(context, c);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        /*
         * TextView for showing Title
         */
        TextView title = (TextView)view.findViewById(R.id.abTitle);
        title.setText(cursor.getString(cursor.getColumnIndexOrThrow("TITLE")));
        /*
         * TextView for showing Topic
         */
        TextView topic = (TextView)view.findViewById(R.id.abTopic);
        topic.setText(cursor.getString(cursor.getColumnIndexOrThrow("TOPIC")));
        /*
         * TextView for Showing Type
         */
        TextView type = (TextView)view.findViewById(R.id.abType);
        int absSortID = cursor.getInt(cursor.getColumnIndexOrThrow("SORTID"));
        
        if(absSortID != 0) {	
        	int groupid =  ((absSortID & (0xFFFF << 16)) >> 16);
        	
        	switch (groupid) {
			case 1:
				type.setText("TALK");
				type.setBackgroundColor(Color.parseColor("#33B5E5"));
				break;
			
			case 2:
				type.setText("TALK & POSTER");
				type.setBackgroundColor(Color.parseColor("#ef4172"));
				break;
			
			case 3:
				type.setText("POSTER");
				type.setBackgroundColor(Color.parseColor("#AA66CC"));
				break;
				
			case 4:
				type.setText("POSTER");
				type.setBackgroundColor(Color.parseColor("#AA66CC"));
				break;	
			default:
				break;
			}
        	
        	//absSortID.append("Group ID: " + get_groupid_str(groupid));
        	
        }else {
        	//type.setVisibility(View.GONE);
        	type.setTextSize(TypedValue.COMPLEX_UNIT_SP,4);
        	type.setVisibility(View.INVISIBLE);
        }

        /*
        Following piece of commented code isn't needed as Abstract Type is 
        selected now based on GroupID. Previously it was being selected based on 'isTalk' field
        but now it's selected on basis of GroupID extracted from SortID. 
        
        String typeText = cursor.getString(cursor.getColumnIndexOrThrow("TYPE"));
        type.setText(typeText.toUpperCase());
        
        if(typeText.compareToIgnoreCase("poster") == 0) {
        	type.setBackgroundColor(Color.parseColor("#AA66CC"));
        } else {
        	type.setBackgroundColor(Color.parseColor("#33B5E5"));
        }
        */
        
        /*
         * _id for getting Author Names
         */
        String value = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        String authorNamesQuery = 	"SELECT ABSTRACT_UUID, AUTHORS_DETAILS.AUTHOR_UUID, AUTHORS_DETAILS.AUTHOR_FIRST_NAME, AUTHOR_MIDDLE_NAME, AUTHOR_LAST_NAME, AUTHOR_EMAIL, ABSTRACT_AUTHOR_POSITION_AFFILIATION.AUTHOR_POSITION as _aPos FROM ABSTRACT_AUTHOR_POSITION_AFFILIATION LEFT OUTER JOIN AUTHORS_DETAILS USING (AUTHOR_UUID) WHERE ABSTRACT_UUID = '" + value + "' ORDER BY _aPos;";
        cursorOne =  DatabaseHelper.database.rawQuery(authorNamesQuery, null);
        
      if (cursorOne != null) {
      cursorOne.moveToFirst();
      /*
       * Name format will be like this A, B & C or A,B,C & D. So, if the
       * name is the last name. We should use '&' before the name
       */
      do {

          if (cursorOne.getPosition() == 0) {
              /*
               * First data
               */
              getName = cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_FIRST_NAME")) + " " + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_LAST_NAME"));
          } else if (cursorOne.isLast()) {
              /*
               * Last Data
               */
              getName = getName + " & "
                      + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_FIRST_NAME")) + " " + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_LAST_NAME"));
          } else {
              getName = getName + " , "
                      + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_FIRST_NAME")) + " " + cursorOne.getString(cursorOne.getColumnIndexOrThrow("AUTHOR_LAST_NAME"));

          }

      } while (cursorOne.moveToNext());
  }
        
        /*
         * TextView for Author Names
         */
        TextView authorNames = (TextView)view.findViewById(R.id.absAuthorNames);
        /*
         * Get Width
         */
        WindowManager WinMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displayWidth = WinMgr.getDefaultDisplay().getWidth();
        Paint paint = new Paint();
        Rect bounds = new Rect();
        int text_width = 0;
        paint.getTextBounds(getName, 0, getName.length(), bounds);
        /*
         * Getting Text Width
         */
        text_width = bounds.width();
        /*
         * If Text Width is greater than Display Width Then show First Name et
         * al.
         */
        if (text_width > displayWidth) {
            String output = getName.split(",")[0] + " et al. ";
            authorNames.setText(output);

        } else {
            /*
             * Name Format will be like this If the Name is Yasir Adnan So,It
             * will be Y.Adnan
             */
            authorNames
            .setText(getName.replaceAll("((?:^|[^A-Z.])[A-Z])[a-z]*\\s(?=[A-Z])", "$1."));
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewgroup) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(viewgroup.getContext());
        View returnView = inflater.inflate(R.layout.abstract_content, viewgroup, false);
        return returnView;
    }
}
