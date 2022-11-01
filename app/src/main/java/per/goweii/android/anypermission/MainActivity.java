package per.goweii.android.anypermission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_activity).setOnClickListener(this);
        findViewById(R.id.btn_fragment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_activity:
                startActivity(new Intent(MainActivity.this, TestActivity.class));
                break;
            case R.id.btn_fragment:
                startActivity(new Intent(MainActivity.this, TestFragmentActivity.class));
                break;
        }
    }
}
