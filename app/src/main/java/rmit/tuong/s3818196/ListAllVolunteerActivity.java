package rmit.tuong.s3818196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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