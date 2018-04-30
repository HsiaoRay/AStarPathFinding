import java.util.*;

public class GridAStarPathFinder {

    protected long sessionId;

    protected PriorityQueue<GridNode> openList = new PriorityQueue<>((o1, o2) -> {
        double value = o1.getF() - o2.getF();
        if (value > 0)
            return 1;
        if (value < 0)
            return -1;
        return 0;
    });

    /**
     * @param startNode 开始点
     * @param endNode   结束点
     * @param gragh     可以根据坐标取到Node对象
     * @return 得到的路径结束点在0位置
     */
    public List<GridNode> find(GridNode startNode, GridNode endNode, GridNode[][] gragh) {

        if (startNode == endNode) {
            return null;
        }
        startNode.clear();

        endNode.clear();

        openList.clear();

        sessionId++;
        startNode.setG(0);
        startNode.setH(judgeCost(startNode, endNode));
        startNode.setF(startNode.getG() + startNode.getH());

        openList.add(startNode);


        GridNode pollNode;
        GridNode neighbor;
        int direction;
        while (!openList.isEmpty()) {
            pollNode = openList.poll();
            pollNode.setSessionId(sessionId);
            for (Map.Entry<Integer, GridNode> integerGridNodeEntry : pollNode.getNeighbors().entrySet()) {
                neighbor = integerGridNodeEntry.getValue();
                direction = integerGridNodeEntry.getKey();
                if (neighbor.getSessionId() == this.sessionId) {

                    double newG = pollNode.getG() + (direction < 5 ? 1d : Math.sqrt(2));

                    if (newG < neighbor.getG()) {
                        neighbor.setG(newG);
                        neighbor.setF(newG + neighbor.getH());
                        neighbor.setParent(pollNode);
                        openList.remove(neighbor);
                        openList.add(neighbor);
                    }
                    continue;
                }
                if (neighbor == endNode) {
                    endNode.setParent(pollNode);
                    break;
                }

                neighbor.setSessionId(sessionId);

                if (!neighbor.isWalkable()) {
                    continue;
                }

                neighbor.setParent(pollNode);
                neighbor.setG(pollNode.getG() + (direction < 5 ? 1d : Math.sqrt(2)));
                neighbor.setH(judgeCost(neighbor, endNode));
                neighbor.setF(neighbor.getG() + neighbor.getH());
                openList.offer(neighbor);
            }
        }
        return floyd(getResult(startNode, endNode), gragh);
    }

    protected List<GridNode> getResult(GridNode startNode, GridNode endNode) {
        //找到路径
        if (endNode.getParent() != null) {
            List<GridNode> nodeList = new ArrayList<>();
            while (endNode != null && endNode.getParent() != null) {
                nodeList.add(endNode);
                endNode = endNode.getParent();
            }
            nodeList.add(startNode);
            return nodeList;
        }
        return null;
    }

    /**
     * 路径平滑
     *
     * @param nodeList
     * @param gragh
     * @return
     */
    protected List<GridNode> floyd(List<GridNode> nodeList, GridNode[][] gragh) {
        if (nodeList == null)
            return null;
        int size = nodeList.size();
        if (size > 2) {

            GridNode gridNode1 = nodeList.get(size - 1);
            GridNode gridNode2 = nodeList.get(size - 2);
            GridNode gridNode3;
            GridNode gridNode4;

            //删除共线的点
            for (int i = size - 3; i >= 0; i--) {
                gridNode3 = nodeList.get(i + 1);
                gridNode4 = nodeList.get(i);

                if (gridNode1.getX() - gridNode2.getX() == gridNode3.getX() - gridNode4.getX() &&
                        gridNode1.getY() - gridNode2.getY() == gridNode3.getY() - gridNode4.getY()) {
                    nodeList.remove(i + 1);
                } else {
                    gridNode1 = gridNode3;
                    gridNode2 = gridNode4;
                }
            }
            //判断两点间是否有阻挡
            size = nodeList.size();
            for (int i = size - 1; i >= 0; i--) {
                gridNode1 = nodeList.get(i);
                for (int j = 0; j < i - 2; j++) {
                    gridNode2 = nodeList.get(j);
                    if (!hasBarrier(gridNode1.getX(), gridNode1.getY(), gridNode2.getX(), gridNode2.getY(), gragh)) {
                        for (int k = i - 1; k > j; k--) {
                            nodeList.remove(k);
                        }
                        i = j;
                        break;
                    }

                }

            }

        }
        return nodeList;
    }

    /**
     * 两点连线间是否有阻挡
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param gragh
     * @return
     */
    protected boolean hasBarrier(double startX, double startY, double endX, double endY, GridNode[][] gragh) {
        if (startX == endX && startY == endY) {
            return false;
        }
        //取到节点中心位置
        double center1X = startX + 0.5;
        double center1Y = startY + 0.5;
        double center2X = endX + 0.5;
        double center2Y = endY + 0.5;
        double distX = Math.abs(endX - startX);
        double distY = Math.abs(endY - startY);
        boolean loopDirection = distX > distY;

        int i;
        int loopStart;
        int loopEnd;

        List<GridNode> checkList;
        if (loopDirection) {
            loopStart = (int) Math.min(startX, endX);
            loopEnd = (int) Math.max(startX, endX);
            for (i = loopStart; i < loopEnd; i++) {
                double xPos = i + 1;
                double yPos = getY(center1X, center1Y, center2X, center2Y, xPos);
                checkList = getNodesUnderXY(xPos, yPos, gragh);
                for (GridNode gridNode : checkList) {
                    if (!gridNode.isWalkable())
                        return true;
                }
            }
        } else {
            loopStart = (int) Math.min(startY, endY);
            loopEnd = (int) Math.max(startY, endY);
            for (i = loopStart; i < loopEnd; i++) {
                double yPos = i + 1;
                double xPos = getX(center1X, center1Y, center2X, center2Y, yPos);
                checkList = getNodesUnderXY(xPos, yPos, gragh);
                for (GridNode gridNode : checkList) {
                    if (gridNode == null || !gridNode.isWalkable())
                        return true;
                }
            }

        }

        return false;
    }

    List<GridNode> tempGridNodeList = new ArrayList<>();

    /**
     * 给定 x,y 求 落到哪些格子里
     *
     * @param xPos
     * @param yPos
     * @param gragh
     * @return
     */
    protected List<GridNode> getNodesUnderXY(double xPos, double yPos, GridNode[][] gragh) {
        tempGridNodeList.clear();
        boolean xIsInt = xPos % 1 == 0;
        boolean yIsInt = yPos % 1 == 0;

        if (xIsInt && yIsInt) {
            tempGridNodeList.add(this.getNode(xPos - 1, yPos - 1, gragh));
            tempGridNodeList.add(this.getNode(xPos, yPos - 1, gragh));
            tempGridNodeList.add(this.getNode(xPos - 1, yPos, gragh));
            tempGridNodeList.add(this.getNode(xPos, yPos, gragh));
        }
        //点由2节点共享情况
        //点落在两节点左右临边上
        else if (xIsInt) {
            tempGridNodeList.add(this.getNode(xPos - 1, Math.floor(yPos), gragh));
            tempGridNodeList.add(this.getNode(xPos, Math.floor(yPos), gragh));
        }
        //点落在两节点上下临边上
        else if (yIsInt) {
            tempGridNodeList.add(this.getNode(Math.floor(xPos), yPos - 1, gragh));
            tempGridNodeList.add(this.getNode(Math.floor(xPos), yPos, gragh));
        }
        //点由一节点独享情况
        else {
            tempGridNodeList.add(this.getNode(Math.floor(xPos), Math.floor(yPos), gragh));
        }

        return tempGridNodeList;
    }

    protected GridNode getNode(double v, double v1, GridNode[][] gragh) {
        return gragh[(int) v][(int) v1];
    }

    /**
     * 两点连线，给定坐标x 求  y
     *
     * @param center1X
     * @param center1Y
     * @param center2X
     * @param xPos
     * @return
     */
    protected double getY(double center1X, double center1Y, double center2X, double center2Y, double xPos) {
//        xPos = (center1X + rate * center2X) / (1 + rate);

//        xPos * (1 + rate) = center1X + rate * center2X;

//        xPos + xPos * rate = center1X + rate * center2X;

//        xPos * rate - rate * center2X = center1X-xPos;
        // rate* (xPos-center2X) = center1X-xPos;

//        yPos = (center1Y + rate * center2Y) / (1 + rate);
        double rate = (center1X - xPos) / (xPos - center2X);//被除数 不能为0
        return (center1Y + center2Y * rate) / (1 + rate);
    }

    /**
     * 两点连线，给定坐标Y 求  x
     *
     * @param center1X
     * @param center1Y
     * @param center2X
     * @param yPos
     * @return
     */
    protected double getX(double center1X, double center1Y, double center2X, double center2Y, double yPos) {
        double rate = (center1Y - yPos) / (yPos - center2Y);//被除数 不能为0
        return (center1X + center2X * rate) / (1 + rate);
    }

    /**
     * 预计消耗
     *
     * @param from
     * @param to
     * @return
     */
    protected double judgeCost(GridNode from, GridNode to) {

        return Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2));

    }


}
