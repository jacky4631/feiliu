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
    <tags:content_header icon="fa-user-secret" sysname="${sysname}" title="失败回调拦截器"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <c:if test="${not empty message}">
          <div id="message" class="alert alert-success">
            <button data-dismiss="alert" class="close">×</button>
              ${message}</div>
        </c:if>
        <div class="box-body form-inline">
          <div class="btn-group" role="group">
            <a id="btn_new" class="btn btn-info" href="${ctx}/oc/interceptors/callback/create">新建拦截器</a>
            <button type="button" id="btn_del" class="btn btn-info">删除拦截器</button>
          </div>
        </div>
      </div>

      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th style="width: 20px;padding-right: 2px;">选</th>
              <th>名称</th>
              <th>拦截对象</th>
              <th>处理方法</th>
              <th style="width:80px;">次序</th>
              <th style="width:80px;">状态</th>
              <th style="width:50px;">操作</th>
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
    activeMenu("ma-trade-failinterceptor");
    $.fn.dataTable.ext.legacy.ajax = true;
    var dt = $('#dt1').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "stateSave": true,
      "ajax": {
        "url": "${ctx}/oc/interceptors/callback/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {});
        }
      },
      "order": [4, "asc"],
      "columns": [
        {"data": "id", sortable: false},
        {"data": "name", sortable: false},
        {"data": "users", sortable: false},
        {"data": "nextStep", sortable: false},
        {"data": "idx"},
        {"data": "status", sortable: false},
        {"data": null, sortable: false}
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
            return "<a href='" + $ctx + "/oc/interceptors/callback/update/" + row.id + "'>" + data + "</a>";
          },
          "targets": 1
        },
        {
          "render": function (data, type, row) {
            return row.cmcc + " " + row.telecom + " " + row.unicom + " " + row.users;
          },
          "targets": 2
        },
        {
          "render": function (data, type, row) {
            if (data == 'AUTO') {
              return "自动选择下一可用通道";
            } else {
              return "等待人工处理";
            }
          },
          "targets": 3
        },
        {
          "render": function (data, type, row) {
            if (data == true)
              return "<small class='label label-primary'>启用</small>";
            else
              return "<small class='label'>禁用</small>";
          },
          "targets": -2
        },
        {
          "render": function (data, type, row) {
            var btn =
              '<div class="btn-group">' +
              '<button class="btn-white btn btn-xs chg" data-iid="' + row.id + '"> 开关</button>' +
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

    function selectedIds() {
      var ids = [];
      $("input[name=ids]:checked").each(function () {
        ids.push($(this).val());
      });
      return ids;
    }

    $(document).on('click', '.chg', function () {
      var id = $(this).data("iid");
      $.ajax({
        type: "post",
        url: "${ctx}/oc/interceptors/callback/chgstatus",
        data: {"iid": id},
        success: function (data) {
          if (data.status == "success") {
            dt.draw(false);
          }
        }
      });
    });

    $('#btn_del').on('click', function () {
      var ids = selectedIds();
      if (ids.length == 0) {
        alert("没有选择要删除的拦截器");
        return;
      }
      if (confirm("确定要删除这些拦截器吗?")) {
        $.ajax({
          url: "${ctx}/oc/interceptors/callback/batch-del",
          traditional: true,
          type: 'post',
          data: {"ids": ids},
          success: function (data) {
            dt.draw(false);
          }
        });
      }
    });
  })
</script>
</body>
</html>
