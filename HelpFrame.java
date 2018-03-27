//显示帮助菜单下的各项的相关类
import javax.swing.*;
import java.awt.*;
import java.io.*;

//读取txt文件中的内容
class getInfoFromTxt{
    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
}//getInfoFromTxt

public class HelpFrame extends JFrame{
    public HelpFrame(String title, String fileName){
        this.setSize(400,500);
        this.add(outInfo);
        this.setLayout(null);
        this.setTitle(title);
        this.setVisible(true);
        this.setResizable(false);   //禁止拖曳改变窗口大小
        this.setLocation(1010, 200); //初始化时定位

        outInfo.setBounds(0,0,400,500);
        outInfo.setBackground(new Color(76,76,76));
        outInfo.setForeground(Color.WHITE);
        outInfo.setEditable(false);
        //文本框内容设置
        File file = new File(fileName);
        String content = new getInfoFromTxt().txt2String(file);
        outInfo.setText(content);

    }//HelpFrame
    private JTextArea outInfo = new JTextArea();
}//HelpFrame