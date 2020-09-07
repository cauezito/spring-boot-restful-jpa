package br.com.cauezito.api.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.cauezito.api.ApplicationContextLoad;
import br.com.cauezito.api.model.Person;
import br.com.cauezito.api.repository.PersonRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAuthService {
	/* Tempo para o token expirar */
	private static final long EXPIRATION_TIME = 1728000000;
	/* Senha para compor a autenticação */
	private static final String SECRET = "AWESOME96";
	/* Prefixo padrão de token */
	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";


	/* Gerando token de autenticação e adicionando ao cabeçalho de resposta HTTP */
	public void addAuthentication(HttpServletResponse response, String login) throws Exception {
		Person person = ApplicationContextLoad.getAppContext().getBean(PersonRepository.class)
				.findPersonByLogin(login);		
		
		// Se o usuário for encontrado
		if (person != null) {
			String token;
			if(person.getToken() != null && !person.getToken().isEmpty()){
				token = person.getToken();
			} else {
				/* Montagem do token */
				String JWT = Jwts.builder().setSubject(login)
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS512, SECRET).compact();

				token = TOKEN_PREFIX + " " + JWT;			
				person.setToken(token);
				ApplicationContextLoad.getAppContext().getBean(PersonRepository.class).save(person);						
			}			
			response.addHeader(HEADER_STRING, token);
			response.getWriter().write("{\"Authorization\": \"" + token + "\"}");			
		}

		allowCors(response);

	}

	/* Retorna o usuário validado com TOKEN ou NULL */
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		allowCors(response);
		String token = request.getHeader("Authorization");
		
		try {

			if (token != null) { // se existir um token
				String newToken = token.replace(TOKEN_PREFIX, "").trim();

				String personToken = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(newToken).getBody()
						.getSubject();
				if (personToken != null) {
					// Todos os recursos (controllers, services, daos, repositories etc) que foram
					// carregados na memória quando o spring subiu
					Person person = ApplicationContextLoad.getAppContext().getBean(PersonRepository.class)
							.findPersonByLogin(personToken);
					if (person != null) {
						// Se o token salvo no usuário for o mesmo recuperado pelo localstorage
						if (newToken.equalsIgnoreCase(person.getToken().replace(TOKEN_PREFIX, "").trim())) {
							return new UsernamePasswordAuthenticationToken(person.getLogin(), person.getPass(),
									person.getAuthorities());
						}
					}
				}
			}
		} catch (ExpiredJwtException e) {
			try {
				response.getOutputStream().println("O seu TOKEN expirou! Entre novamente para continuar.");
			} catch (IOException e1) {
			}
		}

		return null;
	}

	private void allowCors(HttpServletResponse response) {
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}

		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}

		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		}

	}

}
