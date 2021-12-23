package com.company.listen;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

public class Listener extends Thread {
    private Connection conn;
    private PGConnection pgconn;
    private String channel;

    public Listener(Connection conn, String channel) throws SQLException {
        this.conn = conn;
        this.channel = channel;
        this.pgconn = (org.postgresql.PGConnection) conn;
        String sql = "LISTEN "+channel;
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
    }

    public void run() {
        while (true) {
            try {
                PGNotification[] notifications = pgconn.getNotifications();
                if (notifications != null) {
                    for (PGNotification notification : notifications) {
                        System.out.println("Got notification: " + notification.getName() + notification.getParameter());
                    }
                }
                Thread.sleep(500);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
}
