package link.team71.emailserver.models;

import jakarta.persistence.*;
import link.team71.emailserver.models.enumRole.Role;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User implements UserDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;
    @NonNull
    @Column(nullable = false)
    public String username;
    @NonNull
    @Column(nullable = false)
    private String password;
    @NonNull
    @Column(unique = true, nullable = false)
    private String email;
    @NonNull
    private String phone;
    private String token;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean accountNonExpired;
    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public @NonNull String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
