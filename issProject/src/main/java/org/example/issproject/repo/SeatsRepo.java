package org.example.issproject.repo;

import org.example.issproject.domain.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatsRepo {
    protected String url;
    protected String username;
    protected String password;
    public SeatsRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    public List<Seat> getAllSeatsFromLodge(Integer LodgeId) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM seat where lodge = ?")) {
            statement.setInt(1, LodgeId); // Set the LodgeId as a parameter in the query
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Seat> seats = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int number = resultSet.getInt("number");
                    int row = resultSet.getInt("row");
                    float price = resultSet.getFloat("price");
                    boolean isReserved = resultSet.getString("taken").equals("yes");
                    Seat seat = new Seat(number, row, price, isReserved);
                    seat.setId(id);
                    seats.add(seat);
                }
                return seats;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public float getSeatPrice(int seatId) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT price FROM seat where id = ?")) {
            statement.setInt(1, seatId); // Set the seatId as a parameter in the query
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getFloat("price");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public Seat addSeat( int number,int lodge,float price,String taken,int row) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO seat (number,lodge,price,taken,row) VALUES (?,?,?,?,?)")) {
            statement.setInt(1, number);
            statement.setInt(2, lodge);
            statement.setFloat(3, price);
            statement.setString(4, taken);
            statement.setInt(5, row);
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    Seat seat = new Seat(number, row, price, false);
                    seat.setId(id);
                    return seat;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Seat updateSeatPrice(int seatId, float price) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE seat SET price = ? WHERE id = ?")) {
            statement.setFloat(1, price);
            statement.setInt(2, seatId);
            statement.executeUpdate();
            Seat seat = new Seat(0, 0, price, false);
            seat.setId(seatId);
            return seat;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Seat deleteSeat(int seatId) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM seat WHERE id = ?")) {
            statement.setInt(1, seatId);
            statement.executeUpdate();
            Seat seat = new Seat(0, 0, 0, false);
            seat.setId(seatId);
            return seat;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateSeatTaken(int seatId, String taken) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE seat SET taken = ? WHERE id = ?")) {
            statement.setString(1, taken);
            statement.setInt(2, seatId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
