<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
  <style>
    .wordwrap {
      white-space: normal !important;
      word-wrap: break-word !important;
      word-break: normal;
    }
  </style>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-exchange" sysname="${sysname}" title="用户回调"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <div class="form-inline">
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">用户名</div>
              <input type="text" class="form-control" id="filter_EQ_username" placeholder="用户名">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">手机号码</div>
              <input type="text" class="form-control" id="filter_EQ_mobile" placeholder="手机号码">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">交易编号</div>
              <input type="text" class="form-control" id="filter_EQ_tradeId" placeholder="交易编号">
            </div>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="search" class="btn btn-primary">查询</button>
            <button type="button" id="reset" class="btn btn-primary">全部</button>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="btn-callback" class="btn btn-info">重新回调</button>
            <button type="button" id="btn-remove" class="btn btn-info">删除</button>
          </div>
        </div>
      </div>

      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th style="width:18px;"><input id='checkall' type='checkbox' value='true'/></th>
              <th>交易编号</th>
              <th>用户</th>
              <th>手机号</th>
              <th style="width: 30px;">重试</th>
              <th>时间</th>
              <th>交易</th>
              <th>消息</th>
              <th style="width:20%;">回调日志</th>
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
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("ma-userreport");
    $.fn.dataTable.ext.legacy.ajax = true;
    $.ajaxSetup({cache: false});
    toastr.options = {
      closeButton: true,
      "positionClass": "toast-top-center",
      showMethod: 'slideDown',
      timeOut: 2000
    };

    var dt = $('#dt1').addClass("nowrap").DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "responsive": true,
      "ajax": {
        "url": "${ctx}/oc/userreport/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_username": $("#filter_EQ_username").val(),
            "filter_EQ_mobile": $("#filter_EQ_mobile").val(),
            "filter_EQ_tradeId": $("#filter_EQ_tradeId").val()
          });
        }
      },
      "columns": [
        {"data": "tradeId", sortable: false},
        {"data": "tradeId", sortable: false},
        {"data": "username", sortable: false},
        {"data": "mobile", sortable: false},
        {"data": "retryTimes", sortable: false},
        {"data": "createDate"},
        {"data": "isSuccess", sortable: false},
        {"data": "message", sortable: false},
        {"data": "pushFailReason", sortable: false}
      ],
      "filter": false,
      "order": [5, "desc"],
      "columnDefs": [
        {
          "render": function (data, type, row) {
            return "<input name='ids' type='checkbox' value='" + data + "'>";
          },
          "targets": 0
        },
        {
          "render": function (data, type, row) {
            if (data)
              return "<small class='label label-info'>成功</small>";
            else
              return "<small class='label'>失败</small>";
          },
          className: 'dt-body-center',
          "targets": 6
        },
        {
          "render": function (data, type, row) {
            return data + "次";
          },
          "targets": 4
        },
        {targets: [8], className: 'wordwrap'}
      ],
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });

    $('#dt1').on('draw.dt', function () {
      $("#checkall").removeAttr("checked");
    });

    $("#search").on('click', function () {
      dt.draw(false);
    });
    $("#reset").on('click', function () {
      $("input[id^='filter_']").val("");
      dt.draw(false);
    });
    $('#checkall').click(function () {
      var items = document.getElementsByName('ids');
      for (var i = 0; i < items.length; i++) {
        items[i].checked = this.checked;
      }
    });

    function selectedIds() {
      var ids = [];
      $("input[name=ids]:checked").each(function () {
        ids.push($(this).val());
      });
      return ids;
    }

    $('#btn-callback').click(function () {
      var ids = selectedIds();
      if (ids.length == 0) {
        toastr.warning("没有选择要操作的报告!", "警告");
        return;
      }
      if (confirm("确定要重新推送这些报告吗?")) {
        $.ajax({
          url: $ctx + "/oc/userreport/repush",
          traditional: true,
          type: 'post',
          data: {"ids": ids},
          success: function (data) {
            dt.draw();
          }
        });
      }
    });
    $('#btn-remove').click(function () {
      var ids = selectedIds();
      if (ids.length == 0) {
        toastr.warning("没有选择要操作的报告!", "警告");
        return;
      }
      if (confirm("确定要删除这些报告吗?")) {
        $.ajax({
          url: $ctx + "/oc/userreport/batch-del",
          traditional: true,
          type: 'post',
          data: {"ids": ids},
          success: function (data) {
            dt.draw();
          }
        });
      }
    });
  });
</script>
</body>
</html>
