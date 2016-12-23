var convertRestult = function (data, row) {
  if (row.channelId == null && data != 0 && data != -1) {
    return "<small class='label label-info'>已收单</small>";
  }
  var dis;
  var color;
  if (data == 0) {
    if (row.channelResult != 0) {
      color = "label-success";
    } else {
      color = "label-primary";
    }
    dis = "<small class='label " + color + "'>成功</small>"
  } else if (data == -1) {
    var msg = row.message == null ? row.channelMessage : row.message;
    dis = "<small class='label label-danger' data-toggle='tooltip' data-placement='bottom' title='" +
      msg + "'>失败</small>";
  } else {
    dis = "<small class='label label-warning'>充值中</small>"
  }
  return dis;
};

var getIds = function (disableAlert) {
  var ids = [];
  $("input[name=ids]:checked").each(function () {
    ids.push($(this).val());
  });
  if (ids.length == 0) {
    if (!disableAlert) {
      var d = dialog({
        title: '提示',
        content: '没有选择需要处理的订单'
      });
      d.show();
    }
    return null;
  } else {
    return ids;
  }
};

var dt;
$(function () {
  toastr.options = {
    closeButton: true,
    progressBar: true,
    "positionClass": "toast-top-center",
    timeOut: 4000
  };

  $(".chosen").chosen();
  $.fn.dataTable.ext.legacy.ajax = true;

  var showDetail = function (tradeId) {
    var elem = document.getElementById('dlg');
    var d = dialog({
      "id": 'dlg_detail',
      "zIndex": 2000,
      "content": elem,
      "padding": 10,
      "title": "订单详情 -- " + tradeId,
      "onshow": function () {
        $(elem).load($ctx + '/oc/tradelog/detail/' + tradeId);
      }
    });
    d.show();
  };

  $('#dt1').on('draw.dt', function () {
    $("#checkall").removeAttr("checked");
  });

  $("#search").on('click', function () {
    dt.draw();
  });

  $("#reset").on('click', function () {
    $("input[id^='filter_']").val("");
    $("#filter_EQ_result option:first").prop("selected", 'selected');
    $("#filter_EQ_provider option:first").prop("selected", 'selected');
    $("#filter_EQ_state option:first").prop("selected", 'selected');
    $("#filter_EQ_channel option:first").prop("selected", 'selected');
    dt.draw();
  });

  $(document).on('click', '.detail', function (e) {
    e.preventDefault();
    showDetail($(this).data("id"));
  });

  $(document).on('click', '.save-remark', function (e) {
    var id = $("#detail_id").val();
    var remark = $("#detail_remark").val();

    $.ajax({
      url: $ctx + "/oc/tradelog/save-remark",
      type: 'post',
      data: {
        "tradeId": id,
        "remark": remark
      },
      success: function (data) {
        toastr.success(data.message, "提示");
        dt.draw(false);
      }
    });
  });

  $("#export").on('click', function () {
    var frm = $("#frm_filter");
    frm.attr("action", $ctx + "/oc/tradelog/export");
    frm.submit();
  });

  $("#export2").on('click', function () {
    var frm = $("#frm_filter");
    frm.attr("action", $ctx + "/oc/tradelog/export2");
    frm.submit();
  });

  $('#checkall').click(function () {
    var items = document.getElementsByName('ids');
    for (var i = 0; i < items.length; i++) {
      items[i].checked = this.checked;
    }
  });

  $('#filter_GED_startDate, #filter_LED_startDate').datepicker({
    todayBtn: "linked",
    keyboardNavigation: false,
    forceParse: false,
    autoclose: true,
    language: 'zh-CN',
    format: "yyyy-mm-dd"
  });

  $("#btn_force_suc, #btn_force_fail").on("click", function (e) {
    var success = true;
    if (e.target.id == 'btn_force_fail') {
      success = false;
    }

    e.preventDefault();
    var ids = getIds();
    if (ids == null) {
      return;
    }

    var url = $ctx + "/oc/trade/callback";
    var title = success ? "警告: 将要回调为成功" : "警告: 将要回调为失败";
    var d = dialog({
      title: title,
      content: '您确定要手工回调吗? 这会导致拒收上游的回调报告, 并有可能产生对账偏差！<br>同时, 下游厂家可能会出现投诉。' +
      "<span class='label label-warning'>上游已经返回状态的订单不会重复处理</span><br><br>" +
      "<b>处理效果:</b><ul><li>回调为失败时, 系统将会全额退款</li><li>回调为成功时, 将视为实际成功, 交易正常收费</li></ul>" +
      "用户方系统将在该操作结束后, 收到此批订单的报告回调(或查询结果)。",
      okValue: '确定',
      ok: function () {
        this.title('提交中…');
        $.ajax({
          type: "post",
          traditional: true,
          url: url,
          data: {"ids": ids, "success": success},
          success: function (data) {
            if (data.status == "success") {
              dt.draw(false);
            } else {
              alert(data.message);
            }
          }
        });
      },
      cancelValue: '取消',
      cancel: function () {
      }
    });
    d.show();
  });

  $("#btn_ok").on("click", function (e) {
    e.preventDefault();
    var ids = getIds();
    if (ids == null) {
      return;
    }
    var url = $ctx + "/oc/trade/ok";
    var d = dialog({
      title: '警告',
      content: '注意: 你执行这些操作后, 这些订单将会强制认为上游已成功回调(当前时间), 走完后续流程并回调下游用户. <br>' +
      '该操作一般用于提交环节网络超时的订单, 此时订单未获得上游流水号, 上游是否收单不明确.(执行前请和上游客服联系确认)',
      okValue: '确定',
      ok: function () {
        $.ajax({
          type: "post",
          traditional: true,
          url: url,
          data: {"ids": ids},
          success: function (data) {
            if (data.status == "success") {
              dt.draw(false);
            } else {
              alert(data.message);
            }
          }
        });
      },
      cancelValue: '取消',
      cancel: function () {
      }
    });
    d.show();
  });

  $("#btn_route").on("click", function (e) {
    var ids = getIds();
    if (ids == null) {
      return;
    }
    var elem = document.getElementById('dlg-sel');
    var url = $ctx + "/oc/trade/selchannel";
    var d = dialog({
      "content": elem,
      "title": "选择通道",
      "zIndex": 2000,
      "onshow": function () {
        $(elem).load(url);
      },
      "okValue": '确定',
      "ok": function () {
        var channelId = $('#selchannel').val();
        if (channelId == '') {
          alert("请选择供应商通道");
          return false;
        }
        var naFirst = $("input[name='naFirst']:checked").val();
        var priceProtected = $('#priceProtected').is(':checked');
        $.ajax({
          url: $ctx + "/oc/trade/reroute",
          type: 'post',
          traditional: true,
          data: {
            "ids": ids,
            "channelId": channelId,
            "naFirst": naFirst,
            "priceProtected": priceProtected
          },
          success: function (data) {
            if (data.status == "success") {
              toastr.success(data.message, "重选成功");
              dt.draw(false);
            } else {
              toastr.error(data.message, "重选失败");
              return false;
            }
          }
        });
      },
      "cancelValue": '关闭',
      "cancel": function () {
      }
    });
    d.showModal();
  });

  $("#btn_retry").on("click", function (e) {
    var ids = getIds();
    if (ids == null) {
      return;
    }
    if (confirm("这将在原通道再次执行这些交易, 是否继续?")) {
      $.ajax({
        url: $ctx + "/oc/trade/retry",
        type: 'post',
        traditional: true,
        data: {"ids": ids},
        success: function (data) {
          if (data.status == "success") {
            toastr.success(data.message, "重新执行提交成功");
            dt.draw(false);
          } else {
            toastr.error(data.message, "重新执行提交失败");
          }
        }
      });
    }
  });

  function autoByCondition() {
    var elem = document.getElementById('dlg-sel');
    var url = $ctx + "/oc/trade/autosel-dlg";
    var d = dialog({
      "content": elem,
      "title": "选择将执行的订单",
      "zIndex": 2000,
      "onshow": function () {
        $(elem).load(url);
      },
      "okValue": '确定',
      "ok": function () {
        var stateCode = $('#sel_state').val();
        if (stateCode == 'NA') {
          alert("选择全国范围太大, 请选择具体的省份");
          return false;
        }
        var provider = $("#sel_provider").val();
        $.ajax({
          url: $ctx + "/oc/trade/auto-by-condition",
          type: 'post',
          traditional: true,
          data: {
            "provider": provider,
            "stateCode": stateCode
          },
          success: function (data) {
            if (data.status == "success") {
              toastr.success(data.message, "提交成功");
              dt.draw(false);
            } else {
              toastr.error(data.message, "提交失败");
              return false;
            }
          }
        });
      },
      "cancelValue": '取消',
      "cancel": function () {
      }
    });
    d.showModal();
  }

  $("#btn_auto").on("click", function (e) {
    var ids = getIds(true);
    if (ids == null) {
      autoByCondition();
      return;
    }
    if (confirm("这将在开始自动执行这些交易, 是否继续?")) {
      $.ajax({
        url: $ctx + "/oc/trade/auto",
        type: 'post',
        traditional: true,
        data: {"ids": ids},
        success: function (data) {
          if (data.status == "success") {
            toastr.success(data.message, "提交成功");
            dt.draw(false);
          } else {
            toastr.error(data.message, "提交失败");
          }
        }
      });
    }
  });

  $("#btn_fail").on("click", function (e) {
    var ids = getIds();
    if (ids == null) {
      return;
    }

    if (!confirm("这将退款并产生回调, 交易不可再处理, 是否继续?")) {
      return;
    }
    $.ajax({
      url: $ctx + "/oc/trade/fail-pass",
      type: 'post',
      traditional: true,
      data: {"ids": ids},
      success: function (data) {
        if (data.status == "success") {
          toastr.success(data.message, "执行成功");
          dt.draw(false);
        } else {
          toastr.error(data.message, "执行失败");
        }
      }
    });
  });

  $("#btn_report2").on("click", function (e) {
    e.preventDefault();
    var ids = getIds();
    if (ids == null) {
      return;
    }
    if (confirm("您确定需要重新推送这些订单的报告给用户吗? 注意, 未设置推送功能的用户会被忽略. 未完成的订单也不作处理.")) {
      var url = $ctx + "/oc/trade/report-2";
      $.ajax({
        type: "post",
        traditional: true,
        url: url,
        data: {"ids": ids},
        success: function (data) {
          if (data.status == "success") {
            toastr.success(data.message, "执行成功");
            dt.draw(false);
          } else {
            toastr.error(data.message, "执行失败");
          }
        }
      });
    }
  });

  $(document).on('click', '.taga', function (e) {
    e.preventDefault();
    $("#new_message").val(this.innerText);
  });

  $("#btn_msg").on("click", function (e) {
    e.preventDefault();
    var ids = getIds();
    if (ids == null) {
      return;
    }

    var elem = document.getElementById('dlg-sel');
    var url = $ctx + "/oc/tradelog/message-dlg";
    var d = dialog({
      "content": elem,
      "title": "修订上游返回信息(同步到下游)",
      "zIndex": 2000,
      "onshow": function () {
        $(elem).load(url);
      },
      "okValue": '确定',
      "ok": function () {
        var message = $("#new_message").val();
        $.ajax({
          url: $ctx + "/oc/tradelog/message",
          type: 'post',
          traditional: true,
          data: {
            "ids": ids,
            "message": message
          },
          success: function (data) {
            if (data.status == "success") {
              toastr.success(data.message, "修改成功");
              dt.draw(false);
            } else {
              toastr.error(data.message, "修改失败");
              return false;
            }
          }
        });
      },
      "cancelValue": '取消',
      "cancel": function () {
      }
    });
    d.showModal();
  });
});