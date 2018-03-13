//计算器 ver 0.1
//2018/3
//ZJR

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;

public class calculator extends JFrame{
    private double num; //输入及运算的结果 即显示在面板上的数字
    public static void main(String args[]){
        try {
            new calculator();
		} catch (Exception e) {
			System.out.println("程序异常终止");
			System.exit(0);   //退出虚拟机
		}
    }
    //构造函数
    calculator(){
        UIinit();
        controlListen();//C、CE、Back监听
        numListen();    //1~9数字监听
        zeroListen();   //0监听
        symbolListen(); //+ - * /监听
        resultListen(); //=监听
    } //calculator

    /***************界面生成*********************/
    private JFrame jf=new JFrame("Calculator"); //窗口
	private JPanel centerPanel=new JPanel();    //中间面板
	private JPanel bottomPanel=new JPanel();    //底部面板
	
	//中间三个按钮
	private JButton Backbtn=new JButton("Back");
	private JButton CEbtn=new JButton("CE");
	private JButton Cbtn=new JButton("C");
	
	//初始化剩余功能键
	String[] nums={"7","8","9","+","4","5","6","-","1","2","3","*","0",".","=","/"};
	private JButton btn7=new JButton(nums[0]);
	private JButton btn8=new JButton(nums[1]);
	private JButton btn9=new JButton(nums[2]);
	private JButton btnAdd=new JButton(nums[3]);
	private JButton btn4=new JButton(nums[4]);
	private JButton btn5=new JButton(nums[5]);
	private JButton btn6=new JButton(nums[6]);
	private JButton btnMimus=new JButton(nums[7]);
	private JButton btn1=new JButton(nums[8]);
	private JButton btn2=new JButton(nums[9]);
	private JButton btn3=new JButton(nums[10]);
	private JButton btnMultipus=new JButton(nums[11]);
	private JButton btn0=new JButton(nums[12]);
	private JButton btnDot=new JButton(nums[13]);
	private JButton btnResult=new JButton(nums[14]);
	private JButton btnDivide=new JButton(nums[15]);

    //单行输入文本框
	private JTextField txt=new JTextField(15);
	private List<String>lists=new ArrayList<String>(); //用来记录用户输入的数字和操作符
	
    void UIinit(){
        //使用网格布局方式
		bottomPanel.setLayout(new GridLayout(4,4,3,3)); //左右上下间隔是3
		//将功能键添加到底部面板中
		bottomPanel.add(btn7);
		bottomPanel.add(btn8);
		bottomPanel.add(btn9);
		bottomPanel.add(btnAdd);
		bottomPanel.add(btn4);
		bottomPanel.add(btn5);
		bottomPanel.add(btn6);
		bottomPanel.add(btnMimus);
		bottomPanel.add(btn1);
		bottomPanel.add(btn2);
		bottomPanel.add(btn3);
		bottomPanel.add(btnMultipus);
		bottomPanel.add(btn0);
		bottomPanel.add(btnDot);
		bottomPanel.add(btnResult);
		bottomPanel.add(btnDivide);
		
		//将中间的三个按钮添加到中间面板
		centerPanel.add(Backbtn);
		centerPanel.add(CEbtn);
		centerPanel.add(Cbtn);
		
		jf.add(txt,BorderLayout.NORTH);   //将单行文本框添加到窗口的 北部
		jf.add(centerPanel);              //将中间面板添加到窗口中间（默认是中间）
		jf.add(bottomPanel,BorderLayout.SOUTH);  //将底部面板添加到窗口的南部
		

        //自定义窗口的图标
        ImageIcon image=new ImageIcon("image/img.jpg"); //图片位置
        image.setImage(image.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
        jf.setIconImage(image.getImage());
        //设置UI的风格为Nimbus
        //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        //更新f窗口内所有组件的UI
        SwingUtilities.updateComponentTreeUI(jf.getContentPane());
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点X关闭窗口
        jf.setLocation(400, 200); //初始化时定位
        jf.setResizable(false);   //禁止拖曳改变窗口大小
        jf.pack();               //让窗口的大小自适应
        jf.setVisible(true);  //显示窗口

    }//UIinit
    /**********界面生成结束************************* */

    /***********C、CE、Back按钮监听****************** */
    public void controlListen(){
        //为C按钮添加事件监听
		Cbtn.addActionListener(e->{
			lists.clear();    //将集合中的数据清零
			txt.setText("");  //将 文本域的 值设置为空
		});
		//为Back按钮添加事件监听         使用Lamdba表达式
		Backbtn.addActionListener(e->{
			String text=txt.getText(); //得到输入框文本
			if("".equals(text) || text.length()==1){ 
				txt.setText(""); //如果是空文本或者文本长度为1，直接设为空
				return;
			}
			if(text.length()>1){ //如果文本的长度大于1就要向前截取
				text=text.substring(0,text.length()-1);
				txt.setText(text);
			}
		});
		
		//为CE按钮添加事件监听
		CEbtn.addActionListener(e->{
			//得到输入文本框
			String text=txt.getText();
			if("".equals(text)){
				return;
			}
			if("+".equals(text) || "-".equals(text) || "*".equals(text) || "/".equals(text)){
				//表示要把+ - * /清除,所以要把集合中的第一个数也移除集合（因为用户可能点了+的时候，就点CE键，此时如果再点数字键就会出现问题）
				lists.remove(0);
			}
			txt.setText("");  //把文本直接清掉
		});
    }//controlListen
    /***********C、CE、Back按钮监听结束*************** */

    /***********1~9监听******************************/
    public void numListen(){
        ActionListener numBtnListener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//得到输入文本框的内容
				String text=txt.getText();
				//判断有没有这句话 （0不能做分母，请清零后重试）,如果有 ，当点击数字按钮的时候要它消失
				if("除数不能为0".equals(text)){
					txt.setText(e.getActionCommand());
					return;
				}
				//如果第一个数是0，就不能输入其他数字了，只能输入小数点了
				if("0".equals(text)){
					return;
				}
				if("".equals(text)){  //当文本域中没数据的时候，把当前点击的数字显示上去
					txt.setText(e.getActionCommand());
				}else{
					//如果当前输入框有数据，并且是操作符时，记录下该操作符
					if(text.equals("+") || text.equals("-") || text.equals("*") || text.equals("/")){
						lists.add(text);  //将操作符添加到集合中
						txt.setText("");
						text=""; //将得到的文本符空，也就是将+或-或 *或/赋空值
					}
					text+=e.getActionCommand();
					txt.setText(text);
				}//else
			}//actionPerformed
		};//class numBtnListener
		//为数字注册 监听器
		btn1.addActionListener(numBtnListener);
		btn2.addActionListener(numBtnListener);
		btn3.addActionListener(numBtnListener);
		btn4.addActionListener(numBtnListener);
		btn5.addActionListener(numBtnListener);
		btn6.addActionListener(numBtnListener);
		btn7.addActionListener(numBtnListener);
		btn8.addActionListener(numBtnListener);
		btn9.addActionListener(numBtnListener);
    }//numListen
    /***********1~9监听结束************************* */
    
    //0监听
    public void zeroListen(){
        btn0.addActionListener(e->{
			//得到输入文本框
			String text=txt.getText();
			if("0".equals(text)){  //如果第一个数是0，不能再出现0了
				return;
			}
			//如果出现操作符,就记录下来
			if("+".equals(text) || "-".equals(text) || "*".equals(text) || "/".equals(text)){
				lists.add(text);
				text="";  //把操作符情况
			}
			text+=e.getActionCommand();
			txt.setText(text);
		});
    }//zeroListen

    //.监听
    public void pointListen(){
        btnDot.addActionListener(e->{
			//得到输入文本框
			String text=txt.getText();
			if("".equals(text)){
				return;
			}
			//判断文本是否为+ - * / .
			if("+".equals(text) || "-".equals(text) || "*".equals(text) || "/".equals(text)){
				return;
			}
			//如果该数字后面已经有小数点了，那么就不能连续出现两次小数点了
			if(text.contains(".")){
				return;
			}
			text+=e.getActionCommand();
			txt.setText(text); //设置进去
		});
    }

    /**********  + - * /监听 *****************/
    public void plusListen(){
        btnDot.addActionListener(e->{
			//得到输入文本框
			String text=txt.getText();
			if("".equals(text)){
				return;
			}
			//判断文本是否为+ - * / .
			if("+".equals(text) || "-".equals(text) || "*".equals(text) || "/".equals(text)){
				return;
			}
			//如果该数字后面已经有小数点了，那么就不能连续出现两次小数点了
			if(text.contains(".")){
				return;
			}
			text+=e.getActionCommand();
			txt.setText(text); //设置进去
		});
    }//plusListen

    /*********** + - * /监听 ******************/
    public void symbolListen(){
        ActionListener operationBtnListener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//获取输入文本框内的内容
				String text=txt.getText();
				if("".equals(text)){
					return;
				}
				if("+".equals(text) || "-".equals(text) || "*".equals(text) || "/".equals(text)){
					return;
				}
				//将内容添加到集合中
				lists.add(text);
				//将输入框清空
				txt.setText("");
				//把按钮上面的字显示进去
				txt.setText(e.getActionCommand());
			}
		};
		//为* - + /注册监听器
		btnAdd.addActionListener(operationBtnListener);
		btnMimus.addActionListener(operationBtnListener);
		btnMultipus.addActionListener(operationBtnListener);
		btnDivide.addActionListener(operationBtnListener);
    }//symbolListen
    /*********** + - * /监听 ******************/

    //=监听（带结果输出）
    public void resultListen(){
        ActionListener resultBtnListener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//判断集合中的数据
				if(lists.isEmpty()){
					return;
				}
				//获取文本输入框的内容
				String text=txt.getText();
				if("".equals(txt) || "+".equals(text) || "-".equals(text) || "*".equals(text) || "/".equals(text)){
					return;
				}
				//将其添加到集合中
				lists.add(text);
				if(lists.size()<3){
					return;
				}
				String one=lists.get(0);  //得到集合中的第一个数
				String two=lists.get(1); //得到集合中的第二个数
				String three=lists.get(2); //得到集合中的第三个数
				switch(two){
				case "+":
					double i=Double.parseDouble(one);
					double j=Double.parseDouble(three);
					txt.setText((i+j)+""); //显示结果
					break;
				case "-":
					double x=Double.parseDouble(one);
					double y=Double.parseDouble(three);
					txt.setText((x-y)+""); //显示结果
					break;
					
				case "*":
					double a=Double.parseDouble(one);
					double b=Double.parseDouble(three);
					txt.setText((a*b)+"");
					break;
				case "/":
					double k=Double.parseDouble(one);
					double h=Double.parseDouble(three);
					if(h==0){
						txt.setText("除数不能为0");
						lists.clear();
						return;
					}
					txt.setText((k/h)+"");
					break;
				}
				//将集合中的数据清空
				lists.clear();
			}//actionPerformed
		};
        btnResult.addActionListener(resultBtnListener);
    }//resultListen
}//class calculator