package com.example.reactive.reactive.chapter06;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
public class PasswordVerificationServiceImpl implements PasswordVerificationService {

    private final WebClient webClient;

    public PasswordVerificationServiceImpl() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Override
    public Mono<Void> check(String raw, String encoded) {

        return webClient.post()
                .uri("/check")
                .body(BodyInserters.fromPublisher(Mono.just(new PasswordDTO(raw, encoded)), PasswordDTO.class))
                .exchange()
                .flatMap(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        log.info("CHECK SUCCESS");
                        return Mono.empty();
                    } else {
                        log.info("CHECK FAILURE");
                        return Mono.error(new RuntimeException("BAD CREDENTIALS"));
                    }
                });
    }

    public static void main(String[] args) {

        PasswordEncoder encoder = new BCryptPasswordEncoder(18);

        PasswordVerificationServiceImpl verificationService = new PasswordVerificationServiceImpl();

        String pass = "12345";

        verificationService.check(pass, encoder.encode(pass)).block();
    }
}
