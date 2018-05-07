package parser;

import java.util.Arrays;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import optProblem.Map;
import optProblem.Point;
import optProblem.SpecialZone;

/**
 * @author group2
 * 
 *  OptProblemHandler extends the default hander for
 * use in the current problem using SAX.
 */
public class OptProblemHandler extends DefaultHandler {

    int maxpop;
    double finalinst;
    int initpop;
    int comfortsens;

    int dmean;
    int mmean;
    int rmean;

    Map map;

    Point initialPoint;
    Point finalPoint;

    int no;
    int nz;
    int obst;
    int zone;
    int cmax;

    boolean flagzone = false;

    Point[] obstacles;
    SpecialZone[] specialZones;
    Point sz_p1;
    Point sz_p2;
    int max_cost = -1;

    public int getMaxpop() {
        return maxpop;
    }

    public double getFinalinst() {
        return finalinst;
    }

    public int getInitpop() {
        return initpop;
    }

    public int getComfortsens() {
        return comfortsens;
    }

    public int getDmean() {
        return dmean;
    }

    public int getMmean() {
        return mmean;
    }

    public int getRmean() {
        return rmean;
    }

    public Map getMap() {
        return map;
    }

    public Point getInitialPoint() {
        return initialPoint;
    }

    public Point getFinalPoint() {
        return finalPoint;
    }

    public int getNo() {
        return no;
    }

    public int getObst() {
        return obst;
    }

    public int getCmax() {
        return cmax;
    }

    public Point[] getObstacles() {
        return obstacles;
    }

    public SpecialZone[] getSpecialZones() {
        return specialZones;
    }

    @Override
    public void startElement(String uri, String name, String tag, Attributes attributes)
            throws SAXException {

        if (tag.equalsIgnoreCase("simulation")) {

            finalinst = Double.parseDouble(attributes.getValue(0));
            initpop = Integer.parseInt(attributes.getValue(1));
            maxpop = Integer.parseInt(attributes.getValue(2));
            comfortsens = Integer.parseInt(attributes.getValue(3));

        } else if (tag.equalsIgnoreCase("grid")) {
            map =
                    new Map(
                            new Point(
                                    Integer.parseInt(attributes.getValue(0)),
                                    Integer.parseInt(attributes.getValue(1))));

        } else if (tag.equalsIgnoreCase("initialpoint")) {
            
        	int a = Integer.parseInt(attributes.getValue(0));
        	int b = Integer.parseInt(attributes.getValue(1));
        	
        	Point mapDim = map.getDimensions();
        	
        	if(a>mapDim.getX() || a<1) {
        		a=1; //default value for the initial point in case it is not valid
        		//System.out.println("Changing A INIT to "+a);
        	}
        	if(b>mapDim.getY() || b<1) {
        		b=1; //default value for the initial point in case it is not valid
        		//System.out.println("Changing B INIT to "+b);
        	}
        	initialPoint = new Point(a,b);

        } else if (tag.equalsIgnoreCase("finalpoint")) {
            
        	int a = Integer.parseInt(attributes.getValue(0));
        	int b = Integer.parseInt(attributes.getValue(1));
        	
        	Point mapDim = map.getDimensions();
        	
        	if(a>mapDim.getX() || a<1) {
           		a=mapDim.getX(); //default value for the final point in case it is not valid
        		//System.out.println("Changing A FINAL to "+a);
        	}
        	if(b>mapDim.getY() || b<1) {
        		b=mapDim.getY(); //default value for the final point in case it is not valid
        		//System.out.println("Changing B FINAL to "+b);
        	}
        	
        	finalPoint = new Point(a,b);

        } else if (tag.equalsIgnoreCase("specialcostzones")) {
        	nz = Integer.parseInt(attributes.getValue(0));
            specialZones = new SpecialZone[nz];
            zone = 0;

        } else if (tag.equalsIgnoreCase("zone")) {
            sz_p1 =
                    new Point(
                            Integer.parseInt(attributes.getValue(0)),
                            Integer.parseInt(attributes.getValue(1)));
            sz_p2 =
                    new Point(
                            Integer.parseInt(attributes.getValue(2)),
                            Integer.parseInt(attributes.getValue(3)));
            flagzone = true;

        } else if (tag.equalsIgnoreCase("obstacles")) {
            no = Integer.parseInt(attributes.getValue(0));
            obstacles = new Point[no];
            obst = 0;

        } else if (tag.equalsIgnoreCase("obstacle")) {
            if(obst<no) {
	        	obstacles[obst] =
	                    new Point(
	                            Integer.parseInt(attributes.getValue(0)),
	                            Integer.parseInt(attributes.getValue(1)));
	            obst++;
            }
            else {
            	System.out.println("Ignoring extra entry of an Obstacle!");
            }

        } else if (tag.equalsIgnoreCase("events")) {
            // do nothing
        } else if (tag.equalsIgnoreCase("death")) {
            dmean = Integer.parseInt(attributes.getValue(0));

        } else if (tag.equalsIgnoreCase("reproduction")) {
            rmean = Integer.parseInt(attributes.getValue(0));

        } else if (tag.equalsIgnoreCase("move")) {
            mmean = Integer.parseInt(attributes.getValue(0));
        } else {
            System.out.println("Start Element :" + tag);
        }
    }

    @Override
    public void endElement(String uri, String name, String tag) throws SAXException {
        if (tag.equalsIgnoreCase("obstacles")) {
            map.addObstacles(obstacles);
        }
        if (tag.equalsIgnoreCase("specialcostzones")) {
            map.addSpecialZones(specialZones);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (flagzone) {
            String s = new String(Arrays.copyOfRange(ch, start, start + length));
            int cost = Integer.parseInt(s);
            if (cost > max_cost) {
                map.addMaxCost(cost);
                max_cost = cost;
            }
            if(zone<nz) { //extra entries of zones are IGNORED
	            specialZones[zone] = (new SpecialZone(sz_p1, sz_p2, cost));
	            flagzone = false;
	            zone++;
            }else {
            	//System.out.println("Ignoring extra entry of a Special Cost Zone!");
            }
        }
    }
}
