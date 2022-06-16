package com.jovinn.capstoneproject.security;

import com.jovinn.capstoneproject.filter.JwtRequestFilter;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.service.custom.CustomUserDetailsService;
import com.jovinn.capstoneproject.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

//    @Autowired
//    public SecurityConfig(UserRepository userRepository, CustomUserDetailsService customUserDetailsService,
//                          JwtAuthenticationEntryPoint unauthorizedHandler, JwtRequestFilter jwtRequestFilter) {
//        this.customUserDetailsService = customUserDetailsService;
//        this.unauthorizedHandler = unauthorizedHandler;
//        this.jwtRequestFilter = jwtRequestFilter;
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/**").permitAll().anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
//                .antMatchers("/api/auth/**").permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
