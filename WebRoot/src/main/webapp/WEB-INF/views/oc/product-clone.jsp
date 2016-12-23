<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<from class="form-horizontal">
  <div class="box-body">
    <div class="form-group">
      <label class="col-sm-4 control-label">全网产品组</label>

      <div class="col-sm-8">
        <select name="provider" id="provider" class="form-control input-sm">
          <option value="CMCC">中国移动</option>
          <option value="TELECOM">中国电信</option>
          <option value="UNICOM">中国联通</option>
        </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-4 control-label">克隆到</label>

      <div class="col-sm-8">
        <select name="state" id="state" class="form-control input-sm">
          <option value="">-请选择-</option>
          <c:forEach var="state" items="${states}">
            <option value="${state.code}">${state.name}</option>
          </c:forEach>
        </select>
      </div>
    </div>
  </div>
</from>
