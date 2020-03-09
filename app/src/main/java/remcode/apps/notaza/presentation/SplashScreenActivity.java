package remcode.apps.notaza.presentation;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import remcode.apps.notaza.R;
import remcode.apps.notaza.presentation.category.CategoryActivity;

public class SplashScreenActivity extends AppCompatActivity
    {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent mainActivity = new Intent(
                            SplashScreenActivity.this, CategoryActivity.class);

                    startActivity(mainActivity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}