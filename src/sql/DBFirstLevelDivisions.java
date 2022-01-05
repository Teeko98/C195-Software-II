package sql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.FirstLevelDivision;

import java.sql.*;

public class DBFirstLevelDivisions {

    /**
     * @return an observable list of the FirstLevelDivision class of all first level divisions in the sql server.
     */
    public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() throws SQLException {

        ObservableList<FirstLevelDivision> firstLevelDivisionsList = FXCollections.observableArrayList();
        Connection connection = JDBC.getConnection();
        DBQuery.setStatement(connection);
        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT * FROM client_schedule.first_level_divisions;";
        statement.execute(selectStatement);
        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            int divisionId = resultSet.getInt("Division_ID");
            String division = resultSet.getString("Division");
            Date createDate = resultSet.getDate("Create_Date");
            String createdBy = resultSet.getString("Created_By");
            Date lastUpdate = resultSet.getDate("Last_Update");
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");
            int countryId = resultSet.getInt("Country_ID");

            FirstLevelDivision firstLevelDivision = new FirstLevelDivision(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
            firstLevelDivisionsList.add(firstLevelDivision);
        }

        return firstLevelDivisionsList;
    }
}
