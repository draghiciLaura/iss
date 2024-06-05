package org.example.issproject.domain;

public class Seat extends Entity<Integer>{
    private int number;
    private float price;
    private int row;
    private boolean isReserved;

    public Seat(int number, int row, float price, boolean isReserved) {
        this.number = number;
        this.price = price;
        this.isReserved = isReserved;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "number=" + number +
                ", price=" + price +
                ", isReserved=" + isReserved +
                ", id=" + id +
                '}';
    }
}
