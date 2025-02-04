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
package com.cityhub.daemon.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 공통 상수 관리 클래스
 */
public class Consts {
  public static final List<String> CONTEXT = new ArrayList<>(Arrays.asList("http://uri.etsi.org/ngsi-ld/core-context.jsonld", "http://datahub.kr/cor19.jsonld"));

  public static final String CONTENT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss,SSS";

  public static final String CONTENT_DATE_TIMEZONE = "Asia/Seoul";

  public static final String BASE_PACKAGE = "com.cityhub.daemon";

}
