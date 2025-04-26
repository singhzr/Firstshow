package com.example.BookMyShow.SecurityServices;

import com.example.BookMyShow.Entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipalTelusko implements UserDetails {
    String username;

    String password;

    List<GrantedAuthority> authorities;

    public UserPrincipalTelusko(User user){

        this.username = user.getUserName();
        this.password = user.getPassword();

        this.authorities = new ArrayList<>();
        String roles[] = user.getRoles().split(",");

        for(String role: roles){
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
            authorities.add(simpleGrantedAuthority);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
/*

    1.Authentication Process: When a user attempts to log in, Spring Security uses an AuthenticationProvider
    (often a UserDetailsService implementation) to load user details. The loadUserByUsername() method from your
    UserDetailsService returns an instance of UserPrincipalTelusko.

    2.During Authentication: After retrieving the UserDetails, Spring Security uses it to authenticate the user.
    The AuthenticationManager calls methods like getPassword() and getUsername() to compare the provided credentials
    with the stored ones.

    3.Authorization Process: When authorizing access to specific resources, Spring Security checks the roles and authorities
    of the authenticated user. The getAuthorities() method is called to determine what permissions the user has.

    4.Session Management: When managing sessions, Spring Security may call the isEnabled(), isAccountNonExpired(),
    isAccountNonLocked(), and isCredentialsNonExpired() methods to determine the user's account status.

 */

/*

    GrantedAuthority and SimpleGrantedAuthority are both related to Spring Security's handling of user
    permissions and roles, but they serve different purposes in the framework.

    Here’s a comparison of the two:

    1.GrantedAuthority :
    a)Interface: GrantedAuthority is an interface defined in Spring Security that represents an authority granted
    to an Authentication object.

    b)Purpose: It provides a way to specify an authority (like roles or permissions) that can be granted to a user.
    This is the fundamental building block for access control in Spring Security.

    c)Implementation: You can create your own implementations of GrantedAuthority if you need custom behavior beyond
    what is provided by the default implementations.

    2.SimpleGrantedAuthority :
    a)Class: SimpleGrantedAuthority is a concrete implementation of the GrantedAuthority interface.

    b)Purpose: It is used to represent a simple authority, typically defined as a string
    (like role names, e.g., "ROLE_USER" or "ROLE_ADMIN").

    c)Usage: This class is commonly used in applications to grant specific roles or permissions to users without requiring
     complex logic. It simplifies the process of authority management by providing a straightforward implementation.
 */