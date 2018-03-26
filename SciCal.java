//The core part of science calculating
//ljq 3.17

import java.math.*;

/**
 * SciCal
 */
public class SciCal {
    private String formula; //表达式字符串
    private int loc; //字符串读取到的位置
    private String nsym; //下一个符号
    private static double e = 2.71828182845904523536;
    private static double pi = 3.14159265358979323846;

    private String[] symList = { "sin(", "cos(", "tan(", "lg(", "ln(", "^", "!", "e", "π", "sqrt(", "(", ")", "sinh(",
            "cosh(", "tanh(", "+", "-", "*", "/", "=" };            //识别符号集

    public SciCal(String str) {
        formula = str;
        loc = 0;
    }

    private void getsym() { //获取下一个符号
        while (loc < formula.length() && (formula.charAt(loc) == ' ' || formula.charAt(loc) == '\t' || formula.charAt(loc) == '\n')) {   //清除可能混入的空格
            loc++;
        }
        if (loc == formula.length()) { //可能会有符号漏掉
            nsym = "null";
        } else if (Character.isDigit(formula.charAt(loc))) { //识别组成数字的符号
            int begin = loc;
            loc++;
            while (loc < formula.length() && Character.isDigit(formula.charAt(loc)))
                loc++;
            if (loc < formula.length() && formula.charAt(loc) == '.') { //确保只有一个'.'
                loc ++;
                while (loc < formula.length() && Character.isDigit(formula.charAt(loc)))
                    loc++;
            }
            nsym = formula.substring(begin, loc);
        } else {
            int i;
            for (i = 0; i < symList.length; i++) { //识别特殊符号
                int len = symList[i].length();
                //System.out.print("###" + i + " " + nsym + " " + formula.substring(loc, loc + len) + "/" +  + "\n");
                if (loc + len <= formula.length() && formula.substring(loc, loc + len).equals(symList[i])) {
                    loc += len;
                    nsym = symList[i];
                    //System.out.print("***" + i + nsym + "\n");
                    break;
                }
            }
            if (i == symList.length) {
                nsym = "error"; //返回语法错误的信息
            }
        }
        //System.out.print(loc + " " + nsym + "\n");
    }

    private double factor() {
        double result;
        if (nsym.equals("error")) { //若出错，返回一个无意义数，来给上层一个判断的线索
            return Double.NaN;
        }
        if (nsym.equals("(")) {     //factor -> (expression)
            getsym();
            result = expression();
            if(Double.isNaN(result) && nsym.equals("error"))    //同时满足结果为NaN，且nsym为"error"，则可判断出错（下同）
                return Double.NaN;
            if (nsym.equals(")")) { //对漏掉 ')'的容忍(下同)
                getsym();
            }
        } else if (nsym.equals("π")) {  //factor -> π
            result = pi;
            getsym();
        } else if (nsym.equals("e")) {  //factor -> e
            result = e;
            getsym();
        } else if (Character.isDigit(nsym.charAt(0))) { //factor -> number
            result = Double.parseDouble(nsym);
            getsym();
        } else
            result = Double.NaN;
        return result;
    }

    private double mulExpr() {
        double result;
        if (nsym.equals("error")) { //若出错，返回一个无意义数，来给上层一个判断的线索
            return Double.NaN;
        }
        if (nsym.equals("sqrt(")) { //mul_expr -> sqrt( expression )
            getsym();
            result = Math.sqrt(expression());
            if (nsym.equals(")")) { //对漏掉 ')'的容忍(下同)
                getsym();
            }
        } else if (nsym.equals("sin(")) { //mul_expr -> sin( expression )
            getsym();
            result = Math.sin(expression());
            if (nsym.equals(")")) {
                getsym();
            }
        } else if (nsym.equals("cos(")) { //mul_expr -> cos( expression )
            getsym();
            result = Math.cos(expression());
            if (nsym.equals(")")) {
                getsym();
            }
        } else if (nsym.equals("tan(")) { //mul_expr -> tan( expression )
            getsym();
            result = Math.tan(expression());
            if (nsym.equals(")")) {
                getsym();
            }
        } else if (nsym.equals("sinh(")) { //mul_expr -> sinh( expression )
            getsym();
            result = Math.sinh(expression());
            if (nsym.equals(")")) {
                getsym();
            }
        } else if (nsym.equals("cosh(")) { //mul_expr -> cosh( expression )
            getsym();
            result = Math.cosh(expression());
            if (nsym.equals(")")) {
                getsym();
            }
        } else if (nsym.equals("tanh(")) { //mul_expr -> tanh( expression )
            getsym();
            result = Math.tanh(expression());
            if (nsym.equals(")")) {
                getsym();
            }
        } else if (nsym.equals("lg(")) { //mul_expr -> lg( expression )
            getsym();
            result = Math.log10(expression());
            if (nsym.equals(")")) {
                getsym();
            }
        } else if (nsym.equals("ln(")) { //mul_expr -> ln( expression )
            getsym();
            result = Math.log(expression());
            if (nsym.equals(")")) {
                getsym();
            }
        } else if (nsym.equals("-")) { //mul_expr -> - factor
            getsym();
            result = -factor();
        } else {
            result = factor();          //mul_expr -> factor
            if (Double.isNaN(result) && nsym.equals("error")) //同时满足结果为NaN，且nsym为"error"，则可判断出错
                return Double.NaN;
            if (nsym.equals("^")) {         //mul_expr -> factor ^ factor
                getsym();
                result = Math.pow(result, factor());
            } else if (nsym.equals("!")) {  //mul_expr -> factor !
                long intPart = (long) result;
                if(Double.compare(result, (double) intPart) != 0) { //若非整数，则不能进行阶乘操作
                    nsym = "error";
                    return Double.NaN;
                }
                if (intPart == 0) {
                    result = 1;
                }
                while(intPart > 0) {
                    result = result * (double) intPart;
                    intPart --;
                }
                getsym();
            }
        }
        if (Double.isNaN(result) && nsym.equals("error")) //同时满足结果为NaN，且nsym为"error"，则可判断出错
            return Double.NaN;
        return result;
    }

    private double additionExpr() {
        double result;
        String op;
        result = mulExpr();
        if (Double.isNaN(result) && nsym.equals("error")) //同时满足结果为NaN，且nsym为"error"，则可判断出错
            return Double.NaN;
        op = nsym;
        while (nsym.equals("*") || nsym.equals("/")) {
            getsym();
            if (op.equals("*")) {
                result = result * mulExpr();
            } else {
                result = result / mulExpr();
            }
            if (Double.isNaN(result) && nsym.equals("error")) //同时满足结果为NaN，且nsym为"error"，则可判断出错
                return Double.NaN;
            op = nsym;
        }
        if (nsym == "error") {
            return Double.NaN;
        }
        return result;
    }

    private double expression() {
        double result;
        String op;
        result = additionExpr();
        if (Double.isNaN(result) && nsym.equals("error")) //同时满足结果为NaN，且nsym为"error"，则可判断出错
            return Double.NaN;
        op = nsym;
        while (nsym.equals("+") || nsym.equals("-")) {
            getsym();
            if (op.equals("+")) {
                result = result + additionExpr();
            } else {
                result = result - additionExpr();
            }
            if (Double.isNaN(result) && nsym.equals("error")) //同时满足结果为NaN，且nsym为"error"，则可判断出错
                return Double.NaN;
            op = nsym;
        }
        if (nsym == "error") {
            return Double.NaN;
        }
        return result;
    }

    public String getResult() {
        double result;
        String resultStr;
        getsym();
        result = expression();
        if (Double.isNaN(result) && nsym.equals("error")) //同时满足结果为NaN，且nsym为"error"，则可判断出错
            resultStr = "error";
        else {
            resultStr = Double.toString(result);
        }
        return resultStr;
    }

    public static void main(String[] args) {
        SciCal f = new SciCal(args[0]);
        String str = f.getResult();
    }

}
