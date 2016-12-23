<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<div>
  <div class="form-group">
    <label class="col-sm-3 control-label" for="sectionNo">号段</label>

    <div class="col-sm-8">
      <input type="text" id="sectionNo" <c:if test="${not empty location.sectionNo}"> readonly</c:if> name="sectionNo" data-validation="required" value="${location.sectionNo}" class="canreset form-control input-sm">
    </div>
  </div>

  <div class="form-group">
    <label class="col-sm-3 control-label" for="area">地区名</label>

    <div class="col-sm-8">
      <input type="text" id="area" name="area" data-validation="required" value="${location.area}" class="canreset form-control input-sm">
    </div>
  </div>

  <div class="form-group">
    <label class="col-sm-3 control-label" for="area">地区代码</label>

    <div class="col-sm-8">
      <input type="text" id="areaCode" name="areaCode" value="${location.areaCode}" class="canreset form-control input-sm">
    </div>
  </div>

  <div class="form-group">
    <label class="col-sm-3 control-label" for="area">号段类型</label>

    <div class="col-sm-8">
      <input type="text" id="mobileType" name="mobileType" data-validation="required" value="${location.mobileType}" class="canreset form-control input-sm">
    </div>
  </div>

  <div class="form-group">
    <label class="col-sm-3 control-label" for="area">邮编</label>

    <div class="col-sm-8">
      <input type="text" id="postcode" name="postcode" value="${location.postcode}" class="canreset form-control input-sm">
    </div>
  </div>

</div>