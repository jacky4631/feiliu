<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<form class="form-horizontal">
  <div class="box-body">
    <div class="form-group">
      <label class="col-sm-3 control-label">运营商</label>

      <div class="col-sm-9">
        <select name="n_telco" id="n_telco" class="form-control input-sm">
          <option value="CMCC">中国移动</option>
          <option value="TELECOM">中国电信</option>
          <option value="UNICOM">中国联通</option>
        </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label">地区</label>

      <div class="col-sm-9">
        <select name="n_state" id="n_state" class="form-control input-sm">
          <c:forEach var="state" items="${states}">
            <option value="${state.code}">${state.name}</option>
          </c:forEach>
        </select>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label">全国漫游</label>

      <div class="checkbox col-sm-9">
        <label>
          <input type="checkbox" id="roamable" value="true" checked>可漫游
        </label>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label" for="n_discount">折扣</label>

      <div class="col-sm-9">
        <input type="text" id="n_discount" value="0.7" class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label" for="n_prior">优先级</label>

      <div class="col-sm-9">
        <input type="text" id="n_prior" value="5" class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label">供方ID生成</label>

      <div class="col-sm-9">
        <div>
          <label><input type="radio" name="n_id_method" value="0" checked> 按规格(1G=1024) </label>
        </div>
        <div>
          <label><input type="radio" name="n_id_method" value="1"> 按规格(1G=1000) </label>
        </div>
        <div>
          <label><input type="radio" name="n_id_method" value="3"> 飞流标准</label>
        </div>
        <div>
          <label><input type="radio" name="n_id_method" value="2"> 先设为1, 稍后手工修改</label>
        </div>
      </div>
    </div>
  </div>
</form>
