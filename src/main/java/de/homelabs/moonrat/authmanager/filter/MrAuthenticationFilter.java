package de.homelabs.moonrat.authmanager.filter;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import de.homelabs.moonrat.authmanager.entity.ClientAuthenticationRequest;
import de.homelabs.moonrat.authmanager.service.MrAuthManagerClientKeyCacheManager;
import de.homelabs.moonrat.javacrypto.helper.AESHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MrAuthenticationFilter extends OncePerRequestFilter {
	Logger log = LoggerFactory.getLogger(MrAuthenticationFilter.class);
	
	@Autowired
	MrAuthManagerClientKeyCacheManager clientCacheManager;

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//every client must authenticate to get access 
		authenticateClient(request);
				
		//do the rest of the filter
		filterChain.doFilter(request, response);

	}
	
	/**
	 * expects an 'Authorization' header with clientid and client generated token
	 * create same token with persisted client key and check 
	 * @param request
	 */
	private void authenticateClient (HttpServletRequest request) {
		//TODO: how to avoid buffer overflow?
		ClientAuthenticationRequest clientAuthRequest = getClientAuthenticationRequest(request);
		
		if (checkClientToken(clientAuthRequest)) {
			setSecurityContex(clientAuthRequest, request);
		}
						
		return;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	private ClientAuthenticationRequest getClientAuthenticationRequest(HttpServletRequest request) {
		final String authHeader = request.getHeader("Authorization");
		log.debug(authHeader);
		
		if (authHeader.contains(";")) {
			String[] headerContent = authHeader.split(";");
			if ((!headerContent[0].isEmpty() && !headerContent[0].isBlank())
				&& (!headerContent[1].isEmpty() && !headerContent[1].isBlank()))
				return new ClientAuthenticationRequest(headerContent[0], headerContent[1]);
			else
				return new ClientAuthenticationRequest("",""); //avoid null pointer
		} else 
			return new ClientAuthenticationRequest("",""); //avoid null pointer
	}
	
	/**
	 * 
	 * @param clientAuthRequest
	 * @return
	 */
	private boolean checkClientToken(ClientAuthenticationRequest clientAuthRequest) {
		byte[] clientKey = clientCacheManager.getClientKeyByClientId(clientAuthRequest.clientId());
		
		if (clientKey != null) {
			logger.debug("[id:"+clientAuthRequest.clientId()+"] key(hex): "+new String(Hex.encode(clientKey)));
			
			//TODO: make dynamic
			//String iv = "1234567890123456";	
			String iv = clientCacheManager.getConfiguration().getIv();
			String input = "$$00123";
			
			try {
				String result = AESHelper.decrypt(clientAuthRequest.clientToken(), new String(clientKey), iv);
				if (result.compareTo(input) != 0 ) {
					log.warn("["+clientAuthRequest.clientId()+"] token can not be validated!");
					//do the rest of the filter
					return false;
				}
			} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
					| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
					| InvalidKeySpecException e) {
				log.warn("["+clientAuthRequest.clientId()+"] "+e.getLocalizedMessage());
				//do the rest of the filter
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param clientAuthRequest
	 * @param request
	 */
	private void setSecurityContex(ClientAuthenticationRequest clientAuthRequest, HttpServletRequest request) {
		//set security context for spring security chain
		//TODO: set client authorities (define which data, what means setDetails)
		byte[] clientKey = clientCacheManager.getClientKeyByClientId(clientAuthRequest.clientId());
		UsernamePasswordAuthenticationToken authToken = 
				new UsernamePasswordAuthenticationToken(clientAuthRequest.clientId(), clientKey, null);
		
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
		return;
	}
}
