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


public class AddDept extends ActionBarActivity {
    DB_New db_new;
    ListView listDept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dept);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkRed));
        }

        db_new=new DB_New(getApplicationContext());
        listDept=(ListView)findViewById(R.id.listDept);
        ArrayList<String> subDept=new ArrayList<String>();
        db_new.open();
        Cursor Csubs=db_new.getValues("dept");
        if(Csubs.getCount()!=0)
        {
            if(Csubs.moveToFirst()) {
                while(Csubs.moveToNext()){//!Csubs.isAfterLast()){
                    subDept.add(Csubs.getString(0));
                }

            }
        }
        else{
            Toast.makeText(getApplicationContext(), "oops! No departments are available!! Tap '+' to add.", Toast.LENGTH_LONG).show();
        }
        db_new.close();

        String[] from = new String[] { "col_1"};
        int[] to = new int[] { R.id.col1tv };
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < subDept.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("col_1", subDept.get(i));

            fillMaps.add(map);
        }
        final SimpleAdapter simpleAdapter= new SimpleAdapter(this,fillMaps,
                R.layout.custom_lv1, from, to);
        listDept.setAdapter(simpleAdapter);
        listDept.setLongClickable(true);


        listDept.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final int pos=i;
                new AlertDialog.Builder(AddDept.this)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this department?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                db_new.open();
                                String val=((HashMap<String,String>)(adapterView.getAdapter().getItem(pos))).get("col_1");
                                Toast.makeText(getApplicationContext(),val+" is removed", Toast.LENGTH_LONG).show();
                                db_new.deleteRow("dept", val);
                                finish();startActivity(getIntent());
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
        getMenuInflater().inflate(R.menu.menu_add_dept, menu);
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
        if (id == R.id.action_addDept) {
            final EditText txtRev = new EditText(getApplicationContext());
            txtRev.setInputType(InputType.TYPE_CLASS_TEXT);
            new AlertDialog.Builder(AddDept.this)
                    .setTitle("New Department")
                    .setMessage("New Department name please..")
                    .setView(txtRev)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(txtRev.getText().toString().length()>0)
                            {
                                db_new.open();
                                db_new.insertmaster("dept",txtRev.getText().toString());
                                finish();startActivity(getIntent());
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Please enter department name (eg: Department of Computer science and Engineering)", Toast.LENGTH_LONG).show();
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
