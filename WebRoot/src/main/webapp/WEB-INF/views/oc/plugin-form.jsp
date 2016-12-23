<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav-admin.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-cubes" sysname="${sysname}" title="插件管理"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>插件管理
            <small>官方插件已内置定义, 请勿重复定义, 自主开发的插件需要注册方可使用</small>
          </h5>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-xs-12">
              <c:if test="${not empty message}">
                <div id="message" class="alert alert-success">
                  <button data-dismiss="alert" class="close">×</button>
                    ${message}</div>
              </c:if>
              <form role="form" method="post" action="${ctx}/oc/plugins/${action}">
                <input type="hidden" name="id" value="${plugin.id}">

                <div class="form-group">
                  <label>插件名称</label>
                  <input data-validation="required" name="title" placeholder="请输入标题..." class="form-control" value="${plugin.title}"/>
                </div>
                <div class="form-group">
                  <label>插件处理类(Java Class)</label>
                  <input data-validation="required" name="handlerClass" placeholder="请输入处理类名..." class="form-control" value="${plugin.handlerClass}"/>
                </div>

                <div class="form-group">
                  <label>回调监听路径(插件以回调方式获取时填写, 建议使用Spring MVC实现, 以便自动发现)</label>
                  <input name="callbackUrl" class="form-control" value="${plugin.callbackUrl}"/>
                </div>
                <div class="form-group">
                  <label>版本</label>
                  <input data-validation="required" name="version" class="form-control" value="${plugin.version}"/>
                </div>
                <div class="form-group">
                  <label>作者</label>
                  <input name="author" class="form-control" value="${plugin.author}"/>
                </div>
                <div class="form-group">
                  <label>注册时间</label>
                  <input name="created" readonly class="form-control" value="<fmt:formatDate value="${plugin.created}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </div>
                <div class="form-group">
                  <label>说明</label>
                  <input name="description" class="form-control" value="${plugin.description}"/>
                </div>
                <div class="box-footer">
                  <a class="btn btn-info" href="${ctx}/oc/plugins">返回</a>
                  <button class="btn btn-info" type="submit">保存</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/oc/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/form-validator/jquery.form-validator.min.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("mc-plugin");
    $("#message").delay(2000).hide(300);

    $.validate({
      lang: "zh"
    });
  });
</script>
</body>
</html>
