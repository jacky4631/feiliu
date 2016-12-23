<%@ page contentType="text/html;charset=UTF-8" %>
<div class="panel-body">
  <form id="basic-remote" method="post" action="${ctx}/oc/channel/${action}">
    <fieldset class="form-horizontal">
      <input type="hidden" name="id" value="${channel.id}">

      <div class="form-group">
        <label class="col-sm-2 control-label"><a target="_blank" title="打开网站" href="${channel.remoteUrl}">WEB端URL</a></label>

        <div class="col-sm-10">
          <input type="text" placeholder="平台URL..." name="remoteUrl" value="${channel.remoteUrl}" class="form-control">
        </div>
      </div>
      <shiro:hasAnyRoles name="admin">
        <div class="form-group">
          <label class="col-sm-2 control-label">用户名</label>

          <div class="col-sm-10">
            <input name="remoteUsername" placeholder="请输入用户名..." class="form-control" value="${channel.remoteUsername}"/>
          </div>
        </div>
        <div class="form-group">
          <label class="col-sm-2 control-label">WEB端密码</label>

          <div class="col-sm-10">
            <input name="remotePassword" placeholder="请输入管理端密码..." class="form-control" value="${channel.remotePassword}"/>
          </div>
        </div>
      </shiro:hasAnyRoles>

      <div class="form-group">

        <label class="col-sm-2 control-label">说明</label>

        <div class="col-sm-10">
          <textarea name="remoteDescription" placeholder="说明信息..." rows="3" class="form-control">${channel.remoteDescription}</textarea>
        </div>
      </div>
      <div class="hr-line-dashed"></div>
      <div class="form-group">
        <div class="col-sm-4 col-sm-offset-2">
          <a type="button" class="btn btn-primary" href="${ctx}/oc/channel"><i class="fa fa-angle-double-left"></i>返回</a>
          <button type="submit" class="btn btn-primary">保存</button>
        </div>
      </div>
    </fieldset>
  </form>
</div>