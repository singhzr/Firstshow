package com.example.BookMyShow.Controllers;

import com.example.BookMyShow.RequestDTOs.AddTicketRequest;
import com.example.BookMyShow.RequestDTOs.TicketImageRequest;
import com.example.BookMyShow.Response.ShowTicketResponse;
import com.example.BookMyShow.Services.TicketService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    HttpServletRequest httpServletRequest;
    @PostMapping("/bookTicket")
    public ResponseEntity bookTicket(@RequestBody AddTicketRequest addTicketRequest){

        String sessionId = httpServletRequest.getHeader("Session-ID");
        try{
            String result = ticketService.bookTicket(addTicketRequest, sessionId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e){

            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/viewTicket")
    public ResponseEntity viewTicket(@RequestParam("ticketId")Integer ticketId){

        try{
            ShowTicketResponse showTicketResponse = ticketService.viewTicket(ticketId);

            return new ResponseEntity<>(showTicketResponse, HttpStatus.OK);
        }
        catch (Exception e){

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/viewAllTicketOfUser/{username}")
    public ResponseEntity viewAllTicketOfUser(@RequestParam("username")String username){

        try{
            List<ShowTicketResponse> showTicketResponse = ticketService.viewAllTicketOfUser(username);

            return new ResponseEntity<>(showTicketResponse, HttpStatus.OK);
        }
        catch (Exception e){

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/sendTicket")
    public ResponseEntity<String> sendTicketImage(@RequestBody TicketImageRequest request) throws MessagingException, IOException {
        ticketService.sendImageEmail(request.getEmail(), "Your Ticket", request.getImageBase64());
        return ResponseEntity.ok("Email sent");
    }

    @DeleteMapping("/cancelTicket")
    public ResponseEntity cancelTicket(@RequestParam("ticketId")Integer ticketId){

        try{
            String result = ticketService.cancelTicket(ticketId);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
