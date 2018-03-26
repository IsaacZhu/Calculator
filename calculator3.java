//calculator
//real version
//zjr 18/3

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.InputEvent; 
import java.awt.event.KeyEvent;

public class calculator3 extends JFrame{
    public static void main(String args[]){
        try {
            new calculator3();
		} catch (Exception e) {
			System.out.println("程序异常终止");
			System.exit(0);   //退出虚拟机
		}
    }//main
    //构造函数
    calculator3(){
        UIinit();
        listenInit();
    } //calculator

    /***************界面生成********************************************************/
    private JFrame jf=new JFrame("Calculator"); //窗口
	private JPanel sciCalPanel=new JPanel();    //科学计算器面板
	private JPanel progCalPanel=new JPanel();   //程序计算器面板
    private JPanel matrixCalPanel = new JPanel();//矩阵计算器面板
    private JPanel equationCalPanel = new JPanel();//方程计算器面板
    
    //创建并添加菜单栏
    JMenuBar menuBar = new JMenuBar();
    

    //创建并添加各菜单，注意：菜单的快捷键是同时按下Alt键和字母键，方法setMnemonic('F')是设置快捷键为Alt +Ｆ
    JMenu menuFile = new JMenu("文件");
    JMenuItem itemExit = new JMenuItem("退出");

    JMenu menuHelp = new JMenu("帮助");
    JMenuItem itemSci = new JMenuItem("科学计算器用法");
    JMenuItem itemProg = new JMenuItem("程序员计算器用法");
    JMenuItem itemMat = new JMenuItem("矩阵计算器用法");
    JMenuItem itemEqua = new JMenuItem("方程计算器用法");
    JMenuItem itemAbout = new JMenuItem("关于");

    //初始化菜单
    public void menuInit(){
        jf.setJMenuBar(menuBar);    //添加菜单栏

        menuBar.add(menuFile);      //添加 “文件” 菜单
        menuBar.add(menuHelp);      //添加帮助菜单
        menuFile.setMnemonic('F');  //设置快捷键
        menuHelp.setMnemonic('H');

        //文件菜单初始化
        menuFile.add(itemExit);

        //帮助菜单初始化
        menuHelp.add(itemSci);
        menuHelp.add(itemProg);
        menuHelp.add(itemMat);
        menuHelp.add(itemEqua);
        menuHelp.add(itemAbout);
    }//menuInit

    //初始化成四个tab，每块的参数在这里设置
    public void tabInit(){
        JTabbedPane tp = new JTabbedPane();
        //添加各面板
        tp.add(sciCalPanel);
        tp.add(progCalPanel);
        tp.add(matrixCalPanel);
        tp.add(equationCalPanel);
        // 设置tab的标题
        tp.setTitleAt(0, "科学计算器");
        tp.setTitleAt(1, "程序计算器");
        tp.setTitleAt(2, "矩阵计算器");
        tp.setTitleAt(3, "方程计算器");
        jf.setContentPane(tp);      //将tab作为面板内容
        tp.setBackground(deepGrey); //颜色
    }//tabInit

    /******************程序员计算器************* */
    private JPanel scalePanel = new JPanel();   //进制选择块
	//private JTextField progText = new JTextField(50);  //单行输入文本框
    private JTextArea progText = new JTextArea(1,50);
    private JLabel progResult = new JLabel();
    
    private JPanel progCenterPanel = new JPanel();   //中间面板
    private JPanel progBtnPanel = new JPanel();    //按键面板

    //16进制、10进制、8进制、2进制按钮
    private JRadioButton progBtnHEX = new JRadioButton("HEX");  
    private JRadioButton progBtnDEC = new JRadioButton("DEC");
    private JRadioButton progBtnOCT = new JRadioButton("OCT");
    private JRadioButton progBtnBIN = new JRadioButton("BIN");
    
    private ButtonGroup bg = new ButtonGroup();// 按钮分组

    //其他各种按钮
    String progBtnText[] = {"Or","Xor","Not","And","1","2","3","+","HEX","DEC","OCT","BIN","4","5","6","-",
    "(",")","A","B","7","8","9","*","C","D","E","F","AC","0","=","/"};
    JButton progBtns[] = new JButton[progBtnText.length];

    //程序员计算器面板初始化
    public void progUIInit(){
        progCalPanel.setLayout(null);  //空布局
        //添加三个基本块
        progCalPanel.add(scalePanel);
        progCalPanel.add(progCenterPanel);
        progCalPanel.add(progBtnPanel);
        
        //处理第一个基本块
        progBtnHEX.setSelected(true);     //默认第一个选中
        //progBtnHEX.setBounds(50, 50, 130, 30);    //设置位置
        //progBtnDEC.setBounds(50, 100, 130, 30); //同上
        bg.add(progBtnHEX); //添加各个按钮到组里，下同
        bg.add(progBtnDEC);
        bg.add(progBtnOCT);
        bg.add(progBtnBIN);
        scalePanel.add(progBtnHEX); //添加各个按钮到组里，下同
        scalePanel.add(progBtnDEC);
        scalePanel.add(progBtnOCT);
        scalePanel.add(progBtnBIN);
        scalePanel.setBounds(0,0,680,50);  //设置该块尺寸
        //scalePanel.setBackground(Color.WHITE);

        //处理第二个基本块
        progCenterPanel.setLayout(new GridLayout(2,1,0,0));
        progCenterPanel.add(progText,BorderLayout.NORTH);
        progCenterPanel.add(progResult);
        progCenterPanel.setBounds(0,50,680,50);  //设置该块尺寸
        progCenterPanel.setBackground(Color.WHITE);
        progResult.setText("");

        //处理第三个基本块
        //初始化按钮
        for (int i = 0;i < progBtnText.length;++i){
            progBtns[i] = new JButton(progBtnText[i]); //设置文字
            progBtns[i].setBackground(Color.WHITE);
            progBtnPanel.add(progBtns[i]);          //加入面板
        }
        progBtnPanel.setLayout(new GridLayout(4,8,0,0)); //设置网格
        progBtnPanel.setBounds(0,100,680,240);  //设置该块尺寸
        progBtnPanel.setBackground(deepGrey);   //颜色
    }//progUIInit
    /**********程序员计算器结束***************** */

    /**********科学计算器********************** */
    private JPanel sciInputPanel = new JPanel();    //输入面板
    private JPanel sciLeftSymPanel = new JPanel();     //左边按键面板
    private JPanel sciNumPanel = new JPanel();      //数字面板
    private JPanel sciRightSymPanel = new JPanel(); //右边按键面板

    private Color deepGrey = new Color(76,76,76);   //深灰
    private Color thickOrange = new Color(246,173,47);  //一种橙色

    //private JTextField sciText = new JTextField(50);  //单行输入文本框
    private JTextArea sciText = new JTextArea(1,50);
    private JLabel sciResult = new JLabel("");

    //左边面板符号按钮
    String sciLeftBtnText[] = {"sin","cos","tan","lg","ln","x^a","n!","e","π","sqrt","(",")",
    "sinh","cosh","tanh"};
    JButton sciLeftBtns[] = new JButton[sciLeftBtnText.length];
    //右边数字面板按钮
    String sciNumBtnText [] = {"AC","CM","M","1","2","3","4","5","6","7","8","9","ANS","0","."};
    JButton sciNumBtns [] = new JButton[sciNumBtnText.length];
    //右边面板符号按钮
    String sciRightBtnText [] = {"+","-","*","/","="};
    JButton sciRightBtns [] = new JButton[sciRightBtnText.length];

    //科学计算器面板初始化
    public void sciInit(){
        sciCalPanel.setLayout(null);    //空布局
        //添加各个面板
        sciCalPanel.add(sciInputPanel); 
        sciCalPanel.add(sciLeftSymPanel);
        sciCalPanel.add(sciNumPanel);
        sciCalPanel.add(sciRightSymPanel);

        //输入面板初始化
        sciInputPanel.setLayout(new GridLayout(2,1,0,0));
        sciInputPanel.setBackground(Color.WHITE);
        sciInputPanel.add(sciText);
        sciInputPanel.add(sciResult);  
        sciInputPanel.setBounds(0,0,680,50);

        //左边按键面板初始化
        sciLeftSymPanel.setLayout(new GridLayout(5,3,0,0)); //网格式布局
        for (int i =0 ;i < sciLeftBtns.length; ++i){
            sciLeftBtns[i] = new JButton(sciLeftBtnText[i]);    //按键名称
            sciLeftBtns[i].setBackground(Color.WHITE);
            sciLeftSymPanel.add(sciLeftBtns[i]);
        }
        sciLeftSymPanel.setBounds(0,50,400,290);    //设置大小及位置
        sciLeftSymPanel.setBackground(deepGrey);

        //数字面板初始化
        sciNumPanel.setLayout(new GridLayout(5,3,0,0));
        for (int i = 0;i < sciNumBtns.length; ++i){
            sciNumBtns[i] = new JButton(sciNumBtnText[i]);
            sciNumBtns[i].setBackground(Color.WHITE);
            sciNumPanel.add(sciNumBtns[i]);
        }
        sciNumPanel.setBounds(400,50,200,290);
        sciNumPanel.setBackground(deepGrey);

        //右边按钮面板初始化
        sciRightSymPanel.setLayout(new GridLayout(5,1,0,0));
        for (int i = 0;i < sciRightBtns.length; ++i){
            sciRightBtns[i] = new JButton(sciRightBtnText[i]);
            sciRightSymPanel.add(sciRightBtns[i]);
            sciRightBtns[i].setBorderPainted(false);//去掉边框
            sciRightBtns[i].setForeground(Color.WHITE);
            sciRightBtns[i].setBackground(thickOrange);
            sciRightBtns[i].setFont(new Font("苹方",1,20));
        }
        sciRightSymPanel.setBounds(600,50,80,290);
        sciRightSymPanel.setBackground(thickOrange);
    }//sciInit

    /**********科学计算器结束********************** */

    /**********矩阵计算器********************** */
    private JPanel matInputPanel = new JPanel();    //输入面板
    private JPanel matBtnPanel = new JPanel();      //按键面板
    private JPanel matResultPanel = new JPanel();   //结果面板

    //输入面板信息
    private JCheckBox matA = new JCheckBox("矩阵A");    //矩阵选中按钮
    private JCheckBox matB = new JCheckBox("矩阵B");
        //A信息
    private JLabel matRowTipA = new JLabel("行数:");
    private JTextArea matRowA = new JTextArea(1,10);   //行输入
    private JLabel matColTipA = new JLabel("列数:");
    private JTextArea matColA = new JTextArea(1,10);   //列输入
    private JLabel matFacTipA = new JLabel("元素:");
    private JTextArea matTextA = new JTextArea(2,30);   //矩阵输入
    private int matHeightA = 20;    //小组件的纵坐标
    private int matSize = 20;      //小组件的宽度
        //B信息
    private JLabel matRowTipB = new JLabel("行数:");
    private JTextArea matRowB = new JTextArea(1,10);   //行输入
    private JLabel matColTipB = new JLabel("列数:");
    private JTextArea matColB = new JTextArea(1,10);   //列输入
    private JLabel matFacTipB = new JLabel("元素:");
    private JTextArea matTextB = new JTextArea(2,30);   //矩阵输入
    private int matHeightB = 85;    //小组件的纵坐标

    //按钮面板信息
    private String matBtnText[] = {
        "+","-","*","÷","逆","|A|","转置","="
    };
    private JButton matBtns[] = new JButton[matBtnText.length];

    //结果面板信息
    private JTextArea matResult = new JTextArea();

    //矩阵面板初始化
    public void matInit(){
        matrixCalPanel.setLayout(null);//空布局

        //添加面板
        matrixCalPanel.add(matInputPanel);
        matrixCalPanel.add(matBtnPanel);
        matrixCalPanel.add(matResultPanel);

        //输入面板初始化
        matInputPanel.setLayout(null);  //空布局
        matInputPanel.setBounds(0,0,680,140);
        matInputPanel.setBackground(deepGrey);
            //添加各个组件
        matInputPanel.add(matA);
        matInputPanel.add(matB);
        matInputPanel.add(matRowTipA);
        matInputPanel.add(matRowA);
        matInputPanel.add(matColTipA);
        matInputPanel.add(matColA);
        matInputPanel.add(matTextA);
        matInputPanel.add(matFacTipA);
        matInputPanel.add(matRowTipB);
        matInputPanel.add(matRowB);
        matInputPanel.add(matColTipB);
        matInputPanel.add(matColB);
        matInputPanel.add(matTextB);
        matInputPanel.add(matFacTipB);
            //设置各组件位置
        matA.setBounds(0,matHeightA,80,matSize);
        matRowTipA.setBounds(80,matHeightA,40,matSize);
        matRowA.setBounds(120,matHeightA,40,matSize);
        matColTipA.setBounds(160,matHeightA,40,matSize);
        matColA.setBounds(200,matHeightA,40,matSize);
        matFacTipA.setBounds(240,matHeightA,40,matSize);
        matTextA.setBounds(280,10,400,40);

        matB.setBounds(0,matHeightB,80,matSize);
        matRowTipB.setBounds(80,matHeightB,40,matSize);
        matRowB.setBounds(120,matHeightB,40,matSize);
        matColTipB.setBounds(160,matHeightB,40,matSize);
        matColB.setBounds(200,matHeightB,40,matSize);
        matFacTipB.setBounds(240,matHeightB,40,matSize);
        matTextB.setBounds(280,75,400,40);
            //设置各组件其他属性
        matA.setSelected(true);         //默认选中A
        matA.setForeground(Color.WHITE);    //颜色
        matA.setBackground(deepGrey);
        matRowTipA.setForeground(Color.WHITE);
        matColTipA.setForeground(Color.WHITE);
        matFacTipA.setForeground(Color.WHITE);
        matB.setForeground(Color.WHITE);
        matB.setBackground(deepGrey);
        matRowTipB.setForeground(Color.WHITE);
        matColTipB.setForeground(Color.WHITE);
        matFacTipB.setForeground(Color.WHITE);  
        matRowB.setEditable(false);        //初始时各框不可输入
        matRowB.setBackground(Color.GRAY);
        matColB.setEditable(false);
        matColB.setBackground(Color.GRAY);
        matTextB.setEditable(false);
        matTextB.setBackground(Color.GRAY);

        //按钮面板初始化
        matBtnPanel.setLayout(new GridLayout(2,4,0,0));
        matBtnPanel.setBounds(0,140,400,200);
        matBtnPanel.setBackground(deepGrey);
        for (int i = 0; i < matBtnText.length; ++i){
            matBtns[i] = new JButton(matBtnText[i]);
            matBtns[i].setBackground(Color.WHITE);
            matBtnPanel.add(matBtns[i]);    //添加到面板
        }

        //结果面板初始化
        matResultPanel.setBounds(400,140,280,200);
        matResultPanel.add(matResult);
        matResultPanel.setLayout(null);
        matResult.setBounds(0,0,280,200); 
        matResult.setForeground(Color.BLACK);
        matResult.setEditable(false);
    }//matInit

    /**********矩阵计算器结束********************** */

    /**********方程计算器************************* */
    private JPanel equaInputPanel = new JPanel();   //输入面板
    private JPanel equaBtnPanel = new JPanel();     //等号用的按钮
    private JPanel equaResultPanel = new JPanel();  //结果面板

    String[] equaTypeText = new String[] {"一元二次","n元一次"};    //选择计算类型
    JComboBox equaType = new JComboBox(equaTypeText);

    private JLabel equaVarTip = new JLabel("变量数:");
    private JTextArea equaVar = new JTextArea(1,10);    //变量数输入
    private JLabel equaCoeffTip = new JLabel("参数输入:");
    private JTextArea equaCoeff = new JTextArea(3,40);  //参数输入
    private JButton equaCalBtn = new JButton("=");  //结果按键

    private JLabel equaResultTip = new JLabel("计算结果");
    //private JLabel equaLeftLine = new JLabel(); //一条线  
    //private JLabel equaRightLine = new JLabel(); //一条线
    private JPanel equaLeftLine = new JPanel(); //一条线  
    private JPanel equaRightLine = new JPanel(); //一条线
    private JTextArea equaResult = new JTextArea(); //结果显示
    private JTextArea equaResultRightPart = new JTextArea();    //右边版块。左边可以输出原方程
 
    private static int equaHeight = 20;  //用于定位小组件位置 
    private static int equaResultHeight = 25;//
    //方程面板初始化
    public void equaInit(){
        //添加面板
        equationCalPanel.setLayout(null);
        equationCalPanel.add(equaInputPanel);
        equationCalPanel.add(equaBtnPanel);
        equationCalPanel.add(equaResultPanel);

        //输入面板初始化
        equaInputPanel.setBounds(0,0,600,60);
        equaInputPanel.setBackground(deepGrey);
        equaInputPanel.setLayout(null);
            //添加元素
        equaInputPanel.add(equaType);
        equaInputPanel.add(equaVarTip);
        equaInputPanel.add(equaVar);
        equaInputPanel.add(equaCoeffTip);
        equaInputPanel.add(equaCoeff);
            //设置各元素位置
        equaType.setBounds(0,0,105,20);
        equaVarTip.setBounds(0,40,55,20);
        equaVar.setBounds(50,40,50,20);
        equaCoeffTip.setBounds(110,equaHeight,70,20);
        equaCoeff.setBounds(180,0,400,60);
            //其他元素设置
        equaVarTip.setForeground(Color.WHITE);
        equaCoeffTip.setForeground(Color.WHITE);
        equaVar.setBackground(Color.GRAY);

        //等号面板初始化
        equaBtnPanel.add(equaCalBtn);
        equaBtnPanel.setBounds(600,0,80,60);
        equaBtnPanel.setBackground(thickOrange);
        equaCalBtn.setBorderPainted(false);
        equaCalBtn.setForeground(Color.WHITE);
        equaCalBtn.setBackground(thickOrange);
        equaCalBtn.setFont(new Font("苹方",1,30));

        //结果面板初始化
        equaResultPanel.setBounds(0,60,680,300);
        equaResultPanel.setBackground(deepGrey);
        equaResultPanel.setLayout(null);
            //元素添加
        //equaResultPanel.add(equaResult);
        equaResultPanel.add(equaLeftLine);
        equaResultPanel.add(equaResultTip);
        equaResultPanel.add(equaRightLine);
        equaResultPanel.add(equaResult);
        equaResultPanel.add(equaResultRightPart);
            //位置设置
        equaLeftLine.setBounds(0,equaResultHeight+5,290,3);
        equaResultTip.setBounds(310,20,60,20);
        equaRightLine.setBounds(380,equaResultHeight+5,300,3);
        equaResult.setBounds(0,80,340,220);
        equaResultRightPart.setBounds(400,80,280,220);
            //其他设置
        equaLeftLine.setBackground(Color.WHITE);
        equaRightLine.setBackground(Color.WHITE);
        equaResultTip.setForeground(Color.WHITE);
        equaResult.setForeground(Color.WHITE);
        equaResult.setBackground(deepGrey);
        equaResult.setEditable(false);
        equaResultRightPart.setBackground(deepGrey);
        equaResultRightPart.setForeground(Color.WHITE);
        equaResultRightPart.setEditable(false);
    }//equaInit

    /**********方程计算器结束********************** */

    //初始化界面
    public void UIinit(){
        menuInit();
        tabInit();
        progUIInit();
        sciInit();
        matInit();  //矩阵面板初始化
        equaInit(); //方程面板初始化

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点X关闭窗口
        jf.setLocation(300, 200); //初始化时定位
        jf.setSize(700,430);
        jf.setBackground(deepGrey);
        jf.setResizable(false);   //禁止拖曳改变窗口大小
        jf.setVisible(true);  //显示窗口
    }//UIinit

    /***************界面生成结束******************************************************/

    /********************监听********************************************************/
    //所按即所得监听器
    ActionListener sciTextBtnListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            //得到输入文本框的内容
            String text = sciText.getText();
            text+=e.getActionCommand(); //连接数字
            sciText.setText(text);
        }//actionPerformed
    };//class sciTextBtnListener
    ActionListener progTextBtnListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            //得到输入文本框的内容
            String text = progText.getText();
            text+=e.getActionCommand(); //连接数字
            progText.setText(text);
        }//actionPerformed
    };//class progTextBtnListener
        
    String sciMemText = "";
    String sciAnsText = "";
    //科学计算器监听
    public void sciListen(){
        //左边面板
        for (int i = 0;i < 5; ++i){         //sin,cos,tan,lg,ln 显示为 xx(
            sciLeftBtns[i].addActionListener(e->{
                String text = sciText.getText();    //获取文本
                text += e.getActionCommand();
                text += "(";
                sciText.setText(text);
            });
        }
        sciLeftBtns[5].addActionListener(e->{   //x^a
            String text = sciText.getText();    //获取文本
            text += "^";
            sciText.setText(text);
        });
        sciLeftBtns[6].addActionListener(e->{   //n!
            String text = sciText.getText();    //获取文本
            text += "!";
            sciText.setText(text);
        });
        for (int i = 7; i < 12; ++i){   //e,π,(,) 其中第9个，即sqrt，是例外
            if (i != 9){
                sciLeftBtns[i].addActionListener(sciTextBtnListener);
            }
            else{
                sciLeftBtns[i].addActionListener(e->{   //sqrt
                    String text = sciText.getText();    //获取文本
                    text += e.getActionCommand();
                    text += "(";
                    sciText.setText(text);
                });
            }
        }//for
        for (int i = 12; i < 15; ++i){  //sinh,cosh,tanh
            sciLeftBtns[i].addActionListener(e->{   //sqrt
                String text = sciText.getText();    //获取文本
                text += e.getActionCommand();
                text += "(";
                sciText.setText(text);
            });
        }

        //数字面板
        for (int i = 0;i < sciNumBtns.length; ++i){
            if (sciNumBtnText[i].equals("AC")){ //AC键
                sciNumBtns[i].addActionListener(e->{
			        sciText.setText("");  //将 文本域的 值设置为空
                    sciResult.setText("");  //将结果域值置为空
		        });
            }//if
            else if (sciNumBtnText[i].equals("CM")){
                sciNumBtns[i].addActionListener(e->{
			        sciText.setText(sciMemText);  //将 文本域的 值设置为记录好的值
		        });
            }//else if CM
            else if (sciNumBtnText[i].equals("M")){
                sciNumBtns[i].addActionListener(e->{
			        //sciMemText = sciText.getText();  //记录文本域的值
                    sciMemText = sciResult.getText();  //记录结果域的值
                    if (sciMemText.length() > 0){     //不空
                        sciMemText = sciMemText.substring(1);   //去掉等号
                    }
		        });
            }
            else if (sciNumBtnText[i].equals("ANS")) {
                sciNumBtns[i].addActionListener(e->{
                    sciText.setText("ANS");
                });
            }//else if M
            else {
                sciNumBtns[i].addActionListener(sciTextBtnListener);
            }//else
        }//for numbtns

        //右边符号面板
        for (int i = 0;i < 4; ++i){
            sciRightBtns[i].addActionListener(sciTextBtnListener);
        }

        // = 号
        sciRightBtns[4].addActionListener(e->{
            String inputText = sciText.getText().replace("ANS", sciAnsText);
            sciResult.setText("=" + new SciCal(inputText).getResult());
            sciAnsText = sciResult.getText();   //将结果记录到ANS中
            if (sciAnsText.length() > 0) {
                sciAnsText = sciAnsText.substring(1); //去掉等号
            }
        });

    }//sciListen

  
    int radix = 16; //记录进制

    //程序员计算器监听
    public void progListen(){
        //输入状态面板监听
        progBtnHEX.addActionListener(e->{
            radix = 16;
        });
        progBtnDEC.addActionListener(e->{
            radix = 10;
        });
        progBtnOCT.addActionListener(e->{
            radix = 8;
        });
        progBtnBIN.addActionListener(e->{
            radix = 2;
        });

        //按键面板监听
        progBtns[0].addActionListener(e->{      //or
            String text = progText.getText();
            text += "|";
            progText.setText(text);
        });
        progBtns[1].addActionListener(e->{      //xor
            String text = progText.getText();
            text += "^";
            progText.setText(text);
        });
        progBtns[2].addActionListener(e->{      //not
            String text = progText.getText();
            text += "!";
            progText.setText(text);
        });
        progBtns[3].addActionListener(e->{      //and
            String text = progText.getText();
            text += "&";
            progText.setText(text);
        });
        for (int i = 4;i < 8; ++i){ //1,2,3,+
            progBtns[i].addActionListener(progTextBtnListener);
        }

        //HEX,DEC,OCT,BIN
        progBtns[8].addActionListener(e->{  //HEX
            if (progResult.getText().length() > 1){
                progText.setText(progResult.getText().substring(1));
            }
                progResult.setText("=" + new ProgCal().getRadixConvertResult(progText.getText(),radix,16));
        });
        progBtns[9].addActionListener(e->{  //DEC
            if (progResult.getText().length() > 1){
                progText.setText(progResult.getText().substring(1));
            }
            progResult.setText("=" + new ProgCal().getRadixConvertResult(progText.getText(),radix,10));
        });
        progBtns[10].addActionListener(e->{  //OCT
            if (progResult.getText().length() > 1){
                progText.setText(progResult.getText().substring(1));
            }
            progResult.setText("=" + new ProgCal().getRadixConvertResult(progText.getText(),radix,8));
        });
        progBtns[11].addActionListener(e->{  //BIN
            if (progResult.getText().length() > 1){
                progText.setText(progResult.getText().substring(1));
            }
            progResult.setText("=" + new ProgCal().getRadixConvertResult(progText.getText(),radix,2));
        });

        for (int i = 12;i < progBtns.length; ++i){
            if (progBtnText[i].equals("AC")){   //AC
                progBtns[i].addActionListener(e->{
                    progText.setText("");   //清空
                    progResult.setText("");
                });
            }
            else if (progBtnText[i].equals("=")){   //=     //TODO: =未设置 
                    progBtns[i].addActionListener(e->{
                        progResult.setText("=" + new ProgCal().getProgResult(progText.getText(),radix));
                    });
            }
            else{
                progBtns[i].addActionListener(progTextBtnListener);
            }
        }
    }//progListen

    //矩阵计算器监听
    boolean matAChosen = true, matBChosen = false;  //记录是否选中

    public void matListen(){
        //两个选中按钮的监听
        matA.addActionListener(e->{
            matAChosen = !matAChosen;   //按下则状态反转
            if (matAChosen){    //被选中
                matRowA.setEditable(true);
                matRowA.setBackground(Color.WHITE);
                matColA.setEditable(true);
                matColA.setBackground(Color.WHITE);
                matTextA.setEditable(true);
                matTextA.setBackground(Color.WHITE);
            }
            else{ //未被选中，不可输入
                matRowA.setEditable(false);
                matRowA.setBackground(Color.GRAY);
                matColA.setEditable(false);
                matColA.setBackground(Color.GRAY);
                matTextA.setEditable(false);
                matTextA.setBackground(Color.GRAY);
            }
        });
        matB.addActionListener(e->{
            matBChosen = !matBChosen;
            if (matBChosen){    //被选中
                matRowB.setEditable(true);
                matRowB.setBackground(Color.WHITE);
                matColB.setEditable(true);
                matColB.setBackground(Color.WHITE);
                matTextB.setEditable(true);
                matTextB.setBackground(Color.WHITE);
            }
            else{ //未被选中，不可输入
                matRowB.setEditable(false);
                matRowB.setBackground(Color.GRAY);
                matColB.setEditable(false);
                matColB.setBackground(Color.GRAY);
                matTextB.setEditable(false);
                matTextB.setBackground(Color.GRAY);
            }
        });
        //计算按钮监听
        matBtns[0].addActionListener(e->{   //+
            matResult.setText(new MatCal(matRowA.getText(), matColA.getText(), matTextA.getText(), matRowB.getText(),
                    matColB.getText(), matTextB.getText(), "+", matAChosen, matBChosen).getResult());
        });
        matBtns[1].addActionListener(e->{   //-
            matResult.setText(new MatCal(matRowA.getText(), matColA.getText(), matTextA.getText(), matRowB.getText(),
                    matColB.getText(), matTextB.getText(), "-", matAChosen, matBChosen).getResult());
        });
        matBtns[2].addActionListener(e->{   //*
            matResult.setText(new MatCal(matRowA.getText(), matColA.getText(), matTextA.getText(), matRowB.getText(),
                    matColB.getText(), matTextB.getText(), "*", matAChosen, matBChosen).getResult());
        });
        matBtns[3].addActionListener(e->{   //÷
            matResult.setText(new MatCal(matRowA.getText(), matColA.getText(), matTextA.getText(), matRowB.getText(),
                    matColB.getText(), matTextB.getText(), "/", matAChosen, matBChosen).getResult());
        });
        matBtns[4].addActionListener(e->{   //求逆
            matResult.setText(new MatCal(matRowA.getText(), matColA.getText(), matTextA.getText(), matRowB.getText(),
                    matColB.getText(), matTextB.getText(), "Inv", matAChosen, matBChosen).getResult());
        });
        matBtns[5].addActionListener(e->{   //行列式
            matResult.setText(new MatCal(matRowA.getText(), matColA.getText(), matTextA.getText(), matRowB.getText(),
                    matColB.getText(), matTextB.getText(), "||", matAChosen, matBChosen).getResult());
        });
        matBtns[6].addActionListener(e->{   //转置
            matResult.setText(new MatCal(matRowA.getText(), matColA.getText(), matTextA.getText(), matRowB.getText(),
                    matColB.getText(), matTextB.getText(), "Trans", matAChosen, matBChosen).getResult());
        });
        matBtns[7].addActionListener(e->{   //=
            matResult.setText(new MatCal(matRowA.getText(), matColA.getText(), matTextA.getText(), matRowB.getText(),
                    matColB.getText(), matTextB.getText(), "=", matAChosen, matBChosen).getResult());
        });
    }//matListen

    //方程计算器监听
    int equaTypeIndex = 0,dim = 0;
    String equaTypeSelected = "一元二次";
    String equaResultArray[];
    public void equaListen(){
        equaType.addActionListener(e->{ //解方程类型
            equaTypeSelected = (String)equaType.getSelectedItem();
            if (equaTypeSelected == "一元二次") {
                equaVar.setEditable(false);
                equaVar.setBackground(Color.GRAY);
            }
            else {
                equaVar.setEditable(true);
                equaVar.setBackground(Color.WHITE);
            }
        });
        equaCalBtn.addActionListener(e->{   //等号
            if (equaTypeSelected.equals("一元二次")){
                equaResult.setText(new EquaCal().getTwoTimesEquationResult(equaCoeff.getText()));
            }
            else{   //n元一次
                equaResultArray = new EquaCal().gaussEmilation(equaVar.getText(), equaCoeff.getText());
                equaResult.setText("原方程:"+"\n"+equaResultArray[0]);
                equaResultRightPart.setText("解:"+"\n"+equaResultArray[1]);
            }
        });
    }//equaListen

    //菜单监听
    public void menuListen(){
        itemExit.addActionListener(e->{ //退出
            System.exit(0);
        });
        itemSci.addActionListener(e->{  //科学计算器帮助
            HelpFrame shf = new HelpFrame("科学计算器用法","HelpDoc/sciHelp.txt");
        });
        itemProg.addActionListener(e->{  //程序员计算器帮助
            HelpFrame shf = new HelpFrame("程序员计算器用法","HelpDoc/progHelp.txt");
        });
        itemMat.addActionListener(e->{  //矩阵计算器帮助
            HelpFrame shf = new HelpFrame("矩阵计算器用法","HelpDoc/matHelp.txt");
        });
        itemEqua.addActionListener(e->{  //方程计算器帮助
            HelpFrame shf = new HelpFrame("方程计算器用法","HelpDoc/equaHelp.txt");
        });
        itemAbout.addActionListener(e->{  //关于
            HelpFrame shf = new HelpFrame("关于","HelpDoc/about.txt");
        });
    }//menuListen

    //开始监听
    public void listenInit(){
        menuListen();
        sciListen();
        progListen();
        matListen();
        equaListen();
    }//listenInit
    /*********************监听部分结束************************************************ */
}//calculator3