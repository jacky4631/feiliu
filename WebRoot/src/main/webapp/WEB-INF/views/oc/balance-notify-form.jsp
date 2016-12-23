<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
  <link href="${cdn}/css/plugins/chosen/chosen.css" rel="stylesheet">
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-cc-visa" sysname="${sysname}" title="余额提醒"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>余额提醒
            <small>一个通道的余额警告, 半小时内仅提醒一次, 所选通道全部关闭则不提醒</small>
          </h5>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-xs-12">
              <form role="form" method="post" action="${ctx}/oc/notify/save">
                <input type="hidden" name="id" value="${notify.id}">

                <div class="form-group">
                  <label>标题</label>
                  <input name="title" placeholder="请输入标题, 建议输入供应商名称即可..." class="form-control" value="${notify.title}"/>
                </div>

                <div class="form-group">
                  <label>监控通道</label>
                  <form:select data-placeholder="请选择被监控的通道" path="notify.channels" multiple="true" cssClass="form-control chosen">
                    <form:options items="${channels}" itemLabel="name" itemValue="id"/>
                  </form:select>
                  <div style="margin:15px 0;">如果选择多个通道, 表示这几个通道余额之和与阈值比较. 适用于一个上游账号, 开了多个通道的情形</div>
                </div>

                <div class="form-group">
                  <label>警告阈值(元)</label>
                  <input name="threshold" placeholder="低于此金额将警告" class="form-control" value="${notify.threshold}"/>
                </div>

                <div class="form-group">
                  <label>提醒手机号</label>
                  <input name="mobiles" placeholder="多个号码用半角逗号分隔" class="form-control" value="${notify.mobiles}"/>
                </div>

                <div class="form-group checkbox">
                  <label>
                    <form:checkbox path="notify.status" value="true"/>启用该提醒定义
                  </label>
                </div>

                <div class="form-group">
                  <label>创建时间</label>

                  <div>
                    <input type="text" name="created" readonly value="<fmt:formatDate value="${notify.created}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="form-control">
                  </div>
                </div>

                <div class="box-footer">
                  <a class="btn btn-info" href="${ctx}/oc/notify">返回</a>
                  <button class="btn btn-primary" type="submit">保存</button>
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
<script src="${cdn}/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("ma-tools-notify");
    $(".chosen").chosen({});
  });
</script>
</body>
</html>
