package GUIPackage;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
/**
 *  Class that allows the user to change the refresh settings
 */
public class RefreshFrame extends JFrame implements ActionListener{
  
  //GUI components initializing  
  JPanel refreshPanel;
  JLabel refreshLabel;
  JTextField refreshRate;
  JButton startButton;
  String city;
  String arrivalsDepartures;
  int refresh;
  int currentTimeframe;
  int planes;
  
  public RefreshFrame(String primaryCity,String arrivalOrDeparture,int currentHour) {
    super("Refresh Settings");
    this.setBounds(100, 100, 600, 600);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
    this.city = primaryCity;
    this.arrivalsDepartures = arrivalOrDeparture;
    this.currentTimeframe = currentHour;
    
    refreshPanel = new JPanel();
    refreshLabel = new JLabel("Refresh Rate (in seconds): ");
    refreshPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,10));
    refreshRate = new JTextField();
    refreshRate.setToolTipText("Enter the time in seconds you would like the program to automatically refresh. 0 for no auto refresh");
    refreshRate.setPreferredSize(new Dimension(300,20));
    startButton = new JButton("Set Refresh");
    startButton.addActionListener(this);
    
    refreshPanel.add(refreshLabel);
    refreshPanel.add(refreshRate);
    refreshPanel.add(startButton);

    this.add(refreshPanel);
    this.pack();
    this.setVisible(true);    
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    //When the user clicks the button, it checks if the input is valid. 
    //If not it calls the error frame, and allows the user to try again
    //If it is, it sets the refresh and continues with the program.
    if(command.equals("Set Refresh")) {
      try{
        refresh = Integer.parseInt(refreshRate.getText());
        if(refresh<0||refresh>99999999) {
          this.dispose();
          RefreshFrame refreshFrame = new RefreshFrame(city,arrivalsDepartures,currentTimeframe);
          ErrorFrame error = new ErrorFrame("Number is out of bounds. Please enter a new number.");
        }
      }
      catch(NumberFormatException ex) {
        this.dispose();
        RefreshFrame refreshFrame = new RefreshFrame(city,arrivalsDepartures,currentTimeframe);
        ErrorFrame error = new ErrorFrame("Please enter a NUMBER.");
      }
      
      this.dispose();
      MainFrame mainView = new MainFrame(city,arrivalsDepartures,refresh,currentTimeframe);
    }
  }
}
