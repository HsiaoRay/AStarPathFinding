import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenqiang on 2018/4/27.
 */
public class GridNode {


    public static final int UP = 1;//上方
    public static final int BOTTOM = 2;//右
    public static final int LEFT = 3;//左
    public static final int RIGHT = 4;//右

    public static final int LEFT_UP = 5;//左上
    public static final int RIGHT_UP = 6;//右上
    public static final int LEFT_BOTTOM = 7;//右
    public static final int RIGHT_BOTTOM = 8;//右


    private double x;

    private double y;

    private Map<Integer, GridNode> neighbors = new HashMap<>();

    private boolean isWalkable = true;


    private double f;

    private double g;

    private double h;

    private long sessionId;

    private GridNode parent;


    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getG() {
        return g;
    }

    public GridNode getParent() {
        return parent;
    }

    public void setParent(GridNode parent) {
        this.parent = parent;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public GridNode(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Map<Integer, GridNode> getNeighbors() {
        return neighbors;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public void clear() {
        g = h = f = 0;
        parent = null;
    }
    

    public void setWalkable(boolean walkable) {
        isWalkable = walkable;
    }
}
