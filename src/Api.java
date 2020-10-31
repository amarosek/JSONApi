import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

import org.json.*;


public class Api {

    public static void main(String[] args) {
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        boolean exists = false;

        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://gorest.co.in/public-api/categories");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();


            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader((new InputStreamReader(connection.getInputStream())));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }

            String jsonString = responseContent.toString();

            JSONObject obj = new JSONObject(jsonString);
            JSONArray array = obj.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                String name = array.getJSONObject(i).getString("name");
                if (name.toLowerCase().equals("movies") && !exists) {
                    System.out.println("Category movies exists.");
                    exists = true;
                } else if (name.toLowerCase().contains("movies") && !exists) {
                    System.out.println("Category movies exists within another category!");
                    exists = true;
                }
            }
            if (exists == false) {
                System.out.println("Category movies doesn't exist!");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
}
