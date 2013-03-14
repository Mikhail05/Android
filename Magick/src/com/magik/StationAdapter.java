package com.magik;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StationAdapter extends CursorAdapter
{
    private DataSource dbAdapter = null;
 
    @SuppressWarnings("deprecation")
	public StationAdapter(Context context, Cursor c)
    {
        super(context, c);
        dbAdapter = new DataSource(context);
        dbAdapter.open();
    }
    
    public void bindView(View view, Context context, Cursor cursor)
    {
        String item = createItem(cursor);     
        ((TextView) view).setText(item);      
    }
    
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final TextView view = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
       
        String item = createItem(cursor);
        view.setText(item);
        return view;
    }
 
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint)
    {
    	
        Cursor currentCursor = null;
        
        if (getFilterQueryProvider() != null)
        {
        	
            return getFilterQueryProvider().runQuery(constraint);
        }
        
        String args = "";
        
        if (constraint != null)
        {
        	
            args = constraint.toString();      
         
        }
      
        currentCursor = dbAdapter.getStationCursor(args);
       
        return currentCursor;
    }
    
    private String createItem(Cursor cursor)
    {
        String item = cursor.getString(2);      
        return item;
    }
    
    public void close()
    {
        dbAdapter.close();
    }
}