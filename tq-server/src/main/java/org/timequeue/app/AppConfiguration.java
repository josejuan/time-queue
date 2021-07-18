package org.timequeue.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.timequeue.auth.AuthProvider;

import java.time.ZoneId;

@Configuration
@EnableScheduling
@EnableWebSecurity
public class AppConfiguration extends WebSecurityConfigurerAdapter {
    // TODO define per user
    public static final ZoneId USER_ZONE_ID = ZoneId.of("Europe/Madrid");

    @Autowired
    private AuthProvider authProvider;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .httpBasic()

                .and().antMatcher("/p/**").authorizeRequests().anyRequest().authenticated()

                // we use basic auth over https, no csrf attack is possible
                .and().csrf().disable();
    }
}