package DatabasePackage;

import java.sql.Statement;

/**
 * Class to create the database and tables and to add needed data into the database
 */
public class DatabaseInstall{
  
  
  public static void main(String[]args) throws ClassNotFoundException{    
  //intialiaztions 
  String flightDataTable;
  String airportDataTable;
  String airlineTable;
  String landingTimeTable;
  String statusTable;
  String cityTable;
  String flightIndexSequence;
  String timeSequence;
  String property;
  DatabaseConnect databaseObject = new DatabaseConnect();
  
  //creation of database and tables
  databaseObject.createDatabase("FlightDatabase");
  
  flightDataTable = "CREATE TABLE flightData ( "+
                "FlightIndex int PRIMARY KEY, " +
                "FlightNumber VARCHAR(10), " +
                "AirlineCode VARCHAR(5), " +
                "LandingTimeCode int, " +
                "StatusCode int, "+
                "Terminal VARCHAR(2), " +
                "ExternalAirportCode VARCHAR(5) "+
             ")";
  
  airportDataTable = "CREATE TABLE airportData ( "+
                "AirportCode VARCHAR(5) PRIMARY KEY, " +
                "Latitude decimal(10,4), " +
                "Longitude decimal(10,4), " +
                "HostCityName VARCHAR(100) " +
             ")"; 
  
  airlineTable = "CREATE TABLE airlineData ( "+
                "AirlineCode VARCHAR(5) PRIMARY KEY," +
                "Airline VARCHAR(50) " +
             ")";  
  
  landingTimeTable = "CREATE TABLE landingTimeData ( "+
                "LandingTimeCode int PRIMARY KEY," +
                "Time VARCHAR(10) " +
             ")";
  
  statusTable = "CREATE TABLE statusData ( "+
                "StatusCode int PRIMARY KEY," +
                "Status VARCHAR(50) " +
             ")";
  
  flightIndexSequence = "CREATE SEQUENCE flightIndex "+
                        "AS INT " +
                        "START WITH 0 "+
                        "INCREMENT BY 1 "+
                        "NO MAXVALUE "+
                        "MINVALUE 0";
  
  timeSequence = "CREATE SEQUENCE timeCode "+
                        "AS INT " +
                        "START WITH 0 "+
                        "INCREMENT BY 1 "+
                        "MAXVALUE 1439 "+
                        "MINVALUE 0";
                        
                     
  //Sets up the program for use by creating the tables and loading geopositioning data into the database 
  databaseObject.createTable(flightDataTable, "FlightDatabase");
  databaseObject.createTable(airportDataTable, "FlightDatabase");
  databaseObject.createTable(airlineTable, "FlightDatabase");
  databaseObject.createTable(landingTimeTable, "FlightDatabase");
  databaseObject.createTable(statusTable, "FlightDatabase");
  databaseObject.createSequence(flightIndexSequence, "FlightDatabase");
  databaseObject.createSequence(timeSequence, "FlightDatabase");
  databaseObject.setAutoIncrement(1,"FlightDatabase");
  CodeConvertClass codeHash = new CodeConvertClass();
  codeHash.preloadLandingTimeTable();
  codeHash.preloadStatusTable();
  readFromCSV loadData = new readFromCSV();
  }
}
