<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav-admin.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-cubes" sysname="${sysname}" title="角色管理"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <c:if test="${not empty message}">
          <div id="message" class="alert alert-success">
            <button data-dismiss="alert" class="close">×</button>
              ${message}</div>
        </c:if>
        <div class="box-body form-inline">
          <div class="btn-group" role="group">
            <a id="btn_new" class="btn btn-info" href="${ctx}/oc/roles/create">添加角色</a>
          </div>
        </div>
      </div>

      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th>角色名</th>
              <th>角色编码</th>
              <th>说明</th>
              <th style="width:80px;">操作</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/oc/footer.jsp" %>
  </div>
</div>

<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("mc-role");
    $.fn.dataTable.ext.legacy.ajax = true;
    var dt = $('#dt1').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "stateSave": true,
      "ajax": {
        "url": "${ctx}/oc/roles/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {});
        }
      },
      "order": [0, "asc"],
      "columns": [
        {"data": "name"},
        {"data": "code", sortable: false},
        {"data": "description", sortable: false},
        {"data": null, sortable: false}
      ],
      "filter": false,

      "columnDefs": [
        {
          "render": function (data, type, row) {
            var btn =
              '<div class="btn-group">' +
              '<a class="btn-white btn btn-xs edit" href="${ctx}/oc/roles/update/' + row.id + '">编辑</a>' +
              '<button class="btn-white btn btn-xs del" data-id="' + row.id + '"> 删除</button>' +
              '</div>';
            return btn;
          },
          "targets": -1
        }
      ],
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });

    $("#message").delay(2000).hide(300);

    $(document).on('click', '.del', function () {
      var id = $(this).data("id");
      if (!confirm("您确认要删除角色" + id + "吗?")) {
        return;
      }
      $.ajax({
        type: "post",
        traditional: true,
        url: "${ctx}/oc/roles/remove",
        data: {"rid": id},
        success: function (data) {
          if (data.status == "success") {
            dt.draw(false);
          } else {
            alert(data.message);
          }
        }
      });
    });

  })
</script>
</body>
</html>
