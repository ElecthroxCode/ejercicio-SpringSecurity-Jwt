package com.school.school_app.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.school.school_app.util.UtilSecurityJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

	private UtilSecurityJWT utilSecurityJWT;

	public JwtTokenValidator(UtilSecurityJWT utilSecurityJWT) {
		this.utilSecurityJWT = utilSecurityJWT;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (jwtToken != null) {
			
			jwtToken = jwtToken.replace("Bearer ", "");
			
			try {
				DecodedJWT decodedJWT = utilSecurityJWT.validateToken(jwtToken);
				String username = utilSecurityJWT.extractUsername(decodedJWT);
				String stringAuthorities = utilSecurityJWT.getSpecificClaim(decodedJWT, "Authorities").asString();
				Collection<? extends GrantedAuthority> authorities = AuthorityUtils
						.commaSeparatedStringToAuthorityList(stringAuthorities);

				SecurityContext securityContext = SecurityContextHolder.getContext();
				Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
				securityContext.setAuthentication(authentication);
				SecurityContextHolder.setContext(securityContext);
			} catch (Exception e) {
//				 Si ocurre un error en la validación, como un token no válido o expirado
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Token inválido o expirado.");
				return; // No continuar con el filtro si el token es inválido
			}
		}

		filterChain.doFilter(request, response);
	}

}
