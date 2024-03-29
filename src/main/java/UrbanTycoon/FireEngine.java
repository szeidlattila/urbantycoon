
package UrbanTycoon;
import java.awt.Image;
import java.util.ArrayList;

class FireEngine extends Sprite {
    private ArrayList<Road> route = new ArrayList<>();
    private final ArrayList<Road> routeBack = new ArrayList<>();
    private Road currentRoad = null;
    private Buildable destination = null;
    private boolean movingBack = false;
    private boolean available = true;

    public FireEngine(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }

    public boolean isAvailable() {
        return available;
    }
    
    public boolean isMovingBack() {
        return movingBack;
    }
    
    public Buildable getDestination() {
        return destination;
    }
    
    /**
     * if route and destination is not null set available to false, set route, set destination
     * otherwise return
     * @param route
     * @param destination 
     */
    public void setRouteAndDestination(ArrayList<Road> route, Buildable destination) {
        if (route == null || route.isEmpty() || destination == null)    return;
        this.available = false;
        this.route = route;
        this.destination = destination;
    }
    
    /**
     * move fire engine to next road
     * @return true if fire engine has arrived, otherwise false
     */
    public boolean moveNextRoad() {
        if (currentRoad != null)    currentRoad.setFireTrackOnTheRoad(false);
        this.available = false;
        if (!route.isEmpty()) {
            Road nextRoad = route.remove(0);
            routeBack.add(nextRoad);
            nextRoad.setFireTrackOnTheRoad(true);
            currentRoad = nextRoad;
            return false;
        }
        movingBack = true;
        return true;
    }
    
    /**
     * move fire engine to next road (back to fire station)
     */
    public void moveBackNextRoad() {
        if (currentRoad != null)    currentRoad.setFireTrackOnTheRoad(false);
        if (!routeBack.isEmpty()) {
            Road nextRoad = routeBack.remove(routeBack.size() - 1);
            nextRoad.setFireTrackOnTheRoad(true);
            currentRoad = nextRoad;
        } else {
            this.movingBack = false;
            this.available = true;
            this.destination = null;
        }
    }
}
