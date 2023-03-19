package hcmute.edu.vn.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText num1;
    private EditText num2;
    private Button add;
    private EditText re;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        num1=(EditText) findViewById(R.id.number1);
        num2=(EditText) findViewById(R.id.number2);
        add=(Button)findViewById(R.id.sum);
        re=(EditText) findViewById(R.id.result);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number1 = Integer.parseInt(num1.getText().toString());
                int number2 = Integer.parseInt(num2.getText().toString());
                int sum = number1 + number2;
                re.setText(String.valueOf(sum));
            };
        });
    }
    public void sendMessage(View view)
    {
        Log.i("Event Handling", "Welcome to my handling exercises");
    }
}