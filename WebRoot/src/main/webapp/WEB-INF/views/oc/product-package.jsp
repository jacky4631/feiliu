<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>

<div style="height: 330px; overflow-y: auto;">
  <table style="font-size:13px !important;" class="table table-striped">
    <thead>
    <tr>
      <th>供应商</th>
      <th>产品名称</th>
      <th>供方产品ID</th>
      <th>折扣</th>
      <th>优先级</th>
      <th>状态</th>
      <th style="width:60px;">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${empty flowpackages}">
      <tr>
        <td colspan="7"><i class="fa fa-meh-o"></i> 尚无通道支持该产品</td>
      </tr>
    </c:if>
    <c:forEach var="fpbean" items="${flowpackages}">
      <tr>
        <td>
          <c:if test="${fpbean.channelStatus}">
            <span style="font-weight:600;color:#1ab394">${fpbean.channel}</span>
          </c:if>
          <c:if test="${not fpbean.channelStatus}">
            ${fpbean.channel}
          </c:if>
        </td>
        <td>${fpbean.name}</td>
        <td>${fpbean.origiProductId}</td>
        <td>${fpbean.discount}</td>
        <td style="text-align: center;">${fpbean.priority}</td>
        <td class="status">
          <c:if test="${fpbean.enabled}">
            <small class='label label-primary'>正常</small>
          </c:if>
          <c:if test="${not fpbean.enabled}">
            <small class='label'>禁用</small>
          </c:if>
        </td>
        <td>
          <button class="btn-white btn btn-xs chg-status" data-id="${fpbean.id}">开关</button>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
<script type="text/javascript">
  $(function () {
    $('.chg-status').on("click", function () {
      var id = $(this).data('id');
      var that = this;
      $.ajax({
        type: "post",
        url: "${ctx}/oc/flowpackage/chstatus",
        data: {
          "id": id
        },
        success: function (data) {
          if (data.status == 'success') {
            if (data.enabled) {
              $(that).parent().prev().html("<small class='label label-primary'>正常</small>");
            } else {
              $(that).parent().prev().html("<small class='label'>禁用</small>");
            }
          }
        }
      });
    });
  });
</script>
