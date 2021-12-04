package rmit.tuong.s3818196;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtUsername,edtPass, edtRePass;
    private Button btnSignUp, btnLogin;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    public static final String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        databaseHelper = new DatabaseHelper(SignUpActivity.this);
        sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
        initial();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = edtUsername.getText().toString();
                String pass = edtPass.getText().toString();
                String repass = edtRePass.getText().toString();

                if(user.equals("")||pass.equals("")||repass.equals(""))
                    Toast.makeText(SignUpActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkUser = databaseHelper.checkUsername(user);
                        Log.d(TAG, "onClick: pass equal pass");
                        if(checkUser==false){
                            Boolean insert = databaseHelper.createUser(user, pass, "leader");
                            if(insert==true){
                                UserModel userModel = databaseHelper.getUser(user,pass);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("userID", userModel.getId());
                                editor.putString("username", userModel.getUsername());
                                editor.commit();
                                Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
                
                
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });
    }

    private void initial() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPass =findViewById(R.id.edtPassword);
        edtRePass = findViewById(R.id.edtRePassword);
        btnLogin = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);
    }
}