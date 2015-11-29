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
	
    public static boolean getSearchResult(String search, final MainActivity mainact1) {

        final AQuery aq1 = new AQuery(mainact1.getApplicationContext());

		String url = URL_SITE;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("search", search);
	    
//		String url1 = new StringBuilder().append("http://avtolux.uz.ua/flow.php?search=").append(search).toString();
	       
//      Toast.makeText(aq1.getContext(), "Error1:" + url , Toast.LENGTH_LONG).show();
        aq1.ajax(url, params, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray jSONArrayTemp, AjaxStatus status) {

            	mainact1.searchItem = jSONArrayTemp;
				
                if (jSONArrayTemp != null) {
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
    
    public static String convertToString(JSONObject jsonData) {
        String sTemp = null;
        try {
            sTemp = "Name: " + jsonData.getString("name");
            sTemp += "\nKilk: " + jsonData.getString("quantity");
            sTemp += "\nCena: " + jsonData.getString("priceusd");
            sTemp += "\nID: " + jsonData.getString("product_id");
            sTemp += "\nInfo: " + jsonData.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sTemp;
    }
    

}
