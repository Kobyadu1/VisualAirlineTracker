/*
  Class to take in hash and decode the landing time code and status code
  or to reverse hash them
*/
package DatabasePackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CodeConvertClass {
   Connection  myDatabaseConnection;
   DatabaseConnect databaseObj;
   
  public CodeConvertClass() {
    databaseObj = new DatabaseConnect("FlightDatabase");
    myDatabaseConnection = databaseObj.getDatabaseConnection();
  }
  
  public int landingTimeHash(String landingTime) {
    if(landingTime.trim().split(" ").length==2) {
      String query = "SELECT * FROM landingTimeData WHERE Time = "+
              "'"+landingTime.trim()+"'";
      
      try{
        Statement statement = myDatabaseConnection.createStatement();
        
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return rs.getInt(1);

      } 
      catch (SQLException ex) {
      }
    }
    else {
      String[]time;
      int hours;
      int mins;
      
      time = landingTime.trim().split(":");
      hours = Integer.parseInt(time[0])*60;
      mins = Integer.parseInt(time[1]);
      return (hours+mins);
    }  
     return 0;
  }
  
  public int statusHash(String status) {
    String query = "SELECT * FROM statusData WHERE Status = "+
              "'"+status.trim()+"'";
      
    try{
        Statement statement = myDatabaseConnection.createStatement();
        
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return rs.getInt(1);

      } 
    catch (SQLException ex) {
    }
    return -2;
  }
  
  public void preloadLandingTimeTable() {
    String time = "";
    for(int i = 0;i<=1439;i++) {
      String query = "INSERT INTO landingTimeData VALUES"+
                     "(NEXT VALUE FOR timeCode,?) ";
      
      if((i/60)==0) {
        time += 12;
      }
      else if(i>=780){
        time += (i/60)-12;
      }
      else if((i/60)<10) {
        time += (0+""+(i/60));
      }
      else {
        time += (i/60);
      }
      
      
      time += ":";
      
      
      if((i%60)<10) {
        time += (0+""+(i%60));
      }
      else {
        time += (i%60);
      }
      
      
      if(i<720) {
        time += " am";
      }
      else {
        time += " pm";
      }
      
      try {
        PreparedStatement ps = myDatabaseConnection.prepareStatement(query);
        ps.setString(1,time);
        ps.executeUpdate();
      } 
     
      catch (SQLException ex) {
        ex.printStackTrace();
      }
      time = "";
    }
      
  }
  
  public void preloadStatusTable() {
    String query = "INSERT INTO statusData VALUES"+
                   "(?,?) ";
    String currentString="";
    int statusCode=0;
    for(int i = -1;i<3;i++) {
      for(int j = 2;j<5;j++) {
        try {
          PreparedStatement ps = myDatabaseConnection.prepareStatement(query);
          if(i == -1) {
            switch (j) {
              case 3:
                statusCode = 3;
                currentString = "On - time";
                break;
              case 4:
                statusCode = 4;
                currentString = "Delayed";
                break;
              default:
                statusCode = -1;
                currentString = "Canceled";
                break;
            } 
          }
          else if(i == 0) {
            switch (j) {
              case 3:
                statusCode = 30;
                currentString = "Landed - On-time";
                break;
              case 4:
                statusCode = 40;
                currentString = "Landed - Delayed";
                break;
              default:
                statusCode = 0;
                currentString = "Landed";
                break;
            }   
          }
          
          else if(i == 1) {
            switch (j) {
              case 3:
                statusCode = 31;
                currentString = "En Route - On-time";
                break;
              case 4:
                statusCode = 41;
                currentString = "En Route - Delayed";
                break;
              default:
                statusCode = 1;
                currentString = "En Route";
                break;
            }   
          }
          
          else if(i == 2) {
            switch (j) {
              case 3:
                statusCode = 32;
                currentString = "Scheduled - On-time";
                break;
              case 4:
                statusCode = 42;
                currentString = "Scheduled - Delayed";
                break;
              default:
                statusCode = 2;
                currentString = "Scheduled";
                break;
            }   
          }
          
          ps.setInt(1,statusCode);
          ps.setString(2, currentString);
          ps.executeUpdate();
        } 
     
        catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    }
  }
}
