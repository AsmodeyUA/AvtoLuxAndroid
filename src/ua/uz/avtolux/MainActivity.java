package ua.uz.avtolux;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

@SuppressLint("InlinedApi")
@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity {

	public String loginname = new String("");
	public String hashpass = new String("");
	public String resultlogin = new String("notok");
	public String user_id = new String("0");

	public boolean islogged = false;

	public LinearLayout mainFormLayer;

	// Main Activity Layer for User Info
	public TableLayout llayer;
	public TextView textViewUserID;
	public TableRow tRow;
	public Button button;

	// Main Activity Layer for Search info
	public LinearLayout searchFormlayer;
	public TableLayout llayerSearchTable;
	public TableRow llayerSearchRow;
	public TableRow llayerSearchRow1;
	public TextView llayerSearchTextView;
	public EditText llayerSearchEditText;
	public Button llayerSearchButton;
	public Button llayerSearchButtonDescr;	

	// Search Result
	public JSONArray searchItem;
	// Main Activity Layer for Search Result
	public LinearLayout searchResultlayer;
	public TableLayout llayerResultTable;
	public ScrollView llayerResultTableScrollView;

	public SharedPreferences sPref;

	private static final int LOGIN_RESULT_CODE = 1;

	// to calculate which items was choused for viewing analogs
	private static final int SHIFT_ANALOG_BUTTON_ID = 50;
	
	// to calculate which analog button was choused for buying
	private static final int KOEF_ANALOG_BUTTON_ID = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button = new Button(this);
		button.setText("Login");
		button.setId(1);

		textViewUserID = new TextView(this);
		textViewUserID.setText("Not Registered User");

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {

				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivityForResult(intent, LOGIN_RESULT_CODE);
			}
		});
		mainFormLayer = new LinearLayout(this);

		llayer = new TableLayout(this);
		tRow = new TableRow(this);
		tRow.setLayoutParams(
				new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
		tRow.addView(textViewUserID);
		tRow.addView(button);
		// llayer.addView(textViewUserID);
		llayer.addView(tRow);

		searchFormlayer = new LinearLayout(this);
		searchResultlayer = new LinearLayout(this);
		mainFormLayer.addView(llayer);
		mainFormLayer.addView(searchFormlayer);
		mainFormLayer.addView(searchResultlayer);
		mainFormLayer.setOrientation(LinearLayout.VERTICAL);
		addContentView(mainFormLayer, new LinearLayout.LayoutParams(Toolbar.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		try_login();
	}

	public void try_login() {
		loadText();
		SiteApi.savePass(getApplicationContext(), loginname, hashpass);
		islogged = false;
		SiteApi.logged(loginname, hashpass, this);
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
		loginname = sPref.getString(SiteApi.SAVED_USER, "no_user");
		hashpass = sPref.getString(SiteApi.SAVED_HASH, "no_pass");
		resultlogin = sPref.getString(SiteApi.SAVED_RESULT, "notok");
		user_id = sPref.getString(SiteApi.SAVED_USERID, "0");
		searchFormlayer.removeAllViews();
		searchResultlayer.removeAllViews();

		if (resultlogin.equals("ok")) {
			islogged = true;
			button.setText("перелогуватись");
			textViewUserID.setText("Ви ввійшли як " + loginname);

			llayerSearchTable = new TableLayout(searchFormlayer.getContext());
			llayerSearchTable.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
					TableLayout.LayoutParams.WRAP_CONTENT));

			llayerSearchRow = new TableRow(llayerSearchTable.getContext());
			llayerSearchRow.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
					TableRow.LayoutParams.WRAP_CONTENT));
			llayerSearchRow1 = new TableRow(llayerSearchTable.getContext());
			llayerSearchRow1.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
					TableRow.LayoutParams.WRAP_CONTENT));
			llayerSearchTable.addView(llayerSearchRow);
			llayerSearchTable.addView(llayerSearchRow1);

			llayerSearchTextView = new TextView(llayerSearchRow.getContext());
			llayerSearchTextView.setText("Фільтр :");
			llayerSearchTextView.setGravity(Gravity.START);
			llayerSearchTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

			llayerSearchEditText = new EditText(llayerSearchRow.getContext());
			llayerSearchEditText.setText("");
			llayerSearchEditText.setGravity(Gravity.CENTER_HORIZONTAL);
			llayerSearchEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);

			llayerSearchButton = new Button(llayerSearchRow.getContext());
			llayerSearchButton.setText("Пошук(артикул)");
			llayerSearchButton.setGravity(Gravity.END);
			llayerSearchButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String searchText = new String(llayerSearchEditText.getText().toString());
					String searchGoal = "artikul"; 
					searchProducts(searchText, searchGoal);
					

				}

			});
			llayerSearchButtonDescr = new Button(llayerSearchRow.getContext());
			llayerSearchButtonDescr.setText("Пошук(опис)");
			llayerSearchButtonDescr.setGravity(Gravity.END);
			llayerSearchButtonDescr.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String searchText = new String(llayerSearchEditText.getText().toString());
					String searchGoal = "description"; 
					searchProducts(searchText, searchGoal);

				}

			});
			llayerSearchRow.addView(llayerSearchTextView);
			llayerSearchRow.addView(llayerSearchEditText);
			llayerSearchRow1.addView(llayerSearchButton);
			llayerSearchRow1.addView(llayerSearchButtonDescr);

			searchFormlayer.addView(llayerSearchTable);

		} else {
			islogged = false;
			button.setText("login");
			textViewUserID.setText("Not Registered User - ");

		}
	}

	public void searchProducts(String searchText, String SearchGoal) {
		SiteApi.getSearchResult(searchText, SearchGoal, this);
	}

	public void drawsearchtable() {
		searchResultlayer.removeAllViews();
		llayerResultTableScrollView = new ScrollView(searchResultlayer.getContext());
		llayerResultTable = new TableLayout(llayerResultTableScrollView.getContext());
		llayerResultTable.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));
		
		drawdata(searchItem,true, llayerResultTable);
		
		llayerResultTableScrollView.addView(llayerResultTable);

		searchResultlayer.addView(llayerResultTableScrollView);

	}

	
	public void drawdata(final JSONArray drawItem, boolean showAnalogButton, TableLayout tableResAnalogLayout){

		for (int i = 0; i < drawItem.length(); i++) {
			JSONObject jsonData;
			try {
				jsonData = drawItem.getJSONObject(i);

				TableRow tRow = new TableRow(this);
				tRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT));
				
				final TableLayout tRowAnalog = new TableLayout(this);
				tRowAnalog.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT));
				
				LinearLayout tRowIn = new LinearLayout(this);;
				//tRowIn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				//		LinearLayout.LayoutParams.WRAP_CONTENT));
				tRowIn.setOrientation(LinearLayout.VERTICAL);
				// TextView text1 = new TextView(this);
				// text1.setText(jsonData.getString("name"));
				TextView text2 = new TextView(this);
				text2.setText(SiteApi.convertToString(jsonData));
				text2.setLines(6);

				final ImageButton button = new ImageButton(this);
				button.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_shopping_cart_black_24dp));
				
				if (showAnalogButton){
					button.setId(i+1);
				} else {
					button.setId(KOEF_ANALOG_BUTTON_ID*(i+1)+1);
				} 
				
				button.setOnClickListener(new View.OnClickListener() {
					public void onClick(final View v) {
						// Perform action on click
						//
						//
						JSONObject itemToChouse = null;

						AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
						try {
							if (button.getId()<KOEF_ANALOG_BUTTON_ID){
								itemToChouse = drawItem.getJSONObject(button.getId() - 1);
							} else {
							//	Toast.makeText(getApplicationContext(), "ID: " + button.getId(),
								//		Toast.LENGTH_SHORT).show();									
								int number_i = (((button.getId()-1)/100)-1);
								itemToChouse = drawItem.getJSONObject(number_i);
							}					
							int quantity = Integer.valueOf(itemToChouse.getString("quantity"));
							final String[] mCatsName = new String[quantity];
							for (int i1 = 0; i1 < quantity; i1++) {
								mCatsName[i1] = Integer.toString(i1 + 1);
							}
							builder.setTitle(itemToChouse.getString("name")); 
							final JSONObject finalItemToChouse = itemToChouse;
							builder.setItems(mCatsName, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int item) {
									// TODO Auto-generated method stub
									Toast.makeText(getApplicationContext(), "Замовлено кількість: " + mCatsName[item],
											Toast.LENGTH_SHORT).show();
									makeOrder(finalItemToChouse, mCatsName[item]);
								}

							});
							builder.setCancelable(true);
							AlertDialog dialog = builder.create();
							dialog.show();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					
					}

				});
				tRowIn.addView(button);
				
				if (showAnalogButton){
					final Button buttonAnalog = new Button(this);
					buttonAnalog.setText("Аналоги");
					buttonAnalog.setId(i + SHIFT_ANALOG_BUTTON_ID + 1);
					
					buttonAnalog.setOnClickListener(new View.OnClickListener() {
						public void onClick(final View v) {
							// Perform action on click
							JSONObject itemToChouse = null;
							buttonAnalog.setVisibility(View.INVISIBLE);
							try {
								itemToChouse = drawItem.getJSONObject(buttonAnalog.getId() - SHIFT_ANALOG_BUTTON_ID -1);
								JSONArray drawItemAnalog = itemToChouse.getJSONArray("analog");
								
								tRowAnalog.removeAllViews();
								drawdata(drawItemAnalog, false, tRowAnalog);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						
						}

					});					
					
					tRowIn.addView(buttonAnalog);
				}				
				
				tRow.addView(tRowIn);
				tRow.addView(text2);
				tableResAnalogLayout.addView(tRow, new TableLayout.LayoutParams(Toolbar.LayoutParams.FILL_PARENT,
						Toolbar.LayoutParams.WRAP_CONTENT));
				tableResAnalogLayout.addView(tRowAnalog, new TableLayout.LayoutParams(Toolbar.LayoutParams.FILL_PARENT,
						Toolbar.LayoutParams.WRAP_CONTENT));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void makeOrder(JSONObject finalItemToChouse, String quantity) {
		// TODO Auto-generated method stub

		try {
			SiteApi.makeOrder(finalItemToChouse, quantity, user_id, loginname, hashpass, this);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
