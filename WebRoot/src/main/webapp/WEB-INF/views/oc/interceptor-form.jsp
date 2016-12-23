<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/chosen/chosen.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
  <style>
    .chosen-container, .input-cus {
      width: 430px !important;
    }
  </style>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-user-secret" sysname="${sysname}" title="充值拦截器"/>
    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-title">
        <h5>流量充值拦截器定义
          <small>被拦截的订单已经扣费, 需要手工进行处理(如果设置了超时自动处理,也可等待超时)</small>
        </h5>
      </div>
      <div class="ibox-content">
        <c:if test="${not empty message}">
          <div id="message" class="alert alert-success">
            <button data-dismiss="alert" class="close">×</button>
              ${message}</div>
        </c:if>
        <form class="form-horizontal" method="post" action="${ctx}/oc/interceptors/save">
          <input type="hidden" data-validation="required" name="id" value="${interceptor.id}">

          <div class="form-group">
            <label class="col-sm-2 control-label" for="name">拦截器名称</label>

            <div class="col-sm-10">
              <input type="text" id="name" data-validation="required" name="name" value="${interceptor.name}" class="form-control input-cus">
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-2 control-label" for="idx">生效次序</label>

            <div class="col-sm-10">
              <input type="text" id="idx" data-validation="required" name="idx" value="${interceptor.idx}" class="form-control input-cus">
              <small>输入整数, 数字越小越先执行。只开启一个产品拦截器的版本可忽略此参数</small>
            </div>
          </div>
          <div class="hr-line-dashed"></div>
          <div class="form-group">
            <label class="col-sm-2 control-label">移动进黑洞地区</label>

            <div class="col-sm-6">
              <form:select data-placeholder="选择需要拦截的地区..." path="interceptor.cmcc" multiple="true" cssClass="form-control chosen">
                <form:options items="${states}" itemLabel="name" itemValue="code"/>
              </form:select>
              <br>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">电信进黑洞地区</label>

            <div class="col-sm-6">
              <form:select data-placeholder="选择需要拦截的地区..." path="interceptor.telecom" multiple="true" cssClass="form-control chosen">
                <form:options items="${states}" itemLabel="name" itemValue="code"/>
              </form:select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">联通进黑洞地区</label>

            <div class="col-sm-6">
              <form:select data-placeholder="选择需要拦截的地区..." path="interceptor.unicom" multiple="true" cssClass="form-control chosen">
                <form:options items="${states}" itemLabel="name" itemValue="code"/>
              </form:select>
            </div>
          </div>

          <div class="hr-line-dashed"></div>
          <div class="form-group">
            <label class="col-sm-2 control-label">进黑洞的产品ID前缀</label>

            <div class="col-sm-6">
              <input type="text" id="productIdPrefix" name="productIdPrefix" value="${interceptor.productIdPrefix}" placeholder="输入需要拦截的前缀" class="form-control input-cus">
              <small>例如HB7(拦截湖北电信)或NA8-HB(拦截移动全网中的湖北地区), 拦截多个前缀, 请用半角逗号分开多值</small>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">进黑洞的特定用户</label>

            <div class="col-sm-6">
              <form:select data-placeholder="选择需要拦截的用户..." path="interceptor.users" multiple="true" cssClass="form-control chosen">
                <form:options items="${users}" itemLabel="displayName" itemValue="username"/>
              </form:select>
            </div>
          </div>

          <div class="hr-line-dashed"></div>
          <div class="form-group">
            <div class="col-sm-4 col-sm-offset-2">
              <button type="submit" class="btn btn-primary">保存</button>
            </div>
          </div>
        </form>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/oc/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/form-validator/jquery.form-validator.min.js"></script>
<script src="${cdn}/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript">
  $.validate({
    lang: "zh"
  });
  $(".chosen").chosen({});
  $(function () {
    activeMenu("ma-trade-interceptor");
    $("#message").delay(3000).hide(300);
  });
</script>
</body>
</html>
