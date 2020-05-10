package budget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseStructure {

    static final String URL = "jdbc:mysql://localhost:3306/budget?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
    static final String USER = "root";
    static final String PASSWORD = "admin";

    static void cleanTables(Connection connection) {
        String cleanSql = "delete from transaction;";
        try {
            PreparedStatement statement = connection.prepareStatement (cleanSql);
            statement.executeUpdate ();
        } catch (SQLException e) {
            System.err.println ("Wystąpił błąd");
        }
    }

    static void createTable(Connection connection) {
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

    static void createSchema(Connection connection) {
        String createSchematSql = "create schema if not exists budget";
        try {
            PreparedStatement statement = connection.prepareStatement (createSchematSql);
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd przy tworzeniu schematu");
        }
    }

    static void insertExamples(Connection connection) {
        String insertExampleOne = "insert into transaction (type, description, amount, date) values ('INCOME', 'urodziny', 200.50, '2020-05-10');";
        String insertExampleTwo = "insert into transaction (type, description, amount, date) values ('EXPENSE', 'zakupy', 139.99, '2020-05-08');";
        String insertExampleThree = "insert into transaction (type, description, amount, date) values ('INCOME', 'zwrot podatku', 1.50, '2020-04-30');";
        String insertExampleFour = "insert into transaction (type, description, amount, date) values ('EXPENSE', 'doładowanie telefonu', 50, '2020-05-04');";
        try {
            connection.prepareStatement (insertExampleOne).executeUpdate ();
            connection.prepareStatement (insertExampleTwo).executeUpdate ();
            connection.prepareStatement (insertExampleThree).executeUpdate ();
            connection.prepareStatement (insertExampleFour).executeUpdate ();
        } catch (SQLException exception) {
            System.err.println ("Wystąpił błąd poczas wczytywania przykładowych danych");
        }
    }
}
