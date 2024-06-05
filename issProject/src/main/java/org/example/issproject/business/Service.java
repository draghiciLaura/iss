package org.example.issproject.business;

import org.example.issproject.Observer;
import org.example.issproject.domain.Lodge;
import org.example.issproject.domain.Seat;
import org.example.issproject.repo.LodgeRepo;
import org.example.issproject.repo.SeatsRepo;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private LodgeRepo lodgeRepo;
    private SeatsRepo seatsRepo;
    private List<Observer> observers = new ArrayList<>();
    public Service(LodgeRepo lodgeRepo, SeatsRepo seatsRepo) {
        this.lodgeRepo = lodgeRepo;
        this.seatsRepo = seatsRepo;
    }

//    public Integer findIdByName(String name) {
//        return lodgeRepo.findIdByName(name).orElseThrow(() -> new RuntimeException("Lodge not found"));
//    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
    public List<Seat> getAllSeatsFromLodge(String name) {
        Integer lodgeId = lodgeRepo.findIdByName(name).orElseThrow(() -> new RuntimeException("Lodge not found"));
        return seatsRepo.getAllSeatsFromLodge(lodgeId);
    }
    public float getSeatPrice(int seatId) {
        return seatsRepo.getSeatPrice(seatId);
    }

    public float getSeatPricesTotal(List<Integer> seatIds) {
        float total = 0;
        for (Integer seatId : seatIds) {
            total += getSeatPrice(seatId);
        }
        return total;
    }

    public List<String> getAllLodges() {
        return lodgeRepo.getAllLodges();
    }


    public int getLodgeId(String lodgeName) {
        return lodgeRepo.findIdByName(lodgeName).orElseThrow(() -> new RuntimeException("Lodge not found"));
    }

    public int getMaxSeatNumberFromLodge(int lodgeId, int row) {
        List<Seat> seats = seatsRepo.getAllSeatsFromLodge(lodgeId);
        int maxSeatNumber = 0;
        for (Seat seat : seats) {
            if (seat.getNumber() > maxSeatNumber && seat.getRow() == row) {
                maxSeatNumber = seat.getNumber();
            }
        }
        return maxSeatNumber;
    }

    public Seat addSeat(int number, int lodge, float price, String taken, int row) {
        Seat seat = seatsRepo.addSeat(number, lodge, price, taken, row);
        notifyObservers();
        return seat;
    }
    public Seat updateSeatPrice(int seatId, float price) {
        Seat seat = seatsRepo.updateSeatPrice(seatId, price);
        notifyObservers();
        return seat;
    }

    public Seat deleteSeat(int seatId) {
        Seat seat = seatsRepo.deleteSeat(seatId);
        notifyObservers();
        return seat;
    }
    public void updateSeatTaken(int seatId) {
        seatsRepo.updateSeatTaken(seatId, "yes");
        notifyObservers();
    }
}
