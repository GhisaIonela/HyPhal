package com.company.listen;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import com.company.events.*;
import com.company.observer.Observer;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

public class Listener extends Thread {
    private Connection conn;
    private PGConnection pgconn;
    private String channel;
    private Observer<MessageChangeEvent> messageObserver;
    private Observer<RequestChangeEvent> requestChangeEventObserver;

    public Listener(Connection conn, String channel) throws SQLException {
        this.conn = conn;
        this.channel = channel;
        this.pgconn = (PGConnection) conn;
        String sql = "LISTEN "+channel;
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
    }

    public void setMessageObserver(Observer<MessageChangeEvent> messageObserver) {
        this.messageObserver = messageObserver;
    }

    public void setRequestChangeEventObserver(Observer<RequestChangeEvent> requestChangeEventObserver) {
        this.requestChangeEventObserver = requestChangeEventObserver;
    }

    private void notifyMessageObserver(MessageChangeEvent messageChangeEvent){
        messageObserver.update(messageChangeEvent);
    }

    private void notifyRequestObserver(RequestChangeEvent requestChangeEvent){
        requestChangeEventObserver.update(requestChangeEvent);
    }

    public void run() {
        try {
            while (!conn.isClosed()) {
                try {
                    PGNotification[] notifications = pgconn.getNotifications();
                    if (notifications != null) {
                        for (PGNotification notification : notifications) {

                            if( notification.getParameter().contains("message")){
                                notifyMessageObserver(new MessageChangeEvent(ChangeEventType.ADDMessage));
                            }else{
                                if(notification.getParameter().contains("accepted")){
                                    notifyMessageObserver(new MessageChangeEvent(ChangeEventType.ACCEPTINGListener));
                                }else if(notification.getParameter().equals("")){
                                    notifyMessageObserver(new MessageChangeEvent(ChangeEventType.UNFRIENDListener));
                                }
                                notifyRequestObserver(new RequestChangeEvent(ChangeEventType.ANYRequest));
                            }
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
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
