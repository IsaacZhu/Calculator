//方程计算器
//zjr 18.3
import java.util.ArrayList;
import java.util.List;

public class EquaCal{
    /**********************解一元二次方程************************/
    private double a = 0.0,b = 0.0,c = 0.0,d = 0.0,ttResult1 = 0.0, ttResult2 = 0.0, delta = 0.0;
    private char[] inputText;

    public String getTwoTimesEquationResult(String text){
        String errorMsg = "格式有误，请输入形如：" + "\n" + "{4,2,3,5}" + "\n" + "以表示 4x^2+2x+3 = 5";
        int index = 0, length = 0;
        char ch;
        String tmpString;
        inputText = text.toCharArray(); //转化为字符数组
        //分析字符数组
        length = inputText.length;
        ch = text.charAt(0);
        if (ch != '{') return errorMsg; //输入有误
        index = text.indexOf(",");   //获取第一个逗号位置
        if (index == -1) return errorMsg; //输入有误
        //避免用户第二个字符出错
        ch = text.charAt(1);
        if (!((ch >= '0' && ch <= '9') || ch == '-' )) return errorMsg;

        a = Double.parseDouble(text.substring(1,index));   //获取第一个数
        System.out.println("a"+a);//tc
        tmpString = text.substring(index + 1);  //切割掉第一个数
        text = tmpString;

        index = text.indexOf(",");   //获取第二个逗号位置
        if (index == -1) return errorMsg; //输入有误
        b = Double.parseDouble(text.substring(0,index));   //获取第二个数
        tmpString = text.substring(index + 1);  //切割掉第二个数
        text = tmpString;

        index = text.indexOf(",");   //获取第三个逗号的位置
        if (index == -1) return errorMsg; //输入有误
        c = Double.parseDouble(text.substring(0,index));   //获取第三个数
        tmpString = text.substring(index + 1);  //切割掉第三个数
        text = tmpString;

        index = text.indexOf("}");   //获取}的位置
        if (index == -1) return errorMsg; //输入有误
        d = Double.parseDouble(text.substring(0,index));   //获取第四个数
        
        c = c - d;
        //计算delta
        delta = b*b - 4*a*c;
        System.out.println(delta);//tc
        if (delta < 0) return "无实根";
        ttResult1 = (-b + Math.sqrt(delta))/(2*a);
        ttResult2 = (-b - Math.sqrt(delta))/(2*a);
        System.out.println("re:"+ttResult1);//tc
        return  a + " x^2+" + b + " x+" + c + "=" + d + "\n"
                + "x1 = " + Double.toString(ttResult1) + "\n"
                + "x2 = " + Double.toString(ttResult2);
    }//getTwoTimesEquationResult
    /**********************解一元二次方程************************/

    /**********************解多元一次方程************************/
    private static int dimMax = 5;
    public String[] gaussEmilation(String dimText, String text){
        String errorMsg = "格式有误，请输入形如：" + "\n" + "{{2,3,5},{1,4,6}}" + "\n" 
                            + "以表示 2*x0 +3*x1 = 5" + "\n" + "x0 + 4*x1 = 6";
        String[] returnText= {"",""};
        int i,j,k,index;
        char ch;
        String tmpText;
        double coeff = 0.0;
        int dim = Integer.valueOf(dimText);
        if (dim <= 0 || dim > dimMax){ 
            returnText[0] = "变量数过多或过少！范围:";
            return returnText;
        }
        double matrix[][] = new double[dim][dim+1];
        //double matrix[dim][dim+1];
        //double solution[dim];
        double solution[] = new double[dim];
        ch = text.charAt(0);
        if (ch != '{'){ //不按格式输入
            returnText[0] = errorMsg;
            return returnText;
        }
        tmpText = text.substring(1);    //裁掉第一个{
        text = tmpText;
        /**************读入系数*******************/
        for (i = 0;i < dim;++i){
            ch = text.charAt(0);
            if (ch != '{'){ //不按格式输入
                returnText[0] = errorMsg;
                return returnText;
            }
            tmpText = text.substring(1);    //裁掉第一个{
            text = tmpText;
            for (j = 0; j < dim; ++j){
                index = text.indexOf(",");   //获取逗号位置
                if (index == -1){           //逗号不存在
                    returnText[0] = errorMsg;
                    return returnText;
                }
                matrix[i][j] = Double.parseDouble(text.substring(0,index));
                tmpText = text.substring(index + 1);  //切割掉这个数和逗号
                text = tmpText;
            }//for j
            index = text.indexOf("}");   //获取}的位置
            if (index == -1){
                returnText[0] = errorMsg;
                return returnText;
            }
            matrix[i][dim] = Double.parseDouble(text.substring(0,index));   //获取等式右边的常量
            tmpText = text.substring(index + 1);  //切割掉这个数和}
            text = tmpText;
            ch = text.charAt(0);
            if (ch == ','){
                tmpText = text.substring(1);  //切割掉 ','
                text = tmpText;
            }
        }//for i
        /**************读入系数结束*******************/
        
        /************将读入的矩阵输出*****************/
        for (i = 0; i < dim; ++i){
            for (j = 0; j < dim-1; ++j){
                returnText[0] += matrix[i][j] + "*x" + j + "+";
            }//for j
            returnText[0] += matrix[i][dim-1] + "*x" + dim;       //最后一个变量系数
            returnText[0] += "=" + matrix[i][dim] + "\n";   //等式右边的数
        }//for i
        /************读入矩阵输出结束*****************/
        
        /************计算结果********************** */
        //前代
        for (i = 0; i < dim; ++i){
            for (j = i + 1; j < dim; ++j){
                coeff = -matrix[j][i] / matrix[i][i];
                for (k = 0; k <= dim; ++k){
                    matrix[j][k] += matrix[i][k]*coeff;
                }//for k
            }//for j
        }//for i
        if (matrix[dim-1][dim] == 0 && matrix[dim-1][dim-1] == 0){  //最后一行没式子，无穷多解
            returnText[1] = "该方程有无穷多解";
            return returnText;
        }
        //回代
        for (i = dim - 1;i > 0; --i){
            for (j = 0;j < i; ++j){
                coeff = -matrix[j][i]/matrix[i][i];
                matrix[j][i] = 0;
                matrix[j][dim] += coeff * matrix[i][dim];
            }//for j
        }//for i
        //求解
        for (i = 0;i < dim;++i){
            if (matrix[i][i] == 0){
                if (matrix[i][dim] != 0){   //无解！
                    returnText[1] = "该方程无解!";
                    return returnText;
                }
                else solution[i] = 0;
            }//if 
            else{
                solution[i] = matrix[i][dim]/matrix[i][i];
            }
        }//for i
        /************计算结果结束******************* */
        
        /************输出求解结果******************* */
        for (i = 0;i < dim; ++i){
            returnText[1] += "x" + i + "=" + solution[i] + "\n";
        }
        /************输出结果结束******************* */
        return returnText;
    }//gaussEmilation

    /**********************解多元一次方程************************/

}//class EquaCal