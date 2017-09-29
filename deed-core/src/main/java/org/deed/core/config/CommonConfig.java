package org.deed.core.config;

import org.deed.core.boot.AbstractDeedServce;
import org.deed.core.boot.DefaultAbstractDeedServce;
import org.deed.core.register.DefaultAbstractRegister;
import org.deed.montior.MonitorTimer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
	
	@Bean
	public MonitorTimer monitorTimer() {
		return new MonitorTimer();
	}
	
	@Bean
	public DefaultAbstractRegister defaultAbstractRegister() {
		return new DefaultAbstractRegister();
	}
	
	@Bean
	public AbstractDeedServce abstractDeedServce() {
		return new DefaultAbstractDeedServce();
	}
}
