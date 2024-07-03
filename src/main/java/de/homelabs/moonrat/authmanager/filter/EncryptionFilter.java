package de.homelabs.moonrat.authmanager.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import de.homelabs.moonrat.authmanager.service.MrAuthManagerCryptService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EncryptionFilter implements Filter {
	Logger log = LoggerFactory.getLogger(EncryptionFilter.class);
	
	@Autowired
	MrAuthManagerCryptService cryptService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.debug("in do filter before ");
		

		ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper((HttpServletResponse) response);
		
		chain.doFilter(request, responseCacheWrapperObject);

        byte[] responseArray = responseCacheWrapperObject.getContentAsByteArray();
        String responseStr = new String(responseArray, responseCacheWrapperObject.getCharacterEncoding());
        String result = cryptService.encrypt(responseStr);
        
        responseCacheWrapperObject.setHeader("X-MR-Encrypted", "true");    
        responseCacheWrapperObject.resetBuffer();
        responseCacheWrapperObject.getWriter().write(result);
        responseCacheWrapperObject.copyBodyToResponse();
        
		log.debug("in do filter after, new response "+result);
	}

}
