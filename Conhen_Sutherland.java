package photoCute;


import javax.swing.*;
import java.awt.*;

public class Conhen_Sutherland {
    private static final int LEFT=1;
    private static final int RIGHT=2;
    private static final int BOTTOM=4;
    private static final int TOP=8;

    private int XL;
    private int XR;
    private int YB;
    private int YT;
    int x,y;

    private int encode(float x,float y){
        int c=0;
        if(x<XL)c|=LEFT;
        if(x>XR)c|=RIGHT;
        if(y<YB)c|=BOTTOM;
        if(y>YT)c|=TOP;
        return c;
    }

    public void CS_LineClip(int x1,int y1,int x2,int y2,int XL,int XR,int YB,int YT){
        int code1,code2,code;

        code1=encode(x1,y1);
        code2=encode(x2,y2);
        while(code1!=0||code2!=0){
            if((code1&code2)!=0)return;
            if(code1!=0)code=code1;
            else code=code2;

            if((LEFT&code)!=0){
                x=XL;
                y=y1+(y2-y1)*(XL-x1)/(x2-x1);
            } else if ((RIGHT&code)!=0) {
                x=XR;
                y=y1+(y2-y1)*(XR-x1)/(x2-x1);
            } else if ((BOTTOM&code)!=0) {
                y=YB;
                x=x1+(x2-x1)*(YB-y1)/(y2-y1);
            } else if ((TOP&code)!=0) {
                y=YT;
                x=x1+(x2-x1)*(YT-y1)/(y2-y1);
            }

            if (code==code1){
                x1=x;
                y1=y;
                code1=encode(x,y);
            }
            else{
                x2=x;
                y2=y;
                code2=encode(x,y);
            }
        }
        displayLine(x1,y1,x2,y2);
    }

    private void displayLine(int x1, int y1, int x2, int y2) {
        // 创建一个窗口并绘制线段
        JFrame frame = new JFrame("直线切割");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // 绘制裁剪窗口
                g.drawRect(XL, YB, XR - XL, YT - YB);

                // 绘制原始线段
                g.drawLine(x1, y1, x2, y2);

                // 绘制裁剪后的线段
                g.setColor(Color.RED); // 设置颜色为红色
                g.drawLine(x1, y1, x2, y2);
            }
        });

        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Conhen_Sutherland cs = new Conhen_Sutherland();
        cs.XL = 100; // 设置裁剪窗口左边界
        cs.XR = 300; // 设置裁剪窗口右边界
        cs.YB = 100; // 设置裁剪窗口下边界
        cs.YT = 300; // 设置裁剪窗口上边界

        //（线一点x1,y1）（线第二点x2,y2）
        cs.CS_LineClip(0, 0, 263, 160, cs.XL, cs.XR, cs.YB, cs.YT);
    }
}
