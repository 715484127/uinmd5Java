package com.rush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Uinmd5Application {

    //100000000~2000000000 共1900000001
    //-2000000000~-100000000 共1900000001

	public static void main(String[] args) throws SQLException, InterruptedException {
		SpringApplication.run(Uinmd5Application.class, args);
		System.out.println("================");
        long bTime1 = System.currentTimeMillis();
        final int batchSize = 50000000;

//		InsertThread insertThread0 = new InsertThread(100000000,149999999,"thread-0", "00");
//        Thread thread0 = new Thread(insertThread0);
//        thread0.start();

//        InsertThread insertThread1 = new InsertThread(150000000,199999999,"thread-1", "01");
//        Thread thread1 = new Thread(insertThread1);
//        thread1.start();

        Map<Integer,List<Integer>> uinMap = getUinMap();

        for(int i=0;i<80;i++){
            String tableName = "";
            if(i<=9){
                tableName = "0" + String.valueOf(i);
            }else{
                tableName = String.valueOf(i);
            }
            List<Integer> uinRange = uinMap.get(i);
            if(uinRange != null){
                int uinStart = uinRange.get(0);
                int uinEnd = uinRange.get(1);
                System.out.println("|#主线程|#开始插入:" + tableName + "表|#uin范围:" + uinStart + "~" + uinEnd);
                InsertThread insertThread = new InsertThread(uinStart,uinEnd,"thread-"+i, tableName);
                Thread thread = new Thread(insertThread);
                thread.start();
                thread.join();
            }
        }

        long eTime1 = System.currentTimeMillis();
        System.out.println("|#总耗时:"+(eTime1-bTime1));

//        InsertThread insertThread2 = new InsertThread(200000000,249999999,"thread-2", "02");
//        Thread thread2 = new Thread(insertThread2);
//        thread2.start();


//        InsertThread insertThread3 = new InsertThread(250000000,299999999,"thread-3", "03");
//        Thread thread3 = new Thread(insertThread3);
//        thread3.start();
//
//        InsertThread insertThread4 = new InsertThread(300000000,349999999,"thread-4", "04");
//        Thread thread4 = new Thread(insertThread4);
//        thread4.start();




//        if(!thread1.isAlive()){ //thread1结束
//            InsertThread insertThread2 = new InsertThread(-2000000000,-100000000,"thread-2");
//            Thread thread2 = new Thread(insertThread2);
//            thread2.start();
//        }


//        CreateFileThread createFileThread1 = new CreateFileThread(100000000,115000000,"thread-1");
//        Thread thread1 = new Thread(createFileThread1);
//        thread1.start();
	}

    // 初始化
    public static Connection getConn() {
        // 不同的数据库有不同的驱动
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/uin?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false";
        String user = "root";
        String password = "2006wang";

        Connection conn = null;
        try {
            Class.forName(driverName); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //生成uin分段Map
    public static Map<Integer,List<Integer>> getUinMap(){
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

        return uinMap;
    }

}
