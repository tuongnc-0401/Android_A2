package rmit.tuong.s3818196;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JoinSiteActivity extends AppCompatActivity {

    EditText edtFriendName;
    TextView txtSiteIDFriend;
    Button btnRegister;
    DatabaseHelper databaseHelper;
    private int siteID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_site);
        edtFriendName = findViewById(R.id.edtFriendName);
        txtSiteIDFriend = findViewById(R.id.txtSiteIDJoin);
        btnRegister = findViewById(R.id.btnRegisterFriendName);
        databaseHelper = new DatabaseHelper(JoinSiteActivity.this);
        Intent intent = getIntent();
        siteID = intent.getIntExtra("siteID", 0);

        txtSiteIDFriend.setText(siteID+"");


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtFriendName.getText().toString().equals("")){
                    Toast.makeText(JoinSiteActivity.this, "Can not input empty name!", Toast.LENGTH_SHORT).show();
                } else {


                    alertDialogShow("Notification","Do you want to register this friend into this site?");

                }
            }
        });

    }

    private void alertDialogShow(String title,String messages){
        AlertDialog dlg = new AlertDialog.Builder(JoinSiteActivity.this)
                .setTitle(title)
                .setMessage(messages)
                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Boolean result =   databaseHelper.createMembership(-1,edtFriendName.getText().toString(),siteID);
                        Toast.makeText(JoinSiteActivity.this, "Add this friend into site successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(JoinSiteActivity.this,MapsActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                    }
                })
                .create();
        dlg.show();
    }
}