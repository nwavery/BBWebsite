package net.bst.springboot.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import net.bst.springboot.springsecurity.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{
	private final UserService userService;

	@Autowired
	public SecurityConfiguration(UserService userService)
	{
		this.userService = userService;
	}

	// providing access to some type of urls for reg ,login, css, jss,html images etc
	// Remove the PasswordEncoder bean from here, it's moved to PasswordEncoderConfig
	/*
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	*/

	@Bean
	public DaoAuthenticationProvider authenticationProvider(BCryptPasswordEncoder passwordEncoder)
	{
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder);
		return auth;
	}

	// Define the SecurityFilterChain bean
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http
				.authorizeRequests(authorize -> authorize
						.antMatchers(
								"/registration**",
								"/js/**",
								"/css/**",
								"/img/**",
								"/webjars/**"
						).permitAll()
						.anyRequest().authenticated()
				)
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.permitAll()
				)
				.logout(logout -> logout
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout")
						.permitAll()
				);
		// Authentication provider setup is handled by the DaoAuthenticationProvider bean

		return http.build();
	}
}
