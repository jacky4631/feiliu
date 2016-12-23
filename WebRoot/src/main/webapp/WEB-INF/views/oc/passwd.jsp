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
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-user" sysname="${sysname}" title="修改密码"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>密码修改
            <small>为保障您的权益, 切勿使用简单密码</small>
          </h5>
        </div>
        <div class="ibox-content">
          <form role="form" id="passwd-form" method="post" class="form-horizontal" action="${ctx}/oc/passwd">
            <div class="form-group">
              <label class="col-sm-2 control-label" for="oldPassword">原密码</label>

              <div class="col-sm-10">
                <input type="password" id="oldPassword" data-validation="required" name="oldPassword" class="form-control">
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label" for="password">新密码</label>

              <div class="col-sm-10">
                <input type="password" id="password" data-validation="required" name="password" class="form-control">
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label" for="password1">再输一次</label>

              <div class="col-sm-10">
                <input type="password" id="password1" data-validation="required" name="password1" class="form-control">
              </div>
            </div>
            <div class="form-group">
              <div class="col-sm-4 col-sm-offset-2">
                <button class="btn btn-info" type="submit">确认修改</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/agent/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/form/jquery.form.js"></script>
<script src="${cdn}/js/plugins/form-validator/jquery.form-validator.min.js"></script>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script>
  $(function () {
    toastr.options = {
      closeButton: true,
      progressBar: true,
      "positionClass": "toast-top-center",
      timeOut: 4000
    };
    $.validate({
      lang: "zh",
      onSuccess: function (form) {
        $(form).ajaxSubmit({
          dataType: "json",
          success: function (data) {
            if (data.status == "success") {
              toastr.success("新密码已经生效", '修改成功');
            } else {
              toastr.error(data.message, '修改失败');
            }
          },
          error: function () {
            toastr.error("请稍后再试, 始终失败请联系管理员", '修改执行失败');
          }
        });
        return false;
      }
    });
  });
</script>
</body>
</html>
