package com.school.school_app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.school.school_app.service.serviceImpl.UserDetailsServiceImpl;
import com.school.school_app.util.UtilSecurityJWT;

@Configuration
@EnableWebSecurity //habilita la seguridad web

public class SecurityConfig {
	
	private final UtilSecurityJWT utilSecurityJWT; //nuetsro filtro configurado
	
	private final AuthenticationConfiguration athenticationConfiguration;
	@Autowired
	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, UtilSecurityJWT utilSecurityJWT) {
		this.athenticationConfiguration = authenticationConfiguration;
		this.utilSecurityJWT = utilSecurityJWT;
	}
	
	
	//***PASO 1: configurar el securityFilterChain
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecutity) throws Exception {
		//aqui configuranos las condiciones a las urls:
		return httpSecutity
				.csrf( csrf -> csrf.disable()) //desabilitanos esta proteccion ya que es una app web
				.httpBasic(Customizer.withDefaults())//se usa en apps con login y no se usan token.
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //configuramos sin estado
				.authorizeHttpRequests(http -> {
					//endpoint publicos
					http.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();//permite a todos
					//endpoints privado
					http.requestMatchers(HttpMethod.GET, "/method/public-secured").hasAnyRole("USER");//los que tienen autirizacion con ROLE_USER
					http.requestMatchers(HttpMethod.GET, "/method/public-create").hasAuthority("CREATE");//los que tienen autorización CREATE
					http.anyRequest().denyAll(); //cualquier otro request no especificado denegar el acceso
				})
				.addFilterBefore(new JwtTokenValidator(utilSecurityJWT), BasicAuthenticationFilter.class) //agregamos nuestro filtro antes del fitro authentication
				.build();
	}
	
	//***PASO 2: configurar el authenticacionManager.
	//Se necesita un AuthenticactionConfiguration para obtene un AuthenticacionManager:
	@Bean
	public AuthenticationManager authenticationManager() throws Exception{
		return this.athenticationConfiguration.getAuthenticationManager();
	}
	
	//***PASO 3: Configurar el AuthenticationProvider,
	// usareos el DaoAuthenticationProvider para establecer PasswordEncoder y UserDetailsService:
	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsServiceImpl);
		return provider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//Este objeto conecta con la BBDD.
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails userDetails = User.withUsername("manuel").password("1234")
//				.roles("ADMIN").authorities("READ", "CREATE").build(); //definimos los roles
//		return new InMemoryUserDetailsManager(userDetails); //definimos los permisos
//	}
	
	
}
