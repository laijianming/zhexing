package com.zhexing.sso.springmvcConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.zhexing.sso.Filter.AutoLoginFilter;
import com.zhexing.sso.Filter.MyCharacterEncodingIntercept;
import com.zhexing.sso.Filter.returnIntercept;
@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	/**
	 * 配置跨域拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(new returnIntercept()).addPathPatterns("/**");
		registry.addInterceptor(new MyCharacterEncodingIntercept()).addPathPatterns("/**");
		registry.addInterceptor(new AutoLoginFilter()).addPathPatterns("/autoLogin");
	}
//	@Override
//	protected void addInterceptors(InterceptorRegistry registry) {
//		super.addInterceptors(registry);
//		registry.addInterceptor(new returnIntercept()).addPathPatterns("/**");
//	}
/*解决跨域问题*/

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver resolver=new InternalResourceViewResolver();
		resolver.setSuffix(".html");
		resolver.setPrefix("/static");
		registry.viewResolver(resolver);
	}
	
	
}
