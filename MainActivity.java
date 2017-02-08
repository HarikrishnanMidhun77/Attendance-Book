package com.nssce_cse.harikrishnanp.attendancebook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nssce_cse.harikrishnanp.attendancebook.database.DB_New;
import com.nssce_cse.harikrishnanp.attendancebook.database.DB_att;
import com.thomashaertel.widget.MultiSpinner;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
   // String[] Arr_cls;
    MultiSpinner spnrHour;
   private DatePicker datePicker;
    private Calendar calendar;
    EditText etDate;
    boolean notEntered=false;
    private int year, month, day;
    String strDept="null",strClass="null",strSub="null",strDate="null",strHour="null";
    EditText etDept;
    Spinner spnrCls;
    Spinner spnrSub;
     EditText etAbs;
    ImageButton btnAddCls,btnAddSub;
    DB_att db_att;
    DB_New db_new;
    ArrayList<String> selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.mainTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //Arr_cls=new String[10];
        db_att=new DB_att(getApplicationContext());
        db_new = new DB_New(getApplicationContext());
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        selected = new ArrayList<>(8);
        final String arHr[]={"1st HOUR","2nd HOUR","3rd HOUR","4th HOUR","5th HOUR","6th HOUR","7th HOUR","8th HOUR"};
        ArrayList<String> clsList=new ArrayList<String>();
        ArrayList<String> subList=new ArrayList<String>();
        try{
            db_new.open();
            Cursor curCls= db_new.getValues("class");
            if(curCls.getCount()!=0)
            {
                notEntered=false;
                if(curCls.moveToFirst()) {
                    while(curCls.moveToNext()){//!Csubs.isAfterLast()){
                        clsList.add(curCls.getString(0));
                    }

                }
            }
            else{
                Toast.makeText(getApplicationContext(), "No classes are available!! Tap '+' to add.", Toast.LENGTH_LONG).show();
                notEntered=true;
                clsList.add(0,"Select/add classes");
            }
            db_new.close();
        }catch(NullPointerException e)
        {
            clsList.add(0,"Select/add classes");
        }


        try{
            db_new.open();
            Cursor curSub= db_new.getValues("subject");
            if(curSub.getCount()!=0)
            {
                notEntered=false;
                if(curSub.moveToFirst()) {
                    while(curSub.moveToNext()){//!Csubs.isAfterLast()){
                        subList.add(curSub.getString(0));
                    }

                }
            }
            else{
                Toast.makeText(getApplicationContext(), "No subjects are available!! Tap '+' to add.", Toast.LENGTH_LONG).show();
                notEntered=true;
                subList.add(0,"Select/add subjects");
            }
            db_new.close();
        }catch(NullPointerException e)
        {
            subList.add(0,"Select/add subjects");
        }



        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkRed));
        }

         etDept=(EditText)findViewById(R.id.etDept);
        etDate=(EditText)findViewById(R.id.etDate);
        spnrCls=(Spinner)findViewById(R.id.spnrCls);
        spnrSub=(Spinner)findViewById(R.id.spnrSub);
        etAbs=(EditText)findViewById(R.id.etAbs);
        etAbs.setTextIsSelectable(false);
         btnAddCls=(ImageButton)findViewById(R.id.btnAddCls);
         btnAddSub=(ImageButton)findViewById(R.id.btnAddSub);
        spnrHour=(MultiSpinner) findViewById(R.id.spnrHr);
        showDate(year, month + 1, day);

        try{
            db_new.open();
            Cursor curDept= db_new.getValues("dept");
            if(curDept.getCount()!=0)
            {
                notEntered=false;
                if(curDept.moveToFirst()) {
                    etDept.setText(curDept.getString(0));
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "No Departments are available!! Tap '+' to add.", Toast.LENGTH_LONG).show();
                notEntered=true;
                subList.add(0,"Select/add Departments");
            }
            db_new.close();
        }catch(NullPointerException e)
        {
            subList.add(0,"Select/add Departments");
        }

        etDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddDept.class);
                startActivity(intent);
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });
        ArrayAdapter<CharSequence> adapterHr=ArrayAdapter.createFromResource(
                this,R.array.hour_array, R.layout.spinner_layout);
       ArrayAdapter<String> adapterCls =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,clsList);
        adapterCls.setDropDownViewResource(R.layout.spinner_layout);
        spnrCls.setAdapter(adapterCls);
        ArrayAdapter<String> adapterSub =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,subList);
        adapterSub.setDropDownViewResource(R.layout.spinner_layout);
        spnrSub.setAdapter(adapterSub);
        spnrHour.setAdapter(adapterHr, false, new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] booleans) {
                for(int i=0;i<booleans.length;i++)
                {
                    if(booleans[i])
                        selected.add(arHr[i]);
                    //put it to data base
                    Toast.makeText(getApplicationContext(),selected.toString(), Toast.LENGTH_SHORT).show();
                }
                selected.clear();
            }


        });
        spnrHour.setText("Select hour/period");
        spnrCls.setTop(0);
        spnrSub.setSelection(0);
try {
    btnAddSub.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, AddSub.class);
            startActivity(intent);
        }
    });
    btnAddCls.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, AddClass.class);
            startActivity(intent);
        }
    });
    etAbs.setOnKeyListener(new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            int p;
            if (etAbs.getText().length() > 0) {
            // if (i == KeyEvent.ACTION_DOWN || i== EditorInfo.IME_ACTION_DONE || i==EditorInfo.IME_ACTION_GO || i==EditorInfo.IME_ACTION_NEXT && i==KeyEvent.KEYCODE_ENTER ) {
            if(i==KeyEvent.KEYCODE_ENTER){
                  /*  String temp = etAbs.getText().toString();
                    temp = temp + ",";
                    etAbs.setText(temp);*/
                 p=etAbs.getSelectionStart();
                 etAbs.getText().insert(p,",");

                 //etAbs.getText().append(',');
                   // etAbs.setSelected(false);

                  //etAbs.setSelection(etAbs.getText().length());
                }




            }

            return false;
        }
    });
}catch(NullPointerException e)
{

}
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
      /*  Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();*/
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
       etDate.setText(new StringBuilder().append(day).append("/")
               .append(month).append("/").append(year));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_submit)
        {
            getAllvalues();
        }
        if(id==R.id.action_export)
        {
          /*  db_att.open();
            db_att.export();
            db_att.close();*/
        }
        if(id==R.id.action_classes)
        {
            Intent intent = new Intent(MainActivity.this, AddClass.class);
            startActivity(intent);
        }
        if(id==R.id.action_subjects)
        {
            Intent intent = new Intent(MainActivity.this, AddSub.class);
            startActivity(intent);
        }
        if(id==R.id.action_Departments)
        {
            Intent intent = new Intent(MainActivity.this, AddDept.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public void getAllvalues()
    {
        if (clarify()) {
            strDept = etDept.getText().toString();
            strClass = spnrCls.getSelectedItem().toString();
            strSub = spnrSub.getSelectedItem().toString();
            strDate = etDate.getText().toString();
            strHour = "1st Hour";
            // strHour=
            db_att.open();
            db_att.insertmaster(strDept, strClass, strSub, strDate, strHour);
            db_att.close();
        }

    }
    public boolean clarify()
    {
        if(etAbs.getText().length()==0)
            etAbs.setText("0");
        if(spnrHour.getText().toString().equals("Select hour/period"))
            notEntered=true;
        return (!notEntered);
    }

}
