package com.omnischool.config;

import com.omnischool.entity.User;
import com.omnischool.enums.UserRole;
import com.omnischool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Creates (or updates) a default admin user.
 *
 * <p>Controlled via configuration/env:
 * <ul>
 *   <li>APP_ADMIN_EMAIL (default admin@omnischool.tn)</li>
 *   <li>APP_ADMIN_PASSWORD (default Admin123!)</li>
 *   <li>APP_ADMIN_FIRST_NAME (default Admin)</li>
 *   <li>APP_ADMIN_LAST_NAME (default User)</li>
 *   <li>APP_ADMIN_RECREATE (default false) - if true, force-create/update admin on each startup</li>
 * </ul>
 */
@Configuration
public class DataInitializer {

    // Commented out to allow manual admin registration via /api/auth/register-admin endpoint
    // Uncomment if you want automatic admin creation on startup
    /*
    @Bean
    CommandLineRunner initAdmin(
            UserRepository userRepository,
            PasswordEncoder encoder,
            @Value("${app.admin.email:admin@omnischool.tn}") String adminEmail,
            @Value("${app.admin.password:Admin123!}") String adminPassword,
            @Value("${app.admin.first-name:Admin}") String adminFirstName,
            @Value("${app.admin.last-name:User}") String adminLastName,
            @Value("${app.admin.recreate:false}") boolean recreate
    ) {
        return args -> {
            User admin = userRepository.findByEmail(adminEmail).orElse(null);

            if (admin == null) {
                admin = User.builder()
                        .email(adminEmail)
                        .role(UserRole.ADMIN)
                        .isActive(true)
                        .build();
            }

            // If recreate is enabled, always overwrite fields & password.
            if (recreate || admin.getId() == null) {
                admin.setFirstName(adminFirstName);
                admin.setLastName(adminLastName);
                admin.setRole(UserRole.ADMIN);
                admin.setIsActive(true);
                admin.setPassword(encoder.encode(adminPassword));
                userRepository.save(admin);
            }
        };
    }
    */
}
