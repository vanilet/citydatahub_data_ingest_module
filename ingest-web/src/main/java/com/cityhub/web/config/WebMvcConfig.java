/**
 *
 * Copyright 2021 PINE C&I CO., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cityhub.web.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


  @Autowired
  private WebInterceptor webInterceptor;

  private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/static/"};
  private static final List<String> excludePattern = new ArrayList<>();


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    /*
    excludePattern.add("/login");
    excludePattern.add("/logout");
    excludePattern.add("/restApi/**");
    excludePattern.add("/agents/**");
    excludePattern.add("/adaptorType/**");
    excludePattern.add("/sourceModel/**");
    excludePattern.add("/sourceModels/**");
    excludePattern.add("/sourceModelsTest");
     */
    registry.addInterceptor(webInterceptor).addPathPatterns("/**");
  }


  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
    .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS)
    .setCachePeriod(3600)
    .resourceChain(true)
    .addResolver(new PathResourceResolver());
  }

}
