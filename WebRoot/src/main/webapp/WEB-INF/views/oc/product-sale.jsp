<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>

<div style="height: 330px; overflow-y: auto;">
  <table style="font-size:13px !important;" class="table table-striped">
    <thead>
    <tr>
      <th>授权用户</th>
      <th>用户名</th>
      <th>公司名</th>
      <th>折扣</th>
      <th style="width:125px;">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${empty saleProducts}">
      <tr>
        <td colspan="5"><i class="fa fa-meh-o"></i>尚无授权该产品给用户</td>
      </tr>
    </c:if>
    <c:forEach var="p" items="${saleProducts}">
      <tr class="item">
        <td>
            ${p.displayUsername}
        </td>
        <td>${p.username}</td>
        <td>${p.company}</td>
        <td>${p.discount}</td>
        <td>
          <div class="btn-group">
            <button class="btn-white btn btn-xs remove" data-pid="${p.userProductId}">解除</button>
            <button class="btn-white btn btn-xs remove remove-all" data-pid="${p.userProductId}">解除整组</button>
          </div>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
<script type="text/javascript">
  $(function () {
    $('.remove, .remove-all').on("click", function () {
      var id = $(this).data('pid');
      if(!confirm("这将解除该用户的产品授权, 是否继续?")) {
        return;
      }
      var that = this;
      var url = "${ctx}/oc/users/product/remove";
      if($(this).hasClass("remove-all")) {
        url = "${ctx}/oc/users/product/remove-grp";
      }
      $.ajax({
        type: "post",
        url: url,
        data: {
          "id": id
        },
        success: function (data) {
          if (data.status == 'success') {
            $(that).parents(".item").remove();
          }
        }
      });
    });
  });
</script>
