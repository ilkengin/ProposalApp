package com.ilkengin.proposalapp.utils;

import android.content.Context;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class JsonOperations {

    private static final String TAG = JsonOperations.class.getSimpleName();

    public static <T> List<T> getItemsFromFile(Context context, String fileName, Class<T> classType) {
        int id = context.getResources().getIdentifier(fileName, "raw", context.getPackageName());
        String itemsString = getJSONStringFromAssets(id, context);
        List<T> cardItems = getItemsFromString(itemsString,classType);
        return cardItems;
    }

    public static <T> List<T> getItemsFromUrl(Context context,String fileUrl, Class<T> classType) {
        String itemsString = "";

        try {
            itemsString = getJSONStringFromURL(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getItemsFromString(itemsString,classType);
    }

    public static String getJSONStringFromURL(String urlString) throws IOException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return jsonString;
    }

    public static <T extends Object> List<T> getItemsFromString(String itemsString, Class<T> classType) {
        List<T> items;

        try {

            Moshi moshi = new Moshi
                    .Builder()
                    .build();

            Type type = Types.newParameterizedType(List.class, classType);
            JsonAdapter<List<T>> adapter = moshi.adapter(type);

            items = adapter.fromJson(itemsString);
        } catch (IOException e) {
            Log.d(TAG, e.getLocalizedMessage());
            items = new ArrayList<>();
        }

        return items;
    }

    public static String getJSONStringFromAssets(int id, Context context) {
        try {
            InputStream is = context.getResources().openRawResource(id);

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            return responseStrBuilder.toString();

        } catch (IOException ex) {
            Log.d(Utils.class.toString(), ex.getLocalizedMessage());
            return "[]";
        } catch (NullPointerException ex) {
            Log.d(Utils.class.toString(), ex.getLocalizedMessage());
            return "[]";
        }
    }

}
