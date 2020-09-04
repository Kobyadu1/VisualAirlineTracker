package HeadingCalculation;
/**
 * Calculates the bearing between Houston and a passed in secondary city
 */ 

public class HeadingCalculationIAH extends HeadingCalculationAbstract{
  double bearing;
  //Default constructor
  public HeadingCalculationIAH() {
    super();
    bearing = 0;
  }
  //Constructor that runs the bearing calculation
  public HeadingCalculationIAH(String airportCode) {
    super(airportCode);
    bearing = calculateBearing();
  }
  
  public HeadingCalculationIAH(Double lat,Double lon) {
    super(lat,lon);
    bearing = calculateBearing();
  }

  @Override
  //Calculates the first input based on the equation for bearing
  public double calculateInputOne() {
    double lonDifference;
    double returnValue;
    lonDifference = getAirportCords()[1]-getHOUSTONCORDS()[1];
    
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
    lonDifference = getAirportCords()[1]-getHOUSTONCORDS()[1];

    firstArguement = Math.cos(Math.toRadians(getHOUSTONCORDS()[0])) * Math.sin(Math.toRadians(getAirportCords()[0]));
    secondArguement = Math.sin(Math.toRadians(getHOUSTONCORDS()[0])) * Math.cos(Math.toRadians(getAirportCords()[0])) *  Math.cos(Math.toRadians(lonDifference));
    
    returnValue = firstArguement - secondArguement;

    return returnValue;
  }

  @Override
  //Calculates the bearing based on the eqation for it.
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

  //Getter
  @Override
  public double getBearing() {
    return bearing;
  }
}
