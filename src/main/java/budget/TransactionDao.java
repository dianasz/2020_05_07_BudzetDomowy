package budget;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionDao {
    private Connection connection;

    public TransactionDao() {
        connectToDatabase ();
    }

    public void createDatabaseStructure() {
        DatabaseStructureService.createSchema (connection);
        DatabaseStructureService.createTable (connection);
        DatabaseStructureService.cleanTables (connection);
        DatabaseStructureService.insertExamples (connection);
    }

    private void connectToDatabase() {
        try {
            Class.forName ("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection (DatabaseStructureService.URL, DatabaseStructureService.USER, DatabaseStructureService.PASSWORD);
        } catch (ClassNotFoundException exception) {
            System.err.println ("Nie znaleziono klasy sterownika");
        } catch (SQLException exception) {
            System.err.println ("Nie można nawiązać połączenia");
        }
    }

    //C-create
    public void createTransaction(Transaction transaction) {
        String insertTransactionSql = "insert into transaction(type, description, amount, date) values(?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement (insertTransactionSql);
            statement.setString (1, String.valueOf (transaction.getType ()));
            statement.setString (2, transaction.getDescription ());
            statement.setDouble (3, transaction.getAmount ());
            statement.setString (4, transaction.getDate ());
            statement.executeUpdate ();
        } catch (SQLException exception) {
            System.err.println ("Nie udało się zapisać rekordu");
        }
    }

    //R-read incomes/expenses
    public List<Transaction> read(TransactionType type) {
        String readSql = "select * from transaction  where type=?";
        try {
            PreparedStatement statement = connection.prepareStatement (readSql);
            statement.setString (1, String.valueOf (type));
            ResultSet resultSet = statement.executeQuery ();

            List<Transaction> transactionList = new ArrayList<> ();
            while (resultSet.next ()) {
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
        return Collections.emptyList();
    }

    //U-update
    public void updateTransaction(Transaction transaction) {
        String updateSql = "update transaction set type=?, description=?, amount=?, date=? where id=?";
        try {
            PreparedStatement statement = connection.prepareStatement (updateSql);
            statement.setString (1, String.valueOf (transaction.getType ()));
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
    public void deleteTransaction(Long id) {
        String deleteSql = "delete from transaction where id=?";
        try {
            PreparedStatement statement = connection.prepareStatement (deleteSql);
            statement.setLong (1, id);
            statement.executeUpdate ();
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd podczas usuwaniu rekordu");
        }
    }

    public void close() {
        try {
            connection.close ();
        } catch (SQLException exception) {
            System.err.println ("Nie udało się zamknąć połączenia");
        }
    }
}