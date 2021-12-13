package rmit.tuong.s3818196;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateStatisticActivity extends AppCompatActivity {
    TextView txtTitle, txtTotalNum;
    Button btnUpdate;
    EditText edtPositive, edtNegative;
    DatabaseHelper databaseHelper;
    int userID, siteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_statistic);
        initial();



        databaseHelper = new DatabaseHelper(UpdateStatisticActivity.this);
        Intent intent = getIntent();
        siteID = intent.getIntExtra("siteID", 0);

        SiteModel site = databaseHelper.getSiteByID(siteID);

        txtTitle.setText("Update data of site "+ site.getName());
        txtTotalNum.setText(site.getNumOfPeopleTested()+"");
        edtPositive.setText(site.getNumOfPositive()+"");
        edtNegative.setText(site.getNumOfNegative()+"");



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numPos = Integer.parseInt(edtPositive.getText().toString());
                int numNe = Integer.parseInt(edtNegative.getText().toString());
                databaseHelper.updateStatistic(siteID,numPos,numNe);

                Intent i2 = new Intent(UpdateStatisticActivity.this, SiteDetailActivity.class);
                setResult(RESULT_OK,i2);
                finish();
            }
        });

    }

    private void initial() {
        txtTitle = findViewById(R.id.txtUpdateSite);
        txtTotalNum = findViewById(R.id.txtUpdateTotal);
        btnUpdate = findViewById(R.id.btnUpdateData);
        edtPositive = findViewById(R.id.edtPositive);
        edtNegative = findViewById(R.id.edtNegative);
    }
}