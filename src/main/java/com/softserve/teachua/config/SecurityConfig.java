package com.softserve.teachua.config;

import com.softserve.teachua.constants.RoleData;
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
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(1)
public class SecurityConfig {
    private static final String ADMIN = RoleData.ADMIN.getRoleName();
    private static final String USER = RoleData.USER.getRoleName();
    private static final String MANAGER = RoleData.MANAGER.getRoleName();
    private final JwtFilter jwtFilter;
    private CustomUserDetailsService customUserDetailsService;
    private CustomOAuth2UserService customOAuth2UserService;
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .disable().csrf().disable().cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/", "/main").permitAll()
                .antMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/swagger",
                        "/swagger-resources/**",
                        "/swagger-resources").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/manifest.json").permitAll()
                .antMatchers("/favicon**").permitAll()
                .antMatchers("/upload/**").permitAll()
                .antMatchers(HttpMethod.GET, "/club/*", "/clubs", "/challenge", "/challenge/*", "/challenge/**",
                        "/challenges/task/**", "/challenge/task/**", "/marathon", "/marathon/*", "/marathon/task/*",
                        "/about", "/banners", "/banner/*", "/centers", "/center/*", "/service")
                .permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers(
                        HttpMethod.GET,
                        "/challengeUA",
                        "/challengeUA/registration",
                        "/challengeUA/task/*").permitAll()
                .antMatchers(HttpMethod.GET, "/user/*").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/user/**").hasAnyRole(USER, ADMIN, MANAGER)
                .antMatchers("/verify", "/verifyreset").permitAll()
                .antMatchers("/roles").hasRole(ADMIN)
                .antMatchers("/index").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers(HttpMethod.GET, "/logs").permitAll().antMatchers(HttpMethod.DELETE, "/logs").permitAll()
                .and() // oauth2
                .oauth2Login().authorizationEndpoint().baseUri("/oauth2/authorize")
                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                .and().redirectionEndpoint().baseUri("/oauth2/callback/*")
                .and().userInfoEndpoint().userService(customOAuth2UserService)
                .and().successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and() // jwtFilter and logout
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/signout")).logoutSuccessUrl("/signin");
        return http.build();
    }

    @Autowired
    public void setCustomUserDetailsService(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Autowired
    public void setCustomOAuth2UserService(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Autowired
    public void setoAuth2AuthenticationSuccessHandler(OAuth2AuthenticationSuccessHandler oAuth2AuthSuccessHandler) {
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthSuccessHandler;
    }

    @Autowired
    public void setoAuth2AuthenticationFailureHandler(OAuth2AuthenticationFailureHandler oAuth2AuthFailureHandler) {
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthFailureHandler;
    }

    @Autowired
    public void setHttpCookieOAuth2Authorization(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth;
    }
}
