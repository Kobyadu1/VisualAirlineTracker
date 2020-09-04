package HeadingCalculation;

import DatabasePackage.DatabaseConnect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class that serves as the blueprint for the other heading calculation classes 
 */
public abstract class HeadingCalculationAbstract {
  //Intialzations
  private String airportCode; 
  private double[] airportCords = new double[2];
  private final double[] HOUSTONCORDS = {29.9843, -95.3414};
  private final double[] CHICAGOCORDS = {41.9786, -87.9048};
  private final double[] LUANDACORDS = {-8.8583,13.2312};
  private final double[] LOSANGELESCORDS = {33.9425,-118.4079};
  private final double[] NEWYORKCORDS = {40.6398,-73.7789};
  
  //Default constructor
  public HeadingCalculationAbstract() {
    airportCords = null;
  }
  //onstructor that runs the calculations
  public HeadingCalculationAbstract(String airportCode) {
      this.airportCode = airportCode;
     
      //First connects to the database and sets up the queries
      DatabaseConnect databaseObj = new DatabaseConnect("FlightDatabase");
      
      Connection  myDatabaseConnection;
      myDatabaseConnection = databaseObj.getDatabaseConnection();
      
      String query = "SELECT * FROM airportData WHERE AirportCode = "+
              "'"+this.airportCode+"'";  
      //Executes the queries to get the geopositioning data for the provided secondry city
      //Goes through the result set. If there is no result, it flags it by setting the cordinates to -9999
      try{
        Statement statement = myDatabaseConnection.createStatement();
        
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        airportCords[0] = rs.getDouble("Latitude");
        airportCords[1] = rs.getDouble("Longitude");
      } 
      catch (SQLException ex) {
        ex.printStackTrace();
        airportCords[0] = -9999;
        airportCords[1] = -9999;
      }
  }
  public HeadingCalculationAbstract(Double lat,Double lon) {
    airportCords[0] = lat;
    airportCords[1] = lon;

  }
  //Calculate the first input for the bearing
  public abstract double calculateInputOne();
  
  //Calculate the second input for the bearing
  public abstract double calculateInputTwo();
  
  //Calculates the bearing
  public abstract double calculateBearing();
  
  //Getters
  public abstract double getBearing();

  public double[] getCHICAGOCORDS() {
    return CHICAGOCORDS;
  }

  public double[] getHOUSTONCORDS() {
    return HOUSTONCORDS;
  }

  public double[] getLOSANGELESCORDS() {
    return LOSANGELESCORDS;
  }

  public double[] getLUANDACORDS() {
    return LUANDACORDS;
  }

  public double[] getNEWYORKCORDS() {
    return NEWYORKCORDS;
  }

  public String getAirportCode() {
    return airportCode;
  }

  public double[] getAirportCords() {
    return airportCords;
  }
}
