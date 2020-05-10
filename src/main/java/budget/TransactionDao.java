package budget;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionDao {
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/budget?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public TransactionDao() {
        DatabaseConnection.connectToDatabase ();
        DatabaseConnection.createDatabaseStructure ();
    }

    //C-create
    public void createTransaction(Transaction transaction){
        String insertTransactionSql = "insert into transaction(type, description, amount, date) values(?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement (insertTransactionSql);
            statement.setString (1, String.valueOf (transaction.getType ()));
            statement.setString (2, transaction.getDescription ());
            statement.setDouble (3, transaction.getAmount ());
            statement.setString(4, transaction.getDate ());
            statement.executeUpdate ();
        } catch (SQLException exception) {
            System.err.println ("Nie udało się zapisać rekordu");
        }
    }

    //R-read incomes/expenses
    public List<Transaction> read(TransactionType type){
        String readSql = "select * from transactions  where type=?";
        try {
            PreparedStatement statement = connection.prepareStatement (readSql);
            statement.setString (1, type.name ());
            ResultSet resultSet = statement.executeQuery ();

            List<Transaction> transactionList = new ArrayList<>();
            while (resultSet.next ()){
                Transaction transaction = new Transaction ();
                transaction.setId (resultSet.getLong (1));
                transaction.setType (type);
                transaction.setDescription (resultSet.getString (3));
                transaction.setAmount (resultSet.getDouble (4));
                transaction.setDate (resultSet.getString (5));
                transactionList.add (transaction);
            }
            return transactionList;
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd podczas wczytywania transakcji");
        }
        return null;
    }

    //U-update
    public void updateTransaction(Transaction transaction){
        String updateSql = "update transaction set type=?, description=?, amount=?, date=? where id=?";
        try {
            PreparedStatement statement = connection.prepareStatement (updateSql);
            statement.setString(1, String.valueOf(transaction.getType ()));
            statement.setString (2, transaction.getDescription ());
            statement.setDouble (3, transaction.getAmount ());
            statement.setString (4, transaction.getDate ());
            statement.setLong (5, transaction.getId ());
            statement.executeUpdate ();
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd podczas aktualizowania transakcji");
        }
    }

    //D-delete
    public void deleteTransaction(Long id){
        String deleteSql = "delete from transaction where id=?";
        try {
            PreparedStatement statement = connection.prepareStatement (deleteSql);
            statement.setLong (1, id);
            statement.executeUpdate ();
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd podczas usuwaniu rekordu");
        }
    }

    public void performDeleteAction(){
        System.out.println ("Podaj id transakcji, którą chcesz usunąć");
        Scanner scanner = new Scanner (System.in);
        Long id = scanner.nextLong ();
        deleteTransaction (id);
        close ();
    }

    public void close(){
        try {
            connection.close ();
        } catch (SQLException exception) {
            System.err.println ("Nie udało się zamknąć połączenia");
        }
    }
}
