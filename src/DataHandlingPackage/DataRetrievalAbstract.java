package DataHandlingPackage;

import java.util.ArrayList;

/**
 * Abstract class that serves as a blueprint for the other data retrieval classes
 */
public abstract class DataRetrievalAbstract{
  //Default constructor
  public DataRetrievalAbstract() {
    
  }
  //Constructor taking in the websites and retrieving the data and adding said data to the database.
  public DataRetrievalAbstract(String[] websites) {
    ArrayList<ArrayList<String>> data;
    //Takes in the array of the websites, making sure none are null
    for(String website:websites) {
      if(website==null) {
        break;
      }
      //Runs the method to clean the data and then adds it to the database.
      data = cleanData(readFromWeb(website));
      addToDatabase(data);
    }
  }

  //Method to read the data from the web
  public abstract ArrayList<ArrayList<String>> readFromWeb(String website); 
  
  //Method to clean the data recieved form the web
  public abstract ArrayList<ArrayList<String>> cleanData(ArrayList<ArrayList<String>> rawData);
  
  //Method to add that data into the database
  public abstract void addToDatabase(ArrayList<ArrayList<String>> cleanedData);
   
}

