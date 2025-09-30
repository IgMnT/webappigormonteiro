package br.edu.iff.ccc.webappigormonteiro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        @Value("${app.security.enabled:true}")
        private boolean securityEnabled;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                if (!securityEnabled) {
                        http
                                        .csrf(AbstractHttpConfigurer::disable)
                                        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                                        .httpBasic(AbstractHttpConfigurer::disable)
                                        .formLogin(AbstractHttpConfigurer::disable)
                                        .logout(AbstractHttpConfigurer::disable);
                        return http.build();
                }

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/", "/principal", "/home", "/login", "/error", "/error/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/desafios/**").hasAnyRole("ADMIN", "AUTHOR", "VISITOR")
                        .requestMatchers("/desafios/**").hasAnyRole("ADMIN", "AUTHOR")
                        .requestMatchers("/categorias/**").hasAnyRole("ADMIN", "AUTHOR")
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/principal", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/error/403"))
                .csrf(Customizer.withDefaults());

        return http.build();
    }
}
