<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<div class="col-xs-12">
  <form id="balanceForm" class="form-horizontal">
    <input name="uid" id="uid" type="hidden" value="${user.id}"/>
    <input name="username" id="username" type="hidden" value="${user.username}"/>

    <div class="form-group">
      <label class="col-sm-3 control-label" for="channelName">充入通道</label>

      <div class="col-sm-9">
        <input type="text" name="channelName" id="channelName" readonly value="${channel.name}" class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label" for="amount">入账金额</label>

      <div class="col-sm-9">
        <input type="text" id="amount" name="amount" value="" oninput="showC();" class="form-control">
        <span id="showC"></span>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label" for="remark">摘要</label>

      <div class="col-sm-9">
        <input type="text" id="remark" name="remark" class="form-control">
        <small>请输入扼要的说明信息, 以备对账查阅</small>
      </div>
    </div>
  </form>
</div>
<script>
  function showC() {
    $("#showC").html(number2C($("#amount").val()));
  }
</script>
