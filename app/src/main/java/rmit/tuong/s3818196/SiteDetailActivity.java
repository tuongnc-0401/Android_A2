package rmit.tuong.s3818196;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SiteDetailActivity extends AppCompatActivity {
    TextView txtSiteName, txtWelcome, txtSiteLeader, txtNumOfVolunteer, txtNumOfPeople, txtNoti, txtNumOfPositive, txtNumOfNegative;
    private DatabaseHelper databaseHelper;
    private int userID, siteID;
    Button btnJoinSite, btnQuitSite, btnRegister, btnViewVolunteer, btnUpdateStatistic;
    ConstraintLayout layoutSatistic, layoutAdmin;

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
        // count of volunteer
        int numOfVolunteer = databaseHelper.getNumOfVolunteer(siteID+"");
        txtNumOfVolunteer.setText(numOfVolunteer+"");
        txtNumOfPeople.setText(site.getNumOfPeopleTested()+"");
        txtNumOfPositive.setText(site.getNumOfPositive()+"");
        txtNumOfNegative.setText(site.getNumOfNegative()+"");
        int leaderID = site.getLeaderID();


        boolean isMember = databaseHelper.checkMembershipByUserID(userID+"", siteID+"");
        if (leaderID == userID){
            btnJoinSite.setVisibility(View.INVISIBLE);
            txtNoti.setVisibility(View.VISIBLE);
            txtNoti.setText("You are the leader of this site");
            layoutSatistic.setVisibility(View.VISIBLE);
            layoutAdmin.setVisibility(View.VISIBLE);
        } else if(user.getUsername().equals("admin")) {
            btnJoinSite.setVisibility(View.INVISIBLE);
            btnQuitSite.setVisibility(View.INVISIBLE);
            txtNoti.setVisibility(View.INVISIBLE);
            layoutSatistic.setVisibility(View.VISIBLE);
            layoutAdmin.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.INVISIBLE);
        }else if(isMember){
            txtNoti.setVisibility(View.VISIBLE);
            txtNoti.setText("You are the volunteer of this site");
            layoutSatistic.setVisibility(View.VISIBLE);
            btnJoinSite.setVisibility(View.INVISIBLE);
            btnQuitSite.setVisibility(View.VISIBLE);
        } else {
            txtNoti.setVisibility(View.INVISIBLE);
            layoutSatistic.setVisibility(View.INVISIBLE);
            btnJoinSite.setVisibility(View.VISIBLE);
            btnQuitSite.setVisibility(View.INVISIBLE);
        }

        // BTN JOIN
        btnJoinSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean checkUser = databaseHelper.checkMembershipByUserID(user.getId()+"",site.getId()+"");
                if(checkUser){
                    Toast.makeText(SiteDetailActivity.this, "You have joined this site before", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog dlg = new AlertDialog.Builder(SiteDetailActivity.this)
                            .setTitle("Notification")
                            .setMessage("Are you sure to join this site?")
                            .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Boolean result =   databaseHelper.createMembership(user.getId(),user.getUsername(),site.getId());
                                    if(result){
                                        Toast.makeText(SiteDetailActivity.this, "You join this site successfully!", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(getIntent());

                                    }
                                }
                            })
                            .create();
                    dlg.show();




                }

            }
        });


        // BTN REGISTER FOR FRIEND
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SiteDetailActivity.this, JoinSiteActivity.class);
                i.putExtra("siteID", siteID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        // BTN QUIT
        btnQuitSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog dlg = new AlertDialog.Builder(SiteDetailActivity.this)
                        .setTitle("Notification")
                        .setMessage("Are you sure to quit this site?")
                        .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Boolean check = databaseHelper.deleteOneMembership(userID+"", siteID+"");
                                    Toast.makeText(SiteDetailActivity.this, "You quit this site successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(getIntent());


                            }
                        })
                        .create();
                dlg.show();







            }
        });

        // BTN VIEW VOLUNTEER
        btnViewVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SiteDetailActivity.this, ListAllVolunteerActivity.class);
                i.putExtra("siteID", siteID);
                startActivity(i);
            }
        });

    }

    private void initial() {
        txtSiteName = findViewById(R.id.txtSiteName);
        txtWelcome = findViewById(R.id.txtWelcomeSite);
        txtSiteLeader =findViewById(R.id.txtSiteLeader);
        txtNumOfVolunteer =findViewById(R.id.txtNumOfVolunteer);
        txtNumOfPeople =findViewById(R.id.txtNumOfPeople);
        txtNoti = findViewById(R.id.txtNoti);
        btnJoinSite = findViewById(R.id.btnJoinSite);
        layoutSatistic = findViewById(R.id.layoutStatistic);
        txtNumOfPositive = findViewById(R.id.txtNumOfPositive);
        txtNumOfNegative = findViewById(R.id.txtNumOfNegative);
        layoutAdmin = findViewById(R.id.layoutAdmin);
        btnQuitSite = findViewById(R.id.btnQuitSite);
        btnRegister = findViewById(R.id.btnRegisterFriend);
        btnViewVolunteer = findViewById(R.id.btnViewVolunteer);
        btnUpdateStatistic = findViewById(R.id.btnUpdateStatistic);
    }
}