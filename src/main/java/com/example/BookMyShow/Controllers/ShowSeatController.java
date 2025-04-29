package com.example.BookMyShow.Controllers;
import com.example.BookMyShow.RequestDTOs.LockShowSeatRequest;
import com.example.BookMyShow.Services.ShowSeatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("showSeat")
public class ShowSeatController {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ShowSeatService showSeatService;

    @PostMapping("/lockSeat")
    public ResponseEntity lockSeat(@RequestBody LockShowSeatRequest lockShowSeatRequest){

        String sessionId = httpServletRequest.getHeader("Session-ID");

        try{
            String result = showSeatService.lockSeat(lockShowSeatRequest, sessionId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e){

            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
