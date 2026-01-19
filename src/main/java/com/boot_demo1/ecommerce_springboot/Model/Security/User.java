package com.boot_demo1.ecommerce_springboot.Model.Security;

import com.boot_demo1.ecommerce_springboot.Model.Cart;
import com.boot_demo1.ecommerce_springboot.Model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_Id")
    private Long userId;
    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "username")
    private String userName;
    @NotNull
    @Size(min = 3, max = 150)
    @Column(name = "password")
    private String password;
    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "email", nullable = false, unique = true)
    private String email;



    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;


    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Product> products;

    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_address",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<Address> addresses = new ArrayList<>();


    @ToString.Exclude
    @OneToOne (mappedBy = "user" ,cascade = {CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REFRESH})
    private Cart cart;



}
