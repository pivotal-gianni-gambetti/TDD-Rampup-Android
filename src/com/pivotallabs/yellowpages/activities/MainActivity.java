package com.pivotallabs.yellowpages.activities;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.pivotallabs.yellowpages.R;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity implements OnClickListener {
	
	@InjectView(R.id.what) EditText whatField;
	@InjectView(R.id.where) EditText whereField;
	@InjectView(R.id.start_search) Button search;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        search.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void doSearch(){
    	String what = whatField.getText().toString();
    	String where = whereField.getText().toString();
    	
    	if(what.length() > 0 && where.length() > 0){
    		SearchActivity.start(this, what, where);
    	}
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.start_search:
				doSearch();
				break;
		}
	}
    
}
