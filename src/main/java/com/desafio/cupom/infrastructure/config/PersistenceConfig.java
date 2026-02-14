package com.desafio.cupom.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.desafio.cupom.infrastructure.persistence")
@EnableTransactionManagement
public class PersistenceConfig {
}

