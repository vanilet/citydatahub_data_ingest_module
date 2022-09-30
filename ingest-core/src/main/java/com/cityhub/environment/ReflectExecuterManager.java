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

package com.cityhub.environment;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.flume.channel.ChannelProcessor;
import org.json.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReflectExecuterManager {
  public static ReflectExecuter getInstance(String invokeClass, ChannelProcessor channelProcessor ,JSONObject ConfItem, JSONObject templateItem ) {
    ReflectExecuter reflectExecuter = null;

    try {
      Class<?> clz = Class.forName(invokeClass);
      reflectExecuter  = (ReflectExecuter)clz.newInstance();
      reflectExecuter.init(channelProcessor, ConfItem, templateItem);
    } catch (Exception e) {
      log.error("Exception : "+ExceptionUtils.getStackTrace(e));
    }

    return reflectExecuter;
  }

} // end of class
