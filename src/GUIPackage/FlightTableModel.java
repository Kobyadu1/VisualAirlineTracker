package GUIPackage;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * Class that is the background model of my table
 */
public class FlightTableModel extends AbstractTableModel{
  private final int FLIGHTNUMBERCOLUMN = 0;
  private final int AIRLINECOLUMN = 1;
  private final int LANDINGTIMECOLUMN = 2;
  private final int SECONDARYCITYCOLUMN = 3;
  private final int FLIGHTSTATUSCOLUMN = 4;
  private final int BOOKMARKCOLUMN = 5;
  
  private final String[] headers = {"Flight Number", "Airline", "Landing Time", "Secondary City", "Terminal","Status", "Bookmarks"};
  private ArrayList<ArrayList<String>>dataList = new ArrayList<>();
  
  //default constructor
  public FlightTableModel() {
    
  }
  //constructor that creates the table
  public FlightTableModel(ResultSet rs) {
    try {
      int rowCount;
      
      rs.last();
      rowCount = rs.getRow();
      rs.first();
      
      for(int i = 1; i <= rowCount;i++) {
        ArrayList<String> temp = new ArrayList<>();
        for(int j = 1;j<7;j++) {
          temp.add(rs.getString(j));
          if(temp.get(temp.size()-1)==null) {
            temp.set(temp.size()-1, "N/A");
          }
        }
        temp.add(" ");
        dataList.add(temp);
        rs.next();
      }
    } 
    catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
  
  //gets the amount of rows
  @Override
  public int getRowCount() {
    return dataList.size();
  }
  
  //gets the amount of columns
  @Override
  public int getColumnCount() {
    return headers.length;
  }

  //gets the value at given indexes
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if(rowIndex>=dataList.size()||columnIndex>=dataList.get(0).size()) {
      return " ";
    }
    else {
      return dataList.get(rowIndex).get(columnIndex);
    }
  } 
  
  //sets value at given indexes 
  @Override
  public void setValueAt(Object value,int rowIndex, int columnIndex) {
    if(rowIndex>dataList.size()||columnIndex>dataList.get(0).size()) {
      return;
    }
    else {
       dataList.get(rowIndex).set(columnIndex,(String) value);
       fireTableCellUpdated(rowIndex,columnIndex);
       
    }
  }
  
  //gets the column name of a certain column
  @Override
  public String getColumnName(int columnIndex) {
    return headers[columnIndex];
  }
  
  //sets the data of a single column
  public void setColumnData(int columnIndex,int dataColumnIndex,ArrayList<ArrayList<String>>data) {
   for(int i = 0;i<data.size();i++) {
     this.setValueAt(data.get(i).get(dataColumnIndex),i,columnIndex);    
   }
  }
  //sets the data of a single column
  public void setColumnData(int columnIndex,ArrayList<String>data) {
    for(int i = 0;i<data.size();i++) {
      this.setValueAt(data.get(i),i,columnIndex);
    }
  }
  //Getters
  public int getFLIGHTNUMBERCOLUMN() {
    return FLIGHTNUMBERCOLUMN;
  }

  public int getAIRLINECOLUMN() {
    return AIRLINECOLUMN;
  }

  public int getLANDINGTIMECOLUMN() {
    return LANDINGTIMECOLUMN;
  }

  public int getSECONDARYCITYCOLUMN() {
    return SECONDARYCITYCOLUMN;
  }

  public int getFLIGHTSTATUSCOLUMN() {
    return FLIGHTSTATUSCOLUMN;
  }

  public int getBOOKMARKCOLUMN() {
    return BOOKMARKCOLUMN;
  }

  public String[] getHeaders() {
    return headers;
  }

  public ArrayList<ArrayList<String>> getDataList() {
    return dataList;
  }
  
  @Override
  public String toString() {
    String objectString="Websites: ";
    for(ArrayList<String>a:dataList) {
      objectString += " "+a.toString();
    }
    return objectString; 
  }
}
