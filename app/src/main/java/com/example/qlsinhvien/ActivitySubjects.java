package com.example.qlsinhvien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qlsinhvien.adapter.adapterSubjects;
import com.example.qlsinhvien.database.database;
import com.example.qlsinhvien.model.Subject;

import java.util.ArrayList;

public class ActivitySubjects extends AppCompatActivity {

    Toolbar toolbarSubject;
    ListView listViewSubject;
    ArrayList<Subject> ArrayListSubject;
    database database;
    adapterSubjects adapterSubjects;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        toolbarSubject = findViewById(R.id.toolbarSubject);
        listViewSubject = findViewById(R.id.listviewSubject);

        setSupportActionBar(toolbarSubject);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = new database(this);

        ArrayListSubject = new ArrayList<>();

        Cursor cursor = database.getDataSubjects();
        while (cursor.moveToNext()){

            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            int credits = cursor.getInt(2);
            String time = cursor.getString(3);
            String place = cursor.getString(4);

            ArrayListSubject.add(new Subject(id,title,credits,time,place));
        }
        adapterSubjects = new adapterSubjects(ActivitySubjects.this,ArrayListSubject);
        listViewSubject.setAdapter(adapterSubjects);
        cursor.moveToFirst();
        cursor.close();

        //Chuy???n qua danh s??ch sinh vi??n
            listViewSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ActivitySubjects.this,ActivityStudent.class);
                    int id_subject = ArrayListSubject.get(position).getId_subject();
                    //G???i d??? li???u id qua activity student ????? xem sinh vi??n h???c m??n h???c n??y
                    intent.putExtra("id_subject",id_subject);
                    startActivity(intent);
                }
            });

    }
    //N???p m???t menu add v??o actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuadd,menu);
        return true;
    }
    //B???t s??? ki???n khi click v??o Add
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuAdd:
                //Chuy???n t???i m??n h??nh th??m m??n h???c
                Intent intent = new Intent(ActivitySubjects.this,ActivityAddSubjects.class);
                startActivity(intent);
                break;
            default:
                Intent intent2 = new Intent(ActivitySubjects.this,MainActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //Ph????ng th???c x??a subject
    public void delete(final int position){

        DialogDeleteSubject(position);
    }
    public void update(final int position){

        Cursor cursor = database.getDataSubjects();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            if(id == position){
                Intent intent = new Intent(ActivitySubjects.this,ActivityUpdateSubject.class);
                intent.putExtra("id",position);

                String title = cursor.getString(1);
                int credits = cursor.getInt(2);
                String time = cursor.getString(3);
                String place = cursor.getString(4);

                intent.putExtra("title",title);
                intent.putExtra("credit",credits);
                intent.putExtra("time",time);
                intent.putExtra("place",place);

                startActivity(intent);
            }
        }


    }
    public void information(final int pos){


        Cursor cursor = database.getDataSubjects();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            if (id == pos) {
                Intent intent = new Intent(ActivitySubjects.this, ActivityInformationSubject.class);
                intent.putExtra("id", pos);

                String title = cursor.getString(1);
                int credits = cursor.getInt(2);
                String time = cursor.getString(3);
                String place = cursor.getString(4);

                intent.putExtra("title", title);
                intent.putExtra("credit", credits);
                intent.putExtra("time", time);
                intent.putExtra("place", place);

                startActivity(intent);
            }
        }

    }

    //Dialog Update
    private void DialogDeleteSubject(int position) {

        //T???o ?????i t?????ng c???a s??? dialog
        Dialog dialog  =  new Dialog(this);

        //N???p layout v??o
        dialog.setContentView(R.layout.dialogdeletesubject);

        //Click No m???i tho??t, click ngo??i ko tho??t
        dialog.setCanceledOnTouchOutside(false);

        //??nh x???
        Button btnYes = dialog.findViewById(R.id.buttonYesDeleteSubject);
        Button btnNo = dialog.findViewById(R.id.buttonNoDeleteSubject);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = new database(ActivitySubjects.this);
                //X??a trong SQL
                database.DeleteSubject(position);
                database.DeleteSubjectStudent(position);
                //C???p nh???t l???i listview
                Intent intent = new Intent(ActivitySubjects.this,ActivitySubjects.class);
                startActivity(intent);

            }
        });
        //N???u no th?? ????ng dialog
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        //show dialog l??n activity
        dialog.show();
    }


    //Nh???n n??t back ??? activity n??y th?? chuy???n qua activity ???????c thi???t l???p ri??ng
    @Override
    public void onBackPressed() {
        counter++;
        if (counter ==1){
            Intent intent = new Intent(ActivitySubjects.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }

}