package rmit.tuong.s3818196;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SiteDetailActivity extends AppCompatActivity {
    TextView txtSiteName, txtWelcome, txtSiteLeader, txtNumOfVolunteer, txtNumOfPeople, txtNoti;
    private DatabaseHelper databaseHelper;
    private int userID, siteID;
    Button btnJoinSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_detail);
        initial();

        databaseHelper = new DatabaseHelper(SiteDetailActivity.this);
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", 0);
        siteID = intent.getIntExtra("siteID", 0);

        UserModel user = databaseHelper.getUserByID(userID+"");
        SiteModel site = databaseHelper.getSiteByID(siteID);
        txtWelcome.setText("Welcome "+user.getUsername()+ " to "+site.getName());
        txtSiteName.setText(""+site.getName());
        txtSiteLeader.setText(site.getLeaderName());
        txtNumOfVolunteer.setText(site.getNumOfVolunteer()+"");
        txtNumOfPeople.setText(site.getNumOfVolunteer()+"");
        int leaderID = site.getLeaderID();
        if (leaderID == userID){
            btnJoinSite.setVisibility(View.INVISIBLE);
            txtNoti.setText("Note: You are the leader of this site");
        }


    }

    private void initial() {
        txtSiteName = findViewById(R.id.txtSiteName);
        txtWelcome = findViewById(R.id.txtWelcomeSite);
        txtSiteLeader =findViewById(R.id.txtSiteLeader);
        txtNumOfVolunteer =findViewById(R.id.txtNumOfVolunteer);
        txtNumOfPeople =findViewById(R.id.txtNumOfPeople);
        txtNoti = findViewById(R.id.txtNoti);
        btnJoinSite = findViewById(R.id.btnJoinSite);
    }
}