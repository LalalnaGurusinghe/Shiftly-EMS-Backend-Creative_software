package com.EMS.Employee.Management.System.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration // Marks as configuration class
@EnableWebSecurity // Enables Spring Security
@EnableMethodSecurity // Enables method-level security with @PreAuthorize
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    // Constructor injection
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API (handled via JWT) cuz CSRF is usually needed in
                                              // form-based login systems
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // For H2 console UI
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS config for different type of
                                                                                   // requests with different origins
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/shiftly/ems/events/all").permitAll()
                        .requestMatchers("/api/v1/shiftly/ems/teams/all").permitAll()
                        .requestMatchers("/api/v1/shiftly/ems/departments/all").permitAll()
                        .requestMatchers("/api/v1/shiftly/ems/events/{id}").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/uploads/files/**").permitAll() // <-- Allow public access to uploaded files
                        .requestMatchers("/uploads/events/**").permitAll() // <-- Allow public access to uploaded event
                        .requestMatchers("/api/v1/shiftly/ems/vacancies/**").permitAll()
                                                                           // images
                        .requestMatchers("/api/v1/shiftly/ems/ai-letter/**").permitAll()

                        // User endpoints
                        .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")
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
                        .requestMatchers("/api/v1/shiftly/ems/events/all").authenticated()
                        .requestMatchers("/api/v1/shiftly/ems/events/approve/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/shiftly/ems/events/reject/**").hasRole("ADMIN")

                        // Super admin endpoints
                        .requestMatchers("/superadmin/**").hasRole("SUPER_ADMIN")

                        // Any other request
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                )
                .authenticationProvider(authenticationProvider()) // Custom auth provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT filter

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Custom user details
        provider.setPasswordEncoder(passwordEncoder()); // Password encoder
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Strong password hashing
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Use specific origin(s)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        // Remove allowCredentials since we're using Authorization header instead of
        // cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}