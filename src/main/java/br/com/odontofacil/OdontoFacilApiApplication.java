package br.com.odontofacil;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@SpringBootApplication
public class OdontoFacilApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OdontoFacilApiApplication.class, args);
	}

	@Bean
	public DataSource dataSource() throws Exception {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
	    dataSourceBuilder.url("jdbc:mysql://localhost:3306/odontoFacil");
	    dataSourceBuilder.username("root");
	    dataSourceBuilder.password("");
	        	
	    return dataSourceBuilder.build();
	}
	
	
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		private static final String USUARIO_POR_LOGIN = "SELECT login, senha, ativo FROM usuario "
				+ "WHERE login = ?";

		 private static final String PERMISSAO_POR_USUARIO = "SELECT u.login"
		 + "f.nomeCompleto FROM funcionario_tem_permissao up "
		 + "JOIN funcionario u ON u.id = up.idFuncionario " + 
		 "JOIN permissao p ON p.id = up.idPermissao "
		 + "WHERE u.login = ?";

		@Autowired
		private DataSource dataSource;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// Para qualquer requisição (anyRequest) é preciso estar
			// autenticado (authenticated).
			// http.httpBasic().and().authorizeRequests()
			// .antMatchers("/lib/**", "/js/**", "/", "/index.html",
			// "/cadastrar_psicologo.html",
			// "/salvarPsicologo")
			// .permitAll().anyRequest().authenticated().and().formLogin().loginPage("/index.html")
			// .usernameParameter("login").passwordParameter("senha").and().logout().and().requiresChannel()
			// .anyRequest().requiresSecure().and().csrf()
			// .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

			http
	    	.httpBasic().and()
	    	.authorizeRequests()
	        	.antMatchers("/lib/**", "/js/**", "/plugins/**", "/", "/index.html").permitAll()
	        	.anyRequest().authenticated().and()
	        .formLogin()
				.loginPage("/index.html")
				.usernameParameter("login")
				.passwordParameter("senha").and()
	        .logout().and()
	        .csrf()
	        	.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());	
			
			/*
			http.httpBasic().and().authorizeRequests()
					.antMatchers("/lib/**", "/js/**", "/plugins/**","/", "/index.html", "/cadastrar_psicologo.html",
							"/salvarCliente")
					.permitAll().anyRequest()
					.authenticated().and()
					.formLogin()
					.loginPage("/index.html")
					.usernameParameter("login")
					.passwordParameter("senha")
					.and()
					.logout()
					.and()
					.csrf()
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
			 */

		}

		@Override
		public void configure(AuthenticationManagerBuilder builder) throws Exception {
			builder
        	.jdbcAuthentication()
        		.dataSource(dataSource)
        		.passwordEncoder(new BCryptPasswordEncoder())
        		.usersByUsernameQuery(USUARIO_POR_LOGIN)
        		.authoritiesByUsernameQuery(PERMISSAO_POR_USUARIO)
        	.rolePrefix("ROLE_");
		}
	}
}
