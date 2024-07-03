package de.homelabs.moonrat.authmanager.service;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.homelabs.moonrat.authmanager.entity.Client;
import de.homelabs.moonrat.javacrypto.helper.AESHelper;

//TODO: implement encryption with byte[] result
@Service
public class MrAuthManagerCryptService {
	Logger log = LoggerFactory.getLogger(MrAuthManagerCryptService.class);

	@Autowired
	MrAuthManagerClientKeyCacheManager clientCacheManager;

	public String encrypt(String input) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String key = new String(clientCacheManager.getClientKeyByClientId(String.valueOf(authentication.getPrincipal())));
		
		//String key = new String((byte[]) authentication.getCredentials());
		
		//byte[] clientKey = clientCacheManager.getClientKeyByClientId(clientAuthRequest.clientId());

		if (key != null && key.length() > 0) {
			log.debug("encrypt!");
			
			try {
				return AESHelper.encrypt(input, key, clientCacheManager.getConfiguration().getIv());
			} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
					| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
					| InvalidKeySpecException e) {
				log.error(e.getLocalizedMessage());
				return "";
			}
		} else 
		
		log.error("no valid key found!");
		return "";
	}
}
