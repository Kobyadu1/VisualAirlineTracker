package DatabasePackage;
/**
 *Class to connect to the database
 *Learned in My teachers class
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnect {
    //Intiliaztions
    private String databaseName;
    private Connection databaseConnection;
    
    //Constructor if the databasename is known
    public DatabaseConnect(String databaseName) {
      try {
        this.databaseName = databaseName;
        setDatabaseConnection();
      } catch (ClassNotFoundException ex) {
        Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    //Constructor if the databasename is not known
    public DatabaseConnect() {
      this.databaseName = "";
      this.databaseConnection = null;
    }
    //Method to get the data from the table
    public ArrayList<ArrayList<String>> getData(String tableName,String[]headers) {
      //Intialiaztions 
      int columns = headers.length;
      ResultSet result = null;
      Statement s = null;
      String query = "SELECT * FROM "+tableName;
      ArrayList<ArrayList<String>>dataList = new ArrayList<>();
      
      try {
        s = this.databaseConnection.createStatement();
        result = s.executeQuery(query);
        //loops through the result set retreived from the database and adds it to an arrayList
        while(result.next()) {
          ArrayList<String> row = new ArrayList<>();
          for(int i = 0;i<columns;i++) {
            row.add(result.getString(headers[i]));
          }
          dataList.add(row);
        }  
      }
      catch (SQLException ex) {
        ex.printStackTrace();
        System.out.println("Unable to get data");
        System.exit(0);
      }
      return dataList;
    }
    //getters and setters
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }


    public Connection getDatabaseConnection() {
        return databaseConnection;
    }
    
    public void setDatabaseConnection() throws ClassNotFoundException {
        String connectionLink = "jdbc:derby:"+this.databaseName;
        this.databaseConnection = null;
        try{
          Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
          this.databaseConnection = DriverManager.getConnection(connectionLink);
        }
        catch(Exception e) {
          e.printStackTrace();
          System.out.println("SQL connect error");
          System.exit(0);
        }
    }
    //closes the database connection after use
    public void closeDatabaseConnect() {
        try {
          this.databaseConnection.close();
        }
        catch(SQLException e) {
          System.out.println("Unable to close connection");
          System.exit(0);
        }
    }
    //creates a database with the name
    public void createDatabase(String databaseName) {
     this.databaseName = databaseName;
     String connectionLink = "jdbc:derby:"+this.databaseName+";create=true";
     

     try {
       Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
       this.databaseConnection = DriverManager.getConnection(connectionLink);
       System.out.println("New database created");
       this.databaseConnection.close();
     }
     catch(ClassNotFoundException | SQLException e) {
       System.out.println("Error creating database: "+databaseName);
       e.printStackTrace();
       System.exit(0);
     }
     
    }
    //creates tables within the database with the table name and database name
    public void createTable(String newTable,String databaseName) throws ClassNotFoundException {
      Statement s;
      setDatabaseName(databaseName);
      setDatabaseConnection();
      try {
        s=this.databaseConnection.createStatement();
        s.execute(newTable);
        System.out.println("New table created");
        this.databaseConnection.close(); 
      } 
      catch (SQLException ex) {
       System.out.println("Error creaing table "+newTable);
       System.exit(0);
      }
    }
    
    public void createSequence(String sequence,String databaseName) throws ClassNotFoundException {
      Statement s;
      setDatabaseName(databaseName);
      setDatabaseConnection();
      try {
        s=this.databaseConnection.createStatement();
        s.execute(sequence);
        System.out.println("New sequence created");
        
        this.databaseConnection.close(); 
      } 
      catch (SQLException ex) {
       System.out.println("Error creating sequence "+sequence);
       System.exit(0);
      }
    }
    
  public void setAutoIncrement(int increment,String databaseName) {
      try {
        setDatabaseName(databaseName);
        setDatabaseConnection();
         
        Statement s = this.databaseConnection.createStatement();
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
            "'derby.language.sequence.preallocator', '"+increment+"')");
      } 
      
      catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
      }

  }

}

