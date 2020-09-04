package DatabasePackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Class to read csv file with a list of cities and their latitude and longitude
 * Data sourced from https://simplemaps.com/data/world-cities
 */
public class readFromCSV {

  //default constructor
  public readFromCSV() {
    readAirlineData();
    readAirportData();
  }

  //Method to read airline data
  private void readAirlineData() {
    //Intializations 
    String fileName = "airlines.txt";
    BufferedReader reader;
    String line;
    DatabaseConnect databaseObj = new DatabaseConnect("FlightDatabase");
    Connection  myDatabaseConnection;
    myDatabaseConnection = databaseObj.getDatabaseConnection();
    String airlineCode;
    String airline;
    
    //Creates a reader similar to the way the URLs are read
    try {
      reader = new BufferedReader(new FileReader(fileName));     
      while( (line = reader.readLine()) != null) {
        
        
        //Seprerates the values by the commas
        String[] splitArray = line.split(",");
        
        //if the reader is on the first line(the headers) skip
        if(splitArray[0].equals("0")){
          continue;
        }
        //if the airport doesnt have a IATA number or the airport is closed skip it
        if(splitArray[3].length()<=2) {          
          continue;
        }
        
        airline = splitArray[1].split("\"")[1];
        airlineCode = splitArray[3].split("\"")[1];
        //sets up the query to be inserted into the database
        String query = "INSERT INTO airlineData VALUES"+
                       "(?,?) ";
        if(splitArray[splitArray.length-1].equals("\"Y\"")) {
          try{
           PreparedStatement ps = myDatabaseConnection.prepareStatement(query);
           ps.setString(1,airlineCode);
           ps.setString(2,airline);
           ps.executeUpdate();
         }
          catch (SQLException ex) {
          } 
        }
      }
    } 
    catch (FileNotFoundException e) {
    }
    catch (IOException e) {
    }
  }
  //Method to read and add airport data into the database
  private void readAirportData() {
    //Intializations 
    String fileName = "airportcodes.csv";
    BufferedReader reader;
    String line;
    DatabaseConnect databaseObj = new DatabaseConnect("FlightDatabase");
    Connection  myDatabaseConnection;
    myDatabaseConnection = databaseObj.getDatabaseConnection();
    String airportCode;
    String city;
    double lat;
    double lon;
    
    //Creates a reader similar to the way the URLs are read
    try {
      reader = new BufferedReader(new FileReader(fileName));     
      while( (line = reader.readLine()) != null) {
        
        
        //Seprerates the values by the commas
        String[] splitArray = line.split(",");
        
        //if the reader is on the first line(the headers) skip
        if(splitArray[2].equals("name")){
          continue;
        }
        //if the airport doesnt have a IATA number or the airport is closed skip it
        if(splitArray[splitArray.length-4].length()!=3||splitArray[1].equals("closed")) {          
          continue;
        }
        
        //Set the latatitude and longiude values with the city name. 
        airportCode = splitArray[splitArray.length-4].trim();
        lat = Double.parseDouble(splitArray[splitArray.length-1].split("\"")[0]);
        lon = Double.parseDouble(splitArray[splitArray.length-2].split("\"")[1]);
        city = splitArray[splitArray.length-6].trim();
        
        //sets up the query to be inserted into the database
        String query = "INSERT INTO airportData VALUES"+
                       "(?,?,?,?) ";
        try{
          PreparedStatement ps = myDatabaseConnection.prepareStatement(query);
          ps.setString(1,airportCode);
          ps.setDouble(2,lat);
          ps.setDouble(3,lon);
          ps.setString(4, city);
          ps.executeUpdate();
        }
        catch (SQLException ex) {
        } 
      }
    } 
    catch (FileNotFoundException e) {

    }
    
    catch (IOException e) {
    }
  }
}
