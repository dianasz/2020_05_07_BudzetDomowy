package budget;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/budget?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public TransactionDao() {
        connectToDatabase ();
    }

    public void createDatabaseStructure(){
        createSchema();
        createTable();
        cleanTables();
        insertExamples ();
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException exception) {
            System.err.println ("Nie znaleziono klasy sterownika");
        } catch (SQLException exception) {
            System.err.println ("Nie można nawiązać połączenia");
        }
    }

    private void cleanTables() {
        String cleanSql ="delete from transaction;";
        try {
            PreparedStatement statement = connection.prepareStatement (cleanSql);
            statement.executeUpdate ();
        } catch (SQLException e) {
            System.err.println ("Wystąpił błąd");
        }
    }

    private void createTable() {
        String createTableSql = "create table if not exists transaction(" +
                "id INT primary key auto_increment," +
                "type ENUM('EXPENSE', 'INCOME')," +
                "description varchar(100)," +
                "amount double," +
                "date varchar(50))";
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

    private void insertExamples(){
        String insertExampleOne ="insert into transaction (type, description, amount, date) values ('INCOME', 'urodziny', 200.50, '2020-05-10');";
        String insertExampleTwo ="insert into transaction (type, description, amount, date) values ('EXPENSE', 'zakupy', 139.99, '2020-05-08');";
        String insertExampleThree ="insert into transaction (type, description, amount, date) values ('INCOME', 'zwrot podatku', 1.50, '2020-04-30');";
        String insertExampleFour ="insert into transaction (type, description, amount, date) values ('EXPENSE', 'doładowanie telefonu', 50, '2020-05-04');";
        try {
            connection.prepareStatement (insertExampleOne).executeUpdate ();
            connection.prepareStatement (insertExampleTwo).executeUpdate ();
            connection.prepareStatement (insertExampleThree).executeUpdate ();
            connection.prepareStatement (insertExampleFour).executeUpdate ();
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd poczas wczytywania przykładowych danych");
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
    public List<Transaction> read(TransactionType type){
        String readSql = "select * from transaction  where type=?";
        try {
            PreparedStatement statement = connection.prepareStatement (readSql);
            statement.setString (1, String.valueOf (type));
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

    public void close(){
        try {
            connection.close ();
        } catch (SQLException exception) {
            System.err.println ("Nie udało się zamknąć połączenia");
        }
    }
}
