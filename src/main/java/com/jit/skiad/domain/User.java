package com.jit.skiad.domain;


import java.util.Date;
import java.util.List;

public class User {
    private Integer id;

    private String username;
//    @NotBlank
//    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_-]{5,19}$",message = "密码不匹配")
    private String password;
    private String image;
    private List<Role> roles;
    private Date register_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Date getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", roles=" + roles +
                ", register_time=" + register_time +
                '}';
    }
}