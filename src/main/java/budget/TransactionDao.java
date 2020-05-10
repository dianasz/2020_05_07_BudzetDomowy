package budget;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDao {
    private static final String URL = "jdbc:mysql://localhost:3306/budget?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    private Connection connection;

    public TransactionDao(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException exception) {
            System.err.println ("Nie znaleziono klasy sterownika");
        } catch (SQLException exception) {
            System.err.println ("Nie można nawiązać połączenia");
        }
    }

    public void createDatabaseStructure(){
        createSchema();
        createTable();
    }

    private void createTable() {
        String createTableSql = "create table if not exists transaction(" +
                "    id INT primary key auto_increment," +
                "    type ENUM('EXPENSE', 'INCOME')," +
                "    description varchar(100)," +
                "    amount double," +
                "    date varchar(50))";
        try {
            PreparedStatement statement = connection.prepareStatement (createTableSql);
            statement.executeUpdate ();
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd przy tworzeniu tabeli");
        }
    }

    private void createSchema() {
        String createSchematSql = "create schema if not exists budget";
        try {
            PreparedStatement statement = connection.prepareStatement (createSchematSql);
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd przy tworzeniu schematu");
        }
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
            System.err.println ("Błąd podczas wczytywania transakcji");
        }
        return null;
    }

    //U-update
    public void updateTransaction(Transaction transaction){

    }

    //D-delete
    public void deleteTransaction(Long id){

    }

    public void close(){
        try {
            connection.close ();
        } catch (SQLException exception) {
            System.err.println ("Nie udało się zamknąć połączenia");
        }
    }
}
