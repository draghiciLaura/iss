package org.example.issproject.repo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.*;
public class LodgeRepo {

    protected String url;
    protected String username;
    protected String password;

    public LodgeRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Optional<Integer> findIdByName(String name) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id FROM lodge WHERE name = ?");
        ) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                return Optional.of(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<String> getAllLodges() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT name FROM lodge");
             ResultSet resultSet = statement.executeQuery()) {
            List<String> lodges = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                lodges.add(name);
            }
            return lodges;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
