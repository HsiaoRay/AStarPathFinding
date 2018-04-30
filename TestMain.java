import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by chenqiang on 2018/4/30.
 */
public class TestMain extends Application {


    public static void main(String[] args) {

        launch(args);


    }

    static boolean isOk(double x) {
        return x >= 0 && x < 100;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Canvas canvas = new Canvas(1920, 1080);
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        GridNode[][] gragh = new GridNode[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                gragh[i][j] = new GridNode(i, j);
            }
        }

        for (int i = 20; i < 80; i++) {
            gragh[50][i].setWalkable(false);
        }
        int rate = 10;
        graphicsContext2D.setLineWidth(2);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                GridNode gridNode = gragh[i][j];

                int gridNodeX = (int) gridNode.getX();
                int gridNodeY = (int) gridNode.getY() - 1;

                if (isOk(gridNodeX) && isOk(gridNodeY)) {
                    GridNode tempNode = gragh[gridNodeX][gridNodeY];
                    if (tempNode != null) {
                        gridNode.getNeighbors().put(GridNode.BOTTOM, tempNode);
                        tempNode.getNeighbors().put(GridNode.UP, gridNode);
                    }
                }
                gridNodeX = (int) gridNode.getX() - 1;
                gridNodeY = (int) gridNode.getY() - 1;

                if (isOk(gridNodeX) && isOk(gridNodeY)) {
                    GridNode tempNode = gragh[gridNodeX][gridNodeY];
                    if (tempNode != null) {
                        gridNode.getNeighbors().put(GridNode.LEFT_BOTTOM, tempNode);
                        tempNode.getNeighbors().put(GridNode.RIGHT_UP, gridNode);
                    }
                }

                gridNodeX = (int) gridNode.getX() + 1;
                gridNodeY = (int) gridNode.getY() - 1;

                if (isOk(gridNodeX) && isOk(gridNodeY)) {
                    GridNode tempNode = gragh[gridNodeX][gridNodeY];
                    if (tempNode != null) {
                        gridNode.getNeighbors().put(GridNode.RIGHT_BOTTOM, tempNode);
                        tempNode.getNeighbors().put(GridNode.LEFT_UP, gridNode);
                    }
                }

                gridNodeX = (int) gridNode.getX() + 1;
                gridNodeY = (int) gridNode.getY();

                if (isOk(gridNodeX) && isOk(gridNodeY)) {
                    GridNode tempNode = gragh[gridNodeX][gridNodeY];
                    if (tempNode != null) {
                        gridNode.getNeighbors().put(GridNode.RIGHT, tempNode);
                        tempNode.getNeighbors().put(GridNode.LEFT, gridNode);
                    }
                }
                gridNodeX = (int) gridNode.getX() - 1;
                gridNodeY = (int) gridNode.getY();

                if (isOk(gridNodeX) && isOk(gridNodeY)) {
                    GridNode tempNode = gragh[gridNodeX][gridNodeY];
                    if (tempNode != null) {
                        gridNode.getNeighbors().put(GridNode.LEFT, tempNode);
                        tempNode.getNeighbors().put(GridNode.RIGHT, gridNode);
                    }
                }
                gridNodeX = (int) gridNode.getX();
                gridNodeY = (int) gridNode.getY() + 1;

                if (isOk(gridNodeX) && isOk(gridNodeY)) {
                    GridNode tempNode = gragh[gridNodeX][gridNodeY];
                    if (tempNode != null) {
                        gridNode.getNeighbors().put(GridNode.UP, tempNode);
                        tempNode.getNeighbors().put(GridNode.BOTTOM, gridNode);
                    }
                }
                gridNodeX = (int) gridNode.getX() - 1;
                gridNodeY = (int) gridNode.getY() + 1;

                if (isOk(gridNodeX) && isOk(gridNodeY)) {
                    GridNode tempNode = gragh[gridNodeX][gridNodeY];
                    if (tempNode != null) {
                        gridNode.getNeighbors().put(GridNode.LEFT_UP, tempNode);
                        tempNode.getNeighbors().put(GridNode.RIGHT_BOTTOM, gridNode);
                    }
                }
                gridNodeX = (int) gridNode.getX() + 1;
                gridNodeY = (int) gridNode.getY() + 1;

                if (isOk(gridNodeX) && isOk(gridNodeY)) {
                    GridNode tempNode = gragh[gridNodeX][gridNodeY];
                    if (tempNode != null) {
                        gridNode.getNeighbors().put(GridNode.RIGHT_UP, tempNode);
                        tempNode.getNeighbors().put(GridNode.LEFT_BOTTOM, gridNode);
                    }
                }

                if (gridNode.isWalkable()) {
                    graphicsContext2D.strokeLine(i * rate + 10, j * rate + 10, i * rate + 10, j * rate + 10);
                }
            }
        }


        GridAStarPathFinder gridAStarPathFinder = new GridAStarPathFinder();
        GridNode startNode = gragh[30][31];
        GridNode endNode = gragh[70][70];
        List<GridNode> gridNodes = gridAStarPathFinder.find(startNode, endNode, gragh);
//        graphicsContext2D.strokeLine(startNode.getX() * rate + 10, startNode.getY() * rate + 10,
//                endNode.getX() * rate + 10, endNode.getY() * rate + 10);
        for (int i = 0; i < gridNodes.size(); i++) {
            if (i == gridNodes.size() - 1)
                continue;
            graphicsContext2D.strokeLine(gridNodes.get(i).getX() * rate + 10, gridNodes.get(i).getY() * rate + 10,
                    gridNodes.get(i + 1).getX() * rate + 10, gridNodes.get(i + 1).getY() * rate + 10);
        }


        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        Runnable runnable = new Runnable() {
            public void run() {
                long now = System.currentTimeMillis();
            }
        };
    }
}
