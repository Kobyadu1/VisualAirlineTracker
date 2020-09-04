package GUIPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * Frame that pops up when there is an error in the program
 */
public class ErrorFrame extends JFrame implements ActionListener{
  JLabel error;
  JButton continueButton;
  JPanel mainPanel;
  
  public ErrorFrame(String errorMessage) {
    super("ERROR");
    this.setBounds(100, 100, 400, 100);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    mainPanel = new JPanel();
    mainPanel.setBackground(Color.red);
    
    error = new JLabel(errorMessage);
    
    continueButton = new JButton("Continue");
    continueButton.addActionListener(this);
    
    mainPanel.add(error);
    mainPanel.add(continueButton);
    
    this.add(mainPanel);
    this.setVisible(true);
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    if(command.equals("Continue")) {
      this.dispose();
    }
  }
}
