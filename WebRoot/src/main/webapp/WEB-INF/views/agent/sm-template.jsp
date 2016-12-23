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
    <tags:content_header icon="fa-commenting-o" sysname="${sysname}" title="短信设置"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>短信模板
            <small>设置是否有效受限于管理员的全局配置</small>
          </h5>
        </div>
        <c:if test="${not empty message}">
          <div id="message" class="alert alert-success">
            <button data-dismiss="alert" class="close">×</button>
              ${message}</div>
        </c:if>
        <div class="ibox-content">
          <div class="row">
            <div class="col-md-8">
              <form role="form" method="post" action="${ctx}/agent/sms-template">
                <div class="form-group">
                  <div class="checkbox">
                    <label>
                      <form:checkbox path="template.sendSm" value="true"/>
                      成功后需要短信提醒
                    </label>
                  </div>
                </div>
                <div class="form-group">
                  <label>WEB端发送短信模板</label>
                  <textarea name="webTemplate" placeholder="请输入短信内容 ..." rows="3" class="form-control">${template.webTemplate}</textarea>
                </div>
                <div class="form-group">
                  <label>API发送接入短信模板</label>
                  <textarea name="content" placeholder="请输入短信内容 ..." rows="3" class="form-control">${template.content}</textarea>
                </div>
                <div class="box-footer">
                  <button class="btn btn-primary pull-right" type="submit">保存设置</button>
                </div>
              </form>
            </div>
            <div class="col-md-4">
              <div class="callout callout-info">
                <h4>模板设置技巧</h4>

                <p>您在设置短信模板的时候, 可以使用动态关键字, 这些关键字可在发送短信时转换为实际的参数。可用关键字描述如下:</p>
                <ul>
                  <li><i>$mobile$</i> 将会被替换为被充值的手机号码</li>
                  <li><i>$company$</i> 将会被替换为您的公司名称</li>
                  <li><i>$packet$</i> 将会被替换为流量包大小</li>
                </ul>
                <p>如果您选择需要发送短信, 但实际您的短信模板中没有填写内容, 那么等同于设置为不发送短信。</p>

                <b>示例</b>

                <p>尊敬的客户，您好！$company$给您赠送的$packet$MB流量已经生效，本月可用。感谢您的支持！</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/agent/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/http-script.jsp" %>
<script type="text/javascript">
  $(function () {
    $("#m-sm").addClass("active");
  });
</script>
</body>
</html>
