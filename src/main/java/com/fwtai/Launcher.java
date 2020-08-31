package com.fwtai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)//排除,移开
@EnableAsync
public class Launcher extends SpringBootServletInitializer{

	public static void main(final String[] args){
		SpringApplication.run(Launcher.class,args);
        System.out.println("--应用启动成功--");
	}

    /**打包war时能正常运行*/
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Launcher.class);
    }
}