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
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-cc-visa" sysname="${sysname}" title="通道余额提醒"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <c:if test="${not empty message}">
          <div id="message" class="alert alert-success">
            <button data-dismiss="alert" class="close">×</button>
              ${message}</div>
        </c:if>
        <div class="box-body form-inline">
          <div class="btn-group" role="group">
            <a id="btn_new" class="btn btn-info" href="${ctx}/oc/notify/create">增加提醒</a>
          </div>
        </div>
      </div>

      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th style="width: 20px;padding-right: 2px;">选</th>
              <th style="width:200px;">标题</th>
              <th style="width:120px;">提醒阈值(元)</th>
              <th>提醒手机号</th>
              <th style="width:140px;">设置时间</th>
              <th style="width:50px;">状态</th>
              <th style="width:116px;">操作</th>
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
    activeMenu("ma-tools-notify");
    $.fn.dataTable.ext.legacy.ajax = true;
    var dt = $('#dt1').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "stateSave": false,
      "ajax": {
        "url": "${ctx}/oc/notify/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {});
        }
      },
      "order": [1, "asc"],
      "columns": [
        {"data": "id", sortable: false},
        {"data": "title", sortable: false},
        {"data": "threshold", sortable: false},
        {"data": "mobiles", sortable: false},
        {"data": "created", sortable: false},
        {"data": "status", sortable: false},
        {"data": "id", sortable: false}
      ],
      "filter": false,

      "columnDefs": [
        {
          "render": function (data, type, row) {
            return "<input name='ids' type='checkbox' value='" + data + "'>";
          },
          "targets": 0
        },
        {
          "render": function (data, type, row) {
            return "<a href='" + $ctx + "/oc/notify/update/" + row.id + "'>" + data + "</a>";
          },
          targets: 1
        },
        {
          "render": function (data, type, row) {
            if (data) {
              return "<small class='label label-primary'>启用</small>";
            } else {
              return "<small class='label'>禁用</small>";
            }
          },
          "targets": -2
        },
        {
          "render": function (data, type, row) {
            return '<div class="btn-group">' +
              "<a type='button' class='btn-white btn btn-xs' href='" + $ctx + "/oc/notify/update/" + data + "'> 编辑 </a>" +
              "<input type='button' class='btn-white btn btn-xs chg' value='开关' data-id='" + data + "'>" +
              "<input type='button' class='btn-white btn btn-xs del' value='删除' data-id='" + data + "'>" +
              "</div>";
          },
          "targets": -1
        }
      ],
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });

    $("#message").delay(2000).hide(300);

    $(document).on('click', '.chg', function () {
      var id = $(this).data("id");
      $.ajax({
        type: "post",
        url: "${ctx}/oc/notify/chgstatus",
        data: {"nid": id},
        success: function (data) {
          if (data.status == "success") {
            dt.draw(false);
          }
        }
      });
    });

    $(document).on('click', '.del', function () {
      if (!confirm("你确定要删除该提醒吗?")) {
        return;
      }
      var id = $(this).data("id");
      $.ajax({
        type: "post",
        url: "${ctx}/oc/notify/del",
        data: {"nid": id},
        success: function (data) {
          if (data.status == "success") {
            dt.draw(false);
          }
        }
      });
    });

  });
</script>
</body>
</html>
