package com.example.springsecuritysample.config;

import com.example.springsecuritysample.jwt.JwtFilter;
import com.example.springsecuritysample.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final UsersService usersService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(DataSource dataSource, UsersService usersService, JwtFilter jwtFilter) {
        this.dataSource = dataSource;
        this.usersService = usersService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/login","/error","/info","/jwt/login").permitAll()
                //.antMatchers("/user/**").hasAnyAuthority("USER","ADMIN")
                //.antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                // all other requests need to be authenticated
                .anyRequest().authenticated()
                //.and().formLogin().loginPage("/login")
                //.usernameParameter("email")
                //.defaultSuccessUrl("/admin",true);
                //.successHandler(new LoginSuccessHandler())
                .and().oauth2Login()
                .and().rememberMe().rememberMeCookieName("remember")
                .tokenValiditySeconds(60)
                .rememberMeParameter("remember")
                .and().exceptionHandling().accessDeniedPage("/error")
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // Authentication with UserDetailsService
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
