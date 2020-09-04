
package GUIPackage;

//License: GPL. Copyright 2008 by Jan Peter Stotz

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

/**
 *
 * Demonstrates the usage of {@link JMapViewer}
 *
 * @author Jan Peter Stotz
 *
 */
public class testMap extends JFrame {

    private static final long serialVersionUID = 1L;

    public testMap() {
        super("JMapViewer Demo");
        this.setBounds(100, 100, 1050, 900);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JMapViewer map = new JMapViewer();
        //map.setDisplayToFitMapMarkers();
        //map.setDisplayPosition(new Coordinate(13,-8.64), 8);
        map.addMapMarker(new MapMarkerDot(49.8588, 8.643));
        Coordinate one = new Coordinate(49.85,8.64);
        Coordinate two = new Coordinate(51,0);
        ArrayList<Coordinate> route = new ArrayList<Coordinate>(Arrays.asList(one, two, two));
        map.addMapPolygon(new MapPolygonImpl(route));
        
        this.add(map);
        this.setVisible(true);
        
    }
    public static void main(String[] args) {
         //java.util.Properties systemProperties = System.getProperties();
         //systemProperties.setProperty("http.proxyHost", "localhost");
         //systemProperties.setProperty("http.proxyPort", "8008");
         testMap test = new testMap();
    }

}
