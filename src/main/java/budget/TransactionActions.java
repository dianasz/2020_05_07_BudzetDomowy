package budget;

import java.util.Scanner;

public class TransactionActions {

    public void performDeleteAction(){
        TransactionDao transactionDao = new TransactionDao ();
        System.out.println ("Podaj id transakcji, którą chcesz usunąć");
        Scanner scanner = new Scanner (System.in);
        Long userChoice = scanner.nextLong ();
        transactionDao.deleteTransaction (userChoice);
        transactionDao.close ();
    }

}
