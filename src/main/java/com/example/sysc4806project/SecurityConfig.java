package com.example.sysc4806project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration used to configure Spring's Security (as of spring boot 2.7.0+)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Used to configure http security to define specific URLs that should be secured.
     * In our case every path must be authenticated except the login page.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        //.requestMatchers("/owner").hasRole("OWNER") // restrict this template to owner's only
                        //.requestMatchers("/").permitAll() // allow index access without login
                        //.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated() //all other URLs are blocked
                )
                //custom login page (no httpBasic)
                .formLogin((form) -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/customer",true)
                                .permitAll() // everybody has access to this page
                ).logout((logout) -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                //.csrf().disable().cors();
                //To allow any ajax functions to work
                .csrf((csrf) -> csrf
                    .disable());


        //Timeout
        /*
        http.sessionManagement()
                .sessionFixation()
                .newSession()
                .invalidSessionUrl("/login")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login")
                .and()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionAuthenticationErrorUrl("/login")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false);*/

        return http.build();
    }

    /**
     * Used to configure web security
     * antMatchers replaced with requestMatchers
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //return (web) -> web.ignoring().requestMatchers("/hello");
        // Ignore requests that match what's specified, in this case h2 route shouldn't interfere
        return web -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/h2-console/**"));
    }


    /**
     * Temporary: Sets up an in memory user store used for hard coding 2 user configurations.
     * a user can either have role OWNER or USER (customer).
     * Used for prototyping before switching to a cloud database.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 =
                User.withUsername("owner1")
                        .password(passwordEncoder().encode("ownerPassword"))
                        .roles("OWNER")
                        .build();
        UserDetails user2 =
                User.withUsername("user1")
                        .password(passwordEncoder().encode("userPassword"))
                        .roles("USER")
                        .build();

       return new InMemoryUserDetailsManager(user1,user2);
    }

    /**
     * Define password encoder used to encrypt passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
