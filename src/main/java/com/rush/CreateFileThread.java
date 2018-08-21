package com.rush;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WYP on 2018/8/16.
 */
public class CreateFileThread implements Runnable {
    public int uinstart;
    public int uinend;
    public String threadName;

    public CreateFileThread(int uinstart, int uinend, String threadName) {
        this.uinstart = uinstart;
        this.uinend = uinend;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        System.out.println("|#线程:" + threadName + "|#开始|#计算范围:" + uinstart + "~" + uinend);
        //开始总计时
        long bTime1 = System.currentTimeMillis();
        for(int i = uinstart; i <= uinend; i++) {
            System.out.println("|#线程:" + threadName + "|#写入uin:" + i);
            String uin = "mm" + String.valueOf(i);
            String uinMd5Str = MD5.getMD5Code(uin);
            String md5Prefix = uinMd5Str.substring(0,2);
            String md5FileName = md5Prefix + ".txt";
            String md5FilePath = "D:\\MD5\\" + md5FileName;
            String md5UinStr = uinMd5Str + "," + String.valueOf(i) + "\r\n";
            FileChannel channel = null;
            FileOutputStream outputStream = null;
            try{
                File file = new File(md5FilePath);
                outputStream = new FileOutputStream(file,true);
                channel = outputStream.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put(md5UinStr.getBytes());
                buffer.flip();     //此处必须要调用buffer的flip方法
                channel.write(buffer);
                channel.close();
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if(channel != null){
                        channel.close();
                    }
                    if(outputStream != null){
                        outputStream.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //关闭总计时
        long eTime1 = System.currentTimeMillis();
        //输出
        System.out.println("|#线程:" + threadName + "写文件1500W数据共耗时："+(eTime1-bTime1));

    }


    public static void main(String[] args){
//        String uinMd5Str = "1F722FF4851F14DBF4546EB428349D61";
//        String md5Prefix = uinMd5Str.substring(0,2);
//        System.out.println(md5Prefix);
//        int i = -2000000000;
//        System.out.println(String.valueOf(i));

        Map<Integer,List<Integer>> uinMap = new HashMap<Integer,List<Integer>>();
        int count = 0; //计数
        int minUin = 100000000;
        int maxUin = 2000000000;

        while(true){
            int uinStart = 0;
            int uinEnd = 0;
            List<Integer> uinList = new ArrayList<Integer>();
            if(minUin == maxUin){
                uinStart = minUin;
                uinEnd = maxUin;
            }else{
                uinStart = minUin;
                uinEnd = uinStart + 49999999;
            }
            uinList.add(uinStart);
            uinList.add(uinEnd);
            uinMap.put(count,uinList);
            count++;
            minUin = uinEnd + 1;
            if(minUin > maxUin){
                break;
            }
        }
        System.out.println("=============");

        int minUin_minus = -2000000000;
        int maxUin_minus = -100000000;
        while(true){
            int uinStart = 0;
            int uinEnd = 0;
            List<Integer> uinList = new ArrayList<Integer>();
            if(minUin_minus == maxUin_minus){
                uinStart = minUin_minus;
                uinEnd = maxUin_minus;
            }else{
                uinStart = minUin_minus;
                uinEnd = uinStart + 49999999;
            }
            uinList.add(uinStart);
            uinList.add(uinEnd);
            uinMap.put(count,uinList);
            count++;
            minUin_minus = uinEnd + 1;
            if(minUin_minus > maxUin_minus){
                break;
            }
        }

        System.out.println("=============");
        uinMap.get(80);
        System.out.println("=============");
    }


}
