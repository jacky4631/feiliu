<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-bullhorn" sysname="${sysname}" title="通知广播"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <c:if test="${not empty message}">
          <div id="message" class="alert alert-success">
            <button data-dismiss="alert" class="close">×</button>
              ${message}</div>
        </c:if>
        <div class="box-body form-inline">
          <div class="btn-group" role="group">
            <a id="btn_new" class="btn btn-info" href="${ctx}/oc/bulletin/create">新建公告</a>
            <button type="button" id="btn_del" class="btn btn-info">删除公告</button>
          </div>
        </div>
      </div>

      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th style="width: 20px;padding-right: 2px;">选</th>
              <th style="width: 150px;">公告时间</th>
              <th>公告标题</th>
              <th>公告正文</th>
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

<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("ma-tools-bulletin");
    $.fn.dataTable.ext.legacy.ajax = true;
    var dt = $('#dt1').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "stateSave": true,
      "ajax": {
        "url": "${ctx}/oc/bulletin/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {});
        }
      },
      "order": [1, "asc"],
      "columns": [
        {"data": "id", sortable: false},
        {"data": "created"},
        {"data": "title", sortable: false},
        {"data": "content", sortable: false}
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

    $("#message").delay(2000).hide(300);

    function selectedIds() {
      var ids = [];
      $("input[name=ids]:checked").each(function () {
        ids.push($(this).val());
      });
      return ids;
    }

    $('#btn_del').on('click', function () {
      var ids = selectedIds();
      if (ids.length == 0) {
        alert("没有选择要删除的公告");
        return;
      }
      if (confirm("确定要删除这些公告吗?")) {
        $.ajax({
          url: $ctx + "/oc/bulletin/batch-del",
          traditional: true,
          type: 'post',
          data: {"ids": ids},
          success: function (data) {
            dt.draw(false);
          }
        });
      }
    });
  })
</script>
</body>
</html>
