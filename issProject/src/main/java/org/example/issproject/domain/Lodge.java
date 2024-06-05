package org.example.issproject.domain;

public class Lodge extends Entity<Integer>{
    private String name;
    private int capacity;
    private Seat seats;

    public Lodge(String name, int capacity, Seat seats) {
        this.name = name;
        this.capacity = capacity;
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Seat getSeats() {
        return seats;
    }

    public void setSeats(Seat seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Lodge{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", seats=" + seats +
                ", id=" + id +
                '}';
    }
}
