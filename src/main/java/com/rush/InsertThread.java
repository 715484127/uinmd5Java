package com.rush;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by WYP on 2018/8/16.
 */
public class InsertThread implements Runnable {
    public int uinstart;
    public int uinend;
    public String threadName;
    public String tableName;

    public InsertThread(int uinstart, int uinend, String threadName, String tableName) {
        this.uinstart = uinstart;
        this.uinend = uinend;
        this.threadName = threadName;
        this.tableName = tableName;
    }

    @Override
    public void run() {
        System.out.println("|#线程:" + threadName + "|#开始|#计算范围:" + uinstart + "~" + uinend + "|#插入表:" + tableName);
        Connection conn = InsertThread.getConn();

        String sql = "insert into `" + tableName + "` values(?,?)";
        PreparedStatement pstmt;
        try {
            conn.setAutoCommit(false);
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            //开始总计时
            long bTime1 = System.currentTimeMillis();
            final int batchSize = 100000;
            int count = 0;
            for(int i = uinstart; i <= uinend; i++){
                long bTime = System.currentTimeMillis();
                long eTime = 0l;
                String uin = "mm" + String.valueOf(i);
                String uinMd5Str = MD5.getMD5Code(uin);

                try {
                    pstmt.setString(1, uinMd5Str);
                    pstmt.setInt(2, i);
                    pstmt.addBatch();
                    if(++count % batchSize == 0) {
                        pstmt.executeBatch();
                        conn.commit();
                        //关闭分段计时
                        eTime = System.currentTimeMillis();
                        System.out.println("|#线程:" + threadName + "成功插入10W条数据耗时："+(eTime-bTime));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            pstmt.executeBatch();
            conn.commit();
            pstmt.close();
            conn.close();
            //关闭总计时
            long eTime1 = System.currentTimeMillis();
            //输出
            System.out.println("|#线程:" + threadName + "插入5000W数据共耗时："+(eTime1-bTime1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




    public static Connection getConn() {
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/uin?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false&useServerPrepStmts=false&rewriteBatchedStatements=true";
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
