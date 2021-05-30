package com.example.springdatasecurity.configuration;

import com.example.springdatasecurity.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         //говорим что хотим использовать собственную аутентфикацию
         auth.authenticationProvider(authenticationProvider());
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
         DaoAuthenticationProvider  auth = new DaoAuthenticationProvider();
         //используй наш собственный userservice для работы с пользователем
         auth.setUserDetailsService(userService);
         // все пароли в базе даненых должны храниться в bcrypt hash
         auth.setPasswordEncoder(passwordEncoder());
         return auth;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//         http.authorizeRequests()
//                 .anyRequest().permitAll()
//                 .antMatchers("/register/**").permitAll()
//                 .antMatchers("/admin/**").hasRole("ADMIN")
//                 .antMatchers("/products/**").hasRole("ADMIN")
//                 .antMatchers("/shop/order/**").authenticated()
//                 .antMatchers("/profile/**").authenticated()
//                 .and()
//                 .formLogin()
//                 .loginPage("/login")
//                 .loginProcessingUrl("/authenticateTheUser")
////                 .successHandler(customAuthenticationSuccessHandler)
//                 .permitAll()
//                 .and()
//                 .logout()
//                 .logoutSuccessUrl("/")
//                 .permitAll();
        http.authorizeRequests()//here we write what kind of requests we need  to secure
//                .anyRequest().permitAll()//любые запросы в нашем приложении доступны абсолютно всем
                .antMatchers("/products/show/**").hasAnyRole("ADMIN")//если какойто блок сайта начинается на
                .anyRequest().permitAll()
//                .antMatchers("/products/**").hasAnyRole("ADMIN")
                //  /secured, мы можем его защитить (ЧТОБ ЗАЙТИ В ЭТОТ БЛОК НУЖНЫ ПРАВА АДМИНА)
                .and()
                .formLogin() // применяем свою форму
//                .httpBasic();
//                .loginPage("/login")// адрес формы
//                .loginProcessingUrl("/authenticateTheUser") // пост запрос для формы url
                .permitAll();

    }
}
