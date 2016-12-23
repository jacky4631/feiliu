<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/agent/header.jsp" %>
</head>
<body class="gray-bg">
<div class="middle-box text-center animated fadeInDown">
  <h1>404</h1>
  <h3 class="font-bold">页面找不到</h3>
  <div class="error-desc">
    对不起, 您访问的页面我们经仔细鉴定后, 发现确实脱离了服务区, 呼叫没有应答, 您可以按照我们的建议, 试试其他姿势再来一遍? ~…~
    <form class="form-inline m-t" role="form">
      <a type="button" class="btn btn-primary" href="${ctx}/">主界面</a>
      <a type="button" class="btn btn-primary" href="${ctx}/agent">代理端</a>
    </form>
  </div>
</div>
</body>
</html>

