package com.springapi.springrestapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.springapi.springrestapi.model.Car;
import com.springapi.springrestapi.model.Slot;
import com.springapi.springrestapi.model.Token;
@Service
public class ParkingLot {
	 ArrayList<Slot> availableSlotList;
	   private final List<Token> tokenForLot;
	   private final List<Token> historyOfParking;

	   public ParkingLot() {
	      this.tokenForLot = new ArrayList<>();
	      this.historyOfParking = new ArrayList<>();
	   }

	   public ArrayList<Slot> initiateLot(int numberOfLots) {
	      ArrayList<Slot> totalSlots = new ArrayList<Slot>();
	      for (int i = 1; i<= numberOfLots; i++) {
	         Slot getSlotAssignment = new Slot(i);
	         totalSlots.add(getSlotAssignment);
	      }

	      return this.availableSlotList = totalSlots;
	   }

	   public Token parkTheCar(String carColor, String carNumber){
	      Car car = new Car(carColor,carNumber);
	      if(isSlotAvailable()){
	         Slot availableSlot = getTheNextFreeSlot();
	         Token parkingToken = new Token(String.valueOf(System.currentTimeMillis()),availableSlot,car);
	         this.tokenForLot.add(parkingToken);
	         return parkingToken;
	      }else {
	         return null;
	      }
	   }
	   private boolean isSlotAvailable() {
	      boolean isSlotAvailable = false;

	      for(Slot slot:availableSlotList){
	         if(slot.isSlotFree()){
	            isSlotAvailable = true;
	            break;
	         }
	      }
	      return isSlotAvailable;
	   }
	   private Slot getTheNextFreeSlot() {
	      for(Slot slot : availableSlotList){
	         if(slot.isSlotFree()){
	            slot.makeSlotOccupied();
	            return slot;
	         }
	      }
	      return null;
	   }
	   
	    public List<Token> searchCar(String colorOrNumber){
	    	List<Token> searchColorObj = tokenForLot.stream().
	    		    filter(p -> p.getCarDetails().getCarColor().equals(colorOrNumber)).collect(Collectors.toList());
	    	if(searchColorObj.size() < 1) {
	    		List<Token> searchNumberObj =tokenForLot.stream().
	        		    filter(p -> p.getCarDetails().getCarNumber().equals(colorOrNumber)).collect(Collectors.toList());
	    		return searchNumberObj;
	    	}
	    	return searchColorObj;
	    }

	   public String unParkTheCar(String tokenNumber) {
	      for(Token tokenInLot:tokenForLot){
	         if(tokenInLot.getTokenNumber().equals(tokenNumber)){
	            tokenForLot.remove(tokenInLot);
	            Slot slot = tokenInLot.getSlotDetails();
	            int slotNumber = slot.getSlotNumber();
	            return removeCarFromSlot(tokenInLot,slotNumber);
	         }
	         return "No token found";
	      }
	      return null;
	   }


	   private String removeCarFromSlot(Token token, int slotNumber) {
	      for (Slot removeEntry:availableSlotList){
	         if(removeEntry.getSlotNumber() == slotNumber){
	            removeEntry.makeSlotFree();
	            Token historyToken = token.updateCheckOutTime();
	            historyOfParking.add(historyToken);
	            return "Car entry removed";
	         }

	      }
	      return null;
	   }

	   public List<Token> historyOfParking(){
	      return historyOfParking;
	   }
	   
	   public List<Token> listAllCars(){
			return tokenForLot;
	    }
}

