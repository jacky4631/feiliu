<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-bullhorn" sysname="${sysname}" title="黑名单"/>
    <div class="wrapper wrapper-content animated fadeInRight">
      <c:if test="${not empty message}">
        <div id="message" class="alert alert-success">
          <button data-dismiss="alert" class="close">×</button>
            ${message}
        </div>
      </c:if>
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>黑名单
            <small>出现在黑名单中的手机号不能在本系统充值</small>
          </h5>
        </div>

        <div class="ibox-content">
          <div class="row">
            <div class="col-xs-12">
              <form role="form" method="post" action="${ctx}/oc/blacklist/save">
                <input type="hidden" name="id" value="${blacklist.id}">

                <div class="form-group">
                  <label>号码列表(号码请使用半角逗号分隔)</label>
                  <textarea name="mobiles" placeholder="请输入手机号码..." rows="15" class="form-control">${blacklist.mobiles}</textarea>
                </div>
                <div class="hr-line-dashed"></div>
                <div class="row">
                  <div class="col-sm-2 form-group">
                    <label class="control-label">动态黑名单</label>

                    <div class="checkbox">
                      <label>
                        <form:checkbox path="blacklist.enableDynamic" value="true"/> 启用
                      </label>
                    </div>
                  </div>

                  <div class=" col-sm-3 form-group">
                    <label class="control-label">自动解除时长(小时)</label>

                    <input type="text" name="monitorPeriod" value="${blacklist.monitorPeriod}" class="form-control">
                  </div>

                  <div class="col-sm-6 form-group">
                    <label class="control-label">充值失败上限(24小时内, 对同一号码充值达到上限进入动态黑名单)</label>

                    <input type="text" name="allowFailTimes" value="${blacklist.allowFailTimes}" class="form-control" style="width:380px;">
                  </div>

                </div>
                <div class="hr-line-dashed"></div>
                <div class="box-footer">
                  <button class="btn btn-primary pull-left" type="submit">更新</button>
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
<script type="text/javascript">
  $(function () {
    activeMenu("ma-trade-blacklist");
    $("#message").delay(2000).hide(300);
  });
</script>
</body>
</html>
