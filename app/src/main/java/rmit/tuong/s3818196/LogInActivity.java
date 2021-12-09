package rmit.tuong.s3818196;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {

    private EditText edtUsername1, edtPassword1;
    private Button btnLogin1, btnSignUp1;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        databaseHelper = new DatabaseHelper(LogInActivity.this);
        initial();


        btnSignUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUsername1.getText().toString();
                String pass = edtPassword1.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(LogInActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = databaseHelper.checkUsernamePassword(user, pass);
                    if(checkuserpass==true){
                        UserModel userModel = databaseHelper.getUser(user,pass);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("userID", userModel.getId());
                        editor.putString("username", userModel.getUsername());
                        editor.putString("role", userModel.getRole());
                        editor.commit();
                        Toast.makeText(LogInActivity.this, "LogIn successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LogInActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }

    private void initial() {
        edtUsername1 = findViewById(R.id.edtUsername1);
        edtPassword1 = findViewById(R.id.edtPassword1);
        btnLogin1 = findViewById(R.id.btnLogIn1);
        btnSignUp1 = findViewById(R.id.btnSignUp1);
        sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
    }
}