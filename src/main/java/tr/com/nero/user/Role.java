package tr.com.nero.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    ADMIN(1L, "Admin", "Yönetici"),
    CLIENT(2L, "Client", "Müşteri");

    private final Long id;
    private final String role;
    private final String desc;

    Role(Long id, String role, String desc){
        this.id = id;
        this.role = role;
        this.desc = desc;
    }

    @Override
    public String getAuthority() {
        return this.desc;
    }
}
