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
    <tags:content_header icon="fa-bullhorn" sysname="${sysname}" title="短信备案"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>短信备案
            <small>集中记录通道的短信提醒</small>
          </h5>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-xs-12">
              <form role="form" method="post" action="${ctx}/oc/smmemo/save">
                <input type="hidden" name="id" value="${smMemo.id}">

                <div class="form-group">
                  <label>所属通道-产品组</label>
                  <input name="owner" placeholder="请输入所属通道信息..." class="form-control" value="${smMemo.owner}"/>
                  <small>标记技巧: 通道编号-通道名-产品组, 例如: 500-测试通道-移动全网</small>
                </div>

                <div class="form-group">
                  <label>短信特服号</label>
                  <input name="spcode" placeholder="请输入短信特服号..." class="form-control" value="${smMemo.spcode}"/>
                  <small>短信特服号又称短信发送端口号, 即提醒短信的发送发号码, 例如: 10086</small>
                </div>

                <div class="form-group">
                  <label>内容</label>
                  <textarea name="smTemplate" placeholder="请输入短信模板..." rows="3" class="form-control">${smMemo.smTemplate}</textarea>
                </div>

                <div class="form-group">
                  <label>备注</label>
                  <textarea name="desc" placeholder="请输入备注信息..." rows="2" class="form-control">${smMemo.desc}</textarea>
                </div>

                <div class="form-group">
                  <label>创建时间</label>

                  <div>
                    <input type="text" name="created" readonly value="<fmt:formatDate value="${smMemo.created}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="form-control">
                    <input type="hidden" name="creator" value="${smMemo.creator}">
                  </div>
                </div>

                <div class="box-footer">
                  <a class="btn btn-info" href="${ctx}/oc/smmemo">返回</a>
                  <button class="btn btn-primary pull-right" type="submit">保存</button>
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
    activeMenu("ma-tools-smmemo");
  });
</script>
</body>
</html>
