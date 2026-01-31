package org.example.Security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import org.example.Security.DebugAuthenticationProvider;
import org.example.Security.security.JwtAuthenticationFilter;
import org.example.Security.security.JwtService;
import org.example.Security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Configuration
@EnableWebSecurity
public class CustomConfigurationWebSecurity {
    private PasswordEncoder passwordEncoder;
    private CustomUserDetailsService customUserDetailsService;
private JwtService jwtService;
    @Autowired
    public CustomConfigurationWebSecurity( JwtService jwtService, PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {

        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
        return authManagerBuilder.build();
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",      // Rest авторизации
                                "/api/loginPage",    // web Страница логина
                                "/api/registrationPage", // web Страница регистрации
                                "/api/",             // Главная (редирект)
                                "/error",            // Страница ошибок
                                "/css/**",           // Статика
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/.well-known/**"
                        ).permitAll()
                        .requestMatchers("/api/home").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**","/api/home/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .authenticationProvider(debugAuthenticationProvider)
//                .build();
//    }
//    @Bean//Миши
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config
//    ) {
//        return config.getAuthenticationManager();
//    }



//
//    @Bean//Фильтр Димы
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/api/loginNew", "/css/**", "/js/**", "/webjars/**").permitAll()
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/home/**", "/api/profile/**").hasAnyRole("USER", "ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/api/loginNew")
//                        .loginProcessingUrl("/api/login")
//                        .defaultSuccessUrl("/api/home")
//                        .failureUrl("/api/loginNew?error=true")
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/api/logout")
//                        .logoutSuccessUrl("/api/loginNew?logout=true")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
//        basicAuthenticationEntryPoint.setRealmName("WhoAreYou");
//
//        return http
//                .httpBasic(config -> config
//                        .authenticationEntryPoint(basicAuthenticationEntryPoint)
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/loginNew").permitAll()
//                        .requestMatchers("/home/**").hasAllRoles("USER","ADMIN")
//                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
//                        .anyRequest().authenticated() // Все остальные требуют аутентификации
//                )
//                // Добавляем фильтр для перенаправления с корня на unauthorized
//                .addFilterBefore(new OncePerRequestFilter() {
//                    @Override
//                    protected void doFilterInternal(HttpServletRequest request,
//                                                    HttpServletResponse response,
//                                                    FilterChain filterChain)
//                            throws ServletException, IOException {
//
//                        // Проверяем, если запрос не содержит стартовую директорию отправляем в заглушку
//                        if (!request.getRequestURI().contains("/api/")) {
//
//                            // Перенаправляем на страницу unauthorized
//                            response.sendRedirect("http://localhost:8080/api/loginNew");
//                            return;
//                        }
//
//                        filterChain.doFilter(request, response);
//                    }
//                }, BasicAuthenticationFilter.class)
//                .build();
//    }


}
