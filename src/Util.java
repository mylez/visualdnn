import cern.colt.matrix.tdouble.DoubleMatrix2D;

public class Util {


    /**
     * Find the maximum matrix element in an array of matrices
     *
     * @param matrices
     * @return
     */
    public static double absMaxElement(DoubleMatrix2D[] matrices) {
        double networkMax = 0;
        for (DoubleMatrix2D mat: matrices) {
            double layerMax = mat.getMaxLocation()[0];
            double min = mat.getMinLocation()[0];
            if (layerMax > Math.abs(networkMax)) {
                networkMax = layerMax;
            }
            if (Math.abs(min) > Math.abs(networkMax)) {
                networkMax = min;
            }
        }
        return networkMax;
    }


    /**
     * format a number rounded to three decimal places with a
     * placeholder space character for non negative numbers
     *
     * @param num
     * @return
     */
    public static String roundFormat(double num) {
        return String.format("%s%.3f",  num < 0 ? "" : " ", num);
    }
}
