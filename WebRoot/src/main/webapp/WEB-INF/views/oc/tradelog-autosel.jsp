<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<div class="col-xs-12">
  <form class="form-horizontal" method="post">
    <div class="form-group">
      <label class="col-sm-3 control-label" for="sel_provider">运营商</label>

      <div class="col-sm-9">
        <select name="sel_provider" id="sel_provider" class="form-control input-sm">
          <option value="CMCC">中国移动</option>
          <option value="TELECOM">中国电信</option>
          <option value="UNICOM">中国联通</option>
        </select>
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-3 control-label">地区</label>

      <div class="col-sm-9">
        <select name="sel_state" id="sel_state" class="form-control input-sm">
          <c:forEach var="state" items="${states}">
            <option value="${state.code}">${state.name}</option>
          </c:forEach>
        </select>
      </div>
    </div>
  </form>
</div>