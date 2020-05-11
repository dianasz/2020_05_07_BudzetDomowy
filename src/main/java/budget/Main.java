package budget;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TransactionService transactionActions = new TransactionService();
        TransactionDao transactionDao = new TransactionDao();
        transactionDao.createDatabaseStructure();

        Scanner scanner = new Scanner(System.in);

        boolean isOptionSelected = true;
        while (isOptionSelected) {
            System.out.println("Wybierz akcję");
            System.out.println("0. Wyjście");
            System.out.println("1. Dodaj transakcję");
            System.out.println("2. Modyfikuj transakcję");
            System.out.println("3. Usuń transakcję");
            System.out.println("4. Wyświetl wszystkie przychody");
            System.out.println("5. Wyświetl wszystkie wydatki");

            String userChoice = scanner.nextLine();
            Transaction transaction = new Transaction();
            switch (userChoice) {
                case "0":
                    isOptionSelected = false;
                    break;
                case "1":
                    transactionActions.performCreateAction();
                    break;
                case "2":
                    transactionActions.performUpdateAction();
                    break;
                case "3":
                    transactionActions.performDeleteAction();
                    break;
                case "4":
                    transactionActions.performReadIncomesAction();
                    break;
                case "5":
                    transactionActions.performReadExpensesAction();
                    break;
                default:
                    System.out.println("Nie ma takiej opcji");
            }
        }
        scanner.close();
    }
}
