package jlnshen.playstorutil;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;

import jlnshen.playutil.PlayUtil;


public class MyTestActivity extends ActionBarActivity {

    public static final String TAG = "MyTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    List<PlayUtil.Category> categories = PlayUtil.getCategories();

                    for(PlayUtil.Category c:categories) {
                        Log.d(TAG, "category name:" + c.name());
                    }

                    PlayUtil.Category c = PlayUtil.getCategory("com.facebook.katana");

                    if(c != null) {
                        Log.d(TAG, "CAT=" + c.name());
                    }

                    try {
                        c = PlayUtil.getCategory("wrong.package_name");
                        if (c != null) {
                            Log.d(TAG, "CAT=" + c.name()); //Should never happen
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Wrong package", e);
                    }

                    Log.d(TAG, PlayUtil.getCoverImage("com.facebook.katana"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
