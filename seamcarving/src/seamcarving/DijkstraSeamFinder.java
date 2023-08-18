package seamcarving;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import graphs.shortestpaths.ShortestPath;
import graphs.shortestpaths.ShortestPathFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DijkstraSeamFinder implements SeamFinder {
    // Pixels are intergers, indexing the image row by row
    // index = nCols * row + col
    // y = index / nCols
    // x = index % nCols
    class ImageGraph implements Graph<Integer, Edge<Integer>> {
        // Negative values for the virtual verices
        public static final int IMAGE_TOP = -1;
        public static final int IMAGE_BOTTOM = -2;
        public static final int IMAGE_LEFT = -3;
        public static final int IMAGE_RIGHT = -4;
        private int nRows;
        private int nCols;
        private double[][] data;

        public int getYFromIndex(int index) {
            return index / nCols;
        }
        public int getXFromIndex(int index) {
            return index % nCols;
        }
        public int getIndexFromXY(int x, int y) {
            return y * nCols + x;
        }
        @Override
        public Collection<Edge<Integer>> outgoingEdgesFrom(Integer vertex) {
            // based ont the image return the collection of edges
            // first check the special vertices
            ArrayList<Edge<Integer>> result = new ArrayList<>();
            switch (vertex) {
                case IMAGE_TOP:
                    // return the top row as neighbors
                    for (int x = 0; x < nCols; x++) {
                        result.add(new Edge<>(IMAGE_TOP, getIndexFromXY(x, 0), data[x][0]));
                    }
                    break;
                case IMAGE_BOTTOM:
                    for (int x = 0; x < nCols; x++) {
                        result.add(new Edge<>(IMAGE_BOTTOM, getIndexFromXY(x, nRows - 1), data[x][nRows - 1]));
                    }
                    break;
                case IMAGE_LEFT:
                    for (int y = 0; y < nRows; y++) {
                        result.add(new Edge<>(IMAGE_LEFT, getIndexFromXY(0, y), data[0][y]));
                    }
                    break;
                case IMAGE_RIGHT:
                    for (int y = 0; y < nRows; y++) {
                        result.add(new Edge<>(IMAGE_RIGHT, getIndexFromXY(nCols - 1, y), data[nCols - 1][y]));
                    }
                    break;
                default:
                    updateImagePixelNeighbors(vertex, result);
            }
            return result;
        }

        private void updateImagePixelNeighbors(Integer vertex, ArrayList<Edge<Integer>> result) {
            // in the image pixels
            // check for the pixels on the border
            int curX = getXFromIndex(vertex);
            int curY = getYFromIndex(vertex);
            if (curX == 0) {
                // connect to the left side
                result.add(new Edge<>(vertex, IMAGE_LEFT, 0));
            } else {
                result.add(new Edge<>(vertex, getIndexFromXY(curX - 1, curY), data[curX - 1][curY]));
            }

            if (curX == nCols - 1) {
                // connect to the right side
                result.add(new Edge<>(vertex, IMAGE_RIGHT, 0));
            } else {
                result.add(new Edge<>(vertex, getIndexFromXY(curX + 1, curY), data[curX + 1][curY]));
            }

            if (curY == 0) {
                // connect to the top side
                result.add(new Edge<>(vertex, IMAGE_TOP, 0));
            } else {
                result.add(new Edge<>(vertex, getIndexFromXY(curX, curY - 1), data[curX][curY - 1]));
            }

            if (curY == nRows - 1) {
                // connect to the top side
                result.add(new Edge<>(vertex, IMAGE_BOTTOM, 0));
            } else {
                result.add(new Edge<>(vertex, getIndexFromXY(curX, curY + 1), data[curX][curY + 1]));
            }

            if (curX > 0 && curY > 0) {
                result.add(new Edge<>(vertex, getIndexFromXY(curX - 1, curY - 1), data[curX - 1][curY - 1]));
            }
            if (curX > 0 && curY < nRows - 1) {
                result.add(new Edge<>(vertex, getIndexFromXY(curX - 1, curY + 1), data[curX - 1][curY + 1]));
            }
            if (curX < nCols - 1 && curY > 0) {
                result.add(new Edge<>(vertex, getIndexFromXY(curX + 1, curY - 1), data[curX + 1][curY - 1]));
            }
            if (curX < nCols - 1 && curY < nRows - 1) {
                result.add(new Edge<>(vertex, getIndexFromXY(curX + 1, curY + 1), data[curX + 1][curY + 1]));
            }
        }

        public ImageGraph(double[][] image) {
             nRows = image[0].length;
             nCols = image.length;
             data = new double[nCols][nRows];
             for (int row = 0; row < nRows; row++) {
                 for (int col = 0; col < nCols; col++) {
                     data[col][row] = image[col][row];
                 }
             }
        }
    }
    private final ShortestPathFinder<Graph<Integer, Edge<Integer>>, Integer, Edge<Integer>> pathFinder;

    public DijkstraSeamFinder() {
        this.pathFinder = createPathFinder();
    }

    protected <G extends Graph<V, Edge<V>>, V> ShortestPathFinder<G, V, Edge<V>> createPathFinder() {
        /*
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
        */
        return new DijkstraShortestPathFinder<>();
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        ImageGraph graph = new ImageGraph(energies);
        ShortestPath<Integer, Edge<Integer>> sp = pathFinder.findShortestPath(graph,
            ImageGraph.IMAGE_LEFT, ImageGraph.IMAGE_RIGHT);
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer v: sp.vertices()) {
            if (v >= 0) {
                result.add(graph.getYFromIndex(v));
            }
        }
        return result;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        ImageGraph graph = new ImageGraph(energies);
        ShortestPath<Integer, Edge<Integer>> sp = pathFinder.findShortestPath(graph,
            ImageGraph.IMAGE_TOP, ImageGraph.IMAGE_BOTTOM);
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer v: sp.vertices()) {
            if (v >= 0) {
                result.add(graph.getXFromIndex(v));
            }
        }
        return result;
    }
}
