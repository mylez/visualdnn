import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.jet.math.tdouble.DoubleFunctions;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Util {

    /**
     *
     * @param arr
     * @return
     */
    public static int max(int[] arr) {
        int maxval = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > maxval) {
                maxval = arr[i];
            }
        }
        return maxval;
    }

    /**
     *
     * @param matrices
     * @return
     */
    public static int maxRowCount(DoubleMatrix2D[] matrices) {
        int maxval = matrices[0].rows();
        for (int i = 1; i < matrices.length; i++) {
            if (matrices[i].rows() > maxval) {
                maxval = matrices[i].rows();
            }
        }
        return maxval;
    }

    /**
     *
     * @param matrices
     * @return
     */
    public static double absMaxElement(DoubleMatrix2D[] matrices) {
        double networkMax = 0;
        for (DoubleMatrix2D matrice : matrices) {
            double layerMax = matrice.getMaxLocation()[0];
            double min = matrice.getMinLocation()[0];
            if (layerMax > Math.abs(networkMax)) {
                networkMax = layerMax;
            }
            if (Math.abs(min) > Math.abs(networkMax)) {
                networkMax = min;
            }
        }
        return networkMax;
    }
}
