<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/agent/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/agent/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/agent/banner.jsp" %>
    <tags:content_header icon="fa-cubes" sysname="${sysname}" title="应用接入"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>API接入
            <small>接入信息</small>
          </h5>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-sm-6 b-r">
              <h3 class="m-t-none m-b">接入参数</h3>

              <form role="form">
                <div class="form-group">
                  <label>账号</label>

                  <div class="input-group">
                    <span class="input-group-addon"><i class="fa fa-user text-aqua"></i></span>
                    <input id="username" type="text" value="${username}" readonly class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label>授权码</label>

                  <div class="input-group">
                    <span class="input-group-addon"><i class="fa fa-unlock-alt text-aqua"></i></span>
                    <input type="text" id="authcode" value="${authcode}" readonly class="form-control">
                  </div>
                </div>
                <button id="reauth" type="button" class="btn btn-sm btn-primary pull-right m-t-n-xs">更新授权码</button>
              </form>
            </div>
            <div class="col-sm-6"><h4>需要API手册?</h4>

              <p>请在这里下载</p>

              <p class="text-center">
                <a href="${cdn}/doc/商户充值协议.pdf"><i class="fa fa-cloud-download big-icon"></i></a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/agent/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/http-script.jsp" %>
<script type="text/javascript" src="${cdn}/js/plugins/bootbox/bootbox.min.js"></script>
<script type="text/javascript">
  $(function () {
    $("#m-apis").addClass("active");
    var doClear = function () {
      $.ajax({
        type: "post",
        url: $ctx + "/agent/apis/clear",
        success: function () {
          $('#authcode').val("");
        }
      });
    };

    var reauth = function () {
      $.ajax({
        type: "post",
        url: $ctx + "/agent/apis/reauth",
        success: function (data) {
          $('#authcode').val(data);
        }
      });
    };

    $("#clear-authcode").on("click", function () {
      var question = "授权码删除后, 您将不能再使用API接入. 但您可以在需要的时候, 再次生成新的授权码.";
      bootbox.confirm(question, function (result) {
        if (result) doClear();
      });
    });
    $("#reauth").on("click", function () {
      if ($("#authcode").val() == "") {
        reauth();
      } else {
        var question = "您确定要生成新的授权码吗? 旧的授权码将立即失效.";
        bootbox.confirm(question, function (result) {
          if (result) reauth();
        });
      }
    });
  });
</script>
</body>
</html>
