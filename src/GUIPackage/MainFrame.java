package GUIPackage;

import DataHandlingPackage.DataRefresh;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
/**
 * GUI class that displays the main view and all the components in it
 */
public class MainFrame extends JFrame implements ActionListener {
  //GUI Intialzations
  Timer timer;
  MenuBar menuBar;
  Menu changeCity;
  MenuItem houstonItem;
  MenuItem chicagoItem;
  MenuItem newYorkItem;
  MenuItem LosAngelesItem;
  MenuItem LuandaItem;
  JButton sixHours;
  JButton twelveHours;
  JButton twentyFourHours;
  JButton manualRefresh;
  JButton refreshSettings;
  JButton openList;
  JButton switchToArrivals;
  JButton switchToDepartures;
  JPanel buttonPanel;
  JPanel textfieldPanel;
  JLabel currentHour;
  JLabel arrivalOrDeparture;
  JLabel setRefresh;
  JLabel currentCity;
  Font labelFont;
  String selectedCity;
  String arrivalDeparture;
  int refreshRate;
  int currentTimeframe;

  //Constructor that takes in all the needed fields to display in the Labels
  public MainFrame(String primaryCity,String arrivalOrDeparture,int setRefresh,int currentHour) {
    super("Visual Airplane Tracker");
    this.setBounds(100, 100, 1050, 900);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    //Varaible refresh rate setting
    if(setRefresh>0) {
      timer = new Timer((setRefresh*1000),this);
    }
    else {
      timer = new Timer((100000*1000),this);
    }


    
    //creating the menu
    menuBar = new MenuBar();
    changeCity = new Menu("Change City");

    houstonItem = new MenuItem("Houston");
    houstonItem.addActionListener(this);
    
    chicagoItem = new MenuItem("Chicago");
    chicagoItem.addActionListener(this);
    
    newYorkItem = new MenuItem("New York");
    newYorkItem.addActionListener(this);
    
    LosAngelesItem = new MenuItem("Los Angeles");
    LosAngelesItem.addActionListener(this);
    
    LuandaItem = new MenuItem("Luanda");
    LuandaItem.addActionListener(this);
    
    changeCity.add(houstonItem);
    changeCity.add(chicagoItem);
    changeCity.add(newYorkItem);
    changeCity.add(LosAngelesItem);
    changeCity.add(LuandaItem);
    
    menuBar.add(changeCity);
    
    //setting varaibles for labels
    selectedCity = primaryCity;
    arrivalDeparture = arrivalOrDeparture;
    refreshRate = setRefresh;
    currentTimeframe = currentHour;
    
    labelFont = new Font("Ariel",Font.PLAIN,18);
    
    //creating and setting up panels
    textfieldPanel = new JPanel();
    buttonPanel = new JPanel();
    
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,10));
    textfieldPanel.setLayout(new GridLayout(9,1));
    textfieldPanel.setBackground(Color.CYAN);
    buttonPanel.setBackground(Color.BLUE);
    
    //creating the labels
    this.currentHour = new JLabel("Current set timeframe is: "+currentHour+" hours");
    this.arrivalOrDeparture = new JLabel("Current view setting is: "+arrivalOrDeparture);
    this.setRefresh = new JLabel("Current refresh rate is: "+setRefresh+" seconds");
    this.currentCity = new JLabel("Current selected city is "+primaryCity);

    this.currentHour.setFont(labelFont);
    this.arrivalOrDeparture.setFont(labelFont);
    this.setRefresh.setFont(labelFont);
    this.currentCity.setFont(labelFont);
    
    //creating and setting up the buttons
    sixHours = new JButton("6 Hours");
    sixHours.addActionListener(this);
    sixHours.setToolTipText("Click here to change the time of the viewable flights from 12:00 am to 5:59 am");

    twelveHours = new JButton("12 Hours");
    twelveHours.addActionListener(this);
    twelveHours.setToolTipText("Click here to change the time of the viewable flights from 12:00 am to 11:59 am");

    twentyFourHours = new JButton("24 Hours");
    twentyFourHours.addActionListener(this);
    twentyFourHours.setToolTipText("Click here to change the time of the viewable flights from 12:00 am to 11:59 pm");

    manualRefresh = new JButton("Manual Update");
    manualRefresh.addActionListener(this);
    manualRefresh.setToolTipText("Click here to manual refresh the data");

    refreshSettings = new JButton("Refresh Settings");
    refreshSettings.addActionListener(this);
    refreshSettings.setToolTipText("Click here to change the automatic refresh rate");

    openList = new JButton("Display list");
    openList.addActionListener(this);
    openList.setToolTipText("Click here to display the list");

    switchToArrivals = new JButton("Arrivals");
    switchToArrivals.addActionListener(this);
    switchToArrivals.setToolTipText("Click here to show arrivals");

    switchToDepartures = new JButton("Departures");
    switchToDepartures.addActionListener(this);
    switchToDepartures.setToolTipText("Click here to show departures");
    
    //adding all the elements to needed upper compenents
    textfieldPanel.add(this.currentCity);
    textfieldPanel.add(this.currentHour);
    textfieldPanel.add(this.arrivalOrDeparture);
    textfieldPanel.add(this.setRefresh);
    
    buttonPanel.add(sixHours);
    buttonPanel.add(twelveHours);
    buttonPanel.add(twentyFourHours);
    buttonPanel.add(openList);
    buttonPanel.add(switchToArrivals);
    buttonPanel.add(switchToDepartures);
    buttonPanel.add(manualRefresh);
    buttonPanel.add(refreshSettings);
    
    this.add(buttonPanel, BorderLayout.NORTH);
    this.add(textfieldPanel,BorderLayout.WEST);
    this.setMenuBar(menuBar);
    
    JMapViewer map = new JMapViewer();
    VisualHelper test = new VisualHelper("Houston",map);
    this.add(map);

    
    this.setVisible(true);
    timer.start();

  }
  public static void main(String[]args) {
    //Opening the welcome frame
    //WelcomeFrame frame = new WelcomeFrame();
    
    DataRefresh runUpdate = new DataRefresh("Houston","Arrivals",24);
    MainFrame frame = new MainFrame("Houston","Arrivals",0,24);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    
    //Detects when the timer has hit the limit to refresh automatically
    if(e.getSource().equals(timer)) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh(selectedCity,arrivalDeparture,currentTimeframe);
      MainFrame frame = new MainFrame(selectedCity,arrivalDeparture,refreshRate,currentTimeframe);
    }
    //Next 3 switch the timeframe of the flights
    else if(command.equals("6 Hours")) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh(selectedCity,arrivalDeparture,6);
      MainFrame frame = new MainFrame(selectedCity,arrivalDeparture,refreshRate,6);
    }
    else if (command.equals("12 Hours")) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh(selectedCity,arrivalDeparture,12);
      MainFrame frame = new MainFrame(selectedCity,arrivalDeparture,refreshRate,12);
    }
    else if (command.equals("24 Hours")) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh(selectedCity,arrivalDeparture,24);
      MainFrame frame = new MainFrame(selectedCity,arrivalDeparture,refreshRate,24);
    }
    //Opens the list frame
    else if (command.equals("Display list")) {
      timer.stop();
      ListViewFrame listViewFrame = new ListViewFrame();
      timer.start();
    }
    //The next 2 switch between arrivals and departures
    else if (command.equals("Arrivals")) {  
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh(selectedCity,"Arrivals",currentTimeframe);
      MainFrame frame = new MainFrame(selectedCity,"Arrivals",refreshRate,currentTimeframe);
    }
    else if (command.equals("Departures")) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh(selectedCity,"Departures",currentTimeframe);
      MainFrame frame = new MainFrame(selectedCity,"Departures",refreshRate,currentTimeframe);
    }
    //Updates the data manually
    else if (command.equals("Manual Update")) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh(selectedCity,arrivalDeparture,currentTimeframe);
      MainFrame frame = new MainFrame(selectedCity,arrivalDeparture,refreshRate,currentTimeframe);
    }
    //Opens the window to change the refresh settings
    else if (command.equals("Refresh Settings")) {
      timer.stop();
      this.dispose();
      RefreshFrame refreshFrame = new RefreshFrame(selectedCity,arrivalDeparture,currentTimeframe);
    }
    //Next 5 change the current primary city
    else if(command.equals("Houston")) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh("Houston",arrivalDeparture,currentTimeframe);
      MainFrame frame = new MainFrame("Houston",arrivalDeparture,refreshRate,currentTimeframe);
    }
    else if(command.equals("Chicago")) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh("Chicago",arrivalDeparture,currentTimeframe);
      MainFrame frame = new MainFrame("Chicago",arrivalDeparture,refreshRate,currentTimeframe);
    }
    else if(command.equals("Los Angeles")) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh("Los Angeles",arrivalDeparture,currentTimeframe);
      MainFrame frame = new MainFrame("Los Angeles",arrivalDeparture,refreshRate,currentTimeframe);
    }
    else if(command.equals("New York")) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh("New York",arrivalDeparture,currentTimeframe);
      MainFrame frame = new MainFrame("New York",arrivalDeparture,refreshRate,currentTimeframe);
    }
    else if(command.equals("Luanda")) {
      timer.stop();
      this.dispose();
      DataRefresh runUpdate = new DataRefresh("Luanda",arrivalDeparture,currentTimeframe);
      MainFrame frame = new MainFrame("Luanda",arrivalDeparture,refreshRate,currentTimeframe);
    }
  }
}
