package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
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

    private final HashMap<Integer, String> setOfSecurity = new HashMap<>() {
        {
            put(0, "Совершенно секретно");
            put(1, "Секретно");
            put(2, "Открытые данные");
        }
    };

    public HashMap<String, ArrayList<Integer>> matrixOfAccess = new HashMap<>();
    private String currentUsername = "";
    public HashMap<Integer, Integer> confidentialityOfObjects = new HashMap<>();
    public HashMap<String, Integer> confidentialityOfUsers = new HashMap<>();

    public Lab1() {
        System.out.println("Обозначения уровеней доступа: \n" + truthOfAccess + "\n");
        System.out.println("Обозначения уровней конфиденциальности: \n" + setOfSecurity + "\n");
        var random = new Random();
        for (var i = 0; i < COUNT_OF_USERS; i++) {
            confidentialityOfUsers.put(listOfUsers.get(i), random.nextInt(setOfSecurity.size()));
            var listOfAccess = new ArrayList<Integer>();
            for (int j = 0; j < COUNT_OF_OBJECTS; j++) {
                listOfAccess.add(random.nextInt(COUNT_OF_OBJECTS));
            }
            matrixOfAccess.put(listOfUsers.get(i), listOfAccess);
        }
        matrixOfAccess.put("Egor", new ArrayList<>(List.of(5, 5, 5, 5, 5, 5)));
        System.out.println("Матрица доступа: ");
        matrixOfAccess.forEach((key, value) -> System.out.println(key + ":" + value));

        for (var i = 0; i < COUNT_OF_OBJECTS; i++) {
            confidentialityOfObjects.put(i, random.nextInt(setOfSecurity.size()));
        }
        System.out.println("\nУровени конфиденциальности для объектов:");
        System.out.println(confidentialityOfObjects + "\n");
        System.out.println("Уровени конфиденциальности для пользователей:");
        System.out.println(confidentialityOfUsers + "\n");
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

    public void printAccessibleObjects() {
        for (var object : confidentialityOfObjects.keySet()) {
            if (confidentialityOfUsers.get(currentUsername) <= confidentialityOfObjects.get(object)) {
                System.out.println("Объект " + object + " доступен для чтения");
            }
            if (confidentialityOfUsers.get(currentUsername) >= confidentialityOfObjects.get(object)) {
                System.out.println("Объект " + object + " доступен для записи");
            }
        }
    }


    public void accessOfObject(int object) {
        if (confidentialityOfUsers.get(currentUsername) <= confidentialityOfObjects.get(object)) {
            System.out.println("Операция успешна");
        } else {
            System.out.println("Отказ в операции. Недостаточно прав.");
        }
    }


    public void read(Integer numOfObject) {
        if (confidentialityOfUsers.get(currentUsername) <= confidentialityOfObjects.get(numOfObject)) {
            var objectMapper = new ObjectMapper();
            String filePath = "C:\\Users\\grish\\IdeaProjects\\information-protection\\src\\main\\java\\org\\example\\objects.json";
            Objects myObject = null;
            try {
                myObject = objectMapper.readValue(new File(filePath), Objects.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert myObject != null;
            System.out.println(myObject.objects().get(numOfObject));
            System.out.println("Чтение прошло успешно");
        } else {
            System.out.println("У вас нет прав для этой операции");
        }
    }

    public void write(Integer numOfObject, String value) throws IOException {
        if (confidentialityOfUsers.get(currentUsername) >= confidentialityOfObjects.get(numOfObject)) {
            var objectMapper = new ObjectMapper();
            String filePath = "C:\\Users\\grish\\IdeaProjects\\information-protection\\src\\main\\java\\org\\example\\objects.json";
            Objects myObject = null;
            try {
                myObject = objectMapper.readValue(new File(filePath), Objects.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert myObject != null;
            myObject.objects().put(numOfObject, value);
            objectMapper.writeValue(new File(filePath), myObject);;
            System.out.println("Запись прошла успешно");
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
            default -> {
            }
        }
    }


}
