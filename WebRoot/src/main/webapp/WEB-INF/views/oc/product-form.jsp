<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<input id="flag" type="hidden"/>
<div>
  <div class="form-group">
    <label class="col-sm-3 control-label" for="id">产品ID</label>

    <div class="col-sm-8">
      <input type="text" id="id" name="id" readonly value="${product.id}" class="canreset form-control input-sm">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-3 control-label" for="name">产品名称</label>

    <div class="col-sm-8">
      <input type="text" id="name" name="name" data-validation="required" value="${product.name}" class="canreset form-control input-sm">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-3 control-label" for="shortName">产品简称</label>

    <div class="col-sm-8">
      <input type="text" id="shortName" name="shortName" data-validation="required" value="${product.shortName}" class="canreset form-control input-sm">
    </div>
  </div>
  <div class="row">
    <div class="col-sm-6">
      <div class="form-group">
        <label class="col-sm-6 control-label">运营商</label>

        <div class="col-sm-6">
          <form:select path="product.provider" cssClass="form-control input-sm">
            <form:option value="CMCC">中国移动</form:option>
            <form:option value="TELECOM">中国电信</form:option>
            <form:option value="UNICOM">中国联通</form:option>
          </form:select>
        </div>
      </div>
    </div>
    <div class="col-sm-6">
      <div class="form-group">
        <label class="col-sm-4 control-label">范围</label>

        <div class="col-sm-6">
          <form:select path="product.scope" cssClass="form-control input-sm">
            <c:forEach var="state" items="${states}">
              <form:option value="${state.code}">${state.name}</form:option>
            </c:forEach>
          </form:select>
        </div>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-sm-6">
      <div class="form-group">
        <label class="col-sm-6 control-label" for="size">包大小</label>

        <div class="col-sm-6">
          <input type="text" id="size" name="size" data-validation="number" data-validation-allowing="float" value="${product.size}" class="form-control input-sm">
        </div>
      </div>
    </div>
    <div class="col-sm-6">
      <div class="form-group">
        <label class="col-sm-4 control-label" for="price">标价</label>

        <div class="col-sm-6">
          <input type="text" id="price" name="price" data-validation="number" data-validation-allowing="float" value="${product.price}" class="form-control input-sm">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-6">
        <div class="form-group">
          <label class="col-sm-6 control-label">状态</label>

          <div class="checkbox col-sm-6">
            <label>
              <form:checkbox path="product.enabled" value="true"/>启用
            </label>
          </div>

        </div>
      </div>
    </div>
  </div>
</div>

