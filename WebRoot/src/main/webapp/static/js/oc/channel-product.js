$(function () {
  var channelprods = $("#channelprods").val();
  $.ajax({
    type: "post",
    traditional: true,
    url: $ctx + "/oc/channel/product/tags",
    data: {
      "allowChannelProducts": channelprods
    },
    success: function (data) {
      $("#disChannel").append(data);
    }
  });

  $("#btn-modi-channels").on('click', function (e) {
    e.preventDefault();
    if ($('#channel_product_dlg').length == 0) {
      $("body").append("<div id='channel_product_dlg' style='height:400px;width:800px;display:none;'></div>");
    }
    var url = $ctx + "/oc/channel/product/dlg";
    var elem = document.getElementById('channel_product_dlg');
    var d = dialog({
      id: 'dlg-modi-channels',
      "content": elem,
      "title": "通道组定义(双击通道菜单增删组)",
      "zIndex": 2000,
      "padding": 15,
      "onshow": function () {
        $(elem).load(url);
      },
      "okValue": '确定',
      "ok": function () {
        return true;
      }
    });
    d.show();
  });

  $(document).on('click', '.list-channel', function (e) {
    e.preventDefault();
    $(this).parent("ul").children(".active").removeClass("active");
    $(this).addClass("active");
    var channelId = $(this).data("cid");
    $("#content").load($ctx + "/oc/channel/product/detail/" + channelId);
  });

  function actualAdd(code) {
    var value = $("#channelprods").val();
    var codes = [];
    if (value != '') {
      codes = value.split(",");
    }

    if ($.inArray(code, codes) == -1) {
      codes.push(code);
      var channelprods = codes.join();
      $("#channelprods").val(channelprods);
    }
  }

  function actualDel(code) {
    var value = $("#channelprods").val();
    var codes = value.split(",");
    codes = $.grep(codes, function (n, i) {
      return codes[i] != code;
    });
    var channelprods = codes.join();
    $("#channelprods").val(channelprods);
  }

  function delChannel(id) {
    var selector = "#" + id;
    if (selector.indexOf("$") > -1) {
      selector = selector.replace("$", "\\$");
    }
    actualDel(id);
    $(selector).remove();
  }

  function addChannel(id, name, autodel) {
    var elementSel = "#" + id;
    elementSel = elementSel.replace("$", "\\$");
    if ($(elementSel).length > 0) {
      if (autodel) {
        delChannel(id);
        $("#citem-" + id).find(".suc").remove();
        toastr.success('已将' + name + '移出通道组', '提示');
      } else {
        toastr.error(name + "已经加入, 不需要重复添加", '提示');
      }
      return;
    }
    var html = "<span class='tag' id='" + id +
      "'><span>" + name + "</span> <a href='#' class='channel-del' title='删除'>x</a></span>";
    $("#disChannel").append(html);
    actualAdd(id);

    //notify operator
    if (id.indexOf("ALL") > -1) {
      $("#citem-" + id).find("p").append("<span class='suc label pull-right label-info'>已加</span>");
    }
    toastr.success('已将' + name + '加入通道组, 请全部加完后按保存生效', '提示');
  }

  $(document).on('dblclick', '.list-channel', function (e) {
    e.preventDefault();
    var id = $(this).data("cid");
    var name = $(this).data("name");
    addChannel(id + "-ALL", name, true);
    return false;
  });

  $(document).on('click', '.btn-addall', function (e) {
    var id = $("#curChannelId").val();
    var name = $("#curChannelName").val();
    addChannel(id + "-ALL", name);
  });

  $(document).on('click', '.btn-add', function (e) {
    var channelId = $("#curChannelId").val();
    var channelName = $("#curChannelName").val();
    var name = $(this).data("name");
    var groupCode = $(this).data("code");

    addChannel(channelId + "-" + groupCode, name + "@" + channelName);
  });

  $(document).on('click', '.channel-del', function (e) {
    var id = $(this).parent().attr("id");
    delChannel(id);
    return false;
  });

});