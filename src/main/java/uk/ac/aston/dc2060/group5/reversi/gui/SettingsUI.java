package uk.ac.aston.dc2060.group5.reversi.gui;

import com.google.gson.Gson;

import uk.ac.aston.dc2060.group5.reversi.Settings;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

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

  public SettingsUI(BoardUI boardUI) {
    this.boardUI = boardUI;

    //Create main window and set exit on close
    JFrame frame = new JFrame("Settings");
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

  private void changeTheme(String settingName) {
    for (Settings setting : this.setting) {
      if (setting.getName().equals(settingName)) {
        System.out.println(settingName);
      }
    }
  }

}
