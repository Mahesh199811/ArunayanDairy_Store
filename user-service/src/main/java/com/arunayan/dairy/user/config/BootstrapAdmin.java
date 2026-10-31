package com.arunayan.dairy.user.config;

import com.arunayan.dairy.user.domain.Role;
import com.arunayan.dairy.user.domain.UserAccount;
import com.arunayan.dairy.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapAdmin implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapAdmin.class);

    private final BootstrapAdminProperties properties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public BootstrapAdmin(
            BootstrapAdminProperties properties,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.properties = properties;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        boolean hasEmail = properties.email() != null && !properties.email().isBlank();
        boolean hasPassword = properties.password() != null && !properties.password().isBlank();
        if (!hasEmail && !hasPassword) {
            logger.warn("No bootstrap administrator configured. Set BOOTSTRAP_ADMIN_EMAIL and BOOTSTRAP_ADMIN_PASSWORD to enable organization administration.");
            return;
        }
        if (!hasEmail || !hasPassword) {
            throw new IllegalStateException(
                    "BOOTSTRAP_ADMIN_EMAIL and BOOTSTRAP_ADMIN_PASSWORD must both be configured.");
        }
        if (properties.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalStateException("BOOTSTRAP_ADMIN_PASSWORD must be no more than 72 UTF-8 bytes.");
        }

        String email = properties.email().trim().toLowerCase(Locale.ROOT);
        var existingUser = userRepository.findByEmailIgnoreCase(email);
        if (existingUser.isPresent()) {
            UserAccount account = existingUser.get();
            if (!account.getRoles().contains(Role.ADMIN)) {
                account.addRole(Role.ADMIN);
                userRepository.save(account);
                logger.info("Granted administrator role to the configured bootstrap account.");
            }
            return;
        }
        userRepository.save(new UserAccount(
                properties.name().trim(),
                email,
                passwordEncoder.encode(properties.password()),
                Set.of(Role.ADMIN)));
        logger.info("Bootstrap administrator created for configured address {}.", email);
    }
}
