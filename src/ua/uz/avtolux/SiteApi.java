package ua.uz.avtolux;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class SiteApi {

	final static String SAVED_USER = "saved_user";
	final static String SAVED_HASH = "saved_hash";
	final static String SAVED_RESULT = "result_logging";
	final static String SAVED_USERID = "saved_userid";
	final static String USERDATA = "usersdata";
	final static String URL_SITE = "http://avtolux.uz.ua/flow.php";

	public static boolean logged(String user, String hashpass, final MainActivity mainact){

		final AQuery aq = new AQuery(mainact.getApplicationContext());

		String url = URL_SITE;

		Map<String, Object> params = new HashMap<String, Object>();


		params.put("user", user);
		params.put("password", hashpass);

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				//Toast.makeText(aq.getContext(), "Info:" +json.toString(), Toast.LENGTH_LONG).show();
				if (json!=null){
					try {
						if (json.getString("rez").equals("ok")){
							saveText(mainact, json);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			private void saveText(final MainActivity mainact, JSONObject json) throws JSONException {
				SharedPreferences sPref;
				sPref =mainact.getApplicationContext().getSharedPreferences(USERDATA,  Context.MODE_PRIVATE);
				Editor ed = sPref.edit();
				ed.putString(SAVED_USER,json.getString("user"));
				ed.putString(SAVED_HASH,json.getString("pass"));
				ed.putString(SAVED_RESULT,json.getString("rez"));
				ed.putString(SAVED_USERID,json.getString("user_id"));
				ed.commit();

				mainact.loadText();

				//Toast.makeText(mainact.getApplicationContext(), "Text saved", Toast.LENGTH_SHORT).show();
			}
		});  

		return true;
	}

	public static void savePass(Context c, String user, String hash) {
		SharedPreferences sPref;
		sPref =c.getSharedPreferences(USERDATA,  Context.MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putString(SAVED_USER,user);
		ed.putString(SAVED_HASH,hash);
		ed.putString(SAVED_RESULT,"notok");
		ed.putString(SAVED_USERID,"0");
		ed.commit();

	}

	public static boolean getSearchResult(String search, String goal, final MainActivity mainact1) {

		final AQuery aq1 = new AQuery(mainact1.getApplicationContext());

		String url = URL_SITE;

		//       Toast.makeText(aq1.getContext(), "Error2:" + search.toString(), Toast.LENGTH_LONG).show();


		Map<String, Object> params = new HashMap<String, Object>();
		params.put("search", search);
		params.put("goal", goal);

		aq1.ajax(url, params, JSONArray.class, new AjaxCallback<JSONArray>() {
			@Override
			public void callback(String url, JSONArray jSONArrayTemp, AjaxStatus status) {

				mainact1.searchItem = jSONArrayTemp;

				if (jSONArrayTemp != null) {
					//Toast.makeText(aq1.getContext(), "Error2:" + jSONArrayTemp.toString(), Toast.LENGTH_LONG).show();

					mainact1.drawsearchtable();
					//successful ajax call, show status code and json content
				} else {
					//ajax error, show error code

					Toast.makeText(aq1.getContext(), "Error2:" + status.getMessage(), Toast.LENGTH_LONG).show();

				}
			}
		});

		return true;
	}

	/**
	 * @param jsonData
	 * @return
	 */
	public static String convertToString(JSONObject jsonData) {
		String sTemp = "";
		try {
			int quantitytemp = Integer.valueOf(jsonData.getString("quantity"));
			String quantity;
			switch (quantitytemp){
			case 0:
				quantity="0";break;
			case 1:
				quantity="1";break;
			default:
				quantity=">1";break;
			}

			String artikul = jsonData.getString("name");
			int lenArtikul = artikul.length();
			artikul = artikul.substring(0, 3);
			for(int j=2;j<lenArtikul;j++) artikul = artikul+"X";
      	
			int quantityprice = Integer.valueOf((int) (jsonData.getDouble("priceusd")+0.5));
     	
			sTemp += "Назва: " + jsonData.getString("description");
			// sTemp += "\nКод: " + jsonData.getString("name");
			sTemp += "\nКод: " + artikul;
			// sTemp += "\nЦіна: " + jsonData.getString("priceusd");
			sTemp += "\nЦіна: " + quantityprice;
			sTemp += "\nКількість: " + quantity;

			//            sTemp += "\nID: " + jsonData.getString("product_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sTemp;
	}

	public static void makeOrder(JSONObject finalItemToChouse, String quantity, String user_id, String loginname,
			String hashpass, MainActivity mainActivity) throws JSONException {
		// TODO Auto-generated method stub
		final AQuery aq = new AQuery(mainActivity.getApplicationContext());

		String url = URL_SITE;

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("quantity", quantity);
		params.put("user_id", user_id);
		params.put("name", finalItemToChouse.getString("name"));
		params.put("product_id", finalItemToChouse.getString("product_id"));
		params.put("priceusd", finalItemToChouse.getString("priceusd"));
		params.put("user", loginname);
		params.put("password", hashpass);
		params.put("text", convertToString(finalItemToChouse));

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject JSONObjectTemp, AjaxStatus status) {

				//mainact1.searchItem = jSONArrayTemp;

				if (JSONObjectTemp != null) {
					//	mainact1.drawsearchtable();

					//successful ajax call, show status code and json content
				} else {
					//ajax error, show error code

					Toast.makeText(aq.getContext(), "Error:" + status.getMessage(), Toast.LENGTH_LONG).show();

				}
			}
		});


	}




}
