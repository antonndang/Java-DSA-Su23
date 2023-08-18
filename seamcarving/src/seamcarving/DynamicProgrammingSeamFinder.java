package seamcarving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 * @see SeamCarver
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {
    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        int nRows = energies[0].length;
        int nCols = energies.length;
        double[][] dp = energies; // the array to track the total costs


        // for each subsequent column evaluate the min possible sum that comes from the left
        for (int x = 1; x < nCols; x++) {
            // top and bottom rows are special
            // top row
            dp[x][0] = dp[x][0] + Math.min(dp[x - 1][0], dp[x - 1][1]);

            for (int y = 1; y < nRows - 1; y++) {
                dp[x][y] = dp[x][y] + Math.min(Math.min(dp[x - 1][y], dp[x - 1][y + 1]), dp[x - 1][y - 1]);
            }

            // bottom row
            dp[x][nRows - 1] = dp[x][nRows - 1] + Math.min(dp[x - 1][nRows - 1], dp[x - 1][nRows - 2]);
        }
        // now recover the path tracing from the minimal result on the right side
        ArrayList<Integer> result = new ArrayList<>();
        // find the min on the right
        int minY = 0;
        for (int y = 0; y < nRows; y++) {
            if (dp[nCols - 1][y] < dp[nCols - 1][minY]) {
                minY = y;
            }
        }
        result.add(minY);
        // now step back looking for the lowest value
        for (int x = nCols - 2; x >= 0; x--) {
            int newMinY = minY;
            for (int y = Math.max(0, minY - 1); y <= Math.min(nRows - 1, minY + 1); y++) {
                if (dp[x][y] < dp[x][newMinY]) {
                    newMinY = y;
                }
            }
            minY = newMinY;
            result.add(minY);
        }

        Collections.reverse(result);
        return result;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        int nRows = energies[0].length;
        int nCols = energies.length;
        double[][] dp = energies;

        // for each subsequent column evaluate the min possible sum that comes from the left
        for (int y = 1; y < nRows; y++) {
            // left and right cols are special
            // left col
            dp[0][y] = dp[0][y] + Math.min(dp[0][y - 1], dp[1][y - 1]);

            for (int x = 1; x < nCols - 1; x++) {
                dp[x][y] = dp[x][y] + Math.min(Math.min(dp[x][y - 1], dp[x - 1][y - 1]), dp[x + 1][y - 1]);
            }

            // right col
            dp[nCols - 1][y] = dp[nCols - 1][y] + Math.min(dp[nCols - 1][y - 1], dp[nCols - 2][y - 1]);
        }
        // now recover the path tracing from the minimal result on the right side
        ArrayList<Integer> result = new ArrayList<>();
        // find the min on the right
        int minX = 0;
        for (int x = 0; x < nCols; x++) {
            if (dp[x][nRows - 1] < dp[minX][nRows - 1]) {
                minX = x;
            }
        }
        result.add(minX);
        // now step back looking for the lowest value
        for (int y = nRows - 2; y >= 0; y--) {
            int newMinX = minX;
            for (int x = Math.max(0, minX - 1); x <= Math.min(nCols - 1, minX + 1); x++) {
                if (dp[x][y] < dp[newMinX][y]) {
                    newMinX = x;
                }
            }
            minX = newMinX;
            result.add(minX);
        }

        Collections.reverse(result);
        return result;

    }
}
