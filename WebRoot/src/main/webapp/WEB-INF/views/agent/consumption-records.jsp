<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/datepicker/datepicker3.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/agent/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/agent/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/agent/banner.jsp" %>
    <tags:content_header icon="fa-table" sysname="${sysname}" title="消费记录"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <form method="post" action="${ctx}/agent/export-bill">
          <div class="row">
            <div class="col-sm-2 col-first">
              <div class="input-group">
                <label class="control-label" for="filter_EQ_mobile">充值号码</label>
                <input type="text" name="filter_EQ_mobile" class="form-control input-sm" id="filter_EQ_mobile" placeholder="充值号码">
              </div>
            </div>
            <div class="col-sm-2">
              <div class="input-group">
                <label class="control-label" for="filter_EQ_id">交易ID</label>
                <input type="text" name="filter_EQ_id" class="form-control input-sm" id="filter_EQ_id" placeholder="交易ID">
              </div>
            </div>
            <div class="col-sm-2">
              <div class="input-group" style="width:95%;">
                <label class="control-label" for="filter_EQ_result">交易状态</label>
                <select name="filter_EQ_result" id="filter_EQ_result" class="form-control input-sm">
                  <option value="">所有</option>
                  <option value="9">执行中</option>
                  <option value="0">成功</option>
                  <option value="-1">失败</option>
                </select>
              </div>
            </div>
            <div class="col-sm-2 col-first">
              <div class="input-group" style="width:95%;">
                <label class="control-label" for="filter_EQ_provider">运营商</label>
                <select name="filter_EQ_provider" id="filter_EQ_provider" class="form-control input-sm">
                  <option value="">所有</option>
                  <option value="CMCC">中国移动</option>
                  <option value="TELECOM">中国电信</option>
                  <option value="UNICOM">中国联通</option>
                </select>
              </div>
            </div>
            <div class="col-sm-2">
              <div class="input-group" style="width:95%;">
                <label class="control-label" for="filter_EQ_state">地区</label>
                <select name="filter_EQ_state" id="filter_EQ_state" class="form-control input-sm">
                  <option value="">所有</option>
                  <c:forEach var="state" items="${states}">
                    <option value="${state.code}">${state.name}</option>
                  </c:forEach>
                </select>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-sm-2 col-first">
              <div class="input-group">
                <label class="control-label" for="filter_GED_startDate">提交开始</label>
                <input type="text" name="filter_GED_startDate" class="form-control input-sm" id="filter_GED_startDate" placeholder="点击选择...">
              </div>
            </div>
            <div class="col-sm-2">
              <div class="input-group">
                <label class="control-label" for="filter_LED_startDate">提交结束</label>
                <input type="text" name="filter_LED_startDate" class="form-control input-sm" id="filter_LED_startDate" placeholder="点击选择...">
              </div>
            </div>

            <div class="col-sm-8" style="padding-top:24px;">
              <div class="btn-group" role="group">
                <button type="button" class="btn btn-info btn-sm month-help" data-month="2">上上月</button>
                <button type="button" class="btn btn-info btn-sm month-help" data-month="1">上月</button>
                <button type="button" class="btn btn-info btn-sm month-help" data-month="0">本月</button>
                <button type="button" id="search" class="btn btn-primary btn-sm">查询</button>
                <button type="button" id="reset" class="btn btn-primary btn-sm">重置</button>
              </div>
              <div class="btn-group" role="group">
                <button class="btn btn-white btn-sm" type="submit">根据查询导出 <i class="fa fa-sign-in"></i></button>
              </div>
            </div>
          </div>
        </form>
      </div>

      <div class="row">
        <div class="col-lg-12">
          <div class="ibox">
            <div class="ibox-content">
              <table id="tab-history" class="table table-striped table-hover">
                <thead>
                <tr>
                  <th>订单号</th>
                  <th>手机号</th>
                  <th>状态</th>
                  <th>产品</th>
                  <th>单价</th>
                  <th>扣费</th>
                  <th>开始</th>
                  <th>完成</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/agent/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/http-script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="${cdn}/js/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script>
  $(function () {
    $("#m-list").addClass("active");
    $.fn.dataTable.ext.legacy.ajax = true;
    var dt = $('#tab-history').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "ajax": {
        "url": $ctx + "/agent/consumption-records/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_mobile": $("#filter_EQ_mobile").val(),
            "filter_EQ_id": $("#filter_EQ_id").val(),
            "filter_EQ_result": $("#filter_EQ_result").val(),
            "filter_EQ_provider": $("#filter_EQ_provider").val(),
            "filter_GED_startDate": $("#filter_GED_startDate").val(),
            "filter_LED_startDate": $("#filter_LED_startDate").val(),
            "filter_EQ_state": $("#filter_EQ_state").val()
          });
        }
      },
      "lengthMenu": [[15, 30, 50, 100], [15, 30, 50, 100]],
      "columns": [
        {"data": "id", sortable: false},
        {"data": "mobile", sortable: false},
        {"data": "result"},
        {"data": "productName", sortable: false},
        {"data": "price"},
        {"data": "billAmount", "sortable": false},
        {"data": "startDate"},
        {"data": "finishDate"}
      ],
      "order": [6, "desc"],
      "filter": false,
      "columnDefs": [
        {
          "render": function (data, type, row) {
            return data + " " + row.mobileInfo;
          },
          "targets": 1
        },
        {
          "render": function (data, type, row) {
            var dis;
            if (data == 0) {
              dis = "<span class='label label-primary'>成功</span>"
            } else if (data == -1) {
              dis = "<span class='label label-danger' data-toggle='tooltip' data-placement='bottom' title='" + row.message + "'>失败</span>";
            } else {
              dis = "<span class='label label-warning'>充值中</span>"
            }
            return dis;
          },
          "targets": 2
        },
        {
          "render": function (data, type, row) {
            if (data == null)
              return row.size + "MB";
            else
              return data;
          },
          "targets": 3
        },
        {targets: [0], className: 'dt-body-courier'}
      ],
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });

    $("#search").on('click', function () {
      dt.draw();
    });
    $(".month-help").on('click', function () {
      var month = $(this).data("month");
      var url = "${ctx}/agent/month/" + month;
      $.ajax({
        type: "get",
        url: url,
        success: function (data) {
          $("#filter_GED_startDate").val(data[0]);
          $("#filter_LED_startDate").val(data[1]);
        }
      });
    });
    $("#reset").on('click', function () {
      $("input[id^='filter_']").val("");
      $("#filter_EQ_result option:first").prop("selected", 'selected');
      $("#filter_EQ_provider option:first").prop("selected", 'selected');
      $("#filter_EQ_state option:first").prop("selected", 'selected');
      dt.draw();
    });
    $('#filter_GED_startDate, #filter_LED_startDate').datepicker({
      todayBtn: "linked",
      keyboardNavigation: false,
      forceParse: false,
      autoclose: true,
      language: "zh-CN",
      format: "yyyy-mm-dd"
    });
  });
</script>
</body>
</html>
