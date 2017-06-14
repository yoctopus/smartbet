/*
 * Copyright 2017, Solutech RMS
 * Licensed under the Apache License, Version 2.0, "Solutech Limited".
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.json;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HttpConnector {
    String response = "";
    URL url;
    HttpURLConnection conn = null;
    int responseCode = 0;

    public String sendRequest(String path, HashMap<String, String> params) {
        try {
            Log.d("HttpConnector", "Starting process to connect path: " + path);
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("connection", "close");
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
        } catch (IOException ioe) {
            Log.d("HttpConnector", "Problem in getting connection.");
            ioe.printStackTrace();
        } catch (Exception e) {
            Log.d("HttpConnector", "Problem in getting connection. Safegaurd catch.");
            e.printStackTrace();
        }

        OutputStream os = null;
        try {
            if (null != conn) {
                os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(params));
                writer.flush();
                writer.close();
                os.close();
                responseCode = conn.getResponseCode();
            }
        } catch (IOException e) {
            Log.d("HttpConnector", "Problem in getting outputstream and passing parameter.");
            e.printStackTrace();
        }

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            Log.d("HttpConnector", "Connection success to path: " + path);
            String line;
            BufferedReader br = null;

            //getting the reader instance from connection
            try {
                if (null != conn) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }
            } catch (IOException e) {
                Log.d("HttpConnector", "Problem with opening reader.");
                e.printStackTrace();
            }

            //reading the response from stream
            try {
                if (null != br) {
                    while ((line = br.readLine()) != null) {
                        response += line;
                        Log.d("HttpConnector", "output: " + line);
                    }
                }
            } catch (IOException e) {
                response = "";
                Log.d("HttpConnector", "Problem in extracting the result.");
                e.printStackTrace();
            }
        } else {
            response = "";
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                Log.d("HttpConnector", "entry.Key: " + entry.getKey());
                Log.d("HttpConnector", "entry.Value: " + entry.getValue());
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (Exception e) {
            Log.d("HttpConnector", "Problem in getPostDataString while handling params.");
            e.printStackTrace();
            return "";
        }

        return result.toString();
    }
}
