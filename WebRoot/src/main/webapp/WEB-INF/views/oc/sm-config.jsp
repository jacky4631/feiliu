<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-commenting-o" sysname="${sysname}" title="短信全局设置"/>

    <div class="wrapper wrapper-content animated">
      <c:if test="${not empty message}">
        <div id="message" class="alert alert-success">
          <button data-dismiss="alert" class="close">×</button>
            ${message}</div>
      </c:if>
      <div class="ibox-title">
        <h5>短信配置
          <small>全局配置, 比代理端配置有更高优先级</small>
        </h5>
      </div>
      <div class="ibox-content">
        <form role="form" method="post" action="${ctx}/oc/sm-config">
          <input type="hidden" name="id" value="${smConfig.id}">

          <div class="form-group">
            <div class="checkbox">
              <label>
                <form:checkbox path="smConfig.sendSm" value="true"/>
                成功后需要短信提醒
              </label>
              <em>（全局设置, 如果此关闭短信提醒, 则所有用户设置的短信提醒将无效）</em>
            </div>
          </div>
          <div class="form-group">
            <label>关闭提醒的运营商(7 电信 8 移动 9 联通)</label>
            <input name="disabledForTelco" placeholder="逗号分隔多个值..." class="form-control" value="${smConfig.disabledForTelco}"/>
          </div>
          <div class="form-group">
            <label>默认短信模板</label>
            <textarea name="template" placeholder="请输入短信内容 ..." rows="3" class="form-control">${smConfig.template}</textarea>
          </div>
          <div class="form-group">
            <label>短信接口地址</label>
            <input name="smsApiUrl" placeholder="请输入短信API地址 ..." class="form-control" value="${smConfig.smsApiUrl}"/>
          </div>
          <div class="form-group">
            <label>短信账号</label>
            <input name="smsAccount" placeholder="请输入短信账号 ..." class="form-control" value="${smConfig.smsAccount}"/>
          </div>
          <div class="form-group">
            <label>短信账号密码</label>
            <input name="smsPass" type="password" placeholder="请输入短信账号密码 ..." class="form-control" value="${smConfig.smsPass}"/>
          </div>
          <div class="form-group clearfix">
            <button class="btn btn-primary pull-left" type="submit">保存设置</button>
          </div>
        </form>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/oc/footer.jsp" %>
  </div>
</div>
</body>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script type="text/javascript">
  $(function () {
    activeMenu("ma-config-sms");
    $("#message").delay(2000).hide(300);
  });
</script>
</body>
</html>
