package com.caiji;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import java.awt.*;
import javax.swing.*;
public class Calcu extends JFrame {

    private JButton jb0, jb1, jb2, jb3, jb4, jb5, jb6, jb7, jb8, jb9,jb10,
            jb11, jb12, jb13, jb14, jb15, jb16,jb17,jb18,jb19,jb20,jb21,jb22,jb23,
            jb24,jb25,jb26,jb27,jb28,jb29,jb30;
    String s1="",s2="",s3="",s4="",s5="";
    int flag=-1;//符号标志位
    int bFlag=0;//错误处理标志位
    int txtFlag=0;//连续运算标志
    int twoFlag=1;//二进制标志
    int tenFlag=1;//十进制标志
    int faFlag=1;//乘方标志
    int qyFlag=1;//取余标志
    int sixtenFlag=1;//十六进制标志
    int qy=0;
    double nTemp=0;
    double xfa=0;
    private JTextField txt;
    private JTextField txt1;

    public Calcu() {

        super("酒干倘卖无");//标题

        Container c = getContentPane();//定义一个容器
        c.setLayout(null);//自由布局
        /////////////////////////////////按钮区
        jb0 = new JButton("7");
        jb1 = new JButton("8");
        jb2 = new JButton("9");
        jb3 = new JButton("÷");
        jb4 = new JButton("4");
        jb5 = new JButton("5");
        jb6 = new JButton("6");
        jb7 = new JButton("×");
        jb8 = new JButton("1");
        jb9 = new JButton("2");
        jb10 = new JButton("3");
        jb11 = new JButton("－");
        jb12 = new JButton("0");
        jb13 = new JButton(".");
        jb14 = new JButton("+");
        jb15 = new JButton("〓");
        jb16 = new JButton("←");
        jb17 = new JButton("CE");
        jb18 = new JButton("二进制");
        jb19 = new JButton("十进制 ");
        jb20 = new JButton("A");
        jb21 = new JButton("B");
        jb22 = new JButton("C");
        jb23 = new JButton("D");
        jb24 = new JButton("E");
        jb25 = new JButton("F");
        jb26 = new JButton("^");
        jb27 = new JButton("√");
        jb28 = new JButton("%");
        jb29 = new JButton("1/x");
        jb30 = new JButton("十六进制");
        ///////////////////////////////////
        txt = new JTextField();
        txt1 = new JTextField();
        Label lab=new Label("进制转换");
        Label lab1=new Label("author-数媒-12-1-Frb");
        lab.setForeground(Color.red);
        lab1.setForeground(Color.blue);
        txt.setHorizontalAlignment(JTextField.RIGHT);//设置文本框字向右对齐
        txt.setEditable(false);//设置只读
        txt.setText("0");//初始化值
        txt1.setEditable(false);//设置只读
        txt1.setText("");
        jb29.setFont(new java.awt.Font("微软雅黑", 1, 9));
        jb7.setFont(new java.awt.Font("微软雅黑", 1, 14));
        //参数分别为x坐标、y坐标、宽、高
        //////////////////////////////////////////////////按钮监听区
        jb0.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                s1+="7";
                s4+="7";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=1;

            }
        });
        jb1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                s1+="8";
                s4+="8";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=1;

            }
        });
        jb2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                s1+="9";
                s4+="9";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=1;

            }
        });
        jb3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;//设置标志位出错处理，下同
                if(tenFlag==0)return;
                if(sixtenFlag==0)return;
                if(faFlag==0)return;
                if(qyFlag==0)return;
                if(bFlag!=0)
                {
                    double c=Double.parseDouble(s1);//把字符串转化为浮点数
                    switch(flag)//flag的值分别代表加减乘除四种不同的情况
                    {
                        case 0:
                        {
                            nTemp+=c;
                        }
                        break;
                        case 1:
                        {
                            nTemp-=c;
                        }
                        break;
                        case 2:
                        {
                            nTemp*=c;
                        }
                        break;
                        case 3:
                        {
                            nTemp/=c;
                        }
                        break;
                        default:nTemp=c;
                            break;
                    }
                    if(txtFlag>=1)//实现连续运算，
                    {
                        int p=(int)nTemp;//实现整数与浮点数的判别
                        if(nTemp-p==0)
                        {
                            s3=String.valueOf(p);
                        }
                        else
                        {
                            s3=String.valueOf(nTemp);
                        }
                        txt.setText(s3);
                    }
                    s1=s1.substring(0,0);
                    s4+=" ÷ ";
                    txt1.setText(s4);
                    flag=3;
                    bFlag=0;
                    txtFlag++;
                }
            }
        });
        jb4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                s1+="4";
                s4+="4";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=1;
            }
        });
        jb5.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                s1+="5";
                s4+="5";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=1;

            }
        });
        jb6.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                s1+="6";
                s4+="6";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=1;

            }
        });
        jb7.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(sixtenFlag==0)return;
                if(faFlag==0)return;
                if(qyFlag==0)return;
                if(bFlag!=0)
                {
                    double c=Double.parseDouble(s1);
                    switch(flag)
                    {
                        case 0:
                        {
                            nTemp+=c;
                        }
                        break;
                        case 1:
                        {
                            nTemp-=c;
                        }
                        break;
                        case 2:
                        {
                            nTemp*=c;
                        }
                        break;
                        case 3:
                        {
                            nTemp/=c;
                        }
                        break;
                        default:nTemp=c;
                            break;
                    }
                    if(txtFlag>=1)
                    {
                        int p=(int)nTemp;
                        if(nTemp-p==0)
                        {
                            s3=String.valueOf(p);
                        }
                        else
                        {
                            s3=String.valueOf(nTemp);
                        }
                        txt.setText(s3);
                    }
                    s1=s1.substring(0,0);
                    s4+=" × ";
                    txt1.setText(s4);
                    flag=2;
                    bFlag=0;
                    txtFlag++;
                }

            }
        });
        jb8.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                s1+="1";
                s4+="1";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=1;

            }
        });
        jb9.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                s1+="2";
                s4+="2";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=1;

            }
        });
        jb10.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                s1+="3";
                s4+="3";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=1;

            }
        });
        jb11.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(sixtenFlag==0)return;
                if(faFlag==0)return;
                if(qyFlag==0)return;
                if(bFlag!=0)
                {
                    double c=Double.parseDouble(s1);
                    switch(flag)
                    {
                        case 0:
                        {
                            nTemp+=c;
                        }
                        break;
                        case 1:
                        {
                            nTemp-=c;
                        }
                        break;
                        case 2:
                        {
                            nTemp*=c;
                        }
                        break;
                        case 3:
                        {
                            nTemp/=c;
                        }
                        break;
                        default:nTemp=c;
                            break;
                    }
                    if(txtFlag>=1)
                    {
                        int p=(int)nTemp;
                        if(nTemp-p==0)
                        {
                            s3=String.valueOf(p);
                        }
                        else
                        {
                            s3=String.valueOf(nTemp);
                        }
                        txt.setText(s3);
                    }
                    s1=s1.substring(0,0);
                    s4+=" - ";
                    txt1.setText(s4);
                    flag=1;
                    bFlag=0;
                    txtFlag++;
                }

            }
        });
        jb12.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                s1+="0";
                s4+="0";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=1;

            }
        });
        jb13.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(sixtenFlag==0)return;
                if(qyFlag==0)return;
                if(bFlag!=0)
                {
                    s1+=".";
                    s4+=".";
                    txt1.setText(s4);
                    txt.setText(s1);

                }
            }
        });
        jb14.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(sixtenFlag==0)return;
                if(faFlag==0)return;
                if(qyFlag==0)return;
                if(bFlag!=0)
                {
                    double c=Double.parseDouble(s1);
                    switch(flag)
                    {
                        case 0:
                        {
                            nTemp+=c;
                        }
                        break;
                        case 1:
                        {
                            nTemp-=c;
                        }
                        break;
                        case 2:
                        {
                            nTemp*=c;
                        }
                        break;
                        case 3:
                        {
                            nTemp/=c;
                        }
                        break;
                        default:nTemp=c;
                            break;
                    }
                    if(txtFlag>=1)
                    {
                        int p=(int)nTemp;
                        if(nTemp-p==0)
                        {
                            s3=String.valueOf(p);
                        }
                        else
                        {
                            s3=String.valueOf(nTemp);
                        }
                        txt.setText(s3);
                    }
                    s1=s1.substring(0,0);
                    s4+=" + ";
                    txt1.setText(s4);
                    flag=0;
                    bFlag=0;
                    txtFlag++;
                }


            }
        });
        jb15.addActionListener(new ActionListener() { //等号监听区，实现主要计算功能

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(sixtenFlag==0)return;
                if(faFlag==0)//乘方
                {
                    double i=Double.parseDouble(s1);
                    double j=Math.pow(xfa, i);
                    s3=String.valueOf(j);
                    txt.setText(s3);
                    s1=s1.substring(0,0);
                    s4=s4.substring(0,0);
                    faFlag=1;
                    return;

                }
                if(qyFlag==0)//取余
                {
                    int i=Integer.parseInt(s1);
                    int j=qy%i;
                    s3=String.valueOf(j);
                    txt.setText(s3);
                    s1=s1.substring(0,0);
                    s4=s4.substring(0,0);
                    qyFlag=1;
                    return;

                }
                if(bFlag!=0)//判断最后一次运算类型
                {
                    double c=Double.parseDouble(s1);
                    double al=0;
                    switch(flag)
                    {
                        case 0:al=nTemp+c;
                            break;
                        case 1:al=nTemp-c;
                            break;
                        case 2:al=nTemp*c;
                            break;
                        case 3:al=nTemp/c;
                            break;
                    }
                    al=(double)(Math.round(al*100)/100.0);//四舍勿入；
                    int p=(int)al;
                    if(al-p==0)
                    {
                        s3=String.valueOf(p);
                    }
                    else
                    {
                        s3=String.valueOf(al);
                    }
                    txt.setText(s3);
                    s1=s1.substring(0,0);
                    s4=s4.substring(0,0);
                    nTemp=0;
                    flag=-1;
                    bFlag=0;
                    txtFlag=0;
                }

            }
        });
        jb16.addActionListener(new ActionListener() {	//退格键

            @Override
            public void actionPerformed(ActionEvent e) {


                if(s1.length()-1!=0)
                {
                    s1=s1.substring(0,s1.length()-1);
                    s4=s4.substring(0,s4.length()-1);
                    txt.setText(s1);
                    txt1.setText(s4);
                    if(s1.length()-1==0)
                    {
                        s4=s4.substring(0,s4.length()-1);
                    }
                }
                else
                {
                    s1=s1.substring(0,0);
                    txt1.setText(s4);
                    txt.setText("0");
                }

            }
        });
        jb17.addActionListener(new ActionListener() {	//还原键

            @Override
            public void actionPerformed(ActionEvent e) {

                s1=s1.substring(0,0);
                s4=s4.substring(0,0);
                txt.setText("0");
                txt1.setText(s4);
                bFlag=0;
                flag=-1;
                nTemp=0;
                txtFlag=0;
                twoFlag=1;
                tenFlag=1;
                sixtenFlag=1;
                qyFlag=1;
                faFlag=1;
            }
        });
        jb26.addActionListener(new ActionListener() {	//乘方

            @Override
            public void actionPerformed(ActionEvent e) {
                if(s1!="")
                {
                    xfa=Double.parseDouble(s1);
                    s1=s1.substring(0,0);
                    s4+="^";
                    txt1.setText(s4);
                    faFlag=0;
                    bFlag=0;
                }

            }
        });
        jb27.addActionListener(new ActionListener() {	//开方

            @Override
            public void actionPerformed(ActionEvent e) {
                if(s1!="")
                {
                    xfa=Double.parseDouble(s1);
                    double i=Math.sqrt(xfa);
                    s3=String.valueOf(i);
                    txt.setText(s3);
                    s1=s1.substring(0,0);
                    s4=s4.substring(0,0);
                    bFlag=0;
                }

            }
        });
        jb28.addActionListener(new ActionListener() {	//取余

            @Override
            public void actionPerformed(ActionEvent e) {
                if(s1!="")
                {
                    qy=Integer.parseInt(s1);
                    s1=s1.substring(0,0);
                    s4+="%";
                    txt1.setText(s4);
                    qyFlag=0;
                    bFlag=0;
                };

            }
        });
        jb29.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(s1!="")
                {
                    xfa=Double.parseDouble(s1);
                    double i=1/xfa;
                    s3=String.valueOf(i);
                    txt.setText(s3);
                    s1=s1.substring(0,0);
                    s4=s4.substring(0,0);
                    bFlag=0;
                }

            }
        });
        jb20.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(faFlag==0)return;
                if(sixtenFlag==1)return;
                s1+="A";
                s4+="A";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=0;

            }
        });
        jb21.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(faFlag==0)return;
                if(sixtenFlag==1)return;
                s1+="B";
                s4+="B";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=0;

            }
        });
        jb22.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(faFlag==0)return;
                if(sixtenFlag==1)return;
                s1+="C";
                s4+="C";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=0;

            }
        });
        jb23.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(faFlag==0)return;
                if(sixtenFlag==1)return;
                s1+="D";
                s4+="D";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=0;

            }
        });
        jb24.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(faFlag==0)return;
                if(sixtenFlag==1)return;
                s1+="E";
                s4+="E";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=0;

            }
        });
        jb25.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(twoFlag==0)return;
                if(tenFlag==0)return;
                if(faFlag==0)return;
                if(sixtenFlag==1)return;
                s1+="F";
                s4+="F";
                txt.setText(s1);
                txt1.setText(s4);
                bFlag=0;

            }
        });
        ////////////////////////////////////////////
        //下面是实现进制转化监听区，三个标志位巧妙的实现进制转化
        /////////////////////////////////////////////
        jb18.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if(tenFlag==0||sixtenFlag==0)
                {
                    if(tenFlag==0)
                    {
                        int i= Integer.parseInt(s1);
                        s5=Integer.toBinaryString(i);//十进制转二进制
                        txt.setText(s5);
                        tenFlag=1;
                        s1=s1.substring(0,0);
                        s4=s4.substring(0,0);
                    }
                    else
                    {
                        s5=Integer.valueOf(s1,16).toString();//十六进制转十进制
                        int j=Integer.parseInt(s5);
                        s5=Integer.toBinaryString(j);//十进制转二进制
                        txt.setText(s5);
                        sixtenFlag=1;
                        s1=s1.substring(0,0);
                        s4=s4.substring(0,0);
                    }
                }
                else
                {
                    twoFlag=0;
                    s1=s1.substring(0,0);
                    s4=s4.substring(0,0);
                    txt.setText("0");
                    txt1.setText("");
                }

            }
        });
        jb19.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if(twoFlag==0||sixtenFlag==0)
                {
                    if(twoFlag==0)
                    {
                        s5=Integer.valueOf(s1,2).toString();//二进制转十进制
                        txt.setText(s5);
                        twoFlag=1;
                        s1=s1.substring(0,0);
                        s4=s4.substring(0,0);
                    }
                    else
                    {
                        s5=Integer.valueOf(s1,16).toString();//十六进制转二进制
                        txt.setText(s5);
                        sixtenFlag=1;
                        s1=s1.substring(0,0);
                        s4=s4.substring(0,0);
                    }
                }
                else
                {
                    tenFlag=0;
                    s1=s1.substring(0,0);
                    s4=s4.substring(0,0);
                    txt.setText("0");
                    txt1.setText("");
                }

            }
        });
        jb30.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if(tenFlag==0||twoFlag==0)
                {
                    if(tenFlag==0)
                    {
                        int i=Integer.parseInt(s1);
                        s5=Integer.toHexString(i);//十进制转十六进制
                        s5 = s5.toUpperCase();//字符串大写
                        s1=s1.substring(0,0);
                        s1+="0X"+s5;
                        txt.setText(s1);
                        tenFlag=1;
                        s1=s1.substring(0,0);
                        s4=s4.substring(0,0);
                    }
                    else
                    {
                        s5=Integer.valueOf(s1,2).toString();
                        int j=Integer.parseInt(s5);
                        s5=Integer.toHexString(j);
                        s5 = s5.toUpperCase();
                        s1=s1.substring(0,0);
                        s1+="0X"+s5;
                        txt.setText(s1);
                        twoFlag=1;
                        s1=s1.substring(0,0);
                        s4=s4.substring(0,0);
                    }
                }
                else
                {
                    sixtenFlag=0;
                    s1=s1.substring(0,0);
                    s4=s4.substring(0,0);
                    txt.setText("0");
                    txt1.setText("");
                }

            }
        });
        ////////////////////////////////////按钮坐标设置区
        jb0.setBounds(10, 60, 50, 30);
        c.add(jb0);

        jb1.setBounds(70, 60, 50, 30);
        c.add(jb1);

        jb2.setBounds(130, 60, 50, 30);
        c.add(jb2);

        jb3.setBounds(190, 60, 50, 30);
        c.add(jb3);

        jb4.setBounds(10, 100, 50, 30);
        c.add(jb4);

        jb5.setBounds(70, 100, 50, 30);
        c.add(jb5);

        jb6.setBounds(130, 100, 50, 30);
        c.add(jb6);

        jb7.setBounds(190, 100, 50, 30);
        c.add(jb7);

        jb8.setBounds(10, 140,50, 30);
        c.add(jb8);

        jb9.setBounds(70, 140, 50, 30);
        c.add(jb9);
        jb10.setBounds(130, 140, 50, 30);
        c.add(jb10);

        jb11.setBounds(190, 140, 50, 30);
        c.add(jb11);

        jb12.setBounds(10, 180, 110, 30);
        c.add(jb12);

        jb13.setBounds(130, 180, 50, 30);
        c.add(jb13);
        jb14.setBounds(190, 180, 50, 30);
        c.add(jb14);

        jb15.setBounds(190, 220, 50, 70);
        c.add(jb15);

        jb16.setBounds(10, 260, 50, 30);
        c.add(jb16);
        jb17.setBounds(70, 260, 50, 30);
        c.add(jb17);
        jb18.setBounds(10, 300, 80, 30);
        c.add(jb18);
        jb19.setBounds(110, 300, 80, 30);
        c.add(jb19);
        jb20.setBounds(250, 60, 50, 30);
        c.add(jb20);

        jb21.setBounds(250, 100, 50, 30);
        c.add(jb21);

        jb22.setBounds(250, 140, 50, 30);
        c.add(jb22);

        jb23.setBounds(250, 180, 50, 30);
        c.add(jb23);
        jb24.setBounds(250, 220, 50, 30);
        c.add(jb24);
        jb25.setBounds(250, 260, 50, 30);
        c.add(jb25);

        jb26.setBounds(10, 220, 50, 30);
        c.add(jb26);

        jb27.setBounds(70, 220, 50, 30);
        c.add(jb27);
        jb28.setBounds(130, 220, 50, 30);
        c.add(jb28);
        jb29.setBounds(130, 260, 50, 30);
        c.add(jb29);
        jb30.setBounds(210, 300, 90, 30);
        c.add(jb30);
        lab.setBounds(10, 330, 90, 30);
        c.add(lab);
        lab1.setBounds(170, 345, 120, 20);
        c.add(lab1);
        txt.setBounds(10,25,290,30);
        c.add(txt);
        txt1.setBounds(10,2,290,23);
        c.add(txt1);

        setSize(326, 410);//窗口大小设置
        setVisible(true);

        //关闭窗口时，关闭运行程序
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Calcu ca = new Calcu();

    }


}