package com.example.reactive.reactive.chapter06;

import reactor.core.publisher.Mono;

public interface PasswordVerificationService {

    Mono<Void> check(String raw, String secured);
}
