package rmit.tuong.s3818196;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewSite extends AppCompatActivity {
    EditText edtLat, edtLong, edtName;
    Button btnAddSite;
    SiteModel site;
    double latitude, longitude;
    DatabaseHelper dataBaseHelper;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_site);
        initial();
        dataBaseHelper = new DatabaseHelper(AddNewSite.this);
        Intent intent = getIntent();
         latitude = intent.getDoubleExtra("latitude", 0);
         longitude = intent.getDoubleExtra("longitude", 0);
        sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
        int userID = sharedPreferences.getInt("userID", -1);
        String username = sharedPreferences.getString("username", "");

        edtLong.setText(longitude+"");
        edtLat.setText(latitude+"");

        btnAddSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    site = new SiteModel(-1,edtName.getText().toString(),longitude,latitude,userID,username, 0,"",0,0,0);

                }catch (Exception e){

                    Toast.makeText(AddNewSite.this, "Something error", Toast.LENGTH_SHORT).show();
                    site = new SiteModel(-1,"error",1,1,1,"1",1);
                }

                boolean success = dataBaseHelper.addOneSite(site);
                Toast.makeText(AddNewSite.this, "Add a new site successfully!" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddNewSite.this,
                        MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void initial() {
        edtLat = findViewById(R.id.edtLat);
        edtLong= findViewById(R.id.edtLong);
        edtName= findViewById(R.id.edtName);
        btnAddSite = findViewById(R.id.btnAddSite);
    }
}