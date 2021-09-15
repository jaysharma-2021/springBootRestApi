package com.springapi.springrestapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springapi.springrestapi.model.Slot;
import com.springapi.springrestapi.model.Token;
import com.springapi.springrestapi.service.ParkingLot;
import com.springapi.springrestapi.model.Car;

@RestController
public class ParkingLotController {

	private final ParkingLot service;

	public ParkingLotController(ParkingLot service) {
		this.service = service;
	}
    
    @PostMapping("/initiateLot")
    public ArrayList<Slot> initiateLot(@RequestParam("NumberOfslot") String NumberOfslot){
        ArrayList<Slot> availableSlot= service.initiateLot(Integer.parseInt(NumberOfslot));
        return  availableSlot;
    }
    @PostMapping("/carEntry")
    public Token parkCar(@RequestParam("carColor") String carColor,@RequestParam("carNumber") String carNumber){
        Token token = service.parkTheCar(carColor,carNumber);
        return token;
    }

    @DeleteMapping("/unParkTheCar/{tokenNumber}")
    public String unParkCar(@PathVariable String tokenNumber){
        String parkingStatus = service.unParkTheCar(tokenNumber);
        return parkingStatus;
    }

    @GetMapping("/carSearchByColorOrNumber/{colorOrNumber}")
    public List<Token> getCarByColorOrNumber(@PathVariable("colorOrNumber") String colorOrNumber) {
        return service.searchCar(colorOrNumber);
        
    }

    @GetMapping("/carList")
    public List<Token> getAllcarList() {
        return service.listAllCars();
        
    }

}
