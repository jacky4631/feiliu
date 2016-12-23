<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<%
  try {
    Throwable ex = null;
    if (request.getAttribute("javax.servlet.error.exception") != null) {
      ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
    }
    Logger logger = LoggerFactory.getLogger("500");
    if (ex != null) {
      logger.error(ex.getMessage(), ex);
    }
  } catch (Exception ignore) {
  }
%>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/agent/header.jsp" %>
</head>
<body class="gray-bg">
<div class="middle-box text-center animated fadeInDown">
  <h1>500</h1>

  <h3 class="font-bold">执行失败</h3>

  <div class="error-desc">
    对不起, 服务执行失败,请稍后再试
    <form class="form-inline m-t" role="form">
      <a type="button" class="btn btn-primary" href="${ctx}/">主界面</a>
      <a type="button" class="btn btn-primary" href="${ctx}/agent">代理端</a>
    </form>
  </div>
</div>
</body>
</html>

