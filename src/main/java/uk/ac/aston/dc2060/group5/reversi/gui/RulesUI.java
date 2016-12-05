package uk.ac.aston.dc2060.group5.reversi.gui;

import com.google.gson.Gson;

import uk.ac.aston.dc2060.group5.reversi.rulesets.Rules;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Created by Karan on 04/12/2016.
 */
public class RulesUI extends JFrame {

  // Allows us to create a suitable size window for any screen resolution.
  private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

  private final int HEIGHT = SCREEN_SIZE.height * 1/2;
  private final int WIDTH = SCREEN_SIZE.width * 1/3;

  public RulesUI() {
    //Create main window and set exit on close
    JFrame frame = new JFrame("Reversi Rules");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));

    frame.add(createPanes(), BorderLayout.CENTER);
    frame.setResizable(false);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private JPanel createPanes() {
    JTabbedPane tabbedPane = new JTabbedPane();

    Gson gson = new Gson();

    Rules[] rules = null;
    try {
      InputStream is = this.getClass().getResourceAsStream("/rules/rules.json");
      Reader reader = new InputStreamReader(is, "UTF-8");
      rules = gson.fromJson(reader, Rules[].class);
    } catch (IOException e) {
      e.printStackTrace();
    }


    for (Rules rule : rules) {
      if (!rule.getImage().isEmpty()) {
        try {
          InputStream imageStream = this.getClass().getResourceAsStream("/rules/" + rule.getImage());
          JComponent panel1 = makeRulePanel(rule.getText(), new ImageIcon(ImageIO.read(imageStream)));
          tabbedPane.addTab(rule.getTitle(), panel1);
        } catch (IOException e) {

        }
      } else {
        JComponent panel1 = makeRulePanel(rule.getText(), null);
        tabbedPane.addTab(rule.getTitle(), panel1);
      }
    }

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1,1));
    panel.add(tabbedPane);
    return panel;
  }

  protected JComponent makeRulePanel(String text, ImageIcon imageIcon) {
    JPanel panel = new JPanel(false);
    JTextArea textArea = new JTextArea();
    textArea.setText(text);
    textArea.setLineWrap(true);
    textArea.setEditable(false);

    JScrollPane scroll = new JScrollPane (textArea,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    panel.add(scroll);
    
    JLabel image = new JLabel(imageIcon);

    if (imageIcon != null) {
      panel.setLayout(new GridLayout(1, 2));
      panel.add(image);
    } else {
      panel.setLayout(new GridLayout(1, 1));
    }

    return panel;
  }

}
