package budget;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    private static Connection connection;

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
    public List<Transaction> read(Type type){
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
            System.out.println ("Rekord został zaktualizowany");
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd podczas aktualizowania transakcji");
        }
    }

    //D-delete
    public void deleteTransaction(Long id){
        String deleteSql = "delete from transactions where id=?";
        try {
            PreparedStatement statement = connection.prepareStatement (deleteSql);
            statement.setLong (1, id);
            statement.executeUpdate ();
            System.out.println ("Rekord został usunięty");
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd podczas usuwaniu rekordu");
        }
    }

    public void close(){
        try {
            connection.close ();
        } catch (SQLException exception) {
            System.err.println ("Nie udało się zamknąć połączenia");
        }
    }
}
