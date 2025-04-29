package com.example.BookMyShow.Controllers;

import com.example.BookMyShow.Entities.User;
import com.example.BookMyShow.Repositories.UserRepository;
import com.example.BookMyShow.RequestDTOs.AddAdminRequest;
import com.example.BookMyShow.RequestDTOs.AddUserRequest;
import com.example.BookMyShow.Response.UserResponse;
import com.example.BookMyShow.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addUser")
    private ResponseEntity addUser(@RequestBody AddUserRequest addUserRequest){
        String result = userService.addUser(addUserRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getUserByEmailId")
    public ResponseEntity getUserByEmailId(@RequestParam("emailId") String emailId){

        try{

            User user = userRepository.customMethod(emailId);

            UserResponse userResponse = UserResponse.builder()
                    .name(user.getName())
                    .emailId(user.getEmailId())
                    .build();

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/validateUser/{userName}")
    public ResponseEntity validateUser(@PathVariable("userName") String userName){

        String validation = userService.validateUser(userName);

        return new ResponseEntity<>(validation, HttpStatus.OK);
    }
    @GetMapping("/loadUser")
    public ResponseEntity loadUser(){
        return new ResponseEntity<>(userService.loadUsers(), HttpStatus.OK);
    }

    @GetMapping("/login/{userNameEmail}/{password}")
    public ResponseEntity login(@PathVariable("userNameEmail") String userNameEmail,
                                @PathVariable("password") String password){

        UserResponse isValid = userService.login(userNameEmail, password);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }


    //------------------------------------------------------------------------------------------------------------------


    @PostMapping("/addAdmin")
    private ResponseEntity addAdmin(@RequestBody AddAdminRequest addAdminRequest){
        String result = userService.addAdmin(addAdminRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("/welcomeAdmin")
    private ResponseEntity welcomeAdmin(){

        return new ResponseEntity<>("WELCOME ADMIN", HttpStatus.OK);
    }

    @GetMapping("/welcome")
    public String welcome(){

        return "Welcome to PUBLIC area";
    }

    @GetMapping("/csrfToken")
    public CsrfToken getCsrfToken(HttpServletRequest request) {

        return (CsrfToken) request.getAttribute("_csrf");
    }

    @GetMapping("/sessionId")
    public String greet(HttpServletRequest request) {

        return "Welcome your Session Id is "+request.getSession().getId();
    }

    @GetMapping("/detailsOfHttpRequest")
    public String getRequestDetails(HttpServletRequest request) {
        StringBuilder details = new StringBuilder();

        // Request Method
        details.append("Method: ").append(request.getMethod()).append("\n");

        // Request URI
        details.append("Request URI: ").append(request.getRequestURI()).append("\n");

        // Request URL
        details.append("Request URL: ").append(request.getRequestURL()).append("\n");

        // Query String
        details.append("Query String: ").append(request.getQueryString()).append("\n");

        // Remote Address
        details.append("Remote Address: ").append(request.getRemoteAddr()).append("\n");

        // CSRF Token
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        details.append("CSRF Token: ").append(csrfToken != null ? csrfToken.getToken() : "Not Available").append("\n");

        // Headers
        details.append("Headers: \n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            details.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
        }

        // Parameters
        details.append("Parameters: \n");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            details.append(paramName).append(": ").append(request.getParameter(paramName)).append("\n");
        }

        // Session ID
        details.append("Session ID: ").append(request.getSession().getId()).append("\n");

        // Content Type
        details.append("Content Type: ").append(request.getContentType()).append("\n");

        // Content Length
        details.append("Content Length: ").append(request.getContentLength()).append("\n");

        // Protocol
        details.append("Protocol: ").append(request.getProtocol()).append("\n");

        return details.toString();
    } // Sends all the details about HTTP request

}

/*
    The Servlet API in Java is a set of interfaces and classes in the jakarta.servlet (or previously javax.servlet)
    package that defines how web servers (like Tomcat, Jetty) handle HTTP requests and responses in Java-based web
    applications.

    HttpServletRequest is an interface in the Java Servlet API that provides methods to access the details of an
    incoming HTTP request.

    return (CsrfToken) request.getAttribute("_csrf");
    This line retrieves the CSRF token from the request attributes.
    The CSRF token is stored in the request attributes under the key "_csrf", and it is cast to CsrfToken.
    The method returns the CsrfToken object, which typically contains the token value and other information.

 */
