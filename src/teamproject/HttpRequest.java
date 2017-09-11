package teamproject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpRequest {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static String request(String requestMethod, String url, String params) throws Exception {
        URL obj = null;

        boolean isGET = requestMethod.equalsIgnoreCase("get");
        boolean isPOST = requestMethod.equalsIgnoreCase("post");

        if (isGET) {
            obj = new URL(url + "?" + params);
        } else if (isPOST) {
            obj = new URL(url);
        } else
            System.exit(0);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        if (isGET) {
            con.setRequestMethod("GET");
        } else if (isPOST) {
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        }

        // Add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        if (isPOST) {
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();
        }

        int responseCode = con.getResponseCode();

        // Return Error Code
        if (responseCode != 200) return "Error Code: " + Integer.toString(responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        // Close input stream
        in.close();

        // Return response
        return response.toString();
    }
}