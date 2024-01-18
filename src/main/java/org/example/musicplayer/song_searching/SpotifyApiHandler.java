package org.example.musicplayer.song_searching;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SpotifyApiHandler {

    private static final String CLIENT_ID = "a861ad43a377467b9417c975c1b8332b"; // Your Spotify API client ID
    private static final String CLIENT_SECRET = "65fa32ed7e2f4527ae20a6126a142746"; // Your Spotify API client secret

    private final String accessToken;

    public SpotifyApiHandler(String accessToken) {
        this.accessToken = accessToken;
    }

    public static String getAccessToken() {
        try {
            String endpoint = "https://accounts.spotify.com/api/token";
            String grantType = "client_credentials";
            String requestBody = "grant_type=" + grantType;

            String clientIdSecret = CLIENT_ID + ":" + CLIENT_SECRET;
            String accessToken = java.util.Base64.getEncoder().encodeToString(clientIdSecret.getBytes());

            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + accessToken);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();
                // Parse the JSON response to get the access token
                // Example: {"access_token":"yourAccessToken","token_type":"Bearer","expires_in":3600}
                // Extract "yourAccessToken" from the JSON response
                return response.split("\"")[3];
            } else {
                System.out.println("HTTP request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void getTrackInfo(String accessToken, String songName, String artistName) {
        try {

            String endpoint = "https://api.spotify.com/v1/search";
            String query = "q=" + songName + "%20" + artistName + "&type=track&limit=1";
            String url = endpoint + "?" + query;

            System.out.println("URL: " + url);

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parse the JSON response to get the track information
                JsonParser parser = new JsonParser();
                JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();
                JsonObject tracks = jsonResponse.getAsJsonObject("tracks");
                JsonArray items = tracks.getAsJsonArray("items");

                if(!items.isJsonNull() && !items.isEmpty()) {
                    JsonObject firstTrack = items.get(0).getAsJsonObject();

                    String foundSongName = firstTrack.get("name").getAsString();
                    String foundArtistName = firstTrack.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
                    String albumName = firstTrack.getAsJsonObject("album").get("name").getAsString();
                    String albumCoverUrl = firstTrack.getAsJsonObject("album").getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();
                    int durationMs = firstTrack.get("duration_ms").getAsInt();

                    // Example: Display the information in the console
                    System.out.println("Song Name: " + songName);
                    System.out.println("Artist Name: " + foundArtistName);
                    System.out.println("Album Name: " + albumName);
                    System.out.println("Album Cover URL: " + albumCoverUrl);
                    System.out.println("Duration (ms): " + durationMs);
                } else {
                    System.out.println("No track found");
                }

                // Perform other actions to display this information in your JavaFX application
            } else {
                System.out.println("HTTP request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
