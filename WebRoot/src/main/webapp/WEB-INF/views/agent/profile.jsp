<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/agent/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/agent/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/agent/banner.jsp" %>
    <tags:content_header icon="fa-edit" sysname="${sysname}" title="注册信息"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>注册信息
            <small>若有变更, 请及时提交新的信息</small>
          </h5>
        </div>
        <div class="ibox-content">
          <form id="basic-form" method="post" action="${ctx}/agent/profile" class="form-horizontal">
            <input name="id" class="hidden" value="${user.id}"/>

            <div class="form-group">
              <label class="col-sm-2 control-label" for="username">用户名</label>

              <div class="col-sm-10">
                <input type="text" id="username" name="username" value="${user.username}" readOnly class="form-control">
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label" for="displayName">中文名</label>

              <div class="col-sm-10">
                <input type="text" id="displayName" name="displayName" value="${user.displayName}" class="form-control">
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label" for="company">公司名称</label>

              <div class="col-sm-10">
                <input type="text" id="company" name="company" value="${user.company}" class="form-control">
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label" for="company">联系人</label>

              <div class="col-sm-10">
                <input type="text" id="linkman" name="linkman" value="${user.linkman}" class="form-control">
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label" for="mobile">电话</label>

              <div class="col-sm-10">
                <input type="text" id="mobile" name="mobile" value="${user.mobile}" class="form-control">
              </div>
            </div>
            <div class="form-group">
              <div class="col-sm-4 col-sm-offset-2">
                <button class="btn btn-info pull-left" type="submit">应用</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/agent/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/http-script.jsp" %>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script src="${cdn}/js/plugins/form/jquery.form.js"></script>
<script src="${cdn}/js/plugins/form-validator/jquery.form-validator.min.js"></script>
<script>
  $(function () {
    toastr.options = {
      closeButton: true,
      progressBar: true,
      "positionClass": "toast-top-center",
      timeOut: 4000
    };
    $("#m-profile").addClass("active");
    $('#basic-form, #bill-form').on('submit', function (e) {
      e.preventDefault();
      $(this).ajaxSubmit({
        success: function () {
          toastr.success("代理商信息已经保存", '保存成功');
        },
        error: function () {
          toastr.error("请稍后再试, 始终失败请联系管理员", "保存失败");
        }
      })
    });
  });
</script>
</body>
</html>
