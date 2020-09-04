package DataHandlingPackage;

import DatabasePackage.DatabaseConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Class that refreshes the data in the database and displays the updated data on the GUI
 */

public class DataRefresh{
  private String[] websites;
  //Default Constructor
  public DataRefresh() {
    websites = null;
  }
  //Contructor that preforms the refresh
  public DataRefresh(String currentCity,String arrivalsOrDepartures,int timeframe) {
    websites = new String[15];
    //if statments that determined which links to use to do the refresh base on user input
    if(currentCity.equalsIgnoreCase("Houston")) {
      if(arrivalsOrDepartures.equalsIgnoreCase("Arrivals")) {
        //Once the city and timeframe are narrowed down
        //then a switch is called to speficy it down to the timeframe
        //it adds the needed websites to the array of webistes, deletes the old data and then runs the update
        switch (timeframe) {
          case 6: {
            DataRetrievalIAH getURL = new DataRetrievalIAH();
            websites[0] = getURL.getIAHARRIVAL6HOURS();
            deleteTables();
            DataRetrievalIAH runUpdate = new DataRetrievalIAH(getWebsites());
            break;
          }
          case 12: {
            DataRetrievalIAH getURL = new DataRetrievalIAH();
            websites[0] = getURL.getIAHARRIVAL6HOURS();
            websites[1] = getURL.getIAHARRIVAL12HOURS();
            deleteTables();
            DataRetrievalIAH runUpdate = new DataRetrievalIAH(getWebsites());
            break;
          }
          case 24: {
            DataRetrievalIAH getURL = new DataRetrievalIAH();
            websites[0] = getURL.getIAHARRIVAL6HOURS();
            websites[1] = getURL.getIAHARRIVAL12HOURS();
            websites[2] = getURL.getIAHARRIVAL18HOURS();
            websites[3] = getURL.getIAHARRIVAL24HOURS();
            deleteTables();
            DataRetrievalIAH runUpdate = new DataRetrievalIAH(getWebsites());
            break;
          }
        }
      //This same process repeats for each city, matching every case that the use clicks on.
      }
      else {
        switch (timeframe) {
          case 6: {
            DataRetrievalIAH getURL = new DataRetrievalIAH();
            websites[0] = getURL.getIAHDEPARTURE6HOURS();
            deleteTables();
            DataRetrievalIAH runUpdate = new DataRetrievalIAH(getWebsites());
            break;
          }
          case 12: {
            DataRetrievalIAH getURL = new DataRetrievalIAH();
            websites[0] = getURL.getIAHDEPARTURE6HOURS();
            websites[1] = getURL.getIAHDEPARTURE12HOURS();
            deleteTables();
            DataRetrievalIAH runUpdate = new DataRetrievalIAH(getWebsites());
            break;
          }
          case 24: {
            DataRetrievalIAH getURL = new DataRetrievalIAH();
            websites[0] = getURL.getIAHDEPARTURE6HOURS();
            websites[1] = getURL.getIAHDEPARTURE12HOURS();
            websites[2] = getURL.getIAHDEPARTURE18HOURS();
            websites[3] = getURL.getIAHDEPARTURE24HOURS();
            deleteTables();
            DataRetrievalIAH runUpdate = new DataRetrievalIAH(getWebsites());
            break;
          }
        }
      }
    }
  
    else if(currentCity.equalsIgnoreCase("Chicago")) {
      if(arrivalsOrDepartures.equalsIgnoreCase("Arrivals")) {
        switch (timeframe) {
          case 6: {
            DataRetrievalORD getURL = new DataRetrievalORD();
            websites[0] = getURL.getORDARRIVAL6HOURS();
            deleteTables();
            DataRetrievalORD runUpdate = new DataRetrievalORD(getWebsites());
            break;
          }
          case 12: {
            DataRetrievalORD getURL = new DataRetrievalORD();
            websites[0] = getURL.getORDARRIVAL6HOURS();
            websites[1] = getURL.getORDARRIVAL12HOURS();
            deleteTables();
            DataRetrievalORD runUpdate = new DataRetrievalORD(getWebsites());
            break;
          }
          case 24: {
            DataRetrievalORD getURL = new DataRetrievalORD();
            websites[0] = getURL.getORDARRIVAL6HOURS();
            websites[1] = getURL.getORDARRIVAL12HOURS();
            websites[2] = getURL.getORDARRIVAL18HOURS();
            websites[3] = getURL.getORDARRIVAL24HOURS();
            deleteTables();
            DataRetrievalORD runUpdate = new DataRetrievalORD(getWebsites());
            break;
          }
        }
      }
      else {
        switch (timeframe) {
          case 6: {
            DataRetrievalORD getURL = new DataRetrievalORD();
            websites[0] = getURL.getORDDEPARTURE6HOURS();
            deleteTables();
            DataRetrievalORD runUpdate = new DataRetrievalORD(getWebsites());
            break;
          }
          case 12: {
            DataRetrievalORD getURL = new DataRetrievalORD();
            websites[0] = getURL.getORDDEPARTURE6HOURS();
            websites[1] = getURL.getORDDEPARTURE12HOURS();
            deleteTables();
            DataRetrievalORD runUpdate = new DataRetrievalORD(getWebsites());
            break;
          }
          case 24: {
            DataRetrievalORD getURL = new DataRetrievalORD();
            websites[0] = getURL.getORDDEPARTURE6HOURS();
            websites[1] = getURL.getORDDEPARTURE12HOURS();
            websites[2] = getURL.getORDDEPARTURE18HOURS();
            websites[3] = getURL.getORDDEPARTURE24HOURS();
            deleteTables();
            DataRetrievalORD runUpdate = new DataRetrievalORD(getWebsites());
            break;
          }
        }
      }
    }
    
    else if(currentCity.equalsIgnoreCase("New York")) {
      if(arrivalsOrDepartures.equalsIgnoreCase("Arrivals")) {
        switch (timeframe) {
          case 6: {
            DataRetrievalJFK getURL = new DataRetrievalJFK();
            websites[0] = getURL.getJFKARRIVAL6HOURS();
            deleteTables();
            DataRetrievalJFK runUpdate = new DataRetrievalJFK(getWebsites());
            break;
          }
          case 12: {
            DataRetrievalJFK getURL = new DataRetrievalJFK();
            websites[0] = getURL.getJFKARRIVAL6HOURS();
            websites[1] = getURL.getJFKARRIVAL12HOURS();
            deleteTables();
            DataRetrievalJFK runUpdate = new DataRetrievalJFK(getWebsites());
            break;
          }
          case 24: {
            DataRetrievalJFK getURL = new DataRetrievalJFK();
            websites[0] = getURL.getJFKARRIVAL6HOURS();
            websites[1] = getURL.getJFKARRIVAL12HOURS();
            websites[2] = getURL.getJFKARRIVAL18HOURS();
            websites[3] = getURL.getJFKARRIVAL24HOURS();
            deleteTables();
            DataRetrievalJFK runUpdate = new DataRetrievalJFK(getWebsites());
            break;
          }
        }
      }
      else {
        switch (timeframe) {
          case 6: {
            DataRetrievalJFK getURL = new DataRetrievalJFK();
            websites[0] = getURL.getJFKDEPARTURE6HOURS();
            deleteTables();
            DataRetrievalJFK runUpdate = new DataRetrievalJFK(getWebsites());
            break;
          }
          case 12: {
            DataRetrievalJFK getURL = new DataRetrievalJFK();
            websites[0] = getURL.getJFKDEPARTURE6HOURS();
            websites[1] = getURL.getJFKDEPARTURE12HOURS();
            deleteTables();
            DataRetrievalJFK runUpdate = new DataRetrievalJFK(getWebsites());
            break;
          }
          case 24: {
            DataRetrievalJFK getURL = new DataRetrievalJFK();
            websites[0] = getURL.getJFKDEPARTURE6HOURS();
            websites[1] = getURL.getJFKDEPARTURE12HOURS();
            websites[2] = getURL.getJFKDEPARTURE18HOURS();
            websites[3] = getURL.getJFKDEPARTURE24HOURS();
            deleteTables();
            DataRetrievalJFK runUpdate = new DataRetrievalJFK(getWebsites());
            break;
          }
        }
      }
    }
    
    else if(currentCity.equalsIgnoreCase("Luanda")) {
      if(arrivalsOrDepartures.equalsIgnoreCase("Arrivals")) {
        switch (timeframe) {
          case 6: {
            DataRetrievalLAD getURL = new DataRetrievalLAD();
            websites[0] = getURL.getLADARRIVAL6HOURS();
            deleteTables();
            DataRetrievalLAD runUpdate = new DataRetrievalLAD(getWebsites());
            break;
          }
          case 12: {
            DataRetrievalLAD getURL = new DataRetrievalLAD();
            websites[0] = getURL.getLADARRIVAL12HOURS();
            deleteTables();
            DataRetrievalLAD runUpdate = new DataRetrievalLAD(getWebsites());
            break;
          }
          case 24: {
            DataRetrievalLAD getURL = new DataRetrievalLAD();
            websites[0] = getURL.getLADARRIVAL24HOURS();
            deleteTables();
            DataRetrievalLAD runUpdate = new DataRetrievalLAD(getWebsites());
            break;
          }
        }
      }
      else {
        switch (timeframe) {
          case 6: {
            DataRetrievalLAD getURL = new DataRetrievalLAD();
            websites[0] = getURL.getLADDEPARTURE6HOURS();
            deleteTables();
            DataRetrievalLAD runUpdate = new DataRetrievalLAD(getWebsites());
            break;
          }
          case 12: {
            DataRetrievalLAD getURL = new DataRetrievalLAD();
            websites[0] = getURL.getLADDEPARTURE12HOURS();
            deleteTables();
            DataRetrievalLAD runUpdate = new DataRetrievalLAD(getWebsites());
            break;
          }
          case 24: {
            DataRetrievalLAD getURL = new DataRetrievalLAD();
            websites[0] = getURL.getLADDEPARTURE24HOURS();
            deleteTables();
            DataRetrievalLAD runUpdate = new DataRetrievalLAD(getWebsites());
            break;
          }
        }
      }
    }
    
    else if(currentCity.equalsIgnoreCase("Los Angeles")) {
      if(arrivalsOrDepartures.equalsIgnoreCase("Arrivals")) {
        switch (timeframe) {
          case 6: {
            DataRetrievalLAX getURL = new DataRetrievalLAX();
            websites[0] = getURL.getLAXARRIVAL6HOURS();
            deleteTables();
            DataRetrievalLAX runUpdate = new DataRetrievalLAX(getWebsites());
            break;
          }
          case 12: {
            DataRetrievalLAX getURL = new DataRetrievalLAX();
            websites[0] = getURL.getLAXARRIVAL6HOURS();
            websites[1] = getURL.getLAXARRIVAL12HOURS();
            deleteTables();
            DataRetrievalLAX runUpdate = new DataRetrievalLAX(getWebsites());
            break;
          }
          case 24: {
            DataRetrievalLAX getURL = new DataRetrievalLAX();
            websites[0] = getURL.getLAXARRIVAL6HOURS();
            websites[1] = getURL.getLAXARRIVAL12HOURS();
            websites[2] = getURL.getLAXARRIVAL18HOURS();
            websites[3] = getURL.getLAXARRIVAL24HOURS();
            deleteTables();
            DataRetrievalLAX runUpdate = new DataRetrievalLAX(getWebsites());
            break;
          }
        }
      }
      else {
        switch (timeframe) {
          case 6: {
            DataRetrievalLAX getURL = new DataRetrievalLAX();
            websites[0] = getURL.getLAXDEPARTURE6HOURS();
            deleteTables();
            DataRetrievalLAX runUpdate = new DataRetrievalLAX(getWebsites());
            break;
          }
          case 12: {
            DataRetrievalLAX getURL = new DataRetrievalLAX();
            websites[0] = getURL.getLAXDEPARTURE6HOURS();
            websites[1] = getURL.getLAXDEPARTURE12HOURS();
            deleteTables();
            DataRetrievalLAX runUpdate = new DataRetrievalLAX(getWebsites());
            break;
          }
          case 24: {
            DataRetrievalLAX getURL = new DataRetrievalLAX();
            websites[0] = getURL.getLAXDEPARTURE6HOURS();
            websites[1] = getURL.getLAXDEPARTURE12HOURS();
            websites[2] = getURL.getLAXDEPARTURE18HOURS();
            websites[3] = getURL.getLAXDEPARTURE24HOURS();
            deleteTables();
            DataRetrievalLAX runUpdate = new DataRetrievalLAX(getWebsites());
            break;
          }
        }
      }
    } 
  }
  //This method is to delete the old data from the tables.
  private void deleteTables() {
    //Intialization 
    DatabaseConnect databaseObj = new DatabaseConnect("FlightDatabase");
    Connection myDatabaseConnection;
    //The query staments are set up
    myDatabaseConnection = databaseObj.getDatabaseConnection();
    String query = "DELETE FROM flightData";
    //It executes the deletion through the statements.
    try{
      PreparedStatement ps = myDatabaseConnection.prepareStatement(query);
      ps.executeUpdate();
      } 
      catch (SQLException ex) {
          System.out.println("Error deleting data");
      }
  }
  //Prints the websites as a String when called to be printed 
  @Override
  public String toString() {
    String objectString="Websites: ";
    
    for(String w:getWebsites()) {
      objectString += " "+w;
    }
    return objectString;
  }
  //Getter 
  private String[] getWebsites() {
    return websites;
  } 
}

