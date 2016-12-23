<%@ page import="com.jiam365.flow.base.utils.ShiroUtils, org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<%
  if (ShiroUtils.currentUser() != null) {
    response.sendRedirect(request.getContextPath());
  }
%>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="${cdn}/css/plugins/iCheck/custom.css">
  <%@ include file="/WEB-INF/include/agent/header.jsp" %>
  <title>${sysname} | 登录</title>
  <style>
    .middle-box h1 {
      font-size: 100px;
    }

    .logo-name {
      color: #e6e6e6;
      font-size: 180px;
      font-weight: 800;
      letter-spacing: -10px;
      margin-bottom: 0;
    }

    .loginscreen.middle-box {
      width: 400px;
    }

    .middle-box {
      margin: 0 auto;
      max-width: 400px;
      padding-top: 40px;
      z-index: 100;
    }

    .checkbox label, .radio label {
      padding-left: 0;
    }
  </style>
</head>
<body class="gray-bg">
<div class="middle-box text-center loginscreen">
  <div>
    <div>
      <h1 class="logo-name">${shortname}</h1>
    </div>
    <h3>Welcome to ${sysname}</h3>
    <%
      String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
      if (error != null) {
    %>
    <div id="message" class="alert alert-info controls input-large">
      <button type="button" class="close" data-dismiss="alert">×</button>
      <%
        if (error.contains("DisabledAccountException")) {
          out.print("用户已被屏蔽,请登录其他用户.");
        } else {
          out.print("登录失败，请重试.");
        }
      %>
    </div>
    <%
      }
    %>
    <form class="m-t" action="${ctx}/login" method="post">
      <div class="form-group">
        <input type="text" id="username" name="username" value="${username}" class="form-control" placeholder="用户名">
      </div>
      <div class="form-group">
        <input type="password" name="password" class="form-control" placeholder="密码">
      </div>
      <div class="form-group">
        <div class="checkbox icheck" style="text-align: left;margin-left: 2px;">
          <label>
            <input name="rememberMe" type="checkbox"> 保持登录状态 (请勿在公用计算机上勾选此项)
          </label>
        </div>
      </div>
      <button type="submit" class="btn btn-primary block full-width m-b">登录</button>
    </form>
    <p class="m-t">
      <small>${sysname} 版权所有 & Copyright reserved. 2016</small>
    </p>
  </div>
</div>
<%@include file="/WEB-INF/include/http-script.jsp" %>
<script src="${cdn}/js/plugins/iCheck/icheck.min.js"></script>
<script>
  $(function () {
    $('input').iCheck({
      checkboxClass: 'icheckbox_square-green',
      radioClass: 'iradio_square-green',
      increaseArea: '0%'
    });
    $('#username').focus();
    $("#message").delay(1500).hide(300);
  });
</script>
</body>
</html>
