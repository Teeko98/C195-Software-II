package sql;

import main.User;

import java.sql.*;

public class DBUsers {

    public static User currentUser;

    /**
     * Runs an SQL statement to get user data based on provided username and password.
     * Upon successful login, the user's data is copied to the currentUser object.
     *
     * @param username the username entered by the user
     * @param password the password entered by the user
     * @return true if the login was successful. false if unsuccessful.
     */
    public static boolean userLogin(String username, String password) throws SQLException {

        Connection connection = JDBC.getConnection();
        DBQuery.setStatement(connection);
        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT * FROM client_schedule.users WHERE User_Name=" +
                "'" + username + "' AND Password='" + password + "';";
        statement.execute(selectStatement);
        ResultSet resultSet = statement.getResultSet();

        if (!resultSet.next()) {
            return false;
        }

        int userId = resultSet.getInt("User_ID");
        Date createDate = resultSet.getDate("Create_Date");
        String createdBy = resultSet.getString("Created_By");
        Date lastUpdate = resultSet.getDate("Last_Update");
        String lastUpdatedBy = resultSet.getString("Last_Updated_By");

        currentUser = new User(userId, username, password, createDate, createdBy, lastUpdate, lastUpdatedBy);
        return true;
    }

    /**
     * Resets the currentUser object when a user logs out.
     */
    public static void userLogout(){
        currentUser = null;
    }
}
