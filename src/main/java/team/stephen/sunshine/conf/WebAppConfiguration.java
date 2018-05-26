package team.stephen.sunshine.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import team.stephen.sunshine.web.interceptor.AccessLogInterceptor;
import team.stephen.sunshine.web.interceptor.CrossDomainterceptor;

/**
 * Created by stephen on 2017/7/15.
 */
@Configuration
public class WebAppConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor())
//                .excludePathPatterns("/index", "/toLogin", "/login", "/user/sign-up", "/error");
        registry.addInterceptor(new AccessLogInterceptor());
        registry.addInterceptor(new CrossDomainterceptor());
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**");
        super.addResourceHandlers(registry);
    }
}
