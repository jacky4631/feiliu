<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/artDialog/ui-dialog.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/chosen/chosen.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-cubes" sysname="${sysname}" title="基础产品库"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <form class="form-inline">
          <div class="form-group">
            <label for="filter_EQ_id">产品ID</label>
            <input class="form-control input-sm" type="text" class="form-control" id="filter_EQ_id" placeholder="产品ID">
          </div>
          <div class="form-group">
            <label for="filter_LIKE_name">产品名称</label>
            <input class="form-control input-sm" type="text" class="form-control" id="filter_LIKE_name" placeholder="产品名称">
          </div>
          <div class="form-group">
            <select name="filter_EQ_scope" id="filter_EQ_scope" class="form-control input-sm chosen">
              <option value="">全部地区</option>
              <c:forEach var="state" items="${states}">
                <option value="${state.code}">${state.name}</option>
              </c:forEach>
            </select>
          </div>
          <div class="form-group">
            <select name="filter_EQ_provider" id="filter_EQ_provider" class="form-control input-sm">
              <option value="">所有运营商</option>
              <option value="CMCC">中国移动</option>
              <option value="TELECOM">中国电信</option>
              <option value="UNICOM">中国联通</option>
            </select>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="search" class="btn btn-primary">查询</button>
            <button type="button" id="reset" class="btn btn-primary">全部</button>
          </div>
          <button type="button" id="btn-new" class="btn btn-info">增加产品</button>
          <button type="button" id="btn-copy" class="btn btn-info">克隆产品组</button>
        </form>
      </div>
      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th>产品ID</th>
              <th>产品名称</th>
              <th>产品简称</th>
              <th>运营商</th>
              <th>包</th>
              <th>范围</th>
              <th>标价</th>
              <th>状态</th>
              <th style="width:175px;">操作</th>
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
<form id="frm_product" method="post" action="${ctx}/oc/product/create" class="form-horizontal" style="height:245px;width:430px;display:none;"></form>
<div id="dlgPackage" style="height:350px;width:600px;display:none;"></div>
<div id="dlgCopy" style="height:100px;width:290px;display:none;"></div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="${cdn}/js/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-min.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-plus-min.js"></script>
<script src="${cdn}/js/plugins/form/jquery.form.js"></script>
<script src="${cdn}/js/plugins/form-validator/jquery.form-validator.min.js"></script>
<script src="${cdn}/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("ma-config-product");
    $(".chosen").chosen();

    $.fn.dataTable.ext.legacy.ajax = true;
    var dt = $('#dt1').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "stateSave": true,
      "responsive": true,
      "ajax": {
        "url": "${ctx}/oc/product/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_id": $("#filter_EQ_id").val(),
            "filter_EQ_scope": $("#filter_EQ_scope").val(),
            "filter_EQ_provider": $("#filter_EQ_provider").val(),
            "filter_LIKE_name": $("#filter_LIKE_name").val()
          });
        }
      },
      "columns": [
        {"data": "id"},
        {"data": "name"},
        {"data": "shortName"},
        {"data": "provider"},
        {"data": "size"},
        {"data": "scope"},
        {"data": "price"},
        {"data": "enabled", sortable: false},
        {"data": null, sortable: false}
      ],
      "filter": false,
      "columnDefs": [
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
              '<button class="btn-white btn btn-xs chg" data-prdid="' + row.id + '">开关</button>' +
              '<button class="btn-white btn btn-xs prod" data-prdid="' + row.id + '">可用包</button>' +
              '<button class="btn-white btn btn-xs sale" data-prdid="' + row.id + '">已授权</button>' +
              '<button title="编辑" class="btn-white btn btn-xs edit" data-prdid="' + row.id + '"><i class="fa fa-edit"></i></button>' +
              '<button title="删除" class="btn-white btn btn-xs del" data-prdid="' + row.id + '"><i class="fa fa-remove"></i> </button>' +
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

    var copyProducts = function () {
      var elem = document.getElementById('dlgCopy');
      var d = dialog({
        "id": 'dialog-copy',
        "content": elem,
        "title": "克隆产品组",
        "zIndex": 2000,
        "onshow": function () {
          $(elem).load($ctx + '/oc/product/clonedlg');
        },
        button: [
          {
            value: '执行',
            callback: function () {
              var provider = $("#provider").val();
              var state = $("#state").val();
              if (state == '') {
                alert("请选择目标省份");
                return false;
              }
              $.ajax({
                type: "post",
                traditional: true,
                url: "${ctx}/oc/product/clone",
                data: {"state": state, "provider": provider},
                success: function (data) {
                  if (data.status == "success") {
                    dt.draw(false);
                  } else {
                    alert(data.message);
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

    var editProduct = function (id) {
      var elem = document.getElementById('frm_product');
      var d = dialog({
        "id": 'dialog-product',
        "content": elem,
        "title": "产品信息",
        "zIndex": 2000,
        "onshow": function () {
          if (typeof(id) == "undefined" || id == null || id == "") {
            $(elem).load($ctx + '/oc/product/create');
          } else {
            $(elem).load($ctx + '/oc/product/update/' + id);
          }
        },
        button: [
          {
            value: '保存',
            callback: function () {
              $("#flag").val("");
              $("#frm_product").submit();
              return false;
            },
            autofocus: true
          },
          {
            value: '保存并增加',
            callback: function () {
              $("#flag").val("addnew");
              $("#frm_product").submit();
              return false;
            }
          },
          {
            value: '关闭'
          }
        ],
        statusbar: '<label id="msg"></label>'
      });
      d.show();
    };

    $("#search").on('click', function () {
      dt.draw();
    });
    $("#reset").on('click', function () {
      $("input[id^='filter_']").val("");
      $("#filter_EQ_provider").find("option:first").prop("selected", 'selected');
      $("#filter_EQ_scope").find("option:first").prop("selected", 'selected');
      dt.draw();
    });

    $("#btn-new").on('click', function () {
      editProduct();
    });
    $("#btn-copy").on('click', function () {
      copyProducts();
    });
    $(document).on('click', '.edit', function () {
      editProduct($(this).data("prdid"));
    });
    $(document).on('click', '.del', function () {
      var id = $(this).data("prdid");
      if (!confirm("您确认要删除产品" + id + "吗?")) {
        return;
      }
      $.ajax({
        type: "post",
        traditional: true,
        url: "${ctx}/oc/product/remove",
        data: {"prdId": id},
        success: function (data) {
          if (data.status == "success") {
            dt.draw(false);
          } else {
            alert(data.message);
          }
        }
      });
    });

    $(document).on('click', '.chg', function () {
      var id = $(this).data("prdid");
      $.ajax({
        type: "post",
        url: "${ctx}/oc/product/chgstatus",
        data: {"prdId": id},
        success: function (data) {
          if (data.status == "success") {
            dt.draw(false);
          }
        }
      });
    });

    $(document).on('click', '.prod', function () {
      var elem = document.getElementById('dlgPackage');
      var id = $(this).data("prdid");
      var d = dialog({
        "content": elem,
        "title": "挂载产品信息-通道绿色表示开启",
        "zIndex": 2000,
        "onshow": function () {
          $(elem).load($ctx + '/oc/product/package/' + id);
        }
      });
      d.showModal();
    });

    $(document).on('click', '.sale', function () {
      var elem = document.getElementById('dlgPackage');
      var id = $(this).data("prdid");
      var d = dialog({
        "content": elem,
        "title": id + "已授权用户",
        "zIndex": 2000,
        "onshow": function () {
          $(elem).load($ctx + '/oc/product/sale/' + id);
        }
      });
      d.showModal();
    });

    $.validate({
      lang: "zh",
      onSuccess: function (form) {
        $(form).ajaxSubmit({
          dataType: "json",
          success: function (data) {
            if (data.id != "") {
              $('#id').val(data.id);
              if ($("#flag").val() == "addnew") {
                $("#frm_product").find(".canreset").val("");
                $("#msg").html("成功保存" + data.id + ", 再新增").show().delay(3000).hide(300);
              } else {
                $("#msg").html("成功保存" + data.id).show().delay(3000).hide(300);
              }
              dt.draw();
            }
          },
          error: function () {
            $("#msg").html("<span style='color:red'>保存失败, 修改时不可变更产品规格</span>").show().delay(3000).hide(300);
          }
        });
        return false;
      }
    });
  })
</script>
</body>
</html>
