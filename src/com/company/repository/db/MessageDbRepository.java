package com.company.repository.db;

import com.company.domain.Message;
import com.company.domain.User;
import com.company.repository.Repository;
import com.company.utils.Constants;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDbRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;

    public MessageDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message findOne(Long id) {
        String sql ="select msg.id as id_message, msg.id_from as sender_id, msg.message, msg.date_time, msg.id_replayed_to,\n" +
                "                sender.email as sender_email, sender.first_name as sender_first_name, sender.last_name as sender_last_name, \n" +
                "                sender.city as sender_city, sender.date_of_birth as sender_date_of_birth, sender_credentials.password as sender_password,\n" +
                "                mr.id_receiver as receiver_id, receiver.email as receiver_email, receiver.first_name as receiver_first_name, receiver.last_name as receiver_last_name, \n" +
                "                receiver.city as receiver_city, receiver.date_of_birth as receiver_date_birth, receiver_credentials.password as receiver_password\n" +
                "                from messages msg\n" +
                "                inner join users sender on msg.id_from = sender.id\n" +
                "                inner join credentials sender_credentials on sender.email = sender_credentials.email\n" +
                "                inner join \"messageReceivers\" mr on msg.id = mr.id_message\n" +
                "                inner join users receiver on mr.id_receiver = receiver.id\n" +
                "                inner join credentials receiver_credentials on receiver.email = receiver_credentials.email\n" +
                "                where msg.id = ?;";

        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Message> messagesList;
            messagesList = buildMessagesFromTablesJoins(resultSet);
            return messagesList.get(0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    @Override
    public Iterable<Message> findAll() {
        String sql ="select msg.id as id_message, msg.id_from as sender_id, msg.message, msg.date_time, msg.id_replayed_to,\n" +
                "                sender.email as sender_email, sender.first_name as sender_first_name, sender.last_name as sender_last_name, \n" +
                "                sender.city as sender_city, sender.date_of_birth as sender_date_of_birth, sender_credentials.password as sender_password,\n" +
                "                mr.id_receiver as receiver_id, receiver.email as receiver_email, receiver.first_name as receiver_first_name, receiver.last_name as receiver_last_name, \n" +
                "                receiver.city as receiver_city, receiver.date_of_birth as receiver_date_birth, receiver_credentials.password as receiver_password\n" +
                "                from messages msg\n" +
                "                inner join users sender on msg.id_from = sender.id\n" +
                "                inner join credentials sender_credentials on sender.email = sender_credentials.email\n" +
                "                inner join \"messageReceivers\" mr on msg.id = mr.id_message\n" +
                "                inner join users receiver on mr.id_receiver = receiver.id\n" +
                "                inner join credentials receiver_credentials on receiver.email = receiver_credentials.email;";

        List<Message> messagesList = null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery()) {
            messagesList = buildMessagesFromTablesJoins(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return messagesList;
    }

    private List<Message> buildMessagesFromTablesJoins(ResultSet resultSet) throws SQLException {
        Map<Message, List<User>> messageUserMap = new HashMap<>();

        while (resultSet.next()){
            Long id_message = resultSet.getLong("id_message");
            Long sender_id = resultSet.getLong("sender_id");
            String message = resultSet.getString("message");
            LocalDateTime date_time = resultSet.getTimestamp("date_time").toLocalDateTime();
            Long id_replayed_to = resultSet.getLong("id_replayed_to");
            String sender_email = resultSet.getString("sender_email");
            String sender_first_name= resultSet.getString("sender_first_name");
            String sender_last_name = resultSet.getString("sender_last_name");
            String sender_city = resultSet.getString("sender_city");
            LocalDateTime sender_date_of_birth = LocalDateTime.parse(resultSet.getString("sender_date_of_birth"), Constants.DATE_OF_BIRTH_FORMATTER);
            String sender_password = resultSet.getString("sender_password");
            Long receiver_id = resultSet.getLong("receiver_id");
            String receiver_email =resultSet.getString("receiver_email");
            String receiver_first_name = resultSet.getString("receiver_first_name");
            String receiver_last_name = resultSet.getString("receiver_last_name");
            String receiver_city = resultSet.getString("receiver_city");
            LocalDateTime receiver_date_birth = LocalDateTime.parse(resultSet.getString("receiver_date_birth"), Constants.DATE_OF_BIRTH_FORMATTER);
            String receiver_password = resultSet.getString("receiver_password");

            User sender = new User(sender_email, sender_first_name, sender_last_name, sender_city, sender_date_of_birth, sender_password);
            sender.setId(sender_id);
            Message message1 = new Message(sender, null, message);
            message1.setId(id_message);
            message1.setDateTime(date_time);

            if(id_replayed_to!=0)
                message1.setReplay(findOne(id_replayed_to));

            User receiver = new User(receiver_email, receiver_first_name, receiver_last_name, receiver_city, receiver_date_birth, receiver_password);
            receiver.setId(receiver_id);
            List<User> receivers= new ArrayList<>();
            receivers.add(receiver);

            if(!messageUserMap.containsKey(message1)){
                messageUserMap.put(message1, receivers);
            }else{
                messageUserMap.get(message1).add(receiver);
            }
        }

        List<Message> messageList = new ArrayList<>();
        messageUserMap.forEach((message, receivers) -> message.setTo(receivers));
        messageList.addAll(messageUserMap.keySet());
        return messageList;
    }

    @Override
    public Message save(Message message) {
        String sqlSaveMsg = "INSERT into messages (id_from, message, date_time, id_replayed_to) values (?, ?, ?, ?) RETURNING id";
        String sqlSaveReceivers = "INSERT INTO \"messageReceivers\" (id_message, id_receiver) values (?, ?)";
        Long id_message;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSaveMsg)){

            preparedStatement.setLong(1, message.getFrom().getId());
            preparedStatement.setString(2, message.getMessage());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(message.getDateTime()));

            if(message.getReplay()!=null)
                preparedStatement.setLong(4, message.getReplay().getId());
            else
                preparedStatement.setNull(4, Types.NULL);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                id_message = resultSet.getLong("id");
                try(Connection connection2 = DriverManager.getConnection(url, username, password);
                    PreparedStatement preparedStatement2 = connection2.prepareStatement(sqlSaveReceivers)){
                    for(User receiver:message.getTo()){
                        preparedStatement2.setLong(1, id_message);
                        preparedStatement2.setLong(2, receiver.getId());
                        preparedStatement2.executeUpdate();
                    }
                }
            }

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return message;
    }

    public Iterable<Message> getSortedMessagesByDateTwoUsers(Long idUser1, Long idUser2){
        String sql = "select msg.id as id_message, msg.id_from as sender_id, msg.message, msg.date_time, msg.id_replayed_to,\n" +
                "                sender.email as sender_email, sender.first_name as sender_first_name, sender.last_name as sender_last_name, \n" +
                "                sender.city as sender_city, sender.date_of_birth as sender_date_of_birth, sender_credentials.password as sender_password,\n" +
                "                mr.id_receiver as receiver_id, receiver.email as receiver_email, receiver.first_name as receiver_first_name, receiver.last_name as receiver_last_name, \n" +
                "                receiver.city as receiver_city, receiver.date_of_birth as receiver_date_birth, receiver_credentials.password as receiver_password\n" +
                "                from messages msg\n" +
                "                inner join users sender on msg.id_from = sender.id\n" +
                "                inner join credentials sender_credentials on sender.email = sender_credentials.email\n" +
                "                inner join \"messageReceivers\" mr on msg.id = mr.id_message\n" +
                "                inner join users receiver on mr.id_receiver = receiver.id\n" +
                "                inner join credentials receiver_credentials on receiver.email = receiver_credentials.email\n" +
                "                where (msg.id_from = ? and mr.id_receiver = ?) or (msg.id_from =? and mr.id_receiver = ?)\n" +
                "                order by msg.date_time ASC";
        List<Message> messageList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1,idUser1);
            preparedStatement.setLong(2,idUser2);
            preparedStatement.setLong(3,idUser2);
            preparedStatement.setLong(4,idUser1);

            ResultSet resultSet = preparedStatement.executeQuery();
            messageList = buildMessagesFromTablesJoins(resultSet);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return messageList;
    }

    @Override
    public Message delete(Long id) {
        return null;
    }

    @Override
    public Message update(Message message) {
        return null;
    }
}
