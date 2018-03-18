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

    
    //初始化成两个tab，每块的参数在这里设置
    public void tabInit(){
        JTabbedPane tp = new JTabbedPane();
        tp.add(sciCalPanel);
        tp.add(progCalPanel);
        // 设置tab的标题
        tp.setTitleAt(0, "科学计算器");
        tp.setTitleAt(1, "程序计算器");
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
            sciLeftSymPanel.add(sciLeftBtns[i]);
        }
        sciLeftSymPanel.setBounds(0,50,400,290);    //设置大小及位置
        sciLeftSymPanel.setBackground(deepGrey);

        //数字面板初始化
        sciNumPanel.setLayout(new GridLayout(5,3,0,0));
        for (int i = 0;i < sciNumBtns.length; ++i){
            sciNumBtns[i] = new JButton(sciNumBtnText[i]);
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
            sciRightBtns[i].setFont(new Font("苹方",1,20));
        }
        sciRightSymPanel.setBounds(600,50,80,290);
        sciRightSymPanel.setBackground(thickOrange);
    }//sciInit

    //初始化界面
    public void UIinit(){
        tabInit();
        progUIInit();
        sciInit();

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点X关闭窗口
        jf.setLocation(400, 200); //初始化时定位
        jf.setSize(700,400);
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
		        });
            }//if
            else if (sciNumBtnText[i].equals("CM")){
                sciNumBtns[i].addActionListener(e->{
			        sciText.setText(sciMemText);  //将 文本域的 值设置为记录好的值
		        });
            }//else if CM
            else if (sciNumBtnText[i].equals("M")){
                sciNumBtns[i].addActionListener(e->{
			        sciMemText = sciText.getText();  //记录文本域的值
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
        //TODO:未添加

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
            progText.setText(progResult.getText().substring(1));
            progResult.setText("=" + new ProgCal().getRadixConvertResult(progText.getText(),radix,16));
        });
        progBtns[9].addActionListener(e->{  //DEC
            progText.setText(progResult.getText().substring(1));
            progResult.setText("=" + new ProgCal().getRadixConvertResult(progText.getText(),radix,10));
        });
        progBtns[10].addActionListener(e->{  //OCT
            progText.setText(progResult.getText().substring(1));
            progResult.setText("=" + new ProgCal().getRadixConvertResult(progText.getText(),radix,8));
        });
        progBtns[11].addActionListener(e->{  //BIN
            progText.setText(progResult.getText().substring(1));
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

    //开始监听
    public void listenInit(){
        sciListen();
        progListen();
    }//listenInit
    /*********************监听部分结束************************************************ */
}//calculator3