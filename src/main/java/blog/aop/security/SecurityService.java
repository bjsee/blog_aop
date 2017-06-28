package blog.aop.security;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	public boolean isAuthorized(User user, String string) {
		return false;
	}

	public User getUserFromCurrentSession() {
		return null;
	}

}
