<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/artDialog/ui-dialog.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-at" sysname="${sysname}" title="充值提醒短信"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <c:if test="${not empty message}">
          <div id="message" class="alert alert-success">
            <button data-dismiss="alert" class="close">×</button>
              ${message}</div>
        </c:if>
        <div class="box-body form-inline">
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">用户名</div>
              <input type="text" class="form-control" style="width:120px;" id="filter_EQ_username" placeholder="输入用户名">
            </div>
            <div class="input-group">
              <div class="input-group-addon">手机号</div>
              <input type="text" class="form-control" style="width:120px;" id="filter_EQ_mobile" placeholder="输入手机号">
            </div>
            <div class="input-group">
              <div class="input-group-addon">交易号</div>
              <input type="text" class="form-control" style="width:120px;" id="filter_EQ_tradeId" placeholder="输入交易号">
            </div>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="search" class="btn btn-primary">查询</button>
            <button type="button" id="reset" class="btn btn-primary">全部</button>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="btn_fee" class="btn btn-info">统一扣费...</button>
          </div>
        </div>
      </div>

      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th>发送时间</th>
              <th>交易编号</th>
              <th>用户账号</th>
              <th>手机号码</th>
              <th>金额(分)</th>
              <th>扣款标识</th>
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
<div id="fee" style="height:100px;width:390px;display:none;"></div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-min.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-plus-min.js"></script>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("ma-tools-smlog");
    toastr.options = {
      closeButton: true,
      "positionClass": "toast-top-center",
      timeOut: 3500
    };
    $.fn.dataTable.ext.legacy.ajax = true;
    var dt = $('#dt1').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "stateSave": true,
      "responsive": true,
      "ajax": {
        "url": "${ctx}/oc/smlog/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_username": $("#filter_EQ_username").val(),
            "filter_EQ_mobile": $("#filter_EQ_mobile").val(),
            "filter_EQ_tradeId": $("#filter_EQ_tradeId").val()
          });
        }
      },
      "order": [0, "desc"],
      "columns": [
        {"data": "created"},
        {"data": "tradeId", sortable: false},
        {"data": "username", sortable: false},
        {"data": "mobile", sortable: false},
        {"data": "billAmount", sortable: false},
        {"data": "paid", sortable: false}
      ],
      "filter": false,

      "columnDefs": [
        {
          "render": function (data, type, row) {
            if (data)
              return "<small class='label label-primary'>已扣款</small>";
            else
              return "<small class='label'>未扣款</small>";
          },
          "targets": -1
        }
      ],
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });

    $("#search").on('click', function () {
      dt.draw();
    });
    $("#reset").on('click', function () {
      $("input[id^='filter_']").val("");
      dt.draw();
    });

    $("#message").delay(2000).hide(300);

    var payFee = function () {
      var elem = document.getElementById('fee');
      var d = dialog({
        "id": 'dialog-fee',
        "content": elem,
        "title": "阶段性扣除短信费",
        "zIndex": 2000,
        "onshow": function () {
          $(elem).load($ctx + '/oc/smlog/fee-dialog');
        },
        button: [
          {
            value: '扣费',
            callback: function () {
              var username = $("#username").val();
              if (username == '') {
                toastr.error("请输入需要扣费的用户账户", "提示");
                return false;
              }
              $.ajax({
                type: "post",
                traditional: true,
                url: "${ctx}/oc/smlog/pay",
                data: {"username": username},
                success: function (data) {
                  if (data.status == "success") {
                    toastr.success(data.message, "提示");
                    dt.draw(false);
                  } else {
                    toastr.warning(data.message, "错误");
                  }
                }
              });
            },
            autofocus: true
          },
          {
            value: '关闭'
          }
        ],
        statusbar: '<label id="msg"></label>'
      });
      d.show();
    };

    $("#btn_fee").on('click', function () {
      payFee();
    });
  })
</script>
</body>
</html>
