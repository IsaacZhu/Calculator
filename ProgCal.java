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
        "|","^","!","&",".","\0","a","b","c","d","e","f"
    };
    private List<String> allowList = new ArrayList<String>();   //可行集的链表状态
    private String notAllowInDEC[] = {  //十进制禁止集
        "A","B","C","D","E","F","a","b","c","d","e","f"
    };
    private String notAllowInOCT[] = {  //八进制禁止集
        "A","B","C","D","E","F","9","a","b","c","d","e","f"
    };
    private String notAllowInBIN[] = {  //二进制禁止集
        "A","B","C","D","E","F","3","4","5","6","7","8","9","a","b","c","d","e","f"
    };
    private char numSet[] = { //数字集，用于判定是否是数字
        '1','2','3','4','5','6','7','8','9','0','A','B','C','D','E','F',
        'a','b','c','d','e','f'
    };
    private List<Character> numList = new ArrayList<Character>();
    
    Stack<Double> numStack = new Stack<Double>();   //数字栈
    private String hexToBin[] = {   //打表，十六进制转二进制
        "0000","0001","0010","0011","0100","0101","0110","0111",
        "1000","1001","1010","1011","1100","1101","1110","1111"
    }; 
    private String errorList[] = {  //错误列表
        "输入中有非法字符",         //0
        "操作数需要是整数",         //1
        "不允许多个小数点",         //2
        "有小数点但没有小数部分",   //3
        "括号不匹配",              //4
        "符号与操作数数量不匹配",    //5
        "未知错误"                 //6
    };
    private List<String> errorArrayList = new ArrayList<String>();

    //浮点数变整数
    public int floatToInt(Double x){
        int floor = (int)Math.floor(x);  //下整
        int ceil = (int)Math.ceil(x);    //上整
        if (x - floor > 10e-6 && ceil - x > 10e-6){
            errorShower = true; 
            error(1);
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
        //System.out.println("#1");//tc
        if (!allowList.contains(newSym)){   //若该字符不合法
            //System.out.println("#2");//tc
            error(0);       //报错并返回
            return;
        }
        if (numList.contains(inputText[stringIndex])){    //是数字
            sym = "num";    //表明是数字
            symNum = 0.0;
            while(numList.contains(inputText[stringIndex]) || inputText[stringIndex] == '.'){
                System.out.println("num:"+inputText[stringIndex]);//tc
                if (inputText[stringIndex] == '.'){ //小数点
                    if (isFloatNum){    //前面已经有一个小数点
                        error(2);    //输入有误
                        return;
                    }
                    else isFloatNum = true; //记录这个小数点
                }
                else if (inputText[stringIndex] >= 'A' && inputText[stringIndex] <= 'F'
                        || inputText[stringIndex] >= 'a' && inputText[stringIndex] <= 'f'){  //十六进制
                    tmpNum = inputText[stringIndex] - 'A' + 10; //unicode -> num
                    if (isFloatNum){    //小数 
                        if (tmpNum != 0)
                        symNum += Math.pow(tmpNum,-(++bit));
                    }
                    else symNum = symNum * scale + tmpNum ; //逐位加
                }
                else {  //普通数字
                    tmpNum = inputText[stringIndex] - '0';
                    if (isFloatNum){    //小数 
                        if (tmpNum != 0)
                        symNum += Math.pow(tmpNum,-(++bit));
                    }
                    else symNum = symNum * scale + tmpNum ; //逐位加
                }
                /*if (isFloatNum){    //小数 
                    if (tmpNum != 0)
                    symNum += Math.pow(tmpNum,-(++bit));
                }
                else symNum = symNum * scale + tmpNum ;*/ //逐位加
                stringIndex++; 
            }//while
            if (isFloatNum && bit == 0){    //小数点后面没有跟数！
                error(3);
                return;
            }//if
        }//if 数字
        else {  //其他符号
            sym = newSym;   //符号赋值
            stringIndex++;  //下标移动
        }
        System.out.println("sym:"+sym);    //tc
    }//getSym
    
    //报错专用函数
    public void error(){
        sym = "输入有误";
        errorShower = true;
    }//error

    public void error(int x){
        sym = errorList[x];
        errorShower = true;
    }//error

    //递归计算部分
    public void factor(List<String> fsys){
        if (sym.equals("num")){ //数字
            numStack.push(symNum);  //数字入栈
            getSym();//获取下一个符号
        }
        else if (sym.equals("(")){  //匹配到左括号
            getSym();
            List<String> newFsys = new ArrayList<String>();    //终结符
            newFsys.add("\0"); //添加终结符
            expression(newFsys);
            if (sym.equals(")")) return;
            else {  //括号不匹配，有误
                error(4);
                return;
            }
        }//else if (
        //可能是个冒险的决定
        else if (fsys.contains(sym)) return;
        else{   //有误
            error(0);   //不一定是这个错误。。。
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
                error(5);
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
                error(5);
                return;
            }
            else{
                tmpFloat = numStack.pop();    //出栈一个数
                tmpInterger = (int)tmpFloat;    //取整
                if (tmpFloat - tmpInterger > 10E-6 || 
                    tmpInterger - tmpFloat > 10E-6){    //看是否整数
                    error(1);
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
                error(5);
                return;
            }
            else{
                tmpNum1 = numStack.pop();    //出栈一个数
                getSym();
                not_expr(fsys); //算第二个数
                if (numStack.isEmpty()){    //栈空，有误
                    error(5);
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
                error(5);
                return;
            }
            else{
                tmpNum1 = numStack.pop();    //出栈一个数
                getSym();
                mul_expr(fsys); //算第二个数
                if (numStack.isEmpty()){    //栈空，有误
                    error(5);
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
                error(5);
                return;
            }
            else{
                tmpNum1 = floatToInt(numStack.pop());    //出栈一个数
                getSym();
                additive_expr(fsys); //算第二个数
                if (numStack.isEmpty()){    //栈空，有误
                    error(5);
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
                error(5);
                return;
            }
            else{
                tmpNum1 = floatToInt(numStack.pop());    //出栈一个数
                getSym();
                and_expr(fsys); //算第二个数
                if (numStack.isEmpty()){    //栈空，有误
                    error(5);
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
                error(5);
                return;
            }
            else{
                tmpNum1 = floatToInt(numStack.pop());    //出栈一个数
                getSym();
                xor_expr(fsys); //算第二个数
                if (numStack.isEmpty()){    //栈空，有误
                    error(5);
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

        getSym();
        expression(fsys);
        //if (sym.equals("输入有误")){  //输入有问题
        if (errorShower){
            return sym;
        }
        else{
            result = numStack.pop();    //结果出栈
            System.out.println("result is"+ result);    //tc
            if (!numStack.isEmpty()){   //结果出栈后栈不空，说明原式有误
                error(5);
                return sym;
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
        txt = getProgResult(text,inputRadix); //先利用这个函数分析数字，得到result
        int intPart = (int)result;
        System.out.println("txt:"+txt+"intpart"+intPart);//tc
        for (int i = 0; i < errorList.length; ++i){
            errorArrayList.add(errorList[i]);
        }
        if (errorArrayList.contains(txt) || txt.equals("输入有误")) return txt;
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