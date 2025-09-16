package com.belkin.collections.service;

import com.belkin.collections.exceptions.DublicatContactException;
import com.belkin.collections.exceptions.NoSearhContactException;
import com.belkin.collections.model.Contact;

import java.util.*;

public class ContactManager {
    private final List<Contact> contactlist; // Для хранения в порядке добавления
    private final Set<Contact> contactSet; // Для проверки уникальности
    private final Map<String, List<Contact>> groupMap; // Для группировки по категориям
    private final Scanner scanner;

    public ContactManager() {
        this.contactlist = new ArrayList<>();
        this.contactSet = new HashSet<>();
        this.groupMap = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== КОНТАКТНАЯ КНИГА ===");
        try {
            while (true) {
                printMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        addContact();
                        break;
                    case 2:
                        removeContact();
                        break;
                    case 3:
                        viewAllContacts();
                        break;
                    case 4:
                        searchContact();
                        break;
                    case 5:
                        viewContactByGroup();
                        break;
                    case 0:
                        System.out.println("Goodbye");
                        return;
                    default:
                        System.out.println("Завершение программы...");
                }
            }
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("вы ввели не число");

        }
    }

    private void printMenu() {
        System.out.println("---Welcome to menu---");
        System.out.println("Выберети одну из команд!");
        System.out.println("""
                      «1»: Добавить контакт
                      «2»: Удалить контакт
                      «3»: Посмотреть все контакты
                      «4»: Найти контакт
                      «5»: Посмотреть контакты по группе
                      «0»: Выход
                                  
                """);
    }

    private void addContact() {
        System.out.println("Введите имя контакта");
        String name = scanner.nextLine();

        System.out.println("Введите номер телефона");
        String phone = scanner.nextLine();

        System.out.println("Введите email");
        String email = scanner.nextLine();

        System.out.println("Введите группу");
        String group = scanner.nextLine();

        Contact newContact = new Contact(name, phone, email, group);
        try {
            if (contactSet.contains(newContact)) {
                throw new DublicatContactException("Такой контакт уже существует!");
            }
        } catch (DublicatContactException e) {
            System.out.println(e.getMessage());
            return;
        }

        contactlist.add(newContact);
        contactSet.add(newContact);

        groupMap.computeIfAbsent(group, k -> new ArrayList<>()).add(newContact);
        System.out.println("Контакт добавлен успешно!");
    }

    private void viewAllContacts() {
        System.out.println("\n--- ВСЕ КОНТАКТЫ (" + contactlist.size() + ") ---");

        try {
            if (contactlist.isEmpty()) {
                throw new NoSearhContactException("Телефонная книга пуста!");
            }
        } catch (NoSearhContactException e) {
            System.out.println(e.getMessage());
            return;
        }
        Iterator<Contact> iterator = contactlist.iterator();
        int counter = 1;

        while (iterator.hasNext()) {
            Contact contact = iterator.next();
            System.out.println(counter + ". " + contact);
            counter++;
        }


    }

    private void searchContact() {
        System.out.println("\n--- ПОИСК КОНТАКТА ПО ИМЕНИ ---");
        System.out.println("Введите имя для поиска");
        String searchName = scanner.nextLine();

        Iterator<Contact> iterator = contactlist.iterator();

        System.out.println("Результаты поиска:");

        try {
            while (iterator.hasNext()) {
                Contact contact = iterator.next();
                if (contact.getName().equalsIgnoreCase(searchName)) {
                    System.out.println("✓ " + contact);
                    return;
                } else {
                    throw new NoSearhContactException("Контакт не найден");
                }
            }
        } catch (NoSearhContactException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewContactByGroup() {
        System.out.println("\n--- ПРОСМОТР КОНТАКТОВ ПО ГРУППЕ ---");
        System.out.print("Введите название группы: ");

        String groupName = scanner.nextLine();
        List<Contact> groupContacts = groupMap.get(groupName);
        try {
            if (groupContacts == null || groupContacts.isEmpty()) {
                throw new NoSearhContactException("Групаа контактов не найдена");
            }
        } catch (NoSearhContactException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("--- Контакты в группе \"" + groupName + "\" (" + groupContacts.size() + ") ---");
        int counter = 1;

        for (Contact contact : groupContacts) {
            System.out.println(counter + ". " + contact);
            counter++;
        }
    }

    private void removeContact() {
        System.out.println("\n--- УДАЛЕНИЕ КОНТАКТА ---");
        System.out.print("Введите имя контакта для удаления: ");
        String nameToRemove = scanner.nextLine();

        Iterator<Contact> listiterator = contactlist.iterator();
        while (listiterator.hasNext()) {
            Contact contact = listiterator.next();
            try {
                if (contact.getName().equalsIgnoreCase(nameToRemove)) {
                    listiterator.remove();
                    contactSet.remove(contact);
                    groupMap.get(contact.getGroup()).remove(contact);
                    return;
                } else {
                    throw new NoSearhContactException("Контакт для удаления не найден");
                }
            } catch (NoSearhContactException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }
}






