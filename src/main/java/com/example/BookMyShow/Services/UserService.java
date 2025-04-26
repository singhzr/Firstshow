package com.example.BookMyShow.Services;

import com.example.BookMyShow.Entities.User;
import com.example.BookMyShow.Repositories.UserRepository;
import com.example.BookMyShow.RequestDTOs.AddAdminRequest;
import com.example.BookMyShow.RequestDTOs.AddUserRequest;
import com.example.BookMyShow.Response.UserResponse;
import com.example.BookMyShow.SecurityServices.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;

import java.util.List;
import java.util.Objects;

import static com.example.BookMyShow.BookMyShowApplication.userNameMap;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepository;

    public String addUser(AddUserRequest addUserRequest){
        List<User> usersList = userRepository.findAll();

        for(User user : usersList){
            if(user.getEmailId().equals(addUserRequest.getEmailId())){
                return "Email already exist";
            }
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        User user = User.builder()
                .name(addUserRequest.getName())
                .userName(addUserRequest.getUserName())
                .emailId(addUserRequest.getEmailId())
                .roles("ROLE_USER")
                .password(passwordEncoder.encode(addUserRequest.getPassword()))
                .build();

        user = userRepository.save(user);
        return "User with userId "+user.getUserId()+" has been saved to the DB";

    }

    public String addAdmin(AddAdminRequest addAdminRequest){
        List<User> usersList = userRepository.findAll();

        for(User user : usersList){
            if(user.getEmailId().equals(addAdminRequest.getEmailId())){
                return "Email already exist";
            }
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        User user = User.builder()
                .name(addAdminRequest.getName())
                .userName(addAdminRequest.getUserName())
                .emailId(addAdminRequest.getEmailId())
                .roles("ROLE_ADMIN")
                .password(passwordEncoder.encode(addAdminRequest.getPassword()))
                .build();

        user = userRepository.save(user);
        return "Admin with adminId "+user.getUserId()+" has been saved to the DB";

    }

    public String validateUser(String userName){

        if(userNameMap.containsKey(userName)){
            return userName +" not available";
        }
        else{
            return "available";
        }
    }
    public String loadUsers(){

        List<User> allUsers = userRepository.findAll();

        for(User user : allUsers){

            userNameMap.put(user.getUserName(), user.getUserId());
        }
        return "Done";
    }

    public UserResponse login(String userNameEmail, String password){

        User user = null;

        if (userNameEmail.contains("@")) {
            user = userRepository.findByEmailId(userNameEmail);
        }
        else {
            user = userRepository.findByUserName(userNameEmail);
        }

        if (user == null) {
            return UserResponse.builder()
                    .emailId("Wrong email or Username")
                    .username("Wrong email or Username")
                    .build();
        }

        String result = verify(user, userNameEmail, password);

        if(result.equals("Wrong password")){

            return UserResponse.builder()
                    .password("Wrong password")
                    .build();
        }

        else{
            return UserResponse.builder()
                    .username(user.getUserName())
                    .name(user.getName())
                    .emailId(user.getEmailId())
                    .JWToken(result)
                    .build();
        }

    }


    public String verify(User user, String userNameEmail, String password) {

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), password));

            if (authentication.isAuthenticated()) {
                List<String> rolesList = Arrays.asList(user.getRoles().split(","));
                return jwtService.generateToken(user.getUserName(), rolesList);
            }
        }
        catch (AuthenticationException e) {

            return "Wrong password";
        }

        return "Wrong password";
    }

}


/*
    Why user.setRole("ROLE_ADMIN") why not user.setRole("ADMIN");

    By default, Spring Security expects roles to have the prefix "ROLE_". When you use expressions like .hasRole("ADMIN")
    in your security configuration, Spring Security automatically appends "ROLE_" to the role name you provide.

 */

/*

    BCryptPasswordEncoder:
    This is a class provided by Spring Security used for encoding passwords using the BCrypt hashing function.

    Strength Parameter (12):
    The number 12 is the strength parameter for the BCrypt hashing algorithm. This value determines the computational
    cost of hashing the password.

    The BCryptPasswordEncoder with a strength parameter of 12 will perform the hashing process approximately Math.pow(2,12)
    (or 4096) iterations of the hashing algorithm.

    rawPassword -> hash1 -> hash2 ----> hash(Math.pow(2^12)

    We can get the hash value using BCrypt on rawPassword, but it is impossible to get rawPassword from hash value
    more the iterations on hash more difficult to get rawPassword.

    At the time of authentication when rawPassword is sent by user, the rawPassword will be converted to hash and then
    it will be matched with stored hash

 */

/*

    authManager.authenticate(...): This line leverages the AuthenticationManager to authenticate the user.
    It uses UsernamePasswordAuthenticationToken with the username and password provided in UserLoginDto.

    Checking Authentication Success: After attempting authentication, it checks if authentication.isAuthenticated()
    returns true, indicating successful authentication. If authenticated, it calls jwtService.generateToken(...)
    to create and return a JWT.

    JWT Generation on Success: jwtService is presumably responsible for creating a JWT with the username as the
    token’s payload or claims. This token can then be used by clients to access secure parts of the application.

    Returning Failure String: If authentication fails, it simply returns "fail"; alternatively, you could handle
    this more securely with custom exceptions or error handling.

 */

/*
    Default Flow (with UsernamePasswordAuthenticationFilter):

    User submits login form (POST to /login with username and password).

    UsernamePasswordAuthenticationFilter kicks in — it:

    Extracts username and password from the request.

    Creates a UsernamePasswordAuthenticationToken.

    Passes it to the AuthenticationManager for authentication.

    The AuthenticationManager delegates to the proper AuthenticationProvider (usually DaoAuthenticationProvider), which:

    Loads the user via UserDetailsService.

    Verifies the password.

    Returns an authenticated Authentication object (with roles, authorities, etc.).

    If successful: sets authentication in SecurityContext.
 */