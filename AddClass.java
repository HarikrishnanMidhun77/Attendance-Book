package com.nssce_cse.harikrishnanp.attendancebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.nssce_cse.harikrishnanp.attendancebook.database.DB_New;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddClass extends ActionBarActivity {
    DB_New db_new;
    ListView listCls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Classes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkRed));
        }

        db_new=new DB_New(getApplicationContext());
        listCls=(ListView)findViewById(R.id.listClass);
        ArrayList<String> subClass=new ArrayList<String>();
        db_new.open();
        Cursor Csubs=db_new.getValues("class");
        if(Csubs.getCount()!=0)
        {
            if(Csubs.moveToFirst()) {
                while(Csubs.moveToNext()){//!Csubs.isAfterLast()){
                    subClass.add(Csubs.getString(0));
                }

            }
        }
        else{
            Toast.makeText(getApplicationContext(), "oops! No Classes are available!! Tap '+' to add.", Toast.LENGTH_LONG).show();
        }
        db_new.close();

        String[] from = new String[] { "col_1"};
        int[] to = new int[] { R.id.col1tv };
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < subClass.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("col_1", subClass.get(i));

            fillMaps.add(map);
        }
        final SimpleAdapter simpleAdapter= new SimpleAdapter(this,fillMaps,
                R.layout.custom_lv1, from, to);
        listCls.setAdapter(simpleAdapter);
        listCls.setLongClickable(true);

        listCls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final int pos = i;
                new AlertDialog.Builder(AddClass.this)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this class?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                db_new.open();
                                String val = ((HashMap<String, String>) (adapterView.getAdapter().getItem(pos))).get("col_1");
                                Toast.makeText(getApplicationContext(), val + " is removed", Toast.LENGTH_LONG).show();
                                db_new.deleteRow("class", val);
                                finish();
                                startActivity(getIntent());
                                db_new.close();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
                return false;
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        db_new=new DB_New(getApplicationContext());
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addCls) {
            final EditText txtRev = new EditText(getApplicationContext());
            txtRev.setInputType(InputType.TYPE_CLASS_TEXT);
            new AlertDialog.Builder(AddClass.this)
                    .setTitle("New Class")
                    .setMessage("New Class name please..")
                    .setView(txtRev)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(txtRev.getText().toString().length()>0)
                            {
                                db_new.open();
                                db_new.insertmaster("class",txtRev.getText().toString());
                                finish();startActivity(getIntent());
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Please enter class name (S5 CSE)", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
