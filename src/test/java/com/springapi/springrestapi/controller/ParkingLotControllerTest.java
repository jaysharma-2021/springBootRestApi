package com.springapi.springrestapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

//import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;

import com.springapi.springrestapi.controller.ParkingLotController;
import com.springapi.springrestapi.model.Car;
import com.springapi.springrestapi.model.Slot;
import com.springapi.springrestapi.model.Token;
import com.springapi.springrestapi.service.ParkingLot;
@WebMvcTest(ParkingLotController.class)
class ParkingLotControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ParkingLot service;
	
	    @Test
	    public void initiateSlots() throws Exception
	    {
	        Slot slot = new Slot(1);
	        ArrayList<Slot> slotObj = new ArrayList<Slot>();
	        slotObj.add(slot);

	        when(service.initiateLot(5)).thenReturn(slotObj);

	        mockMvc.perform(post("/initiateLot")
	                .param("NumberOfslot","5"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].slotNumber").value("1"))
	                .andExpect(jsonPath("$[0].slotFree").value("true"));
	        verify(service, times(1)).initiateLot(5);
	        verifyNoMoreInteractions(service);
	    }
	    @Test
	    public void parkTheCar() throws Exception
	    {
	        Token token = new Token("12345678",new Slot(1),new Car("Blue","Ts100"));

	        when(service.parkTheCar("Blue","Ts100")).thenReturn(token);
	        mockMvc.perform(post("/carEntry")
	                        .param("carColor","Blue")
	                        .param("carNumber","Ts100")
	                )
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$['carDetails'].carColor").value("Blue"))
	                .andExpect(jsonPath("$['carDetails'].carNumber").value("Ts100"))
	                .andExpect(jsonPath("$.tokenNumber").value("12345678"));
	        verify(service, times(1)).parkTheCar("Blue","Ts100");
	        verifyNoMoreInteractions(service);
	    }
	    
	@Test
	public void getAllcarList() throws Exception{
		
		Token token = new Token("12345678",new Slot(1),new Car("Blue","Ts100"));
		ArrayList<Token> tokenObj = new ArrayList<Token>();
		tokenObj.add(token);
		when(service.listAllCars()).thenReturn(tokenObj);
		
		 mockMvc.perform(get("/carList"))	
         .andExpect(status().isOk())
         .andExpect(jsonPath("$[0].tokenNumber").value("12345678"))
		 .andExpect(jsonPath("$[0].carDetails.carColor").value("Blue"))
		 .andExpect(jsonPath("$[0].carDetails.carNumber").value("Ts100"));
         
		 verify(service, times(1)).listAllCars();
		 verifyNoMoreInteractions(service);
	}
	
	@Test
	public void getCarByColorOrNumber() throws Exception{
      Token token = new Token("12345678",new Slot(1),new Car("Blue","Ts100"));
		ArrayList<Token> tokenObj = new ArrayList<Token>();
		tokenObj.add(token);
      when(service.searchCar("Ts100")).thenReturn(tokenObj);

      mockMvc.perform(get("/carSearchByColorOrNumber/{colorOrNumber}", "Ts100"))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$[0].tokenNumber").value("12345678"))
     		 .andExpect(jsonPath("$[0].carDetails.carColor").value("Blue"))
     		 .andExpect(jsonPath("$[0].carDetails.carNumber").value("Ts100"));
                      
      verify(service, times(1)).searchCar("Ts100");
      verifyNoMoreInteractions(service);
	}
	
	@Test
	public void should_Return404_When_CarNotFound() throws Exception{
		
		  when(service.searchCar("")).thenReturn(null);
		  ResultActions resultActions = mockMvc.perform(get("/carSearchByColorOrNumber/{colorOrNumber}", "")); 
		  resultActions.andExpect(status().isNotFound()); //"status": 404, "error": "Not Found",
	}
	    
    @Test
    public void UnParkTheCar() throws Exception
    {
        String responseString = "Car entry removed";

        when(service.unParkTheCar("12345678")).thenReturn(responseString);

        mockMvc.perform(delete("/unParkTheCar/{tokenNumber}", "12345678"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", is("Car entry removed")));

        verify(service, times(1)).unParkTheCar("12345678");
        verifyNoMoreInteractions(service);

    }

}
