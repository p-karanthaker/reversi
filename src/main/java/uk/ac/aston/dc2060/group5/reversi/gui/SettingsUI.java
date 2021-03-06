package uk.ac.aston.dc2060.group5.reversi.gui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uk.ac.aston.dc2060.group5.reversi.Settings;

import java.awt.Dialog;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Created by Karan on 04/12/2016.
 */
public class SettingsUI extends JFrame {

  // Allows us to create a suitable size window for any screen resolution.
  private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

  private final int HEIGHT = SCREEN_SIZE.height * 1 / 2;
  private final int WIDTH = SCREEN_SIZE.width * 1 / 3;

  private Settings[] setting;
  private BoardUI boardUI;
  private JFrame frame;

  /**
   * Constructs the UI for the theme settings panel.
   *
   * @param boardUI the gui for the game.
   */
  public SettingsUI(BoardUI boardUI) {
    this.boardUI = boardUI;

    //Create main window and set exit on close
    frame = new JFrame("Settings");
    JDialog dialog = new JDialog(frame, "Timer Options", Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    dialog.setPreferredSize(new Dimension(400, 100));
    dialog.setLayout(new FlowLayout());

    dialog.add(new JLabel("Theme: "));
    dialog.add(populateList());
    dialog.setResizable(true);
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
  }

  private JComboBox populateList() {
    Gson gson = new Gson();

    Settings[] settings = null;
    try {
      InputStream is = this.getClass().getResourceAsStream("/themes/themes.json");
      Reader reader = new InputStreamReader(is, "UTF-8");
      settings = gson.fromJson(reader, Settings[].class);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    this.setting = settings;

    ArrayList<String> themeNames = new ArrayList<>();
    for (Settings setting : settings) {
      themeNames.add(setting.getName());
    }

    JComboBox comboBox = new JComboBox(themeNames.toArray());
    comboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        changeTheme((String) comboBox.getSelectedItem());
      }
    });
    return comboBox;
  }

  /**
   * Updates the config.json file with the new theme settings and updates the UI.
   *
   * @param settingName the name of the setting chosen.
   */
  public void changeTheme(String settingName) {
    for (Settings setting : this.setting) {
      if (setting.getName().equals(settingName)) {
        Gson gson = new Gson();
        JsonElement jsonElement = null;
        try {
          InputStream is = this.getClass().getResourceAsStream("/config.json");
          Reader reader = new InputStreamReader(is, "UTF-8");
          jsonElement = gson.fromJson(reader, JsonElement.class);
        } catch (IOException event) {
          event.printStackTrace();
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

        JsonArray pieceNameList = new JsonArray();
        JsonObject pieceNames = new JsonObject();
        pieceNames.addProperty("black", setting.getPieceNames()[0].getBlack());
        pieceNames.addProperty("white", setting.getPieceNames()[0].getWhite());
        pieceNameList.add(pieceNames);
        theme.add("pieceNames", pieceNameList);

        Path configPath = Paths.get(System.getProperty("user.home") + File.separator + "reversi"
            + File.separator + "config.json");

        //Custom button text
        Object[] options = {"Apply", "Cancel"};
        int index = JOptionPane.showOptionDialog(this.boardUI.mainWindow,
            "Confirm to apply theme.",
            "Apply Changes",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        switch (index) {
          case (JOptionPane.YES_OPTION):
            try (FileWriter fileWriter = new FileWriter(configPath.toFile())) {
              fileWriter.write(theme.toString());
            } catch (IOException event) {
              event.printStackTrace();
            } finally {
              frame.dispose();
              boardUI.loadTheme();
              boardUI.game.update();
            }
            break;
          case (JOptionPane.NO_OPTION):
          default:
            frame.dispose();
            break;
        }
      }
    }
  }

}
