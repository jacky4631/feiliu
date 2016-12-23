<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/chosen/chosen.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/artDialog/ui-dialog.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
  <link href="${ctx}/static/css/channel-product.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
  <style>
    .chosen-container {
      width: 100% !important;
    }
  </style>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-sliders" sysname="${sysname}" title="预设通道组"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>通道组
            <small>将通道组合在一起形成一个通道组, 方便对用户进行通道限定</small>
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
              <form role="form" method="post" action="${ctx}/oc/channelgroup/${action}">
                <input type="hidden" name="id" value="${group.id}">

                <div class="form-group">
                  <label>通道组名称</label>

                  <input data-validation="required" name="title" placeholder="请输入名称..." class="form-control" value="${group.title}"/>
                </div>

                <div class="form-group">
                  <label class="control-label">用户产品组&nbsp;&nbsp;</label>

                  <a id="btn-modi-channels" href="#">修改...</a>

                  <div>
                    <div class="channel" id="disChannel">
                    </div>
                    <input type="hidden" id="channelprods" name="allowChannelProducts" value="${fn:join(group.allowChannelProducts, ",")}"/>
                  </div>
                </div>

                <div class="form-group">
                  <label>创建人</label>
                  <input name="creator" readonly class="form-control" value="${group.creator}"/>
                </div>
                <div class="form-group">
                  <label>创建时间</label>
                  <input name="created" readonly class="form-control" value="<fmt:formatDate value="${group.created}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                </div>
                <div class="form-group">
                  <label>说明</label>
                  <input name="desc" class="form-control" value="${group.desc}"/>
                </div>
                <div class="box-footer">
                  <a class="btn btn-info" href="${ctx}/oc/channelgroup">返回</a>
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
<script src="${cdn}/js/plugins/chosen/chosen.jquery.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-min.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-plus-min.js"></script>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script src="${ctx}/static/js/oc/channel-product.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("ma-tools-cgroup");
    toastr.options = {
      closeButton: true,
      progressBar: false,
      positionClass: "toast-top-center",
      timeOut: 550
    };
    $(".chosen").chosen({});
    $("#message").delay(2000).hide(300);

    $.validate({
      lang: "zh"
    });
  });
</script>
</body>
</html>
