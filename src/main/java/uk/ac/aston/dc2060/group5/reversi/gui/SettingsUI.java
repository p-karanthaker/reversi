package uk.ac.aston.dc2060.group5.reversi.gui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uk.ac.aston.dc2060.group5.reversi.Settings;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Created by Karan on 04/12/2016.
 */
public class SettingsUI extends JFrame {

  // Allows us to create a suitable size window for any screen resolution.
  private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

  private final int HEIGHT = SCREEN_SIZE.height * 1/2;
  private final int WIDTH = SCREEN_SIZE.width * 1/3;

  private Settings[] setting;
  private BoardUI boardUI;
  private JFrame frame;

  public SettingsUI(BoardUI boardUI) {
    this.boardUI = boardUI;

    //Create main window and set exit on close
    frame = new JFrame("Settings");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setPreferredSize(new Dimension(400, 100));
    frame.setLayout(new FlowLayout());

    frame.add(new JLabel("Theme: "));
    frame.add(populateList());
    frame.setResizable(true);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private JComboBox populateList() {
    Gson gson = new Gson();

    Settings[] settings = null;
    try {
      InputStream is = this.getClass().getResourceAsStream("/themes/themes.json");
      Reader reader = new InputStreamReader(is, "UTF-8");
      settings = gson.fromJson(reader, Settings[].class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.setting = settings;

    ArrayList<String> themeNames = new ArrayList<>();
    for (Settings setting : settings) {
      themeNames.add(setting.getName());
    }

    JComboBox jComboBox = new JComboBox(themeNames.toArray());
    jComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        changeTheme((String) jComboBox.getSelectedItem());
      }
    });
    return jComboBox;
  }

  public void changeTheme(String settingName) {
    for (Settings setting : this.setting) {
      if (setting.getName().equals(settingName)) {
        Gson gson = new Gson();
        JsonElement jsonElement = null;
        try {
          InputStream is = this.getClass().getResourceAsStream("/config.json");
          Reader reader = new InputStreamReader(is, "UTF-8");
          jsonElement = gson.fromJson(reader, JsonElement.class);
        } catch (IOException e) {
          e.printStackTrace();
        }

        JsonObject theme = jsonElement.getAsJsonObject();
        theme.addProperty("name", setting.getName());
        theme.addProperty("bgColour", setting.getBgColour());
        theme.addProperty("bgImage", setting.getBgImage());

        JsonArray opacityList = new JsonArray();
        JsonObject opacity = new JsonObject();
        opacity.addProperty("light", setting.getOpacity()[0].getLight());
        opacity.addProperty("dark", setting.getOpacity()[0].getDark());
        opacityList.add(opacity);
        theme.add("opacity", opacityList);

        JsonArray pieceColoursList = new JsonArray();
        JsonObject pieceColours = new JsonObject();
        pieceColours.addProperty("black", setting.getPieceColours()[0].getBlack());
        pieceColours.addProperty("white", setting.getPieceColours()[0].getWhite());
        pieceColoursList.add(pieceColours);
        theme.add("pieceColours", pieceColoursList);

        Path configPath = Paths.get(System.getProperty("user.home") + File.separator + "reversi" + File.separator + "config.json");

        //Custom button text
        Object[] options = { "OK" };
        int n = JOptionPane.showOptionDialog(this.boardUI.mainWindow,
            "The new themes will be available in your next game.",
            "Apply Changes",
            JOptionPane.YES_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        switch(n) {
          case (JOptionPane.YES_OPTION):
          default:
            try {
              FileWriter fileWriter = new FileWriter(configPath.toFile());
              fileWriter.write(theme.toString());
              fileWriter.close();
              frame.dispose();
            } catch (IOException e) {
              e.printStackTrace();
            } finally {
              //System.exit(0);
            }
            break;
        }
      }
    }
  }

}
