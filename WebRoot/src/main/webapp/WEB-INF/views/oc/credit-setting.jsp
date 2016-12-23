<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<div class="col-xs-12">
  <p class="bg-info" style="padding:10px 8px;">设置授信后, 用户的余额允许为负数</p>
  <form id="credForm" method="post">
    <input name="uid" id="uid" type="hidden" value="${uid}"/>

    <div class="form-group">
      <label class="col-sm-3 control-label" for="credit">授信额度</label>

      <div class="col-sm-9">
        <input type="text" id="credit" name="credit" value="${credit}" class="form-control">
        <small>输入新的授信额度, 该额度将替换旧的授信额度, 但不影响余额</small>
      </div>
    </div>
  </form>
</div>