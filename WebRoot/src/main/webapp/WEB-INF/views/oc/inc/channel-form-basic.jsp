<%@ page contentType="text/html;charset=UTF-8" %>
<div class="panel-body">
  <form id="basic-form" method="post" action="${ctx}/oc/channel/${action}">
    <fieldset class="form-horizontal">
      <div class="form-group">
        <label class="col-sm-2 control-label">供应商编号</label>

        <div class="col-sm-10"><input type="text" id="id" name="id" value="${channel.id}" readonly class="form-control"></div>
      </div>
      <div class="form-group">
        <label class="col-sm-2 control-label">供应商名称</label>

        <div class="col-sm-10"><input type="text" name="name" value="${channel.name}" placeholder="输入供货商名称" class="form-control"></div>
      </div>
      <div class="form-group">
        <label class="col-sm-2 control-label">机构代码</label>

        <div class="col-sm-10">
          <input type="text" name="orgcode" value="${channel.orgcode}" class="form-control">
          <small>有相同代码的用户和供货商被认为是同一组织</small>
        </div>
      </div>
      <div class="form-group">
        <label class="col-sm-2 control-label">创建时间</label>

        <div class="col-sm-3">
          <input type="text" name="createDate" value="<fmt:formatDate value="${channel.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly class="form-control">
        </div>

        <label class=" col-sm-1 control-label">创建人</label>

        <div class="col-sm-3"><input type="text" name="creator" value="${channel.creator}" readonly class="form-control">
        </div>
      </div>

      <div class="form-group">
        <label class="col-sm-2 control-label">是否同步接口</label>

        <div class="col-sm-10">
          <div class="checkbox">
            <label>
              <form:checkbox path="channel.realTime" id="realTime" value="true"/> 同步接口 (提交立即知道充值结果)
            </label>
          </div>
          <div>
            <small>注意: 一般只有运营商直连通道才会用同步返回状态, 因此设为同步的通道, 其产品组中一般仅配置一组产品</small>
          </div>
        </div>
      </div>

      <div class="form-group">
        <label class="col-sm-2 control-label">说明</label>

        <div class="col-sm-7">
          <textarea name="remark" name="remark" value="${channel.remark}" placeholder="请输入备注内容 ..." rows="2" class="form-control">${channel.remark}</textarea>
        </div>
      </div>

      <div class="form-group">
        <label class="col-sm-2 control-label">初始状态</label>

        <div class="col-sm-10">
          <div class="radio radio-inline">
            <label for="radio-status-o1" style="padding-left: 0;">
              <input type="radio" name="status" value="0" id="radio-status-o1" <c:if test="${channel.status == 0}"> checked</c:if>>启用
            </label>
          </div>

          <div class="radio radio-inline radio-info">
            <label for="radio-status-o2" style="padding-left: 0;">
              <input type="radio" name="status" value="-1" id="radio-status-o2" <c:if test="${channel.status == -1}"> checked</c:if>>禁用
            </label>
          </div>
        </div>
      </div>

      <div class="form-group">
        <label class="col-sm-2 control-label">统付替换</label>

        <div class="col-sm-10">
          <div>
            <label for="radio-repl-o1" style="padding-left: 0;">
              <input type="radio" name="canReplaceNA" value="true" id="radio-repl-o1" <c:if test="${channel.canReplaceNA eq true}"> checked</c:if>>
              参与替换
            </label>
          </div>

          <div>
            <label for="radio-repl-o2" style="padding-left: 0;">
              <input type="radio" name="canReplaceNA" value="false" id="radio-repl-o2" <c:if test="${channel.canReplaceNA eq false}"> checked</c:if>>
              不参与替换
            </label>
          </div>
          <small>该属性适用于省包, 若产品本身为全网统付产品, 该属性无意义</small>
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