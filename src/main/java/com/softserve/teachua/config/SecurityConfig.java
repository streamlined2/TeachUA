package com.softserve.teachua.config;

import com.softserve.teachua.security.CustomUserDetailsService;
import com.softserve.teachua.security.JwtFilter;
import com.softserve.teachua.security.RestAuthenticationEntryPoint;
import com.softserve.teachua.security.oauth2.CustomOAuth2UserService;
import com.softserve.teachua.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.softserve.teachua.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.softserve.teachua.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/manifest.json").permitAll()
                .antMatchers("/favicon**").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/club/*",
                        "/clubs",
                        "/challenge",
                        "/challenge/*",
                        "/about",
                        "/centers",
                        "/center/*",
                        "/service").permitAll()
                .antMatchers(HttpMethod.GET,"/user/*").permitAll()
                .antMatchers(HttpMethod.GET," /admin/*","/manager/*").permitAll()
                .antMatchers("/verify").permitAll()
                .antMatchers("/roles").hasRole("ADMIN")
                .antMatchers("/index", "/api/signup", "/api/signin", "/api/signout", "/api/verify").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/**","/api/verify").hasAnyRole("USER", "ADMIN", "MANAGER")
                .antMatchers(HttpMethod.PUT, "/api/user/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                .antMatchers(HttpMethod.GET, "/api/cities", "/api/city/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/city").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/city/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/city/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/categories/**", "/api/category/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/centers/search/advanced").permitAll()
                .antMatchers(HttpMethod.POST, "/api/category").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/category/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/category/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/newslist", "/api/newslist/search").permitAll()
                .antMatchers(HttpMethod.GET, "/api/club/**", "/api/clubs/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/club").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/upload-excel").permitAll() // TODO: only for admins
                .antMatchers(HttpMethod.POST, "/api/load-excel-to-db").permitAll() // TODO: only for admins
                .antMatchers(HttpMethod.GET, "/api/download-database-sql").permitAll() // TODO: only for admins
                .antMatchers(HttpMethod.PUT, "/api/club").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/complaint", "/api/feedback").hasAnyRole("USER", "MANAGER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/center/**", "/api/centers/**", "/api/feedbacks/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/search").permitAll()
                .antMatchers(HttpMethod.GET, "/api/questions", "/api/question/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/question").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/question/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/question/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/contact-types", "/api/districts/**").permitAll()
                //TODO: only for admin
                .antMatchers(HttpMethod.GET, "/api/logs").permitAll()
                .antMatchers(HttpMethod.GET, "/logs").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/logs").permitAll()
                .antMatchers(HttpMethod.DELETE, "/logs").permitAll()
                .antMatchers(HttpMethod.GET, "/api/log/**").permitAll()
                .antMatchers(HttpMethod.GET, "/log/**").permitAll()

                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/api/upload-image").permitAll()
                .antMatchers("/api/users","/api/user/update").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/signout")).logoutSuccessUrl("/signin");
    }
}
