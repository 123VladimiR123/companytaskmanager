package com.spring.companytaskmanager.security;

import com.spring.companytaskmanager.enums.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityController {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(
                        e -> e.requestMatchers("/recover").permitAll()
                                .requestMatchers(HttpMethod.POST,"/departments").hasAuthority(Roles.ADMIN.getRole())
                                .requestMatchers(HttpMethod.PUT).hasAuthority(Roles.ADMIN.getRole())
                                .requestMatchers(HttpMethod.PATCH).hasAuthority(Roles.ADMIN.getRole())
                                .requestMatchers("/departments/create").hasAuthority(Roles.ADMIN.getRole())
                                .requestMatchers("/users/create").hasAuthority(Roles.ADMIN.getRole())
                                .requestMatchers("/actuator/**").hasAuthority(Roles.ADMIN.getRole())
                                .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .logout(e -> e.logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .formLogin(e -> e.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .requestCache(e -> new NullRequestCache())
        ;
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A,5);
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
}
