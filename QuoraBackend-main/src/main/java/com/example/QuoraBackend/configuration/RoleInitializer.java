package com.example.QuoraBackend.configuration;

import com.example.QuoraBackend.model.Role;
import com.example.QuoraBackend.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initializeRoles() {
        createRoleIfMissing("ROLE_USER");
        createRoleIfMissing("ROLE_ADMIN");
    }

    private void createRoleIfMissing(String roleName) {
        roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }
}
