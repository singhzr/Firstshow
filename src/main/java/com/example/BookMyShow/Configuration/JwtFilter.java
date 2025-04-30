package com.example.BookMyShow.Configuration;

import com.example.BookMyShow.SecurityServices.JWTService;
import com.example.BookMyShow.SecurityServices.MyUserDetailsServiceTelusko;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            //Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjpbIlJPTEVfU1RVREVOVCIsIlJPTEVfUkFORE9NIl0sInN1YiI6InJpeWEiLCJpYXQiOjE3MzAwMjM3MzMsImV4cCI6MTczMDAyMzg0MX0.gT1xf39H_i3dS37KB19LudhuGsrxKj7b7xQ4_bgOG8I

            String token = null;
            String username = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtService.extractUserName(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = context.getBean(MyUserDetailsServiceTelusko.class).loadUserByUsername(username);

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException e) {
            // Prepare JSON response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            HashMap<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "JWT expired");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("expiredAt", e.getClaims().getExpiration());

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}


/*
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getWriter(), errorResponse);

    ObjectMapper mapper = new ObjectMapper();
    What it does: ObjectMapper is a class from the Jackson library used for converting between Java objects and JSON.
    This line creates a new instance of ObjectMapper and assigns it to the variable mapper.

    Why it matters: This instance is your tool for converting Java objects into JSON strings (serialization)
    or converting JSON strings back into Java objects (deserialization).


    mapper.writeValue(response.getWriter(), errorResponse);
    What it does: This line tells the ObjectMapper to:
      1. Convert the Java object errorResponse into JSON format, and

      2. Write that JSON directly to the output stream of the HTTP response (i.e., response.getWriter()).

    Breakdown:
    response.getWriter(): Gets the PrintWriter associated with the HTTP response, which allows you
    to write text (like HTML or JSON) back to the client’s browser.

    writeValue(...): This is a method of ObjectMapper that serializes the given object into JSON and
    writes it to the specified destination. The destination here is the response's writer.
 */


/*
    The @Autowired ApplicationContext context; line injects the Spring ApplicationContext into your JwtFilter class.
    This gives access to the entire Spring container, allowing it to retrieve any Spring-managed bean dynamically.
 */

/*
    The purpose of OncePerRequestFilter is to ensure that the filter logic within it is executed only once per request,
    regardless of how many times the filter might be invoked in the processing of that request
    (due to forwards, includes, or other complex request flows).

    Here’s why OncePerRequestFilter is useful, even if JwtFilter runs for every incoming request:

    1.Single Execution Guarantee: It guarantees that your filter's logic is processed only once per request.
    This is particularly useful in scenarios where multiple filters could potentially be invoked multiple times
    within the same request cycle, ensuring no duplicate processing occurs.

    2.Efficient Resource Usage: By ensuring that your filter's logic runs only once, it helps reduce unnecessary
    computations or operations that could lead to performance degradation, especially in more complex filters.

    3.Simpler State Management: If your filter manages state or performs actions that should not be repeated
    (like logging, token verification, etc.), using OncePerRequestFilter simplifies that process.
    You can rely on the framework to handle the single execution, freeing you from writing additional logic to prevent it.

    4.Stateless Nature of JWT: While JwtFilter does run for every request to validate the JWT token, this
    behavior is in line with how JWT authentication works. Each request is stateless, and validation must occur
    independently. However, within the context of the request, if you had multiple layers or calls that could lead
    to JwtFilter being triggered more than once, OncePerRequestFilter would ensure that the JWT validation logic
    is still only processed a single time for that request.

    5.Extensibility: If you extend OncePerRequestFilter, you can build more complex logic without worrying about
    unintended re-execution, which can lead to bugs or inconsistent behavior.

 */

/*

    In the doFilterInternal method of the JwtFilter class, the parameters HttpServletRequest request,
    HttpServletResponse response, and FilterChain filterChain are essential for handling HTTP requests and
    responses.

    Here’s what each parameter represents:

    1. HttpServletRequest request
    Purpose: This parameter represents the incoming HTTP request.
    Usage: You can use it to:
    Access request headers (e.g., to retrieve the JWT from the Authorization header).
    Get request parameters or attributes.
    Determine the HTTP method (GET, POST, etc.) or the request URI.
    Perform any necessary checks or manipulations related to the incoming request.

    2. HttpServletResponse response
    Purpose: This parameter represents the outgoing HTTP response.
    Usage: You can use it to:
    Modify the response (e.g., setting response headers or status codes).
    Send error messages or custom responses.
    Write output to the response body if needed.

    3. FilterChain filterChain
    Purpose: This parameter represents the chain of filters that the request will go through.
    Usage: You use it to:
    (i)Call filterChain.doFilter(request, response) to pass the request and response to the next filter in the
    chain or to the target resource (e.g., a controller).
    (ii)Ensure that the request continues through the filter chain after processing in your filter, maintaining
    the flow of the request handling.

 */

/*

    SecurityContextHolder.getContext().setAuthentication(authToken);
    Setting the authentication in the SecurityContextHolder in a stateless JWT (JSON Web Token) authentication
    system is necessary for several key reasons, each contributing to the overall security and functionality
    of your application.

    Here’s a deeper look at why this step is essential:

    1. Authorization Checks
    Access Control: When a request is made to a secured endpoint, Spring Security uses the authentication object
    to determine whether the user has the appropriate roles or authorities to access that resource.
    Without setting the authentication, Spring Security won't have the necessary information to make this decision,
    leading to unauthorized access.

    Example: If you have a method protected by @PreAuthorize("hasRole('ADMIN')"), this check relies on the presence of
    valid authentication in the SecurityContextHolder. If the authentication isn't set, the user will always be denied
    access.


    Consider the following simplified flow if authentication is not set:

    1.Incoming Request: A client sends a request with a valid JWT.

    2.JWT Filter: The filter extracts and validates the JWT but does not set the authentication in the
    SecurityContextHolder.

    3.Protected Endpoint Access:
    a)When the client tries to access a protected endpoint:
    b)Authorization Check: Spring Security checks the authentication context and finds it null.
    c)Access Denied: The request is denied, and the client receives a 403 Forbidden response or similar.
 */

/*
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    1.Encapsulating User Information:
    The authToken encapsulates all necessary information about the user who is making the request, including their
    roles and privileges.

    2.Authentication Context:
    By creating this token and setting it in the SecurityContextHolder, you effectively mark the user as authenticated
    for the duration of that request. This is essential for enabling Spring Security to enforce security constraints.

    3.Authorization Decision:

    Once the authentication token is set in the SecurityContextHolder, Spring Security can use it to perform
    authorization checks throughout the application. It checks whether the authenticated user has the necessary
    authorities to access specific resources or execute certain actions.

    4.Integration with Spring Security:
    The UsernamePasswordAuthenticationToken is widely used in Spring Security for handling authentication.
    By using this standard class, you ensure compatibility with Spring Security's authentication mechanisms and features.

 */

/*

 UserDetails userDetails = context.getBean(MyUserDetailsServiceTelusko.class).loadUserByUsername(username);

 returns an instance of UserDetails that contains information about a user,

 */