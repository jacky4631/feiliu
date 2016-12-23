<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<div class="col-xs-12">
  <p class="bg-info" style="padding:10px 8px;">系统仅收取今日之前的未收费提醒短信费用</p>

  <form id="fee" method="post" class="form-horizontal">
    <div class="form-group">
      <label class="col-sm-3 control-label" for="username">用户名</label>

      <div class="col-sm-9">
        <input type="text" id="username" name="username" class="form-control">
      </div>
    </div>
  </form>
</div>
