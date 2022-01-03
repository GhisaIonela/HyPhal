package com.example.networkgui;

import com.company.events.ChangeEventType;
import com.company.events.DbEvent;
import com.company.observer.ObservableDb;
import com.company.observer.ObserverDb;
import javafx.application.Platform;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Listener extends Thread implements ObservableDb<DbEvent> {
    private Connection conn;
    private PGConnection pgconn;
    private String channel;
    private List<ObserverDb<DbEvent>> observers = new ArrayList<>();

    public Listener(Connection conn, String channel) throws SQLException {
        this.conn = conn;
        this.channel = channel;
        this.pgconn = (PGConnection) conn;
        String sql = "LISTEN "+channel;
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
    }
    public void handleNotification(PGNotification notification){};

    public void run() {
        try {
            while (!conn.isClosed()) {
                try {
                    PGNotification[] notifications = pgconn.getNotifications();
                    if (notifications != null) {
                        for (PGNotification notification : notifications) {
                            //handleNotification(notification);
                            //if(notification.getName().equals("messages")){
                           if( notification.getParameter().contains("message")){
                               notifyObservers(new DbEvent(ChangeEventType.ADDMessage));
                           }else{
                               notifyObservers(new DbEvent(ChangeEventType.ANYRequest));
                           }
//                            Platform.runLater(new Runnable() {
//                                @Override
//                                public void run() {
//                                    notifyObservers(new DbEvent(ChangeEventType.ADDMessage));
//                                    notifyObservers(new DbEvent(ChangeEventType.ANYRequest));
//                                }
//                            });

                               // notifyObservers(new DbEvent(ChangeEventType.ADDMessage));
                            //}else{
                               // notifyObservers(new DbEvent(ChangeEventType.ANYRequest));
                            //}

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


    @Override
    public void addObserver(ObserverDb<DbEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(ObserverDb<DbEvent> e) {

    }

    @Override
    public void notifyObservers(DbEvent dbEvent) {
        observers.stream().forEach(obs->obs.updateFromDb(dbEvent));
    }
}
