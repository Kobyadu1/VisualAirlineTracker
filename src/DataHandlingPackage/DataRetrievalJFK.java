package DataHandlingPackage;

import DatabasePackage.CodeConvertClass;
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

/**
  Class that retrieves data for the New York JFK airport
 
 */
public class DataRetrievalJFK extends DataRetrievalAbstract{
  //initialization of the needed websites
  private final String JFKARRIVAL6HOURS = "https://www.airport-jfk.com/arrivals.php?tp=0";
  private final String JFKARRIVAL12HOURS = "https://www.airport-jfk.com/arrivals.php?tp=6";
  private final String JFKARRIVAL18HOURS = "https://www.airport-jfk.com/arrivals.php?tp=12";
  private final String JFKARRIVAL24HOURS = "https://www.airport-jfk.com/arrivals.php?tp=18";
  
  private final String JFKDEPARTURE6HOURS = "https://www.airport-jfk.com/departures.php?tp=0";
  private final String JFKDEPARTURE12HOURS = "https://www.airport-jfk.com/departures.php?tp=6";
  private final String JFKDEPARTURE18HOURS = "https://www.airport-jfk.com/departures.php?tp=12";
  private final String JFKDEPARTURE24HOURS = "https://www.airport-jfk.com/departures.php?tp=18";
  private int FlightCount = 0;
  private String[]websites;
//call to the default constructor of the parent class
  public DataRetrievalJFK() {
    super();
  }
  //call to the constructor that preforms the update 
  public DataRetrievalJFK(String[] websites) {
    super(websites);  
    this.websites = websites;
  }
  
  /*This method reads the data from the web and returns it as a disprganized Arraylist 
  * of Arraylists of Strings
  */
  
  @Override
  public ArrayList<ArrayList<String>> readFromWeb(String website) {
    ArrayList<ArrayList<String>>returnArray = new ArrayList<>();
    try{
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
        if(line.equals("<div id=\"flight_detail\" class=\"even\">")||line.equals("<div id=\"flight_detail\" >")) {
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
      ErrorFrame frame = new ErrorFrame("This program requires internet access.");
    }
    return returnArray;
  }

  @Override
  public  ArrayList<ArrayList<String>> cleanData(ArrayList<ArrayList<String>> rawData) {
    //Intializations of needed data structures 
    int[]indexes = {2,5,8,11,12};
    ArrayList<ArrayList<String>> cleanedArray = new ArrayList<>();
    ArrayList<String>temp;
    CodeConvertClass codeHash = new CodeConvertClass();
    String time,status;
    //combs through raw data looking for set indexes in the arraylist of string to retrieve the data
    /*
    index 2 corresponds to the external airport code
    index 5 corresponds to the airline code
    index 5 corresponds to the flight number
    index 8 corresponds to the landing time
    Index 11 corresponds to the terminal 
    index 12 corresponds to the status
    */
    for(ArrayList<String>a:rawData) {
      temp = new ArrayList<>();
      for(int i:indexes) {
        String[] filterArray = a.get(i).split(">");
        if(filterArray.length>1) {
          switch(i) {
            case 2:              
              if(filterArray.length==4) {
                temp.add(filterArray[3].substring(3, 6));
              }
              else {
              temp.add(filterArray[4].substring(0,filterArray[4].length()-3));
              }              
              break;
            case 5:        
              temp.add(filterArray[2].substring(0,filterArray[2].length()-7).trim());
              temp.add(filterArray[2].substring(0,filterArray[2].length()-3).trim());
              break;
            case 8:
              time = filterArray[3].substring(0,filterArray[3].length()-3).trim();
              temp.add(String.valueOf(codeHash.landingTimeHash(time)));
              break;
            case 11:
              temp.add(""+filterArray[1].split("<")[0].charAt(filterArray[1].split("<")[0].length()-1));              
              break;
            case 12:
              status = filterArray[2].substring(0,filterArray[2].length()-7).trim();
              temp.add(String.valueOf(codeHash.statusHash(status)));
              //System.out.println(temp);
              break; 
            }
          }
    	}
      //At the end of cleaning the arraylist, it adds it back to the main datalist
      cleanedArray.add(temp);
    }
    return cleanedArray;
    }

  @Override
  public void addToDatabase(ArrayList<ArrayList<String>> cleanedData) {
    //Intialiazations of needed variables
    DatabaseConnect databaseObj = new DatabaseConnect("FlightDatabase");
    Connection  myDatabaseConnection;
    String flightNumber;
    String airlineCode;
    String landingTimeCode;
    String statusCode;
    String airportCode;
    String terminal;
    
    //checks to make sure the list has all the data, if not it gets skiped
    for(ArrayList<String>rowList:cleanedData) {
//      if(rowList.size()<5) {
//        continue;
//      }
      //sets up the values for the queries 
      airportCode = rowList.get(0);
      airlineCode = rowList.get(1);
      flightNumber = rowList.get(2);
      landingTimeCode = rowList.get(3);
      terminal = rowList.get(4);
      statusCode = rowList.get(5);
      
      myDatabaseConnection = databaseObj.getDatabaseConnection();
      String query = "INSERT INTO flightData VALUES"+
                     "(NEXT VALUE FOR flightIndex,?,?,?,?,?,?) ";

      //Inserts the data into the table
      try{
        PreparedStatement ps = myDatabaseConnection.prepareStatement(query);
        ps.setString(1,flightNumber);
        ps.setString(2,airlineCode);
        ps.setInt(3, Integer.parseInt(landingTimeCode));
        ps.setInt(4, Integer.parseInt(statusCode));
        ps.setString(5,terminal);
        ps.setString(6, airportCode);
        ps.executeUpdate();
        

      } 
      catch (SQLException ex) {
        System.out.println("Error inserting data");
        ex.printStackTrace();
      }
    }
  }

  //Getters
  public String getJFKARRIVAL6HOURS() {
    return JFKARRIVAL6HOURS;
  }
  public String getJFKARRIVAL12HOURS() {
    return JFKARRIVAL12HOURS;
  }
  public String getJFKARRIVAL18HOURS() {
    return JFKARRIVAL18HOURS;
  }
  public String getJFKARRIVAL24HOURS() {
    return JFKARRIVAL24HOURS;
  }
  public String getJFKDEPARTURE6HOURS() {
    return JFKDEPARTURE6HOURS;
  }
  public String getJFKDEPARTURE12HOURS() {
    return JFKDEPARTURE12HOURS;
  }
  public String getJFKDEPARTURE18HOURS() {
    return JFKDEPARTURE18HOURS;
  }
  public String getJFKDEPARTURE24HOURS() {
    return JFKDEPARTURE24HOURS;
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

