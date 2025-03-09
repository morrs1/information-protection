package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Lab1 {
    private final Integer COUNT_OF_USERS = 7;
    private final Integer COUNT_OF_OBJECTS = 6;

    private final List<String> listOfUsers = new ArrayList<>(List.of("Boris", "Bagrat", "Egor", "Ivan", "Misha", "Denis", "Sonya"));

    private final HashMap<Integer, ArrayList<String>> truthOfAccess = new HashMap<>() {
        {
            put(0, new ArrayList<>(List.of("Полный запрет")));
            put(1, new ArrayList<>(List.of("Передача прав")));
            put(2, new ArrayList<>(List.of("Запись")));
            put(3, new ArrayList<>(List.of("Запись", "Передача прав")));
            put(4, new ArrayList<>(List.of("Чтение")));
            put(5, new ArrayList<>(List.of("Полный доступ")));
        }
    };

    public HashMap<String, ArrayList<Integer>> matrixOfAccess = new HashMap<>();
    private String currentUsername = "";

    public Lab1() {
        var random = new Random();
        for (var i = 0; i < COUNT_OF_USERS; i++) {
            var listOfAccess = new ArrayList<Integer>();
            for (int j = 0; j < COUNT_OF_OBJECTS; j++) {
                listOfAccess.add(random.nextInt(6));
            }
            matrixOfAccess.put(listOfUsers.get(i), listOfAccess);
        }
        matrixOfAccess.put("Egor", new ArrayList<>(List.of(5, 5, 5, 5, 5, 5)));
        matrixOfAccess.forEach((key, value) -> System.out.println(key + ":" + value));
    }

    public boolean authenticate(String username) {
        if (listOfUsers.contains(username)) {
            System.out.println("Вы усешно авторизировались под пользователем: " + username);
            currentUsername = username;
            return true;
        } else {
            System.out.println("Пользователь не найден");
            return false;
        }
    }

    public void logout() {
        currentUsername = "";
    }

    public void printAccesses() {
        var listOfAccess = matrixOfAccess.get(currentUsername);
        for (var i = 0; i < listOfAccess.size(); i++) {
            System.out.printf("Обьект: %d Права доступа: %s \n", i, truthOfAccess.get(listOfAccess.get(i)));
        }
    }


    public void read(Integer numOfObject) {
        if (matrixOfAccess.get(currentUsername).get(numOfObject) == 4 || matrixOfAccess.get(currentUsername).get(numOfObject) == 5) {
            System.out.println("Операция прошла успешно");
        } else {
            System.out.println("У вас нет прав для этой операции");
        }
    }

    public void write(Integer numOfObject) {
        var access = matrixOfAccess.get(currentUsername).get(numOfObject);
        if (access == 2 || access == 3 || access == 5) {
            System.out.println("Операция прошла успешно");
        } else {
            System.out.println("У вас нет прав для этой операции");
        }
    }

    public boolean checkAccess(Integer numOfObject) {
        var access = matrixOfAccess.get(currentUsername).get(numOfObject);
        return access == 1 || access == 3 || access == 5;
    }

    public void grant(String access, String username, int objectId) {
        switch (access) {
            case "read" -> matrixOfAccess.get(username).set(objectId, 4);
            case "write" -> matrixOfAccess.get(username).set(objectId, 2);
            case "grant" -> matrixOfAccess.get(username).set(objectId, 1);
            case "deny" -> matrixOfAccess.get(username).set(objectId, 0);
            default -> {}
        }
    }


}
