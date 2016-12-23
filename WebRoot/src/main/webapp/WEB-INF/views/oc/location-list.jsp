<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/artDialog/ui-dialog.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav-admin.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-phone" sysname="${sysname}" title="号段信息"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <form class="form-inline">
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">号段</div>
              <input type="text" class="form-control" id="filter_EQ_sectionNo" placeholder="支持13*风格通配符">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">地区名称</div>
              <input type="text" class="form-control" id="filter_LIKE_area" placeholder="地区名称">
            </div>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="search" class="btn btn-primary">查询</button>
            <button type="button" id="reset" class="btn btn-primary">全部</button>
            <button type="button" id="new" class="btn btn-info">增加号段</button>
          </div>
        </form>
      </div>

      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th>号段</th>
              <th>地区名</th>
              <th>地区代码</th>
              <th>号段类型</th>
              <th style="width:80px;">操作</th>
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
<form id="frm_location" method="post" action="${ctx}/oc/location/update" class="form-horizontal" style="height:260px;width:370px;display:none;"></form>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-min.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-plus-min.js"></script>
<script src="${cdn}/js/plugins/form/jquery.form.js"></script>
<script src="${cdn}/js/plugins/form-validator/jquery.form-validator.min.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("mc-dic-location");
    $.fn.dataTable.ext.legacy.ajax = true;
    var dt = $('#dt1').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "ajax": {
        "url": "${ctx}/oc/location/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_sectionNo": $("#filter_EQ_sectionNo").val(),
            "filter_LIKE_area": $("#filter_LIKE_area").val()
          });
        }
      },
      "columns": [
        {"data": "sectionNo"},
        {"data": "area", "orderable": false},
        {"data": "areaCode", "orderable": false},
        {"data": "mobileType", "orderable": false},
        {"data": null, sortable: false}
      ],
      "order": [0, "asc"],
      "filter": false,
      "columnDefs": [
        {
          "render": function (data, type, row) {
            var btn =
              '<div class="btn-group">' +
              '<button class="btn-white btn btn-xs edit" data-lid="' + row.sectionNo + '"> 编辑</button>' +
              '<button class="btn-white btn btn-xs del" data-lid="' + row.sectionNo + '"> 删除</button>' +
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

    var editLocation = function (id) {
      var elem = document.getElementById('frm_location');
      var d = dialog({
        "id": 'dialog-location',
        "content": elem,
        "title": "号段信息",
        "zIndex": 2000,
        "onshow": function () {
          $(elem).load($ctx + '/oc/location/update/' + id);
        },
        button: [
          {
            value: '保存',
            callback: function () {
              $("#frm_location").submit();
              return false;
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

    var newLocation = function () {
      var elem = document.getElementById('frm_location');
      var d = dialog({
        "id": 'dialog-location',
        "content": elem,
        "title": "号段信息",
        "zIndex": 2000,
        "onshow": function () {
          $(elem).load($ctx + '/oc/location/new');
        },
        button: [
          {
            value: '保存',
            callback: function () {
              $("#frm_location").submit();
              return false;
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

    $("#search").on('click', function () {
      dt.draw();
    });
    $("#reset").on('click', function () {
      $("input[id^='filter_']").val("");
      dt.draw();
    });
    $("#new").on('click', function () {
      newLocation();
    });
    $(document).on('click', '.edit', function () {
      editLocation($(this).data("lid"));
    });

    $(document).on('input', '#sectionNo', function () {
      var sectionNo = $("#sectionNo").val();
      if (sectionNo.length == 7) {
        $.ajax({
          type: "get",
          url: $ctx + "/oc/location/" + sectionNo,
          success: function (data, status) {
            if (data != null) {
              $("#area").val(data.area);
              $("#areaCode").val(data.areaCode);
              $("#mobileType").val(data.mobileType);
              $("#postcode").val(data.postcode);
            }
          }
        });
      }
    });

    $(document).on('click', '.del', function () {
      var id = $(this).data("lid");
      if (!confirm("您确认要删除号段" + id + "吗? 今后该号段将无法识别出省份地区!")) {
        return;
      }
      $.ajax({
        type: "post",
        url: "${ctx}/oc/location/remove",
        data: {"lid": id},
        success: function (data) {
          if (data.status == "success") {
            dt.draw(false);
          } else {
            alert(data.message);
          }
        }
      });
    });

    $.validate({
      lang: "zh",
      onSuccess: function (form) {
        $(form).ajaxSubmit({
          dataType: "json",
          success: function (data) {
            $('#sectionNo').attr("readonly", "readonly");
            $("#msg").html("成功保存" + data.sectionNo).show().delay(3000).hide(300);
            dt.draw();
          },
          error: function () {
            $("#msg").html("<span style='color:red'>保存失败</span>").show().delay(3000).hide(300);
          }
        });
        return false;
      }
    });
  });
</script>
</body>
</html>
