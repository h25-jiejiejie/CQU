import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//定义一个点
class point{
    double x;
    double y;
    int type=0;//默认是0,但是规定入是1,出是2
    point(double x,double y){
        this.x=x;
        this.y=y;
    }
}


public class Weiler_Atherton{
   //代表多边形的顶点集合,按照连接顺序进行排列,最后一点连接的第一点
   static List<point> duobianxing=new ArrayList<>();//原来的多边形
   //裁剪窗口的顶点,与上面一样
   static List<point> chuangkou=new ArrayList<>();//裁剪窗口

   static List<point> duobianxing2=new ArrayList<>();//序列3
   static List<point> chuangkou2=new ArrayList<>();//序列4

   //计算交点:
    private List<point> jiaodian(){//返回所有的交点组成的list
        List<point> output=new ArrayList<>();//返回的交点list

        List<point> panduanliwai=new ArrayList<>();//用来测试是出点还是入点的list
        int chang=0;//配合测试出入点的

        for(int i=0;i<duobianxing.size();i++){//使用的是线来遍历窗口
            double x1 =duobianxing.get(i).x;
            double y1=duobianxing.get(i).y;
            double x2=duobianxing.get((i+1)%duobianxing.size()).x;
            double y2=duobianxing.get((i+1)%duobianxing.size()).y;

            for(int j=0;j<chuangkou.size();j++){

                double x3=chuangkou.get(j).x;
                double y3=chuangkou.get(j).y;
                double x4=chuangkou.get((j+1)%chuangkou.size()).x;
                double y4=chuangkou.get((j+1)%chuangkou.size()).y;




                point intersection = this.computeIntersection(x1, y1, x2, y2, x3, y3, x4, y4);
                if (intersection != null) {
                    output.add(intersection);
                }

                point yuan=this.computeIntersection(x1, y1, 0, 0, x3, y3, x4, y4);
                if(yuan!=null){
                    boolean ishas=false;
                    for (point point:panduanliwai){
                        if(point.x== yuan.x && point.y==yuan.y){
                            ishas=true;
                            break;
                        }
                    }
                    if(!ishas){
//                        System.out.println(yuan.x+" "+ yuan.y);
                        panduanliwai.add(yuan);
                    }

                }
            }
            //初始点在里里面就是出点,是先出后入
//            System.out.println(panduanliwai.size());
            if(panduanliwai.size()%2!=0){
//                System.out.println("666");
                if(output.size()-chang>0) {
                    if((output.size()-chang)%2==1){//交点是奇数个
                        while(output.size()-chang>0){//当还有点
                            if((output.size()-chang)%2==1){//奇数点是出点
                                output.get(chang).type=2;//出点定义为2
//                                System.out.println("666");
                                chang++;
                                continue;
                            }
                            else {
                                output.get(chang).type=1;//偶数个就是人点
                                chang++;
                                continue;
                            }
                        }
                    }
                    else{//交点是偶数个,出进出进,偶数个是出点,奇数个是进点
                        while(output.size()-chang>0){//当还有点
                            if((output.size()-chang)%2==0){//偶数点是出点
                                output.get(chang).type=2;//出点定义为2
                                chang++;
                                continue;
                            }
                            else {
                                output.get(chang).type=1;//奇数个就是人点
                                chang++;
                                continue;
                            }
                        }
                    }
                }
            }
            else{//在外面
//                System.out.println("666");
                if(output.size()-chang>0){//确定有点
                    if((output.size()-chang)%2==1){//有奇数个交点(进出进)
                        while (output.size()-chang>0){//当还有点
                            if((output.size()-chang)%2==1){//当是奇数时是进点
                                output.get(chang).type=1;
                                chang++;
                                continue;
                            }
                            else{//偶数点是出点
                                output.get(chang).type=2;
                                chang++;
                                continue;
                            }
                        }
                    }
                    else {//有偶数个交点(4321)
                        while((output.size()-chang)>0){
                            if((output.size()-chang)%2==1){//奇数是出点
                                output.get(chang).type=2;
                                chang++;
                                continue;
                            }
                           else {//偶数个是进点
                               output.get(chang).type=1;
                               chang++;
                               continue;
                            }
                        }
                    }
                }
            }
            panduanliwai.clear();//清除所有的在判断里外元素
        }

        return output;
    }

    //计算交点方法
    private  point computeIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        // 计算两条线段的交点
        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        if (denominator == 0) {
            // 两线段平行，没有交点
            return null;
        }

        double px = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / denominator;
        double py = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / denominator;

        if (px >= Math.min(x1, x2) && px <= Math.max(x1, x2) &&
                px >= Math.min(x3, x4) && px <= Math.max(x3, x4) &&
                py >= Math.min(y1, y2) && py <= Math.max(y1, y2) &&
                py >= Math.min(y3, y4) && py <= Math.max(y3, y4)) {
            // 交点在两条线段的范围内
            return new point( px,  py);
        } else {
            // 交点不在两条线段的范围内
            return null;
        }
    }

    private void jiaodianpaixu(){
        List<point> input=jiaodian();//执行交点操作
        //序列3
        for(int i=0;i<duobianxing.size();i++){
            double x1 =duobianxing.get(i).x;
            double y1=duobianxing.get(i).y;
            double x2=duobianxing.get((i+1)%duobianxing.size()).x;
            double y2=duobianxing.get((i+1)%duobianxing.size()).y;

            duobianxing2.add(duobianxing.get(i));
            List<point> savelist=new ArrayList<>();

            for(int j=0;j<input.size();j++){

                double x=input.get(j).x;
                double y=input.get(j).y;
                if(isPointOnLine(x1,y1,x2,y2,x,y)){//在直线上,先保存
                    savelist.add(input.get(j));
//                    System.out.println(input.get(j).x+" "+input.get(j).y);
                }

            }


            while(savelist.size()>0){
                double max=0;
                int save=-1;
                for(int z=0;z<savelist.size();z++){
                    double x=savelist.get(z).x;
                    double y=savelist.get(z).y;
                    double q1=Math.pow((y1-y),2);
                    double q2=Math.pow((x1-x),2);
                    double q=q1+q2;
                    if(q>max){
                        save=z;
                    }
                }
                if(save>=0){
                    for(int a=0;a<duobianxing2.size();a++){
                        if(duobianxing2.get(a).x==x1 && duobianxing2.get(a).y==y1){
                            if(a==duobianxing2.size()-1){
                                duobianxing2.add(savelist.get(save));
                            }
                            else  {
                                duobianxing2.add(a+1,savelist.get(save));
                            }
                            savelist.remove(save);
                        }
                    }

                }
            }

        }

        //序列4
        for(int i=0;i<chuangkou.size();i++){
            double x1 =chuangkou.get(i).x;
            double y1=chuangkou.get(i).y;
            double x2=chuangkou.get((i+1)%chuangkou.size()).x;
            double y2=chuangkou.get((i+1)%chuangkou.size()).y;

            chuangkou2.add(chuangkou.get(i));
            List<point> savelist=new ArrayList<>();

            for(int j=0;j<input.size();j++){//8

                double x=input.get(j).x;
                double y=input.get(j).y;

                if(isPointOnLine(x1,y1,x2,y2,x,y)){//在直线上,先保存
                    savelist.add(input.get(j));
                }
            }
//            System.out.println("666");4
            while(savelist.size()>0){
                double max=0;
                int save=-1;
                for(int z=0;z<savelist.size();z++){
                    double x=savelist.get(z).x;
                    double y=savelist.get(z).y;
                    double q1=Math.pow((y1-y),2);
                    double q2=Math.pow((x1-x),2);
                    double q=q1+q2;
                    if(q>max){
                        save=z;
                    }
                }
                if(save>=0){
                    for(int a=0;a<chuangkou2.size();a++){
                        if(chuangkou2.get(a).x==x1 && chuangkou2.get(a).y==y1){
                            chuangkou2.add(a+1,savelist.get(save));
                            savelist.remove(save);

                        }
                    }

                }
            }
        }
    }

    private void displayLine(List<List<point>> E){

        JFrame frame = new JFrame("多边形切割");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //先画窗口
                g.setColor(Color.BLACK);
                for(int i=0;i<chuangkou.size();i++){
                    g.drawLine((int)chuangkou.get(i).x,(int)chuangkou.get(i).y,(int)chuangkou.get((i+1)%chuangkou.size()).x,(int)chuangkou.get((i+1)%duobianxing.size()).y);
                }
                //直接画裁剪后的图形,加上窗口
                g.setColor(Color.RED);//裁剪后的多边形是红色的
//                g.drawLine(x1,y1,x2,y2);
                for (int i = 0; i < E.size(); i++) {
                    List<point> Q = E.get(i);
                    for (int a = 0; a < Q.size(); a++) {
                        int x1 = (int) Q.get(a).x;
                        int y1 = (int) Q.get(a).y;
                        int x2 = (int) Q.get((a + 1) % Q.size()).x;
                        int y2 = (int) Q.get((a + 1) % Q.size()).y;
//                System.out.println(x1+" "+y1+" "+x2+" "+y2);
//                displayLine(x1,y1,x2,y2);
                        g.drawLine(x1,y1,x2,y2);
                    }
                }

            }
        });

        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    public void huaxian(){
        List<List<point>> get = getline();
        displayLine(get);
    }

    private List<List<point>> getline(){
        jiaodianpaixu();
        List<List<point>> output=new ArrayList<>();

        List<point> Q=new ArrayList<>();
        point save=null;//保存点

        int index=0;
        while(index<duobianxing2.size() &&duobianxing2.get(index).type!=1) {//找入点,会有没找到的情况
            index=(index+1)%duobianxing2.size();
            if(index==0)break;
        }
//                System.out.println("666");
        if(duobianxing2.get(index).type==1){//如果有入点才进行操作
            duobianxing2.get(index).type=0;//删除入点标记
            save=duobianxing2.get(index);//初始点记录
            Q.add(duobianxing2.get(index));//添加到Q中

            //接下去找出点
            int i=(index+1)%duobianxing2.size();
            while(i!=index){
                while(duobianxing2.get(i).type!=2 && i!=index){//顶点不是出点,且没回到找到save点的位置
                    Q.add(duobianxing2.get(i));//添加到Q中
                    i=(i+1)%duobianxing2.size();
                }
                //找到出点(有进必有出不用怀疑),找到序列4对应的顶点
                int j=0;
                while(!chuangkou2.get(j).equals(duobianxing2.get(i))){
                    j=(j+1)%chuangkou2.size();
                }
                //找到对应位置后,如果顶点不是入点添加到Q,当点等于SAVE时也会退出
                while(chuangkou2.get(j).type!=1 && (!chuangkou2.get(j).equals(save))){
                    Q.add(chuangkou2.get(j));
                    j=(j+1)%chuangkou2.size();
                }
                //如果是回到了save点
                if(chuangkou2.get(j).equals(save)){
                    //画线
                    List<point> QCopy=new ArrayList<>();
                    QCopy.addAll(Q);
                    output.add(QCopy);
                    for(int a=0;a<Q.size();a++){
//                                System.out.println(Q.get(a).x+" "+Q.get(a).y);
                        int x1=(int)Q.get(a).x;
                        int y1=(int)Q.get(a).y;
                        int x2=(int)Q.get((a+1)%Q.size()).x;
                        int y2=(int)Q.get((a+1)%Q.size()).y;
//                        System.out.println(x1+" "+y1+" "+x2+" "+y2);
//                        g.drawLine(x1,y1,x2,y2);
                    }
                    Q.clear();
                    //找下一个入点接着来
                    if(index==duobianxing2.size()-1)break;//如果已经找完了退出
                    while(index<duobianxing2.size() && duobianxing2.get(index).type!=1){//不是入点,并且序列4还没遍历完
                        index=(index+1)%duobianxing2.size();
                        if(index==0)break;
                    }
                    if(index==0)break;//遍历完已经没有入点退出
                    else{//有入点
                        save=duobianxing2.get(index);
                        continue;
                    }
                }
                //如果不是save点,则是入点
                chuangkou2.get(j).type=0;
                Q.add(chuangkou2.get(j));//添加到Q中

                //找到序列3对应的I位置
                while(!duobianxing2.get(i).equals(chuangkou2.get(j))){
                    i=(i+1)%duobianxing2.size();
                }
                i=(i+1)%duobianxing2.size();//从下一点找出点
            }
        }

        return output;
    }

    //判断点是不是在直线上
    private static boolean isPointOnLine(double x1, double y1, double x2, double y2, double x, double y) {
        // 使用足够小的误差范围来检查
        double epsilon = 1e-6;

        if(Math.abs(x2-x1)<epsilon){
            return Math.abs(x-x1)<epsilon;
        }
        else {
            // 计算斜率
            double m = (y2 - y1) / (x2 - x1);
            // 计算截距
            double b = y1 - m * x1;
            // 计算点 (x, y) 对应的直线上的 y 值
            double expectedY = m * x + b;
            return Math.abs(y - expectedY) < epsilon;
        }

    }

    /*
    窗口合多边形分别分别输入
     */

    public static void main(String[] args) {
        duobianxing.add(new point(0,0));
        duobianxing.add(new point(100,0));
        duobianxing.add(new point(100,100));
        duobianxing.add(new point(130,320));
        duobianxing.add(new point(0,62.56));
        chuangkou.add(new point(50,50));
        chuangkou.add(new point(150,50));
        chuangkou.add(new point(150,150));
        chuangkou.add(new point(125,300));
        chuangkou.add(new point(50,150));
        Weiler_Atherton test1=new Weiler_Atherton();
        test1.huaxian();
    }
}
