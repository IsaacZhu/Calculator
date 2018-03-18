//ZJR
//18.3
//程序员计算器类
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Arrays;

public class ProgCal{
    //构造函数
    public ProgCal(){

    }//ProgCal
    enum InputState{            //输入状态
        HEX,DEC,OCT,BIN
    }
    double result = 0.0;    //结果
    double symNum = 0.0;         //从字符串中取出的数字，与getSym函数相关
    char inputText[];       //输入
    char endSym[] = {'\0'}; //终结符 
    String sym = "输入有误"; //当前符号
    InputState numState;    //数字状态
    int scale = 16;         //进制基值
    int stringIndex = 0;    //取字符下标指示器
    boolean errorShower = false;    //全局错误指示器 因为变量不够才创建的
    private String allowSet[] = {   //可行符号集
        "0","1","2","3","+","4","5","6","-",
        "(",")","A","B","7","8","9","*","C","D","E","F","AC","0","/",
        "|","^","!","&",".","\0"
    };
    private List<String> allowList = new ArrayList<String>();   //可行集的链表状态
    private String notAllowInDEC[] = {  //十进制禁止集
        "A","B","C","D","E","F"
    };
    private String notAllowInOCT[] = {  //八进制禁止集
        "A","B","C","D","E","F","9"
    };
    private String notAllowInBIN[] = {  //二进制禁止集
        "A","B","C","D","E","F","3","4","5","6","7","8","9"
    };
    private char numSet[] = { //数字集，用于判定是否是数字
        '1','2','3','4','5','6','7','8','9','0','A','B','C','D','E','F'
    };
    private List<Character> numList = new ArrayList<Character>();
    
    Stack<Double> numStack = new Stack<Double>();   //数字栈
    private String hexToBin[] = {   //打表，十六进制转二进制
        "0000","0001","0010","0011","0100","0101","0110","0111",
        "1000","1001","1010","1011","1100","1101","1110","1111"
    }; 
    private String errorList[] = {  //错误列表
        ""
    };

    //浮点数变整数
    public int floatToInt(Double x){
        int floor = (int)Math.floor(x);  //下整
        int ceil = (int)Math.ceil(x);    //上整
        if (x - floor > 10e-6 && ceil - x > 10e-6){
            errorShower = true; 
            error();
            return 0;
        }
        else if (x - floor > ceil - x){ //更接近上整
            return ceil;
        }
        else{
            return floor;
        }
    }//floatToInt

    //取符号操作
    public void getSym(){
        int tmpNum = 0;
        boolean isFloatNum = false; //记录小数
        int bit = 0;    //小数位
        String newSym = String.valueOf(inputText[stringIndex]);  //获得一个字符
        System.out.println("#1");//tc
        if (!allowList.contains(newSym)){   //若该字符不合法
            System.out.println("#2");//tc
            error();       //报错并返回
            return;
        }
        if (numList.contains(inputText[stringIndex])){    //是数字
            sym = "num";    //表明是数字
            symNum = 0.0;
            while(numList.contains(inputText[stringIndex]) || inputText[stringIndex] == '.'){
                if (inputText[stringIndex] == '.'){ //小数点
                    if (isFloatNum){    //前面已经有一个小数点
                        error();    //输入有误
                        return;
                    }
                    else isFloatNum = true; //记录这个小数点
                }
                else if (inputText[stringIndex] >= 'A' && inputText[stringIndex] <= 'F'){  //十六进制
                    tmpNum = inputText[stringIndex] - 'A' + 10; //unicode -> num
                }
                else {  //普通数字
                    tmpNum = inputText[stringIndex] - '0';
                }
                if (isFloatNum){    //小数 
                    symNum += Math.pow(tmpNum,-(++bit));
                }
                else symNum = symNum * scale + tmpNum ; //逐位加
                stringIndex++; 
            }//while
            if (isFloatNum && bit == 0){    //小数点后面没有跟数！
                error();
                return;
            }//if
        }//if 数字
        else {  //其他符号
            sym = newSym;   //符号赋值
            stringIndex++;  //下标移动
        }
        if (sym.equals("num"))  System.out.println(symNum);    //tc
        else System.out.println("getsym:"+sym);    //tc
    }//getSym
    
    //报错专用函数
    public void error(){
        sym = "输入有误";
    }//error

    //递归计算部分
    public void factor(List<String> fsys){
        if (sym.equals("num")){ //数字
            System.out.println("#4");    //tc
            
            numStack.push(symNum);  //数字入栈
            getSym();//获取下一个符号
            System.out.println(sym);    //tc
        }
        else if (sym.equals("(")){  //匹配到左括号
            getSym();
            List<String> newFsys = new ArrayList<String>();    //终结符
            newFsys.add("\0"); //添加终结符
            expression(newFsys);
            if (sym.equals(")")) return;
            else {  //括号不匹配，有误
                error();
                return;
            }
        }//else if (
        //可能是个冒险的决定
        else if (fsys.contains(sym)) return;
        else{   //有误
            error();
            return;
        }//else
    }//factor

    public void neg_expr(List<String> fsys){
        fsys.add("-");
        factor(fsys);
        double tmpNum;
        while (sym.equals("-")){
            getSym();
            factor(fsys);
            if (numStack.isEmpty()){    //栈空，有误
                error();
                return;
            }
            else{
                tmpNum = numStack.pop();    //出栈一个数
                tmpNum = -tmpNum;
                numStack.push(tmpNum);  //取负后入栈
            }
        }//while
    }//neg_expr

    //位取反
    public void not_expr(List<String> fsys){
        int tmpInterger;
        double tmpFloat;
        fsys.add("!");
        neg_expr(fsys);
        while (sym.equals("!")){
            getSym();
            factor(fsys);   //继续分析后面的内容
            if (numStack.isEmpty()){    //栈空，有误
                error();
                return;
            }
            else{
                tmpFloat = numStack.pop();    //出栈一个数
                tmpInterger = (int)tmpFloat;    //取整
                if (tmpFloat - tmpInterger > 10E-6 || 
                    tmpInterger - tmpFloat > 10E-6){    //看是否整数
                    error();
                    return;
                }
                else{   //可认为是整数
                    tmpInterger = ~tmpInterger;
                    numStack.push((double)tmpInterger);  //入栈
                }
            }
        }//while
    }//not_expr

    public void mul_expr(List<String> fsys){
        double tmpNum1,tmpNum2;
        String mulop;
        fsys.add("*");
        fsys.add("/");
        not_expr(fsys);
        while (sym.equals("*") || sym.equals("/")){
            System.out.println("#3");
            mulop = sym;    //记录符号
            if (numStack.isEmpty()){    //栈空，有误
                error();
                return;
            }
            else{
                tmpNum1 = numStack.pop();    //出栈一个数
                getSym();
                not_expr(fsys); //算第二个数
                if (numStack.isEmpty()){    //栈空，有误
                    error();
                    return;
                }
                else if (mulop.equals("*")){   //乘法   
                    tmpNum2 = numStack.pop();    //出栈一个数
                    numStack.push(tmpNum1 * tmpNum2);   //乘并入栈
                }//else if
                else{                   //除法
                    tmpNum2 = numStack.pop();    //出栈一个数
                    numStack.push(tmpNum1 / tmpNum2);   //除并入栈
                }//else
            }//else
        }//while
    }//mul_expr

    public void additive_expr(List<String> fsys){
        double tmpNum1,tmpNum2;
        fsys.add("+");
        mul_expr(fsys);
        while (sym.equals("+")){
            if (numStack.isEmpty()){    //栈空，有误
                error();
                return;
            }
            else{
                tmpNum1 = numStack.pop();    //出栈一个数
                getSym();
                mul_expr(fsys); //算第二个数
                if (numStack.isEmpty()){    //栈空，有误
                    error();
                    return;
                }
                else    {   
                    tmpNum2 = numStack.pop();    //出栈一个数
                    numStack.push(tmpNum1 + tmpNum2);   //加并入栈
                }//else
            }//else
        }//while
    }//additive_expr

    public void and_expr(List<String> fsys){
        int tmpNum1,tmpNum2;
        fsys.add("&");
        additive_expr(fsys);
        while (sym.equals("&")){
            if (numStack.isEmpty()){    //栈空，有误
                error();
                return;
            }
            else{
                tmpNum1 = floatToInt(numStack.pop());    //出栈一个数
                getSym();
                additive_expr(fsys); //算第二个数
                if (numStack.isEmpty()){    //栈空，有误
                    error();
                    return;
                }
                else    {   
                    tmpNum2 = floatToInt(numStack.pop());    //出栈一个数
                    numStack.push((double)(tmpNum1 & tmpNum2));   //与并入栈
                }//else
            }//else
        }//while
    }//and_expr

    public void xor_expr(List<String> fsys){
        int tmpNum1,tmpNum2;
        fsys.add("^");
        and_expr(fsys);
        while (sym.equals("^")){
            if (numStack.isEmpty()){    //栈空，有误
                error();
                return;
            }
            else{
                tmpNum1 = floatToInt(numStack.pop());    //出栈一个数
                getSym();
                and_expr(fsys); //算第二个数
                if (numStack.isEmpty()){    //栈空，有误
                    error();
                    return;
                }
                else    {   
                    tmpNum2 = floatToInt(numStack.pop());   //出栈一个数
                    numStack.push((double)(tmpNum1 ^ tmpNum2));   //异或并入栈
                }//else
            }//else
        }//while
    }//xor_expr

    public void or_expr(List<String> fsys){
        int tmpNum1,tmpNum2;
        fsys.add("|");
        xor_expr(fsys);
        while (sym.equals("|")){
            if (numStack.isEmpty()){    //栈空，有误
                error();
                return;
            }
            else{
                tmpNum1 = floatToInt(numStack.pop());    //出栈一个数
                getSym();
                xor_expr(fsys); //算第二个数
                if (numStack.isEmpty()){    //栈空，有误
                    error();
                    return;
                }
                else    {   
                    tmpNum2 = floatToInt(numStack.pop());    //出栈一个数
                    numStack.push((double)(tmpNum1 | tmpNum2));   //或并入栈
                }//else
            }//else
        }//while
    }//or_expr

    public void expression(List<String> fsys){
        or_expr(fsys);
    }//expression

    //进制转换函数
    //处理的数字变量是result
    //处理十六进制 -> 八进制/二进制
    //只处理六位小数
    public String hexToOtherRadix(InputState outState){
        int intPart = 0;
        int dotIndex,readIndex,tmp;   //小数点位置 及 读到的位置
        double floatPart;
        String intPartText = "",hexFloatPart;
        String binFloatPart = "";   //二进制小数部分
        String octFloatPart = "";   //八进制小数部分
        char ch;    
        //整数部分直接转到目标进制
        intPart = (int)result;
        if (outState == InputState.OCT){    //八进制
            intPartText = Integer.toOctalString(intPart);
        }
        else if (outState == InputState.BIN){   //二进制  
            intPartText = Integer.toBinaryString(intPart);
        }
        //小数部分先转为二进制
        floatPart = result - intPart;
        hexFloatPart = Double.toHexString(floatPart);  //先转为十六进制
        dotIndex = hexFloatPart.indexOf(".");
        if (dotIndex == -1){    //小数点不存在,直接返回整数部分即可
            return intPartText;
        }
        readIndex = dotIndex + 1;   //第一个小数
        for (int i = 0; i < 5 && readIndex < hexFloatPart.length(); ++i){    //数五位小数
            ch = hexFloatPart.charAt(readIndex++);
            if (ch >= 'A' && ch <= 'F'){  //十六进制
                binFloatPart += hexToBin[ch - 'A' + 10]; //unicode -> num
            }
            else if (ch >= '0' && ch <= '9'){  //普通数字
                binFloatPart += hexToBin[ch - '0'];
            }
        }//for
        if (outState == InputState.BIN){    //二进制
            return intPartText + "." + binFloatPart.substring(0,binFloatPart.length()>=6?6:binFloatPart.length());
        }

        //小数部分二进制转八进制
        readIndex = 0;
        for (int i = 0; i < 6 && readIndex <= (binFloatPart.length()-3); ++i){
            tmp = Integer.valueOf((binFloatPart.substring(readIndex,readIndex+3))); //获取二进制数
            switch (tmp){   //二进制数转八进制
                case 0: //000
                    octFloatPart += "0";
                    break;
                case 1: //001
                    octFloatPart += "1";
                    break;
                case 10: //010
                    octFloatPart += "2";
                    break;
                case 11: //011
                    octFloatPart += "3";
                    break;
                case 100: //100
                    octFloatPart += "4";
                    break;
                case 101: //101 
                    octFloatPart += "5";
                    break;
                case 110: //110
                    octFloatPart += "6";
                    break;
                case 111: //111
                    octFloatPart += "7";
                    break;
                default: return "输入有误";
            }///switch
            readIndex += 3;
        }//for
        return intPartText + "." + octFloatPart;
    }//hexToOtherRadix

    //结果计算函数
    public String getProgResult(String text,int radix){
        String resultString;    //结果
        //属性传递
        inputText = text.toCharArray();
        //添加终结符
        inputText = Arrays.copyOf(inputText,inputText.length+1);    //扩容1字节
        System.arraycopy(endSym,0,inputText,inputText.length-1,1);  //复制过去

        scale = radix;
        switch(radix){   //处理进制基值
            case 16 : 
                numState = InputState.HEX;
                break;
            case 10 : 
                numState = InputState.DEC;
                break;
            case 8 : 
                numState = InputState.OCT;
                break;
            case 2 : 
                numState = InputState.BIN;
                break;
        }
        //添加所有可行字符到链表中
        for (int i = 0; i < allowSet.length; ++i){
            allowList.add(allowSet[i]);
        }
        for (int i = 0; i < numSet.length; ++i){
            numList.add(numSet[i]);
        }
        //处理可行集 删除不可行的字符
        if (numState == InputState.DEC){    //十进制
            for (int i = 0; i < notAllowInDEC.length; ++i){
                allowList.remove(notAllowInDEC[i]); 
            }
        }
        else if (numState == InputState.OCT){   //八进制
            for (int i = 0; i < notAllowInOCT.length; ++i){
                allowList.remove(notAllowInOCT[i]); 
            }
        }
        else if (numState == InputState.BIN){   //二进制
            for (int i = 0; i < notAllowInBIN.length; ++i){
                allowList.remove(notAllowInBIN[i]); 
            }
        }
        List<String> fsys = new ArrayList<String>();    //终结符
        fsys.add("\0"); //添加终结符

        System.out.println("#0");//tc
        getSym();
        expression(fsys);
        if (sym.equals("输入有误")){  //输入有问题
            return "输入有误";
        }
        else{
            result = numStack.pop();    //结果出栈
            System.out.println("result is"+ result);    //tc
            if (!numStack.isEmpty()){   //结果出栈后栈不空，说明原式有误
                return "输入有误";
            }
            //处理输出进制
            if (scale == 10){   //十进制
                resultString = Double.toString(result);  //转化为字符串
            }
            else if (scale == 16){  //十六进制
                //resultString = Double.toHexString(result);    //tc
                resultString = Integer.toHexString((int)result);      //tc
            }
            else if (scale == 8){   //八进制
                resultString = Integer.toOctalString((int)result);
            }
            else{   //二进制
                resultString = Integer.toBinaryString((int)result);
            }
            return resultString;    
        }
    }//getProgResult

    //进制转换按钮响应函数
    public String getRadixConvertResult(String text,int inputRadix,int outputRadix){
        String txt;
        int intPart = (int)result;
        txt = getProgResult(text,inputRadix); //先利用这个函数分析数字，得到result
        if (outputRadix == 10){ //十进制，直接输出即可
            return Double.toString(result);
        }
        else if (outputRadix == 16){    //十六进制
            return Integer.toHexString(intPart);//转成十六进制
        }
        else if (outputRadix == 8){    //八进制
            return Integer.toOctalString(intPart);//转成八进制
        }
        else if (outputRadix == 16){    //二进制
            return Integer.toBinaryString(intPart);//转成二进制
        }
        else{
            return "输入错误";
        }
    }//getRadixConvertResult
    
}//class ProgCal