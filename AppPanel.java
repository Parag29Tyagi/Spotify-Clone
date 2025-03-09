import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class AppPanel extends JPanel {
    String imageAddress;
    Timer timer;
    String songAddress;
    // Declare buttons
    JButton plabutton;
    JButton nexButton;
    JButton prevButton;

    private String textToPrint;

    BufferedImage songPhoto;

    public void image() {
        try {
            songPhoto = ImageIO.read(new URL(imageAddress));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void song(String songUrl) {
        try {
            File audioFile = new File(songUrl);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            while (clip.isRunning()) {

                Thread.sleep(10);
            }

            clip.close();
            audioStream.close();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void AppPanelPrint(String text) {
        this.textToPrint = text;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(textToPrint, 20, 30);
        g.drawImage(songPhoto, 100, 100, nexButton);
    }

    void appLoop() {
        timer = new Timer(80, (abc) -> {
            repaint();
        });
        timer.start();
    }

    AppPanel() {
        setSize(1000, 1000);
        fetchData();
        JButton prevButton = new JButton("Prev");
        JButton playbutton = new JButton("PLay/Stop");
        JButton nexButton = new JButton("Next");

        // Add buttons to the panelf
        add(playbutton);
        add(prevButton);
        add(nexButton);
        image();

    }

    private void fetchData() {
        String address = "https://itunes.apple.com/search?term=arijit+singh&limit=25";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(address)).GET().build();

        try {
            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() == 200) {

                String completeData = res.body();

                JSONObject dataObject = new JSONObject(completeData);
                JSONArray dataArray = dataObject.getJSONArray("results");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject singleSong = dataArray.getJSONObject(i);
                    // System.out.println(singleSong.getString("trackName"));
                    String s = singleSong.getString("trackName");
                    imageAddress = singleSong.getString("artworkUrl100");
                    songAddress = singleSong.has("previewUrl") ? songAddress = singleSong.getString("previewUrl")
                            : "No Data Found";
                    song(songAddress);
                    AppPanelPrint(s);
                }
            } else {
                System.out.println("Some error occured");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

   }

} 
