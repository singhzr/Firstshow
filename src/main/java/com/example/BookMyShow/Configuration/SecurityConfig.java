package com.example.BookMyShow.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private UserDetailsService userDetailsService; //Implementation of UserDetailsService will be injected here

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/users/welcomeAdmin").hasRole("ADMIN")
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated())

                        .httpBasic(Customizer.withDefaults())
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .formLogin(Customizer.withDefaults())
                        .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }
}
/*

    The line .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) places your custom jwtFilter in
    the Spring Security filter chain so that it runs before the UsernamePasswordAuthenticationFilter.

    Here’s what that accomplishes:
    Intercept Requests Early: By running before UsernamePasswordAuthenticationFilter, the jwtFilter can check each
    incoming HTTP request for a JWT (JSON Web Token).

    JWT Validation: The jwtFilter typically validates the JWT to ensure it’s valid, authentic, and unexpired.

    User Authentication: If the JWT is valid, jwtFilter extracts user details from it, such as roles or permissions,
    and sets these in Spring's security context, marking the user as authenticated.

    Bypassing Default Authentication: After jwtFilter completes its work, other filters like
    UsernamePasswordAuthenticationFilter don't need to perform authentication, as the user is already validated by the token.

    In short, this line configures Spring Security to check the JWT first for authentication, allowing stateless
    JWT-based security.

 */

/*

    AuthenticationManager

        We define an AuthenticationManager bean like this to make the Spring Security AuthenticationManager available
        for dependency injection throughout your application.

        Here’s why this can be necessary:
        Custom Authentication Needs: If you need to perform custom authentication logic
        (e.g., authenticate a user in a REST controller), you’ll need access to an AuthenticationManager instance.
        This bean allows you to directly call authenticationManager.authenticate(...).



        AuthenticationConfiguration config in your method signature is a Spring Security class that provides access
        to the application's AuthenticationManager.

        Here’s why it's helpful and how it works:
        1.Accessing the AuthenticationManager: AuthenticationConfiguration acts as an entry point to retrieve the
        AuthenticationManager that Spring Security configures. This is useful when you don’t want to build an
        AuthenticationManager from scratch, as it provides the one that’s already configured by Spring Boot.

        2.Simplifying Bean Creation: By injecting AuthenticationConfiguration, you avoid the complexity of manually
        setting up an AuthenticationManager. Instead, config.getAuthenticationManager() automatically returns the
        default AuthenticationManager.

 */

/*
    What is an AuthenticationProvider?

        1.Interface for Authentication Logic: AuthenticationProvider is an interface in Spring Security that defines
         a standard way to authenticate users. It lays out the methods that any authentication provider must implement,
         providing a consistent way to handle authentication.

        2.Responsible for Authenticating Users: It contains the logic to verify whether a user's credentials
        (like username and password) are valid. If the credentials are valid, it creates and returns an Authentication object
        that contains user details and authorities (permissions).

        3. Custom Authentication Logic: If you need custom logic to authenticate users, you can implement your own
        AuthenticationProvider. This allows you to tailor the authentication process to fit your specific requirements.


    How Does It Work?

        Here’s a simplified overview of the authentication process involving AuthenticationProvider:

        1.User Submits Credentials: A user submits their login information (username and password) through a login form.

        2.Authentication Filter: The submitted credentials are captured by an authentication filter
         (like UsernamePasswordAuthenticationFilter). This filter is part of the Spring Security filter chain.

        3.Authentication Manager: The authentication filter delegates the authentication request to an AuthenticationManager.

        4.Provider Invocation: The AuthenticationManager uses one or more AuthenticationProvider instances to
        authenticate the user:

            It calls the authenticate method of the AuthenticationProvider with the user’s credentials.
            The AuthenticationProvider checks the credentials against the user store (like a database).
            If the credentials are valid, it returns an Authentication object.
            If invalid, it throws an exception.

 */

/*

    UserDetailsService is a key interface in the Spring Security framework. It provides a way to retrieve
    user information needed for authentication from various sources, such as databases, in-memory lists,
    or external services.

    Here’s a detailed overview:

    Purpose of UserDetailsService:
    1.User Retrieval: The primary purpose of UserDetailsService is to load user-specific data.
    It defines the method loadUserByUsername(String username), which is called by Spring Security during
    the authentication process to fetch user details based on the username.

    2.Integration with Spring Security: It integrates seamlessly with Spring Security's authentication mechanism.
    By providing user data through this service, it enables Spring Security to perform authentication checks.

    3.Custom User Data Management: It allows for custom implementations to manage how user data is retrieved,
    whether from a database, an in-memory store, or other sources, offering flexibility to adapt to various application needs.

    Key Components:

    1.loadUserByUsername(String username):

    This method is the core of the UserDetailsService. It takes a username as input and returns a
    UserDetails(as it is a interface we have to implement it and override its methods which define the
    object with all the details) object, which contains the user's credentials and roles.
    If the user is not found, it typically throws a UsernameNotFoundException.

    2.UserDetails Interface:
    The returned UserDetails object contains essential information about the user, including:
    Username
    Password
    Granted authorities (roles/permissions)
    Account status (e.g., whether the account is locked or expired)


    How It Works:

    1.User Authentication Process:

    When a user attempts to authenticate (e.g., logging in), Spring Security calls loadUserByUsername()
    with the provided username.
    The implementation of UserDetailsService retrieves the user details from the appropriate source (e.g., database).
    The UserDetails object returned is used to verify the password and determine the user's roles and permissions.

    2.Role Assignment:
    The roles retrieved from the UserDetails object determine the access control for the authenticated user.
    Spring Security uses this information to grant or deny access to specific resources.

 */

/*

    The purpose of the line provider.setUserDetailsService(userDetailsService); is to explicitly set the
    UserDetailsService that the DaoAuthenticationProvider will use to load user details during the authentication process.

    Here’s why this is important:
    1.Linking the Service: By calling setUserDetailsService(userDetailsService), you link a specific implementation
    of UserDetailsService to the DaoAuthenticationProvider. This tells the provider where to find user details
    when it needs to perform authentication.

    2.Customization: The UserDetailsService can be customized to interact with your specific data source (like a database)
    and define how to retrieve user information. By setting your own implementation, you control how user data is
    loaded and what happens if a user is not found.

    3.Decoupling: The DaoAuthenticationProvider does not know how to load user data by itself. It relies on the
    UserDetailsService you provide. This decoupling allows you to easily change the source of user data
    (for example, switching from a database to an in-memory user store) without changing the
    authentication provider's implementation.

    4.Dependency Injection: In a Spring context, you typically want to use dependency injection to manage your components.
    Setting the UserDetailsService in the provider allows Spring to manage the lifecycle of the UserDetailsService
    implementation you provide, making your configuration more maintainable.

    Summary :
    Even though loadUserByUsername(String username) is called automatically during authentication,
    the setUserDetailsService(userDetailsService) method is essential for defining which UserDetailsService
    implementation to use for that process. Without this configuration, the DaoAuthenticationProvider wouldn’t
    know where to find the user details, and authentication would fail.

        The DaoAuthenticationProvider internally calls:
        userDetailsService.loadUserByUsername(username);

 */


/*
    Flow of Api after hitting from client side :
    It goes through the unchanged default filters provided by Spring Security.
    It also goes through any custom filters or modified filters you have defined in your SecurityFilterChain.

 */


/*
    The HttpSecurity parameter in your method allows you to configure security settings for your web application
    In Spring Security, the HttpSecurity object is provided by the Spring Security framework itself when you define a
    method with HttpSecurity as a parameter in a @Bean method. The framework uses a mechanism called dependency injection
    to create and pass instances of the required classes to your methods.

 */

/*
    When an API request is made, it goes through the SecurityFilterChain defined in your configuration as well as
    several other default filters provided by Spring Security.

    @EnableWebSecurity is a Spring Security annotation used to enable and configure web security in a Spring Boot application.

    1.What @EnableWebSecurity Does
    Activates Security: When you add @EnableWebSecurity to a Spring Boot application, it tells Spring to set up its
    default security mechanisms. This includes securing your application’s HTTP endpoints by default.

    Allows Custom Configurations: With @EnableWebSecurity, you can override the default settings to customize how
    users authenticate and authorize access to your application’s endpoints

    2.How @EnableWebSecurity Works with Spring Boot

    When used with Spring Boot:
    Enables Spring Security: It tells Spring Boot that you want to take control over the security settings,
    so it applies any configurations you define.
    Overrides Default Settings: Spring Boot’s default security settings (like default basic authentication) are replaced
    by your custom configurations, allowing for more flexible, custom setups.

    3.Why @EnableWebSecurity Is Important
    Fine-Grained Security Control: Customize which endpoints require authentication, what types of users can access
    which resources, and define the authentication method.

    4.Without @EnableWebSecurity: Spring Boot’s default security setup applies, securing all endpoints with basic
    authentication and a default user.
    With @EnableWebSecurity: You can customize security settings, allowing for specific access control,
    custom authentication, and more flexible configuration to meet application requirements.

    Using @EnableWebSecurity is generally the best approach for production applications, as it lets you tailor
    security settings to your application’s needs.

 */

/*
    1.SecurityFilterChain is a central concept in Spring Security that defines a series of filters that will process
    HTTP requests in a specific order.
    When configured properly, it allows you to control security for your web application in a granular way.

    2.Key Features of SecurityFilterChain
    Chain of Filters: It is a sequence of filters that handle various security concerns, such as authentication,
    authorization, and session management. Each filter in the chain is responsible for a specific aspect of security.

    3.Customizable: You can define your own SecurityFilterChain beans to specify which filters should be applied and
    in what order. This customization allows you to create a tailored security setup based on the needs of your application.

    4.Defined in Configuration: Typically, you define a SecurityFilterChain in a configuration class annotated
    with @Configuration and @EnableWebSecurity.

 */

/*

    If you don’t define a SecurityFilterChain in a Spring Boot application that uses Spring Security,
    several default behaviors and configurations will automatically apply. Here's what you can expect:

    Default Behavior Without a Defined SecurityFilterChain

    1.Basic Authentication:
    Spring Boot will enable basic authentication by default for all endpoints. You’ll be required to provide a
    username and password to access any resource.
    The default user will typically have the username user and a randomly generated password displayed in the
    console at startup.

    2.Access to All Endpoints Restricted:
    By default, all endpoints will be protected. Users must authenticate before accessing any URL, as there are
    no specified rules allowing unauthenticated access.

    3.Default Password Generation:
    Since there’s no custom configuration, Spring Boot will generate a password for the default user every time
    you start the application, which can lead to confusion if you’re trying to remember it.

    4.Lack of Customization:
    Without a SecurityFilterChain, you will not be able to:
    a)Customize which endpoints are public and which require authentication.
    b)Define specific roles or permissions for different users.
    c)Implement more complex authentication mechanisms (like OAuth2, JWT, etc.).
    d)Control session management, such as stateless or stateful sessions.
 */

/*

    CSRF (Cross-Site Request Forgery) is a type of web security vulnerability that allows an attacker to trick a
    user into performing actions on a web application in which the user is authenticated, without the user's consent.
    This can lead to unauthorized actions being taken on behalf of the user, such as transferring funds, changing account
    details, or submitting forms.

    How CSRF Works
    1.User Authentication: The user logs into a web application, and the application creates a session
    (typically stored as a cookie) to identify the user for future requests.

    2.Attacker Setup: The attacker creates a malicious website or script that contains a request designed to be
    executed on the target application.

    3.User Interaction: The user, while still authenticated to the target application, visits the attacker’s
    website or interacts with a malicious email or link.

    4.Triggering the Request: The malicious website or script sends a request to the target application
    (e.g., submitting a form, changing settings, etc.) using the user's authenticated session (cookie).

    5.Execution of Unwanted Actions: Since the user is already authenticated, the target application processes the
    request as if it came from the legitimate user, leading to unauthorized actions being executed.

 */

/*

    If csrf is enabled(in SpringBoot it is by default enabled)

    1.CSRF Token Generation:
    When a user accesses the application and a session is created, Spring Security generates a unique CSRF token for
    that session.
    This token is stored server-side (typically in the user's session) and is also sent to the client (browser) as part
    of the response, often embedded in forms or included in the response headers.

    2.Sending the CSRF Token:
    When the user submits a form or makes a state-changing request (like POST, PUT, or DELETE), the CSRF token
    must be included in the request.
    This is usually done by adding the CSRF token as a hidden input field in HTML forms.

 */

/*
    The CSRF token is designed to be more secure than a session ID in several key ways. Here’s why a hacker typically
    cannot access the CSRF token, even if they can access the session ID:

    -> Same-Origin Policy:
    a)Browser Enforcement: The same-origin policy is a security measure implemented by browsers that prevents
    scripts from one origin (domain) from accessing data on another origin. This means that if a user is logged
    into a legitimate site, a malicious site cannot read the CSRF token embedded in that site's HTML or JavaScript.

    b)Limited Cross-Origin Access: Even if a user is tricked into visiting a malicious site, that site cannot
    read the CSRF token from the legitimate site, as it would be considered cross-origin access.
 */

/*

    Location of Session id vs Location of csrf token

    1.Session ID

        a)Location:

        i)Cookies: The session ID is typically stored in a cookie on the client side. When a user logs in, the server
        generates a session ID and sends it to the client, where it is stored in the browser's cookie storage.

        (ii)HTTP Headers: Sometimes, it can also be passed in HTTP headers (though this is less common).

        b)Transmission:
        The session ID is sent automatically with every HTTP request to the server that includes the cookie, allowing
        the server to identify the user's session without needing to explicitly include it in the request.

    2.CSRF Token

        a)Location:

        (i)Embedded in Forms: The CSRF token is typically included as a hidden input field in HTML forms. This ensures
        it is submitted with the form data when the user performs a state-changing action (e.g., submitting a form).

        b)Transmission:

        (i)The CSRF token must be explicitly included by the application when making a state-changing request.
        It is not sent automatically like the session ID and must be managed by the application code
 */

/*
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/student/add/**").permitAll()
                        .requestMatchers("/student/welcome").hasAnyRole("STUDENT","ADMIN")
                        .requestMatchers("/admin/add/**").permitAll()
                        .requestMatchers("/admin/welcome").hasRole("ADMIN")
                        .anyRequest().authenticated())

                        .httpBasic(Customizer.withDefaults())
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .formLogin(Customizer.withDefaults())
                        .build();
    }

    1. .csrf(customizer -> customizer.disable())

    Lambda Expression: The customizer parameter is part of a lambda expression. It represents an instance of
    CsrfHttpSecurityConfigurer, which provides methods to customize CSRF protection.
    Method Chaining: By using customizer.disable(), you are calling the disable() method on this instance,
    effectively turning off CSRF protection for the application.

    2. .authorizeHttpRequests(request -> request.anyRequest().authenticated())

    .authorizeHttpRequests(...): This method starts the configuration for securing HTTP requests. It allows you to
    specify which requests should be authorized and under what conditions.

    request -> ...: This is a lambda expression where request is an instance of HttpSecurity. It allows you to define
    rules for how requests should be authorized.

    The requestMatchers(...) can include both public (no login) and role-based protected endpoints.
    The .anyRequest().authenticated() is a fallback for all other URLs, saying: “If not listed above,
    user must be logged in — any role is fine.”

    3. .httpBasic(Customizer.withDefaults())
    .httpBasic(...): This method enables HTTP Basic authentication for your application. HTTP Basic authentication
    is a simple authentication scheme, where the client sends credentials (username and password) in the
    Authorization header of HTTP requests. If you use this you will not require form login hence you can also
    send requests using POSTMAN. If you will not define this a login form will be displayed in POSTMAN and you will not
    able to send requests from POSTMAN.

    Customizer.withDefaults(): This is a method that provides default configurations for HTTP Basic authentication.
    By using this, you accept the default behavior without needing to specify additional customization options.

    4. .formLogin(Customizer.withDefaults())

    .formLogin(...): This method enables form-based authentication in your Spring application.
     This allows users to authenticate via a web form rather than using HTTP Basic authentication.

    Customizer.withDefaults(): This method provides default configurations for form login. By using this,
    you are opting for the standard settings that Spring Security provides without additional customizations.

     the Customizer functional interface can be used to customize a variety of components and configurations
     related to security features.

    5. .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

    .sessionManagement(...): This method is used to configure how sessions are managed in your Spring Security application.
    It allows you to specify various settings related to session creation, concurrent sessions, and session fixation.

    session -> ...: This is a lambda expression where session is an instance of SessionManagementConfigurer.
    It allows you to define specific session management settings.

    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS): This line specifies the session creation
    policy for the application.

    The STATELESS policy aligns with the principles of RESTful architecture, which is inherently stateless.
    Each request from the client must contain all the information necessary for the server to understand and
    process it, without relying on any previously stored context(the server does not create or store session data).

    With a stateless configuration, applications often use token-based authentication methods (like JWT).
    Clients must send authentication tokens with each request, allowing the server to verify the user's
    identity without maintaining a session.
 */