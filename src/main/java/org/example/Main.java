package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var lab1 = new Lab1();
        var scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите имя пользователя");
            String username = scanner.nextLine();
            if (lab1.authenticate(username)) {
                lab1.printAccesses();
                lab1.printAccessibleObjects();
                handleUserCommands(scanner, lab1);
            }
        }
    }

    private static void handleUserCommands(Scanner scanner, Lab1 lab1) {
        while (true) {
            System.out.println("Ожидание указаний");
            String command = scanner.nextLine();
            switch (command) {
                case "read" -> {
                    System.out.println("Над каким объектом производится операция?");
                    int objectId = scanner.nextInt();
                    scanner.nextLine();
                    lab1.read(objectId);
                }
                case "write" -> {
                    System.out.println("Над каким объектом производится операция?");
                    int objectId = scanner.nextInt();
                    scanner.nextLine();
                    lab1.write(objectId);
                }
                case "grant" -> {
                    System.out.println("Право на какой объект передается?");
                    int objectId = scanner.nextInt();
                    scanner.nextLine();
                    if (lab1.checkAccess(objectId)) {
                        System.out.println("Какое право передается?");
                        var access = scanner.nextLine();
                        System.out.println("Какому пользователю?");
                        var user = scanner.nextLine();
                        lab1.grant(access, user, objectId);
                        System.out.println("Обновленная матрица прав: \n");
                        lab1.matrixOfAccess.forEach((key, value) -> System.out.println(key + ":" + value));
                    } else {
                        System.out.println("Операция прервана. Недостаточно прав");
                    }
                }
                case "quit" -> {
                    System.out.println("Введите имя пользователя");
                    String username = scanner.nextLine();
                    if (lab1.authenticate(username)) {
                        lab1.printAccesses();
                        lab1.printAccessibleObjects();
                    }
                }
                case "request" -> {
                    System.out.println("К какому объекту хотите осуществить доступ? ");
                    int objectId = scanner.nextInt();
                    scanner.nextLine();
                    lab1.accessOfObject(objectId);
                }
                default -> System.out.println("Неверный выбор операции");
            }
        }
    }

}
