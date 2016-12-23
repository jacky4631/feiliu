<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<div class="col-xs-12">
  <form class="form-horizontal" method="post">
    <div class="form-group">
      <label class="col-sm-3 control-label" for="selchannel">通道列表</label>

      <div class="col-sm-9">
        <select name="selchannel" id="selchannel" class="form-control input-sm">
          <option value="">-请选择通道-</option>
          <c:forEach var="channel" items="${channels}">
            <option value="${channel.id}">${channel.name}</option>
          </c:forEach>
        </select>
      </div>
    </div>

    <div class="hr-line-dashed"></div>
    <div class="form-group">
      <label class="col-sm-3 control-label">全网选择</label>

      <div class="col-sm-9">
        <div>
          <label>
            <input type="radio" name="naFirst" value="false" checked> 优先走省网 </label>
        </div>
        <div>
          <label>
            <input type="radio" name="naFirst" value="true" > 优先精确匹配全网(无则走省网) </label>
        </div>
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-3 control-label">成本保护</label>

      <div class="checkbox col-sm-9">
        <label>
          <input id="priceProtected" type="checkbox" checked="checked" value="true" name="priceProtected"> 启用
        </label>
      </div>
    </div>
    <div class="alert alert-info" style="padding:10px 8px;">
      注意, 手工送产品时, 通道中的产品是否开启不影响送单. 即系统会强制送单.
    </div>
  </form>
</div>