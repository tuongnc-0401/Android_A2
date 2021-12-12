package rmit.tuong.s3818196;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    Button btnLogout;
    SharedPreferences sharedPreferences;
    TextView txtName, txtMemberProfile, txtLeaderProfile;
    DatabaseHelper databaseHelper;
    String username;
    ListView lvMember, lvMemberAdmin, lvLeader;


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initial();
        getData();




        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "logout", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("userID");
                editor.commit();

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    private void initial() {
        btnLogout = findViewById(R.id.btnLogout);
        sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
        txtName = findViewById(R.id.txtUserProfile);
        lvLeader =findViewById(R.id.lvLeader);
        lvMember= findViewById(R.id.lvMember);
        txtLeaderProfile = findViewById(R.id.txtLeaderProfile);
        txtMemberProfile = findViewById(R.id.txtMemberProfile);
        lvMemberAdmin = findViewById(R.id.lvMemberAdmin);
    }

    private void getData(){

        int userID = sharedPreferences.getInt("userID", 1);
        databaseHelper = new DatabaseHelper(ProfileActivity.this);
        UserModel user = databaseHelper.getUserByID(userID+"");
        username = user.getUsername();
        txtName.setText(username);

        List<String> listSite;



        if(username.equals("admin")){
            List<SiteModel> listSiteModel = databaseHelper.getAllSite();
            int numOfSite = databaseHelper.getNumOfAllSite();
            txtMemberProfile.setText("There are "+ numOfSite+ " sites");
            txtLeaderProfile.setVisibility(View.INVISIBLE);
            lvMemberAdmin.setVisibility(View.VISIBLE);
            lvLeader.setVisibility(View.INVISIBLE);
            txtLeaderProfile.setVisibility(View.INVISIBLE);
            lvMember.setVisibility(View.INVISIBLE);

            if(numOfSite == 0){
                lvMemberAdmin.setVisibility(View.GONE);
            }

            if(listSiteModel.size() >0){
                ArrayAdapter  adapter = new ArrayAdapter<SiteModel>(this, android.R.layout.simple_list_item_1,listSiteModel);
                lvMemberAdmin.setAdapter(adapter);
                lvMemberAdmin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //   Toast.makeText(ProfileActivity.this, listSiteModel.get(i).getName(), Toast.LENGTH_SHORT).show();
                        SiteModel site = (SiteModel) adapterView.getItemAtPosition(i);

                        Intent intent = new Intent(ProfileActivity.this,SiteDetailActivity.class);
                        intent.putExtra("userID", userID);
                        intent.putExtra("siteID", site.getId());
                        startActivity(intent);
                    }
                });
            }


        } else {
            List<SiteModel> listSiteModel = new ArrayList<>();;
            int numOfSite = databaseHelper.getNumOfSiteByUserID(userID+"");
            txtMemberProfile.setText("You are volunteer of "+ numOfSite+ " sites");
            listSite = databaseHelper.getAllSiteOfVolunteer(userID+"");
            for (String s:listSite) {
                //listSiteModel =
                SiteModel tmpSite = databaseHelper.getSiteByID(Integer.parseInt(s));
                listSiteModel.add(tmpSite);
            }

            if(numOfSite == 0){
                lvMember.setVisibility(View.GONE);
            }

            if(listSiteModel.size() >0){
                ArrayAdapter  adapter = new ArrayAdapter<SiteModel>(this, android.R.layout.simple_list_item_1,listSiteModel);
                lvMember.setAdapter(adapter);
                lvMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //    Toast.makeText(ProfileActivity.this, listSiteModel.get(i).getName(), Toast.LENGTH_SHORT).show();
                        SiteModel site = (SiteModel) adapterView.getItemAtPosition(i);

                        Intent intent = new Intent(ProfileActivity.this,SiteDetailActivity.class);
                        intent.putExtra("userID", userID);
                        intent.putExtra("siteID", site.getId());
                        startActivity(intent);
                    }
                });
            }
            /**
             * Leader Site
             */

            int numOfLeader = databaseHelper.getNumOfSiteByLeader(userID+"");
            txtLeaderProfile.setText("You are leader of "+ numOfLeader+" sites");
            List<SiteModel> listSiteLeader = databaseHelper.getAllSiteOfLeader(userID+"");
            if(listSiteLeader.size() >0){
                ArrayAdapter  adapter = new ArrayAdapter<SiteModel>(this, android.R.layout.simple_list_item_1,listSiteLeader);
                lvLeader.setAdapter(adapter);
                lvLeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //    Toast.makeText(ProfileActivity.this, listSiteModel.get(i).getName(), Toast.LENGTH_SHORT).show();
                        SiteModel site = (SiteModel) adapterView.getItemAtPosition(i);
                        Intent intent = new Intent(ProfileActivity.this,SiteDetailActivity.class);
                        intent.putExtra("userID", userID);
                        intent.putExtra("siteID",site.getId());
                        startActivity(intent);
                    }
                });
            }
        }

    }
}