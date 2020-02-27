package com.cnksi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@MapperScan(value="com.cnksi.mapper")
@SpringBootApplication
public class SpringBootMybatisApplication {

	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootMybatisApplication.class, args);
		 // 为了使用swagger方便，这里把swagger的访问地址打印出来，启动项目后，在控制台点击此链接，即可自动打开swagger页面
		log.info("swagger url:  http://localhost:8080/swagger-ui.html");
	}

	
	@RequestMapping(value = "/helloWebSocket",method = RequestMethod.GET)
	public String helloWebSocket(){
		return "helloWebSocket";
	}
}
