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
    <tags:content_header icon="fa-user-secret" sysname="${sysname}" title="失败回调拦截器"/>
    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-title">
        <h5>失败回调拦截器定义
          <small>被拦截的订单可自动或手工重新处理, 也可直接放行回调用户端. 被拦截的失败订单暂时未退费</small>
        </h5>
      </div>
      <div class="ibox-content">
        <c:if test="${not empty message}">
          <div id="message" class="alert alert-success">
            <button data-dismiss="alert" class="close">×</button>
              ${message}</div>
        </c:if>
        <form class="form-horizontal" method="post" action="${ctx}/oc/interceptors/callback/${action}">
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
              <p>输入整数, 数字越小越先执行。</p>
            </div>
          </div>
          <div class="hr-line-dashed"></div>
          <div class="form-group">
            <label class="col-sm-2 control-label">移动拦截地区</label>

            <div class="col-sm-6">
              <form:select data-placeholder="选择需要拦截的地区..." path="interceptor.cmcc" multiple="true" cssClass="form-control chosen">
                <form:options items="${states}" itemLabel="name" itemValue="code"/>
              </form:select>
              <br>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">电信拦截地区</label>

            <div class="col-sm-6">
              <form:select data-placeholder="选择需要拦截的地区..." path="interceptor.telecom" multiple="true" cssClass="form-control chosen">
                <form:options items="${states}" itemLabel="name" itemValue="code"/>
              </form:select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">联通拦截地区</label>

            <div class="col-sm-6">
              <form:select data-placeholder="选择需要拦截的地区..." path="interceptor.unicom" multiple="true" cssClass="form-control chosen">
                <form:options items="${states}" itemLabel="name" itemValue="code"/>
              </form:select>
            </div>
          </div>

          <div class="hr-line-dashed"></div>
          <div class="form-group">
            <label class="col-sm-2 control-label">拦截特定用户失败订单</label>

            <div class="col-sm-6">
              <form:select data-placeholder="选择需要拦截的用户..." path="interceptor.users" multiple="true" cssClass="form-control chosen">
                <form:options items="${users}" itemLabel="displayName" itemValue="username"/>
              </form:select>
            </div>
          </div>

          <div class="hr-line-dashed"></div>
          <div class="form-group">
            <label class="col-sm-2 control-label">拦截处理办法</label>

            <div class="col-sm-10">
              <div>
                <label>
                  <input type="radio" name="nextStep" value="AUTO"
                  <c:if test="${interceptor.nextStep eq 'AUTO'}"> checked</c:if>> 自动选择下一可用通道 </label>
              </div>
              <div>
                <label>
                  <input type="radio" name="nextStep" value="MANUAL"
                  <c:if test="${interceptor.nextStep eq 'MANUAL'}"> checked</c:if>> 等待人工处理 </label>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">成本/售价保护</label>

            <div class="checkbox col-sm-10">
              <label>
                <form:checkbox path="interceptor.priceProtected" value="true"/>启用
              </label>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label" for="name">重试次数</label>

            <div class="col-sm-10">
              <input type="text" id="retryTimes" data-validation="required" name="retryTimes" value="${interceptor.retryTimes}" class="form-control input-cus">
              <span>0 表示尝试所有可用通道</span>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">状态</label>

            <div class="checkbox col-sm-10">
              <label>
                <form:checkbox path="interceptor.status" value="true"/>启用
              </label>
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-4 col-sm-offset-2">
              <a href="${ctx}/oc/interceptors/callback" class="btn btn-primary">返回</a>
              <button type="submit" class="btn btn-primary">保存</button>
            </div>
          </div>
        </form>
      </div>
    </div>
    <br>
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
    activeMenu("ma-trade-failinterceptor");
    $("#message").delay(3000).hide(300);
  });
</script>
</body>
</html>
