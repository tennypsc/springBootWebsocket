package com.cnksi.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
 
/**
 * Swagger 测试控制类
 **/
@Slf4j
@RestController
@Api(description = "Swagger测试控制接口")
@RequestMapping("/swaggerTest")
public class SwaggerTestController {
 
	@RequestMapping(value = "/hello/{param}", method = RequestMethod.POST)
	@ApiOperation(value = "测试hello")
	public Integer helloSwagger(@PathVariable("param") Integer param) {
		return param;
	}
 
}