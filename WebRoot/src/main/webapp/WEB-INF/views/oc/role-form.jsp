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
    <tags:content_header icon="fa-cubes" sysname="${sysname}" title="角色管理"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>角色管理</h5>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-xs-12">
              <c:if test="${not empty message}">
                <div id="message" class="alert alert-success">
                  <button data-dismiss="alert" class="close">×</button>
                    ${message}</div>
              </c:if>
              <form method="post" action="${ctx}/oc/roles/${action}">
                <input type="hidden" name="id" value="${role.id}">

                <div class="form-group">
                  <label>角色名称</label>
                  <input data-validation="required" name="name" placeholder="请输入角色名..." class="form-control" value="${role.name}"/>
                </div>
                <div class="form-group">
                  <label>角色编码</label>
                  <input data-validation="required" name="code" placeholder="请输入角色编码..." class="form-control" value="${role.code}"/>
                </div>

                <div class="form-group">
                  <label>说明</label>
                  <input name="description" placeholder="请输入说明..." class="form-control" value="${role.description}"/>
                </div>
                <div class="box-footer">
                  <a class="btn btn-info" href="${ctx}/oc/roles">返回</a>
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
    activeMenu("mc-role");
    $("#message").delay(2000).hide(300);

    $.validate({
      lang: "zh"
    });
  });
</script>
</body>
</html>
