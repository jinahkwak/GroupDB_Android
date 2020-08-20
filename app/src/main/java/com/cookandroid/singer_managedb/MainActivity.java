package com.cookandroid.singer_managedb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    myDBHelper myHelper;
    Button btnInit,btnInsert, btnSelect, btnDelete, btnRevise;
    EditText edtName, edtNumber, edtNameResult, edtNumberResult, edtSerName;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("가수 그룹 관리 DB");

        btnInit = (Button)findViewById(R.id.btnInit);
        btnInsert = (Button)findViewById(R.id.btnInsert);
        btnSelect = (Button)findViewById(R.id.btnSelect);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnRevise = (Button)findViewById(R.id.btnRevise);
        edtName = (EditText)findViewById(R.id.edtName);
        edtNumber = (EditText)findViewById(R.id.edtNumber);
        edtSerName = (EditText)findViewById(R.id.edtSerName);
        edtNameResult = (EditText)findViewById(R.id.edtNameResult);
        edtNumberResult = (EditText)findViewById(R.id.edtNumberResult);

        myHelper = new myDBHelper(this);
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDB, 1, 2);
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "초기화됨",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(edtNumber.getWindowToken(), 0);

                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO groupTBL VALUES ('"
                            + edtName.getText().toString().trim() + "', "
                            + edtNumber.getText().toString().trim() + ");");
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();

            }
        });

        Thread thread = new Thread(){
            public void run(){
                //이 부분에 작업을 코딩
                System.out.println("thread run");
            }
        };
        thread.start();

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);

                String strNames = "그룹 이름" + "\r\n" + "------" + "\r\n";
                String strNumbers = "인원" + "\r\n" + "------" + "\r\n";

                while (cursor.moveToNext()) {
                    strNames += cursor.getString(0) + "\r\n";
                    strNumbers += cursor.getString(1) + "\r\n";
                }

                edtNameResult.setText(strNames);
                edtNumberResult.setText(strNumbers);

                cursor.close();
                sqlDB.close();

                Toast.makeText(getApplicationContext(),"조회됨",Toast.LENGTH_SHORT).show();
            }
        });

        btnRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(edtNumber.getWindowToken(), 0);

                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE groupTBL set gNumber = "
                            + edtNumber.getText().toString().trim()
                            + " where gName = '"
                            + edtName.getText().toString().trim() + "';");
                sqlDB.close();
                    // 처리결과 확인
                Toast.makeText(getApplicationContext(),
                            "그룹이름 : '" + edtName.getText().toString().trim()
                                    + "' 수정됨", Toast.LENGTH_SHORT).show();
                btnSelect.callOnClick();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(edtNumber.getWindowToken(), 0);

                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("DELETE from groupTBL where gName = '"
                        + edtName.getText().toString().trim() + "';");
                sqlDB.close();
                // 처리결과 확인
                Toast.makeText(
                        getApplicationContext(),
                        "그룹이름 : '" + edtName.getText().toString().trim()
                                + "' 삭제됨", Toast.LENGTH_SHORT).show();

                btnSelect.callOnClick();
            }
        });
    }

    public class myDBHelper extends SQLiteOpenHelper{
        public myDBHelper(Context context){
            super(context, "groupDB", null, 1);
        }

        public void onCreate(SQLiteDatabase db){
            db.execSQL("CREATE TABLE groupTBL ( gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }


    }



}
