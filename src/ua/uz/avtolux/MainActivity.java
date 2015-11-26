package ua.uz.avtolux;

import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.Toolbar;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity {
	
	LoginActivity logActivity = new LoginActivity();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        final Button button = new Button(this);
        button.setText("Login");

        button.setId(1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
        		
                Toast.makeText(getApplicationContext(),
                        "Buttonclicked: ",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            	}
            });
        final TableLayout llayer = new TableLayout(this);
        
        llayer.addView(button);
		addContentView(llayer, new TableLayout.LayoutParams(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
