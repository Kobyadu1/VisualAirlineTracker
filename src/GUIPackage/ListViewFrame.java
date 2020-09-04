package GUIPackage;

import DatabasePackage.DatabaseConnect;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Displays the data as a list
 */

public class ListViewFrame extends JFrame implements ActionListener {
  //GUI initilaizations
  private final Color TABLE_COLOR = new Color(43, 70, 114);
  private final Font HEADER_FONT = new Font("Arial", Font.PLAIN, 14);

  FlightTableModel tableModel;
  ArrayList<ArrayList<String>> flightData;
  String joinQuery;
  DatabaseConnect databaseConnection;
  Connection myDatabaseConnection;
  ResultSet rs;
  
  JScrollPane mainPane;
  JPanel listPanel;
  JPanel buttonPanel;
  JButton filterAndSearch;
  JButton bookmark;
  JButton resetBookmarks;
  JLabel helpLabel;
  JTable mainFlightTable;

  //Default constructor when there is no bookmarked data
  public ListViewFrame() {
    super("List of Data");
    this.setBounds(100, 100, 1000, 1000);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setLayout(new BorderLayout());
    //Setting up the database connection
    databaseConnection = new DatabaseConnect("FlightDatabase");
    myDatabaseConnection = databaseConnection.getDatabaseConnection();

    
    //Setting up the upper components
    mainFlightTable = new JTable();
    mainPane = new JScrollPane();
    listPanel = new JPanel();
    buttonPanel = new JPanel();
    //Setting up the buttons
    bookmark = new JButton("Bookmark/Unbookmark selected row");
    bookmark.addActionListener(this);
    bookmark.setToolTipText("Click here to bookmark the selected row");
    
    resetBookmarks = new JButton("Reset Bookmarked");
    resetBookmarks.addActionListener(this);
    resetBookmarks.setToolTipText("CLick here to reset all bookmarks");
    
    filterAndSearch = new JButton("Search/Filter Data");
    filterAndSearch.addActionListener(this);
    filterAndSearch.setToolTipText("Click here to search for data in the table");

    //Setting up the table and adding the data, using a custom tablemodel
    String query = "SELECT FlightNumber, Airline, Time, "
            + "HostCityName, Terminal, Status "
            + "FROM FlightData "
            + "LEFT JOIN airlineData "
            + "ON FlightData.AirlineCode = airlineData.AirlineCode "
            + "LEFT JOIN landingTimeData "
            + "ON FlightData.LandingTimeCode = landingTimeData.LandingTimeCode "
            + "LEFT JOIN airportData "
            + "ON flightData.ExternalAirportCode = airportData.AirportCode "
            + "LEFT JOIN statusData "
            + "ON FlightData.StatusCode = statusData.StatusCode ";
    try{
      Statement statement = myDatabaseConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
      ResultSet.CONCUR_UPDATABLE);
      rs = statement.executeQuery(query);
    } 
    catch (SQLException ex) {
      ex.printStackTrace();
    }
            
    tableModel = new FlightTableModel(rs);
    mainFlightTable = new JTable(tableModel); 
    //Setting up the table view
    mainFlightTable.setAutoCreateRowSorter(true);
    mainFlightTable.setBackground(TABLE_COLOR);
    mainFlightTable.setForeground(Color.WHITE);
    mainFlightTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    mainFlightTable.setFont(HEADER_FONT);
    mainFlightTable.setRowSelectionAllowed(true);

    mainFlightTable.setRowHeight(30);
    mainPane.setPreferredSize(new Dimension(900, 650));

    mainPane.getViewport().add(mainFlightTable);
    mainFlightTable.setFillsViewportHeight(true);

    //Adding everything into  upper components
    listPanel.add(mainPane);
    buttonPanel.add(filterAndSearch);
    buttonPanel.add(bookmark);
    buttonPanel.add(resetBookmarks);

    this.add(listPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);
    this.setVisible(true);
  }

  //constructor for when there is bookmarked data
  /*Everything is the same apart from instead of getting the data from the 
  * database, takes the results from the filtered results.
  */
  public ListViewFrame(JTable filterResults) {
    super("List of Data");
    this.setBounds(100, 100, 1000, 1000);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setLayout(new BorderLayout());

    mainPane = new JScrollPane();
    listPanel = new JPanel();
    buttonPanel = new JPanel();

    bookmark = new JButton("Bookmark/Unbookmark selected row");
    bookmark.addActionListener(this);
    bookmark.setToolTipText("Click here to bookmark the selected row");
    
    resetBookmarks = new JButton("Reset Bookmarked");
    resetBookmarks.addActionListener(this);
    resetBookmarks.setToolTipText("CLick here to reset all bookmarks");
    
    filterAndSearch = new JButton("Search/Filter Data");
    filterAndSearch.addActionListener(this);
    filterAndSearch.setToolTipText("Click here to search for data in the table");
    
    this.mainFlightTable = filterResults;
  
    mainFlightTable.setAutoCreateRowSorter(true);
    mainFlightTable.setBackground(TABLE_COLOR);
    mainFlightTable.setForeground(Color.WHITE);
    mainFlightTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    mainFlightTable.setFont(HEADER_FONT);
    mainFlightTable.setRowSelectionAllowed(true);
    mainFlightTable.setRowHeight(30);
    mainPane.setPreferredSize(new Dimension(850, 600));
    
    mainPane.getViewport().add(mainFlightTable);
    mainFlightTable.setFillsViewportHeight(true);
    
    listPanel.add(mainPane);
    buttonPanel.add(filterAndSearch);
    buttonPanel.add(bookmark);
    buttonPanel.add(resetBookmarks);

    this.add(listPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    //opens the filter frame
    if (command.equals("Search/Filter Data")) {
      this.dispose();
      String[] headers = {"Flight Number", "Airline", "Landing Time", "Secondary City", "Background"};
      FilterFrame filter = new FilterFrame(mainFlightTable,headers);
    }
    //bookmarks or unbookmarks the row
    else if (command.equals("Bookmark/Unbookmark selected row")) {
      if(mainFlightTable.getValueAt(mainFlightTable.getSelectedRow(),5).equals(" ")) {
        mainFlightTable.setValueAt("***",mainFlightTable.getSelectedRow(), 5);
      }
      else {
        mainFlightTable.setValueAt(" ",mainFlightTable.getSelectedRow(), 5);
      }
    } 
    //resets all bookmarks
    else if (command.equals("Reset Bookmarked")) {
      for (int i = 0; i < mainFlightTable.getRowCount(); i++) {
        if (mainFlightTable.getValueAt(i, 5).equals("***")) {
          mainFlightTable.setValueAt(" ", i, 5);
        }

      }
    }
  }
}
