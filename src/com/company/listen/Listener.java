package com.company.listen;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.company.events.*;
import com.company.observer.Observable;
import com.company.observer.ObservableDb;
import com.company.observer.Observer;
import com.company.observer.ObserverDb;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

public class Listener extends Thread implements ObservableDb<DbEvent> {
    private Connection conn;
    private PGConnection pgconn;
    private String channel;
    private List<ObserverDb<DbEvent>> observers = new ArrayList<>();

    public Listener(Connection conn, String channel) throws SQLException {
        this.conn = conn;
        this.channel = channel;
        this.pgconn = (org.postgresql.PGConnection) conn;
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
                                notifyObservers(new DbEvent(ChangeEventType.ADDMessage));
                            //}else{
                                notifyObservers(new DbEvent(ChangeEventType.ANYRequest));
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
