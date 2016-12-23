<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/datepicker/datepicker3.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/artDialog/ui-dialog.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/chosen/chosen.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
  <link href="${ctx}/static/css/loglist.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-pause" sysname="${sysname}" title="订单黑洞"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <form id="frm_filter" method="post" action="${ctx}/oc/tradelog/export">
          <input type="hidden" name="filter_EQ_type" id="EQ_type" value="0">

          <div class="row">
            <div class="col-sm-2 col-first">
              <div class="input-group">
                <label class="control-label" for="filter_EQ_username">账号</label>
                <input type="text" name="filter_EQ_username" id="filter_EQ_username" class="form-control input-sm" placeholder="账号">
              </div>
            </div>
            <div class="col-sm-2">
              <div class="input-group">
                <label class="control-label" for="filter_EQ_mobile">手机号</label>
                <input type="text" class="form-control input-sm" name="filter_EQ_mobile" id="filter_EQ_mobile" placeholder="输入手机号">
              </div>
            </div>
            <div class="col-sm-2">
              <div class="input-group">
                <label class="control-label" for="filter_EQ_id">交易编号</label>
                <input type="text" class="form-control input-sm" name="filter_EQ_id" id="filter_EQ_id" placeholder="输入编号">
              </div>
            </div>
            <div class="col-sm-2">
              <div class="input-group">
                <label class="control-label" for="filter_EQ_productId">产品ID</label>
                <input type="text" class="form-control input-sm" name="filter_EQ_productId" id="filter_EQ_productId" placeholder="产品ID">
              </div>
            </div>
            <div class="col-sm-2">
              <div class="input-group">
                <label class="control-label" for="filter_EQ_state">地区</label>

                <div>
                  <select name="filter_EQ_state" id="filter_EQ_state" class="form-control input-sm chosen">
                    <option value="">全部</option>
                    <c:forEach var="state" items="${states}">
                      <option value="${state.code}">${state.name}</option>
                    </c:forEach>
                  </select>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-sm-2 col-first">
              <div class="input-group">
                <label class="control-label" for="filter_GED_startDate">提交开始</label>
                <input type="text" class="form-control input-sm" name="filter_GED_startDate" id="filter_GED_startDate" placeholder="点击选择...">
              </div>
            </div>
            <div class="col-sm-2">
              <div class="input-group">
                <label class="control-label" for="filter_LED_startDate">提交结束</label>
                <input type="text" class="form-control input-sm" name="filter_LED_startDate" id="filter_LED_startDate" placeholder="点击选择...">
              </div>
            </div>

            <div class="col-sm-2">
              <div class="input-group">
                <label class="control-label" for="filter_EQ_provider">运营商</label>
                <select name="filter_EQ_provider" id="filter_EQ_provider" class="form-control input-sm">
                  <option value="">全部</option>
                  <option value="CMCC">中国移动</option>
                  <option value="TELECOM">中国电信</option>
                  <option value="UNICOM">中国联通</option>
                </select>
              </div>
            </div>

            <div class="col-sm-4" style="padding-top:24px;">
              <div class="btn-group" role="group">
                <button type="button" id="search" class="btn btn-primary btn-sm">查询</button>
                <button type="button" id="reset" class="btn btn-primary btn-sm">全部</button>
                <button type="button" id="export" class="btn btn-primary btn-sm">导出</button>
                <button type="button" class="btn btn-info btn-sm" id="btn_auto">执行</button>
                <button type="button" class="btn btn-info btn-sm" id="btn_route">选送...</button>

                <div class="btn-group">
                  <button class="btn btn-sm btn-info" type="button">高级</button>
                  <button data-toggle="dropdown" class="btn btn-sm btn-info dropdown-toggle" type="button">
                    <span class="caret"></span>
                    <span class="sr-only">Toggle Dropdown</span>
                  </button>
                  <ul role="menu" class="dropdown-menu">
                    <li><a id="btn_force_suc" href="#"><i class="fa fa-hand-peace-o"></i>回调为成功</a></li>
                    <li><a id="btn_force_fail" href="#"><i class="fa fa-hand-paper-o"></i>回调为失败</a></li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>

      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th style="width:18px;padding-right:6px;"><input id='checkall' type='checkbox' value='true'/></th>
              <th>流水号</th>
              <th>账户</th>
              <th>手机号</th>
              <th>状态</th>
              <th>流量包</th>
              <th>提交时间</th>
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
<div id="dlg" style="height:600px;width:670px;display:none;"></div>
<div id="dlg-sel" style="height:100px;width:400px;display:none;"></div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="${cdn}/js/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-min.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-plus-min.js"></script>
<script src="${cdn}/js/plugins/chosen/chosen.jquery.js"></script>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script src="${ctx}/static/js/oc/tradelog-list.js"></script>
<script>
  $(function () {
    activeMenu("ma-tradelog0");
    dt = $('#dt1').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "ajax": {
        "url": $ctx + "/oc/tradelog/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_username": $("#filter_EQ_username").val(),
            "filter_EQ_mobile": $("#filter_EQ_mobile").val(),
            "filter_EQ_id": $("#filter_EQ_id").val(),
            "filter_EQ_result": $("#filter_EQ_result").val(),
            "filter_EQ_provider": $("#filter_EQ_provider").val(),
            "filter_EQ_productId": $("#filter_EQ_productId").val(),
            "filter_EQ_state": $("#filter_EQ_state").val(),
            "filter_GED_startDate": $("#filter_GED_startDate").val(),
            "filter_LED_startDate": $("#filter_LED_startDate").val(),
            "filter_EQ_type": $("#EQ_type").val()
          });
        }
      },
      "columns": [
        {"data": "id", "orderable": false},
        {"data": "id", orderable: false},
        {"data": "username"},
        {"data": "mobile", orderable: false},
        {"data": "result"},
        {"data": "productName", orderable: false},
        {"data": "startDate"}
      ],
      "order": [6, "desc"],
      "filter": false,
      "columnDefs": [
        {
          "render": function (data, type, row) {
            return "<input name='ids' type='checkbox' value='" + data + "'>";
          },
          "targets": 0
        },
        {
          "render": function (data, type, row) {
            var cnt = data;
            if (row.channelFinishDate == null) {
              cnt = "<span style='font-weight:bold;'>" + data + "</span>";
            }
            if (row.remark != '') {
              cnt += " <i class='fa fa-comment-o' style='font-size:12px;color:gray;' title='" + row.remark + "'></i>";
            }
            return "<a class='detail' href='#' data-id='" + data + "'>" + cnt + "</a>";
          },
          "targets": 1
        },
        {
          "render": function (data, type, row) {
            var dis;
            if (row.displayUsername == "") {
              dis = data;
            } else {
              dis = row.displayUsername + "(" + data + ")";
            }
            return dis;
          },
          "targets": 2
        },
        {
          "render": function (data, type, row) {
            return data + "<small class='label'>" + row.mobileInfo + "</small>"
          },
          "targets": 3
        },
        {
          "render": function (data, type, row) {
            return convertRestult(data, row);
          },
          "targets": 4
        },
        {
          "render": function (data, type, row) {
            if (data == null) {
              return row.size + "MB";
            } else {
              return data;
            }
          },
          "targets": 5
        }
      ],
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });

  });
</script>
</body>
</html>
