package ua.uz.avtolux;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toolbar;

@SuppressLint("InlinedApi")
@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity {

	public String loginname;
	public String hashpass;
	public String resultlogin;
	public String user_id;

	public boolean islogged = false;
	Button button;
	TableLayout llayer;
	SharedPreferences sPref;
	
	private static final int LOGIN_RESULT_CODE = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = new Button(this);
		button.setText("Login");
		button.setId(1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {

				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivityForResult(intent,LOGIN_RESULT_CODE);
			}
		});
		llayer = new TableLayout(this);

		llayer.addView(button);
		addContentView(llayer, new TableLayout.LayoutParams(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));
		try_login();
	}
	
	public void try_login(){
		loadText();
		islogged = false;
		SiteApi.logged(loginname,hashpass,this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOGIN_RESULT_CODE) {
			try_login();
		}
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

	public void loadText() {
		sPref = getSharedPreferences(SiteApi.USERDATA, MODE_PRIVATE);
		loginname = sPref.getString(SiteApi.SAVED_USER, "");
		hashpass = sPref.getString(SiteApi.SAVED_HASH, "");
		resultlogin = sPref.getString(SiteApi.SAVED_RESULT, "");
		user_id = sPref.getString(SiteApi.SAVED_USERID, "");
		if (resultlogin.equals("ok")){
			islogged = true;
			button.setText("Change User");
		}else{
			islogged = false;
			button.setText("login");			
		}		    
	}

}
