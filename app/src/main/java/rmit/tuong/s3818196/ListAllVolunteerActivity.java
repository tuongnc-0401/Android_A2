package rmit.tuong.s3818196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListAllVolunteerActivity extends AppCompatActivity {

    TextView txtListAllVolunteerTitle, txtNumOfListVolunteer;
    Button btnDownload;
    DatabaseHelper databaseHelper;
    ListView listView;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_volunteer);
        initial();
        databaseHelper = new DatabaseHelper(ListAllVolunteerActivity.this);
        Intent intent = getIntent();
        int siteID = intent.getIntExtra("siteID", 0);
        txtListAllVolunteerTitle.setText("List volunteer of site "+ siteID);
        int numOfVolunteer = databaseHelper.getNumOfVolunteer(siteID+"");
        txtNumOfListVolunteer.setText(" Number of volunteer: "+ numOfVolunteer);

    //    List<UserModel> userList = databaseHelper.getAllVolunteerOfOneSite(siteID+"");
        List<String> list = convertVolunteer(siteID);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        String text = "List volunteer of site "+siteID+"\n" + "There are " + numOfVolunteer + " volunteers\n";
        for (String s: list) {
            text+= " "+s+"\n";
        }
        String finalText = text;

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canWriteOnExternalStorage()){
                    write(finalText, siteID);
                }
            }
        });

    }


    private void write(String text, int siteID){
        File sdcard = Environment.getExternalStorageDirectory();
        // to this path add a new directory path
        File dir = new File(sdcard.getAbsolutePath() + "/Download/");
        // create this directory if not already created
        dir.mkdir();
        // create the file in which we will write the contents
        File file = new File(dir, "ListOfVolunteer"+siteID+".txt");
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(text.getBytes());
            Toast.makeText(this, "Download successfully!", Toast.LENGTH_SHORT).show();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static boolean canWriteOnExternalStorage() {
        // get the state of your external storage
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // if storage is mounted return true
            return true;
        }
        return false;
    }



    private void initial() {
        txtListAllVolunteerTitle = findViewById(R.id.txtListAllVolunteerTitle);
        txtNumOfListVolunteer = findViewById(R.id.txtNumOfListVolunteer);
        btnDownload = findViewById(R.id.btnDownload);
        listView = findViewById(R.id.lvVolunteer);
    }

    private List<String> convertVolunteer(int siteID){
        List<UserModel> userList = databaseHelper.getAllVolunteerOfOneSite(siteID+"");
        List<String> list = new ArrayList<>();
        for (UserModel user: userList) {
            list.add(user.getUsername());
        }
        return list;
    }
}