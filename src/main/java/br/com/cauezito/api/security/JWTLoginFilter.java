package br.com.cauezito.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cauezito.api.model.Person;

/*Estabelece o gerenciador de Token*/
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	/*Configura o gerenciador de autenticação*/
	protected JWTLoginFilter(String url, AuthenticationManager authManager) {
		/*Obriga a autenticar a URL*/
		super(new AntPathRequestMatcher(url));
		
		/*Gerenciador de autenticação*/
		setAuthenticationManager(authManager);
	}

	@Override
	/*Retorna o usuário ao processar a autenticação*/
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		/*Obtém o token para validação*/
		Person person = new ObjectMapper().readValue(request.getInputStream(), Person.class);
		
		/*Retorna o usuário, login, senha e acessos*/
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
				person.getLogin(), person.getPass()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		try {
			new JWTTokenAuthService().addAuthentication(response, authResult.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
