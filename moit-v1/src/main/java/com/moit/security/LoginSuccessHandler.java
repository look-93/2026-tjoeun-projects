package com.moit.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LoginSuccessHandler implements AuthenticationSuccessHandler{   

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, 
										 HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
	 
		List<String>  roles = new ArrayList<>(); 
		authentication.getAuthorities().forEach(auth->{ roles.add(auth.getAuthority()); });
		
		String loginType = request.getParameter("loginType");
		
		if (roles.contains("ROLE_ADMIN")) {

			response.sendRedirect( request.getContextPath() + "/admin/admininfo");
        }

		else if (roles.contains("ROLE_MEMBER")) {

			response.sendRedirect( request.getContextPath() + "/");
        }else {
        	response.sendRedirect( request.getContextPath() + "/");
        }
		
		/*
		if ("ADMIN".equals(loginType)) {

	        if (!roles.contains("ROLE_ADMIN")) {

	            request.getSession().invalidate();

	            response.sendRedirect( request.getContextPath() + "/users/login?error=admin");

	            return;
	        }

	        response.sendRedirect( request.getContextPath() + "/admin/admininfo");

	        return;
	    }
		
		if("USER".equals(loginType)){

		    if(roles.contains("ROLE_ADMIN")){

		        request.getSession().invalidate();

		        response.sendRedirect( request.getContextPath()+"/users/login?error=user" );

		        return;
		    }

		}	response.sendRedirect( request.getContextPath() + "/main");
		*/
	
		
	}

}












