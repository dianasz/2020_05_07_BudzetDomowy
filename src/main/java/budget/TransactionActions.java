package budget;

import java.util.List;
import java.util.Scanner;

public class TransactionActions {
    Scanner scanner = new Scanner (System.in);

    public void performDeleteAction(){
        TransactionDao transactionDao = new TransactionDao ();
        System.out.println ("Podaj id transakcji, którą chcesz usunąć");
        Long userId = scanner.nextLong ();
        transactionDao.deleteTransaction (userId);
        System.out.println ("Rekord o id="+userId+" został usunięty!");
        transactionDao.close ();
    }

    public void performUpdateAction(){
        TransactionDao transactionDao = new TransactionDao ();
        System.out.println ("Podaj id transakcji, którą chcesz zmodyfikować");
        Long userId = scanner.nextLong ();
        scanner.nextLine ();

        System.out.println ("Podaj typ transakcji || 1-wydatek 2-przychód");
        TransactionType transactionType = null;
        String userType = scanner.nextLine ();
        if (userType.equals("1")){
            transactionType = TransactionType.EXPENSE;
        } else if (userType.equals ("2")){
            transactionType = TransactionType.INCOME;
        } else {
            System.out.println ("Wybrany typ nie istnieje");
        }

        System.out.println ("Podaj opis");
        String userDescription = scanner.nextLine ();

        System.out.println ("Podaj kwotę");
        Double userAmount = scanner.nextDouble ();
        scanner.nextLine ();

        System.out.println ("Podaj datę");
        String userDate = scanner.nextLine ();

        Transaction updatedTransaction = new Transaction (userId, transactionType, userDescription, userAmount, userDate);
        transactionDao.updateTransaction (updatedTransaction);
        System.out.println ("Rekord o id="+userId+" został zmodyfikowany: "+updatedTransaction.toString ());
        transactionDao.close ();
    }

    public void performCreateAction(){
        TransactionDao transactionDao = new TransactionDao ();
        System.out.println ("Podaj typ transakcji || 1-wydatek 2-przychód");
        TransactionType transactionType = null;
        String userType = scanner.nextLine ();
        if (userType.equals("1")){
            transactionType = TransactionType.EXPENSE;
        } else if (userType.equals ("2")){
            transactionType = TransactionType.INCOME;
        } else {
            System.out.println ("Wybrany typ nie istnieje");
        }

        System.out.println ("Podaj opis");
        String userDescription = scanner.nextLine ();

        System.out.println ("Podaj kwotę");
        Double userAmount = scanner.nextDouble ();
        scanner.nextLine ();

        System.out.println ("Podaj datę");
        String userDate = scanner.nextLine ();

        Transaction createdTransaction = new Transaction (transactionType, userDescription, userAmount, userDate);
        transactionDao.createTransaction (createdTransaction);
        System.out.println ("Nowy transakcja została utworzona: "+createdTransaction.toString ());
        transactionDao.close ();
    }

    public void performReadIncomesAction(){
        TransactionDao transactionDao = new TransactionDao ();
        List<Transaction> transactionList = transactionDao.read (TransactionType.INCOME);
        for (Transaction transaction : transactionList) {
            System.out.println (transaction);
        }
        transactionDao.close ();
    }

    public void performReadExpensesAction(){
        TransactionDao transactionDao = new TransactionDao ();
        List<Transaction> transactionList = transactionDao.read (TransactionType.EXPENSE);
        for (Transaction transaction : transactionList) {
            System.out.println (transaction);
        }
        transactionDao.close ();
    }
}
