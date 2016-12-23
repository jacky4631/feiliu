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
    <tags:content_header icon="fa-bullhorn" sysname="${sysname}" title="通知发布"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>通知发布
            <small>运维或通道信息的发布渠道</small>
          </h5>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-xs-12">
              <form role="form" method="post" action="${ctx}/oc/bulletin/save">
                <input type="hidden" name="id" value="${bulletin.id}">

                <div class="form-group">
                  <label>标题</label>
                  <input name="title" placeholder="请输入标题..." class="form-control" value="${bulletin.title}"/>
                </div>
                <div class="form-group">
                  <label>发布时间</label>

                  <div>
                    <input type="text" readonly value="<fmt:formatDate value="${bulletin.created}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="form-control">
                  </div>
                </div>
                <div class="form-group">
                  <label>内容</label>
                  <textarea name="content" placeholder="请输入内容..." rows="3" class="form-control">${bulletin.content}</textarea>
                </div>

                <div class="box-footer">
                  <a class="btn btn-info" href="${ctx}/oc/bulletin">返回</a>
                  <button class="btn btn-primary pull-right" type="submit">发布</button>
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
    activeMenu("ma-tools-bulletin");
  });
</script>
</body>
</html>
