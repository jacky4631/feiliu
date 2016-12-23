<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/chosen/chosen.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/artDialog/ui-dialog.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/codemirror/codemirror.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/chosen/chosen.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
  <style>
    td label {
      margin-top: 5px;
    }

    td label input[type="checkbox"] {
      margin-right: 4px;
    }

    .chosen-container {
      width: 245px !important;
    }

    #tab-advance .chosen-container {
      width: 450px !important;
    }

    .CodeMirror {
      border: 1px solid #eee;
      background: white none repeat scroll 0 0;
      color: black;
      overflow: hidden;
      position: relative;
      font-family: monospace;
      line-height: 180%;
      height: 300px;
    }

    form .chosen-container {
      width: 100% !important;
      min-width: 300px !important;
    }

    .form-profile {
      border: 1px solid gainsboro;
      border-radius: 3px;
      padding: 4px;
      background-color: #f5f5f6;
    }

    .form-profile * {
      -moz-user-select: none;
      -webkit-user-select: none;
      -ms-user-select: none;
      user-select: none;
    }

    .form-profile div.checkbox {
      margin-left: 10px;
    }

    .legend {
      margin-right: 20px !important;
      padding-top: 6px !important;
      color: gray;
      font-size: 12px;
    }

    .legend span {
      text-indent: .5em;
    }
  </style>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>

    <div class="wrapper wrapper-content animated fadeInRight">
      <c:if test="${not empty message}">
        <div id="message" class="alert alert-success">
          <button data-dismiss="alert" class="close">×</button>
            ${message}</div>
      </c:if>
      <div class="row">
        <div class="col-lg-12">
          <div class="tabs-container">
            <ul class="nav nav-tabs">
              <li class="active"><a href="#tab-basic" data-toggle="tab"><i class="fa fa-laptop"></i> 基本信息</a></li>
              <li class=""><a href="#tab-product" data-toggle="tab"><i class="fa fa-th-list"></i> 产品信息</a></li>
              <li class=""><a href="#tab-remote" data-toggle="tab"><i class="fa fa-globe"></i> 上游系统</a></li>
              <shiro:hasAnyRoles name="admin">
                <li class=""><a id="s-adv" href="#tab-advance" data-toggle="tab"><i class="fa fa-gears"></i> 高级(通道参数)</a></li>
              </shiro:hasAnyRoles>
            </ul>
            <div class="tab-content">
              <div class="tab-pane active" id="tab-basic">
                <%@ include file="/WEB-INF/views/oc/inc/channel-form-basic.jsp" %>
              </div>
              <div class="tab-pane" id="tab-product">
                <%@ include file="/WEB-INF/views/oc/inc/channel-form-product.jsp" %>
              </div>
              <div class="tab-pane" id="tab-remote">
                <%@ include file="/WEB-INF/views/oc/inc/channel-form-remote.jsp" %>
              </div>
              <shiro:hasAnyRoles name="admin">
                <div class="tab-pane" id="tab-advance">
                  <%@ include file="/WEB-INF/views/oc/inc/channel-form-adv.jsp" %>
                </div>
              </shiro:hasAnyRoles>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="dlgTest" style="height:160px;width:600px;display:none;"></div>
    <div id="dlgCrtGrp" style="height:335px;width:365px;display:none;"></div>
  </div>
</div>
<table id="template" style="display:none;">
  <tr id="row_template">
    <td>
      <input type="text" name="origiId" class="form-control input-sm">
      <input type="hidden" name="objId" value="">
      <input type="hidden" name="size" value="">
    </td>
    <td>
      <input type="text" readonly name="title" class="form-control input-sm">
    </td>
    <td>
      <div class="input-group">
        <input type="text" readonly name="price" class="form-control input-sm">
      </div>
    </td>
    <td>
      <input type="text" name="discount" value="0.6" onchange="calPrice(this);" class="form-control input-sm">
    </td>
    <td>
      <input type="text" name="priority" value="5" class="form-control input-sm">
    </td>
    <td>
      <div class="input-group">
        <input type="text" name="billAmount" readonly class="form-control input-sm">
      </div>
    </td>
    <td>
      <div class="input-group">
        <select data-placeholder="上架选择..." name="productId" class="chosen-select">
          <option value="">上架选择...</option>
          <c:forEach var="product" items="${products}">
            <option value="${product.id}" data-size="${product.size}" data-price="${product.price}" data-title="${product.shortName}">${product.id}-${product.name}</option>
          </c:forEach>
        </select>
      </div>
    </td>
    <td style="text-align: center;">
      <label>
        <input type="checkbox" checked="checked" name="enabled" value="true">
      </label>
    </td>
    <td>
      <div class="btn-group">
        <button class="btn btn-white save"><i class="fa fa-save"></i></button>
        <button class="btn btn-white remove"><i class="fa fa-trash"></i></button>
      </div>
    </td>
  </tr>
</table>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/form/jquery.form.js"></script>
<script src="${cdn}/js/plugins/form-validator/jquery.form-validator.min.js"></script>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script src="${cdn}/js/plugins/chosen/chosen.jquery.js"></script>
<script src="${cdn}/js/plugins/codemirror/codemirror.js"></script>
<script src="${cdn}/js/plugins/codemirror/mode/javascript/javascript.js"></script>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-min.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-plus-min.js"></script>
<script type="text/javascript">
  $.validate({
    lang: "zh"
  });

  function onChooseProduct(e) {
    var val = $(e.target).find("option:selected").val();
    if (val == "") {
      return;
    }

    var option = $(e.target).find("option:selected");
    var price = option.data("price");
    var size = option.data("size");
    var title = option.data("title");

    $(e.target).parents("tr").find("input[name='title']").val(title);
    $(e.target).parents("tr").find("input[name='price']").val(price);
    $(e.target).parents("tr").find("input[name='size']").val(size);
    calPrice(e.target);
  }

  function addRow() {
    var count = $("#tab-items").find("tr").size();
    var row = $("#row_template").clone(false);
    row[0].id = "row" + count + Math.floor(Math.random() * 100000);
    row.appendTo($("#tab-items").find("tbody"));

    $("#" + row[0].id).find(".chosen-select").chosen().change(function (e) {
      onChooseProduct(e);
    });
  }

  function delRow(obj) {
    $(obj).parent().parent().remove();
  }

  function calPrice(obj) {
    var price = $(obj).parents("tr").find("input[name='price']").val();
    var discount = $(obj).parents("tr").find("input[name='discount']").val();
    if (price != "" && discount != "")
      if (discount <= 1 && discount >= 0) {
        $(obj).parents("tr").find("input[name='billAmount']").val(parseFloat((discount * price).toFixed(2)));
      } else {
        toastr.warning("折扣不能小于0或大于100%, 您确信没有错误? 已经自动设置为0.99", "警告");
        $(obj).parents("tr").find("input[name='discount']").val(1.0);
        $(obj).parents("tr").find("input[name='billAmount']").val(parseFloat((1.0 * price).toFixed(2)));
      }
  }

  $(function () {
    $.ajaxSetup({cache: false});
    $("#main-nav").addClass("white-bg");

    toastr.options = {
      closeButton: true,
      progressBar: true,
      "positionClass": "toast-top-center",
      timeOut: 1500
    };
    var codeEditor = CodeMirror.fromTextArea(document.getElementById("code2"), {
      mode: "text/javascript",
      lineNumbers: true,
      matchBrackets: true,
      styleActiveLine: true,
      autoMatchParens: true,
      autofocus: true,
      lineWrapping: true
    });

    $("#s-adv").click(function () {
      setTimeout(function () {
        codeEditor.refresh();
      }, 10);
    });

    activeMenu("ma-channel");
    $("#message").delay(1000).hide(300);

    $(document).on('click', '.btn-add', function (e) {
      addRow();
    });

    $(document).on('click', '.save', function (e) {
      var obj = e.target;
      var objId = $(obj).parents("tr").find("input[name='objId']").val();
      var title = $(obj).parents("tr").find("input[name='title']").val();
      var price = $(obj).parents("tr").find("input[name='price']").val();
      var size = $(obj).parents("tr").find("input[name='size']").val();
      var origiId = $(obj).parents("tr").find("input[name='origiId']").val();
      var discount = $(obj).parents("tr").find("input[name='discount']").val();
      var priority = $(obj).parents("tr").find("input[name='priority']").val();
      var productId = $(obj).parents("tr").find("option:selected").val();
      var roamable = $('#roamable').is(':checked');
      if (!roamable) {
        productId = productId + "$";
      }
      var enabled = $(e.target).parents("tr").find("input[name='enabled']").is(":checked");
      var channelId = $("input[name='id']").val();

      if (channelId == "") {
        toastr.warning("供货商应该先保存", "提示");
        return;
      }

      if (productId == "") {
        toastr.warning("没有选择归属的上架基础产品", "提示");
        return;
      }
      if (origiId == "" || discount == "" || priority == "") {
        toastr.warning("供方产品编码,折扣,产品优先级必填", "提示");
        return;
      }
      $.ajax({
        type: "post",
        traditional: true,
        url: "${ctx}/oc/flowpackage/save",
        data: {
          "id": objId, "title": title, "price": price, "origiProductId": origiId,
          "productId": productId, "flowChannelId": channelId, "size": size,
          "discount": discount, "priority": priority, "enabled": enabled
        },
        success: function (data) {
          $(obj).parents("tr").find("input[name='objId']").val(data.id);
          toastr.success("产品: " + data.title + "已保存", "保存成功");
        }
      });
    });

    $(".plugin_select").chosen().change(function (e) {
      var className = $(e.target).find("option:selected").val();
      if (className == "") {
        codeEditor.setValue("");
        return;
      }

      $.ajax({
        type: "post",
        url: "${ctx}/oc/channel/flow-params",
        data: {
          "channelClassname": className
        },
        success: function (data) {
          codeEditor.setValue(data);
        }
      });
    });

    $("#adv-save").on('click', function (e) {
      codeEditor.save();
      var channelId = $("#id").val();
      var handlerClass = $("#handlerClass").val();
      var params = $("#code2").val();
      if (channelId == "") {
        toastr.error("请先保存通道基本信息", "无法保存");
        return;
      }
      if (handlerClass == "" || params == "") {
        toastr.error("请选择通道使用的接口协议,并设置好通道提供方提供的各参数", "无法保存");
        return;
      }
      $.ajax({
        type: "post",
        traditional: true,
        url: "${ctx}/oc/channel/update-params",
        data: {
          "channelId": channelId,
          "handlerClass": handlerClass,
          "params": params
        },
        success: function (data) {
          if (data.status == 'success')
            toastr.success("通道参数保存成功", "保存成功");
          else
            toastr.error("通道参数保存失败, " + data.message, "保存失败");
        }
      });
    });

    $("#adv-test").on('click', function (e) {
      var elem = document.getElementById('dlgTest');
      var channelId = $("#id").val();
      if (channelId == '') {
        toastr.error("请先保存通道基本信息", "无法测试");
        return;
      }
      var d = dialog({
          "id": 'dialog-test',
          "content": elem,
          "title": "通道测试",
          "zIndex": 2000,
          "onshow": function () {
            $(elem).load($ctx + '/oc/channel/test/' + channelId);
          },
          button: [
            {
              value: '发送订购',
              callback: function () {
                var flowPackageId = $("input[name='products']:checked").val();
                var testMobile = $("#testMobile").val();
                if (testMobile == '' || typeof(flowPackageId) == "undefined") {
                  toastr.error("请输入手机号码并选择送出的包", "输入不全");
                  return false;
                }
                $.ajax({
                  type: "post",
                  url: "${ctx}/oc/channel/test",
                  data: {
                    "channelId": channelId,
                    "mobile": testMobile,
                    "flowPackageId": flowPackageId
                  },
                  success: function (data) {
                    if (data.status == "success") {
                      toastr.success("测试提交成功, 您可以到交易监控列表中看刚送出的测试执行状态", "测试发出");
                    } else {
                      toastr.error(data.message, "测试错误");
                    }
                  }
                });
                return false;
              },
              autofocus: true
            },
            {
              value: '关闭'
            }
          ]
        })
        ;
      d.show();
    })
    ;

    $(document).on('click', '.remove', function (e) {
      var objId = $(e.target).parents("tr").find("input[name='objId']").val();
      if (objId == "") {
        $(e.target).parents("tr").remove();
        return;
      }
      if (!confirm("您确认要删除该产品吗?")) {
        return;
      }
      $.ajax({
        type: "post",
        url: "${ctx}/oc/flowpackage/remove",
        data: {
          "id": objId
        },
        success: function (data) {
          if (data.status == 'success') {
            $(e.target).parents("tr").remove();
          }
        }
      });
    });

    function loading() {
      $('#tab-prd').html("<div style='text-align:center;'><img src='${ctx}/static/img/loading.gif'/></div>");
    }

    $(document).on('click', '.btn-setting', function (e) {
      e.preventDefault();
      var channelId = $('#id').val();
      var groupCode = $(this).data("group");
      $.ajax({
        url: "${ctx}/oc/channel/product/" + channelId + "/" + groupCode,
        beforeSend: loading,
        method: "get",
        success: function (data) {
          $("#tab-prd").html(data);
        }
      });
    });

    $(document).on('click', '.btn-setprice', function (e) {
      e.preventDefault();
      var channelId = $('#id').val();
      var groupCode = $(this).data("group");
      var d = dialog({
        title: '统一设置折扣',
        content: '<input name="price_set" value="0.9" id="price_set"/><span id="dlg_msg"></span>',
        width: 280,
        ok: function () {
          var discount = $("#price_set").val();
          if (discount == '' || discount > 1.0 || discount < 0) {
            $('#dlg_msg').html("折扣设置不合法");
            return false;
          }

          $.ajax({
            url: $ctx + "/oc/flowpackage/grp-discount",
            type: 'post',
            data: {"channelId": channelId, "groupCode": groupCode, "discount": discount},
            success: function (data) {
              if (data.status == 'success') {
                $("#tab-prd").load("${ctx}/oc/channel/product/" + channelId + "/" + groupCode);
                toastr.success(data.message, "设置成功");
              } else {
                toastr.error("新的折扣设置失败", "设置失败");
              }
            }
          });
        }
      });
      d.showModal();
    });
    $(document).on('click', '.btn-setp', function (e) {
      e.preventDefault();
      var channelId = $('#id').val();
      var groupCode = $(this).data("group");
      var d = dialog({
        title: '统一设置优先级',
        content: '<input name="p_set" value="5" id="p_set"/><span id="dlg_msg"></span>',
        width: 280,
        ok: function () {
          var prior = $("#p_set").val();
          if (prior == '') {
            $('#dlg_msg').html("优先级不能为空");
            return false;
          }

          $.ajax({
            url: $ctx + "/oc/flowpackage/grp-prior",
            type: 'post',
            data: {"channelId": channelId, "groupCode": groupCode, "prior": prior},
            success: function (data) {
              if (data.status == 'success') {
                $("#tab-prd").load("${ctx}/oc/channel/product/" + channelId + "/" + groupCode);
                toastr.success("新优先级设置成功", "设置成功");
              } else {
                toastr.success("新优先级设置失败", "设置失败");
              }
            }
          });
        }
      });
      d.showModal();
    });

    $(document).on('click', '.btn-group-del', function (e) {
      e.preventDefault();
      if (!confirm("这将删除该地区全部产品包, 是否继续?")) {
        return;
      }
      var channelId = $('#id').val();
      var groupCode = $(this).data("group");
      $.ajax({
        url: $ctx + "/oc/flowpackage/grp-del",
        type: 'post',
        data: {"channelId": channelId, "groupCode": groupCode},
        success: function (data) {
          location.reload();
        }
      });
    });

    $(document).on('click', '.btn-group-enable', function (e) {
      e.preventDefault();
      var channelId = $('#id').val();
      var groupCode = $(this).data("group");
      $.ajax({
        url: $ctx + "/oc/flowpackage/grp-enable",
        type: 'post',
        data: {"channelId": channelId, "groupCode": groupCode},
        success: function (data) {
          $("#tab-prd").load("${ctx}/oc/channel/product/" + channelId + "/" + groupCode);
          toastr.success("全部启用成功", "设置成功");
        }
      });
    });

    $(document).on('click', '.btn-group-disable', function (e) {
      e.preventDefault();
      var channelId = $('#id').val();
      var groupCode = $(this).data("group");
      $.ajax({
        url: $ctx + "/oc/flowpackage/grp-disable",
        type: 'post',
        data: {"channelId": channelId, "groupCode": groupCode},
        success: function (data) {
          $("#tab-prd").load("${ctx}/oc/channel/product/" + channelId + "/" + groupCode);
          toastr.success("全部禁用成功", "设置成功");
        }
      });
    });

    $("#btn_add_group").on('click', function (e) {
      var channelId = $('#id').val();
      if (channelId == '') {
        toastr.error("请先保存基本信息", "无法添加");
        return;
      }
      var elem = document.getElementById('dlgCrtGrp');
      var d = dialog({
        "content": elem,
        "title": "添加产品组",
        "zIndex": 2000,
        "onshow": function () {
          $(elem).load($ctx + '/oc/flowpackage/grp-dlg');
        },
        button: [
          {
            value: '确认添加',
            callback: function () {
              var idMethod = $("input[name='n_id_method']:checked").val();
              var telco = $("#n_telco").val();
              var state = $("#n_state").val();
              var discount = $("#n_discount").val();
              var prior = $("#n_prior").val();
              var roamableVal = $('#roamable').is(':checked');
              if (discount == '' || prior == '') {
                toastr.error("请输入完整的折扣数据和优先级数据", "输入不全");
                return false;
              }
              if (!confirm("当前选择的漫游范围是:" + (roamableVal ? "全国漫游 ?" : "省内流量 ?"))) {
                return false;
              }
              $.ajax({
                type: "post",
                url: "${ctx}/oc/flowpackage/grp-create",
                data: {
                  "channelId": channelId,
                  "idMethod": idMethod,
                  "telco": telco,
                  "roamable": roamableVal,
                  "state": state,
                  "discount": discount,
                  "prior": prior
                },
                success: function (data) {
                  if (data.status == "success") {
                    location.reload();
                  } else {
                    toastr.error(data.message, "添加失败");
                  }
                }
              });

              return false;
            },
            autofocus: true
          },
          {
            value: '关闭'
          }
        ]
      });
      d.showModal();
    });
  });
</script>
</body>
</html>
