package GUIPackage;

import DatabasePackage.CodeConvertClass;
import DatabasePackage.DatabaseConnect;
import HeadingCalculation.HeadingCalculationIAH;
import HeadingCalculation.HeadingCalculationJFK;
import HeadingCalculation.HeadingCalculationLAD;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPanel;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
/**
 * Helper class that runs the calculations for drawing the planes
 */
public class VisualHelper{
 // private JPanel airport;
  private String currentCity;
  private DatabaseConnect databaseConnection;
  private Connection  myDatabaseConnection;
  private ArrayList<ArrayList<String>>flightData;
  private DateTimeFormatter timeFormatter;
  private LocalTime timeNow;
  private String time;
  private int zoom = 8;
  private String query;
  private ResultSet rs;
  private final double[] HOUSTONCORDS = {29.9843, -95.3414};
  private final double[] CHICAGOCORDS = {41.9786, -87.9048};
  private final double[] LUANDACORDS = {-8.8583,13.2312};
  private final double[] LOSANGELESCORDS = {33.9425,-118.4079};
  private final double[] NEWYORKCORDS = {40.6398,-73.7789};
  private double[] currentCityCords;
  private final double SPEED = 13.3333;
  JMapViewer map;
  //Default constructor
  public VisualHelper() {
    this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    this.timeNow = LocalTime.now(); 
    this.time = timeFormatter.format(timeNow);
  }
  //Constructor that sets all the fields
  public VisualHelper(String currentCity,JMapViewer map) {
    if(currentCity.equalsIgnoreCase("houston")) {
      currentCityCords = HOUSTONCORDS;
    }
    else if(currentCity.equalsIgnoreCase("new york")) {
      currentCityCords = NEWYORKCORDS;
    }
    else if(currentCity.equalsIgnoreCase("los angeles")) {
      currentCityCords = LOSANGELESCORDS;
    }
    else if(currentCity.equalsIgnoreCase("chicago")) {
      currentCityCords = CHICAGOCORDS;
    }
    else if(currentCity.equalsIgnoreCase("luanda")) {
      currentCityCords = LUANDACORDS;
    }
    this.map = map;
    databaseConnection = new DatabaseConnect("FlightDatabase");
    this.currentCity = currentCity;
    this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    this.timeNow = LocalTime.now(); 
    this.time = timeFormatter.format(timeNow);
    flightData = new ArrayList<>();
    myDatabaseConnection = databaseConnection.getDatabaseConnection();
    
    query = "SELECT FlightNumber, LandingTimeCode, "
            + "HostCityName, Status, AirportCode,Latitude, Longitude "
            + "FROM FlightData "
            + "LEFT JOIN airportData "
            + "ON flightData.ExternalAirportCode = airportData.AirportCode "
            + "LEFT JOIN statusData "
            + "ON FlightData.StatusCode = statusData.StatusCode ";
    try{
      Statement statement = myDatabaseConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
      ResultSet.CONCUR_UPDATABLE);
      rs = statement.executeQuery(query);
      int rowCount;
      
      rs.last();
      rowCount = rs.getRow();
      rs.first();
      
      for(int i = 1; i <= rowCount;i++) {
        ArrayList<String> temp = new ArrayList<>();
        for(int j = 1;j<8;j++) {
          temp.add(rs.getString(j)); 
        }
        if(temp.get(3)==null) {
            temp.set(3, "N/A");
        }
        flightData.add(temp);
        rs.next();
      }
    } 
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    visualTrackerHelper();
  }
  //This method loops through the data pulled from the database to set the location for each plane
  private void visualTrackerHelper() {
    Coordinate primaryCity = new Coordinate(currentCityCords[0],currentCityCords[1]);
    MapMarkerDot primaryMarker = new MapMarkerDot(primaryCity);
    map.addMapMarker(primaryMarker);
    
    if(currentCity.equalsIgnoreCase("houston")) {
      HeadingCalculationIAH headingObject;
      for(ArrayList<String>data:flightData) {
        if(data.get(3).contains("Landed")) {
          continue;
        }
        headingObject = new HeadingCalculationIAH(Double.parseDouble(data.get(5)),Double.parseDouble(data.get(6)));
        
        Coordinate airplanelocation = calculateLength(data.get(1),
        calculateDistance(Double.parseDouble(data.get(5)),
        Double.parseDouble(data.get(6))),headingObject.calculateBearing());
        
        MapMarkerDot planeMarker = new MapMarkerDot(airplanelocation);
        planeMarker.setBackColor(Color.RED);
        planeMarker.setName(data.get(1)+" "+data.get(2));
        
                
        map.addMapMarker(planeMarker);
        map.setDisplayPosition(primaryCity,zoom);
      }
    }
    else if(currentCity.equalsIgnoreCase("new york")) {
      for(ArrayList<String>data:flightData) {
      }
    }
    else if(currentCity.equalsIgnoreCase("los angeles")) {
      for(ArrayList<String>data:flightData) {
      }
    }
    else if(currentCity.equalsIgnoreCase("chicago")) {
      for(ArrayList<String>data:flightData) {
      }
    }
    else if(currentCity.equalsIgnoreCase("luanda")) {
      for(ArrayList<String>data:flightData) {
      }
    }
  }
  public static void main(String[]sdf) {
    VisualHelper te = new VisualHelper("Houston",new JMapViewer());
  }
  //This method takes the cordinate values for the primary and secondary city to compute the distance
  private double calculateDistance(double lat,double lon) {
    //set difference between the lat and lon as well as the earth radius
    double latDifference = Math.toRadians(lat - currentCityCords[0]); 
    double lonDifference = Math.toRadians(lon - currentCityCords[1]); 
    double earthRadius = 6371; 
    //Calculates the first input based on the equation for distance
    double firstInput = Math.pow(Math.sin(latDifference/2),2) + Math.pow(Math.sin(lonDifference/2),2) * Math.cos(Math.toRadians(currentCityCords[0])) *  Math.cos(Math.toRadians(lat)); 
    //Calculates the second input based on the equation for distance
    double secondInput = 2 * Math.asin(Math.sqrt(firstInput)); 
    //Returns the distance
    return earthRadius * secondInput; 
  }

  //This method uses the distance and time left in the flight to compute its approximate postion on the map
  private Coordinate calculateLength(String time,double distance,double bearing) {
    //Intializations
    double[] destCords = {0.0,0.0};
    CodeConvertClass codeHash = new CodeConvertClass();
    double length; 
    int mintuesToLand;
    int currentTimeCode;
    int radius = 6371;
    currentTimeCode = codeHash.landingTimeHash(this.time);
    mintuesToLand = Integer.parseInt(time)-currentTimeCode;    
    length = distance-(mintuesToLand * SPEED);
    double angleDist = length/radius;
    destCords[0] = Math.toDegrees(Math.asin(Math.sin(Math.toRadians(currentCityCords[0]))*Math.cos(angleDist)
            +Math.cos(Math.toRadians(currentCityCords[0])) * Math.sin(angleDist) * Math.cos(Math.toRadians(bearing))));
    
    destCords[1] = Math.toDegrees(Math.toRadians(currentCityCords[1]) + Math.atan2(Math.sin(Math.toRadians(bearing))*Math.sin(angleDist)*Math.cos(Math.toRadians(currentCityCords[0])),
            Math.cos(angleDist)-Math.sin(Math.toRadians(currentCityCords[0]))*Math.sin(Math.toRadians(destCords[0]))));
    return new Coordinate(destCords[0],destCords[1]);
  }

  //Getters 
  public String getCurrentCity() {
    return currentCity;
  }

  public String getTime() {
    return time;
  }
  
  public double getZoom() {
    return zoom;
  }
    
  }

