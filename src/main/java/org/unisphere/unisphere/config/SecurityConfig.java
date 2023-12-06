package org.unisphere.unisphere.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.unisphere.unisphere.auth.filter.MemberAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

	private final ServerConfig serverConfig;
	private final ClientConfig clientConfig;
	private final AuthenticationManager jwtAuthenticationManager;
	private final AccessDeniedHandler jwtAccessDeniedHandler;
	private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
	private final AuthenticationFailureHandler oauth2AuthenticationFailureHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//		@formatter:off
		httpSecurity
				.formLogin().disable()
				.addFilterAfter(new MemberAuthenticationFilter(), BearerTokenAuthenticationFilter.class)
				.cors()
					.configurationSource(corsConfigurationSource())
				.and()
				.csrf().disable()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.oauth2ResourceServer()
					.bearerTokenResolver(new DefaultBearerTokenResolver())
					.jwt()
						.authenticationManager(jwtAuthenticationManager)
					.and()
					.accessDeniedHandler(jwtAccessDeniedHandler)
					.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and()
				.oauth2Login()
					.successHandler(oauth2AuthenticationSuccessHandler)
					.failureHandler(oauth2AuthenticationFailureHandler)
					.userInfoEndpoint()
					.and()
					.authorizationEndpoint()
						.baseUri(serverConfig.getOauth2LoginEndpoint())
					.and()
					.redirectionEndpoint()
						.baseUri(serverConfig.getOauth2CallbackEndpoint())
		;
		return httpSecurity.build();
//		@formatter:on
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOrigin(clientConfig.getClientUrl());
		configuration.addAllowedOrigin(clientConfig.getClientHttpUrl());
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public WebSecurityCustomizer ignoreCustomizer() {
		return web -> web.ignoring()
				.antMatchers("/v3/api-docs/**", "/swagger-ui/**");
	}
}
