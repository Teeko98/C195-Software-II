package sql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Country;

import java.sql.*;

public class DBCountries {

    /**
     * @return an observable list of the Country class of all countries in the sql server.
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {

        ObservableList<Country> countriesList = FXCollections.observableArrayList();

        Connection connection = JDBC.getConnection();
        DBQuery.setStatement(connection);
        Statement statement = DBQuery.getStatement();

        String selectStatement = "SELECT * FROM client_schedule.countries;";
        statement.execute(selectStatement);
        ResultSet resultSet = statement.getResultSet();


        while (resultSet.next()) {
            int countryId = resultSet.getInt("Country_ID");
            String countryName = resultSet.getString("Country");

            Country country = new Country(countryId, countryName);
            countriesList.add(country);
        }

        return countriesList;
        }
    }
