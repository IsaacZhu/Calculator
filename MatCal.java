import javax.lang.model.util.ElementScanner6;

/**
 * Matrix Calculator
 * by ljq March 21th
 */

 /**
  * MatCal
  */    
 public class MatCal {
    private double[][] MatrixA;
    private double[][] MatrixB;
    private int Arow;
    private int Acol;
    private int Brow;
    private int Bcol;
    private boolean Aselected;
    private boolean Bselected;
    private String MatTextA;
    private String MatTextB;
    private String Opcode;

    public MatCal(String row1, String col1, String text1, String row2, String col2, String text2, String op, boolean sel1, boolean sel2) {
        Aselected = sel1;
        Bselected = sel2;
        MatTextA = text1;
        MatTextB = text2;
        if (Aselected) {
            Acol = Integer.parseInt(col1);
            Arow = Integer.parseInt(row1);
        }
        else {
            Acol = 0;
            Arow = 0;
            MatrixA = null;
        }
        if (Bselected) {
            Bcol = Integer.parseInt(col2);
            Brow = Integer.parseInt(row2);
        }
        else {
            Bcol = 0;
            Brow = 0;
            MatrixB = null;
        }
        Opcode = op;
    }

    private double[][] parseMatrix(String text, int row, int col) {  //由输入的字符串构造矩阵
        double[][] Matrix = new double[row][col];
        String content;
        if (text.substring(0, 1).equals("[") && text.substring(text.length() - 1, text.length()).equals("]")) { //[]用来标识整个矩阵
            content = text.substring(1, text.length() - 1);
        }
        else
            return null;
        String[] rows = content.split(";");     //行与行之间用;分隔
        if (rows.length != row) {
            return null;
        }
        for (int i = 0; i < row; i++) {
            String[] elements = rows[i].split(",");     //行内元素用,分隔
            if(elements.length != col) {
                return null;
            }
            for (int j = 0; j < col; j++) {
                try {
                    Matrix[i][j] = Double.parseDouble(elements[j]);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return Matrix;
    }

    private double[][] MatAdd(double[][] A, int rA, int cA, double[][] B, int rB, int cB) {//计算矩阵加法
        double[][] C = new double[rA][cA];
        for (int i = 0; i < rA; i++) {
            for (int j = 0; j < cA; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    private double[][] MatMinus(double[][] A, int rA, int cA, double[][] B, int rB, int cB) {//计算矩阵减法
        double[][] C = new double[rA][cA];
        for (int i = 0; i < rA; i++) {
            for (int j = 0; j < cA; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

    private double[][] MatMul(double[][] A, int rA, int cA, double[][] B, int rB, int cB) {//计算矩阵乘法
        double[][] C = new double[rA][cB];
        for (int i = 0; i < rA; i++) {
            for (int j = 0; j < cB; j++) 
                C[i][j] = 0;
        }
        for (int i = 0; i < rA; i ++) {
            for (int j = 0; j < cB; j++) {
                for (int k = 0; k < cA; k ++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    private double[][] MatDiv(double[][] A, int rA, int cA, double[][] B, int rB, int cB) {//计算矩阵除法
        double[][] C = new double[rB][cA];
        C = MatMul(MatInverse(B, rB), rB, cB, A, rA, cA);       //C = B^(-1) * A即为A / B 结果
        return C;
    }

    private double[][] MatInverse(double[][] Mat, int N) {//计算矩阵的逆， A^(-1) = A* / |A|
        double[][] Inv = new double[N][N];
        double det = MatDet(Mat, N);
        System.out.println(MatStr(Mat, N, N));
        for (int i = 0; i < N; i ++) {
            for (int j = 0; j < N; j ++) {
                double[][] AlgMat = new double[N - 1][N - 1]; //代数余子式
                for (int k = 0; k < N; k ++) {
                    if (k == i) {
                        continue;
                    }
                    for (int l = 0; l < N; l ++) {
                        if (l == j) {
                            continue;
                        }
                        if (k < i && l < j) {
                            AlgMat[k][l] = Mat[k][l];
                        }
                        else if (k < i && l > j) {
                            AlgMat[k][l - 1] = Mat[k][l];
                        }
                        else if (k > i && l < j) {
                            AlgMat[k - 1][l] = Mat[k][l];
                        }
                        else {
                            AlgMat[k -1][l - 1] = Mat[k][l];
                        }
                    }
                }
                if ((i + j) % 2 == 0) {
                    Inv[i][j] = MatDet(AlgMat, N - 1) / det;
                }
                else {
                    Inv[i][j] = - MatDet(AlgMat, N - 1) / det;
                }
            }
        }
        return Inv;
    }

    private double MatDet(double[][] Mat, int N) {//计算矩阵行列式的值
        double det = 0;
        double[][] CMat = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                CMat[i][j] = Mat[i][j];
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j ++) {
                double factor = CMat[j][i] / CMat[i][i];
                for (int k = i; k < N; k ++) {
                    CMat[j][k] -= CMat[i][k] * factor;
                }
            }
        }
        det = 1;
        for (int i = 0; i < N; i++) {
            det *= CMat[i][i];
        }
        return det;
    }
    
    private double[][] MatTrans(double[][] Mat, int row, int col) {//计算矩阵的转置
        double[][] T = new double[col][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j ++) {
                T[j][i] = Mat[i][j];
            }
        }
        return T;
    }
    
    private String MatStr(double[][]Mat, int row, int col) {//将矩阵转化为可显示的字符串
        String result = "";
        for (int i = 0; i < row; i++) {
            String rowStr = "";
            for (int j = 0; j < col; j++) {
                rowStr = rowStr + Double.toString(Mat[i][j]) + "    ";
            }
            result = result + rowStr + "\n";
        }
        return result;
    }

    public String getResult() {
        String result = "";
        if (Aselected) {
            MatrixA = parseMatrix(MatTextA, Arow, Acol);
        }
        if (Bselected) {
            MatrixB = parseMatrix(MatTextB, Brow, Bcol);
        }
        if (Opcode.equals("+") && Aselected && Bselected) {     //A + B
            if (Arow != Brow || Acol != Bcol || MatrixA == null || MatrixB == null) {   //判定是否满足运算前提要求（下同）
                return "error";
            }
            double[][] C = MatAdd(MatrixA, Arow, Acol, MatrixB, Brow, Bcol);
            result = MatStr(C, Arow, Acol);
        }
        else if (Opcode.equals("-") && Aselected && Bselected) {    //A - B
            if (Arow != Brow || Acol != Bcol || MatrixA == null || MatrixB == null) {
                return "error";
            }
            double[][] C = MatMinus(MatrixA, Arow, Acol, MatrixB, Brow, Bcol);
            result = MatStr(C, Arow, Acol);
        }
        else if (Opcode.equals("*") && Aselected && Bselected) {    //A * B
            if (Acol != Brow || MatrixA == null || MatrixB == null) {
                return "error";
            }
            double[][] C = MatMul(MatrixA, Arow, Acol, MatrixB, Brow, Bcol);
            result = MatStr(C, Arow, Bcol);
        }
        else if (Opcode.equals("/") && Aselected && Bselected) {    //A / B
            if (Brow != Bcol || Bcol != Arow || MatrixA == null || MatrixB == null) {
                return "error";
            }
            double[][] C = MatDiv(MatrixA, Arow, Acol, MatrixB, Brow, Bcol);
            result = MatStr(C, Brow, Acol);
        }
        else if (Opcode.equals("Inv") && Aselected) {   //A^(-1)
            if (Arow != Acol || MatrixA == null) {
                return "error";
            }
            double[][] C = MatInverse(MatrixA, Arow);
            result = MatStr(C, Arow, Acol);
        }
        else if (Opcode.equals("Inv") && Bselected) {   //B^(-1)
            if (Brow != Bcol || MatrixB == null) {
                return "error";
            }
            System.out.println(MatStr(MatrixB, Brow, Bcol));
            double[][] C = MatInverse(MatrixB, Brow);
            result = MatStr(C, Brow, Bcol);
        }
        else if (Opcode.equals("||") && Aselected) {    //|A|
            if (Arow != Acol || MatrixA == null) {
                return "error";
            }
            result = Double.toString(MatDet(MatrixA, Arow));
        }
        else if (Opcode.equals("||") && Bselected) {    //|B|
            if (Brow != Bcol || MatrixB == null) {
                return "error";
            }
            result = Double.toString(MatDet(MatrixB, Brow));
        }
        else if (Opcode.equals("Trans") && Aselected) { //A^T
            if (MatrixA == null) {
                return "error";
            }
            double[][] C = MatTrans(MatrixA, Arow, Acol);
            result = MatStr(C, Acol, Arow);
        } 
        else if (Opcode.equals("Trans") && Bselected) { //B^T
            if (MatrixB == null) {
                return "error";
            }
            double[][] C = MatTrans(MatrixB, Brow, Bcol);
            result = MatStr(C, Bcol, Brow);
        }
        else if (Opcode.equals("=") && Aselected) {//输出A
            if (MatrixA == null) {
                return "error";
            }
            result = MatStr(MatrixA, Arow, Acol);
        } 
        else if (Opcode.equals("=") && Bselected) { //输出B
            if (MatrixB == null) {
                return "error";
            }
            result = MatStr(MatrixB, Brow, Bcol);
        }
        else
            result = "error";
        return result;
    }
    
    /* public static void main(String[] args) {
        String TextA = "[1, 2, 3; 4, 5, 6]";
        String TextB = "[1, 2;   3, 4; 5, 6]";
        String TextC = "[1, 2; 3, 4]";
        String TextD = "[1, 2, 3; 4, 5, 6]";
        String TextE = "[1, 2, 3; 4, 5, 6;7 ,8 , 9]";
        String[] oprd = {"+", "-", "*", "/", "Inv", "||", "Trans", "="};
        MatCal M = new MatCal("3", "3", TextE, "2", "2", TextC, oprd[4], false, true);
        System.out.print(M.getResult());
    } */
 }