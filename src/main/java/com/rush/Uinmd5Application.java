package com.rush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class Uinmd5Application {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(Uinmd5Application.class, args);
		System.out.println("================");

//		InsertThread insertThread1 = new InsertThread(100000000,2000000000,"thread-1");
//        Thread thread1 = new Thread(insertThread1);
//        thread1.start();
//        if(!thread1.isAlive()){ //thread1结束
//            InsertThread insertThread2 = new InsertThread(-2000000000,-100000000,"thread-2");
//            Thread thread2 = new Thread(insertThread2);
//            thread2.start();
//        }
        CreateFileThread createFileThread1 = new CreateFileThread(100000000,115000000,"thread-1");
        Thread thread1 = new Thread(createFileThread1);
        thread1.start();
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
}
