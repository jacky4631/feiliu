<%@ page contentType="text/html;charset=UTF-8" %>
<div class="panel-body">
  <div class="row">
    <div class="col-sm-10">
      <form id="form-adv" class="form-horizontal" method="post" action="${ctx}/oc/channel/update-param">
        <div class="form-group">
          <label class="col-sm-2 control-label">接口协议</label>
          <select name="handlerClass" id="handlerClass" class="form-control plugin_select">
            <option value="">-请选择-</option>
            <c:forEach var="plugin" items="${plugins}">
              <option value="${plugin.handlerClass}" <c:if
                test="${channel.channelConnectionParam.handlerClass eq plugin.handlerClass}"> selected="true" </c:if>>${plugin.title}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group">
          <label class="col-sm-2 control-label">接口参数</label>
          <textarea id="code2" name="channelConnectionParam.channelConnectionParamJson" rows="3">${channel.channelConnectionParamJson}</textarea>
        </div>

        <div class="hr-line-dashed"></div>
        <div class="form-group">
          <div class="col-sm-4 col-sm-offset-2">
            <a type="button" class="btn btn-primary" href="${ctx}/oc/channel"><i class="fa fa-angle-double-left"></i>返回</a>
            <button type="button" id="adv-save" class="btn btn-primary">应用参数</button>
            <button type="button" id="adv-test" class="btn btn-primary">通道测试...</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>