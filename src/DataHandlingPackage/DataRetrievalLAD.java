package DataHandlingPackage;

import DatabasePackage.DatabaseConnect;
import GUIPackage.ErrorFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 *  Class that retrieves data for the Luanda LAD airport
 * 
 */
public class DataRetrievalLAD extends DataRetrievalAbstract{
  private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
  private LocalDate dateNow = LocalDate.now(); 
  private String date = dateFormatter.format(dateNow);
  private final String LADARRIVAL6HOURS = "https://www.airportia.com/angola/quatro-de-fevereiro-airport/arrivals/"+date+"/0000/0559";
  private final String LADARRIVAL12HOURS = "https://www.airportia.com/angola/quatro-de-fevereiro-airport/arrivals/"+date+"/0000/1159";
  private final String LADARRIVAL24HOURS = "https://www.airportia.com/angola/quatro-de-fevereiro-airport/arrivals/"+date+"/0000/2359";
  
  private final String LADDEPARTURE6HOURS = "https://www.airportia.com/angola/quatro-de-fevereiro-airport/departures/"+date+"/0000/0559";
  private final String LADDEPARTURE12HOURS = "https://www.airportia.com/angola/quatro-de-fevereiro-airport/departures/"+date+"/0000/1159";
  private final String LADDEPARTURE24HOURS = "https://www.airportia.com/angola/quatro-de-fevereiro-airport/departures/"+date+"/0000/2359";

  private String[]websites;
  private int FlightCount = 0;

  //Default Constructor
  public DataRetrievalLAD() {
    
  }
  //Constructor that runs the update
  public DataRetrievalLAD(String[]websites) {
    super(websites);
    this.websites = websites;
  }
    
  /*This method reads the data from the web and returns it as a disprganized Arraylist 
  * of Arraylists of Strings
  */
  @Override
  public ArrayList<ArrayList<String>> readFromWeb(String website) {
    ArrayList<ArrayList<String>>returnArray = new ArrayList<>();
    try {
    System.setProperty("http.agent", "Chrome");
    //It first casts the website to an URL and opens the stream of data to it
    URL url = new URL(website);
    InputStream inputStream =  url.openStream();
      //Then creates a reader for that stream
    try( BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      ArrayList<String>temp = new ArrayList<>();
        //if there exists a next line of data read it
        while ((line = reader.readLine()) != null) {
        //If that line is equal to the starting of data, it starts building a new ArrayList of string
          if(line.contains("<td class=\"flightsTable-number\">")) {
            returnArray.add(temp);
            temp = new ArrayList<>();
          }
         //if not it adds the data to the ArrayList 
         temp.add(line);
        }
      }
    }
    catch (MalformedURLException ex) {
    }
    catch (IOException e) {
      e.printStackTrace();
      ErrorFrame frame = new ErrorFrame("This program requires internet access.");
    }
    if(!returnArray.isEmpty()) {
      returnArray.remove(0);
    }
    return returnArray;

  }

  @Override
  public ArrayList<ArrayList<String>> cleanData(ArrayList<ArrayList<String>> rawData) {
    //Intializations of needed data structures 
    ArrayList<ArrayList<String>> cleanedArray = new ArrayList<>();
    ArrayList<String>temp;
    int[]indexes = {0,1,2,3,6};
    //combs through raw data looking for set indexes in the arraylist of string to retrieve the data
    /*
    index 0 corresponds to the secondary city
    index 1 corresponds to the airline
    index 2 corresponds to the flight number
    index 3 corresponds to the landing time
    index 6 correspond to the status
    */
   
    for(ArrayList<String>a:rawData) {
      temp = new ArrayList<>();
      for(int i:indexes) {
        String[] filterArray = a.get(i).split(">");
        if(filterArray.length>0) {
          switch(i) {
            case 0:
              temp.add(filterArray[2].substring(0,filterArray[2].length()-3));
              break;
            case 1:
              temp.add(filterArray[3].substring(0, filterArray[3].length()-6).trim());
               break;
            case 2:
              temp.add(filterArray[2].substring(0,filterArray[2].length()-3).trim());
              break;
            case 3:
              temp.add(filterArray[1].substring(0,filterArray[1].length()-4).trim());
              break;
            case 6:
              temp.add(filterArray[1].substring(0,filterArray[1].length()-5).trim());
              break;  
          }
    	}
      }
      cleanedArray.add(temp);
    }
    //at the end returns the cleaned array for database insertion
    return cleanedArray;
  }

  @Override
  public void addToDatabase(ArrayList<ArrayList<String>> cleanedData) {
    //Intialiazations of needed variables
    DatabaseConnect databaseObj = new DatabaseConnect("FlightDatabase");
    Connection  myDatabaseConnection;
    String flightNumber;
    String airline;
    String landingTime;
    String city;
    String status;
    
    //checks to make sure the list has all the data, if not it gets skiped
    for(ArrayList<String>rowList:cleanedData) {
      if(rowList.size()<5) {
        continue;
      }
      //sets up the values for the queries 
      flightNumber = rowList.get(0);
      city = rowList.get(1);
      airline = rowList.get(2);
      landingTime = rowList.get(3);
      status = rowList.get(4);
      FlightCount++;
      
      //If statement to set the time to invalid if the flight was cancelled
      if(status.equalsIgnoreCase("Cancelled")) {
        landingTime = "N/A";
      }
      
      myDatabaseConnection = databaseObj.getDatabaseConnection();
      String query = "INSERT INTO firstFlightTable VALUES"+
                     "(?,?,?,?,?) ";
      String query2 = "INSERT INTO secondFlightTable VALUES"+
                     "(?,?) ";
      //Inserts the data into both tables
      try{
        PreparedStatement ps = myDatabaseConnection.prepareStatement(query);
        ps.setString(1,flightNumber);
        ps.setString(2,airline);
        ps.setString(3, landingTime);
        ps.setString(4,city);
        ps.setInt(5, FlightCount);
        ps.executeUpdate();
        
        PreparedStatement ps2 = myDatabaseConnection.prepareStatement(query2);
        ps2.setInt(1, FlightCount);
        ps2.setString(2,status);
        ps2.executeUpdate();
      } 
      catch (SQLException ex) {
        //System.out.println("Error inserting data");
      }
    }
  }

  //Getters
  public DateTimeFormatter getDateFormatter() {
    return dateFormatter;
  }

  public LocalDate getDateNow() {
    return dateNow;
  }

  public String getDate() {
    return date;
  }

  public String getLADARRIVAL6HOURS() {
    return LADARRIVAL6HOURS;
  }

  public String getLADARRIVAL12HOURS() {
    return LADARRIVAL12HOURS;
  }

  public String getLADARRIVAL24HOURS() {
    return LADARRIVAL24HOURS;
  }

  public String getLADDEPARTURE6HOURS() {
    return LADDEPARTURE6HOURS;
  }

  public String getLADDEPARTURE12HOURS() {
    return LADDEPARTURE12HOURS;
  }

  public String getLADDEPARTURE24HOURS() {
    return LADDEPARTURE24HOURS;
  }
  @Override
  public String toString() {
    String objectString="Websites: ";
    
    for(String w:websites) {
      objectString += " "+w;
    }
    return objectString;
  }
  
}
