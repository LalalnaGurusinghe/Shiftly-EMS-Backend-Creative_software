package com.EMS.Employee.Management.System.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource; // inject our CORS bean

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          UserDetailsService userDetailsService,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // enable CORS and set our CorsConfigurationSource
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authorizeHttpRequests(auth -> auth
                        // Allow preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/shiftly/ems/events/all").permitAll()
                        .requestMatchers("/api/v1/shiftly/ems/teams/all").permitAll()
                        .requestMatchers("/api/v1/shiftly/ems/events/{id}").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/uploads/files/**").permitAll()
                        .requestMatchers("/uploads/events/**").permitAll()
                        .requestMatchers("/api/v1/shiftly/ems/vacancies/**").permitAll()
                        .requestMatchers("/api/v1/shiftly/ems/ai-letter/**").permitAll()
                        .requestMatchers("/users/**").permitAll()

                        // User endpoints
                        .requestMatchers("/api/v1/shiftly/ems/employee/self-update").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/timesheets/**").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/leaves/**").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/claims/**").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/referrals/**").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/education/**").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/teams/my").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/team-members/my-team").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/projects/my").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/events/my/**").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/employee/**").hasRole("USER")
                        .requestMatchers("/api/v1/shiftly/ems/employee/name/**").hasRole("USER")

                        // Admin endpoints
                        .requestMatchers("/api/v1/shiftly/ems/teams/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/team-members/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/departments/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/projects/all").authenticated()
                        .requestMatchers("/api/v1/shiftly/ems/projects/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/timesheets/all").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/leaves/all").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/claims/all").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/referrals/all").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/events/all").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/events/approve/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/events/reject/**").hasRole("ADMIN")

                        // Super admin
                        .requestMatchers("/superadmin/**").hasRole("SUPER_ADMIN")

                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
