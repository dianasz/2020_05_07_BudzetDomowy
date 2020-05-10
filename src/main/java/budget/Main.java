package budget;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TransactionDao transactionDao = new TransactionDao ();

        Scanner scanner = new Scanner (System.in);

        boolean isOptionSelected = true;
        while(isOptionSelected){
            System.out.println ("Wybierz akcję");
            System.out.println ("0. Wyjście");
            System.out.println ("1. Dodaj transakcję");
            System.out.println ("2. Modyfikuj transakcję");
            System.out.println ("3. Usuń transakcję");
            System.out.println ("4. Wyświetl wszystkie przychody");
            System.out.println ("5. Wyświetl wszystkie wydatki");

            String userChoice = scanner.nextLine ();
            Transaction transaction = new Transaction ();
            switch(userChoice){
                case "0": isOptionSelected = false;
                case "1": transactionDao.createTransaction (transaction); break;
                case "2": transactionDao.updateTransaction (transaction); break;
                case "3": transactionDao.deleteTransaction (1L); break;
                case "4": transactionDao.read (Type.INCOME); break;
                case "5": transactionDao.read (Type.EXPENSE); break;
                default:
                    System.out.println ("Nie ma takiej opcji");
            }
        }
        scanner.close ();
    }
}
