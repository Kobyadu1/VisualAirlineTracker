package HeadingCalculation;
/**
 * Calculates the bearing between Luanda and a passed in secondary city
 */ 
public class HeadingCalculationLAD extends HeadingCalculationAbstract{
  double bearing;
  //Default constructor
  public HeadingCalculationLAD() {
    super();
    bearing = 0;
  }
  //Constructor that runs the bearing calculation
  public HeadingCalculationLAD(String airportCode) {
    super(airportCode);
    bearing = calculateBearing();
  }
  public HeadingCalculationLAD(Double lat,Double lon) {
    super(lat,lon);
    bearing = calculateBearing();
  }

  @Override
  //Calculates the first input based on the equation for bearing
  public double calculateInputOne() {
    double lonDifference;
    double returnValue;
    lonDifference = getAirportCords()[1]-getLUANDACORDS()[1];
    
    returnValue = Math.cos(Math.toRadians(getAirportCords()[0])) * Math.sin(Math.toRadians(lonDifference));
    return returnValue;
  }

  @Override
  //Calculates the second input based on the equation for bearing
  public double calculateInputTwo() {
    double firstArguement;
    double secondArguement;
    double lonDifference;
    double returnValue;
    lonDifference = getAirportCords()[1]-getLUANDACORDS()[1];

    firstArguement = Math.cos(Math.toRadians(getLUANDACORDS()[0])) * Math.sin(Math.toRadians(getAirportCords()[0]));
    secondArguement = Math.sin(Math.toRadians(getLUANDACORDS()[0])) * Math.cos(Math.toRadians(getAirportCords()[0])) *  Math.cos(Math.toRadians(lonDifference));
    
    returnValue = firstArguement - secondArguement;

    return returnValue;
  }

  @Override
  //Calculates the bearing based on the equation for it
  public double calculateBearing() {
    double returnValue;
    double arguementOne = calculateInputOne();
    double arguementTwo = calculateInputTwo();
    
    returnValue = Math.atan2(arguementOne, arguementTwo);
    returnValue = Math.toDegrees(returnValue);
    if(returnValue<0){
      return returnValue+360;
    }
    else {
      return returnValue;
    }
  }
  @Override
  //getter
  public double getBearing() {
    return bearing;
  }
}

