$(function () {
  toastr.options = {
    closeButton: true,
    "positionClass": "toast-top-center",
    timeOut: 3000
  };
  activeMenu("ma-translog");
  $('#filter_GED_operateTime, #filter_LED_operateTime').datepicker({
    todayBtn: "linked",
    keyboardNavigation: false,
    forceParse: false,
    autoclose: true,
    language: 'zh-CN',
    format: "yyyy-mm-dd"
  });
  function convert(subject) {
    var ret = "未设置";
    if (subject == 1) {
      ret = "收入";
    } else if (subject == 9) {
      ret = "支出";
    } else if (subject == 102) {
      ret = "内部转账";
    } else if (subject == 103) {
      ret = "平账";
    } else if (subject == 104) {
      ret = "罚款收入";
    } else if (subject == 105) {
      ret = "罚款支出";
    } else if (subject == 106) {
      ret = "投诉退款";
    } else if (subject == 107) {
      ret = "短信扣费";
    }
    return ret;
  }

  $.fn.dataTable.ext.legacy.ajax = true;
  var dt = $('#dt1').DataTable({
    "autoWidth": false,
    "processing": true,
    "serverSide": true,
    "responsive": false,
    "ajax": {
      "url": $ctx + "/oc/transferlog/data",
      "method": "post",
      "data": function (d) {
        return $.extend({}, d, {
          "filter_EQ_username": $("#filter_EQ_username").val(),
          "filter_EQ_operator": $("#filter_EQ_operator").val(),
          "filter_GED_operateTime": $("#filter_GED_operateTime").val(),
          "filter_LED_operateTime": $("#filter_LED_operateTime").val(),
          "filter_EQ_accountingSubject": $("#filter_EQ_accountingSubject").val()
        });
      }
    },
    "columns": [
      {"data": "id", sortable: false},
      {"data": "type", sortable: false},
      {"data": "username", sortable: false},
      {"data": "amount", sortable: false},
      {"data": "beforeAmount", sortable: false},
      {"data": "balance", sortable: false},
      {"data": "operator", sortable: false},
      {"data": "operateTime"},
      {"data": "remark", sortable: false},
      {"data": "accountingSubject", sortable: false}
    ],
    "order": [7, "desc"],
    "columnDefs": [
      {
        "render": function (data, type, row) {
          return "<input name='ids' type='checkbox' value='" + data + "'>";
        },
        "targets": 0
      },
      {
        "render": function (data, type, row) {
          if (data == 0) {
            return "<small class='label label-primary'><i class='fa fa-arrow-down'></i></small>";
          } else {
            return "<small class='label label-warning'><i class='fa fa-arrow-up'></i></small>";
          }
        },
        "targets": 1
      },
      {
        "render": function (data, type, row) {
          if (row.displayName == null) {
            return data;
          } else {
            return row.displayName + "(" + data + ")";
          }
        },
        "targets": 2
      },
      {
        "render": function (data, type, row) {
          return "<b>" + data + "<b>";
        },
        "targets": 3
      },
      {
        "render": function (data, type, row) {
          var v = convert(data);
          var fillColor = (data > 100 || data == 0 ) ? "btn-outline" : "";
          return "<div class='btn-group'>" +
            "<button class='btn btn-success btn-facebook " + fillColor + " btn-xs dropdown-toggle' data-toggle='dropdown'>" +
            v + "<span class='caret'></span></button>" +
            "<ul data-tid='" + row.id + "'" + "class='dropdown-menu subject'>" +
            "<li><a href='#1'>收入（统计项）</a></li>" +
            "<li><a href='#107'>短信扣费</a></li>" +
            "<li><a href='#104'>罚款收入</a></li>" +
            "<li class='divider'></li>" +
            "<li><a href='#9'>支出（统计项）</a></li>" +
            "<li><a href='#106'>投诉退款</a></li>" +
            "<li><a href='#105'>罚款支出</a></li>" +
            "<li class='divider'></li>" +
            "<li><a href='#102'>内部转账</a></li>" +
            "<li><a href='#103'>平账</a></li>" +
            "</ul></div>";
        },
        "targets": -1
      }
    ],
    "filter": false,
    "language": {
      "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
    }
  });

  $(document).on('click', '.subject a', function (e) {
    e.preventDefault();
    var tid = $(this).parents("ul").data("tid");
    var subject = $(this).attr("href").substring(1);
    var url = $ctx + "/oc/transferlog/chgsubject";
    $.ajax({
      type: "post",
      traditional: true,
      url: url,
      data: {"tid": tid, "subject": subject},
      success: function (data) {
        if (data.status == "success") {
          dt.draw(false);
        } else {
          alert(data.message);
        }
      }
    });
  });

  function selectedIds() {
    var ids = [];
    $("input[name=ids]:checked").each(function () {
      ids.push($(this).val());
    });
    return ids;
  }

  $("#merge").on('click', function () {
    var ids = selectedIds();
    if (ids.length == 0) {
      toastr.warning("没有选择要核销的财务流水!", "提示");
      return;
    }
    if (confirm("确定要进行核销吗? 注意, 核销一般用于误操作平账后无效财务流水的清理! \n核销条件: 流水记录属于同一个用户或通道, 且总金额合计为0.")) {
      $.ajax({
        url: $ctx + "/oc/transferlog/checkoff",
        traditional: true,
        type: 'post',
        data: {"ids": ids},
        success: function (data) {
          if (data.status == 'success') {
            toastr.success(data.message, "提示");
          } else {
            toastr.error(data.message, "警告");
          }
          dt.draw(false);
        }
      });
    }
  });

  $("#search").on('click', function () {
    dt.draw();
  });
  $("#reset").on('click', function () {
    $("input[id^='filter_']").val("");
    $("#filter_EQ_accountingSubject").find("option:first").prop("selected", 'selected');
    dt.draw();
  });
});