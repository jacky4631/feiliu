$.ajaxSetup({
  cache: false
});
toastr.options = {
  closeButton: true,
  "positionClass": "toast-top-center",
  timeOut: 2200
};

var setCredit = function (uid) {
  var elem = document.getElementById('dlg');

  var d = dialog({
    "content": elem,
    "title": "设置授信",
    "zIndex": 2000,
    "onshow": function () {
      $(elem).load($ctx + '/oc/credit/update/' + uid);
    },
    "statusbar": '<label id="msg"></label>',
    "okValue": '保存',
    "ok": function () {
      var uid = $('#uid').val();
      var credit = $('#credit').val();
      $.ajax({
        url: $ctx + "/oc/credit/update",
        type: 'post',
        data: {"uid": uid, "credit": credit},
        success: function (data) {
          dt.draw(false);
          $('#msg').html(data.message);
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
var setBalance = function (uid) {
  var elem = document.getElementById('dlg');

  var d = dialog({
    "content": elem,
    "title": "加款/客户资金入账",
    "zIndex": 2000,
    "onshow": function () {
      $(elem).load($ctx + '/oc/transfer/' + uid);
    },
    "statusbar": '<label id="msg"></label>',
    "okValue": '确认入账',
    "ok": function () {
      var username = $('#username').val();
      var amount = $('#amount').val();
      var accountingSubject = $('#accountingSubject').val();
      var remark = $('#remark').val();
      if (username == "" || amount == "" || remark == "") {
        $('#msg').html("请正确完整地填写所有信息");
        return false;
      }
      $.ajax({
        url: $ctx + "/oc/transfer",
        type: 'post',
        data: {"username": username, "amount": amount, "accountingSubject": accountingSubject, "remark": remark},
        success: function (data) {
          dt.draw(false);
          $('#msg').html(data.message);
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

var dt;
$(function () {
  function selectedIds() {
    var ids = [];
    $("input[name=ids]:checked").each(function () {
      ids.push($(this).val());
    });
    return ids;
  }

  var removeUsers = function () {
    var ids = selectedIds();
    if (ids.length == 0) {
      toastr.warning("没有选择要删除的用户!", "提示");
      return;
    }
    if (confirm("确定要删除这些用户吗? 注意, 不能删除已经存在交易的用户, 如有大于或小于0的余额, 有交易记录等.")) {
      $.ajax({
        url: $ctx + "/oc/users/batch-del",
        traditional: true,
        type: 'post',
        data: {"ids": ids},
        success: function (data) {
          if (data.status == 'success') {
            toastr.success(data.message, "提示");
          } else {
            toastr.warning(data.message, "提示");
          }
          dt.draw(false);
        }
      });
    }
  };

  var enableUsers = function (isEnable) {
    var opr = "disable";
    var msg = "禁用";
    if (isEnable) {
      opr = "enable";
      msg = "启用";
    }

    var ids = selectedIds();
    if (ids.length == 0) {
      toastr.warning("没有选择要" + msg + "的用户!", "提示");
      return;
    }
    if (confirm("确定要" + msg + "这些用户吗?")) {
      $.ajax({
        url: $ctx + "/oc/users/enable",
        traditional: true,
        type: 'post',
        data: {"ids": ids, "opr": opr},
        success: function (data) {
          dt.draw(false);
        }
      });
    }
  };

  $("#search").on('click', function () {
    dt.draw();
  });
  $("#reset").on('click', function () {
    $("input[id^='filter_']").val("");
    dt.draw();
  });

  $("#btn_disable").on('click', function () {
    enableUsers(false);
  });
  $("#btn_enable").on('click', function () {
    enableUsers(true);
  });
  $("#btn_remove").on('click', function () {
    removeUsers();
  });

  $("#btn_passwd").on('click', function () {
    var ids = selectedIds();
    if (ids.length != 1) {
      toastr.warning("请选择一个需要重设密码的用户", "提示");
      return;
    }
    var d = dialog({
      title: '请输入新密码',
      content: '<input name="pass" id="pass"/><span id="dlg_msg"></span>',
      width: 280,
      ok: function () {
        var newpass = $("#pass").val();
        if (newpass == '') {
          $('#dlg_msg').html("密码不能为空");
          return false;
        }

        $.ajax({
          url: $ctx + "/oc/users/passwd",
          traditional: true,
          type: 'post',
          data: {"id": ids[0], "pass": newpass},
          success: function (data) {
            $('#dlg_msg').html(data.message);
          }
        });
        return false;
      }
    });
    d.showModal();
  });
})
;