package id.nolimit.api.blogpost.entity;

import org.springframework.security.core.userdetails.UserDetails;

public interface LoginAbleUser extends UserDetails {

    Long getUserId();
    String getEmail();

}
