import java.io.*;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import com.google.gson.Gson;

import org.json.simple.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

public class GotoIstanbul {
    static String s1 = "";
    static JSONArray result = new JSONArray();

    // Function to get data from Mapbox's api call
    public static void call_me() throws Exception {
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/historical.json?limit=10&types=poi&proximity=28.735835883507548,41.654704785638984&access_token=pk.eyJ1Ijoic2hvYmhpdHNybTE4dGgiLCJhIjoiY2tpZzV0aGc2MWlsMDJ4bzV0M2MyNGtocSJ9.gs3Gkr_a0MH_-n8AGy6Y_g";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        // add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // getting the string response from the response received from the api
        String stringToParse = response.toString();
        // JSONParser parser = new JSONParser(); JSONObject json = (JSONObject)
        // parser.parse(stringToParse);

        // Converting the string obtained to the JSON object
        JSONObject myResponse = new JSONObject(response.toString());

        /////////////////////////////////////////////////////////////////////////////////

        JSONArray ja = (JSONArray) myResponse.get("features");
        // JSONArray result = new JSONArray();

        //////////////////// ********************************** */

        // Iterating over the json array to extract the required parameters from the
        // json object
        for (int i = 0; i < ja.length(); i++) {
            JSONObject ja1 = (JSONObject) ja.getJSONObject(i);
            String place_name = ja1.getString("place_name");
            JSONObject sm1 = (JSONObject) ja.getJSONObject(4).get("properties");
            String address = sm1.getString("address");
            String category = sm1.getString("category");

            JSONObject jo = new JSONObject();
            jo.put("place_name", place_name);
            jo.put("category", category);
            jo.put("region", address);

            result.put(jo);

            // System.out.println("Place_name= "+place_name);
            // System.out.println("address= "+address);
            // System.out.println("Category= "+category);
            // System.out.println("Place_name= "+place_name);
        }
        System.out.println("*************************************************");
        // System.out.println(result);
        System.out.println("*************************************************");

        ///////////////////// *********************************** */

        /*
         * Iterator itr2 = ja.iterator();
         * 
         * while (itr2.hasNext()) { Iterator<Map.Entry> itr1 = ((Map)
         * itr2.next()).entrySet().iterator(); Map m= new LinkedHashMap(3); while
         * (itr1.hasNext()) { Map.Entry pair = itr1.next(); m = new LinkedHashMap(3); //
         * System.out.println(pair.getKey() + " : " + pair.getValue()); String s11 =
         * (String) pair.getKey(), s22 = (String) pair.getValue(); if (s11 ==
         * "place_name" || s11 == "category" || s11 == "address") { m.put(s11, s22); } }
         * result.add(m); } System.out.println(
         * "###################222222222222222222222222222222########################################"
         * ); System.out.println(result); System.out.println(
         * "###################222222222222222222222222222222########################################"
         * );
         */

        ////////////////////////////////////////////////////////////////////////////////////////////////
        // JSONObject jo11 = new JSONObject();
        // putting data to JSONObject
        // jo.put("firstName", "John");
        // jo.put("lastName", "Smith");
        // jo.put("age", 25);

    }

    public static void main(String args[]) throws IOException {
        try {
            call_me();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // Converting JSON array to json string and returning to the api call
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            int serverPort = 8000;
            HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

            server.createContext("/api/Istanbul", (exchange -> {
            JSONObject jo11 = new JSONObject();
           // JSONArray jaarr = new JSONArray();
            // populate the array
                try {
                    jo11.put("Itinerary", result);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            String respText= jo11.toString();
            //JSONArray respText = (JSONArray)result;
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
            exchange.close();}));
        server.setExecutor(null); // creates a default executor
        server.start();


    }    
}