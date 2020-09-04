package GUIPackage;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
/**
 * Class that shows the filter window that allows users to filter the data
 */
public class FilterFrame extends JFrame implements ActionListener{
  //GUI initilaizations 
  JTable flightTable;
  String[]headers;
  JTextField searchBar;
  JButton searchButton;
  JComboBox<String> searchMenu;
  String[] menuOptions = {"Flight Number", "Airline", "Landing Time", "Secondary City", "Status"};
  JPanel searchPanel;
  //constructor that takes in the table and the headers
  public FilterFrame(JTable flightTable,String[] headers) {
    super("Search and Filter");
    this.flightTable = flightTable;
    this.headers = headers;
    this.setBounds(100, 100, 550, 100);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setLayout(new BorderLayout());
    
    searchPanel = new JPanel();
    searchBar = new JTextField(20);
    
    searchButton = new JButton("Search");
    searchButton.addActionListener(this);
    
    searchMenu = new JComboBox<>(menuOptions);
    
    searchPanel.add(searchMenu);
    searchPanel.add(searchBar);
    searchPanel.add(searchButton);
    
    this.add(searchPanel,BorderLayout.CENTER);
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    //when search button pressed, search through the list until data is found and bookmark it
    if(command.equals("Search")) {
      String searchInput;
      String searchString;
      String selectedOption;
      int columnIndex = 0;
      
      searchInput = searchBar.getText().toLowerCase();
      selectedOption = (String) searchMenu.getSelectedItem();
      
      for(int i = 0;i<headers.length;i++) {
        if(headers[i].equals(selectedOption)) {
          columnIndex = i;
          break;
        }
      }
      
      for(int i = 0;i<flightTable.getRowHeight();i++) {
        searchString = (String) flightTable.getValueAt(i,columnIndex);
        searchString = searchString.toLowerCase();
        if(searchInput.contains(searchString)) {
          flightTable.setValueAt("***",i,5);
        }
      }
      this.dispose();
      ListViewFrame frame = new ListViewFrame(flightTable);
    }   
  }
}
