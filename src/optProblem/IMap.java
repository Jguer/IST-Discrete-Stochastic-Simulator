package optProblem;

import java.util.List;

public interface IMap {

    public boolean nonValidMove(Point actPoint, int index, Point[] obstacles);

    public int getCost(Point actPoint, Point newPoint, SpecialZone[] specialZones);
}
