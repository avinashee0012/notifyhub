package com.rebellion.notifyhub.config.Audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class Auditor implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("SYSTEM"); // TODO change to authenticated user later
    }

}
