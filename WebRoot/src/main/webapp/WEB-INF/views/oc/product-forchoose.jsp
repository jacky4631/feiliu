<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>

<div class="row">
  <div class="col-xs-12">
    <div class="box-body">
      <form id="chooseForm" class="form-inline">
        <div class="form-group">
          <select name="filter_EQ_provider" id="dlg_EQ_provider" class="form-control input-sm">
            <option value="">所有运营商</option>
            <option value="CMCC">中国移动</option>
            <option value="TELECOM">中国电信</option>
            <option value="UNICOM">中国联通</option>
          </select>
        </div>
        <div class="form-group">
          <select name="filter_EQ_scope" id="dlg_EQ_scope" class="form-control input-sm">
            <option value="">全部地区</option>
            <c:forEach var="state" items="${states}">
              <option value="${state.code}">${state.name}</option>
            </c:forEach>
          </select>
        </div>
        <div class="btn-group" role="group">
          <button type="button" id="search_dlg" class="btn btn-sm btn-primary">查询</button>
          <button type="button" id="reset_dlg" class="btn btn-sm btn-primary">全部</button>
        </div>
        <div class="btn-group" role="group">
          <button type="button" class="btn btn-sm btn-warning btn-auth" data-roamable="true"><i class="fa fa-sun-o"></i> 授权漫游版</button>
          <button type="button" class="btn btn-sm btn-info btn-auth" data-roamable="false"><i class="fa fa-certificate"></i> 授权不漫游版</button>
        </div>
        <!--
        <button type="button" id="close" onclick="dlg.close().remove();" class="btn btn-sm btn-primary">关闭</button>
        -->
      </form>
    </div>

    <div class="box-body">
      <table id="dt_choose" class="table table-bordered table-striped">
        <thead>
        <tr>
          <th style="width:28px;padding-right: 0;"><input id='checkall' type='checkbox' value='true'/></th>
          <th>产品ID</th>
          <th>产品名称</th>
          <th>运营商</th>
          <th>包</th>
          <th>范围</th>
          <th>标价</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script type="text/javascript">
  $(function () {
    $.fn.dataTable.ext.legacy.ajax = true;

    var dt_choose = $('#dt_choose').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "dom": "<f<t>p>",
      "ajax": {
        "url": "${ctx}/oc/product/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_id": $("#filter_EQ_id").val(),
            "filter_EQ_scope": $("#dlg_EQ_scope").val(),
            "filter_EQ_provider": $("#dlg_EQ_provider").val()
          });
        }
      },
      "order": [1, "asc"],
      "columns": [
        {"data": "id", orderable: false},
        {"data": "id"},
        {"data": "name"},
        {"data": "provider"},
        {"data": "size"},
        {"data": "scope"},
        {"data": "price"}
      ],
      "filter": false,
      "columnDefs": [
        {
          "render": function (data, type, row) {
            return "<input name='ids' type='checkbox' value='" + data + "'>";
          },
          "targets": 0
        }
      ],
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });

    $('#dt_choose').on('draw.dt', function () {
      $("#checkall").removeAttr("checked");
    });
    $("#search_dlg").on('click', function () {
      dt_choose.draw();
    });
    $("#reset_dlg").on('click', function () {
      $("#chooseForm input[id^='filter_']").val("");
      $("#dlg_EQ_provider").find("option:first").prop("selected", 'selected');
      $("#dlg_EQ_scope").find("option:first").prop("selected", 'selected');
      dt_choose.draw();
    });
    $('#checkall').click(function () {
      var items = document.getElementsByName('ids');
      for (var i = 0; i < items.length; i++) {
        items[i].checked = this.checked;
      }
    });
    $(".btn-auth").on('click', function () {
      var roamable = $(this).data("roamable");
      var ids = [];
      $("input[name=ids]:checked").each(function () {
        ids.push($(this).val());
      });
      if (ids.length == 0) {
        dialog({title: '提示', content: '没有选择产品, 请选择了产品后, 再进行授权'}).show();
        return;
      }
      var username = $("#username").val();
      $.ajax({
        type: "post",
        traditional: true,
        url: "${ctx}/oc/users/product/auth-products",
        data: {"ids": ids, "username": username, "roamable": roamable},
        success: function (data) {
          if (data.status == "success") {
            toastr.success(data.message, '授权成功');
            dt.draw(false);
          } else {
            toastr.error(data.message, '授权失败');
          }
        }
      });
    });
  });
</script>
