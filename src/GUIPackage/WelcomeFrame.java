package GUIPackage;
import DataHandlingPackage.DataRefresh;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 *  Class the opens to the user to start the program 
 */
public class WelcomeFrame extends JFrame implements ActionListener{
  
  //GUI components initializing  
  JPanel refreshPanel;
  JLabel refreshLabel;
  JPanel mainPanel;
  JLabel helpLabel;
  JTextField refreshRate;
  JButton startButton;
  JComboBox<String> setCity;
  JComboBox<String> setArrivalDeparture;
  JComboBox<String> setTimeFrameOptions;
  String[] cityOptions = {"Houston", "Chicago", "New York", "Los Angeles", "Luanda"};
  String[] arrivalDepartureOptions = {"Arrivals","Departures"};
  String[] timeFrameOptions = {"6 Hours", "12 Hours", "24 Hours"};
  String setRefresh;
  public WelcomeFrame() {
    super("Entry");
    this.setBounds(100, 100, 460, 250);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayout(8,1));
    refreshPanel = new JPanel();
    refreshPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,10));    
    mainPanel.setBackground(new Color(78, 139, 237));
    
    //Creating the textfield
    refreshRate = new JTextField();
    refreshRate.setToolTipText("Enter the time in seconds you would like the program to automatically refresh. 0 for no auto refresh");    
    refreshRate.setPreferredSize(new Dimension(350,20));
    //Creating the button
    startButton = new JButton("Press to start");
    startButton.addActionListener(this);
    startButton.setPreferredSize(new Dimension(this.getWidth(),20));
   
    //Creating the labels
    helpLabel = new JLabel("Hover over most buttons and other components to see what they do!");
    refreshLabel = new JLabel("Refresh Rate: ");
    
    //Creates the comboboxes
    setCity = new JComboBox<>(cityOptions);
    setCity.setToolTipText("Select the primary city");
    setArrivalDeparture = new JComboBox(arrivalDepartureOptions);
    setArrivalDeparture.setToolTipText("Select if you want to see arrivals or departures");
    setTimeFrameOptions = new JComboBox(timeFrameOptions);
    setTimeFrameOptions.setToolTipText("Select if you want to see 6 hours from midnight, 12 hours from midnight or 24 hours from midnight");

    //Adding everything to the upper components
    refreshPanel.add(refreshLabel);
    refreshPanel.add(refreshRate);
    refreshPanel.setBackground(new Color(78, 139, 237));
    
    mainPanel.add(helpLabel);
    mainPanel.add(setCity);
    mainPanel.add(setArrivalDeparture);
    mainPanel.add(setTimeFrameOptions);
    mainPanel.add(refreshPanel);
    mainPanel.add(startButton);
    this.add(mainPanel);
    this.pack();
    this.setVisible(true);    
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    //If all the inputs are valid the program starts up, if not the error frame pops up
    //The program then allows the user to try again.
    if(command.equals("Press to start")) {
      String city = (String) setCity.getSelectedItem();
      String arrivalsOrDepature = (String) setArrivalDeparture.getSelectedItem();
      String timeFrameString = (String) setTimeFrameOptions.getSelectedItem();
      int refresh;
      int timeFrame = Integer.parseInt(timeFrameString.split(" ")[0]);
      
      try{
        refresh = Integer.parseInt(refreshRate.getText());
        
        if(refresh<0||refresh>999999999) {
          this.dispose();
          WelcomeFrame frame = new WelcomeFrame();
          ErrorFrame error = new ErrorFrame("Number is out of bounds. Please enter a new number.");
        }
        else {
          this.dispose();
          DataRefresh runUpdate = new DataRefresh(city,arrivalsOrDepature,timeFrame);
          MainFrame mainView = new MainFrame(city,arrivalsOrDepature,refresh,timeFrame);
        }
      }
      catch(NumberFormatException ex) {
        this.dispose();
        WelcomeFrame frame = new WelcomeFrame();
        ErrorFrame error = new ErrorFrame("Please enter a NUMBER.");
      }
    }
  }
}
