package com.ms.polling;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EntityScan("com.ms.polling.domain")
@EnableJpaRepositories("com.ms.polling.repository")
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.ms.polling.*" })
@Configuration
public class DemoConfiguration {

}
