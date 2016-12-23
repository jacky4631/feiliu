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
    <tags:content_header icon="fa-plug" sysname="${sysname}" title="供应商列表"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <div class="form-inline">
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">编号</div>
              <input type="text" style="width:100px;" class="form-control" id="filter_EQ_id" placeholder="输入ID">
            </div>
            <div class="input-group">
              <div class="input-group-addon">名称</div>
              <input type="text" style="width:120px;" class="form-control" id="filter_LIKE_name" placeholder="供应商名称">
            </div>
            <div class="input-group">
              <div class="input-group-addon">机构代码</div>
              <input type="text" style="width:120px;" class="form-control" id="filter_EQ_orgcode" placeholder="机构代码">
            </div>
            <div class="input-group">
              <div class="input-group-addon">状态</div>
              <select name="filter_EQ_status" id="filter_EQ_status" class="form-control">
                <option value="">全部</option>
                <option value="0">启用</option>
                <option value="-1">禁用</option>
              </select>
            </div>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="search" class="btn btn-primary">查询</button>
            <button type="button" id="reset" class="btn btn-primary">全部</button>
          </div>
          <div class="btn-group" role="group">
            <a href="${ctx}/oc/channel/create" class="btn btn-info">新增</a>
            <button type="button" id="btn_remove" class="btn btn-info">删除</button>
            <button type="button" id="btn_deactive" class="btn btn-info"><i class="fa fa-circle-o-notch"></i> 下线</button>
            <button type="button" id="btn_active" class="btn btn-info"><i class="fa fa-rocket"></i> 上线</button>
          </div>
        </div>
      </div>
      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th style="width:25px;">选</th>
              <th style="width:50px;">编号</th>
              <th>名称</th>
              <th>机构代码</th>
              <th>登记时间</th>
              <th>余额</th>
              <th>实时</th>
              <th>状态</th>
              <th style="width:30%;">说明</th>
              <shiro:hasAnyRoles name="admin,financial">
                <th style="width:75px;">操作</th>
              </shiro:hasAnyRoles>
              <shiro:hasAnyRoles name="operator">
                <th style="width:55px;">操作</th>
              </shiro:hasAnyRoles>
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
<div id="dlg" style="height:160px;width:400px;display:none;"></div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-min.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-plus-min.js"></script>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("ma-channel");
    $.fn.dataTable.ext.legacy.ajax = true;
    toastr.options = {
      closeButton: true,
      "positionClass": "toast-top-center",
      timeOut: 2500
    };
    var dt = $('#dt1').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "stateSave": true,
      "responsive": true,
      "ajax": {
        "url": $ctx + "/oc/channel/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_id": $("#filter_EQ_id").val(),
            "filter_EQ_orgcode": $("#filter_EQ_orgcode").val(),
            "filter_LIKE_name": $("#filter_LIKE_name").val(),
            "filter_EQ_status": $("#filter_EQ_status").val()
          });
        }
      },
      "columns": [
        {"data": "id", "orderable": false},
        {"data": "id"},
        {"data": "name"},
        {"data": "orgcode", "orderable": false},
        {"data": "createDate"},
        {"data": "balance", "orderable": false},
        {"data": "realTime", "orderable": false},
        {"data": "status", "orderable": false},
        {"data": "remark", "orderable": false},
        {"data": "id", "orderable": false}
      ],
      "order": [1, "asc"],
      "filter": false,
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      },
      "columnDefs": [
        {targets: [4], className: 'dt-body-center'},
        {
          "render": function (data, type, row) {
            return "<input name='ids' type='checkbox' value='" + data + "'>";
          },
          "targets": 0
        },
        {
          "render": function (data, type, row) {
            var lock = "";
            if (!row.canReplaceNA) {
              lock = " <i class='fa fa-lock' style='color:darkslategray;'></i>";
            }
            return "<a href='" + $ctx + "/oc/channel/update/" + row.id + "'>" + data + "</a>" + lock;
          },
          "targets": 2
        },
        {
          render: function (data, type, row) {
            if (data == true) {
              return "<i style='color: darkgreen' class='fa fa-check-circle-o'></i>";
            } else {
              return "<i style='color: darkgreen' class='fa fa-circle-o'></i>";
            }
          },
          targets: 6
        },
        {
          "render": function (data, type, row) {
            switch (data) {
              case 0:
                return "<small class='label label-primary'>启用</small>";
              case 8:
                return "<small class='label label-warning'>暂启</small>";
              case 9:
                return "<small class='label label-warning'>暂禁</small>";
              case -1:
                return "<small class='label'>禁用</small>";
            }
          },
          "targets": 7
        },
        {
          render: function (data, type, row) {
            var remoteUrl = "";
            if (row.remoteUrl != null && row.remoteUrl != "") {
              remoteUrl = "<a target='_blank' title='链接到上游网站' href='" + row.remoteUrl +
                "'><i class='fa fa-globe' style='color:mediumvioletred;'></i></a>&nbsp;";
            }
            return remoteUrl + data;
          },
          targets: -2
        },
        {
          render: function (data, type, row) {
            var btn =
              '<div class="btn-group">' +
              '<a class="btn-white  btn btn-xs prod" href="' + $ctx + '/oc/channel/update/'
              + data + '"> 维护</a>'
              <shiro:hasAnyRoles name="admin,financial">
              +
              '<button class="btn-white  btn btn-xs btn-pay" data-cid="' + data + '"> 付款</button>'
              </shiro:hasAnyRoles>
              +
              '</div>';
            return btn;
          },
          targets: -1
        }
      ]
    });

    function selectedIds() {
      var ids = [];
      $("input[name=ids]:checked").each(function () {
        ids.push($(this).val());
      });
      return ids;
    }

    var updateChannel = function (isEnable) {
      var ids = selectedIds();
      if (ids.length == 0) {
        alert("没有选择要操作的通道!");
        return;
      }
      var action = isEnable ? "active" : "deactive";
      if (confirm("确定要变更这些通道吗?")) {
        $.ajax({
          url: $ctx + "/oc/channel/" + action,
          traditional: true,
          type: 'post',
          data: {"ids": ids, "permanent": true},
          success: function (data) {
            dt.draw(false);
          }
        });
      }
    };

    var removeChannels = function () {
      var ids = selectedIds();
      if (ids.length == 0) {
        alert("没有选择要删除的通道!");
        return;
      }
      if (confirm("确定要删除这些通道吗? 通道删除后参数将丢失. 同时, 如果发生过交易的通道, 在交易未被归档前, 删除不会成功!")) {
        $.ajax({
          url: $ctx + "/oc/channel/batch-del",
          traditional: true,
          type: 'post',
          data: {"ids": ids},
          success: function (data) {
            dt.draw(false);
            if (data.status == 'success') {
              toastr.success(data.message, "提示");
            } else {
              toastr.warning(data.message, "提示");
            }
          }
        });
      }
    };

    var transfer2Channel = function (channelId) {
      var elem = document.getElementById('dlg');

      var d = dialog({
        "content": elem,
        "title": "付款/向上游打款",
        "zIndex": 2000,
        "onshow": function () {
          $(elem).load($ctx + '/oc/transfer2channel/' + channelId);
        },
        "okValue": '确认打款',
        "ok": function () {
          var amount = $("#amount").val();
          var remark = $("#remark").val();
          if (amount == '' || remark == '') {
            alert("请输入金额和摘要信息");
            return false;
          }
          $.ajax({
            url: $ctx + "/oc/transfer2channel",
            type: 'post',
            data: {"channelId": channelId, "amount": amount, "remark": remark},
            success: function (data) {
              dt.draw(false);
              if (data.status == 'success') {
                toastr.success(data.message, "提示");
              }
            }
          });
          return false;
        },
        "cancelValue": '关闭',
        "cancel": function () {
        }
      });
      d.showModal();
    };

    $("#btn_active").on('click', function () {
      updateChannel(true);
    });

    $("#btn_deactive").on('click', function () {
      updateChannel(false);
    });

    $("#btn_remove").on('click', function () {
      removeChannels();
    });

    $(document).on('click', '.btn-pay', function (e) {
      transfer2Channel($(this).data("cid"))
    });

    $("#search").on('click', function () {
      dt.draw();
    });
    $("#reset").on('click', function () {
      $("input[id^='filter_']").val("");
      $("#filter_EQ_status option:first").prop("selected", 'selected');
      dt.draw();
    });
  });
</script>
</body>
</html>
