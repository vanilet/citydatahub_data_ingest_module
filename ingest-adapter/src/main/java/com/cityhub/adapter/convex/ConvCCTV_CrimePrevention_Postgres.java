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
package com.cityhub.adapter.convex;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;

import com.cityhub.exception.CoreException;
import com.cityhub.core.AbstractNormalSource;
import com.cityhub.utils.DataCoreCode.ErrorCode;
import com.cityhub.utils.DataCoreCode.SocketCode;
import com.cityhub.utils.DateUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConvCCTV_CrimePrevention_Postgres extends AbstractNormalSource {


  @Override
  public String doit(BasicDataSource datasource)  {
    String rtnStr = "";
    List<Map<String, Object>> rtnList = new LinkedList<>();
    String id = "";
    String cctvType = "Crime Prevention";
    JSONObject cctvInfo = ConfItem.getJSONObject(cctvType);
    String sql = cctvInfo.getString("query");
    try (PreparedStatement pstmt = datasource.getConnection().prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        ){

      while (rs.next()) {
        // 변수 선언
        String name = "";
        String streetAddress = "";
        int numberOfCCTV = 0;
        Float pixel = 0.0f;
        String isRotatable = "";
        String installedAt = "";
        String addressCountry = cctvInfo.get("addressCountry").toString();
        String addressRegion = "";
        String addressLocality = "";
        String addressTown = "";
        ArrayList<Double> location = new ArrayList<>();
        String status = "normal";
        String hasEmergencyBell = "FALSE";
        Float direction = 0.0f;
        Float distance = 30.0f;
        Float fieldOfView = 0.0f;
        Float height = 4.0f;

        id = rs.getString("id");
        toLogger(SocketCode.DATA_RECEIVE, id);
        id = cctvInfo.getString("idPrefix") + id.replace("-", "_") + cctvInfo.getString("idSurffix");
        try {
          name = rs.getString("name");
        } catch (Exception e) {
          name = null;
        }
        try {
          isRotatable = rs.getString("isRotatable");
        } catch (Exception e) {
        }

        if (isRotatable.trim().length() >= 1 && Integer.parseInt(isRotatable) >= 1) {
          isRotatable = "TRUE";
        } else {
          isRotatable = "FALSE";
        }
        hasEmergencyBell = rs.getString("hasEmergencyBell");
        if ("없음".equals(hasEmergencyBell) || hasEmergencyBell.trim().length() == 0) {
          hasEmergencyBell = "FALSE";
        } else {
          hasEmergencyBell = "TRUE";
        }
        String _numberOfCCTV = rs.getString("numberOfCCTV");
        numberOfCCTV = Integer.parseInt(_numberOfCCTV);
        String _pixel = rs.getString("pixel");
        if (_pixel != null && !"".equals(_pixel) && _pixel.contains("만")) {
          _pixel = _pixel.replace("만", "0000");
        }
        pixel = Float.parseFloat(_pixel);
        installedAt = rs.getString("installedAt");
        installedAt = getTimeInfo(LocalDate.of(Integer.parseInt(installedAt), 1, 1), LocalTime.of(12, 0, 0));
        addressRegion = cctvInfo.get("addressRegion").toString();
        addressLocality = rs.getString("addressLocality");
        addressTown = rs.getString("addressTown");
        streetAddress = rs.getString("streetAddress");
        location.add(0.0d);
        location.add(0.0d);

        Map<String, Object> tMap = objectMapper.readValue(templateItem.getJSONObject(ConfItem.getString("modelId")).toString(), new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> wMap = new LinkedHashMap<>();

        // 데이터 삽입
        Find_wMap(tMap, "typeOfCCTV").put("value", cctvType);
        Find_wMap(tMap, "numberOfCCTV").put("value", numberOfCCTV);
        Find_wMap(tMap, "name").put("value", name);
        Find_wMap(tMap, "isRotatable").put("value", isRotatable);
        Find_wMap(tMap, "status").put("value", status);
        Find_wMap(tMap, "installedAt").put("value", installedAt);
        Find_wMap(tMap, "distance").put("value", distance);
        Find_wMap(tMap, "height").put("value", height);
        Find_wMap(tMap, "direction").put("value", direction);
        Find_wMap(tMap, "fieldOfView").put("value", fieldOfView);
        Find_wMap(tMap, "hasEmergencyBell").put("value", hasEmergencyBell);
        Find_wMap(tMap, "pixel").put("value", pixel);
        Find_wMap(tMap, "typeOfCCTV").put("value", cctvType);
        Find_wMap(tMap, "typeOfCCTV").put("value", cctvType);

        wMap = (Map) tMap.get("dataProvider");

        wMap.put("value", cctvInfo.optString("dataProvider", "https://www.siheung.go.kr"));

        wMap = (Map) tMap.get("globalLocationNumber");
        wMap.put("value", id);

        Map<String, Object> addrValue = (Map) ((Map) tMap.get("address")).get("value");
        addrValue.put("addressCountry", addressCountry);
        addrValue.put("addressRegion", addressRegion);
        addrValue.put("addressLocality", addressLocality);
        addrValue.put("addressTown", addressTown);
        addrValue.put("streetAddress", streetAddress);

        Map<String, Object> locMap = (Map) tMap.get("location");
        locMap.put("observedAt", DateUtil.getTime());
        Map<String, Object> locValueMap = (Map) locMap.get("value");
        locValueMap.put("coordinates", location);

        tMap.put("id", id);

        rtnList.add(tMap);
        String str = objectMapper.writeValueAsString(tMap);
        toLogger(SocketCode.DATA_CONVERT_SUCCESS, id, str.getBytes());
      }
      rtnStr = objectMapper.writeValueAsString(rtnList);
      if (rtnStr.length() < 10) {
        throw new CoreException(ErrorCode.NORMAL_ERROR);
      }
    } catch (SQLException e) {
      toLogger(SocketCode.DATA_CONVERT_FAIL, id, e.getMessage());
    } catch (CoreException e) {
      if ("!C0099".equals(e.getErrorCode())) {
        toLogger(SocketCode.DATA_CONVERT_FAIL, id, e.getMessage());
      }
    } catch (Exception e) {
      toLogger(SocketCode.DATA_CONVERT_FAIL, id, e.getMessage());
      log.error("Exception : " + ExceptionUtils.getStackTrace(e));
    }

    return rtnStr;
  } // end of doit

  Map<String, Object> Find_wMap(Map<String, Object> tMap, String Name) {
    Map<String, Object> ValueMap = (Map) tMap.get(Name);
    ValueMap.put("observedAt", DateUtil.getTime());
    return ValueMap;
  }

  protected String getTimeInfo(LocalDate date, LocalTime time) {

    LocalDateTime dt = LocalDateTime.of(date, time);
    ZoneOffset zo = ZonedDateTime.now().getOffset();
    String returnValue = OffsetDateTime.of(dt, zo).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss,SSSXXX")).toString();

    return returnValue;
  }

}
// end of class