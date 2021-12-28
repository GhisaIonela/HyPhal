package com.company;

import com.company.controller.Controller;
import com.company.domain.FriendRequest;
import com.company.dto.FriendRequestDTO;
import com.company.utils.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class PopulateDatabase {
    private Controller controller;

    public PopulateDatabase(Controller controller){
        this.controller = controller;
    }

    public void generateUsers(){
        List<String> firstNames = Arrays.asList("Ana", "Maria", "Diana", "Carmen", "Denisa", "Claudia", "Andreea", "Iulia", "Carina",
                                                "Alex", "Andrei", "Gabriel", "Daniel", "Ionut", "Mihai", "Bogdan", "Robert", "Lucian");
        List<String> lastNames = Arrays.asList("Rusu", "Popescu", "Ionescu", "Badoi", "Bursuc", "Cantemir", "Dumitrescu", "Almasan",
                "Alexandrescu", "Afrim", "Anghel", "Banica", "Barloiu", "Becheru", "Campean");

        int id = 1;

        String password = "12345";

        List<String> cities = Arrays.asList("Baia Mare", "Bucuresti", "Cluj-Napoca", "Timisoara", "Iasi", "Constanta", "Cravoiva", "Brasov",
                "Galati", "Ploiesti", "Oradea", "Braila", "Arad", "Pitesti", "Sibiu");

        for(int i = 0; i < 100; i++){
            Random rand = new Random();

            String randomFirstName = firstNames.get(rand.nextInt(firstNames.size()));
            String randomLastName = lastNames.get(rand.nextInt(lastNames.size()));
            String email = randomFirstName.toLowerCase(Locale.ROOT) + randomLastName.toLowerCase(Locale.ROOT) + "@gmail.com";

            try{
            while (controller.findUserByEmail(email)!=null){
                email = randomFirstName.toLowerCase(Locale.ROOT) + randomLastName.toLowerCase(Locale.ROOT) + id + "@gmail.com";
                id++;
            }
            }catch (Exception e){

            }

            String randomCity =  cities.get(rand.nextInt(cities.size()));

            LocalDate randomDate = createRandomDate(1980, 2010);

            LocalDateTime randomDateOfBirth = LocalDateTime.parse(randomDate.toString(), Constants.DATE_OF_BIRTH_FORMATTER);

            controller.createAccount(email, randomFirstName, randomLastName, randomCity, randomDateOfBirth, password);
        }
    }

    private static int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    private static LocalDate createRandomDate(int startYear, int endYear) {
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(startYear, endYear);
        return LocalDate.of(year, month, day);
    }

    public void sendFriendRequests(){
        for(long i = 0; i < 100; i++){
            long idSender = (long) ((Math.random() * (414 - 315)) + 315);
            long idReceiver = (long) ((Math.random() * (414 - 315)) + 315);
            while (idSender == idReceiver) {
                idReceiver = (long) ((Math.random() * (414 - 315)) + 315);
            }
            try{
                controller.sendFriendRequest(controller.findUserById(idSender).getEmail(), controller.findUserById(idReceiver).getEmail());
            } catch (Exception e) {

            }
        }
    }

    public void acceptFriendRquests(){

    }
}
